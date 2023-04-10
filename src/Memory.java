import java.time.Duration;
import java.util.concurrent.CountDownLatch;

public class Memory {

    private static final CountDownLatch HOLD = new CountDownLatch(1);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("PID: " + ProcessHandle.current().pid());
        System.out.println("Max Memory: " + Runtime.getRuntime().maxMemory());

        final int nThreads = Integer.parseInt(args[0]);

        final Thread.Builder threadBuilder;
        if (args.length > 2) {
            threadBuilder = Thread.ofPlatform();
        } else {
            threadBuilder = Thread.ofVirtual();
        }
        final int methodCalls = Integer.parseInt(args[1]);

        System.out.printf("Creating %d %s threads with %d method calls!%n", nThreads, args.length > 2 ? "platform" : "virtual", methodCalls);

        for (int i = 0; i < nThreads; i++) {
            final CountDownLatch latch = new CountDownLatch(1);
            threadBuilder.start(() -> method(latch, methodCalls));
            latch.await();
        }
        System.out.println("Finished creating threads!");

        Thread.sleep(Duration.ofHours(10));
    }

    private static void method(CountDownLatch latch, int methodCalls) {
        try {
            recursive(latch, methodCalls);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void recursive(CountDownLatch latch, int methodCalls) throws InterruptedException {
        if (methodCalls > 0) {
            recursive(latch, methodCalls - 1);
        } else {
            latch.countDown();
            HOLD.await();
        }
    }

}
