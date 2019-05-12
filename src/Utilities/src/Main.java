
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import util.math.Algebraic;
import util.math.Bounds;
import util.math.Point2d;
import util.math.graphing.Graph2d.CartesianPlot;
import util.math.graphing.Graph2d.GraphDataset;
import util.math.graphing.Graph2d.GraphElement;
import util.math.graphing.Graph2d.GraphFunction;
import util.math.matrices;
import util.math.symbo.Addition;
import util.math.symbo.Constant;
import util.math.symbo.Division;
import util.math.symbo.Multiplication;
import util.math.symbo.Operation;
import util.math.symbo.Polynomial2d;
import util.math.symbo.Power;
import util.math.symbo.Sigma;
import util.math.symbo.Subtraction;
import util.math.symbo.Variable;
import util.math.symbo.Vector;
import util.math.symbo.interpolation.Regression;

public class Main {

    public static void main(String[] args) {
        testLinearRegression();
    }

    public static class Window extends Application {

        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        
        @Override
        public void start(Stage primaryStage) throws Exception {
            System.out.println("this:" + this);
            StackPane pane = new StackPane();
            primaryStage.setScene(new Scene(pane, 500, 500));
            primaryStage.show();
            primaryStage.onCloseRequestProperty().addListener(new ChangeListener(){
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    System.exit(0);
                }
            });
            System.out.println("name:" + this.name);
        }

    }

    public static void runWindow(Window w){
        new JFXPanel();
        Platform.runLater(
                () -> {
                    try {
                        System.out.println("w:" + w);
                        w.start(new Stage());
                    } catch (Exception ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
        );
    }
    
    public static void newWindow(){
        Window w = new Window();
        w.setName("name");
        runWindow(w);
        Timer t = new Timer();
        t.schedule(new TimerTask(){
            @Override
            public void run() {
                w.setName("james");
                t.cancel();
            }
        }, 1000);
    }
    
    public static void testNewClass() {
        newWindow();
    }

    public static void testSub() {
        Variable x = Variable.X, y = Variable.Y;
        Addition poly1 = new Addition(new Multiplication(x, new Constant(4)), new Multiplication(y, new Constant(3)));
        System.out.println(poly1);
        System.out.println(poly1.getDerivativeX());
        Operation poly2 = poly1.substitute(x, Power.var(x, x));
        System.out.println(poly2);
        System.out.println(poly2.getDerivativeX());
        Operation o1 = poly1.evaluate(x, new Constant(0.0));
        System.out.println(o1);
    }

    public static void testMatrix() {

        Variable x = Variable.X, y = Variable.Y;
        Addition poly1 = new Addition(x, y);

        Variable[] abcd = Variable.genVariables("a", "b", "c", "d");
        Algebraic[][] matTest = new Algebraic[][]{
            {abcd[0], abcd[1]},
            {abcd[2], abcd[3]}
        };
        Algebraic det = matrices.det(matTest);
        System.out.println(det);

    }

    public static void testDerivatives() {
        Variable f = new Variable("f"), g = new Variable("g");
        Addition testAdd = new Addition(f, g);
        Subtraction testSub = new Subtraction(f, g);
        Multiplication testMult = new Multiplication(f, g);
        Division testDiv = new Division(f, g);
        Power testPow = new Power(f, g);

        System.out.println("Addition:" + testAdd);
        System.out.println("derivative:" + testAdd.getDerivativeX());
        System.out.println("derivative^2:" + testAdd.getDerivativeX(2));
        System.out.println("derivative_f:" + testAdd.getDerivativeX(2).getDerivative(f));
        System.out.println("");
        System.out.println("Subtraction:" + testSub);
        System.out.println("derivative:" + testSub.getDerivativeX());
        System.out.println("derivative^2:" + testSub.getDerivativeX(2));
        System.out.println("");
        System.out.println("Multiplication:" + testMult);
        System.out.println("derivative:" + testMult.getDerivativeX());
        System.out.println("derivative^2:" + testMult.getDerivativeX(2));
        System.out.println("");
        System.out.println("Division:" + testDiv);
        System.out.println("derivative:" + testDiv.getDerivativeX());
        System.out.println("");
        System.out.println("Power:" + testPow);
        System.out.println("derivative:" + testPow.getDerivativeX());
    }

    public static void testLinearRegression() {
        Vector x = new Vector("x", Variable.I, 0);
        Operation o = x.average();
        System.out.println("average:" + o);
        Power p = new Power(x, new Constant(2));
        Sigma s = new Sigma(p, Variable.I);
        System.out.println(s.evaluateBoundsToString(x.getBounds()));
        Point2d[] points = Point2d.parsePoints(
                12.4, 11.2,
                14.3, 12.5,
                14.5, 12.7,
                14.9, 13.1,
                16.1, 14.1,
                16.9, 14.8,
                16.5, 14.4,
                15.4, 13.4,
                17, 14.9,
                17.9, 15.6,
                18.8, 16.4,
                20.3, 17.7,
                22.4, 19.6,
                19.4, 16.9,
                15.5, 14,
                16.7, 14.6,
                17.3, 15.1,
                18.4, 16.1,
                19.2, 16.8,
                17.4, 15.2,
                19.5, 17,
                19.7, 17.2,
                21.2, 18.6
        );
        Regression r = new Regression(1, points);
        r.plot();
        Vector[] x_y = Point2d.toVectors(points);
        System.out.println(Arrays.toString(points));
        System.out.println(Arrays.toString(x_y[0].getValues()));
        System.out.println(Arrays.toString(x_y[1].getValues()));
//        Regression.linearRegression(x_y[0], x_y[1]);
//        Polynomial2d poly = Regression.polynomialLinearRegression(10, x_y[0], x_y[1]);
        System.out.println("poly:" + r.polynomial);
        System.out.println("r^2:" + r.rSquared());
    }

    public static void testGraph2d(){
        
        GraphElement f1 = new GraphFunction((x) -> {
            return x;
        }, Color.BLUE), f2 = new GraphFunction((x) -> {
            return x * x;
        }, Color.RED), f3 = new GraphFunction((x) -> {
            return Math.sin(x);
        }, Color.GREEN), f4 = new GraphFunction((x) -> {
            return Math.cos(x);
        }, Color.YELLOW), d1 = new GraphDataset(Point2d.parsePoints(0.0, 1.0, 2.0, 3.0, 6.0, 7.0), Color.WHITE);

        CartesianPlot plot = new CartesianPlot(f1, f2, f3);
        plot.show();
        CartesianPlot plot1 = new CartesianPlot(new Bounds(-2 * Math.PI, 2 * Math.PI), new Bounds(-2, 2), f4);
        plot1.show();
//        CartesianPlot plot2 = new CartesianPlot(Bounds.DEFAULT_10, Bounds.DEFAULT_10, d1);
//        plot2.show();
        
        int max = 70;
        Point2d[] points = new Point2d[max];
        GraphFunction gf = new GraphFunction((x) -> {
            return x*x;
        }, Color.RED);
        for (int i = 0; i < max; i++) {
            double x = 0.1*(i - max/2);
            double y =  gf.j_function.apply(x)+ 2*(Math.random()  - 0.5);
            points[i] = new Point2d(x, y);
        }
        GraphElement ge = new GraphDataset(points, Color.WHITE);
        CartesianPlot plot3 = new CartesianPlot(Bounds.DEFAULT_10, Bounds.DEFAULT_10, ge, gf);
//        plot3.show();
        
    }
    
    
}
