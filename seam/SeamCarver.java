import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {

    private Picture picture;
    // private double[][] energy;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) { throw new IllegalArgumentException("Null input"); }

        this.picture = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return picture;
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    public double energy(int x, int y) {
        checkCoordinate(x, width());
        checkCoordinate(y, height());

        // If point is on the border, energy is 1000.
        try {
            checkCoordinate(x - 1, width());
            checkCoordinate(x + 1, width());
            checkCoordinate(y - 1, height());
            checkCoordinate(y + 1, height());
        }
        catch (IllegalArgumentException e) {
            return 1000;
        }

        // Otherwise, find the point's energy.
        int lowerX = picture.getRGB(x - 1, y);
        int upperX = picture.getRGB(x + 1, y);
        int lowerY = picture.getRGB(x, y - 1);
        int upperY = picture.getRGB(x, y + 1);

        // RGB components of a pixel are encoded using least significant
        // 24 bits. Use bit-shifting to extract components to avoid creating
        // multiple Color objects.
        return Math.sqrt(
                Math.pow(((upperX >> 16) & 0xFF) - ((lowerX >> 16) & 0xFF), 2)
                        + Math.pow(((upperX >> 8) & 0xFF) - ((lowerX >> 8) & 0xFF), 2)
                        + Math.pow((upperX & 0xFF) - (lowerX & 0xFF), 2)
                        + Math.pow(((upperY >> 16) & 0xFF) - ((lowerY >> 16) & 0xFF), 2)
                        + Math.pow(((upperY >> 8) & 0xFF) - ((lowerY >> 8) & 0xFF), 2)
                        + Math.pow((upperY & 0xFF) - (lowerY & 0xFF), 2)
        );
    }

    private void checkCoordinate(int coordinate, int limit) {
        if (coordinate < 0 || coordinate > limit - 1) { throw new IllegalArgumentException("coordinate outside of range"); }
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int[] seams = new int[width()];
        double[][] distTo = new double[width()][height()];
        double[][] energy = new double[width()][height()];
        int[][] edgeTo = new int[width()][height()];

        for (int y = 0; y < height(); y++) {
            distTo[0][y] = 0;
            edgeTo[0][y] = -1;
        }

        for (int x = 1; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                distTo[x][y] = Double.POSITIVE_INFINITY;
            }
        }

        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                energy[x][y] = energy(x, y);
            }
        }

        for (int x = 0; x < width() - 1; x++) {
            for (int y = 1; y < height() - 1; y++) {

                if (distTo[x + 1][y + 1] > distTo[x][y] + energy[x + 1][y + 1]) {
                    distTo[x + 1][y + 1] = distTo[x][y] + energy[x + 1][y + 1];
                    edgeTo[x + 1][y + 1] = y;
                }
                if (distTo[x + 1][y] > distTo[x][y] + energy[x + 1][y]) {
                    distTo[x + 1][y] = distTo[x][y] + energy[x + 1][y];
                    edgeTo[x + 1][y] = y;
                }
                if (distTo[x + 1][y - 1] > distTo[x][y] + energy[x + 1][y - 1]) {
                    distTo[x + 1][y - 1] = distTo[x][y] + energy[x + 1][y - 1];
                    edgeTo[x + 1][y - 1] = y;
                }
            }
        }

        double min = Double.POSITIVE_INFINITY;
        int currentY = 0;
        for (int y = 0; y < height() - 1; y++) {
            if (distTo[width() - 1][y] < min) {
                min = distTo[width() - 1][y];
                currentY = y;
            }
        }

        seams[width() - 1] = currentY;
        for (int x = width() - 1; x > 0; x--) {
            seams[x - 1] = edgeTo[x][currentY];
            currentY = seams[x - 1];
        }

        return seams;
    }


    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] seams = new int[height()];
        double[][] distTo = new double[width()][height()];
        double[][] energy = new double[width()][height()];
        int[][] edgeTo = new int[width()][height()];

        for (int x = 0; x < width(); x++) {
            distTo[x][0] = 0;
            edgeTo[x][0] = -1;
        }

        for (int y = 1; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                distTo[x][y] = Double.POSITIVE_INFINITY;
            }
        }

        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                energy[x][y] = energy(x, y);
            }
        }

        for (int y = 0; y < height() - 1; y++) {
            for (int x = 1; x < width() - 1; x++) {

                if (distTo[x - 1][y + 1] > distTo[x][y] + energy[x - 1][y + 1]) {
                    distTo[x - 1][y + 1] = distTo[x][y] + energy[x - 1][y + 1];
                    edgeTo[x - 1][y + 1] = x;
                }
                if (distTo[x][y + 1] > distTo[x][y] + energy[x][y + 1]) {
                    distTo[x][y + 1] = distTo[x][y] + energy[x][y + 1];
                    edgeTo[x][y + 1] = x;
                }
                if (distTo[x + 1][y + 1] > distTo[x][y] + energy[x + 1][y + 1]) {
                    distTo[x + 1][y + 1] = distTo[x][y] + energy[x + 1][y + 1];
                    edgeTo[x + 1][y + 1] = x;
                }
            }
        }

        double min = Double.POSITIVE_INFINITY;
        int currentX = 0;
        for (int x = 0; x < width() - 1; x++) {
            if (distTo[x][height() - 1] < min) {
                min = distTo[x][height() - 1];
                currentX = x;
            }
        }

        seams[height() - 1] = currentX;
        for (int y = height() - 1; y > 0; y--) {
            seams[y - 1] = edgeTo[currentX][y];
            currentX = seams[y - 1];
        }

        return seams;
    }

    private void checkAdjacent(int[] seam, int idx) {
        if (Math.abs(seam[idx] - seam[idx - 1]) > 1) {
            throw new IllegalArgumentException("Two adjacent entries differ by more than 1");
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) { throw new IllegalArgumentException("Null input"); }
        else if (seam.length != width()) { throw new IllegalArgumentException("Horizontal seam of incorrect length"); }
        else if (width() <= 1) { throw new IllegalArgumentException("Width of picture is less than or equal to 1"); }

        for (int idx = 1; idx < seam.length; idx++) {
            checkCoordinate(seam[idx], height());
            checkAdjacent(seam, idx);
        }

        int x = 0;
        for (int idx : seam) {
            for (int y = idx; y < height() - 1; y++) {
                picture.setRGB(x, y, picture.getRGB(x, y + 1));
                picture.setRGB(x, y + 1, 0);
            }
            x++;
        }

        Picture newPicture = new Picture(width(), height() - 1);
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height() - 1; row++) {
                newPicture.setRGB(col, row, picture.getRGB(col, row));
            }
        }
        picture = newPicture;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) { throw new IllegalArgumentException("Null input"); }
        else if (seam.length != height()) { throw new IllegalArgumentException("Vertical seam of incorrect length"); }
        else if (height() <= 1) { throw new IllegalArgumentException("Height of picture is less than or equal to 1"); }

        for (int idx = 1; idx < seam.length; idx++) {
            checkCoordinate(seam[idx], width());
            checkAdjacent(seam, idx);
        }

        int y = 0;
        for (int idx : seam) {
            for (int x = idx; x < width() - 1; x++) {
                picture.setRGB(x, y, picture.getRGB(x + 1, y));
                picture.setRGB(x + 1, y, 0);
            }
            y++;
        }

        Picture newPicture = new Picture(width() - 1, height());
        for (int col = 0; col < width() - 1; col++) {
            for (int row = 0; row < height(); row++) {
                newPicture.setRGB(col, row, picture.getRGB(col, row));
            }
        }
        picture = newPicture;
    }



    //  unit testing (optional)
    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);

        SeamCarver sc = new SeamCarver(picture);
        assert sc.picture == picture;
        assert sc.width() == picture.width();
        assert sc.height() == picture.height();
        assert sc.energy(0,0) == 1000;
        StdOut.println(sc.energy(1,1));

        try {
            sc.energy(-1, -1);
        }
        catch (IllegalArgumentException e) {
            StdOut.println("Correctly caught exception.");
        }

        sc.findVerticalSeam();
        sc.findHorizontalSeam();
    }

}