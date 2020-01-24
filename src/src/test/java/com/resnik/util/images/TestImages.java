package com.resnik.util.images;

import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestImages {

    @Test
    public void testAscii(){
        try {
            byte[][][] giraffe_picture = ImageUtils.loadImageToByteArray("src/res/giraffe.jpg", BufferedImage.TYPE_INT_BGR);

            byte[][][] giraffe_ascii = ImageUtils.coloredASCII(giraffe_picture, BufferedImage.TYPE_INT_BGR);
            ImageUtils.saveImageFromByteArray(giraffe_ascii, "src/res/giraffe_ascii.jpg", BufferedImage.TYPE_INT_BGR);

        } catch (IOException ex) {
            Logger.getLogger(TestImages.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testSunglasses(){
        // Note: This takes a while
        try {
            GifDecoder gd = new GifDecoder();
            gd.read("src/res/sunglasses.gif");
            BufferedImage[] buff = ImageUtils.gifDecoderToBufferedImages(gd);
            BufferedImage[] buff_colored = ImageUtils.coloredASCII(buff);
            ImageUtils.saveGifBuffered(buff_colored, gd, "src/res/sunglasses_ascii.gif");
        } catch (IOException ex) {
            Logger.getLogger(TestImages.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
