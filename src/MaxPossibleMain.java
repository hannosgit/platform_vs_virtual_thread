import java.util.concurrent.CountDownLatch;

public class MaxPossibleMain {

    public static void main(String[] args) throws InterruptedException {
        final Thread.Builder threadBuilder;

        if (args.length > 0) {
            System.out.println("Using platform threads!");
            threadBuilder = Thread.ofPlatform();
        } else {
            System.out.println("Using virtual threads!");
            threadBuilder = Thread.ofVirtual();
        }
        Thread.sleep(3_000L);

        final CountDownLatch hold = new CountDownLatch(1);
        for (int i = 0; i < 100_000_000; i++) {
            final CountDownLatch latch = new CountDownLatch(1);
            threadBuilder.start(() -> method(latch, hold));
            latch.await();
            System.out.println(i);
        }

        hold.countDown();
    }

    private static void method(CountDownLatch latch, CountDownLatch hold) {
        try {
            latch.countDown();

            hold.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
