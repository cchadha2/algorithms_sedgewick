/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MergeX;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {

    private int n;
    private final LineSegment[] segments;

    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("points array cannot be null");

        for (Point elem : points) {
            if (elem == null) throw new IllegalArgumentException("point cannot be null");
        }

        Point[] pointsCopy = points.clone();
        MergeX.sort(pointsCopy);

        for (int i = 0; i < pointsCopy.length - 1; i++) {
            if (pointsCopy[i].compareTo(pointsCopy[i + 1]) == 0) {
                throw new IllegalArgumentException("Duplicate point in input array");
            }
        }

        LineSegment[] tempSegments = new LineSegment[points.length];

        for (int p = 0; p < points.length; p++) {
            // Sort by values and then by slope with respect to p (stable sort)
            MergeX.sort(pointsCopy, points[p].slopeOrder());

            // Check for collinear points
            Point min = points[p];
            Point max;

            int segmentMin = 1;
            for (int i = 0; i < points.length - 1; i++) {

                if (points[p].equals(pointsCopy[i])) continue;
                else if (points[p].slopeTo(pointsCopy[i]) == points[p].slopeTo(pointsCopy[i + 1])) {
                    segmentMin++;
                    continue;
                }
                else if (segmentMin < 3) continue;

                max = pointsCopy[i];

                LineSegment line = new LineSegment(min, max);
                tempSegments[n] = line;
                n++;
                break;
            }

        }
        // Copy all line segments from temp array to smaller segments array
        segments = new LineSegment[n];

        for (int idx = 0; idx < n; idx++) {
            segments[idx] = tempSegments[idx];
        }

        tempSegments = null;
    }  // finds all line segments containing 4 points

    public int numberOfSegments() {
        return n;
    }       // the number of line segments

    public LineSegment[] segments() {
        return segments.clone();
    }                // the line segments

    public static void main(String[] args) {
        // Point[] testPoints = new Point[9];
        // testPoints[0] = new Point(1, 1);
        // testPoints[1] = new Point(2, 2);
        // testPoints[2] = new Point(3, 3);
        // testPoints[3] = new Point(4, 4);
        // testPoints[4] = new Point(5, 5);
        // testPoints[5] = new Point(5, 10);
        // testPoints[6] = new Point(2, 7);
        // testPoints[7] = new Point(5, 6);
        // testPoints[8] = new Point(1, 9);
        //
        // Point[] testMerge = testPoints.clone();
        // MergeX.sort(testMerge);
        // for (Point elem : testMerge) {
        //     StdOut.println(elem);
        // }
        // StdOut.println();
        // MergeX.sort(testMerge, testMerge[0].slopeOrder());
        // for (Point elem : testMerge) {
        //     StdOut.println(elem);
        // }
        //
        // FastCollinearPoints obj = new FastCollinearPoints(testPoints);
        // StdOut.println();
        // StdOut.println(obj.numberOfSegments());
        // StdOut.println(obj.segments());

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();


        // StdDraw.enableDoubleBuffering();
        // StdDraw.setXscale(0, 32768);
        // StdDraw.setYscale(0, 32768);
        // Point[] points = new Point[5];
        // points[0] = new Point(10000, 0);
        // points[1] = new Point(6000, 7000);
        // points[2] = new Point(7000, 3000);
        // points[3] = new Point(3000, 7000);
        // points[4] = new Point(0, 10000);
        // for (Point p : points) {
        //     p.draw();
        // }
        // StdDraw.show();

    }
}
