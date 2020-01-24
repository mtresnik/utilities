package com.resnik.util.images;

import javafx.scene.paint.Color;

public final class ColorUtils {

    public static String colorToHex(Color color) {
        return colorChannelToHex(color.getRed()) + colorChannelToHex(color.getGreen()) + colorChannelToHex(color.getBlue()) + colorChannelToHex(color.getOpacity());
    }

    private static String colorChannelToHex(double chanelValue) {
        String rtn = Integer.toHexString((int) Math.min(Math.round(chanelValue * 255), 255));
        if (rtn.length() == 1) {
            rtn = "0" + rtn;
        }
        return rtn;
    }

    private ColorUtils() {

    }

}
