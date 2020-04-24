package com.resnik.util.math.plot;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.resnik.util.math.plot.elements2d.PlotElement2D;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import com.resnik.util.math.symbo.algebra.Bounds;

public class Plot2D {

    public static class CartesianPlot extends Application {

        private List<PlotElement2D> graphElements;
        private ChangeListener<Number> stageSizeListener;
        private Stage stage;
        private Bounds xBounds, yBounds;
        private static final double SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight(),
                SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        private static final double DEFAULT_HEIGHT = Math.max(SCREEN_HEIGHT, SCREEN_WIDTH) / 4,
                DEFAULT_WIDTH = Math.max(SCREEN_HEIGHT, SCREEN_WIDTH) / 4;
        private final double initHeight, initWidth;

        public CartesianPlot(PlotElement2D... graphElements) {
            this(Bounds.DEFAULT_10, Bounds.DEFAULT_10, graphElements);
        }

        public CartesianPlot(double initHeight, double initWidth, PlotElement2D... graphElements) {
            this(initHeight, initWidth, Bounds.DEFAULT_10, Bounds.DEFAULT_10, graphElements);
        }

        public CartesianPlot(Bounds xBounds, Bounds yBounds, PlotElement2D... graphElements) {
            this(DEFAULT_HEIGHT, DEFAULT_WIDTH, xBounds, yBounds, graphElements);
        }

        public CartesianPlot(double initHeight, double initWidth, Bounds xBounds, Bounds yBounds, PlotElement2D... graphElements) {
            this.graphElements = new ArrayList(Arrays.asList(graphElements));
            this.xBounds = xBounds;
            this.yBounds = yBounds;
            this.initHeight = initHeight;
            this.initWidth = initWidth;
        }

        public void add(PlotElement2D graphElement) {
            graphElements.add(graphElement);
            update();
        }

        public void update() {
            if (this.stageSizeListener == null) {
//                Log.v(TAG,"stage size is null");
                if (this.stage != null) {
                    this.setupStage();
                    return;
                }
//                Log.v(TAG,"stage is null");
                return;
            }
            this.stageSizeListener.changed(null, 0.0, 0.0);
        }

        @Override
        public void start(final Stage stage) {
            this.stage = stage;
            setupStage();
            stage.show();
        }

        public void setupStage() {
            Axes axes = new Axes(
                    /*plot dimensions*/(int) initWidth, (int) initHeight,
                    /*x bounds*/ xBounds.min.real, xBounds.max.real, /*x tick*/ (xBounds.max.real - xBounds.min.real) / 20,
                    /*y bounds*/ yBounds.min.real, yBounds.max.real, /*y tick*/ (yBounds.max.real - yBounds.min.real) / 20
            );

            Plot plot = new Plot(
                    this.graphElements,
                    xBounds.min.real, xBounds.max.real, 0.01 * (xBounds.max.real - xBounds.min.real),
                    axes
            );

            StackPane layout = new StackPane(plot);
            layout.setPadding(new Insets(20));
            layout.setStyle("-fx-background-color: rgb(35, 39, 50);");

            stage.setTitle("");
            stage.setScene(new Scene(layout, Color.rgb(35, 39, 50)));

            stageSizeListener = (obs, oldVal, newVal) -> {
                Double width = stage.getWidth();
                Double height = stage.getHeight();
                if (width.equals(Double.NaN) || height.equals(Double.NaN)) {
                    return;
                }
                Axes newAxes = new Axes(
                        width.intValue() - 40, height.intValue() - 80,
                        axes.xLow, axes.xHi, axes.xTickUnit,
                        axes.yLow, axes.yHi, axes.yTickUnit
                );
                double xMin = plot.xMin, xMax = plot.xMax, xInc = plot.xInc;
                Plot newPlot = new Plot(graphElements, xMin, xMax, xInc, newAxes);
                StackPane newLayout = new StackPane(newPlot);
                newLayout.setStyle(layout.getStyle());
                stage.getScene().setRoot(newLayout);
                newLayout.setPadding(new Insets(20));
                stage.show();
            };
            stage.widthProperty().addListener(stageSizeListener);
            stage.heightProperty().addListener(stageSizeListener);
        }

        private static void startPlot(CartesianPlot plot) {
            new JFXPanel();
            Platform.runLater(
                    () -> {
                        try {
                            plot.start(new Stage());
                        } catch (Exception ex) {
                            Logger.getLogger(Plot2D.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
            );
            return;
        }

        public void show() {
            startPlot(this);
        }

        public static class Axes extends Pane {

            private NumberAxis xAxis;
            private NumberAxis yAxis;
            public double xLow, xHi, xTickUnit,
                    yLow, yHi, yTickUnit;
            public int width, height;

            public Axes(
                    int width, int height,
                    double xLow, double xHi, double xTickUnit,
                    double yLow, double yHi, double yTickUnit
            ) {
                this.width = width;
                this.height = height;
                this.xLow = xLow;
                this.xHi = xHi;
                this.xTickUnit = xTickUnit;
                this.yLow = yLow;
                this.yHi = yHi;
                this.yTickUnit = yTickUnit;
                setMinSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
                setPrefSize(width, height);
                setMaxSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);

                xAxis = new NumberAxis(xLow, xHi, xTickUnit);
                xAxis.setSide(Side.BOTTOM);
                xAxis.setMinorTickVisible(false);
                xAxis.setPrefWidth(width);
                xAxis.setLayoutY(height / 2);

                yAxis = new NumberAxis(yLow, yHi, yTickUnit);
                yAxis.setSide(Side.LEFT);
                yAxis.setMinorTickVisible(false);
                yAxis.setPrefHeight(height);
                yAxis.layoutXProperty().bind(
                        Bindings.subtract(
                                (width / 2) + 1,
                                yAxis.widthProperty()
                        )
                );

                getChildren().setAll(xAxis, yAxis);
            }

            public NumberAxis getXAxis() {
                return xAxis;
            }

            public NumberAxis getYAxis() {
                return yAxis;
            }
        }

        public static class Plot extends Pane {

            public List<PlotElement2D> graphElements;
            public double xMin, xMax, xInc;

            public Plot(
                    List<PlotElement2D> graphElements,
                    double xMin, double xMax, double xInc,
                    Axes axes
            ) {
                this.graphElements = graphElements;
                this.xMin = xMin;
                this.xMax = xMax;
                this.xInc = xInc;
                List<Shape> shapes = new ArrayList();
                for (PlotElement2D elem : graphElements) {
//                Log.v(TAG,pair);
                    Shape[] shapeArray = elem.getShapes(axes, this);
//                    Log.v(TAG,"shapes:" + Arrays.toString(shapeArray));
                    shapes.addAll(Arrays.asList(shapeArray));
                }

                setMinSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
                setPrefSize(axes.getPrefWidth(), axes.getPrefHeight());
                setMaxSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
                List<Node> all = new ArrayList();
                all.add(axes);
                for (Shape p : shapes) {
                    all.add(p);
                }
                getChildren().setAll(all);
            }

            public static double mapX(double x, Axes axes) {
                
                double m = axes.getPrefWidth() / (axes.xHi - axes.xLow);
                double mid = (axes.xHi + axes.xLow)/2;
                return (x  - mid)* m + axes.getPrefWidth() / 2;
            }

            public static double mapY(double y, Axes axes) {
                double m = axes.getPrefHeight()/ (axes.yHi - axes.yLow);
                double mid = (axes.yHi + axes.yLow)/2;
                return -(y  - mid)* m + axes.getPrefHeight()/ 2;
            }
        }

    }

}
