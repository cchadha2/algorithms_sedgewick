import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {

    private TreeSet<Point2D> set;
    private int n;

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
        if (p == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }
        if (set.contains(p)) {
            return;
        }
        set.add(p);
        n++;
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }
        return set.contains(p);
    }

    public void draw() {
        for (Point2D point : set) {
            StdDraw.point(point.x(), point.y());
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }
        ArrayList<Point2D> rangeIterable = new ArrayList<Point2D>();
        for (Point2D point : set) {
            if (rect.contains(point)) {
                rangeIterable.add(point);
            }

        }
        return rangeIterable;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }

        Point2D closest = null;
        double closestDist = Double.POSITIVE_INFINITY;

        for (Point2D pt : set) {
            double dist = pt.distanceSquaredTo(p);
            if (dist < closestDist) {
                closest = pt;
                closestDist = dist;
            }
        }

        return closest;
    }

    public static void main(String[] args) {
        Point2D p = new Point2D(1, 1);

        PointSET points = new PointSET();
        StdOut.println(points.size());
        StdOut.println(points.isEmpty());

        points.insert(new Point2D(2, 2));
        points.insert(new Point2D(3, 4));
        points.insert(new Point2D(-1, 2));
        points.insert(new Point2D(-5, 2));
        points.insert(new Point2D(2, -1));
        points.insert(new Point2D(4, 9));
        points.insert(new Point2D(2, 1));
        points.insert(new Point2D(4, -4));
        points.insert(new Point2D(25, 11));
        StdOut.println(points.size());
        StdOut.println(points.contains(new Point2D(2, 1)));
        StdOut.println(points.contains(p));

        StdOut.println(points.nearest(p));
        StdOut.println();
        for (Point2D point : points.range(new RectHV(1, 1, 10, 10))) {
            StdOut.println(point);
        }
    }
}
