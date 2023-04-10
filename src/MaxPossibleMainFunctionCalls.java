import java.time.Duration;
import java.util.concurrent.CountDownLatch;

public class MaxPossibleMainFunctionCalls {

    private static final CountDownLatch HOLD = new CountDownLatch(1);

    public static void main(String[] args) throws InterruptedException {
        final Thread.Builder threadBuilder;

        if (args.length > 1) {
            System.out.println("Using platform threads!");
            threadBuilder = Thread.ofPlatform();
        } else {
            System.out.println("Using virtual threads!");
            threadBuilder = Thread.ofVirtual();
        }
        System.out.println("PID: " + ProcessHandle.current().pid());
        System.out.println("Max memory: " + Util.toHumanReadableByNumOfLeadingZeros(Runtime.getRuntime().maxMemory()));
        System.out.println("Total memory: " + Util.toHumanReadableByNumOfLeadingZeros(Runtime.getRuntime().totalMemory()));
        Thread.sleep(Duration.ofSeconds(5));

        final int methodCalls = Integer.parseInt(args[0]);
        System.out.printf("Making %d method calls%n", methodCalls);

        for (int i = 0; i < 100_000_000; i++) {
            System.out.println(i);
            CountDownLatch latch = new CountDownLatch(1);
            threadBuilder.start(() -> method(latch, methodCalls));
            latch.await();
        }
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
