package com.resnik.util.math.stats.cluster;

import com.resnik.util.images.GifDecoder;
import com.resnik.util.images.ImageUtils;
import com.resnik.util.logger.Log;
import com.resnik.util.math.plot.Plot3D;
import com.resnik.util.math.plot.elements3d.PlotDataset3D;
import com.resnik.util.math.plot.elements3d.PlotElement3D;
import com.resnik.util.math.plot.elements3d.PointType;
import com.resnik.util.math.plot.points.Point3d;
import com.resnik.util.math.shapes.Polygon;
import com.resnik.util.math.shapes.PolygonPoint;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static com.resnik.util.math.stats.cluster.KMeans.getKMeans;

public class TestCluster {

    public static final String TAG = TestCluster.class.getSimpleName();

    private static void testCircles(){
        // Create circles in an image
        final int WIDTH = 1000;
        final int HEIGHT = 1000;
        int clusterSize = 5;
        int maxPixelsPerCluster = WIDTH * HEIGHT / clusterSize;
        double minThreshold = 0.2;
        int pixelsPerCluster = (int) ((Math.random() + minThreshold) * maxPixelsPerCluster);
        List<double[]> centers = new ArrayList<>();
        for(int i = 0; i < clusterSize; i++){
            centers.add(new double[]{Math.random()*(HEIGHT - 1), Math.random()*(WIDTH - 1)});
        }
        List<Double> radii = new ArrayList<>();
        for(int i = 0; i < clusterSize; i++){
            double area = pixelsPerCluster*0.8;
            double radius = Math.sqrt(area / Math.PI);
            radii.add(radius);
        }
        byte[] red = ImageUtils.awtToByte(Color.red);
        byte[] yellow = ImageUtils.awtToByte(Color.yellow);
        byte[] orange = new byte[]{(byte)255,(byte) 165, 0};
        byte[] blue = new byte[]{0,0,(byte) 127};
        byte[] green = new byte[]{0,(byte)127, 0};
        byte[][] palette = new byte[][]{
                blue, green, red, yellow, orange};
        List<double[]> data = new ArrayList<>();
        for(int i = 0; i < clusterSize; i++){
            double[] center = centers.get(i);
            double radius = radii.get(i);
            List<int[]> possibleCoordinates = new ArrayList<>();
            for(int ROW = 0; ROW < HEIGHT; ROW++){
                for(int COL = 0; COL < WIDTH; COL++){
                    double dx = COL - center[1];
                    double dy = ROW - center[0];
                    double r = Math.sqrt(dx*dx + dy*dy);
                    if(r <= radius){
                        possibleCoordinates.add(new int[]{ROW, COL});
                    }
                }
            }
            double numPoints = 400;
            for(int p = 0; p < numPoints; p++){
                int index = (int)(Math.random() * (possibleCoordinates.size() - 1));
                int[] coordinates = possibleCoordinates.get(index);
                int ROW = coordinates[0];
                int COL = coordinates[1];
                data.add(new double[]{ROW, COL});
            }
        }
        double[][] dataArray = new double[data.size()][];
        dataArray = data.toArray(dataArray);
        List<KMeans> allKMeans = getKMeans(clusterSize, dataArray, 200);
        Collections.sort(allKMeans, Comparator.comparingDouble(KMeans::getVariance));
        List<Cluster> clusters = allKMeans.get(0).getClusters();
        byte[][][] image = new byte[HEIGHT][WIDTH][];
        for(int ROW = 0; ROW < HEIGHT; ROW++){
            for(int COL = 0; COL < WIDTH; COL++){
                image[ROW][COL] = ImageUtils.WHITE_B;
            }
        }
        for(int i = 0; i < clusters.size(); i++){
            Cluster cluster = clusters.get(i);
            List<double[]> clusterPoints = cluster.points;
            double pointRadius = 3;
            for(double[] clusterPoint : clusterPoints){
                int clusterROW = (int) clusterPoint[0];
                int clusterCOL = (int) clusterPoint[1];
                for(int ROW = Math.max(0, (int)(clusterROW - pointRadius)); ROW < Math.min(HEIGHT, (int)(clusterROW + pointRadius)); ROW++){
                    for(int COL = Math.max(0, (int)(clusterCOL - pointRadius)); COL < Math.min(WIDTH, (int)(clusterCOL + pointRadius)); COL++){
                        double dx = COL - clusterCOL;
                        double dy = ROW - clusterROW;
                        if(Math.sqrt(dx*dx + dy*dy) <= pointRadius){
                            image[ROW][COL] = palette[i];
                        }
                    }
                }
            }
            double[] mean = cluster.getMean();
            int meanROW = (int) mean[0];
            int meanCOL = (int) mean[1];
            double meanRadius = 10;
            for(int ROW = 0; ROW < HEIGHT; ROW++){
                for(int COL = 0; COL < WIDTH; COL++){
                    double dx = COL - meanCOL;
                    double dy = ROW - meanROW;
                    if(Math.sqrt(dx*dx + dy*dy) <= meanRadius){
                        image[ROW][COL] = ImageUtils.BLACK_B;
                    }
                }
            }
        }

        try {
            ImageUtils.saveImageBytes(image, "src/res/cluster/circles.bmp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void testRandom(){
        // Create circles in an image
        final int WIDTH = 1000;
        final int HEIGHT = 1000;
        int clusterSize = 5;
        int maxPixelsPerCluster = WIDTH * HEIGHT / clusterSize;
        double minThreshold = 0.2;
        int pixelsPerCluster = (int) ((Math.random() + minThreshold) * maxPixelsPerCluster);
        List<double[]> centers = new ArrayList<>();
        for(int i = 0; i < clusterSize; i++){
            centers.add(new double[]{Math.random()*(HEIGHT - 1), Math.random()*(WIDTH - 1)});
        }
        List<Double> radii = new ArrayList<>();
        for(int i = 0; i < clusterSize; i++){
            double area = Math.random()*pixelsPerCluster*0.8;
            double radius = Math.sqrt(area / Math.PI);
            radii.add(radius);
        }
        byte[] red = ImageUtils.awtToByte(Color.red);
        byte[] yellow = ImageUtils.awtToByte(Color.yellow);
        byte[] orange = new byte[]{(byte)255,(byte) 165, 0};
        byte[] blue = new byte[]{0,0,(byte) 127};
        byte[] green = new byte[]{0,(byte)127, 0};
        byte[][] palette = new byte[][]{
                blue, green, red, yellow, orange};
        List<double[]> data = new ArrayList<>();
        for(int i = 0; i < clusterSize; i++){
            for(int subSize = 0; subSize < 1000; subSize++){
                int ROW = (int)(Math.random() * (HEIGHT - 1));
                int COL = (int)(Math.random() * (WIDTH - 1));
                data.add(new double[]{ROW, COL});
            }
        }
        double[][] dataArray = new double[data.size()][];
        dataArray = data.toArray(dataArray);
        List<KMeans> allKMeans = getKMeans(clusterSize, dataArray, 100);
        Collections.sort(allKMeans, Comparator.comparingDouble(KMeans::getVariance));
        List<Cluster> clusters = allKMeans.get(0).getClusters();
        byte[][][] image = new byte[HEIGHT][WIDTH][];
        for(int ROW = 0; ROW < HEIGHT; ROW++){
            for(int COL = 0; COL < WIDTH; COL++){
                image[ROW][COL] = ImageUtils.WHITE_B;
            }
        }
        for(int i = 0; i < clusters.size(); i++){
            Cluster cluster = clusters.get(i);
            List<double[]> clusterPoints = cluster.points;
            double pointRadius = 3;
            for(double[] clusterPoint : clusterPoints){
                int clusterROW = (int) clusterPoint[0];
                int clusterCOL = (int) clusterPoint[1];
                for(int ROW = 0; ROW < HEIGHT; ROW++){
                    for(int COL = 0; COL < WIDTH; COL++){
                        double dx = COL - clusterCOL;
                        double dy = ROW - clusterROW;
                        if(Math.sqrt(dx*dx + dy*dy) <= pointRadius){
                            image[ROW][COL] = palette[i];
                        }
                    }
                }
            }
            double[] mean = cluster.getMean();
            int meanROW = (int) mean[0];
            int meanCOL = (int) mean[1];
            double meanRadius = 10;
            for(int ROW = 0; ROW < HEIGHT; ROW++){
                for(int COL = 0; COL < WIDTH; COL++){
                    double dx = COL - meanCOL;
                    double dy = ROW - meanROW;
                    if(Math.sqrt(dx*dx + dy*dy) <= meanRadius){
                        image[ROW][COL] = ImageUtils.BLACK_B;
                    }
                }
            }
        }

        try {
            ImageUtils.saveImageBytes(image, "src/res/cluster/random.bmp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testPlot3d(){
        List<double[]> data = new ArrayList<>();
        double bounds = 10.0;
        int numPoints = 3000;
        for(int i = 0; i < numPoints;){
            double x = bounds * (Math.random() - 0.5)* 2;
            double y = bounds * (Math.random() - 0.5)* 2;
            double z = bounds * (Math.random() - 0.5)* 2;
            double r = Math.sqrt(x*x + y*y + z*z);
            if(r <= 10){
                data.add(new double[]{x, y, z});
                i++;
            }
        }
        List<javafx.scene.paint.Color> palette = new ArrayList<javafx.scene.paint.Color>(){{
            this.add(javafx.scene.paint.Color.BLUE);
            this.add(javafx.scene.paint.Color.RED);
            this.add(javafx.scene.paint.Color.ORANGE);
            this.add(javafx.scene.paint.Color.GREEN);
            this.add(javafx.scene.paint.Color.YELLOW);
            this.add(javafx.scene.paint.Color.PURPLE);
            this.add(javafx.scene.paint.Color.MAGENTA);
            this.add(javafx.scene.paint.Color.AQUA);
//            this.add(javafx.scene.paint.Color.CHOCOLATE);
//            this.add(javafx.scene.paint.Color.LIME);
        }};
        int numClusters = palette.size();
        double[][] dataArray = new double[data.size()][];
        dataArray = data.toArray(dataArray);
        List<KMeans> allKMeans = getKMeans(numClusters, dataArray, 50);
        Collections.sort(allKMeans, Comparator.comparingDouble(KMeans::getVariance));
        List<Cluster> clusters = allKMeans.get(0).getClusters();
        List<PlotElement3D> plotElements = new ArrayList<>();
        for(int i = 0; i < clusters.size(); i++){
            Cluster cluster = clusters.get(i);
            javafx.scene.paint.Color color = palette.get(i);
            double[] mean = cluster.getMean();
            Point3d meanPoint = new Point3d(mean[0], mean[1], mean[2]);
            List<Point3d> points = new ArrayList<>();
            for(double[] point : cluster.points){
                Point3d rep = new Point3d(point[0], point[1], point[2]);
                points.add(rep);
                PlotDataset3D connectingLine = new PlotDataset3D(color, meanPoint, rep);
                connectingLine.setLines(true);
                plotElements.add(connectingLine);
            }
            PlotDataset3D meanElement = new PlotDataset3D(javafx.scene.paint.Color.BLACK, meanPoint);
            meanElement.setType(PointType.CUBE);
            plotElements.add(meanElement);
        }
        PlotElement3D[] plotElementArray = new PlotElement3D[plotElements.size()];
        plotElementArray = plotElements.toArray(plotElementArray);
        Plot3D.CartesianPlot plot = new Plot3D.CartesianPlot(plotElementArray);
        plot.useAxes = false;
        plot.show();
    }

    private static void testShape(){
        final int HEIGHT = 1000;
        final int WIDTH = 1000;
        int edges = 50;
        double dtheta = 2*Math.PI / edges;
        List<PolygonPoint> vertices = new ArrayList<>();
        for(int i = 0; i < edges; i++){
            double theta = i*dtheta;
            double x = Math.cos(theta);
            double y = Math.sin(theta);
            int COL = (int)(x*WIDTH/2 + WIDTH/2);
            int ROW = (int)(y*HEIGHT/2 + HEIGHT/2);
            PolygonPoint point = new PolygonPoint(COL, ROW);
            vertices.add(point);
        }
        com.resnik.util.math.shapes.Polygon polygon = new Polygon(vertices);
        int numPoints = 8000;
        List<double[]> data = new ArrayList<>();
        for(int i = 0; i < numPoints;){
            double COL = Math.random() * (WIDTH - 1);
            double ROW = Math.random() * (HEIGHT - 1);
            PolygonPoint polygonPoint = new PolygonPoint(COL, ROW);
            if(polygon.contains(polygonPoint)){
                data.add(new double[]{COL, ROW});
                i++;
            }
        }
        byte[] red = ImageUtils.awtToByte(Color.red);
        byte[] yellow = ImageUtils.awtToByte(Color.yellow);
        byte[] orange = new byte[]{(byte)255,(byte) 165, 0};
        byte[] blue = new byte[]{0,0,(byte) 127};
        byte[] green = new byte[]{0,(byte)127, 0};
        byte[][] palette = new byte[][]{
                new byte[]{(byte)96,(byte) 169,(byte) 23},
                new byte[]{(byte)0,(byte) 171,(byte) 169},
                new byte[]{(byte)0,(byte) 80,(byte) 239},
                new byte[]{(byte)162,(byte) 0,(byte) 37},
                new byte[]{(byte)250,(byte) 104,(byte) 0},
                new byte[]{(byte)227,(byte) 200,(byte) 0},
                new byte[]{(byte)100,(byte) 118,(byte) 135},
                new byte[]{(byte)170,(byte) 0,(byte) 255},
                new byte[]{(byte)63,(byte) 0,(byte) 129},
                new byte[]{(byte)96,(byte) 15,(byte) 142},
                new byte[]{(byte)120,(byte) 35,(byte) 162},
        };
        int numClusters = palette.length;
        double[][] dataArray = new double[data.size()][];
        dataArray = data.toArray(dataArray);
        List<KMeans> allKMeans = getKMeans(numClusters, dataArray, 50);

        Collections.sort(allKMeans, Comparator.comparingDouble(KMeans::getVariance));
        List<Cluster> clusters = allKMeans.get(0).getClusters();
        byte[][][] image = new byte[HEIGHT][WIDTH][];
        for(int ROW = 0; ROW < HEIGHT; ROW++){
            for(int COL = 0; COL < WIDTH; COL++){
                image[ROW][COL] = ImageUtils.WHITE_B;
            }
        }
        for(int i = 0; i < clusters.size(); i++){
            Cluster cluster = clusters.get(i);
            List<double[]> clusterPoints = cluster.points;
            double pointRadius = 3;
            for(double[] clusterPoint : clusterPoints){
                int clusterROW = (int) clusterPoint[0];
                int clusterCOL = (int) clusterPoint[1];
                for(int ROW = 0; ROW < HEIGHT; ROW++){
                    for(int COL = 0; COL < WIDTH; COL++){
                        double dx = COL - clusterCOL;
                        double dy = ROW - clusterROW;
                        if(Math.sqrt(dx*dx + dy*dy) <= pointRadius){
                            image[ROW][COL] = palette[i];
                        }
                    }
                }
            }
            double[] mean = cluster.getMean();
            int meanROW = (int) mean[0];
            int meanCOL = (int) mean[1];
            double meanRadius = 10;
            for(int ROW = 0; ROW < HEIGHT; ROW++){
                for(int COL = 0; COL < WIDTH; COL++){
                    double dx = COL - meanCOL;
                    double dy = ROW - meanROW;
                    if(Math.sqrt(dx*dx + dy*dy) <= meanRadius){
                        image[ROW][COL] = ImageUtils.BLACK_B;
                    }
                }
            }
        }
        try {
            ImageUtils.saveImageBytes(image, "src/res/shapes/circle" + palette.length + ".bmp");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void testImageCluster(){
        String fileLocation = "res/sunday.png";
        byte[][][] image = null;
        try {
            image = ImageUtils.loadImageBytes(fileLocation);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        List<double[]> allData = new ArrayList<>();
        for(int ROW = 0; ROW < image.length; ROW++){
            for(int COL = 0; COL < image[0].length; COL++){
                byte[] pixel = image[ROW][COL];
                double r = pixel[0];
                double g = pixel[1];
                double b = pixel[2];
                while(r < 0){
                    r += 256;
                }
                while(g < 0){
                    g += 256;
                }
                while(b < 0){
                    b += 256;
                }
                allData.add(new double[]{r,g,b});
            }
        }
        int numSample = 10000;
        List<double[]> data = new ArrayList<>();
        while(data.size() < numSample){
            data.add(allData.get((int)(Math.random() * (allData.size() - 1))));
        }
        double[][] dataArray = new double[data.size()][];
        dataArray = data.toArray(dataArray);
        List<BufferedImage> images = new ArrayList<>();
        int[] clusterVals = new int[]{8,12,16,24,32,48,64,80,100,128,164,180,200,232,256,300};
        for(int numClusters : clusterVals){
            Log.d(TAG, "Clustering k=" + numClusters);
            List<KMeans> allKMeans = getKMeans(numClusters, dataArray, 1);
            Collections.sort(allKMeans, Comparator.comparingDouble(KMeans::getVariance));
            byte[][][] retImage = new byte[image.length][image[0].length][];
            for(int ROW = 0; ROW < image.length; ROW++){
                for(int COL = 0; COL < image[0].length; COL++){
                    byte[] pixel = image[ROW][COL];
                    double r = pixel[0];
                    double g = pixel[1];
                    double b = pixel[2];
                    while(r < 0){
                        r += 256;
                    }
                    while(g < 0){
                        g += 256;
                    }
                    while(b < 0){
                        b += 256;
                    }
                    Cluster nextCluster = allKMeans.get(0).getCluster(new double[]{r,g,b});
                    double[] mean = nextCluster.getMean();
                    r = mean[0];
                    g = mean[1];
                    b = mean[2];
                    while(r < 0){
                        r += 256;
                    }
                    while(g < 0){
                        g += 256;
                    }
                    while(b < 0){
                        b += 256;
                    }
                    retImage[ROW][COL] = new byte[]{(byte)r, (byte)g, (byte)b};
                }
            }
            images.add(ImageUtils.bytesToBufferedImage(retImage));
        }
        BufferedImage[] bufferedImages = new BufferedImage[images.size()];
        bufferedImages = images.toArray(bufferedImages);
        try {
            ImageUtils.saveGifBuffered(bufferedImages, new GifDecoder(), "res/sunday.gif", 1000, true);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void testPlotImage(){
        String fileLocation = "res/sunday.png";
        byte[][][] image = null;
        try {
            image = ImageUtils.loadImageBytes(fileLocation);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        List<double[]> allData = new ArrayList<>();
        for(int ROW = 0; ROW < image.length; ROW++){
            for(int COL = 0; COL < image[0].length; COL++){
                byte[] pixel = image[ROW][COL];
                double r = pixel[0];
                double g = pixel[1];
                double b = pixel[2];
                while(r < 0){
                    r += 256;
                }
                while(g < 0){
                    g += 256;
                }
                while(b < 0){
                    b += 256;
                }
                allData.add(new double[]{r,g,b});
            }
        }
        int numSample = 10000;
        List<double[]> data = new ArrayList<>();
        while(data.size() < numSample){
            data.add(allData.get((int)(Math.random() * (allData.size() - 1))));
        }
        double[][] dataArray = new double[data.size()][];
        dataArray = data.toArray(dataArray);
        int numClusters = 256;
        List<KMeans> allKMeans = getKMeans(numClusters, dataArray, 1);
        Collections.sort(allKMeans, Comparator.comparingDouble(KMeans::getVariance));
        List<Cluster> clusters = allKMeans.get(0).getClusters();
        Plot3D.CartesianPlot plot = new Plot3D.CartesianPlot();
        List<PlotElement3D> plotElements = new ArrayList<>();
        for(Cluster cluster : clusters){
            double[] mean = cluster.getMean();
            while(mean[0] < 0){
                mean[0] += 256;
            }
            while(mean[1] < 0){
                mean[1] += 256;
            }
            while(mean[2] < 0){
                mean[2] += 256;
            }
            double r = mean[0] / 256.0;
            double g = mean[1] / 256.0;
            double b = mean[2] / 256.0;
            Log.d(TAG, "Mean:" + Arrays.toString(mean));
            Log.d(TAG, "RGB:" + Arrays.toString(new double[]{r,g,b}));
            javafx.scene.paint.Color color = new javafx.scene.paint.Color(r, g, b, 1.0);
            List<Point3d> points = new ArrayList<>();
            for(double[] point : cluster.points){
                while(point[0] < 0){
                    point[0] += 256;
                }
                while(point[1] < 0){
                    point[1] += 256;
                }
                while(point[2] < 0){
                    point[2] += 256;
                }
                double x = 20.0 * (point[0] / 256.0 - 0.5);
                double y = 20.0 * (point[1] / 256.0 - 0.5);
                double z = 20.0 * (point[2] / 256.0 - 0.5);
                points.add(new Point3d(x,y,z));
            }
            PlotDataset3D plotDataset3D = new PlotDataset3D(color, points);
            plotElements.add(plotDataset3D);
        }
        PlotElement3D[] plotElementArray = new PlotElement3D[plotElements.size()];
        plotElementArray = plotElements.toArray(plotElementArray);
        plot = new Plot3D.CartesianPlot(plotElementArray);
        plot.useAxes = false;
        plot.show();
    }

    public static void main(String[] args) {
        testImageCluster();
    }

}
