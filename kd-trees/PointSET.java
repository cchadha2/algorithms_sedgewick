import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.TreeSet;

public class PointSET {

    TreeSet<Point2D> set;
    int n;

    public PointSET() {
         set = new TreeSet<Point2D>();
         n = 0;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public void insert(Point2D p) {
        set.add(p);
        n++;
    }

    public boolean contains(Point2D p) {
        return set.contains(p);
    }

    public void draw() {
        for (Point2D point : set) {
            StdDraw.point(point.x(), point.y());
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        return set.subSet()
    }

    public static void main(String[] args) {

    }
}
