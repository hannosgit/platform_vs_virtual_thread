import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

public class ThreadCreationTime {

    private static final int MEASUREMENTS = 10;
    private static final List<Long> nanos = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        if (args.length < 1) {
            System.out.println("Missing argument for thread count!");
            System.exit(1);
        }

        final Thread.Builder threadBuilder;
        final int threadCount = Integer.parseUnsignedInt(args[0]);
        if (args.length > 1) {
            System.out.println("Using platform threads!");
            threadBuilder = Thread.ofPlatform();
        } else {
            System.out.println("Using virtual threads!");
            threadBuilder = Thread.ofVirtual();
        }
        System.out.println("Creating " + threadCount + " threads for each run!");
        Thread.sleep(Duration.ofSeconds(3));

        System.out.println("Warmup");
        createThreads(threadBuilder, threadCount);

        System.out.println();
        System.out.println("Start measuring");
        for (int i = 0; i < MEASUREMENTS; i++) {
            createAndMeasureThreads(threadBuilder, threadCount);
            Thread.sleep(Duration.ofSeconds(1));
        }


        System.out.println(nanos.stream().flatMapToLong(LongStream::of).summaryStatistics());
    }

    private static void createAndMeasureThreads(Thread.Builder threadBuilder, int count) {
        long startTime = System.nanoTime();
        final List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            threadList.add(threadBuilder.start(ThreadCreationTime::method));
        }
        long elapsedNanos = System.nanoTime() - startTime;
        nanos.add(elapsedNanos);
        final Duration duration = Duration.ofNanos(elapsedNanos);
        System.out.printf("It took %s%n", duration);

        // Cleanup
        threadList.forEach(Thread::interrupt);
        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                // interrupt is expected
            }
        }
        System.out.println("all joined");
    }

    private static void createThreads(Thread.Builder threadBuilder, int count) {
        final List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            threadList.add(threadBuilder.start(ThreadCreationTime::method));
        }

        // Cleanup
        threadList.forEach(Thread::interrupt);
        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                // interrupt is expected
            }
        }
        System.out.println("all joined");
    }

    private static void method() {
        try {
            Thread.sleep(Duration.ofHours(10));
        } catch (InterruptedException e) {
            // interrupt is expected
        }
    }

}
