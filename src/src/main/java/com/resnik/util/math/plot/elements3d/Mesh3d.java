package com.resnik.util.math.plot.elements3d;

import com.resnik.util.images.ImageUtils;
import com.resnik.util.math.plot.points.Point3d;
import javafx.scene.DepthTest;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.image.Image;

public class Mesh3d extends PlotElement3D {

    public Point3d[] points;

    public Mesh3d(Color color, Point3d ... points) {
        super(color);
        this.points = points;
    }

    @Override
    public Node[] getShapes(Point3d axisRatio, Node o2) {
        TriangleMesh mesh = new TriangleMesh();
        for(Point3d currPoint : this.points){
            mesh.getPoints().addAll((float) (currPoint.x.real*axisRatio.x.real), (float) (-currPoint.y.real*axisRatio.y.real), (float) (-currPoint.z.real*axisRatio.z.real));
        }
        Image diffuseMap = ImageUtils.createJavaFXImage(10,Color.RED);
        mesh.getTexCoords().addAll(
                0,0,0,1,1,0,1,1
        );
        mesh.getFaces().addAll(0,0,1,1,2,2);
        MeshView meshView = new MeshView(mesh);
        PhongMaterial colorMaterial = new PhongMaterial();
        colorMaterial.setDiffuseMap(diffuseMap);
        meshView.setMaterial(colorMaterial);
        meshView.setCullFace(CullFace.NONE);
        meshView.setDrawMode(DrawMode.FILL);
        meshView.setDepthTest(DepthTest.ENABLE);
        return new Node[]{meshView};
    }
}
