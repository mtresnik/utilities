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

public class KMeans {

    public static final String TAG = KMeans.class.getSimpleName();

    final int SIZE;
    List<Cluster> clusters;

    public KMeans(int SIZE, double[][] seedPoints) {
        this.SIZE = SIZE;
        if(seedPoints == null || seedPoints.length != SIZE){
            throw new IllegalArgumentException("Seed points must be of length:" + SIZE);
        }
        this.clusters = new ArrayList<>();
        for(double[] point : seedPoints){
            this.clusters.add(new Cluster(point));
        }
    }

    public List<Cluster> getClusters(){
        return clusters;
    }

    public List<Cluster> cluster(double[][] data){
        Set<Integer> clusterIndices = new LinkedHashSet<>();
        for(double[] point : data){
            int index = getClusterIndex(point);
            clusters.get(index).add(point);
            clusterIndices.add(index);
        }
        class MoveOperation{
            double[] point;
            int fromIndex;
            int toIndex;
        }
        // Ratify clusters
        List<MoveOperation> moveOperations = new ArrayList<>();
        for(int clusterIndex = 0; clusterIndex < clusters.size(); clusterIndex++){
            Cluster currentCluster = clusters.get(clusterIndex);
            for(double[] point : currentCluster.points){
                int expectedIndex = getClusterIndex(point);
                if(expectedIndex != clusterIndex){
                    MoveOperation moveOp = new MoveOperation();
                    moveOp.point = point;
                    moveOp.fromIndex = clusterIndex;
                    moveOp.toIndex = expectedIndex;
                    moveOperations.add(moveOp);
                }
            }
        }
        for(MoveOperation moveOperation : moveOperations){
            clusters.get(moveOperation.fromIndex).remove(moveOperation.point);
            clusters.get(moveOperation.toIndex).add(moveOperation.point);
        }
        List<Cluster> retList = new ArrayList<>();
        for(Integer index : clusterIndices){
            retList.add(clusters.get(index));
        }
        return retList;
    }

    public int getClusterIndex(double[] data){
        double minDistance = Double.MAX_VALUE;
        int clusterIndex = -1;
        for(int i = 0; i < clusters.size(); i++){
            Cluster cluster = clusters.get(i);
            double distance = cluster.distanceTo(data);
            if(distance < minDistance){
                minDistance = distance;
                clusterIndex = i;
            }
        }
        return clusterIndex;
    }

    public Cluster getCluster(double[] data){
        return clusters.get(getClusterIndex(data));
    }

    public double getVariance(){
        double sum = 0.0;
        for(Cluster cluster : clusters){
            sum += cluster.getVariance();
        }
        return sum;
    }

    private static Set<Integer> generateSeedIndices(int size, int length){
        Set<Integer> seedIndices = new LinkedHashSet<>();
        while(seedIndices.size() < size){
            seedIndices.add((int)(Math.random()*(length)));
        }
        return seedIndices;
    }

    private static double[][] getSeedPoints(int size, Set<Integer> seedIndices, double[][] data){
        double[][] seedPoints = new double[size][];
        int i = 0;
        for(Integer seedIndex : seedIndices){
            seedPoints[i] = data[seedIndex];
            i++;
        }
        return seedPoints;
    }

    private static double[][] getPointsArray(Set<Integer> seedIndices, double[][] data){
        List<double[]> points = new ArrayList<>();
        for(int i = 0; i < data.length; i++){
            if(seedIndices.contains(i)){
                continue;
            }
            points.add(data[i]);
        }
        double[][] pointsArray = new double[points.size()][];
        pointsArray = points.toArray(pointsArray);
        return pointsArray;
    }

    public static List<KMeans> getKMeans(int size, double[][] data, int iterations){
        List<KMeans> retList = new ArrayList<>();
        Set<Set<Integer>> seedPool = new LinkedHashSet<>();
        for(int iteration = 0; iteration < iterations; iteration++){
            Set<Integer> seedIndices = generateSeedIndices(size, data.length -1);
            while(seedPool.contains(seedIndices)){
                seedIndices = generateSeedIndices(size, data.length -1);
            }
            Log.d(TAG, "Seed Indices Found");
            seedPool.add(seedIndices);
            double[][] seedPoints = getSeedPoints(size, seedIndices, data);
            double[][] pointsArray = getPointsArray(seedIndices, data);
            KMeans current = new KMeans(size, seedPoints);
            current.cluster(pointsArray);
            retList.add(current);
            Log.d(TAG, "Variance:" + current.getVariance());
            Log.d(TAG, "Finished iteration:" + iteration);
        }
        return retList;
    }

    public static KMeans getBestKMeans(int size, double[][] data, int iterations){
        List<KMeans> allKMeans = getKMeans(size, data, iterations);
        Collections.sort(allKMeans, Comparator.comparingDouble(KMeans::getVariance));
        return allKMeans.get(0);
    }


}
