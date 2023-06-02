package lab8;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
public class MatrixMultiplier {

    public static int[][] multiplyMatrices(int[][] A, int[][] B) {
        ExecutorService executor = Executors.newFixedThreadPool(8);
        int[][] C = new int[A.length][B[0].length];
        int m = A.length;
        executor.execute(() -> {
            for (int i = 0; i < m; i++) {
                final int finalI = i;
//            System.out.println(i);
                int n = A[0].length;
                int p = B[0].length;

                for (int j = 0; j < p; j++) {
                    int sum = 0;
                    for (int k = 0; k < n; k++) {
                        sum += A[finalI][k] * B[k][j];
                    }
                    C[finalI][j] = sum;
                }
            }
        });

        try{
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.DAYS);

        }catch(InterruptedException ex){
            ex.printStackTrace();
        }
        return C;

    }
}