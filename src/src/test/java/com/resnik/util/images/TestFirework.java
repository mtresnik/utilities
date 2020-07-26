package com.resnik.util.images;

import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestFirework {



    private class Projectile {

        private double x, y;
        private double vx, vy;
        private double ax, ay;
        private double mass;

        public Projectile(double ax, double ay, double mass) {
            this.ax = ax;
            this.ay = ay;
            this.mass = mass;
        }

        public double fx(){
            return ax*mass;
        }

        public double fy(){
            return ay*mass;
        }

        public double px(){
            return vx*mass;
        }

        public double py(){
            return vy*mass;
        }

    }


    @Test
    public void testProjectiles(){
        List<Projectile> ring = new ArrayList<>();
        final int WIDTH = 1000;
        final int HEIGHT = 1000;
        double numProjectiles = Math.min(WIDTH, HEIGHT) / 4.0;
        double acceleration = 5;
        int numSeconds = 15;
        double mass = 5;
        for(int i = 0; i < numProjectiles; i++){
            double t = i / numProjectiles;
            double theta = t * Math.PI * 2;
            double xHat = Math.cos(theta);
            double yHat = Math.sin(theta);
            Projectile projectile = new Projectile(xHat*acceleration, yHat*acceleration, mass);
            ring.add(projectile);
        }
        BufferedImage[] bufferedImages = new BufferedImage[numSeconds];
        byte[][][][] images = new byte[numSeconds][][][];
        for(int second = 0; second < numSeconds; second++){
            byte[][][] image = new byte[HEIGHT][WIDTH][];
            byte[][][] prevImage = null;
            if(second > 0){
                prevImage = images[second - 1];
            }
            for(int ROW = 0; ROW < HEIGHT; ROW++){
                for(int COL = 0; COL < WIDTH; COL++){
                    image[ROW][COL] = ImageUtils.WHITE_B;
                }
            }
            // update each projectile based on second
            for(Projectile projectile : ring){
                projectile.x = 0.5*projectile.ax*second*second;
                projectile.y = 0.5*(projectile.ay)*second*second;
                if(second > 0){
                    double prevx = 0.5*projectile.ax*(second - 1)*(second - 1);
                    double prevy = 0.5*projectile.ay*(second - 1)*(second - 1);
                    for(double t = 0; t < 1; t+=0.01){
                        double writeX = (projectile.x - prevx)*t + prevx;
                        double writeY = (projectile.y - prevy)*t + prevy;
                        int COL = (int)(writeX + WIDTH/2);
                        int ROW = (int)(writeY + HEIGHT/2);
                        try{
                            image[ROW][COL] = ImageUtils.RED_B;
                        }catch (ArrayIndexOutOfBoundsException ex){
                            continue;
                        }

                    }
                }
                // Convert xy to colrow
                int COL = (int)(projectile.x + WIDTH/2);
                int ROW = (int)(projectile.y + HEIGHT/2);
                try{
                    image[ROW][COL] = ImageUtils.RED_B;
                }catch (ArrayIndexOutOfBoundsException ex){
                    continue;
                }
            }
            images[second] = image;
            bufferedImages[second] = ImageUtils.bytesToBufferedImage(image);
        }
        GifDecoder gd = new GifDecoder();
        try {
            ImageUtils.saveGifBuffered(bufferedImages, gd, "src/res/firework.gif", 200, true);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
