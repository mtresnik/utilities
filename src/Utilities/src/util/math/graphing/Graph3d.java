package util.math.graphing;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Line;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class Graph3d extends Application {

    // size of graph
    int size = 400;

    // variables for mouse interaction
    private double mousePosX, mousePosY;
    private double mouseOldX, mouseOldY;
    private final Rotate rotateX = new Rotate(20, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(-45, Rotate.Y_AXIS);
    private Axis xAxis, yAxis, zAxis;
    private List<Text> xLabels, yLabels, zLabels;
    private List<Text> xLabelsTEMP, yLabelsTEMP, zLabelsTEMP;
    private double xMin, xMax, yMin, yMax, zMin, zMax;
    private int xTicks, yTicks, zTicks;
    private Camera camera;
    private Stage stage;

    @Override
    public void start(Stage primaryStage) {

        this.stage = primaryStage;
        this.camera = new PerspectiveCamera();
        // create axis walls
        int axesSize = 900;
        this.xMin = -10;
        this.xMax = 10;
        this.xTicks = 20;

        this.yMin = -10;
        this.yMax = 10;
        this.yTicks = 20;

        this.zMin = -10;
        this.zMax = 10;
        this.zTicks = 20;

        this.xTicks = (xTicks % 2 != 0 ? xTicks - 1 : xTicks);
        this.yTicks = (yTicks % 2 != 0 ? yTicks - 1 : yTicks);
        this.zTicks = (zTicks % 2 != 0 ? zTicks - 1 : zTicks);

        Group axes = createAxes(axesSize);
//        System.out.println(yLabelsTEMP);
        // initial cube rotation
        axes.getTransforms().addAll(rotateX, rotateY);
        // add objects to scene
        StackPane root = new StackPane();

        root.getChildren().add(axes);
        root.getChildren().addAll(yLabels);

        // perlin noise
        float[][] noiseArray = createNoise(size);

        // mesh
        TriangleMesh mesh = new TriangleMesh();

        // create points for x/z
        float amplification = 100; // amplification of noise

        for (int x = 0; x < size; x++) {
            for (int z = 0; z < size; z++) {
                mesh.getPoints().addAll(x, noiseArray[x][z] * amplification, z);
            }
        }

        // texture
        //<editor-fold defaultstate="collapsed" desc="comment">
        int length = size;
        float total = length;

        for (float x = 0; x < length - 1; x++) {
            for (float y = 0; y < length - 1; y++) {

                float x0 = x / total;
                float y0 = y / total;
                float x1 = (x + 1) / total;
                float y1 = (y + 1) / total;

                mesh.getTexCoords().addAll( //
                        x0, y0, // 0, top-left
                        x0, y1, // 1, bottom-left
                        x1, y1, // 2, top-right
                        x1, y1 // 3, bottom-right
                );

            }
        }
//</editor-fold>

        // faces
        //<editor-fold defaultstate="collapsed" desc="faces">
        for (int x = 0; x < length - 1; x++) {
            for (int z = 0; z < length - 1; z++) {

                int tl = x * length + z; // top-left
                int bl = x * length + z + 1; // bottom-left
                int tr = (x + 1) * length + z; // top-right
                int br = (x + 1) * length + z + 1; // bottom-right

                int offset = (x * (length - 1) + z) * 8 / 2; // div 2 because we have u AND v in the list

                // working
                mesh.getFaces().addAll(bl, offset + 1, tl, offset + 0, tr, offset + 2);
                mesh.getFaces().addAll(tr, offset + 2, br, offset + 3, bl, offset + 1);

            }
        }
        //</editor-fold>

        // material
        Image diffuseMap = createImage(size, noiseArray);

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(diffuseMap);
        material.setSpecularColor(Color.WHITE);

        // mesh view
        MeshView meshView = new MeshView(mesh);
        meshView.setTranslateX(-0.5 * size);
        meshView.setTranslateZ(-0.5 * size);
        meshView.setMaterial(material);
        meshView.setCullFace(CullFace.NONE);
        meshView.setDrawMode(DrawMode.FILL);
        meshView.setDepthTest(DepthTest.ENABLE);

        axes.getChildren().addAll(meshView);
        // testing / debugging stuff: show diffuse map on chart
        ImageView iv = new ImageView(diffuseMap);
        iv.setTranslateX(-0.5 * size);
        iv.setTranslateY(-0.10 * size);
        iv.setRotate(90);
        iv.setRotationAxis(new Point3D(1, 0, 0));
//        axes.getChildren().add(iv);

        // scene
        int screenWidth = 1280, screenHeight = 720;
        Scene scene = new Scene(root, screenWidth, screenHeight, true, SceneAntialiasing.BALANCED);
        scene.setCamera(this.camera);

        //<editor-fold defaultstate="collapsed" desc="Events">
        scene.setOnMousePressed(me -> {
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
            updateText();
        });
        scene.setOnMouseDragged(me -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            rotateX.setAngle(rotateX.getAngle() + (mousePosY - mouseOldY));
            rotateY.setAngle(rotateY.getAngle() - (mousePosX - mouseOldX));
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            updateText();
        });
        ChangeListener<Number> resizeListener = (obs, oldVal, newVal) -> {
            double width = primaryStage.getWidth();
            double height = primaryStage.getHeight();
            if (width == 0 || height == 0) {
                return;
            }
            double trans = axes.getBoundsInParent().getWidth();
            double scale = Math.min(width, height) / trans;
            scale *= 0.8;
            root.scaleXProperty().set(scale);
            root.scaleYProperty().set(scale);
            root.scaleZProperty().set(scale);
            updateText();
        };
//</editor-fold>

        primaryStage.widthProperty().addListener(resizeListener);
        primaryStage.heightProperty().addListener(resizeListener);

        makeZoomable(root);

        primaryStage.setResizable(true);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public void updateText() {
        // Maps text onto axes using ticks and Text lists.
        for (int i = 0; i < this.yLabelsTEMP.size(); i++) {
            Node from = yLabelsTEMP.get(i);
            Node to = yLabels.get(i);
            Bounds b = from.localToScene(from.getBoundsInLocal());
            Bounds c = to.localToScene(to.getBoundsInLocal());

        }
    }

    /**
     * Create texture for uv mapping
     *
     * @param size
     * @param noise
     * @return
     */
    public Image createImage(double size, float[][] noise) {

        int width = (int) size;
        int height = (int) size;

        WritableImage wr = new WritableImage(width, height);
        PixelWriter pw = wr.getPixelWriter();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                float value = noise[x][y];

                double gray = normalizeValue(value, -.5, .5, 0., 1.);

                gray = clamp(gray, 0, 1);

                Color color = Color.RED.interpolate(Color.YELLOW, gray);

                pw.setColor(x, y, color);

            }
        }

        return wr;

    }

    /**
     * Axis wall
     */
    public static class Axis extends Pane {

        Rectangle wall;

        public Axis(double size, int uTicks, int vTicks) {

            // wall
            // first the wall, then the lines => overlapping of lines over walls
            // works
            wall = new Rectangle(size, size);
            getChildren().add(wall);

            // grid
            double zTranslate = 0;
            double lineWidth = 1.0;
            Color gridColor = Color.BLACK;

            for (int x = 0; x <= size; x += size / uTicks) {

                Line line = new Line(0, 0, 0, size);
                line.setStroke(gridColor);
                line.setFill(gridColor);
                line.setTranslateX(x);
                line.setTranslateZ(zTranslate);
                line.setStrokeWidth(lineWidth);

                getChildren().addAll(line);

            }
            for (int y = 0; y <= size; y += size / vTicks) {

                Line line = new Line(0, 0, size, 0);
                line.setStroke(gridColor);
                line.setFill(gridColor);
                line.setTranslateY(y);
                line.setTranslateZ(zTranslate);
                line.setStrokeWidth(lineWidth);

                getChildren().addAll(line);

            }

//            // labels
//            // TODO: for some reason the text makes the wall have an offset
//             for( int y=0; y <= size; y+=size/10) {
//            
//             Text text = new Text( ""+y);
//             text.setTranslateX(size + 10);
//            
//             text.setTranslateY(y);
//             text.setTranslateZ(zTranslate);
//            
//             getChildren().addAll(text);
//            
//             }
        }

        public void setFill(Paint paint) {
            wall.setFill(paint);
        }

    }

    public void makeZoomable(StackPane control) {

        final double MAX_SCALE = 20.0;
        final double MIN_SCALE = 0.1;

        control.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {

            @Override
            public void handle(ScrollEvent event) {

                double delta = 1.2;

                double scale = control.getScaleX();

                if (event.getDeltaY() < 0) {
                    scale /= delta;
                } else {
                    scale *= delta;
                }

                scale = clamp(scale, MIN_SCALE, MAX_SCALE);

                control.setScaleX(scale);
                control.setScaleY(scale);
                updateText();
                event.consume();

            }

        });

    }

    /**
     * Create axis walls
     *
     * @param size
     * @return
     */
    private Group createCube(int size) {

        Group cube = new Group();

        // size of the cube
        Color color = Color.DARKCYAN;

        List<Axis> cubeFaces = new ArrayList<>();
        Axis r;

        // back face        
        r = new Axis(size, yTicks, xTicks);
        r.setFill(color.deriveColor(0.0, 1.0, (1 - 0.5 * 1), 1.0));
        r.setTranslateX(-0.5 * size);
        r.setTranslateY(-0.5 * size);
        r.setTranslateZ(0.5 * size);

        for (int y = 0; y <= size; y += size / 10) {

            Text text = new Text("" + y);
            text.setTranslateX(-0.5 * size / 4 - text.getBoundsInLocal().getWidth() + 40);

            text.setTranslateY(-y + size);
            text.setTranslateZ(0);

            r.getChildren().addAll(text);

        }

        cubeFaces.add(r);

        // bottom face
        r = new Axis(size, xTicks, yTicks);

        r.setFill(color.deriveColor(0.0, 1.0, (1 - 0.4 * 1), 1.0));
        r.setTranslateX(-0.5 * size);
        r.setTranslateY(0);
        r.setRotationAxis(Rotate.X_AXIS);
        r.setRotate(90);

        cubeFaces.add(r);

        // right face
        r = new Axis(size, zTicks, yTicks);

        r.setFill(color.deriveColor(0.0, 1.0, (1 - 0.3 * 1), 1.0));
        r.setTranslateX(-1 * size);
        r.setTranslateY(-0.5 * size);
        r.setRotationAxis(Rotate.Y_AXIS);
        r.setRotate(90);

        // cubeFaces.add( r);
        // left face
        r = new Axis(size, zTicks, xTicks);

        r.setFill(color.deriveColor(0.0, 1.0, (1 - 0.2 * 1), 1.0));
        r.setTranslateX(0);
        r.setTranslateY(-0.5 * size);
        r.setRotationAxis(Rotate.Y_AXIS);
        r.setRotate(90);

        cubeFaces.add(r);

        cube.getChildren().addAll(cubeFaces);

        return cube;
    }

    private Group createAxes(int size) {

        Group axes = new Group();

        // size of the cube
        Color color = Color.TRANSPARENT;

        List<Axis> cubeFaces = new ArrayList<>();
        Axis r;

        // back face
        r = new Axis(size, xTicks, yTicks);
        r.setFill(color.deriveColor(0.0, 1.0, (1 - 0.5 * 1), 1.0));
        double xVal = -0.5 * size, yVal = -0.5 * size, zVal = 0;
        r.setTranslateX(xVal);
        r.setTranslateY(yVal);
        r.setTranslateZ(zVal);
        yLabelsTEMP = new ArrayList();
        yLabels = new ArrayList();
        double DEC = Math.pow(10, 12);
        for (double y = 0; y <= size; y += size / yTicks) {
            double delta = (yMax - yMin) / yTicks;
            double index = yTicks * y / size;
            double num = delta * index + yMin;
            num = Math.floor(num * DEC) / DEC;
            Text text = new Text("" + num);
            text.setTranslateX(-0.5 * size / 4 - text.getBoundsInLocal().getWidth() + 100);

            text.setTranslateY(-y + size);
            text.setTranslateZ(0);
            text.setVisible(true);
            r.getChildren().addAll(text);
        }

//        textGroup.getTransforms().addAll(this.rotateNonX, this.rotateNonY);
        this.zAxis = r;
        cubeFaces.add(r);

        // bottom face
        r = new Axis(size, xTicks, zTicks);
        r.setFill(color.deriveColor(0.0, 1.0, (1 - 0.4 * 1), 1.0));
        r.setTranslateX(-0.5 * size);
        r.setTranslateY(-0.5 * size);
        r.setRotationAxis(Rotate.X_AXIS);
        r.setRotate(90);

        cubeFaces.add(r);

        // left face
        r = new Axis(size, xTicks, yTicks);
        r.setFill(color.deriveColor(0.0, 1.0, (1 - 0.2 * 1), 1.0));
        r.setTranslateX(-size * 0.5);
        r.setTranslateY(-0.5 * size);
        r.setRotationAxis(Rotate.Y_AXIS);
        r.setRotate(90);

        cubeFaces.add(r);

        // cubeFaces.add( r);
        axes.getChildren().addAll(cubeFaces);

        return axes;
    }

    private Group createTextAxes(int size) {

        Group axes = new Group();

        // size of the cube
        Color color = Color.TRANSPARENT;

        List<Axis> cubeFaces = new ArrayList<>();
        Axis r;

        r = new Axis(size, xTicks, yTicks);

        r.setFill(color.deriveColor(0.0, 1.0, (1 - 0.5 * 1), 1.0));
        r.setTranslateX(-0.5 * size);
        r.setTranslateY(-0.5 * size);
        r.setTranslateZ(0.5 * size);

        cubeFaces.add(r);

        axes.getChildren().addAll(cubeFaces);

        return axes;
    }

    /**
     * Create an array of the given size with values of perlin noise
     *
     * @param size
     * @return
     */
    private float[][] createNoise(int size) {
        float[][] noiseArray = new float[(int) size][(int) size];

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {

                double frequency = 10.0 / (double) size;

                double noise = ImprovedNoise.noise(x * frequency, y * frequency, 0);

                noiseArray[x][y] = (float) noise;
            }
        }

        return noiseArray;

    }

    public static double normalizeValue(double value, double min, double max, double newMin, double newMax) {

        return (value - min) * (newMax - newMin) / (max - min) + newMin;

    }

    public static double clamp(double value, double min, double max) {

        if (Double.compare(value, min) < 0) {
            return min;
        }

        if (Double.compare(value, max) > 0) {
            return max;
        }

        return value;
    }

    /**
     * Perlin noise generator
     *
     * // JAVA REFERENCE IMPLEMENTATION OF IMPROVED NOISE - COPYRIGHT 2002 KEN
     * PERLIN. // http://mrl.nyu.edu/~perlin/paper445.pdf //
     * http://mrl.nyu.edu/~perlin/noise/
     */
    public final static class ImprovedNoise {

        static public double noise(double x, double y, double z) {
            int X = (int) Math.floor(x) & 255, // FIND UNIT CUBE THAT
                    Y = (int) Math.floor(y) & 255, // CONTAINS POINT.
                    Z = (int) Math.floor(z) & 255;
            x -= Math.floor(x);                                // FIND RELATIVE X,Y,Z
            y -= Math.floor(y);                                // OF POINT IN CUBE.
            z -= Math.floor(z);
            double u = fade(x), // COMPUTE FADE CURVES
                    v = fade(y), // FOR EACH OF X,Y,Z.
                    w = fade(z);
            int A = p[X] + Y, AA = p[A] + Z, AB = p[A + 1] + Z, // HASH COORDINATES OF
                    B = p[X + 1] + Y, BA = p[B] + Z, BB = p[B + 1] + Z;      // THE 8 CUBE CORNERS,

            return lerp(w, lerp(v, lerp(u, grad(p[AA], x, y, z), // AND ADD
                    grad(p[BA], x - 1, y, z)), // BLENDED
                    lerp(u, grad(p[AB], x, y - 1, z), // RESULTS
                            grad(p[BB], x - 1, y - 1, z))),// FROM  8
                    lerp(v, lerp(u, grad(p[AA + 1], x, y, z - 1), // CORNERS
                            grad(p[BA + 1], x - 1, y, z - 1)), // OF CUBE
                            lerp(u, grad(p[AB + 1], x, y - 1, z - 1),
                                    grad(p[BB + 1], x - 1, y - 1, z - 1))));
        }

        static double fade(double t) {
            return t * t * t * (t * (t * 6 - 15) + 10);
        }

        static double lerp(double t, double a, double b) {
            return a + t * (b - a);
        }

        static double grad(int hash, double x, double y, double z) {
            int h = hash & 15;                      // CONVERT LO 4 BITS OF HASH CODE
            double u = h < 8 ? x : y, // INTO 12 GRADIENT DIRECTIONS.
                    v = h < 4 ? y : h == 12 || h == 14 ? x : z;
            return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
        }
        static final int p[] = new int[512], permutation[] = {151, 160, 137, 91, 90, 15,
            131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23,
            190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33,
            88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139, 48, 27, 166,
            77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 46, 245, 40, 244,
            102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200, 196,
            135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123,
            5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42,
            223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9,
            129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228,
            251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239, 107,
            49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254,
            138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180
        };

        static {
            for (int i = 0; i < 256; i++) {
                p[256 + i] = p[i] = permutation[i];
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
