import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class QueueTest {

    private static final int N_COUNT = 1_0_000;

    private static final int N_THREADS = 7;

    private static long START_NANOS;

    public static void main(String[] args) {
        final List<QueueHolder> holders = new ArrayList<>();
        for (int i = 0; i < N_THREADS; i++) {
            final QueueHolder queueHolder = new QueueHolder();
            queueHolder.setOutgoing(new ArrayBlockingQueue<>(1));
            holders.add(queueHolder);
        }

        for (int i = 0; i < N_THREADS; i++) {
            final int previousIndex = i - 1;
            if (previousIndex < 0) {
                holders.get(i).setIncoming(holders.get(holders.size() - 1).outgoing);
            } else {
                holders.get(i).setIncoming(holders.get(previousIndex).outgoing);
            }
        }

        System.out.println(Runtime.getRuntime().availableProcessors());
        try (final ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS, Thread.ofPlatform().factory())) {

            for (var holder : holders) {
                executorService.submit(holder::work);
            }
            START_NANOS = System.nanoTime();
            holders.get(0).incoming.put(1);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private static class QueueHolder {

        private BlockingQueue<Integer> incoming;

        private BlockingQueue<Integer> outgoing;

        private QueueHolder() {
        }

        public void setIncoming(BlockingQueue<Integer> incoming) {
            this.incoming = incoming;
        }

        public void setOutgoing(BlockingQueue<Integer> outgoing) {
            this.outgoing = outgoing;
        }

        public void work() {
            try {
                final int take = incoming.take();
                if (take == N_COUNT) {
                    long elapsedNanos = System.nanoTime() - START_NANOS;
                    System.out.println("It took " + Duration.ofNanos(elapsedNanos));
                    System.out.println("Finished");
                } else {
                    //final long threadId = Thread.currentThread().threadId();
                    //System.out.printf("%s: Took %d%n", threadId, take);
                    outgoing.put(take + 1);
                    //System.out.printf("%s: Put %d%n", threadId, (take + 1));
                    //}
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        public void workPolling() {
            Integer take = null;
            do {
                take = incoming.peek();
            } while (take == null);

            if (take == N_COUNT) {
                long elapsedNanos = System.nanoTime() - START_NANOS;
                System.out.println("It took " + Duration.ofNanos(elapsedNanos));
                System.out.println("Finished");
            } else {
                try {
                    outgoing.put(take + 1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

}

