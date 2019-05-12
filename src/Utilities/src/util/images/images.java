package util.images;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javafx.util.Callback;
import javax.imageio.ImageIO;
import util.objects.arrays;
import util.objects.files;
import util.math.matrices;

/**
 *
 * @author Mike
 */
public final class images {

    private static final boolean DEBUG = true;
    private static final int BLACK = 0x00000000, WHITE = 0xffffffff;
    public static final byte[] BLACK_B = new byte[]{BLACK, BLACK, BLACK}, WHITE_B = new byte[]{(byte) WHITE, (byte) WHITE, (byte) WHITE};

    private images() {

    }

    public static final class strel {

        private final int[][] int_representation;
        private final int[][][] vec_representation;
        private final int center_x, center_y;

        private strel(int[][] int_representation, int center_x, int center_y) {
            this.int_representation = int_representation;
            this.center_x = center_x;
            this.center_y = center_y;
            // System.out.printf("x:%s\ty:%s\n", center_x, center_y);
            this.vec_representation = new int[this.int_representation.length][this.int_representation[0].length][2];
            for (int ROW = 0; ROW < this.int_representation.length; ROW++) {
                for (int COL = 0; COL < this.int_representation[0].length; COL++) {
                    this.vec_representation[ROW][COL] = (this.int_representation[ROW][COL] == 1 ? new int[]{COL - center_x, ROW - center_y} : null);
                }
            }
        }

        public void print() {
            for (int i = 0; i < this.int_representation.length; i++) {
                System.out.println(Arrays.toString(this.int_representation[i]));
            }
        }

        public void printVec() {
            for (int ROW = 0; ROW < this.vec_representation.length; ROW++) {
                System.out.println(Arrays.deepToString(this.vec_representation[ROW]));
            }

        }

        public int[][][] apply_vec_representation(int[] coord) {
            int[][][] retArray = new int[this.vec_representation.length][this.vec_representation[0].length][];
            for (int ROW = 0; ROW < this.vec_representation.length; ROW++) {
                for (int COL = 0; COL < this.vec_representation[0].length; COL++) {
                    int[] currVec = this.vec_representation[ROW][COL];
                    retArray[ROW][COL] = (currVec != null ? new int[]{coord[0] + currVec[0], coord[1] + currVec[1]} : null);
                }
            }
            return retArray;
        }

        public static strel square(int size) {
            size = (size % 2 == 0 ? size + 1 : size);
            int[][] temp_rep = new int[size][size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    temp_rep[i][j] = 1;
                }
            }
            return new strel(temp_rep, size / 2, size / 2);
        }

        public static strel diamond(int size) {
            size = (size % 2 == 0 ? size + 1 : size);
            double[] center = new double[]{size / 2, size / 2};
            int[][] temp_rep = new int[size][size];
            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    double dist = Math.abs(row - center[1]) + Math.abs(col - center[0]);
                    temp_rep[row][col] = (dist > size / 2 ? 0 : 1);
                }
            }
            return new strel(temp_rep, size / 2, size / 2);
        }

        public static strel circle(int radius) {
            int[][] temp_rep = new int[2 * radius + 1][2 * radius + 1];
            double[] center = new double[]{(2 * radius + 1) / 2, (2 * radius + 1) / 2};
            for (int row = 0; row < temp_rep.length; row++) {
                for (int col = 0; col < temp_rep[0].length; col++) {
                    double dist = Math.sqrt(Math.pow(center[1] - row, 2) + Math.pow(center[0] - col, 2));
                    temp_rep[row][col] = (dist > radius ? 0 : 1);
                }
            }
            return new strel(temp_rep, (2 * radius + 1) / 2, (2 * radius + 1) / 2);
        }

        public static strel rect(int height, int width, int c_x, int c_y) {
            int[][] temp_rep = new int[height][width];
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    temp_rep[row][col] = 1;
                }
            }
            return new strel(temp_rep, c_x, c_y);
        }

    }

    public static List<byte[][][]> detectionWindow(byte[][][] inputImage, int[] dim, int... offsets) {
        List<byte[][][]> retImages = new ArrayList();
        int y_offset = (offsets.length > 0 ? offsets[0] : 1);
        int x_offset = (offsets.length > 1 ? offsets[1] : 1);
        for (int ROW = 0; ROW < inputImage.length - dim[1] + 1; ROW += y_offset) {
            for (int COL = 0; COL < inputImage[0].length - dim[0] + 1; COL += x_offset) {
                byte[][][] currImage = arrays.subset(inputImage, ROW, COL, dim[1], dim[0], BLACK_B);
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
                byte[][][] currImage = arrays.subset(inputImage, ROW, COL, dim[1], dim[0], BLACK_B);
                results.add(callback.call(currImage));
                System.out.println("row:" + ROW + "\tcol:" + COL);
            }
        }
        return results;
    }

    public static double interpolateLinear(double t_0, double L_0, double t_1, double L_1, double t) {
        double delta_t = (t - t_0) / (t_1 - t_0);
        double L_t = (L_1 - L_0) * delta_t + L_0;
        return L_t;
    }

    //<editor-fold defaultstate="collapsed" desc="Resize">
    public static byte[][][] resizeNearest(byte[][][] inputImage, int[] newRes) {
        byte[][][] retImage = images.solidRect(newRes[1], newRes[0], BLACK_B);
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

    //<editor-fold defaultstate="collapsed" desc="Histogram of Oriented Gradients">
    public static final class HOG {

        public final int[][] positive_representation, negative_representation;
        public final Cell[][] cells;
        public final Block[][] blocks;

        public HOG(int[][] positive_representation, int[][] negative_representation, Cell[][] cells, Block[][] blocks) {
            this.positive_representation = positive_representation;
            this.negative_representation = negative_representation;
            this.cells = cells;
            this.blocks = blocks;
        }

        public static class Cell {

            final int[] histogram;

            public Cell(int[] histogram) {
                this.histogram = histogram;
            }

            @Override
            public String toString() {
                return "Cell{" + "histogram=" + Arrays.toString(histogram) + '}';
            }

        }

        public static class Block {

            final Cell[][] cells;

            public Block(Cell[][] cells) {
                this.cells = cells;
            }

            public int[] histogramVector() {
                int[] retArray = new int[cells.length * cells[0].length * cells[0][0].histogram.length];
                int index = 0;
                for (int ROW = 0; ROW < cells.length; ROW++) {
                    for (int COL = 0; COL < cells[0].length; COL++) {
                        for (int HIST = 0; HIST < cells[0][0].histogram.length; HIST++) {
                            retArray[index] = cells[ROW][COL].histogram[HIST];
                            index++;
                        }
                    }
                }
                return retArray;
            }

            @Override
            public String toString() {
                return "Block{" + "histVector=" + Arrays.toString(this.histogramVector()) + '}';
            }

        }

        public int[] featureVector() {
            int[] retVector = new int[blocks[0][0].histogramVector().length * blocks.length * blocks[0].length];
            int counter = 0;
            for (int ROW = 0; ROW < blocks.length; ROW++) {
                for (int COL = 0; COL < blocks[0].length; COL++) {
                    int[] blockVec = blocks[ROW][COL].histogramVector();
                    for (int i = 0; i < blockVec.length; i++) {
                        retVector[counter] = blockVec[i];
                        counter++;
                    }
                }
            }
            return retVector;
        }

        public static int[] featureVectorFromRGB(byte[][][] image) {
            HOG temp = images.hogRepresentation(image);
            return temp.featureVector();
        }
    }

    public static class Gradient {

        public final double G_x, G_y;
        public final double theta, mag;

        public Gradient(double G_x, double G_y, double theta, double mag) {
            this.G_x = G_x;
            this.G_y = G_y;
            this.theta = theta;
            this.mag = mag;
        }

        public double positiveTheta() {
            if (theta < 0) {
                return theta + Math.PI;
            }
            return theta;
        }

        public double positiveDegree() {
            return this.positiveTheta() * 180 / Math.PI;
        }

        @Override
        public String toString() {
            return "Gradient{" + "G_x=" + G_x + ", G_y=" + G_y + ", theta=" + theta + ", mag=" + mag + '}';
        }

    }

    public static Gradient[][] resizeNearestG(Gradient[][] inputGradients, int[] newRes) {
        Gradient[][] retGradients = new Gradient[newRes[1]][newRes[0]];
        double y_scalar = inputGradients.length * (1.0 / retGradients.length);
        double x_scalar = inputGradients[0].length * (1.0 / retGradients[0].length);
        System.out.println("scalars:" + y_scalar + " " + x_scalar);
        for (int ROW = 0; ROW < retGradients.length; ROW++) {
            for (int COL = 0; COL < retGradients[0].length; COL++) {
                int yVal = (int) (ROW * y_scalar);
                int xVal = (int) (COL * x_scalar);
                retGradients[ROW][COL] = inputGradients[yVal][xVal];
            }
        }
        return retGradients;
    }

    public static void generateHOG(String root, String inputDirName) throws IOException {
        // make output files in root
        String neg_rep_dir = root + "negative_representations/", pos_rep_dir = root + "positive_representations/";
        File negDir = new File(neg_rep_dir);
        File posDir = new File(pos_rep_dir);
        negDir.mkdirs();
        posDir.mkdirs();
        String fullInputDirectory = root + inputDirName;
        System.out.println(fullInputDirectory);
        Map<String, List<String>> fileMap = files.getFileExtensionNameMap(fullInputDirectory);
        System.out.println(fileMap);
        String[] names = files.getNames(fullInputDirectory, "bmp");
        String[] locs = files.getLocations(fullInputDirectory, "bmp");
        System.out.println(Arrays.toString(locs));
        HOG[] hogs = new HOG[locs.length];
        for (int LOC_INDEX = 0; LOC_INDEX < locs.length; LOC_INDEX++) {
            byte[][][] original = images.loadImageBytes(locs[LOC_INDEX]);
            hogs[LOC_INDEX] = hogRepresentation(original);
            images.saveImageFromIntArray(hogs[LOC_INDEX].negative_representation, neg_rep_dir + names[LOC_INDEX], BufferedImage.TYPE_INT_RGB);
            images.saveImageFromIntArray(hogs[LOC_INDEX].positive_representation, pos_rep_dir + names[LOC_INDEX], BufferedImage.TYPE_INT_RGB);
        }
    }

    public static HOG hogRepresentation(byte[][][] original) {
        Gradient[][] gradients = images.findGradientsSplit(original);
        return gradientsToHOG(gradients);

    }

    public static HOG gradientsToHOG(Gradient[][] gradients) {
        double[][] thetas = new double[gradients.length][gradients[0].length];
        double[][] degrees = new double[gradients.length][gradients[0].length];
        for (int ROW = 0; ROW < gradients.length; ROW++) {
            for (int COL = 0; COL < gradients[0].length; COL++) {
                thetas[ROW][COL] = gradients[ROW][COL].positiveTheta();
                degrees[ROW][COL] = gradients[ROW][COL].positiveDegree();
            }
        }

        int cell_size = 8;
        HOG.Cell[][] cells = new HOG.Cell[gradients.length / cell_size][gradients[0].length / cell_size];
        for (int CELL_ROW = 0; CELL_ROW < cells.length; CELL_ROW++) {
            for (int CELL_COL = 0; CELL_COL < cells[0].length; CELL_COL++) {
                Gradient[][] sub = arrays.subset(gradients, CELL_ROW * cell_size, CELL_COL * cell_size, cell_size, cell_size, new Gradient(-1, -1, -1, -1));
                int[] histogram = images.applyHistogram(sub);
                cells[CELL_ROW][CELL_COL] = new HOG.Cell(histogram);
            }
        }
        int block_size = 2;
        HOG.Block[][] blocks = new HOG.Block[cells.length - block_size + 1][cells[0].length - block_size + 1];
        for (int ROW = 0; ROW < blocks.length; ROW++) {
            for (int COL = 0; COL < blocks[0].length; COL++) {
                HOG.Cell[][] sub = arrays.subset(cells, ROW, COL, block_size, block_size, new HOG.Cell(null));
                blocks[ROW][COL] = new HOG.Block(sub);
                // System.out.println(blocks[ROW][COL].histogramVector().length);
            }
        }

        int dim = 9;
        strel square = strel.square(dim);
        int[][] retImagePOS = new int[cells.length * dim][cells[0].length * dim];
        int[][] retImageNEG = new int[cells.length * dim][cells[0].length * dim];
        for (int CELL_ROW = 0; CELL_ROW < cells.length; CELL_ROW++) {
            for (int CELL_COL = 0; CELL_COL < cells[0].length; CELL_COL++) {

                byte[][][][] h_images_POS = images.histogramToImages(cells[CELL_ROW][CELL_COL].histogram);
                byte[][][][] h_images_NEG = images.histogramToImages(cells[CELL_ROW][CELL_COL].histogram, -1.0);
                byte[][][] combined_POS = images.add(h_images_POS);
                byte[][][] combined_NEG = images.add(h_images_NEG);
                int[][] int_combined_POS = images.rgbaToINT(combined_POS);
                int[][] int_combined_NEG = images.rgbaToINT(combined_NEG);
                retImagePOS = images.insert(retImagePOS, int_combined_POS, square, CELL_COL * dim, CELL_ROW * dim);
                retImageNEG = images.insert(retImageNEG, int_combined_NEG, square, CELL_COL * dim, CELL_ROW * dim);
            }
        }
        HOG retHOG = new HOG(retImagePOS, retImageNEG, cells, blocks);
        // retImage = insert(retImage, intLine, square, x_ins, y_ins);
        return retHOG;
    }

    public static byte[][][][] histogramToImages(int[] histogram, double... slopeMod) {
        double sum = 0.0;
        for (int i : histogram) {
            sum += i;
        }
        double[] normalized = new double[histogram.length];
        byte[][][][] lineImages = new byte[normalized.length][][][];
        for (int i = 0; i < histogram.length; i++) {
            normalized[i] = histogram[i] / sum;
            double theta = Math.PI * i / histogram.length;
            double slope = Math.tan(theta);
            int grey = (int) (normalized[i] * 255);
            lineImages[i] = images.slopeLinePixel((slopeMod.length > 0 ? slopeMod[0] : 1.0) * slope, histogram.length, new byte[]{(byte) grey, (byte) grey, (byte) grey}, false);
        }
        return lineImages;
    }

    public static Gradient[][] findGradientsSplit(byte[][][] inputImage, double... optScalar) {
        byte[][][][] split = images.splitRGB(inputImage);
        int[][][] splitInt = new int[split.length][][];
        for (int i = 0; i < split.length; i++) {
            splitInt[i] = images.rgbaToINT(split[i]);
        }
        strel structure_x = strel.rect(3, 1, 0, 1);
//        structure_x.print();
//        structure_x.printVec();

        // {{1}, {c}, {1}}
        strel structure_y = strel.rect(1, 3, 1, 0);
//        structure_y.print();
//        structure_y.printVec();

        double[][] filter_x = {{-1}, {0}, {1}};
        double[][] filter_y = {{-1, 0, 1}};

        Gradient[][] slope_field = images.findGradientsMax(splitInt, structure_x, structure_y, filter_x, filter_y, 1.0);
        return slope_field;
    }

    public static Gradient[][] findGradientsMax(int[][][] inputImages, strel structure_x, strel structure_y, double[][] filter_x, double[][] filter_y, double... optScalar) {
        // Find gradients for all input images
        Gradient[][][] gradient_images = new Gradient[inputImages.length][][];
        Gradient[][] slope_field = new Gradient[inputImages[0].length][inputImages[0][0].length];
        for (int IMAGE_INDEX = 0; IMAGE_INDEX < inputImages.length; IMAGE_INDEX++) {
            gradient_images[IMAGE_INDEX] = findGradients(inputImages[IMAGE_INDEX], structure_x, structure_y, filter_x, filter_y, IMAGE_INDEX, optScalar);
        }
        // return new array that is the max of all Gx, Gy, calculate new gradients
        for (int ROW = 0; ROW < gradient_images[0].length; ROW++) {
            for (int COL = 0; COL < gradient_images[0][0].length; COL++) {
                double max_x = 0, max_y = 0;
                for (int IMAGE_INDEX = 0; IMAGE_INDEX < gradient_images.length; IMAGE_INDEX++) {
                    Gradient curr = gradient_images[IMAGE_INDEX][ROW][COL];
                    if (Math.abs(curr.G_x) > Math.abs(max_x)) {
                        max_x = curr.G_x;
                    }
                    if (Math.abs(curr.G_y) > Math.abs(max_y)) {
                        max_y = curr.G_y;
                    }
                }
                double theta = Math.atan(max_y / max_x);
                int mag = (int) Math.sqrt(max_x * max_x + max_y * max_y);
                slope_field[ROW][COL] = new Gradient(max_x, max_y, theta, mag);

            }
        }
        return slope_field;

    }

    public static Gradient[][] findGradientsAvg(int[][][] inputImages, strel structure_x, strel structure_y, double[][] filter_x, double[][] filter_y, double... optScalar) {
        // Find gradients for all input images
        Gradient[][][] gradient_images = new Gradient[inputImages.length][][];
        Gradient[][] slope_field = new Gradient[inputImages[0].length][inputImages[0][0].length];
        for (int IMAGE_INDEX = 0; IMAGE_INDEX < inputImages.length; IMAGE_INDEX++) {
            gradient_images[IMAGE_INDEX] = findGradients(inputImages[IMAGE_INDEX], structure_x, structure_y, filter_x, filter_y, IMAGE_INDEX, optScalar);
        }
        // return new array that is the max of all Gx, Gy, calculate new gradients
        for (int ROW = 0; ROW < gradient_images[0].length; ROW++) {
            for (int COL = 0; COL < gradient_images[0][0].length; COL++) {
                double sum_x = 0, sum_y = 0;
                for (int IMAGE_INDEX = 0; IMAGE_INDEX < gradient_images.length; IMAGE_INDEX++) {
                    Gradient curr = gradient_images[IMAGE_INDEX][ROW][COL];
                    sum_x += curr.G_x;
                    sum_y += curr.G_y;
                }
                double avg_x = sum_x / gradient_images.length, avg_y = sum_y / gradient_images.length;
                double theta = Math.atan(avg_y / avg_x);
                int mag = (int) Math.sqrt(avg_x * avg_x + avg_y * avg_y);
                slope_field[ROW][COL] = new Gradient(avg_x, avg_y, theta, mag);

            }
        }
        return slope_field;

    }

    public static Gradient[][] findGradients(int[][] inputImage, strel structure_x, strel structure_y, double[][] filter_x, double[][] filter_y, double... optScalar) {
        Gradient[][] slope_field = findGradients(inputImage, structure_x, structure_y, filter_x, filter_y, 0, optScalar);
        return slope_field;
    }

    public static Gradient[][] findGradients(int[][] inputImage, strel structure_x, strel structure_y, double[][] filter_x, double[][] filter_y, int pixelIndex, double... optScalar) {
        Gradient[][] slope_field = new Gradient[inputImage.length][inputImage[0].length];
        double scalar = (optScalar.length > 0 ? optScalar[0] : 1.0);
        // Fill outside.
        final Gradient ZERO = new Gradient(0, 0, 0, 0);
        for (int ROW = 0; ROW < inputImage.length; ROW++) {
            slope_field[ROW][0] = ZERO;
            slope_field[ROW][inputImage[0].length - 1] = ZERO;
        }
        for (int COL = 0; COL < inputImage[0].length; COL++) {
            slope_field[0][COL] = ZERO;
            slope_field[inputImage.length - 1][COL] = ZERO;
        }
        for (int ROW = 1; ROW < inputImage.length - 1; ROW++) {
            for (int COL = 1; COL < inputImage[0].length - 1; COL++) {
                int[][][] surr_coord_x = structure_x.apply_vec_representation(new int[]{COL, ROW});
                int[][][] surr_coord_y = structure_y.apply_vec_representation(new int[]{COL, ROW});
                // For accuracy
                double[][] surr_pixels_x = new double[surr_coord_x.length][surr_coord_x[0].length];
                double[][] surr_pixels_y = new double[surr_coord_y.length][surr_coord_y[0].length];
                for (int i = 0; i < surr_coord_x.length; i++) {
                    for (int j = 0; j < surr_coord_x[0].length; j++) {
                        int x_index = surr_coord_x[i][j][0], y_index = surr_coord_x[i][j][1];
                        byte[] pixel = images.intToRGB(inputImage[y_index][x_index]);
                        int identifier = pixel[pixelIndex];
                        surr_pixels_x[i][j] = identifier;
                    }
                }
                for (int i = 0; i < surr_coord_y.length; i++) {
                    for (int j = 0; j < surr_coord_y[0].length; j++) {
                        int x_index = surr_coord_y[i][j][0], y_index = surr_coord_y[i][j][1];
                        byte[] pixel = images.intToRGB(inputImage[y_index][x_index]);
                        int identifier = pixel[pixelIndex];
                        surr_pixels_y[i][j] = identifier;
                    }
                }

                double[][] x_applied = matrices.hadamard(surr_pixels_x, filter_x);
                double G_x = matrices.sigma(x_applied) / scalar;
                double[][] y_applied = matrices.hadamard(surr_pixels_y, filter_y);
                double G_y = matrices.sigma(y_applied) / scalar;
                double theta = Math.atan(G_y / G_x);
                int mag = (int) Math.sqrt(G_x * G_x + G_y * G_y);
                slope_field[ROW][COL] = new Gradient(G_x, G_y, theta, mag);
            }
        }
        return slope_field;
    }
//</editor-fold>

    //<editor-fold desc="Operations">
    public static byte[][][] add(byte[][][][] inputImages) {
        byte[][][] retImage = solidRect(inputImages[0].length, inputImages[0][0].length, images.BLACK_B);
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

    public static byte[][][] erodeBinary(byte[][][] binaryImage, strel B) {
        byte[][][] retImage = new byte[binaryImage.length][binaryImage[0].length][];
        for (int image_ROW = 0; image_ROW < binaryImage.length; image_ROW++) {
            for (int image_COL = 0; image_COL < binaryImage[0].length; image_COL++) {
                int[][][] coordCheck = B.apply_vec_representation(new int[]{image_COL, image_ROW});

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

    public static byte[][][] dilateBinary(byte[][][] binaryImage, strel B) {
        byte[][][] retImage = new byte[binaryImage.length][binaryImage[0].length][];
        for (int image_ROW = 0; image_ROW < binaryImage.length; image_ROW++) {
            for (int image_COL = 0; image_COL < binaryImage[0].length; image_COL++) {
                retImage[image_ROW][image_COL] = WHITE_B;
                if (Arrays.equals(binaryImage[image_ROW][image_COL], BLACK_B)) {
                    int[][][] coordCheck = B.apply_vec_representation(new int[]{image_COL, image_ROW});
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

    public static byte[][][] openBinary(byte[][][] binaryImage, strel B) {
        return dilateBinary(erodeBinary(binaryImage, B), B);
    }

    public static byte[][][] closeBinary(byte[][][] binaryImage, strel B) {
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
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Apply">
    public static int[] applyHistogram(Gradient[][] gradients) {
        // min = 0.0, max = 180.0
        // binNum = 9
        // always apply some splitting to the gradients
        // locations based on positiveThetas and frequencies based on magnitude.
        // divide theta by binSize to see what index, fractional part scales between two bins.

        double min_val = -20.0, max_val = 200.0;
        int number_of_bins = 9;
        int[] tempArray = new int[11];
        double binsize = (max_val - min_val) / tempArray.length;
        // System.out.println("BINSIZE:" + binsize);
        for (int ROW = 0; ROW < gradients.length; ROW++) {
            for (int COL = 0; COL < gradients[0].length; COL++) {
                Gradient curr = gradients[ROW][COL];
                double theta = curr.positiveDegree();
                for (int i = 0; i < tempArray.length; i++) {
                    double right = min_val + (i + 1.5) * binsize;
                    if (theta < right) {
                        // theta belongs in bin i
                        double left = (min_val + (i + 0.5) * binsize);
                        double delta = theta - left;
                        double perc = delta / binsize;
                        double amt_r = perc * curr.mag;
                        double amt_l = (1 - perc) * curr.mag;
                        // System.out.println("delta:" + delta + "\tperc:" + perc + "\ti:" + i  + "\tleft:" + left+ "\tcurr:" + theta + "\tright:" + right + "\tamtL:" + amt_l + "\tamtR:" + amt_r);

                        if (i + 1 < tempArray.length) {
                            tempArray[i] += (int) amt_l;
                            tempArray[i + 1] += (int) amt_r;
                        } else {
                            tempArray[i] += (int) curr.mag;
                        }
                        break;
                    }
                }
            }
        }
        // System.out.println("temp:" + Arrays.toString(tempArray));
        int[] retArray = new int[number_of_bins];
        tempArray[1] += tempArray[0];
        tempArray[tempArray.length - 2] += tempArray[tempArray.length - 1];
        for (int i = 1; i < tempArray.length - 1; i++) {
            retArray[i - 1] = tempArray[i];
        }

        return retArray;
    }

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
        strel structure_x = strel.square(3), structure_y = structure_x;

        Gradient[][] slope_field = findGradients(inputImage, structure_x, structure_y, filter_x, filter_y, (div.length > 0 ? div[0] : 1.0));

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
                retImage[ROW][COL] = images.rgbaToINT(pixel);
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
        strel structure_x = strel.square(3), structure_y = structure_x;

        Gradient[][] slope_field = findGradientsMax(inputImages, structure_x, structure_y, filter_x, filter_y, (div.length > 0 ? div[0] : 1.0));

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
                retImage[ROW][COL] = images.rgbaToINT(pixel);
            }
        }

        return retImage;

    }

    // apply edges using regular gradients / kernals
    public static int[][] applySlopefield(int[][] inputImage, double scalar, boolean... debug) {
        int[][] retImage;
        // {{1,c,1}}
        strel structure_x = strel.rect(3, 1, 0, 1);
//        structure_x.print();
//        structure_x.printVec();

// {{1}, {c}, {1}}
        strel structure_y = strel.rect(1, 3, 1, 0);
//        structure_y.print();
//        structure_y.printVec();

        double[][] filter_x = {{-1}, {0}, {1}};
        double[][] filter_y = {{-1, 0, 1}};

        Gradient[][] slope_field = findGradients(inputImage, structure_x, structure_y, filter_x, filter_y, 0, scalar);

        int dim = 7;
        int thresh = 10;
        strel square = strel.square(dim);
        retImage = new int[inputImage.length * dim][inputImage[0].length * dim];

        for (int ROW = 0; ROW < slope_field.length; ROW++) {
            for (int COL = 0; COL < slope_field[0].length; COL++) {
                Gradient g = slope_field[ROW][COL];
                double dx = g.G_x, dy = g.G_y;
                double slope = (dx == 0 ? Double.MAX_VALUE : dy / dx);
                byte[][][] line = slopeLine(-1.0 * slope, dim, dy == 0 && dx == 0 || g.mag < thresh);
                int[][] intLine = images.rgbaToINT(line);
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
        strel structure_x = strel.rect(3, 1, 0, 1);
//        structure_x.print();
//        structure_x.printVec();

// {{1}, {c}, {1}}
        strel structure_y = strel.rect(1, 3, 1, 0);
//        structure_y.print();
//        structure_y.printVec();

        double[][] filter_x = {{-1}, {0}, {1}};
        double[][] filter_y = {{-1, 0, 1}};

        Gradient[][] slope_field = findGradientsMax(inputImages, structure_x, structure_y, filter_x, filter_y, scalar);

        int dim = 7;
        int thresh = 10;
        strel square = strel.square(dim);
        retImage = new int[inputImages[0].length * dim][inputImages[0][0].length * dim];

        for (int ROW = 0; ROW < slope_field.length; ROW++) {
            for (int COL = 0; COL < slope_field[0].length; COL++) {
                Gradient g = slope_field[ROW][COL];
                double dx = g.G_x, dy = g.G_y;
                double slope = (dx == 0 ? Double.MAX_VALUE : dy / dx);
                byte[][][] line = slopeLine(-1.0 * slope, dim, dy == 0 && dx == 0 || g.mag < thresh);
                int[][] intLine = images.rgbaToINT(line);
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
        strel structure_x = strel.rect(3, 1, 0, 1);
//        structure_x.print();
//        structure_x.printVec();

// {{1}, {c}, {1}}
        strel structure_y = strel.rect(1, 3, 1, 0);
//        structure_y.print();
//        structure_y.printVec();

        double[][] filter_x = {{-1}, {0}, {1}};
        double[][] filter_y = {{-1, 0, 1}};

        Gradient[][] slope_field = findGradientsAvg(inputImages, structure_x, structure_y, filter_x, filter_y, scalar);

        int dim = 7;
        int thresh = 10;
        strel square = strel.square(dim);
        retImage = new int[inputImages[0].length * dim][inputImages[0][0].length * dim];

        for (int ROW = 0; ROW < slope_field.length; ROW++) {
            for (int COL = 0; COL < slope_field[0].length; COL++) {
                Gradient g = slope_field[ROW][COL];
                double dx = g.G_x, dy = g.G_y;
                double slope = (dx == 0 ? Double.MAX_VALUE : dy / dx);
                byte[][][] line = slopeLine(-1.0 * slope, dim, dy == 0 && dx == 0 || g.mag < thresh);
                int[][] intLine = images.rgbaToINT(line);
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
                    byte[] delta = arrays.deltaBytes(current, right);
                    new_image[2 * ROW_INDEX][2 * COL_INDEX + 1] = delta;
                } catch (ArrayIndexOutOfBoundsException aioobe) {
                }
                //try down
                try {
                    byte[] down = original[ROW_INDEX + 1][COL_INDEX];
                    byte[] delta = arrays.deltaBytes(current, down);
                    new_image[2 * ROW_INDEX + 1][2 * COL_INDEX] = delta;
                } catch (ArrayIndexOutOfBoundsException aioobe) {

                }
                //try downright
                try {
                    byte[] downRight = original[ROW_INDEX + 1][COL_INDEX + 1];
                    byte[] delta = arrays.deltaBytes(current, downRight);
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
                //System.out.println("val:" + val + "\t normalized:" + normalized);
                retImage[i][j] = ramp.charAt((int) (normalized * ramp.length()));
            }
        }
        return retImage;
    }
//</editor-fold>

    // <editor-fold desc="Generate">
    public static byte[][][] slopeLine(double slope, int dim, boolean... drawDot) {

        return slopeLinePixel(slope, dim, images.WHITE_B, drawDot);
    }

    public static byte[][][] slopeLinePixel(double slope, int dim, byte[] pixel, boolean... drawDot) {
        // needs to make the space for the line
        dim = (dim % 2 == 0 ? dim + 1 : dim);
        int c_x = dim / 2, c_y = dim / 2;
        byte[][][] retImage = solidRect(dim, dim, images.BLACK_B);
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

    public static int[][] insert(int[][] inputImage, int[][] toInsert, strel structure, int c_x, int c_y) {
        int[][][] insertionPoints = structure.apply_vec_representation(new int[]{c_x, c_y});
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

    public static byte[][][] insert(byte[][][] inputImage, byte[][][] toInsert, strel structure, int c_x, int c_y) {
        int[][][] insertionPoints = structure.apply_vec_representation(new int[]{c_x, c_y});
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
        int frames = gd.getFrameCount();
        System.out.println("Frames:" + frames);
        BufferedImage[] images = new BufferedImage[frames];
        for (int frame_index = 0; frame_index < frames; frame_index++) {
            images[frame_index] = gd.getFrame(frame_index);
        }
        List<byte[][][]> retList = new ArrayList();
        for (BufferedImage frame : images) {
            retList.add(bufferedImageToBytes(frame, BufferedImage.TYPE_INT_RGB));
        }
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
        int frames = gd.getFrameCount();
        System.out.println("Frames:" + frames);
        BufferedImage[] images = new BufferedImage[frames];
        for (int frame_index = 0; frame_index < frames; frame_index++) {
            images[frame_index] = gd.getFrame(frame_index);
        }
        return images;
    }

    public static byte[][][] loadImageBytes(String pathName) throws IOException {
        String extension = files.getFileExtension(new File(pathName));
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
        String extension = files.getFileExtension(new File(pathName));
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
        String extension = files.getFileExtension(file);
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
        int height = pixels.length, width = pixels[0].length;
        BufferedImage bi = new BufferedImage(width, height, CODEC);
        for (int ROW_INDEX = 0; ROW_INDEX < height; ROW_INDEX++) {
            for (int COL_INDEX = 0; COL_INDEX < width; COL_INDEX++) {
                bi.setRGB(COL_INDEX, ROW_INDEX, rgbaToINT(pixels[ROW_INDEX][COL_INDEX]));
            }
        }
        File file = new File(pathName);
        String extension = files.getFileExtension(file);
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
        String extension = files.getFileExtension(file);
        ImageIO.write(bi, extension.toUpperCase(), file);
        if (DEBUG) {
            System.out.println("Write success".toUpperCase());
        }
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

    public static BufferedImage bytesToBufferedImage(byte[][][] inputImage) throws IOException {

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

}
