package com.resnik.util.images;

import com.resnik.util.files.FileUtils;
import com.resnik.util.images.features.Gradient;
import com.resnik.util.images.features.HOG;
import com.resnik.util.math.symbo.Bounds;
import com.resnik.util.math.symbo.operations.Operation;
import com.resnik.util.objects.arrays.ArrayUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.util.Callback;
import javafx.util.Pair;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 *
 * @author Mike
 */
public final class ImageUtils {

    private static final boolean DEBUG = true;
    private static final int BLACK = 0x00000000, WHITE = 0xffffffff;
    public static final byte[] BLACK_B = new byte[]{BLACK, BLACK, BLACK}, WHITE_B = new byte[]{(byte) WHITE, (byte) WHITE, (byte) WHITE};
    public static final byte[] BLACK_ARGB = new byte[]{BLACK, BLACK, BLACK, -1};
    public static final byte[] WHITE_ARGB = new byte[]{(byte) WHITE, (byte) WHITE, (byte) WHITE, -1};

    public static final Color[] RAINBOW_AWT = new Color[]{
            Color.RED,
            Color.ORANGE,
            Color.YELLOW,
            Color.GREEN,
            Color.CYAN,
            Color.BLUE,
            Color.MAGENTA
    };

    public static byte[] gradientRainbow(Color[] inputs, double val){
        double inner = val*inputs.length;
        int index = (int)(inner);
        if(index >= inputs.length - 1){
            return awtToByte(inputs[inputs.length - 1]);
        }
        if(index <= 0){
            return awtToByte(inputs[0]);
        }
        Color left = inputs[index];
        Color right = inputs[index + 1];
        double percent = inner - index;
        return gradientTwo(left, right, percent);
    }

    public static byte[] gradientRYG(double val){
        return gradientRainbow(new Color[]{Color.RED, Color.YELLOW, Color.GREEN}, val);
    }

    public static byte[] gradientTwo(Color red, Color yellow, double val){
        int r = (int)((yellow.getRed() - red.getRed()) * val + red.getRed());
        int g = (int)((yellow.getGreen() - red.getGreen()) * val + red.getGreen());
        int b = (int)((yellow.getBlue() - red.getBlue()) * val + red.getBlue());
        return awtToByte(new Color(r,g,b));
    }

    public static byte[] toByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }

    public static final byte[] GREY_B(double scale) {
        byte GREY_VAL = (byte) (scale * 255);
        byte[] retArray = new byte[]{GREY_VAL, GREY_VAL, GREY_VAL};
        return retArray;
    }

    private ImageUtils() {

    }

    public static List<byte[][][]> detectionWindow(byte[][][] inputImage, int[] dim, int... offsets) {
        List<byte[][][]> retImages = new ArrayList();
        int y_offset = (offsets.length > 0 ? offsets[0] : 1);
        int x_offset = (offsets.length > 1 ? offsets[1] : 1);
        for (int ROW = 0; ROW < inputImage.length - dim[1] + 1; ROW += y_offset) {
            for (int COL = 0; COL < inputImage[0].length - dim[0] + 1; COL += x_offset) {
                byte[][][] currImage = ArrayUtils.subset(inputImage, ROW, COL, dim[1], dim[0], BLACK_B);
                retImages.add(currImage);
                System.out.println("row:" + ROW + "\tcol:" + COL);
            }
        }
        return retImages;
    }

    public static <R> List<R> detectionWindowRun(Callback<byte[][][], R> callback, byte[][][] inputImage, int[] dim, int... offsets) {
        List<R> results = new ArrayList();
        int y_offset = (offsets.length > 0 ? offsets[0] : 1);
        int x_offset = (offsets.length > 1 ? offsets[1] : 1);
        for (int ROW = 0; ROW < inputImage.length - dim[1] + 1; ROW += y_offset) {
            for (int COL = 0; COL < inputImage[0].length - dim[0] + 1; COL += x_offset) {
                byte[][][] currImage = ArrayUtils.subset(inputImage, ROW, COL, dim[1], dim[0], BLACK_B);
                results.add(callback.call(currImage));
                System.out.println("row:" + ROW + "\tcol:" + COL);
            }
        }
        return results;
    }

    //<editor-fold defaultstate="collapsed" desc="Resize">
    public static byte[][][] resizeNearest(byte[][][] inputImage, int[] newRes) {
        byte[][][] retImage = ImageUtils.solidRect(newRes[1], newRes[0], BLACK_B);
        double y_scalar = inputImage.length * (1.0 / retImage.length);
        double x_scalar = inputImage[0].length * (1.0 / retImage[0].length);
        System.out.println("scalars:" + y_scalar + " " + x_scalar);
        for (int ROW = 0; ROW < retImage.length; ROW++) {
            for (int COL = 0; COL < retImage[0].length; COL++) {
                int yVal = (int) (ROW * y_scalar);
                int xVal = (int) (COL * x_scalar);
                retImage[ROW][COL] = inputImage[yVal][xVal];
            }
        }
        return retImage;
    }

    public static List<byte[][][]> resizeNearest(List<byte[][][]> inputImages, int[] newRes) {
        List<byte[][][]> retList = new ArrayList();
        for (byte[][][] inputImage : inputImages) {
            retList.add(resizeNearest(inputImage, newRes));
        }
        return retList;
    }
//</editor-fold>

    //<editor-fold desc="Operations">
    public static byte[][][] add(byte[][][][] inputImages) {
        byte[][][] retImage = solidRect(inputImages[0].length, inputImages[0][0].length, ImageUtils.BLACK_B);
        for (byte[][][] image : inputImages) {
            for (int ROW = 0; ROW < retImage.length; ROW++) {
                for (int COL = 0; COL < retImage[0].length; COL++) {
                    int r = image[ROW][COL][0];
                    int g = image[ROW][COL][1];
                    int b = image[ROW][COL][2];

                    int rRET = retImage[ROW][COL][0];
                    int gRET = retImage[ROW][COL][1];
                    int bRET = retImage[ROW][COL][2];

                    rRET = (r + rRET > 255 ? 255 : r + rRET);
                    gRET = (g + gRET > 255 ? 255 : g + gRET);
                    bRET = (b + bRET > 255 ? 255 : b + bRET);
                    retImage[ROW][COL] = new byte[]{(byte) rRET, (byte) gRET, (byte) bRET};
                }
            }
        }
        return retImage;
    }

    public static byte[][][] erodeBinary(byte[][][] binaryImage, StructuringElement B) {
        byte[][][] retImage = new byte[binaryImage.length][binaryImage[0].length][];
        for (int image_ROW = 0; image_ROW < binaryImage.length; image_ROW++) {
            for (int image_COL = 0; image_COL < binaryImage[0].length; image_COL++) {
                int[][][] coordCheck = B.applyVectorRepresentation(new int[]{image_COL, image_ROW});

                boolean forAll = true;
                for (int c_ROW = 0; forAll && c_ROW < coordCheck.length; c_ROW++) {
                    for (int c_COL = 0; forAll && c_COL < coordCheck[0].length; c_COL++) {
                        try {
                            int[] coord = coordCheck[c_ROW][c_COL];
                            if (coord == null) {
                                continue;
                            }
                            if (Arrays.equals(binaryImage[coord[1]][coord[0]], BLACK_B) == false) {
                                forAll = false;
                                continue;
                            }
                        } catch (Exception e) {
                            continue;
                        }

                    }
                }
                retImage[image_ROW][image_COL] = (forAll ? BLACK_B : WHITE_B);
            }
        }
        return retImage;
    }

    public static byte[][][] dilateBinary(byte[][][] binaryImage, StructuringElement B) {
        byte[][][] retImage = new byte[binaryImage.length][binaryImage[0].length][];
        for (int image_ROW = 0; image_ROW < binaryImage.length; image_ROW++) {
            for (int image_COL = 0; image_COL < binaryImage[0].length; image_COL++) {
                retImage[image_ROW][image_COL] = WHITE_B;
                if (Arrays.equals(binaryImage[image_ROW][image_COL], BLACK_B)) {
                    int[][][] coordCheck = B.applyVectorRepresentation(new int[]{image_COL, image_ROW});
                    for (int c_ROW = 0; c_ROW < coordCheck.length; c_ROW++) {
                        for (int c_COL = 0; c_COL < coordCheck[0].length; c_COL++) {
                            try {
                                int[] coord = coordCheck[c_ROW][c_COL];
                                if (coord == null) {
                                    continue;
                                }
                                retImage[coord[1]][coord[0]] = BLACK_B;
                            } catch (Exception e) {
                                continue;
                            }
                        }
                    }
                }

            }
        }
        return retImage;
    }

    public static byte[][][] openBinary(byte[][][] binaryImage, StructuringElement B) {
        return dilateBinary(erodeBinary(binaryImage, B), B);
    }

    public static byte[][][] closeBinary(byte[][][] binaryImage, StructuringElement B) {
        return erodeBinary(dilateBinary(binaryImage, B), B);
    }

    public static BufferedImage scale(BufferedImage image, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, image.getType());
        Graphics2D graphics2D = scaledImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();
        return scaledImage;
    }

    public static List<BufferedImage> scale(List<BufferedImage> inputImages, int width, int height) {
        List<BufferedImage> retList = new ArrayList();
        for (BufferedImage image : inputImages) {
            retList.add(scale(image, width, height));
        }
        return retList;
    }

    public static Color bleach(Color color, float amount) {
        int red = (int) ((color.getRed() * (1 - amount) / 255 + amount) * 255);
        int green = (int) ((color.getGreen() * (1 - amount) / 255 + amount) * 255);
        int blue = (int) ((color.getBlue() * (1 - amount) / 255 + amount) * 255);
        return new Color(red, green, blue);
    }

    public static byte[][][] reflectRows(byte[][][] inputImage) {
        byte[][][] retImage = new byte[inputImage.length][inputImage[0].length][];
        final int HEIGHT = inputImage.length, WIDTH = inputImage[0].length;
        for (int ROW = 0; ROW < HEIGHT; ROW++) {
            for (int COL = 0; COL < WIDTH; COL++) {
                retImage[HEIGHT - ROW - 1][COL] = inputImage[ROW][COL];
            }
        }
        return retImage;
    }

    public static byte[][][] reflectCols(byte[][][] inputImage) {
        byte[][][] retImage = new byte[inputImage.length][inputImage[0].length][];
        final int HEIGHT = inputImage.length, WIDTH = inputImage[0].length;
        for (int ROW = 0; ROW < HEIGHT; ROW++) {
            for (int COL = 0; COL < WIDTH; COL++) {
                retImage[ROW][WIDTH - COL - 1] = inputImage[ROW][COL];
            }
        }
        return retImage;
    }

    public static byte[][][] reflectRowsCols(byte[][][] inputImage) {
        byte[][][] retImage = new byte[inputImage.length][inputImage[0].length][];
        final int HEIGHT = inputImage.length, WIDTH = inputImage[0].length;
        for (int ROW = 0; ROW < HEIGHT; ROW++) {
            for (int COL = 0; COL < WIDTH; COL++) {
                retImage[HEIGHT - ROW - 1][WIDTH - COL - 1] = inputImage[ROW][COL];
            }
        }
        return retImage;
    }
// </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Apply">
    public static int[][] applySobel(int[][] inputImage, double... div) {
        int[][] retImage = new int[inputImage.length][inputImage[0].length];

        double[][] filter_x = new double[][]{
            {-1, 0, 1},
            {-2, 0, 2},
            {-1, 0, 1}
        }, filter_y = new double[][]{
            {-1, -2, -1},
            {0, 0, 0},
            {1, 2, 1}
        };
        StructuringElement structure_x = StructuringElement.square(3), structure_y = structure_x;

        Gradient[][] slope_field = HOG.findGradients(inputImage, structure_x, structure_y, filter_x, filter_y, (div.length > 0 ? div[0] : 1.0));

        for (int ROW = 0; ROW < slope_field.length; ROW++) {
            for (int COL = 0; COL < slope_field[0].length; COL++) {
                double mag = slope_field[ROW][COL].mag;
                if (mag < 0) {
                    mag += 256;
                }
                if (mag > 256) {
                    mag -= 256;
                }
                byte[] pixel = new byte[]{(byte) mag, (byte) mag, (byte) mag};
                retImage[ROW][COL] = ImageUtils.rgbaToINT(pixel);
            }
        }

        return retImage;

    }

    public static int[][] applySobelMax(int[][][] inputImages, double... div) {
        int[][] retImage = new int[inputImages[0].length][inputImages[0][0].length];

        double[][] filter_x = new double[][]{
            {-1, 0, 1},
            {-2, 0, 2},
            {-1, 0, 1}
        }, filter_y = new double[][]{
            {-1, -2, -1},
            {0, 0, 0},
            {1, 2, 1}
        };
        StructuringElement structure_x = StructuringElement.square(3), structure_y = structure_x;

        Gradient[][] slope_field = HOG.findMax(inputImages, structure_x, structure_y, filter_x, filter_y, (div.length > 0 ? div[0] : 1.0));

        for (int ROW = 0; ROW < slope_field.length; ROW++) {
            for (int COL = 0; COL < slope_field[0].length; COL++) {
                double mag = slope_field[ROW][COL].mag;
                if (mag < 0) {
                    mag += 256;
                }
                if (mag > 256) {
                    mag -= 256;
                }
                byte[] pixel = new byte[]{(byte) mag, (byte) mag, (byte) mag};
                retImage[ROW][COL] = ImageUtils.rgbaToINT(pixel);
            }
        }

        return retImage;

    }

    // apply edges using regular gradients / kernals
    public static int[][] applySlopefield(int[][] inputImage, double scalar, boolean... debug) {
        int[][] retImage;
        // {{1,c,1}}
        StructuringElement structure_x = StructuringElement.rect(3, 1, 0, 1);
//        structure_x.print();
//        structure_x.printVec();

// {{1}, {c}, {1}}
        StructuringElement structure_y = StructuringElement.rect(1, 3, 1, 0);
//        structure_y.print();
//        structure_y.printVec();

        double[][] filter_x = {{-1}, {0}, {1}};
        double[][] filter_y = {{-1, 0, 1}};

        Gradient[][] slope_field = HOG.findGradients(inputImage, structure_x, structure_y, filter_x, filter_y, 0, scalar);

        int dim = 7;
        int thresh = 10;
        StructuringElement square = StructuringElement.square(dim);
        retImage = new int[inputImage.length * dim][inputImage[0].length * dim];

        for (int ROW = 0; ROW < slope_field.length; ROW++) {
            for (int COL = 0; COL < slope_field[0].length; COL++) {
                Gradient g = slope_field[ROW][COL];
                double dx = g.G_x, dy = g.G_y;
                double slope = (dx == 0 ? Double.MAX_VALUE : dy / dx);
                byte[][][] line = slopeLine(-1.0 * slope, dim, dy == 0 && dx == 0 || g.mag < thresh);
                int[][] intLine = ImageUtils.rgbaToINT(line);
                int x_ins = (int) Math.round((COL + 0.5) * dim);
                int y_ins = (int) Math.round((ROW + 0.5) * dim);
                retImage = insert(retImage, intLine, square, x_ins, y_ins);
            }
            if (debug.length > 0 && debug[0]) {
                System.out.printf("ROW:%s\n", ROW);
            }
        }
        return retImage;
    }

    public static int[][] applySlopefieldMax(int[][][] inputImages, double scalar, boolean... debug) {
        int[][] retImage;
        // {{1,c,1}}
        StructuringElement structure_x = StructuringElement.rect(3, 1, 0, 1);
//        structure_x.print();
//        structure_x.printVec();

// {{1}, {c}, {1}}
        StructuringElement structure_y = StructuringElement.rect(1, 3, 1, 0);
//        structure_y.print();
//        structure_y.printVec();

        double[][] filter_x = {{-1}, {0}, {1}};
        double[][] filter_y = {{-1, 0, 1}};

        Gradient[][] slope_field = HOG.findMax(inputImages, structure_x, structure_y, filter_x, filter_y, scalar);

        int dim = 7;
        int thresh = 10;
        StructuringElement square = StructuringElement.square(dim);
        retImage = new int[inputImages[0].length * dim][inputImages[0][0].length * dim];

        for (int ROW = 0; ROW < slope_field.length; ROW++) {
            for (int COL = 0; COL < slope_field[0].length; COL++) {
                Gradient g = slope_field[ROW][COL];
                double dx = g.G_x, dy = g.G_y;
                double slope = (dx == 0 ? Double.MAX_VALUE : dy / dx);
                byte[][][] line = slopeLine(-1.0 * slope, dim, dy == 0 && dx == 0 || g.mag < thresh);
                int[][] intLine = ImageUtils.rgbaToINT(line);
                int x_ins = (int) Math.round((COL + 0.5) * dim);
                int y_ins = (int) Math.round((ROW + 0.5) * dim);
                retImage = insert(retImage, intLine, square, x_ins, y_ins);
            }
            if (debug.length > 0 && debug[0]) {
                System.out.printf("ROW:%s\n", ROW);
            }
        }
        return retImage;
    }

    public static int[][] applySlopefieldAvg(int[][][] inputImages, double scalar, boolean... debug) {
        int[][] retImage;
        // {{1,c,1}}
        StructuringElement structure_x = StructuringElement.rect(3, 1, 0, 1);
//        structure_x.print();
//        structure_x.printVec();

// {{1}, {c}, {1}}
        StructuringElement structure_y = StructuringElement.rect(1, 3, 1, 0);
//        structure_y.print();
//        structure_y.printVec();

        double[][] filter_x = {{-1}, {0}, {1}};
        double[][] filter_y = {{-1, 0, 1}};

        Gradient[][] slope_field = HOG.findAvg(inputImages, structure_x, structure_y, filter_x, filter_y, scalar);

        int dim = 7;
        int thresh = 10;
        StructuringElement square = StructuringElement.square(dim);
        retImage = new int[inputImages[0].length * dim][inputImages[0][0].length * dim];

        for (int ROW = 0; ROW < slope_field.length; ROW++) {
            for (int COL = 0; COL < slope_field[0].length; COL++) {
                Gradient g = slope_field[ROW][COL];
                double dx = g.G_x, dy = g.G_y;
                double slope = (dx == 0 ? Double.MAX_VALUE : dy / dx);
                byte[][][] line = slopeLine(-1.0 * slope, dim, dy == 0 && dx == 0 || g.mag < thresh);
                int[][] intLine = ImageUtils.rgbaToINT(line);
                int x_ins = (int) Math.round((COL + 0.5) * dim);
                int y_ins = (int) Math.round((ROW + 0.5) * dim);
                retImage = insert(retImage, intLine, square, x_ins, y_ins);
            }
            if (debug.length > 0 && debug[0]) {
                System.out.printf("ROW:%s\n", ROW);
            }
        }
        return retImage;
    }

    public static byte[][][] novelSlopeField(byte[][][] original) {
        byte[][][] new_image = new byte[2 * original.length - 1][2 * original[0].length - 1][original[0][0].length];
        for (int ROW_INDEX = 0; ROW_INDEX < original.length; ROW_INDEX++) {
            for (int COL_INDEX = 0; COL_INDEX < original[0].length; COL_INDEX++) {
                byte[] current = original[ROW_INDEX][COL_INDEX];
                // new_image[2*ROW_INDEX][2*COL_INDEX] = current;
                // try right
                try {
                    byte[] right = original[ROW_INDEX][COL_INDEX + 1];
                    byte[] delta = ArrayUtils.deltaBytes(current, right);
                    new_image[2 * ROW_INDEX][2 * COL_INDEX + 1] = delta;
                } catch (ArrayIndexOutOfBoundsException aioobe) {
                }
                //try down
                try {
                    byte[] down = original[ROW_INDEX + 1][COL_INDEX];
                    byte[] delta = ArrayUtils.deltaBytes(current, down);
                    new_image[2 * ROW_INDEX + 1][2 * COL_INDEX] = delta;
                } catch (ArrayIndexOutOfBoundsException aioobe) {

                }
                //try downright
                try {
                    byte[] downRight = original[ROW_INDEX + 1][COL_INDEX + 1];
                    byte[] delta = ArrayUtils.deltaBytes(current, downRight);
                    new_image[2 * ROW_INDEX + 1][2 * COL_INDEX + 1] = delta;
                } catch (ArrayIndexOutOfBoundsException aioobe) {

                }
            }
        }
        return new_image;
    }

    public static byte[][][] applyThreshold_b(byte[][][] inputImage, double d_threshold) {
        return intToRGB(applyThreshold(inputImage, RPERC, GPERC, BPERC, d_threshold, true));
    }

    public static int[][] applyThreshold(byte[][][] inputImage, double d_threshold) {
        return applyThreshold(inputImage, RPERC, GPERC, BPERC, d_threshold, true);
    }

    public static int[][] applyThreshold(byte[][][] inputImage, double r_perc, double g_perc, double b_perc, double d_threshold, boolean apply_threshold) {
        int[][] retImage = new int[inputImage.length][inputImage[0].length];

        double threshold = (int) (r_perc * d_threshold * 255 + g_perc * d_threshold * 255 + b_perc * d_threshold * 255);

        // System.out.println("threshold:" + threshold);
        for (int i = 0; i < inputImage.length; i++) {
            for (int j = 0; j < inputImage[0].length; j++) {
                int r = inputImage[i][j][0], g = inputImage[i][j][1], b = inputImage[i][j][2];
                if (r < 0) {
                    r += 256;
                }
                if (g < 0) {
                    g += 256;
                }
                if (b < 0) {
                    b += 256;
                }
                double GREY_d = (r_perc * r + g_perc * g + b_perc * b);
                int GREY = (int) GREY_d;
                if (GREY > 256) {
                    GREY -= 256;
                } else if (GREY < 0) {
                    GREY += 256;
                }
                if (apply_threshold) {
                    retImage[i][j] = rgbaToINT((GREY >= threshold ? WHITE_B : BLACK_B));
                } else {
                    r = GREY;
                    g = GREY;
                    b = GREY;
                    byte[] greyArray = new byte[]{(byte) r, (byte) g, (byte) b};
                    retImage[i][j] = rgbaToINT(greyArray);
                }
            }
        }
        return retImage;
    }

    public static char[][] applySlant(int[][] greyImage) {
        String ramp = "$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\"^`'. ";
        char[][] retImage = new char[greyImage.length][greyImage[0].length];
        for (int i = 0; i < greyImage.length; i++) {
            for (int j = 0; j < greyImage[0].length; j++) {
                int val = greyImage[i][j];
                double normalized = 0.5 - val / 255.0;
                System.out.println("val:" + val + "\t normalized:" + normalized);
//                retImage[i][j] = ramp.charAt((int) (normalized * ramp.length()));
            }
        }
        return retImage;
    }

    public static char[][] applySlant(byte[][][] greyValues) {
        String ramp = "$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\"^`'. ";
        char[][] retImage = new char[greyValues.length][greyValues[0].length];
        System.out.println("ramp:" + ramp.length());
        for (int i = 0; i < greyValues.length; i++) {
            for (int j = 0; j < greyValues[0].length; j++) {
                int val = greyValues[i][j][0];
                if (val < 0) {
                    val += 256;
                }
                if (val >= 255) {
                    val -= 256;
                }
                double normalized = val / 255.0;
//                System.out.println("val:" + val + "\t normalized:" + normalized);
                int index = (int) (normalized * ramp.length());
//                System.out.println(index);
                retImage[i][j] = ramp.charAt(index);
            }
        }
        return retImage;
    }

    public static byte[][][] averageImage(byte[][][] inputImage, int stride) {
        byte[][][] retImage = new byte[inputImage.length / stride][inputImage[0].length / stride][inputImage[0][0].length];
        for (int ROW = stride / 2; ROW < inputImage.length - Math.ceil(stride / 2); ROW += stride) {
            for (int COL = stride / 2; COL < inputImage[0].length - Math.ceil(stride / 2); COL += stride) {
//                System.out.println(ROW + ", " + COL);
                double r_sum = 0, g_sum = 0, b_sum = 0;
                for (int i = -stride / 2; i < Math.ceil(stride / 2); i++) {
                    for (int j = -stride / 2; j < Math.ceil(stride / 2); j++) {
                        r_sum += inputImage[ROW + i][COL + j][0];
                        g_sum += inputImage[ROW + i][COL + j][1];
                        b_sum += inputImage[ROW + i][COL + j][2];
                    }
                }
                byte r_avg = (byte) (r_sum / (1.0 * stride * stride));
                byte g_avg = (byte) (g_sum / (1.0 * stride * stride));
                byte b_avg = (byte) (b_sum / (1.0 * stride * stride));
                retImage[ROW / stride][COL / stride] = new byte[]{r_avg, g_avg, b_avg};
            }
        }
        return retImage;
    }
//</editor-fold>

    // <editor-fold desc="Generate">
    public static byte[][][] slopeLine(double slope, int dim, boolean... drawDot) {
        return slopeLinePixel(slope, dim, ImageUtils.WHITE_B, drawDot);
    }

    public static byte[][][] slopeLinePixel(double slope, int dim, byte[] pixel, boolean... drawDot) {
        // needs to make the space for the line
        dim = (dim % 2 == 0 ? dim + 1 : dim);
        int c_x = dim / 2, c_y = dim / 2;
        byte[][][] retImage = solidRect(dim, dim, ImageUtils.BLACK_B);
        if (drawDot.length > 0 && drawDot[0]) {
            retImage[c_y][c_x] = pixel;
            return retImage;
        }
        if (Math.abs(slope) > dim) {
            for (int Y = 0; Y < retImage.length; Y++) {
                retImage[Y][c_x] = pixel;
            }
            return retImage;
        }
        for (int X = 0; X < retImage[0].length; X++) {
            int y = (int) Math.round(slope * (X - c_x) + c_y);
            try {
                retImage[y][X] = pixel;
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                continue;
            }
        }
        if (slope != 0) {
            for (int Y = 0; Y < retImage.length; Y++) {
                int x = (int) Math.round((Y - c_y) / slope + c_x);
                try {
                    retImage[Y][x] = pixel;
                } catch (ArrayIndexOutOfBoundsException aioobe) {
                    continue;
                }
            }
        }
        return retImage;
    }

    public static byte[][][] solidRect(int height, int width, byte[] color) {
        byte[][][] retImage = new byte[height][width][];
        for (int ROW = 0; ROW < height; ROW++) {
            for (int COL = 0; COL < width; COL++) {
                retImage[ROW][COL] = color;
            }
        }
        return retImage;
    }

    public static int[][] insert(int[][] inputImage, int[][] toInsert, StructuringElement structure, int c_x, int c_y) {
        int[][][] insertionPoints = structure.applyVectorRepresentation(new int[]{c_x, c_y});
        int[][] copyImage = copyOf(inputImage);
        for (int ROW = 0; ROW < toInsert.length; ROW++) {
            for (int COL = 0; COL < toInsert[0].length; COL++) {
                int pixel = toInsert[ROW][COL];
                int x_index = insertionPoints[ROW][COL][0], y_index = insertionPoints[ROW][COL][1];
                try {
                    copyImage[y_index][x_index] = pixel;
                } catch (ArrayIndexOutOfBoundsException aioobe) {
                    continue;
                }
            }
        }
        return copyImage;
    }

    public static byte[][][] insert(byte[][][] inputImage, byte[][][] toInsert, StructuringElement structure, int c_x, int c_y) {
        int[][][] insertionPoints = structure.applyVectorRepresentation(new int[]{c_x, c_y});
        byte[][][] copyImage = copyOf(inputImage);
        for (int ROW = 0; ROW < toInsert.length; ROW++) {
            for (int COL = 0; COL < toInsert[0].length; COL++) {
                byte[] pixel = toInsert[ROW][COL];
                int x_index = insertionPoints[ROW][COL][0], y_index = insertionPoints[ROW][COL][1];
                try {
                    copyImage[y_index][x_index] = pixel;
                } catch (ArrayIndexOutOfBoundsException aioobe) {
                    continue;
                }
            }
        }
        return copyImage;
    }

    public static int[][] copyOf(int[][] inputImage) {
        int[][] copyImage = new int[inputImage.length][inputImage[0].length];
        for (int ROW = 0; ROW < inputImage.length; ROW++) {
            for (int COL = 0; COL < inputImage[0].length; COL++) {
                copyImage[ROW][COL] = inputImage[ROW][COL];
            }
        }
        return copyImage;
    }

    public static byte[][][] copyOf(byte[][][] inputImage) {
        byte[][][] copyImage = new byte[inputImage.length][inputImage[0].length][];
        for (int ROW = 0; ROW < inputImage.length; ROW++) {
            for (int COL = 0; COL < inputImage[0].length; COL++) {
                copyImage[ROW][COL] = inputImage[ROW][COL];
            }
        }
        return copyImage;
    }

    public static byte[][][] textImageBorder(char inputText, int size, int CODEC, Color inputColor, int thickness, byte[] borderColor) {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        Font font = new Font("Arial", Font.PLAIN, size);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth("" + inputText);
        int height = fm.getHeight();
        g2d.dispose();

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setFont(font);
        fm = g2d.getFontMetrics();
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(inputColor);
        AttributedString as1 = new AttributedString("" + inputText);
        as1.addAttribute(TextAttribute.FONT, fm);
        as1.addAttribute(TextAttribute.SIZE, size);
        g2d.drawString(as1.getIterator(), 0, fm.getAscent());
        g2d.dispose();

        byte[][][] tempImage = ImageUtils.bufferedImageToBytes(img, CODEC);

        byte[][][] retArray = new byte[height][height][];
        int space = (height - width) / 2;
        for (int ROW = 0; ROW < height; ROW++) {
            for (int COL = 0; COL < height; COL++) {
                retArray[ROW][COL] = ImageUtils.WHITE_B;
            }
        }
        for (int ROW = 0; ROW < height; ROW++) {
            for (int COL = 0; COL < width; COL++) {
                retArray[ROW][COL + space] = tempImage[ROW][COL];
            }
        }
        byte[][][] borderedArray = new byte[height + 2 * thickness][height + 2 * thickness][];
        for (int ROW = 0; ROW < borderedArray.length; ROW++) {
            for (int COL = 0; COL < borderedArray[ROW].length; COL++) {
                borderedArray[ROW][COL] = borderColor;
            }
        }
        for (int ROW = 0; ROW < height; ROW++) {
            for (int COL = 0; COL < height; COL++) {
                borderedArray[ROW + thickness][COL + thickness] = retArray[ROW][COL];
            }
        }

        return borderedArray;
    }

    public static byte[][][] addBorder(byte[][][] retArray) {
        int thickness = 2;
        byte[][][] borderedArray = new byte[retArray.length + 2 * thickness][retArray[0].length + 2 * thickness][];
        for (int ROW = 0; ROW < borderedArray.length; ROW++) {
            for (int COL = 0; COL < borderedArray[ROW].length; COL++) {
                borderedArray[ROW][COL] = ImageUtils.BLACK_B;
            }
        }
        for (int ROW = 0; ROW < retArray.length; ROW++) {
            for (int COL = 0; COL < retArray[0].length; COL++) {
                borderedArray[ROW + thickness][COL + thickness] = retArray[ROW][COL];
            }
        }
        return borderedArray;
    }

    public static byte[][][] textImage(char inputText, int size, int CODEC, Color inputColor) {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        Font font = new Font("Arial", Font.PLAIN, size);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth("" + inputText);
        int height = fm.getHeight();
        g2d.dispose();

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setFont(font);
        fm = g2d.getFontMetrics();
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(inputColor);
        Color back_color_awt = ImageUtils.bleach(inputColor, 0.90f);
        byte[] back_color = ImageUtils.awtToByte(back_color_awt);
        AttributedString as1 = new AttributedString("" + inputText);
        as1.addAttribute(TextAttribute.BACKGROUND, back_color_awt);
        as1.addAttribute(TextAttribute.FONT, fm);
        as1.addAttribute(TextAttribute.SIZE, size);
        as1.addAttribute(TextAttribute.BACKGROUND, back_color_awt);
        g2d.drawString(as1.getIterator(), 0, fm.getAscent());
        g2d.dispose();

        byte[][][] tempImage = ImageUtils.bufferedImageToBytes(img, CODEC);

        byte[][][] retArray = new byte[height][height][];
        int space = (height - width) / 2;
        for (int ROW = 0; ROW < height; ROW++) {
            for (int COL = 0; COL < height; COL++) {
                retArray[ROW][COL] = back_color;
            }
        }
        for (int ROW = 0; ROW < height; ROW++) {
            for (int COL = 0; COL < width; COL++) {
                retArray[ROW][COL + space] = tempImage[ROW][COL];
            }
        }
        return retArray;
    }

    public static byte[][][] combineImages(byte[][][][][] inputImageArray) {
        int n = inputImageArray.length;
        int m = inputImageArray[0].length;
        int image_height = inputImageArray[0][0].length;
        int image_width = inputImageArray[0][0][0].length;
        int ret_width = n * image_width;
        int ret_height = m * image_height;
        byte[][][] retArray = new byte[ret_width][ret_height][];
        for (int IMAGE_ROW = 0; IMAGE_ROW < n; IMAGE_ROW++) {
            for (int IMAGE_COL = 0; IMAGE_COL < m; IMAGE_COL++) {
                byte[][][] currImage = inputImageArray[IMAGE_ROW][IMAGE_COL];
                int START_ROW = IMAGE_ROW * image_height;
                int START_COL = IMAGE_COL * image_width;
                for (int ROW = 0; ROW < image_height; ROW++) {
                    for (int COL = 0; COL < image_width; COL++) {
//                        System.out.printf("s_row:%s, s_col:%s\n", START_ROW + ROW, START_COL+COL);
                        retArray[START_ROW + ROW][START_COL + COL] = currImage[ROW][COL];
                    }
                }
            }
        }
        return retArray;
    }

    public static byte[][][] coloredASCII(byte[][][] inputImage, int CODEC) {
        int STRIDE = 4;
        int FONT_SIZE = 12;
        // Make the average color
        //byte[][][] avg_color = images.averageImage(inputImage, STRIDE);
        byte[][][] avg_color = inputImage;
        // Make the slant for the result
        char[][] avg_slant = ImageUtils.applySlant(avg_color);
        // Stitch the two together?
        int height = avg_slant.length, width = avg_slant[0].length;
        byte[][][][][] separate_images = new byte[height][width][][][];
        for (int ROW = 0; ROW < height; ROW++) {
            for (int COL = 0; COL < width; COL++) {
                byte[] avgColor = avg_color[ROW][COL];
                char currChar = avg_slant[ROW][COL];
                byte[][][] currImage = ImageUtils.textImage(currChar, FONT_SIZE, CODEC, ImageUtils.toAWTColor(avgColor));
                separate_images[ROW][COL] = currImage;
            }
        }
        byte[][][] combined = combineImages(separate_images);
        return combined;
    }

    public static BufferedImage[] coloredASCII(BufferedImage[] inputImages) {
        BufferedImage[] retArray = new BufferedImage[inputImages.length];
        for (int i = 0; i < retArray.length; i++) {
            BufferedImage from = inputImages[i];
            byte[][][] converted = ImageUtils.bufferedImageToBytes(from, from.getType());
            byte[][][] colored = ImageUtils.coloredASCII(converted, from.getType());
            BufferedImage to = ImageUtils.bytesToBufferedImage(colored);
            retArray[i] = to;
        }
        return retArray;
    }
// </editor-fold>

    // <editor-fold desc="Greyscale">
    private static final double RPERC = 0.30, GPERC = 0.59, BPERC = 0.11;

    public static int[][] toGreyscale(byte[][][] inputImage) {
        return toGreyscale(inputImage, RPERC, GPERC, BPERC);
    }

    public static int toGreyscale(int pixel) {
        return toGreyscale(new byte[][][]{{intToRGB(pixel)}})[0][0];
    }

    public static int[][] toGreyscale(byte[][][] inputImage, double r_perc, double g_perc, double b_perc) {
        return applyThreshold(inputImage, r_perc, g_perc, b_perc, 0.0, false);
    }

    public static byte[][][] toGreyscale_b(byte[][][] inputImage) {
        return intToRGB(toGreyscale(inputImage, RPERC, GPERC, BPERC));
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Load">
    public static List<byte[][][]> loadGif(String pathName) throws IOException {
        File file = new File(pathName);
        BufferedImage img = ImageIO.read(file);
        int width = img.getWidth(), height = img.getHeight();
        System.out.printf("GIF Loaded. width=%s \theight=%s\n", width, height);
        System.out.println("Sources:" + img.getSources());
        GifDecoder gd = new GifDecoder();
        gd.read(pathName);
        List<byte[][][]> retList = gifDecoderToBytes(gd);
        return retList;
    }

    public static BufferedImage[] loadGifBuffered(String pathName) throws IOException {
        File file = new File(pathName);
        BufferedImage img = ImageIO.read(file);
        int width = img.getWidth(), height = img.getHeight();
        System.out.printf("GIF Loaded. width=%s \theight=%s\n", width, height);
        System.out.println("Sources:" + img.getSources());
        GifDecoder gd = new GifDecoder();
        gd.read(pathName);
        return gifDecoderToBufferedImages(gd);
    }

    public static byte[][][] loadImageBytes(String pathName) throws IOException {
        String extension = FileUtils.getFileExtension(new File(pathName));
        switch (extension) {
            case "bmp":
                return loadImageToByteArray(pathName, BufferedImage.TYPE_INT_RGB);
            case "png":
                return loadImageToByteArray(pathName, BufferedImage.TYPE_INT_ARGB);
            default:
                System.out.printf("PATHNAME:%s \t EXTENSION:%s", pathName, extension);
                throw new IllegalArgumentException("Invalid path, must be of type BMP or PNG.");
        }
    }

    public static List<byte[][][]> loadImageBytes(List<String> locations) throws IOException {
        List<byte[][][]> retList = new ArrayList();
        for (String pathName : locations) {
            retList.add(loadImageBytes(pathName));
        }
        return retList;
    }

    public static BufferedImage loadImageBuffered(String pathName) throws IOException {
        System.out.println("load:" + pathName);
        BufferedImage img = ImageIO.read(new File(pathName));
        return img;
    }

    public static List<BufferedImage> loadImageBuffered(List<String> locations) throws IOException {
        List<BufferedImage> retList = new ArrayList();
        for (String pathName : locations) {
            retList.add(loadImageBuffered(pathName));
        }
        return retList;
    }

    public static byte[][][] loadImageToByteArray(String pathName, final int CODEC) throws IOException {
        BufferedImage img = ImageIO.read(new File(pathName));
        byte[][][] retMatrix = bufferedImageToBytes(img, CODEC);
        if (DEBUG) {
            System.out.println("Read success".toUpperCase());
        }
        return retMatrix;
    }

    public static int[][] loadImageToIntArray(String pathName, final int CODEC) throws IOException {
        BufferedImage img = ImageIO.read(new File(pathName));
        int width = img.getWidth(), height = img.getHeight();
        BufferedImage newImage = new BufferedImage(
                width, height, CODEC);

        Graphics2D graphic = newImage.createGraphics();
        graphic.drawImage(img, 0, 0, null);
        graphic.dispose();

        int[][] retMatrix = new int[height][width];
        int pixel = Integer.MIN_VALUE;
        for (int ROW_INDEX = 0; ROW_INDEX < height; ROW_INDEX++) {
            for (int COL_INDEX = 0; COL_INDEX < width; COL_INDEX++) {
                retMatrix[ROW_INDEX][COL_INDEX] = img.getRGB(COL_INDEX, ROW_INDEX);
            }
        }
        if (DEBUG) {
            System.out.println("Read success".toUpperCase());
        }
        return retMatrix;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Save">
    public static void saveImageBytes(byte[][][] pixels, String pathName) throws IOException {
        File file = new File(pathName);
        if(!file.exists()){
            file.getParentFile().mkdirs();
        }
        String extension = FileUtils.getFileExtension(new File(pathName));
        switch (extension) {
            case "bmp":
                saveImageFromByteArray(pixels, pathName, BufferedImage.TYPE_INT_RGB);
                break;
            case "png":
                saveImageFromByteArray(pixels, pathName, BufferedImage.TYPE_INT_ARGB);
                break;
            default:
                System.out.printf("PATHNAME:%s \t EXTENSION:%s\n", pathName, extension);
                throw new IllegalArgumentException("Invalid path, must be of type BMP or PNG.");
        }
    }

    public static void saveImageBytes(List<byte[][][]> inputImages, List<String> pathNames) throws IOException {
        if (inputImages.size() != pathNames.size()) {
            throw new IllegalArgumentException("image length != path length");
        }
        for (int i = 0; i < inputImages.size(); i++) {
            saveImageBytes(inputImages.get(i), pathNames.get(i));
        }
    }

    public static void saveImageBuffered(BufferedImage image, String pathName) throws IOException {
        File file = new File(pathName);
        if(!file.exists()){
            file.getParentFile().mkdirs();
        }
        String extension = FileUtils.getFileExtension(file);
        ImageIO.write(image, extension.toUpperCase(), file);
        if (DEBUG) {
            System.out.println("Write success".toUpperCase());
        }
    }

    public static void saveImageBuffered(List<BufferedImage> inputImages, List<String> pathNames) throws IOException {
        if (inputImages.size() != pathNames.size()) {
            throw new IllegalArgumentException("image length != path length");
        }
        for (int i = 0; i < inputImages.size(); i++) {
            saveImageBuffered(inputImages.get(i), pathNames.get(i));
        }

    }

    public static void saveImageFromByteArray(byte[][][] pixels, String pathName, final int CODEC) throws IOException {
        File file = new File(pathName);
        if(!file.exists()){
            file.getParentFile().mkdirs();
        }
        int height = pixels.length, width = pixels[0].length;
        BufferedImage bi = new BufferedImage(width, height, CODEC);
        for (int ROW_INDEX = 0; ROW_INDEX < height; ROW_INDEX++) {
            for (int COL_INDEX = 0; COL_INDEX < width; COL_INDEX++) {
                bi.setRGB(COL_INDEX, ROW_INDEX, rgbaToINT(pixels[ROW_INDEX][COL_INDEX]));
            }
        }
        String extension = FileUtils.getFileExtension(file);
        ImageIO.write(bi, extension.toUpperCase(), file);
        if (DEBUG) {
            System.out.println("Write success".toUpperCase());
        }
    }

    public static void saveImageFromIntArray(int[][] pixels, String pathName, final int CODEC) throws IOException {
        int height = pixels.length, width = pixels[0].length;
        BufferedImage bi = new BufferedImage(width, height, CODEC);
        for (int ROW_INDEX = 0; ROW_INDEX < height; ROW_INDEX++) {
            for (int COL_INDEX = 0; COL_INDEX < width; COL_INDEX++) {
                bi.setRGB(COL_INDEX, ROW_INDEX, pixels[ROW_INDEX][COL_INDEX]);
            }
        }
        File file = new File(pathName);
        String extension = FileUtils.getFileExtension(file);
        ImageIO.write(bi, extension.toUpperCase(), file);
        if (DEBUG) {
            System.out.println("Write success".toUpperCase());
        }
    }

    public static void saveGifBuffered(BufferedImage[] inputImages, GifDecoder gd, String pathName, int time, boolean loop) throws IOException {
        BufferedImage firstImage = inputImages[0];
        File file = new File(pathName);
        if(!file.exists()){
            file.getParentFile().mkdirs();
        }
        ImageOutputStream output = new FileImageOutputStream(file);
        GifSequenceWriter writer = new GifSequenceWriter(output, firstImage.getType(), time, loop);
        for (BufferedImage img : inputImages) {
            writer.writeToSequence(img);
        }
        writer.close();
        output.close();
    }

    public static void saveGifBuffered(BufferedImage[] inputImages, GifDecoder gd, String pathName) throws IOException {
        File file = new File(pathName);
        if(!file.exists()){
            file.getParentFile().mkdirs();
        }
        BufferedImage firstImage = inputImages[0];
        ImageOutputStream output = new FileImageOutputStream(new File(pathName));
        GifSequenceWriter writer = new GifSequenceWriter(output, firstImage.getType(), gd.getDelay(0), (gd.getLoopCount() == 0 ? true : false));
        for (BufferedImage img : inputImages) {
            writer.writeToSequence(img);
        }
        writer.close();
        output.close();
        System.out.println("WRITE SUCCESS");
    }
//</editor-fold>

    //<editor-fold desc="Conversions">
    public static byte[][][] intToRGB(int[][] input) {
        byte[][][] retArray = new byte[input.length][input[0].length][];
        for (int i = 0; i < input.length; i++) {
            retArray[i] = intToRGB(input[i]);
        }
        return retArray;
    }

    public static byte[][] intToRGB(int[] input) {
        byte[][] retArray = new byte[input.length][];
        for (int i = 0; i < input.length; i++) {
            retArray[i] = intToRGB(input[i]);
        }
        return retArray;
    }

    public static byte[] intToRGB(final int input) {
        return new byte[]{/*r*/(byte) ((input >> 16) & 0x000000FF), /*g*/ (byte) ((input >> 8) & 0x000000FF), /*b*/ (byte) (input & 0x000000FF)};
    }

    public static byte[] intToRGBA(final int input) {
        return new byte[]{/*r*/(byte) ((input >> 16) & 0x000000FF), /*g*/ (byte) ((input >> 8) & 0x000000FF), /*b*/ (byte) (input & 0x000000FF), /*a*/ (byte) ((input << 24) & 0x000000FF)};
    }

    public static int rgbaToINT(byte[] data) {
        return (((byte) (data.length == 4 ? data[3] : 0) << 24) + ((byte) data[0] << 16) + ((byte) data[1] << 8) + ((byte) data[2]));
    }

    public static int[] rgbaToINT(byte[][] data) {
        int[] retArray = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            retArray[i] = rgbaToINT(data[i]);
        }
        return retArray;
    }

    public static int[][] rgbaToINT(byte[][][] data) {
        int[][] retArray = new int[data.length][data[0].length];
        for (int i = 0; i < data.length; i++) {
            retArray[i] = rgbaToINT(data[i]);
        }
        return retArray;
    }

    public static BufferedImage bytesToBufferedImage(byte[][][] inputImage) {

        final int height = inputImage.length, width = inputImage[0].length, depth = inputImage[0][0].length;
        final int CODEC = (depth == 3 ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB);
        BufferedImage retImage = new BufferedImage(width, height, CODEC);
        byte[] inputLinear = new byte[height * width * depth];
        int count = 0;
        for (int ROW = 0; ROW < height; ROW++) {
            for (int COL = 0; COL < width; COL++) {
                retImage.setRGB(COL, ROW, rgbaToINT(inputImage[ROW][COL]));
            }
        }
        return retImage;
    }

    public static byte[][][] bufferedImageToBytes(BufferedImage img, final int CODEC) {
        int width = img.getWidth(), height = img.getHeight();
        BufferedImage newImage = new BufferedImage(
                width, height, CODEC);

        Graphics2D graphic = newImage.createGraphics();
        graphic.drawImage(img, 0, 0, null);
        graphic.dispose();

        byte[][][] retMatrix = new byte[height][width][CODEC];
        int pixel = Integer.MIN_VALUE;
        byte r, g, b, a;
        for (int ROW_INDEX = 0; ROW_INDEX < height; ROW_INDEX++) {
            for (int COL_INDEX = 0; COL_INDEX < width; COL_INDEX++) {
                pixel = img.getRGB(COL_INDEX, ROW_INDEX);
                // Bitmaps don't use ARGB model by default
                retMatrix[ROW_INDEX][COL_INDEX] = (CODEC == BufferedImage.TYPE_INT_RGB ? intToRGB(pixel) : intToRGBA(pixel));
            }
        }
        return retMatrix;
    }

    public static List<int[]> binaryToList(byte[][][] binaryImage) {
        List<int[]> retList = new ArrayList();
        for (int ROW = 0; ROW < binaryImage.length; ROW++) {
            for (int COL = 0; COL < binaryImage[0].length; COL++) {
                if (Arrays.equals(binaryImage[ROW][COL], new byte[]{BLACK, BLACK, BLACK})) {
                    retList.add(new int[]{COL, ROW});
                }
            }
        }
        return retList;
    }

    public static byte[][][] listToBinary(List<int[]> coordImage) {
        int max_height = 0, max_width = 0;
        for (int[] coord : coordImage) {
            max_width = Math.max(coord[0], max_width);
            max_height = Math.max(coord[1], max_height);
        }
        System.out.printf("mw:%s\tmh:%s\n", max_width, max_height);
        byte[][][] retImage = new byte[max_height + 1][max_width + 1][];
        for (int ROW = 0; ROW < max_height + 1; ROW++) {
            for (int COL = 0; COL < max_width + 1; COL++) {
                retImage[ROW][COL] = WHITE_B;
            }
        }
        for (int[] coord : coordImage) {
            retImage[coord[1]][coord[0]] = BLACK_B;
        }
        return retImage;
    }

    public static byte[] colorScaled(byte[] inputColor, double scale) {
        byte r = inputColor[0], g = inputColor[1], b = inputColor[2];
        Function<Byte, Byte> scaling = (input) -> {
            int converted_input = input;
            byte retByte = (byte) ((converted_input - 256) * scale + 256);
            return retByte;
        };
        byte[] retArray = new byte[]{scaling.apply(r), scaling.apply(g), scaling.apply(b)};
        return retArray;
    }

    public static Color colorScaled(Color inputColor, double scale) {
        float r = inputColor.getRed() / 256, g = inputColor.getGreen() / 256, b = inputColor.getBlue() / 256;
        Function<Float, Float> scaling = (input) -> {
            float retByte = (float) (Math.min(1.0f, input * scale));
            return retByte;
        };
        Color retColor = new Color((int) (255 * scaling.apply(r)), (int) (255 * scaling.apply(g)), (int) (255 * scaling.apply(b)));
        return retColor;
    }

    public static Color toAWTColor(byte[] inputColor) {
        Color retColor = null;
        int r = inputColor[0];
        int g = inputColor[1];
        int b = inputColor[2];
        if (r < 0) {
            r += 256;
        }
        if (r > 255) {
            r = 255;
        }
        if (g < 0) {
            g += 256;
        }
        if (g > 255) {
            g = 255;
        }
        if (b < 0) {
            b += 256;
        }
        if (b > 255) {
            b = 255;
        }
//        System.out.println("rgb:" + r + " " + g + " " + b);
        retColor = new Color(r, g, b);
        return retColor;
    }

    public static byte[] awtToByte(Color inputColor) {
        byte[] retArray = null;
        float r = inputColor.getRed(), g = inputColor.getGreen(), b = inputColor.getBlue(), a = inputColor.getAlpha();
        byte r_b = (byte) (r), g_b = (byte) (g), b_b = (byte) (b), a_b = (byte)(a);
        retArray = new byte[]{r_b, g_b, b_b, a_b};
        return retArray;
    }

    public static List<byte[][][]> gifDecoderToBytes(GifDecoder gd) {
        int frames = gd.getFrameCount();
        System.out.println("Frames:" + frames);
        BufferedImage[] images = new BufferedImage[frames];
        for (int frame_index = 0; frame_index < frames; frame_index++) {
            images[frame_index] = gd.getFrame(frame_index);
        }
        List<byte[][][]> retList = new ArrayList();
        int type = BufferedImage.TYPE_INT_ARGB;
        for (BufferedImage frame : images) {
            retList.add(bufferedImageToBytes(frame, frame.getType()));
        }
        return retList;
    }

    public static BufferedImage[] gifDecoderToBufferedImages(GifDecoder gd) {
        int frames = gd.getFrameCount();
        System.out.println("Frames:" + frames);
        BufferedImage[] images = new BufferedImage[frames];
        for (int frame_index = 0; frame_index < frames; frame_index++) {
            images[frame_index] = gd.getFrame(frame_index);
        }
        return images;
    }
//</editor-fold>

    public static byte[][][][] splitRGB(byte[][][] inputImage) {
        byte[][][] r_img = new byte[inputImage.length][inputImage[0].length][], g_img = new byte[inputImage.length][inputImage[0].length][], b_img = new byte[inputImage.length][inputImage[0].length][];
        byte[][][][] retImages = new byte[3][][][];
        for (int ROW = 0; ROW < inputImage.length; ROW++) {
            for (int COL = 0; COL < inputImage[0].length; COL++) {
                byte[] pixel = inputImage[ROW][COL];
                int r = pixel[0];
                r %= 256;
                int g = pixel[1];
                if (g < 0) {
                    g += 256;
                }
                if (g > 256) {
                    g -= 256;
                }
                int b = pixel[2];
                b %= 256;
                r_img[ROW][COL] = new byte[]{(byte) r, 1, 1};
                g_img[ROW][COL] = new byte[]{1, (byte) g, 1};
                b_img[ROW][COL] = new byte[]{1, 1, (byte) b};
            }
        }
        retImages[0] = r_img;
        retImages[1] = g_img;
        retImages[2] = b_img;
        return retImages;
    }

    public static Image createJavaFXImage(int size, javafx.scene.paint.Color color){
        WritableImage wr = new WritableImage(size, size);
        PixelWriter pw = wr.getPixelWriter();
        for(int x = 0; x < size; x++){
            for(int y = 0; y < size; y++){
                pw.setColor(x,y,color);
            }
        }
        return wr;
    }

    public static byte[][][] fillImage(byte[][][] inputImage, Color color){
        byte[][][] retImage = new byte[inputImage.length][inputImage[0].length][];
        byte[] pixel = awtToByte(color);
        for(int ROW = 0; ROW < inputImage.length; ROW++){
            for (int COL = 0; COL < inputImage[0].length; COL++){
                retImage[ROW][COL] = pixel;
            }
        }
        return retImage;
    }

    public static BufferedImage fillImage(BufferedImage inputImage, Color color){
        for(int ROW = 0; ROW < inputImage.getHeight(); ROW++){
            for (int COL = 0; COL < inputImage.getWidth(); COL++){
                inputImage.setRGB(COL, ROW, color.getRGB());
            }
        }
        return inputImage;
    }

    public static BufferedImage drawBox(BufferedImage inputImage, Color drawColor, boolean fill, int[] ROW_BOUNDS, int[] COL_BOUNDS){
        int ROW_MIN = Math.min(ROW_BOUNDS[0], ROW_BOUNDS[1]);
        int ROW_MAX = Math.max(ROW_BOUNDS[0], ROW_BOUNDS[1]);
        int COL_MIN = Math.min(COL_BOUNDS[0], COL_BOUNDS[1]);
        int COL_MAX = Math.max(COL_BOUNDS[0], COL_BOUNDS[1]);
        byte[] pixel = awtToByte(drawColor);
        for(int ROW = 0; ROW < inputImage.getHeight(); ROW++){
            for (int COL = 0; COL < inputImage.getWidth(); COL++){
                if(fill){
                    if(ROW >= ROW_MIN && ROW <= ROW_MAX && COL >= COL_MIN && COL <= COL_MAX){
                        inputImage.setRGB(COL, ROW, drawColor.getRGB());
                    }
                }else{
                    if((ROW == ROW_MIN || ROW == ROW_MAX) && COL >= COL_MIN && COL <= COL_MAX){
                        inputImage.setRGB(COL, ROW, drawColor.getRGB());
                    }else if((COL == COL_MIN || COL == COL_MAX) && ROW >= ROW_MIN && ROW <= ROW_MAX){
                        inputImage.setRGB(COL, ROW, drawColor.getRGB());
                    }
                }
            }
        }
        return inputImage;
    }

    public static byte[][][] drawBox(byte[][][] inputImage, Color drawColor, boolean fill, int[] ROW_BOUNDS, int[] COL_BOUNDS){
        byte[][][] retImage = new byte[inputImage.length][inputImage[0].length][];
        int ROW_MIN = Math.min(ROW_BOUNDS[0], ROW_BOUNDS[1]);
        int ROW_MAX = Math.max(ROW_BOUNDS[0], ROW_BOUNDS[1]);
        int COL_MIN = Math.min(COL_BOUNDS[0], COL_BOUNDS[1]);
        int COL_MAX = Math.max(COL_BOUNDS[0], COL_BOUNDS[1]);
        byte[] pixel = awtToByte(drawColor);
        for(int ROW = 0; ROW < inputImage.length; ROW++){
            for (int COL = 0; COL < inputImage[0].length; COL++){
                retImage[ROW][COL] = inputImage[ROW][COL];
            }
        }
        for(int ROW = 0; ROW < inputImage.length; ROW++){
            for (int COL = 0; COL < inputImage[0].length; COL++){
                if(fill){
                    if(ROW >= ROW_MIN && ROW <= ROW_MAX && COL >= COL_MIN && COL <= COL_MAX){
                        retImage[ROW][COL] = pixel;
                    }
                }else{
                    if((ROW == ROW_MIN || ROW == ROW_MAX) && COL >= COL_MIN && COL <= COL_MAX){
                        retImage[ROW][COL] = pixel;
                    }else if((COL == COL_MIN || COL == COL_MAX) && ROW >= ROW_MIN && ROW <= ROW_MAX){
                        retImage[ROW][COL] = pixel;
                    }
                }
            }
        }
        return retImage;
    }

    public static byte[] getColorSliding(double d) {
        if (d > 1.0) {
            d = 1.0;
        }
        if (d < 0.0) {
            d = 0.0;
        }
        double val = 120;
        return getColor(d * val + 360.0 - val);
    }

    public static byte[] getColor(double theta) {
        List<Pair<Double, int[]>> pairlist = new ArrayList();
        pairlist.add(new Pair(0.0, new int[]{255, 0, 0}));
        pairlist.add(new Pair(60.0, new int[]{255, 0, 255}));
        pairlist.add(new Pair(120.0, new int[]{0, 0, 255}));
        pairlist.add(new Pair(180.0, new int[]{0, 255, 255}));
        pairlist.add(new Pair(240.0, new int[]{0, 255, 0}));
        pairlist.add(new Pair(300.0, new int[]{255, 255, 0}));
        pairlist.add(new Pair(360.0, new int[]{255, 0, 0}));
        if (theta > 360.0) {
            theta -= 360.0;
        }
        if (theta < 0) {
            theta += 360.0;
        }
        for (int i = 0; i < pairlist.size() - 1; i++) {
            Pair<Double, int[]> pair1 = pairlist.get(i), pair2 = pairlist.get(i + 1);
            if (theta >= pair1.getKey() && theta <= pair2.getKey()) {
                int[] final_color = pair2.getValue();
                int[] initial_color = pair1.getValue();
                double percentage = (theta - pair1.getKey()) / (pair2.getKey() - pair1.getKey());
                int[] new_color = new int[3];
                byte[] ret_color = new byte[3];
                for (int j = 0; j < 3; j++) {
                    new_color[j] = (int) (percentage * (final_color[j] - initial_color[j]) + initial_color[j]);
                    ret_color[j] = (byte) new_color[j];
                }
                return ret_color;
            }
        }
        return new byte[]{(byte) 255, 0, 0};

    }
}
