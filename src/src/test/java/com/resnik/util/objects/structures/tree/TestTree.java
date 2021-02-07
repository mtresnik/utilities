package com.resnik.util.objects.structures.tree;

import com.resnik.util.images.GifDecoder;
import com.resnik.util.images.ImageUtils;
import com.resnik.util.logger.Log;
import com.resnik.util.math.shapes.BoundingBox;
import com.resnik.util.math.shapes.PolygonPoint;
import com.resnik.util.objects.structures.Node;
import com.resnik.util.objects.structures.tree.kd.KDTree;
import com.resnik.util.objects.structures.tree.kd.KDTreeNode;
import com.resnik.util.objects.structures.tree.kd.KDTreeValue;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.resnik.util.objects.structures.tree.binary.BinaryTree.generateBinaryGif;

public class TestTree {

    public static final String TAG = TestTree.class.getSimpleName();

    @Test
    public void testBinaryGif(){
        generateBinaryGif("src/res/nodes/binaryGif.gif", Arrays.asList(3.0, 2.0,5.0,4.0,2.5, 1.0,6.0, -1.0, 1.5, 7.0, 3.5,4.5, 2.2,2.8));
    }

    @Test
    public void testBinaryTree(){
        Node<Integer> root = Node.constructBinaryTree(1,2,3,4,5);
        Log.v(TAG,root.contains(6));
    }

    @Test
    public void testKDTree(){
        KDTree<Double> kdTree = new KDTree<>(2);
        kdTree.insert(new KDTreeValue<Double>(new double[]{0, 0}, Math.random()));
        kdTree.insert(new KDTreeValue<Double>(new double[]{0, 1}, Math.random()));
        Log.e(TAG, kdTree.closest(new double[]{1,2}));
    }

    @Test
    public void writeKDTree(){
        int height = 600;
        int width = 600;

        KDTree<Double> kdTree = new KDTree<>(2);
        double[] pt = new double[]{Math.random()*width, Math.random()*height};
        kdTree.insert(new KDTreeValue<Double>(pt, Math.random()));
        List<double[]> points = new ArrayList<>();
        points.add(pt);
        int iterations = 100;
        BufferedImage[] images = new BufferedImage[iterations];
        for(int i = 0; i < iterations; i++){
            Log.e(TAG, "Iteration:" + i);
            double[] p1 = new double[]{Math.random()*width, Math.random()*height};
            kdTree.insert(new KDTreeValue<Double>(p1, Math.random()));
            points.add(p1);
            byte[][][] image = new byte[height][width][];
            for(int ROW = 0; ROW < height; ROW++){
                for(int COL = 0; COL < width; COL++){
                    KDTreeNode<Double> closest = kdTree.closest(new double[]{COL, ROW});
                    int greyVal = (int) (closest.value.data * 255);
                    image[ROW][COL] = new byte[]{(byte) greyVal, (byte) greyVal, (byte) greyVal};
                }
            }
            images[i] = ImageUtils.bytesToBufferedImage(image);
        }

        GifDecoder gd = new GifDecoder();
        gd.delay = 100;
        try {
            ImageUtils.saveGifBuffered(images, gd, "src/res/kdTree.gif");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BoundingBox randomBoundingBox(int width, int height){
        double x1 = Math.random()*width;
        double y1 = Math.random()*height;
        double x2 = Math.random()*width;
        double y2 = Math.random()*height;
        return new BoundingBox(x1, y1, x2, y2);
    }

    @Test
    public void writeRandomSquares(){
        int height = 600;
        int width = 600;

        List<BoundingBox> boxes = new ArrayList<>();
        List<byte[]> matchingColors = new ArrayList<>();
        boxes.add(randomBoundingBox(width, height));
        double greyVal = Math.random()*255;
        matchingColors.add(new byte[]{(byte) greyVal, (byte) greyVal, (byte) greyVal});
        int iterations = 100;
        BufferedImage[] images = new BufferedImage[iterations];
        for(int i = 0; i < iterations; i++){
            Log.e(TAG, "Iteration:" + i);
            byte[][][] image = new byte[height][width][];
            boxes.add(randomBoundingBox(width, height));
            greyVal = Math.random()*255;
            matchingColors.add(new byte[]{(byte) greyVal, (byte) greyVal, (byte) greyVal});
            for(int ROW = 0; ROW < height; ROW++){
                for(int COL = 0; COL < width; COL++){
                    image[ROW][COL] = ImageUtils.WHITE_B;
                }
            }
            for(int ROW = 0; ROW < height; ROW++){
                for(int COL = 0; COL < width; COL++){
                    for(int b = 0; b < boxes.size(); b++){
                        if(boxes.get(b).contains(new PolygonPoint(COL, ROW))){
                            image[ROW][COL] = matchingColors.get(b);
                        }
                    }
                }
            }
            images[i] = ImageUtils.bytesToBufferedImage(image);
        }

        GifDecoder gd = new GifDecoder();
        gd.delay = 100;
        try {
            ImageUtils.saveGifBuffered(images, gd, "src/res/randomBoxes.gif");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNN(){
        int height = 600;
        int width = 600;

        List<double[]> points = new ArrayList<>();
        List<byte[]> matchingColors = new ArrayList<>();
        points.add(new double[]{Math.random()*width, Math.random()*height});
        double greyVal = Math.random();
        matchingColors.add(ImageUtils.grey(greyVal));
        int iterations = 150;
        BufferedImage[] images = new BufferedImage[iterations];
        for(int i = 0; i < iterations; i++){
            Log.e(TAG, "Iteration:" + i);
            byte[][][] image = new byte[height][width][];
            points.add(new double[]{Math.random()*width, Math.random()*height});
            greyVal = Math.random();
            matchingColors.add(ImageUtils.grey(greyVal));
            for(int ROW = 0; ROW < height; ROW++){
                for(int COL = 0; COL < width; COL++){
                    image[ROW][COL] = ImageUtils.WHITE_B;
                }
            }
            for(int ROW = 0; ROW < height; ROW++){
                for(int COL = 0; COL < width; COL++){
                    int minIndex = -1;
                    double minDistance = Double.MAX_VALUE;
                    for(int index = 0; index < points.size(); index++){
                        double[] point = points.get(index);
                        double dx = COL - point[0];
                        double dy = ROW - point[1];
                        double dist = Math.sqrt(dx*dx + dy*dy);
                        if(dist < minDistance){
                            minDistance = dist;
                            minIndex = index;
                        }
                    }
                    image[ROW][COL] = matchingColors.get(minIndex);
                }
            }
            images[i] = ImageUtils.bytesToBufferedImage(image);
        }

        GifDecoder gd = new GifDecoder();
        gd.delay = 100;
        try {
            ImageUtils.saveGifBuffered(images, gd, "src/res/testNN.gif");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNNImageInput() throws IOException {
        byte[][][] sourceImage = ImageUtils.loadImageBytes("src/res/giraffe.jpg");

        int height = sourceImage.length;
        int width = sourceImage[0].length;

        List<double[]> points = new ArrayList<>();
        List<byte[]> matchingColors = new ArrayList<>();
        double tmpX = Math.random()*width;
        double tmpY = Math.random()*height;
        double[] pt = new double[]{tmpX, tmpY};
        points.add(pt);
        matchingColors.add(sourceImage[(int)tmpY][(int)tmpX]);
        int iterations = 1800;
        int saveBy = 30;
        BufferedImage[] images = new BufferedImage[iterations/saveBy];
        for(int i = 0; i < iterations; i++){
            Log.e(TAG, "Iteration:" + i);
            tmpX = Math.random()*width;
            tmpY = Math.random()*height;
            pt = new double[]{tmpX, tmpY};
            points.add(pt);
            matchingColors.add(sourceImage[(int)tmpY][(int)tmpX]);
            if(i % saveBy == 0){
                byte[][][] image = new byte[height][width][];
                for(int ROW = 0; ROW < height; ROW++){
                    for(int COL = 0; COL < width; COL++){
                        image[ROW][COL] = ImageUtils.WHITE_B;
                    }
                }
                for(int ROW = 0; ROW < height; ROW++){
                    for(int COL = 0; COL < width; COL++){
                        int minIndex = -1;
                        double minDistance = Double.MAX_VALUE;
                        for(int index = 0; index < points.size(); index++){
                            double[] point = points.get(index);
                            double dx = COL - point[0];
                            double dy = ROW - point[1];
                            double dist = Math.sqrt(dx*dx + dy*dy);
                            if(dist < minDistance){
                                minDistance = dist;
                                minIndex = index;
                            }
                        }
                        image[ROW][COL] = matchingColors.get(minIndex);
                    }
                }
                images[i/saveBy] = ImageUtils.bytesToBufferedImage(image);
            }

        }

        GifDecoder gd = new GifDecoder();
        gd.delay = 100;
        try {
            ImageUtils.saveGifBuffered(images, gd, "src/res/testNNImage.gif");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNNImageInput2() throws IOException {
        byte[][][] sourceImage = ImageUtils.loadImageBytes("src/res/giraffe.jpg");

        int height = sourceImage.length;
        int width = sourceImage[0].length;

        List<double[]> points = new ArrayList<>();
        List<byte[]> matchingColors = new ArrayList<>();
        double tmpX = Math.random()*width;
        double tmpY = Math.random()*height;
        double[] pt = new double[]{tmpX, tmpY};
        points.add(pt);
        matchingColors.add(sourceImage[(int)tmpY][(int)tmpX]);
        int iterations = 20000;
        for(int i = 0; i < iterations; i++){
            Log.e(TAG, "Iteration:" + i);
            tmpX = Math.random()*width;
            tmpY = Math.random()*height;
            pt = new double[]{tmpX, tmpY};
            points.add(pt);
            matchingColors.add(sourceImage[(int)tmpY][(int)tmpX]);
        }
        byte[][][] image = new byte[height][width][];
        for(int ROW = 0; ROW < height; ROW++){
            for(int COL = 0; COL < width; COL++){
                int minIndex = -1;
                double minDistance = Double.MAX_VALUE;
                for(int index = 0; index < points.size(); index++){
                    double[] point = points.get(index);
                    double dx = COL - point[0];
                    double dy = ROW - point[1];
                    double dist = Math.sqrt(dx*dx + dy*dy);
                    if(dist < minDistance){
                        minDistance = dist;
                        minIndex = index;
                    }
                }
                image[ROW][COL] = matchingColors.get(minIndex);
            }
        }

        GifDecoder gd = new GifDecoder();
        gd.delay = 100;
        try {
            ImageUtils.saveImageBytes(image, "src/res/testNNImageOut.bmp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTree(){
        SimpleTree<Double> tree = new SimpleTree<>();
        tree.add(10.0);
        tree.add(5.0);
        tree.add(7.0);
        Log.v(TAG,tree.size());
        Log.v(TAG,tree);
        Log.v(TAG,tree.contains(8.0));
        Log.v(TAG,tree.contains(7.0));
    }
}
