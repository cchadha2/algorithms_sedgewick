/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MergeX;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {

    private int n;
    private final LineSegment[] segments;

    public BruteCollinearPoints(Point[] points) {
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

        LineSegment[] tempLineSegments = new LineSegment[pointsCopy.length];
        Point[] tempPoints = new Point[4];

        n = 0;
        for (int p = 0; p < pointsCopy.length - 3; p++) {
            for (int q = 1; q < pointsCopy.length - 2; q++) {
                for (int r = 2; r < pointsCopy.length - 1; r++) {
                    for (int s = 3; s < pointsCopy.length; s++) {
                        tempPoints[0] = pointsCopy[p];
                        tempPoints[1] = pointsCopy[q];
                        tempPoints[2] = pointsCopy[r];
                        tempPoints[3] = pointsCopy[s];

                        if (p >= q || p >= r || p >= s || q >= r || q >= s || r >= s) continue;

                        // Compare both extreme points to inner points and check for equivalence
                        if (tempPoints[3].slopeOrder().compare(tempPoints[1], tempPoints[2])
                                == 0 && tempPoints[0].slopeOrder()
                                                     .compare(tempPoints[1], tempPoints[2]) == 0) {

                            LineSegment fourPointLine = new LineSegment(tempPoints[0],
                                                                        tempPoints[3]);
                            // Add line segment of extreme points to segments if above is true
                            tempLineSegments[n] = fourPointLine;
                            n++;
                        }
                    }
                }
            }
        }

        // Copy all line segments from temp array to smaller segments array
        segments = new LineSegment[n];
        for (int idx = 0; idx < n; idx++) {
            segments[idx] = tempLineSegments[idx];
        }

    }  // finds all line segments containing 4 points

    public int numberOfSegments() {
        return n;
    }       // the number of line segments

    public LineSegment[] segments() {
        return segments.clone();
    }                // the line segments

    public static void main(String[] args) {
        // Point[] testPoints = new Point[8];
        // testPoints[0] = new Point(1, 1);
        // testPoints[1] = new Point(2, 2);
        // testPoints[2] = new Point(3, 3);
        // testPoints[3] = new Point(4, 4);
        // testPoints[4] = new Point(5, 10);
        // testPoints[5] = new Point(2, 7);
        // testPoints[6] = new Point(5, 6);
        // testPoints[7] = new Point(1, 9);
        //
        // BruteCollinearPoints obj = new BruteCollinearPoints(testPoints);
        // StdOut.println(obj.numberOfSegments());
        // StdOut.println(obj.segments());
        //
        // Point[] testPoints2 = new Point[8];
        // testPoints2[0] = new Point(1, 1);
        // // testPoints2[1] = new Point(1, 1);
        // testPoints2[1] = new Point(5, 10);
        // testPoints2[2] = new Point(3, 3);
        // testPoints2[3] = new Point(4, 4);
        // testPoints2[4] = new Point(5, 10);
        // testPoints2[5] = new Point(2, 7);
        // testPoints2[6] = new Point(5, 6);
        // testPoints2[7] = new Point(1, 9);
        //
        // BruteCollinearPoints obj2 = new BruteCollinearPoints(testPoints2);
        // StdOut.println(obj2.numberOfSegments());
        // StdOut.println(obj2.segments());
        //
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();

    }
}
