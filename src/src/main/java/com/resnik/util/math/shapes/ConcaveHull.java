package com.resnik.util.math.shapes;

import com.resnik.util.images.ImageUtils;
import com.resnik.util.logger.Log;
import com.resnik.util.math.stats.StatisticsUtils;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;

public class ConcaveHull {

    public double minX, minY;
    public double maxX, maxY;
    List<PolygonPoint> points;
    List<PolygonEdge> edges;
    List<Triangle> partition;
    public String name;
    public int count;

    private static class PolygonEdge{
        PolygonPoint p1, p2;
        double minX, minY;
        double maxX, maxY;

        public PolygonEdge(PolygonPoint p1, PolygonPoint p2) {
            this.p1 = p1;
            this.p2 = p2;
            this.minX = Math.min(p1.x, p2.x);
            this.minY = Math.min(p1.y, p2.y);
            this.maxX = Math.max(p1.x, p2.x);
            this.maxY = Math.max(p1.y, p2.y);
        }

        public List<PolygonPoint> getPoints(double dt){
            List<PolygonPoint> ret = new ArrayList<>();
            for(double t = 0; t <= 1; t+= dt){
                ret.add(getPoint(t));
            }
            return ret;
        }

        public PolygonPoint getPoint(double t){
            double dx = p2.x - p1.x;
            double dy = p2.y - p1.y;
            double x = dx * t + p1.x;
            double y = dy * t + p1.y;
            return new PolygonPoint(x, y);
        }

        public double distance(){
            double dx = p2.x - p1.x;
            double dy = p2.y - p1.y;
            return Math.sqrt(dx*dx + dy*dy);
        }

    }

    public ConcaveHull(PolygonPoint ... points){
        this(Arrays.asList(points));
    }

    public ConcaveHull(List<PolygonPoint> points){
        this.points = points;
        this.minX = Double.MAX_VALUE;
        this.minY = Double.MAX_VALUE;
        this.maxX = - Double.MAX_VALUE;
        this.maxY = - Double.MAX_VALUE;
        for(PolygonPoint point : points){
            this.minX = Math.min(point.x, minX);
            this.minY = Math.min(point.y, minY);
            this.maxX = Math.max(point.x, maxX);
            this.maxY = Math.max(point.y, maxY);
        }
        this.edges = new ArrayList<>();
        for(int i = 0; i <= points.size(); i++){
            int index = i % points.size();
            int nextIndex = (i + 1) % points.size();
            PolygonPoint curr = points.get(index);
            PolygonPoint next = points.get(nextIndex);
            this.edges.add(new PolygonEdge(curr, next));
        }
    }

    public boolean contains(double x, double y){
        if ( x < minX || x > maxX || y < minY || y > maxY )
        {
            return false;
        }

        // https://wrf.ecse.rpi.edu/Research/Short_Notes/pnpoly.html
        boolean inside = false;
        for ( int i = 0, j = points.size() - 1 ; i < points.size() ; j = i++ )
        {
            if ( ( points.get(i).y > y ) != ( points.get(j).y > y ) &&
                    x < ( points.get(j).x - points.get(i).x ) * ( y - points.get(i).y ) / ( points.get(j).y - points.get(i).y ) + points.get(i).x )
            {
                inside = !inside;
            }
        }

        return inside;
    }

    public byte[][][] toImage(int WIDTH, int HEIGHT){
        byte[][][] ret = new byte[HEIGHT][WIDTH][];
        for(int ROW = 0; ROW < HEIGHT; ROW++){
            for(int COL = 0; COL < WIDTH; COL++){
                ret[ROW][COL] = ImageUtils.WHITE_B;
            }
        }
        int offset = 10;
        Function<Double, Integer> getCol = (x) -> (int)(((WIDTH - 1 - 2*offset)/(maxX - minX)) * ( x - minX) + offset);
        Function<Double, Integer> getRow = (y) -> (int)(((HEIGHT - 1 - 2*offset)/(minY - maxY)) * ( y - maxY) + offset);

        double screenDistance = Math.sqrt(Math.pow(WIDTH, 2) + Math.pow(HEIGHT,2));
        double dx = maxX - minX;
        double dy = maxY - minY;
        double pointDistance = Math.sqrt(dx*dx + dy*dy);
        double diagonalSlope = screenDistance / pointDistance;

        for(PolygonEdge edge : edges){
            double dist = edge.distance();
            double numPixels = diagonalSlope * dist* 2;
            double dt = 1/numPixels;
            List<PolygonPoint> currPoints = edge.getPoints(dt);
            for(PolygonPoint point : currPoints){
                int col = getCol.apply(point.x);
                int row = getRow.apply(point.y);
                ret[row][col] = ImageUtils.RED_B;
            }
        }
        int num = 1000;
        for(double x = minX; x < maxX; x+= dx / num){
            for(double y = minY; y < maxY; y+= dy / num){
                if(this.contains(x, y)){
                    int col = getCol.apply(x);
                    int row = getRow.apply(y);
                    ret[row][col] = new byte[]{(byte) 0, (byte) 0, (byte) 100, (byte) 255};
                }

            }
        }
        return ret;
    }

    public static byte[][][] commit(int WIDTH, int HEIGHT, Map<ConcaveHull, byte[]> colorMap, List<double[]> highlightLocations, List<double[]> nextLocations, Map<String, Double> populationMap, Map<String, Double> incomeMap){
        byte[][][] ret = new byte[HEIGHT][WIDTH][];
        for(int ROW = 0; ROW < HEIGHT; ROW++){
            for(int COL = 0; COL < WIDTH; COL++){
                ret[ROW][COL] = ImageUtils.WHITE_B;
            }
        }
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = - Double.MAX_VALUE;
        double maxY = - Double.MAX_VALUE;
        List<Map.Entry<ConcaveHull, byte[]>> entryList = new ArrayList<>(colorMap.entrySet());
        List<ConcaveHull> hulls = new ArrayList<>();
        for(ConcaveHull hull : colorMap.keySet()){
            minX = Math.min(hull.minX, minX);
            minY = Math.min(hull.minY, minY);
            maxX = Math.max(hull.maxX, maxX);
            maxY = Math.max(hull.maxY, maxY);
            hulls.add(hull);
        }
        int offset = 10;
        double finalMinX = minX;
        double finalMaxX = maxX;
        Function<Double, Integer> getCol = (x) -> (int)(((WIDTH - 1 - 2*offset)/(finalMaxX - finalMinX)) * ( x - finalMinX) + offset);
        double finalMaxY = maxY;
        double finalMinY = minY;
        Function<Double, Integer> getRow = (y) -> (int)(((HEIGHT - 1 - 2*offset)/(finalMinY - finalMaxY)) * ( y - finalMaxY) + offset);

        double screenDistance = Math.sqrt(Math.pow(WIDTH, 2) + Math.pow(HEIGHT,2));
        double dx = maxX - minX;
        double dy = maxY - minY;
        double pointDistance = Math.sqrt(dx*dx + dy*dy);
        double diagonalSlope = screenDistance / pointDistance;


        for(int concaveIndex = 0; concaveIndex < hulls.size(); concaveIndex++){
            Log.d("ConcaveHull", "Hull:" + concaveIndex);
            ConcaveHull hull = hulls.get(concaveIndex);
            byte[] color = entryList.get(concaveIndex).getValue();
            int minCol = getCol.apply(hull.minX);
            int maxCol = getCol.apply(hull.maxX);
            int minRow = getRow.apply(hull.minY);
            int maxRow = getRow.apply(hull.maxY);
            int colNum = Math.abs(maxCol - minCol) + 1;
            int rowNum = Math.abs(maxRow - minRow) + 1;
            for(double x = minX; x < maxX; x+= (hull.maxX - hull.minX) / colNum){
                for(double y = minY; y < maxY; y+=(hull.maxY - hull.minY) / rowNum){
                    if(hull.contains(x, y)){
                        int col = getCol.apply(x);
                        int row = getRow.apply(y);
                        ret[row][col] = color;
                    }
                }
            }
        }
        double r = 5;

        for(ConcaveHull hull : hulls){
            for(PolygonEdge edge : hull.edges){
                double dist = edge.distance();
                double numPixels = diagonalSlope * dist* 2;
                double dt = 1/numPixels;
                List<PolygonPoint> currPoints = edge.getPoints(dt);
                for(PolygonPoint point : currPoints){
                    int col = getCol.apply(point.x);
                    int row = getRow.apply(point.y);
                    ret[row][col] = new byte[]{(byte) 100, (byte) 100, (byte) 100, (byte) 255};
                }
            }
        }

        for(ConcaveHull hull : hulls){
            for(double[] location : highlightLocations){
                double x = location[1];
                double y = location[0];
                if(hull.contains(x, y)){
                    int col = getCol.apply(x);
                    int row = getRow.apply(y);
                    for(int SUBROW = (int)(row - r); SUBROW < row + r; SUBROW++){
                        for(int SUBCOL = (int)(col - r); SUBCOL < col + r; SUBCOL++){
                            double dRow = SUBROW - row;
                            double dCol = SUBCOL - col;
                            double dist = Math.sqrt(dRow*dRow + dCol*dCol);
                            if(dist <= r){
                                ret[SUBROW][SUBCOL] = new byte[]{(byte) 128, (byte) 0, (byte) 0, (byte) 255};
                            }
                        }
                    }
                }
            }
            for(double[] location : nextLocations){
                double x = location[1];
                double y = location[0];
                if(hull.contains(x, y)){
                    hull.count++;
                    int col = getCol.apply(x);
                    int row = getRow.apply(y);
                    for(int SUBROW = (int)(row - r); SUBROW < row + r; SUBROW++){
                        for(int SUBCOL = (int)(col - r); SUBCOL < col + r; SUBCOL++){
                            double dRow = SUBROW - row;
                            double dCol = SUBCOL - col;
                            double dist = Math.sqrt(dRow*dRow + dCol*dCol);
                            if(dist <= r){
                                ret[SUBROW][SUBCOL] = new byte[]{(byte) 200, (byte) 200, (byte) 0, (byte) 255};
                            }
                        }
                    }
                }
            }
        }
        double[] supermarkets = new double[hulls.size()];
        double[] incomes = new double[hulls.size()];
        double[] populations = new double[hulls.size()];
        int index = 0;
        for(ConcaveHull hull : hulls){
            supermarkets[index] = hull.count;
            incomes[index] = incomeMap.get(hull.name);
            populations[index] = populationMap.get(hull.name);
            index++;
        }
        Log.e("ConcaveHull", "Income vs Population:" + StatisticsUtils.cor(incomes, populations));
        Log.e("ConcaveHull", "Supermarkets vs Income:" + StatisticsUtils.cor(supermarkets, incomes));
        Log.e("ConcaveHull", "Supermarkets vs Population:" + StatisticsUtils.cor(supermarkets, populations));

        return ret;
    }

    public List<Triangle> partition(){
        // Iterate over all edges, taking pairs starting from beginning.
//        if(points.size() == 3){
//            return Collections.singletonList(new Polygon(points));
//        }
//        List<PolygonPoint> local = new ArrayList<>(points);
//        PolygonPoint p1 = local.get(0);
//        PolygonPoint p2 = local.get(1);
//        PolygonPoint p3 = local.get(2);
//        List<Polygon> ret = new ArrayList<>();
//        ret.add(new Polygon(p1, p2, p3));
//        local.remove(1);
//        ConcaveHull subHull = new ConcaveHull(local);
//        ret.addAll(subHull.partition());
        if(partition != null){
            return partition;
        }
        Log.d("ConcaveHull", "Partition");
        List<Triangle> ret = new ArrayList<>();

        final int pointSize = points.size();
        if(pointSize < 3){
            return null;
        }
        int[][] indices = new int[pointSize - 1][3];
        boolean[] keep = new boolean[pointSize];

        int v1Index;
        double v1Value;
        for(int index = 0; index < pointSize - 2; ++index){
            v1Index = 0;
            v1Value = Double.NEGATIVE_INFINITY;
            for(int i = 0; i < pointSize; i++){
                if(!keep[i] && points.get(i).x > v1Value){
                    v1Value = points.get(i).x;
                    v1Index = i;
                }
            }
            keep[v1Index] = true;
            int prevIndex = v1Index;
            do{
                if(--prevIndex < 0)
                    prevIndex = pointSize - 1;
            }while (keep[prevIndex]);

            int nextIndex = v1Index;
            do{
                if(++nextIndex >= pointSize)
                    nextIndex = 0;
            }while (keep[nextIndex]);

            indices[index][0] = prevIndex;
            indices[index][1] = v1Index;
            indices[index][2] = nextIndex;
        }

        for(int[] triangle : indices){
            PolygonPoint p1 = points.get(triangle[0]);
            PolygonPoint p2 = points.get(triangle[1]);
            PolygonPoint p3 = points.get(triangle[2]);
            ret.add(new Triangle(p1,p2,p3));
        }
        this.partition = ret;
        return ret;
    }

    public static void main(String[] args) {
        List<PolygonPoint> points = new ArrayList<>();
        points.add(new PolygonPoint(0.5,0.5));
        points.add(new PolygonPoint(0.25,0.25));
        points.add(new PolygonPoint(0.5,0));
        points.add(new PolygonPoint(0,-0.5));
        points.add(new PolygonPoint(-0.5,0.5));
        Collections.reverse(points);
        ConcaveHull hull = new ConcaveHull(points);
        byte[][][] image = hull.toImage(500, 500);
        try {
            ImageUtils.saveImageBytes(image, "src/res/hull.png");
        } catch (IOException e) {
            e.printStackTrace();
        }


        List<Triangle> subPoly = hull.partition();
    }

}
