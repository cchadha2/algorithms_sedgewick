/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class KdTree {

    private int n = 0;

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private boolean horizontal;

        public Node(Point2D p) {
            this.p = p;
        }
    }

    private Node root;

    public KdTree() {
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return n;
    }

    private void insert(Node nd, Point2D p) {
        if (!nd.horizontal) {
            // check point to left or right:
            double diff = p.x() - nd.p.x();
            if (diff >= 0) { // right
                if (nd.rt == null) {
                    Node child = new Node(p);
                    child.horizontal = true;
                    child.rect = new RectHV(nd.p.x(), nd.rect.ymin(), nd.rect.xmax(),
                                            nd.rect.ymax());

                    nd.rt = child;
                }
                else {
                    insert(nd.rt, p);
                }
            }
            else // left
            {
                if (nd.lb == null) {
                    Node child = new Node(p);
                    child.horizontal = true;
                    child.rect = new RectHV(nd.rect.xmin(), nd.rect.ymin(), nd.p.x(),
                                            nd.rect.ymax());

                    nd.lb = child;
                }
                else {
                    insert(nd.lb, p);
                }
            }
        }
        else {
            // check point to top or bottom:
            double diff = p.y() - nd.p.y();
            if (diff >= 0) { // top
                if (nd.rt == null) {
                    Node child = new Node(p);
                    child.horizontal = false;
                    child.rect = new RectHV(nd.rect.xmin(), nd.p.y(), nd.rect.xmax(),
                                            nd.rect.ymax());

                    nd.rt = child;
                }
                else {
                    insert(nd.rt, p);
                }
            }
            else // bottom
            {
                if (nd.lb == null) {
                    Node child = new Node(p);
                    child.horizontal = false;
                    child.rect = new RectHV(nd.rect.xmin(), nd.rect.ymin(), nd.rect.xmax(),
                                            nd.p.y());

                    nd.lb = child;
                }
                else {
                    insert(nd.lb, p);
                }
            }
        }
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }

        if (isEmpty()) {
            root = new Node(p);
            root.rect = new RectHV(0, 0, 1, 1);
            root.horizontal = false;
            n++;
        }
        else if (!contains(p)) {
            n++;
            insert(root, p);
        }
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }
        return contains(root, p);
    }

    private boolean contains(Node node, Point2D p) {
        if (node == null) {
            return false;
        }
        else if (node.p.equals(p)) {
            return true;
        }

        double compare;
        if (!node.horizontal) {
            compare = p.x() - node.p.x();
        }
        else {
            compare = p.y() - node.p.y();
        }

        if (compare < 0) {
            return contains(node.lb, p);
        }
        else {
            return contains(node.rt, p);
        }
    }

    public void draw() {
        Stack<Node> stack = new Stack<Node>();
        stack.push(root);

        while (!stack.isEmpty()) {
            Node searchNode = stack.pop();
            StdOut.println(searchNode.p);
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            searchNode.p.draw();

            if (!searchNode.horizontal) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.setPenRadius(0.002);
                StdDraw.line(searchNode.p.x(), searchNode.rect.ymin(),
                             searchNode.p.x(), searchNode.rect.ymax());
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.setPenRadius(0.002);
                StdDraw.line(searchNode.rect.xmin(), searchNode.p.y(),
                             searchNode.rect.xmax(), searchNode.p.y());
            }

            if (searchNode.rt != null) {
                stack.push(searchNode.rt);
            }
            if (searchNode.lb != null) {
                stack.push(searchNode.lb);
            }
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }

        ArrayList<Point2D> rangeIterable = new ArrayList<Point2D>();
        if (root != null) {
            range(root, rangeIterable, rect);
        }

        return rangeIterable;
    }

    private void range(Node node, ArrayList<Point2D> rangeIterable, RectHV rect) {
        if (rect.contains(node.p)) {
            rangeIterable.add(node.p);
        }

        // recursively search left/bottom
        if (node.lb != null && rect.intersects(node.lb.rect)) {
            range(node.lb, rangeIterable, rect);
        }

        // recursively search right/top
        if (node.rt != null && rect.intersects(node.rt.rect)) {
            range(node.rt, rangeIterable, rect);
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }

        if (root == null) {
            return null;
        }

        // at start, champion is always the first point
        return nearest(root, root.p, root.p.distanceSquaredTo(p), p);
    }

    private Point2D auxNearest(Node node, Point2D champion, double minDist, Point2D p) {
        if (node == null) {
            return champion;
        }
        else {
            double distSq = node.rect.distanceSquaredTo(p);
            if (distSq > minDist) {
                // exclude from search if rectangle distance is
                // further from minimum distance
                return champion;
            }

            Point2D chp = champion;
            double mdist = minDist;
            double dist = node.p.distanceSquaredTo(p);
            if (dist < minDist) {
                chp = node.p;
                mdist = dist;
            }
            return nearest(node, chp, mdist, p);
        }
    }

    private Point2D nearest(Node node, Point2D champion, double minDist, Point2D p) {
        if (!node.horizontal) {
            // check point to left or right:
            double diff = p.x() - node.p.x();
            if (diff >= 0) { // right
                return auxNearest(node.rt, champion, minDist, p);
            }
            else // left
            {
                return auxNearest(node.lb, champion, minDist, p);
            }
        }
        else {
            // check point to top or bottom:
            double diff = p.y() - node.p.y();
            if (diff >= 0) { // top
                return auxNearest(node.rt, champion, minDist, p);
            }
            else // bottom
            {
                return auxNearest(node.lb, champion, minDist, p);
            }
        }
    }

    public static void main(String[] args) {
        Point2D p = new Point2D(0.34, 0.54);

        KdTree points = new KdTree();
        StdOut.println(points.size());
        StdOut.println(points.isEmpty());

        points.insert(new Point2D(0.65, 0.52));
        StdOut.println(points.size());
        StdOut.println(points.isEmpty());

        points.insert(new Point2D(0.4, 0.75));
        points.insert(new Point2D(0.75, 0.11));
        points.insert(new Point2D(0.894, 0.51));
        points.insert(new Point2D(0.32, 0.04));
        points.insert(new Point2D(0.12, 0.98));
        StdOut.println(points.size());
        StdOut.println(points.contains(new Point2D(0.75, 0.11)));
        StdOut.println(points.contains(p));

        // points.draw();

        StdOut.println(points.nearest(p));
        points.insert(p);
        points.draw();

        StdOut.println();
        for (Point2D point : points.range(new RectHV(0, 0, 1, 1))) {
            StdOut.println(point);
        }
        KdTree newPoints = new KdTree();
        newPoints.insert(new Point2D(0.0, 1.0));
        StdOut.println(newPoints.size());
        newPoints.insert(new Point2D(0.0, 1.0));
        StdOut.println(newPoints.size());
    }
}

