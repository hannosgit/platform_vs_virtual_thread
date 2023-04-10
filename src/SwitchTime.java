import java.math.BigInteger;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SwitchTime {

    private static final int N = 10_000;

    public static long blackHole;

    public static void main(String[] args) throws InterruptedException {
        final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
        //final ExecutorService executorService = Executors.newCachedThreadPool();

        long startTimeTotal = System.nanoTime();
        for (int i = 0; i < N; i++) {
            long startTime = System.nanoTime();
            final int id = i;

            executorService.submit(() -> {
                BigInteger res = BigInteger.ZERO;

                for (int j = 0; j < 1_000_000; j++) {
                    res = res.add(BigInteger.valueOf(1L));
                }

                blackHole = res.longValue();

                long elapsedNanos = System.nanoTime() - startTime;
                System.out.println(id + ";" + Duration.ofNanos(elapsedNanos).toMillis());
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.HOURS);

        long elapsedNanos = System.nanoTime() - startTimeTotal;
        System.out.println("It took " + Duration.ofNanos(elapsedNanos).toMillis());

    }


}
