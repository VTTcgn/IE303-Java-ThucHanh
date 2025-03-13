import java.util.*;

public class Bai1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Nhập vào bán kính r = ");
        double r = sc.nextDouble();
        int N = 2000000;
        int M = 0;

        Random random = new Random();

        for (int i=0; i<N; i++) {
            double x = 2*r*random.nextDouble() - r;
            double y = 2*r*random.nextDouble() - r;

            if ((x*x + y*y) <= r*r) M++;
        }
        double Dientich = (double) M / N * 4 * r * r;
        System.out.println("Diện tích hình trong xấp xỉ là: " + Dientich);
        sc.close();
    }
}