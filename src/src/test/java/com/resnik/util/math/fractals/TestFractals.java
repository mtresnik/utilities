package com.resnik.util.math.fractals;

import org.junit.Test;

import java.io.IOException;

import static com.resnik.util.math.fractals.SquareFractal.generateSquareGif;
import static com.resnik.util.math.fractals.TriangleFractal.generateTriangleGif;

public class TestFractals {

    @Test
    public void testCircleFractal() throws IOException {
        CircleApproximation.SquareElement.testCircleSquare("src/res/fractals/circle_approx.gif");
    }

    @Test
    public void testSquareFractal() {
        generateSquareGif("src/res/fractals/squareTest.gif");
    }

    // Return three triangles and one dark triangle
    // For each return, repeat the algorithm
    @Test
    public void testTriangle() {
        generateTriangleGif("src/res/fractals/triangle.gif", 6);
    }
}
