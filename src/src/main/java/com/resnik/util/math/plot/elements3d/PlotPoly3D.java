package com.resnik.util.math.plot.elements3d;

import com.resnik.util.math.plot.points.Point3d;
import com.resnik.util.math.symbo.operations.Constant;
import com.resnik.util.math.symbo.operations.Operation;
import com.resnik.util.math.symbo.operations.Variable;
import javafx.geometry.Point2D;
import javafx.scene.DepthTest;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.transform.Scale;
import org.fxyz3d.shapes.primitives.Function3DMesh;
import org.fxyz3d.shapes.primitives.SurfacePlotMesh;

import java.util.function.Function;

public class PlotPoly3D extends PlotElement3D {

    Operation polynomial;

    public PlotPoly3D(Color color, Operation polynomial) {
        super(color);
        this.color = this.color.interpolate(Color.TRANSPARENT, 0.25);
        this.polynomial = polynomial;
    }

    @Override
    public Node[] getShapes(Point3d axisRatio, Node o2) {
        return new Node[]{getFromCull(axisRatio, o2, CullFace.BACK)};
    }

    public Node getFromCull(Point3d axisRatio, Node o2, CullFace cullFace){
        Variable[] variables = this.polynomial.getVariables();
        Function<Point2D, Number> surface = (pt)->{
            Operation temp = null;
            if(variables.length == 0){
                temp = this.polynomial;
            }else {
                temp = this.polynomial.evaluate(variables[0], new Constant(pt.getX()));
                if(variables.length > 1){
                    temp = temp.evaluate(variables[1], new Constant(pt.getY()));
                }
            }
            double z = temp.constantRepresentation().getValue().real;
            double layoutz = z * axisRatio.z.real;
            if (layoutz > o2.getBoundsInParent().getMaxZ() / 2 || layoutz < o2.getBoundsInParent().getMinZ() / 2) {
                return Double.NaN;
            }
            return z;
        };
        SurfacePlotMesh surfacePlotMesh = new SurfacePlotMesh(surface);
        PhongMaterial colorMaterial = new PhongMaterial();
        colorMaterial.setDiffuseColor(this.color);
        colorMaterial.setSpecularColor(Color.TRANSPARENT);
        surfacePlotMesh.setMaterial(colorMaterial);
        surfacePlotMesh.setRangeX(20);
        surfacePlotMesh.setRangeY(20);
        surfacePlotMesh.setDepthTest(DepthTest.ENABLE);
        if(cullFace != null){
            surfacePlotMesh.setCullFace(cullFace);
        }

        Function3DMesh mesh = new Function3DMesh(surfacePlotMesh, false);
        Scale scale = new Scale();
        scale.setPivotX(0.0);
        scale.setPivotY(0.0);
        scale.setPivotZ(0.0);
        scale.setX(axisRatio.x.real);
        scale.setY(-axisRatio.y.real);
        scale.setZ(-axisRatio.z.real);
        mesh.getTransforms().add(scale);

        return mesh;
    }
}
