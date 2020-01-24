package com.resnik.util.math.plot;

import com.resnik.util.math.plot.elements3d.PlotElement3D;
import com.resnik.util.math.plot.points.Point3d;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Plot3D {

    public static class CartesianPlot extends Application {

        private List<PlotElement3D> graphElements;
        private com.resnik.util.math.symbo.Bounds xBounds, yBounds, zBounds;
        private double initHeight, initWidth;
        private static double DEFAULT_HEIGHT=720, DEFAULT_WIDTH = 1280;

        // variables for mouse interaction
        private double mousePosX, mousePosY;
        private double mouseOldX, mouseOldY;
        private final Rotate rotateX = new Rotate(20, Rotate.X_AXIS);
        private final Rotate rotateY = new Rotate(-45, Rotate.Y_AXIS);
        private Axis xAxis, yAxis, zAxis;
        private List<Text> xLabels, yLabels, zLabels;
        private List<Text> xLabelsTEMP, yLabelsTEMP, zLabelsTEMP;
        private int xTicks, yTicks, zTicks;
        private Camera camera;
        private Stage stage;


        public CartesianPlot(PlotElement3D... graphElements) {
            this(com.resnik.util.math.symbo.Bounds.DEFAULT_10, com.resnik.util.math.symbo.Bounds.DEFAULT_10, com.resnik.util.math.symbo.Bounds.DEFAULT_10, graphElements);
        }

        public CartesianPlot(double initHeight, double initWidth, PlotElement3D... graphElements) {
            this(initHeight, initWidth, com.resnik.util.math.symbo.Bounds.DEFAULT_10, com.resnik.util.math.symbo.Bounds.DEFAULT_10, com.resnik.util.math.symbo.Bounds.DEFAULT_10, graphElements);
        }

        public CartesianPlot(com.resnik.util.math.symbo.Bounds xBounds, com.resnik.util.math.symbo.Bounds yBounds, com.resnik.util.math.symbo.Bounds zBounds, PlotElement3D... graphElements) {
            this(DEFAULT_HEIGHT, DEFAULT_WIDTH, xBounds, yBounds, zBounds, graphElements);
        }

        public CartesianPlot(double initHeight, double initWidth, com.resnik.util.math.symbo.Bounds xBounds, com.resnik.util.math.symbo.Bounds yBounds, com.resnik.util.math.symbo.Bounds zBounds, PlotElement3D... graphElements) {
            super();
            this.graphElements = new ArrayList(Arrays.asList(graphElements));
            this.xBounds = xBounds;
            this.yBounds = yBounds;
            this.zBounds = zBounds;
            this.initHeight = initHeight;
            this.initWidth = initWidth;
        }


        private static void startPlot(Plot3D.CartesianPlot plot) {
            new JFXPanel();
            Platform.runLater(
                    () -> {
                        try {
                            plot.start(new Stage());
                        } catch (Exception ex) {
                            Logger.getLogger(Plot3D.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
            );
            return;
        }

        public void show() {
            startPlot(this);
        }

        @Override
        public void start(Stage primaryStage) {
            this.stage = primaryStage;
            this.camera = new PerspectiveCamera();
            // create axis walls
            int axesSize = 900;
            this.xTicks = (int)(this.xBounds.max.real - this.xBounds.min.real);

            this.yTicks = (int)(this.yBounds.max.real - this.yBounds.min.real);

            this.zTicks = (int)(this.zBounds.max.real - this.zBounds.min.real);;

            this.xTicks = (xTicks % 2 != 0 ? xTicks - 1 : xTicks);
            this.yTicks = (yTicks % 2 != 0 ? yTicks - 1 : yTicks);
            this.zTicks = (zTicks % 2 != 0 ? zTicks - 1 : zTicks);

            Group axes = createAxes(axesSize);
            axes.getTransforms().addAll(rotateX, rotateY);
            StackPane root = new StackPane();

            root.getChildren().add(axes);
            root.getChildren().addAll(yLabels);
            System.out.println(this.graphElements);
            for(PlotElement3D element : this.graphElements) {
                axes.getChildren().add(element.getGroup(new Point3d((1.0*axesSize)/xTicks, (1.0*axesSize)/yTicks, (1.0*axesSize)/zTicks), axes));
            }

            Scene scene = new Scene(root, initWidth, initHeight, true, SceneAntialiasing.BALANCED);
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
            scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if(event.getEventType().equals(KeyEvent.KEY_RELEASED)){
                        return;
                    }
                    if(event.getCode().equals(KeyCode.LEFT)){
                        System.out.println("left");
                        root.setTranslateX(root.getTranslateX() - 20);
                    }else if(event.getCode().equals(KeyCode.RIGHT)){
                        System.out.println("right");
                        root.setTranslateX(root.getTranslateX() + 20);

                    }else if(event.getCode().equals(KeyCode.UP)){
                        System.out.println("up");
                        root.setTranslateY(root.getTranslateY() - 20);

                    }else if(event.getCode().equals(KeyCode.DOWN)){
                        System.out.println("down");
                        root.setTranslateY(root.getTranslateY() + 20);
                    }else if(event.getCode().equals(KeyCode.C)){
                        root.setTranslateX(0);
                        root.setTranslateY(0);
                    }

                }
            });
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
                double delta = (this.yBounds.max.real - this.yBounds.min.real) / yTicks;
                double index = yTicks * y / size;
                double num = delta * index + this.yBounds.min.real;
                num = Math.floor(num * DEC) / DEC;
                Text text = new Text("" + num);
                text.setTranslateX(-0.5 * size / 4 - text.getBoundsInLocal().getWidth() + 100);

                text.setTranslateY(-y + size);
                text.setTranslateZ(0);
                text.setVisible(true);
                r.getChildren().addAll(text);
            }

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
    }

}
