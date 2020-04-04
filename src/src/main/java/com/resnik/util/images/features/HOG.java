package com.resnik.util.images.features;

import com.resnik.util.images.StructuringElement;
import com.resnik.util.images.ImageUtils;
import com.resnik.util.logger.Log;
import com.resnik.util.math.MatrixUtils;
import com.resnik.util.objects.arrays.ArrayUtils;
import com.resnik.util.files.FileUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class HOG {

    public static final String TAG = HOG.class.getSimpleName();

    public final int[][] positive_representation, negative_representation;
    public final Cell[][] cells;
    public final Block[][] blocks;

    public HOG(int[][] positive_representation, int[][] negative_representation, Cell[][] cells, Block[][] blocks) {
        this.positive_representation = positive_representation;
        this.negative_representation = negative_representation;
        this.cells = cells;
        this.blocks = blocks;
    }

    public static Gradient[][] resizeNearestG(Gradient[][] inputGradients, int[] newRes) {
        Gradient[][] retGradients = new Gradient[newRes[1]][newRes[0]];
        double y_scalar = inputGradients.length * (1.0 / retGradients.length);
        double x_scalar = inputGradients[0].length * (1.0 / retGradients[0].length);
        for (int ROW = 0; ROW < retGradients.length; ROW++) {
            for (int COL = 0; COL < retGradients[0].length; COL++) {
                int yVal = (int) (ROW * y_scalar);
                int xVal = (int) (COL * x_scalar);
                retGradients[ROW][COL] = inputGradients[yVal][xVal];
            }
        }
        return retGradients;
    }

    public static void generate(String root, String inputDirName) throws IOException {
        // make output files in root
        String neg_rep_dir = root + "negative_representations/", pos_rep_dir = root + "positive_representations/";
        File negDir = new File(neg_rep_dir);
        File posDir = new File(pos_rep_dir);
        negDir.mkdirs();
        posDir.mkdirs();
        String fullInputDirectory = root + inputDirName;
        Log.v(TAG,fullInputDirectory);
        Map<String, List<String>> fileMap = FileUtils.getFileExtensionNameMap(fullInputDirectory);
        Log.v(TAG,fileMap);
        String[] names = FileUtils.getNames(fullInputDirectory, "bmp");
        String[] locs = FileUtils.getLocations(fullInputDirectory, "bmp");
        Log.v(TAG,Arrays.toString(locs));
        HOG[] hogs = new HOG[locs.length];
        for (int LOC_INDEX = 0; LOC_INDEX < locs.length; LOC_INDEX++) {
            byte[][][] original = ImageUtils.loadImageBytes(locs[LOC_INDEX]);
            hogs[LOC_INDEX] = representation(original);
            ImageUtils.saveImageFromIntArray(hogs[LOC_INDEX].negative_representation, neg_rep_dir + names[LOC_INDEX], BufferedImage.TYPE_INT_RGB);
            ImageUtils.saveImageFromIntArray(hogs[LOC_INDEX].positive_representation, pos_rep_dir + names[LOC_INDEX], BufferedImage.TYPE_INT_RGB);
        }
    }

    public static HOG fromGradients(Gradient[][] gradients) {
        double[][] thetas = new double[gradients.length][gradients[0].length];
        double[][] degrees = new double[gradients.length][gradients[0].length];
        for (int ROW = 0; ROW < gradients.length; ROW++) {
            for (int COL = 0; COL < gradients[0].length; COL++) {
                thetas[ROW][COL] = gradients[ROW][COL].positiveTheta();
                degrees[ROW][COL] = gradients[ROW][COL].positiveDegree();
            }
        }

        int cell_size = 8;
        Cell[][] cells = new Cell[gradients.length / cell_size][gradients[0].length / cell_size];
        for (int CELL_ROW = 0; CELL_ROW < cells.length; CELL_ROW++) {
            for (int CELL_COL = 0; CELL_COL < cells[0].length; CELL_COL++) {
                Gradient[][] sub = ArrayUtils.subset(gradients, CELL_ROW * cell_size, CELL_COL * cell_size, cell_size, cell_size, new Gradient(-1, -1, -1, -1));
                int[] histogram = applyHistogram(sub);
                cells[CELL_ROW][CELL_COL] = new Cell(histogram);
            }
        }
        int block_size = 2;
        Block[][] blocks = new Block[cells.length - block_size + 1][cells[0].length - block_size + 1];
        for (int ROW = 0; ROW < blocks.length; ROW++) {
            for (int COL = 0; COL < blocks[0].length; COL++) {
                Cell[][] sub = ArrayUtils.subset(cells, ROW, COL, block_size, block_size, new Cell(null));
                blocks[ROW][COL] = new Block(sub);
            }
        }

        int dim = 9;
        StructuringElement square = StructuringElement.square(dim);
        int[][] retImagePOS = new int[cells.length * dim][cells[0].length * dim];
        int[][] retImageNEG = new int[cells.length * dim][cells[0].length * dim];
        for (int CELL_ROW = 0; CELL_ROW < cells.length; CELL_ROW++) {
            for (int CELL_COL = 0; CELL_COL < cells[0].length; CELL_COL++) {

                byte[][][][] h_images_POS = toImage(cells[CELL_ROW][CELL_COL].histogram);
                byte[][][][] h_images_NEG = toImage(cells[CELL_ROW][CELL_COL].histogram, -1.0);
                byte[][][] combined_POS = ImageUtils.add(h_images_POS);
                byte[][][] combined_NEG = ImageUtils.add(h_images_NEG);
                int[][] int_combined_POS = ImageUtils.rgbaToINT(combined_POS);
                int[][] int_combined_NEG = ImageUtils.rgbaToINT(combined_NEG);
                retImagePOS = ImageUtils.insert(retImagePOS, int_combined_POS, square, CELL_COL * dim, CELL_ROW * dim);
                retImageNEG = ImageUtils.insert(retImageNEG, int_combined_NEG, square, CELL_COL * dim, CELL_ROW * dim);
            }
        }
        HOG retHOG = new HOG(retImagePOS, retImageNEG, cells, blocks);
        // retImage = insert(retImage, intLine, square, x_ins, y_ins);
        return retHOG;
    }

    public static HOG representation(byte[][][] image) {
        Gradient[][] gradients = findGradientsSplit(image);
        return fromGradients(gradients);

    }

    public static byte[][][][] toImage(int[] histogram, double... slopeMod) {
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
            lineImages[i] = ImageUtils.slopeLinePixel((slopeMod.length > 0 ? slopeMod[0] : 1.0) * slope, histogram.length, new byte[]{(byte) grey, (byte) grey, (byte) grey}, false);
        }
        return lineImages;
    }

    public static Gradient[][] findGradientsSplit(byte[][][] inputImage) {
        byte[][][][] split = ImageUtils.splitRGB(inputImage);
        int[][][] splitInt = new int[split.length][][];
        for (int i = 0; i < split.length; i++) {
            splitInt[i] = ImageUtils.rgbaToINT(split[i]);
        }
        StructuringElement structure_x = StructuringElement.rect(3, 1, 0, 1);

        StructuringElement structure_y = StructuringElement.rect(1, 3, 1, 0);

        double[][] filter_x = {{-1}, {0}, {1}};
        double[][] filter_y = {{-1, 0, 1}};

        Gradient[][] slope_field = findMax(splitInt, structure_x, structure_y, filter_x, filter_y, 1.0);
        return slope_field;
    }

    public static Gradient[][] findMax(int[][][] inputImages, StructuringElement structure_x, StructuringElement structure_y, double[][] filter_x, double[][] filter_y, double... optScalar) {
        // Find gradients for all input images
        Gradient[][][] gradient_images = new Gradient[inputImages.length][][];
        Gradient[][] slope_field = new Gradient[inputImages[0].length][inputImages[0][0].length];
        for (int IMAGE_INDEX = 0; IMAGE_INDEX < inputImages.length; IMAGE_INDEX++) {
            gradient_images[IMAGE_INDEX] = findGradients(inputImages[IMAGE_INDEX], structure_x, structure_y, filter_x, filter_y, IMAGE_INDEX, optScalar);
        }
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

    public static Gradient[][] findAvg(int[][][] inputImages, StructuringElement structure_x, StructuringElement structure_y, double[][] filter_x, double[][] filter_y, double... optScalar) {
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

    public static Gradient[][] findGradients(int[][] inputImage, StructuringElement structure_x, StructuringElement structure_y, double[][] filter_x, double[][] filter_y, double... optScalar) {
        Gradient[][] slope_field = findGradients(inputImage, structure_x, structure_y, filter_x, filter_y, 0, optScalar);
        return slope_field;
    }

    public static Gradient[][] findGradients(int[][] inputImage, StructuringElement structure_x, StructuringElement structure_y, double[][] filter_x, double[][] filter_y, int pixelIndex, double... optScalar) {
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
                int[][][] surr_coord_x = structure_x.applyVectorRepresentation(new int[]{COL, ROW});
                int[][][] surr_coord_y = structure_y.applyVectorRepresentation(new int[]{COL, ROW});
                // For accuracy
                double[][] surr_pixels_x = new double[surr_coord_x.length][surr_coord_x[0].length];
                double[][] surr_pixels_y = new double[surr_coord_y.length][surr_coord_y[0].length];
                for (int i = 0; i < surr_coord_x.length; i++) {
                    for (int j = 0; j < surr_coord_x[0].length; j++) {
                        int x_index = surr_coord_x[i][j][0], y_index = surr_coord_x[i][j][1];
                        byte[] pixel = ImageUtils.intToRGB(inputImage[y_index][x_index]);
                        int identifier = pixel[pixelIndex];
                        surr_pixels_x[i][j] = identifier;
                    }
                }
                for (int i = 0; i < surr_coord_y.length; i++) {
                    for (int j = 0; j < surr_coord_y[0].length; j++) {
                        int x_index = surr_coord_y[i][j][0], y_index = surr_coord_y[i][j][1];
                        byte[] pixel = ImageUtils.intToRGB(inputImage[y_index][x_index]);
                        int identifier = pixel[pixelIndex];
                        surr_pixels_y[i][j] = identifier;
                    }
                }

                double[][] x_applied = MatrixUtils.hadamard(surr_pixels_x, filter_x);
                double G_x = MatrixUtils.sigma(x_applied) / scalar;
                double[][] y_applied = MatrixUtils.hadamard(surr_pixels_y, filter_y);
                double G_y = MatrixUtils.sigma(y_applied) / scalar;
                double theta = Math.atan(G_y / G_x);
                int mag = (int) Math.sqrt(G_x * G_x + G_y * G_y);
                slope_field[ROW][COL] = new Gradient(G_x, G_y, theta, mag);
            }
        }
        return slope_field;
    }

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
                        // Log.v(TAG,"delta:" + delta + "\tperc:" + perc + "\ti:" + i  + "\tleft:" + left+ "\tcurr:" + theta + "\tright:" + right + "\tamtL:" + amt_l + "\tamtR:" + amt_r);

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
        // Log.v(TAG,"temp:" + Arrays.toString(tempArray));
        int[] retArray = new int[number_of_bins];
        tempArray[1] += tempArray[0];
        tempArray[tempArray.length - 2] += tempArray[tempArray.length - 1];
        for (int i = 1; i < tempArray.length - 1; i++) {
            retArray[i - 1] = tempArray[i];
        }

        return retArray;
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
        HOG temp = representation(image);
        return temp.featureVector();
    }
}