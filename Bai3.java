import java.util.Scanner;

class Point {
    int x, y;

    Point(int _x, int _y) {
        this.x = _x;
        this.y = _y;
    }

    int dist(Point other) {
        return (x - other.x) * (x - other.x) + (y - other.y) * (y - other.y);
    }
}

class Vector {
    int x, y;

    Vector(int _x, int _y) {
        this.x = _x;
        this.y = _y;
    }

    int crossProduct(Vector other) {
        return this.x * other.y - this.y * other.x;
    }
}

public class Bai3 {
    public static Vector subtract(Point a, Point b) {
        return new Vector(a.x - b.x, a.y - b.y);
    }

    public static int orientation(Point a, Point b, Point c) {
        Vector x = subtract(b, a);
        Vector y = subtract(c, b);
        int orient = x.crossProduct(y);

        if (orient < 0) return -1; 
        else if (orient == 0) return 0; 
        return 1;
    }

    public static void swap(Point[] p, int i, int j) {
        Point temp = p[i];
        p[i] = p[j];
        p[j] = temp;
    }

    public static void sortPoints(Point[] points, int n, Point p0) {
        for (int i = 1; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int orient = orientation(p0, points[i], points[j]);

                if (orient < 0 || (orient == 0 && p0.dist(points[i]) > p0.dist(points[j]))) {
                    swap(points, i, j);
                }
            }
        }
    }

    public static Point[] convexHull(Point[] points, int n) {
        if (n < 3) return points;

        int minIndex = 0;
        for (int i = 1; i < n; i++) {
            if (points[i].y < points[minIndex].y || (points[i].y == points[minIndex].y && points[i].x < points[minIndex].x)) {
                minIndex = i;
            }
        }

        swap(points, 0, minIndex);
        Point p0 = points[0];

        sortPoints(points, n, p0);

        Point[] hull = new Point[n];
        int hullSize = 0;

        for (int i = 0; i < n; i++) {
            while (hullSize >= 2 && orientation(hull[hullSize - 2], hull[hullSize - 1], points[i]) <= 0) {
                hullSize--;
            }
            hull[hullSize++] = points[i];
        }

        for (int i = 0; i < hullSize / 2; i++) {
            swap(hull, i, hullSize - 1 - i);
        }

        Point[] result = new Point[hullSize];
        for (int i = 0; i < hullSize; i++) {
            result[i] = hull[i];
        }

        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            points[i] = new Point(x, y);
        }

        Point[] hull = convexHull(points, n);

        for (Point p : hull) {
            System.out.println(p.x + " " + p.y);
        }
        scanner.close();
    }
}
