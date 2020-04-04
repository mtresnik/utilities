package com.resnik.util.math.plot;

import com.resnik.util.images.ImageUtils;
import com.resnik.util.logger.Log;
import com.resnik.util.math.plot.elements3d.*;
import com.resnik.util.math.plot.points.Point3d;
import com.resnik.util.math.symbo.ComplexNumber;
import com.resnik.util.math.symbo.operations.Constant;
import com.resnik.util.math.symbo.operations.Operation;
import com.resnik.util.math.symbo.operations.Point;
import com.resnik.util.math.symbo.operations.Variable;
import com.resnik.util.math.symbo.operations.functions.Sine;
import javafx.scene.paint.Color;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class TestPlot3D {

    public static final String TAG = TestPlot3D.class.getSimpleName();

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

        PlotLineSet3D plotLineSet3D = new PlotLineSet3D();
        List<Point3d> basePoints = getRandomProjection();
        List<Line3d> baseLines = connectClosest(basePoints, 4);
        List<Point3d> allPoints = new ArrayList<>();
        allPoints.addAll(basePoints);
        List<Line3d> allLines = new ArrayList<>();
        allLines.addAll(baseLines);
        double delta = 2;
        List<Point3d> level1 = elevate(basePoints, delta, Color.BLUE);
        allPoints.addAll(level1);
        List<Line3d> level1Lines = connectLevels(basePoints, level1, Color.LIGHTBLUE);
        allLines.addAll(level1Lines);
        List<Line3d> elevatedLines1 = elevate(baseLines, delta);
        allLines.addAll(elevatedLines1);

        List<Point3d> level2 = elevate(level1, delta, Color.BLUE);
        allPoints.addAll(level2);
        List<Line3d> level2Lines = connectLevels(level1, level2, Color.LIGHTBLUE);
        allLines.addAll(level2Lines);
        List<Line3d> elevatedLines2 = elevate(baseLines, delta*2);
        allLines.addAll(elevatedLines2);

        List<Point3d> level3 = elevate(level2, delta, Color.BLUE);
        allPoints.addAll(level3);
        List<Line3d> level3Lines = connectLevels(level2, level3, Color.LIGHTBLUE);
        allLines.addAll(level3Lines);
        List<Line3d> elevatedLines3 = elevate(baseLines, delta*3);
        allLines.addAll(elevatedLines3);


        plotLineSet3D.setPoints(allPoints);
        plotLineSet3D.setLines(allLines);
        Plot3D.CartesianPlot plot = new Plot3D.CartesianPlot(plotLineSet3D);
        plot.useAxes = false;
        plot.show();
    }

    private static List<Line3d> connectLevels(List<Point3d> points, List<Point3d> pointsOther, Color color){
        List<Line3d> retList = new ArrayList<>();
        for(int i = 0; i < points.size(); i++){
            Point3d a = points.get(i);
            Point3d b = pointsOther.get(i);
            Line3d currLine = new Line3d(color, a, b);
            retList.add(currLine);
        }
        return retList;
    }

    private static List<Line3d> elevate(List<Line3d> lines, double delta){
        List<Line3d> retList = new ArrayList<>();

        for(Line3d line : lines){
            Point3d a = line.a;
            Point3d b = line.b;
            Color color = line.color;
            Point3d a1 = new Point3d(a.x.real, a.y.real + delta, -a.z.real);
            Point3d b1 = new Point3d(b.x.real, b.y.real + delta, -b.z.real);
            Point3d aRat = new Point3d(a.x.real, a.y.real, -a.z.real);
            Point3d bRat = new Point3d(b.x.real, b.y.real, -b.z.real);
            Line3d line1 = new Line3d(color, a1, bRat);
            Line3d line2 = new Line3d(color, aRat, b1);
            Line3d line3 = new Line3d(color, a1, b1);
            retList.addAll(Arrays.asList(line1, line2, line3));
        }

        return retList;
    }

    private static List<Point3d> elevate(List<Point3d> points, double delta, Color color){
        List<Point3d> retList = new ArrayList<>();
        for(Point3d currPoint : points){
            retList.add(new Point3d(currPoint.x, ComplexNumber.a(currPoint.y.real + delta), currPoint.z, color));
        }

        return retList;
    }

    private static List<Line3d> connectClosest(List<Point3d> points, int num){
        List<Line3d> lines = new ArrayList<>();
        List<Point3d> subList = new ArrayList<>(points);
        double maxDist = - Double.MAX_VALUE;
        for(Point3d currPoint : points){
            subList.sort(new Comparator<Point3d>() {
                @Override
                public int compare(Point3d point3d, Point3d t1) {
                    return Double.compare(Point3d.dist(point3d, currPoint).real, Point3d.dist(t1, currPoint).real);
                }
            });
            List<Point3d> connectedList = new ArrayList<>();
            int count = 0;
            for(Point3d next : subList){
                if(count >= num){
                    break;
                }
                if(currPoint.equals(next)){
                    continue;
                }
                connectedList.add(next);
                count++;
            }
            for(Point3d to : connectedList){
                Line3d connection = new Line3d(Color.BLACK, currPoint, to);
                boolean contains = false;
                for(Line3d testLine : lines){
                    if(testLine.equals(connection)){
                        contains = true;
                        break;
                    }
                    if(testLine.a.equals(connection.a) && testLine.b.equals(connection.b)){
                        contains = true;
                        break;
                    }
                    if(testLine.a.equals(connection.b) && testLine.b.equals(connection.a)){
                        contains = true;
                        break;
                    }
                }
                if(!contains){
                    lines.add(connection);
                    maxDist = Math.max(maxDist, connection.dist());
                }
            }
        }
        for(Line3d line : lines){
            double gradient = 1.0 - Math.abs(line.dist() / maxDist);
            byte[] colorB = ImageUtils.gradientRYG(gradient);
            int red = (colorB[0] + 256) % 256;
            int green = (colorB[1] + 256) % 256;
            int blue = (colorB[2] + 256) % 256;
            Color color = Color.rgb(red, green, blue);
            line.color = color;
        }
        return lines;
    }

    private static List<Point3d> getRandomProjection(){
        List<Point3d> points = new ArrayList<>();
        for(int i = 0; i < 200; i++){
            points.add(new Point3d(2*(Math.random() - 0.5)*10, 0, 2*(Math.random() - 0.5)*10));
        }
        return points;
    }

    @Test
    public void testPlotLoad() throws ParserConfigurationException, SAXException, IOException {
        PlotLineSet3D plotLineSet3D = PlotLineSet3DXMLLoader.load("src/in/plot/testplot3d.xml");
        Log.v(TAG,plotLineSet3D);
    }


}
