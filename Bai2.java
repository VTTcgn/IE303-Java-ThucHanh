import java.util.*;

public class Bai2 {
    public static void main(String[] args) {
        double r = 1;
        int N = 2000000;
        int M = 0;

        Random random = new Random();

        for (int i=0; i<N; i++) {
            double x = 2*r*random.nextDouble() - r;
            double y = 2*r*random.nextDouble() - r;

            if ((x*x + y*y) <= r*r) M++;
        }
        double pi = (double) M / N * 4;
        System.out.println("Giá trị của số pi xấp xỉ là: " + pi);
    }    
}
