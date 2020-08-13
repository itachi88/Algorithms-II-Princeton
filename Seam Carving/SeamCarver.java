/* *****************************************************************************
 *  Name: Spandan Mishra
 *  Date: 12 Aug 2020
 *  Description: Seam Carver datatype
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.Arrays;

public class SeamCarver {

    private static final int MAX_ENERGY = 1000;
    private Picture picture;
    private double[][] energy;
    private int[][] parent;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
        // this.picture = picture;
        // energy = new double[height()][width()];
        // parent = new int[height()][width()];

    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
        // return picture;
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height())
            throw new IllegalArgumentException("pixel out of boundary");

        // on border
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1)
            return MAX_ENERGY;

        Color left = picture.get(x - 1, y);
        Color right = picture.get(x + 1, y);
        Color top = picture.get(x, y - 1);
        Color bot = picture.get(x, y + 1);

        return Math.sqrt(sqGrad(top, bot) + sqGrad(left, right));
    }

    private double sqGrad(Color c1, Color c2) {
        return Math.pow(c1.getRed() - c2.getRed(), 2) +
                Math.pow(c1.getBlue() - c2.getBlue(), 2) +
                Math.pow(c1.getGreen() - c2.getGreen(), 2);

    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        // System.out.println("before transpose====");
        // System.out.println("rows = " + picture.height() + " cols = " + picture.width());


        transpose();

        // System.out.println("rows = " + picture.height() + " cols = " + picture.width());

        int[] seam = findVerticalSeam();
        transpose();
        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] seam = new int[height()];

        energy = new double[height()][width()];
        parent = new int[height()][width()];
        // energy computation

        // System.out.println("Picture height = " + height() + " width = " + width());
        // System.out.println();

        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                energy[y][x] = energy(x, y);
            }
        }

        // System.out.println("Printing energy array========");
        // for (int i = 0; i < height(); i++)
        //     System.out.println(Arrays.toString(energy[i]));

        double[][] dp = new double[height()][width()];
        for (int y = 0; y < height(); y++)
            Arrays.fill(dp[y], Double.POSITIVE_INFINITY);

        Arrays.fill(dp[0], MAX_ENERGY);

        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                // push

                // bottom left
                // if (x < width() - 1 && y > 0) {
                if (y < height() - 1 && x > 0) {
                    if (dp[y + 1][x - 1] > dp[y][x] + energy[y + 1][x - 1]) {
                        dp[y + 1][x - 1] = dp[y][x] + energy[y + 1][x - 1];
                        // update col no cause it's obviously modified by its
                        // top row only
                        parent[y + 1][x - 1] = x;
                    }
                }

                // bottom right

                if (y < height() - 1 && x < width() - 1) {
                    if (dp[y + 1][x + 1] > dp[y][x] + energy[y + 1][x + 1]) {
                        dp[y + 1][x + 1] = dp[y][x] + energy[y + 1][x + 1];
                        // update col no cause it's obviously modified by its
                        // top row only
                        parent[y + 1][x + 1] = x;
                    }
                }

                // bottom mid

                if (y < height() - 1) {
                    if (dp[y + 1][x] > dp[y][x] + energy[y + 1][x]) {
                        dp[y + 1][x] = dp[y][x] + energy[y + 1][x];
                        // update col no cause it's obviously modified by its
                        // top row only
                        parent[y + 1][x] = x;
                    }
                }
            }
        }

        // System.out.println("\nPrinting DP array ===============\n");
        //
        // for (int i = 0; i < height(); i++) {
        //     System.out.println(Arrays.toString(dp[i]));
        // }
        //
        // System.out.println("\nPrinting parent array======\n");
        // for (int i = 0; i < height(); i++) {
        //     System.out.println(Arrays.toString(parent[i]));
        // }

        // find the min dp val in last col and traverse till parent

        int minStart = 0;
        double minVal = dp[height() - 1][0];
        for (int i = 1; i < width(); i++) {
            if (minVal > dp[height() - 1][i]) {
                minVal = dp[height() - 1][i];
                minStart = i;
            }
        }

        // modifying the seam logic a bit to keep self instead of parent
        for (int i = height() - 1; i >= 0; i--) {
            // seam[i] = parent[i][minStart];
            seam[i] = minStart;
            minStart = parent[i][minStart];
        }

        // System.out.println("\nSeam Print=============\n");
        // System.out.println(Arrays.toString(seam));
        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        transpose();
        removeVerticalSeam(seam);
        transpose();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null || seam.length == 0)
            throw new IllegalArgumentException("Empty / null seam");

        // System.out.println("pic height = " + height() + " width = " + width());
        Picture ans = new Picture(width() - 1, height());

        for (int row = 0; row < height(); row++) {
            int k = 0;
            for (int col = 0; col < width(); col++) {
                if (col != seam[row]) {
                    // continue;
                    ans.set(k, row, picture.get(col, row));
                    k++;
                }

                // else
                //     System.out.println("seam =" + col);

                // System.out.println("row = " + row + " col = " + col);
            }
        }

        picture = ans;
    }

    private void transpose() {

        // width - first param , height - 2nd param
        // trans ---> row = width , col = height
        Picture trans = new Picture(height(), width());

        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                trans.set(i, j, picture.get(j, i));
            }
        }
        picture = trans;
    }

    //  unit testing (optional)
    public static void main(String[] args) {

        Picture p = new Picture("/home/itachi/Algs 2 Princeton/seam/HJocean.png");
        SeamCarver sc = new SeamCarver(p);

        // sc.findHorizontalSeam();
        sc.removeVerticalSeam(sc.findVerticalSeam());
        // p.show();
        // sc.transpose();
        // sc.picture.show();


    }
}
