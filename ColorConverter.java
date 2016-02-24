import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

public class ColorConverter {

    private static final double[][] RGB_TO_YUV_MATRIX = {
        {0.299, 0.587, 0.114},
        {-0.14713, -0.28886, 0.436},
        {0.615, -0.51499, -0.10001}
    };

    private static final double[][] YUV_TO_RGB_MATRIX = {
        {1, 0, 1.13983},
        {1, -0.39465, -0.58060},
        {1, 2.03211, 0}
    };

    public static PixelBlock YUVtoRGB(PixelBlock original) {
        int[][][] yuvPixels = original.getAllChannels();
        PixelBlock rgbPixels = new PixelBlock();

        for (int i = 0; i < PixelBlock.HEIGHT; i++) {
            for (int j = 0; j < PixelBlock.WIDTH; j++) {
                int y = yuvPixels[i][j][PixelBlock.Y];
                int u = yuvPixels[i][j][PixelBlock.U];
                int v = yuvPixels[i][j][PixelBlock.V];

                int r = (int) (YUV_TO_RGB_MATRIX[0][0] * y + YUV_TO_RGB_MATRIX[0][1] * u + 
                    YUV_TO_RGB_MATRIX[0][2] * v);
                int g = (int) (YUV_TO_RGB_MATRIX[1][0] * y + YUV_TO_RGB_MATRIX[1][1] * u + 
                    YUV_TO_RGB_MATRIX[1][2] * v);
                int b = (int) (YUV_TO_RGB_MATRIX[2][0] * y + YUV_TO_RGB_MATRIX[2][1] * u + 
                    YUV_TO_RGB_MATRIX[2][2] * v);

                int[] rgb = {r, g, b};
                rgbPixels.setPixel(i, j, rgb);
            }
        }

        return rgbPixels;
    }

    public static PixelBlock RGBtoYUV(PixelBlock original) {
        int[][][] rgbPixels = original.getAllChannels();
        PixelBlock yuvPixels = new PixelBlock();

        for (int i = 0; i < PixelBlock.HEIGHT; i++) {
            for (int j = 0; j < PixelBlock.WIDTH; j++) {
                int r = rgbPixels[i][j][PixelBlock.R];
                int g = rgbPixels[i][j][PixelBlock.G];
                int b = rgbPixels[i][j][PixelBlock.B];

                int y = (int) (RGB_TO_YUV_MATRIX[0][0] * r + RGB_TO_YUV_MATRIX[0][1] * g + 
                    RGB_TO_YUV_MATRIX[0][2] * b);
                int u = (int) (RGB_TO_YUV_MATRIX[1][0] * r + RGB_TO_YUV_MATRIX[1][1] * g + 
                    RGB_TO_YUV_MATRIX[1][2] * b);
                int v = (int) (RGB_TO_YUV_MATRIX[2][0] * r + RGB_TO_YUV_MATRIX[2][1] * g + 
                    RGB_TO_YUV_MATRIX[2][2] * b);

                int[] yuv = {y, u, v};
                yuvPixels.setPixel(i, j, yuv);
            }
        }
        return yuvPixels;
    }
}
