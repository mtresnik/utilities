package com.resnik.util.math.plot;

import com.resnik.util.math.plot.elements3d.Mesh3d;
import com.resnik.util.math.plot.elements3d.PlotDataset3D;
import com.resnik.util.math.plot.elements3d.PlotPoly3D;
import com.resnik.util.math.plot.elements3d.Sphere;
import com.resnik.util.math.plot.points.Point3d;
import com.resnik.util.math.symbo.operations.Constant;
import com.resnik.util.math.symbo.operations.Operation;
import com.resnik.util.math.symbo.operations.Variable;
import com.resnik.util.math.symbo.operations.functions.Sine;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class TestPlot3D {


    public static void main(String[] args) {
        List<Point3d> points = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            points.add(new Point3d(2*(Math.random() - 0.5)*10, 2*(Math.random() - 0.5)*10, 2*(Math.random() - 0.5)*10));
        }
        System.out.println(points);
        PlotDataset3D dataset3D = new PlotDataset3D(Color.BLUE, points);
        dataset3D.setLines(true);

        Mesh3d mesh = new Mesh3d(Color.RED, new Point3d(1,2,3), new Point3d(2,4,1), new Point3d(10,5,6));

        Operation poly = (Variable.X.pow(Constant.TWO).add(Variable.Y.pow(Constant.TWO)));
        PlotPoly3D graph1 = new PlotPoly3D(Color.RED, poly);

        Operation poly2 = new Sine(Variable.X.multiply(Variable.Y));
        PlotPoly3D graph2 = new PlotPoly3D(Color.BLUE, poly2);

        Operation poly3 = Variable.X.multiply(Variable.Y);
        PlotPoly3D graph3 = new PlotPoly3D(Color.GREEN, poly3);

        Sphere sphere = new Sphere(Color.GREEN, 10, new Point3d(1,1,1));

        Plot3D.CartesianPlot plot = new Plot3D.CartesianPlot(graph3);
        plot.show();
    }

}
