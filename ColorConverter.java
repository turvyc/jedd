import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

public class ColorConverter {

    private static final int N_CHANNELS = 4;

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

    public static BufferedImage YUVToRGB(WritableRaster original) {

        int width = original.getWidth();
        int height = original.getHeight();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        int[] ycbcr = new int[N_CHANNELS];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                ycbcr = original.getPixel(i, j, ycbcr);
                int y = ycbcr[0];
                int cb = ycbcr[1];
                int cr = ycbcr[2];

                System.out.printf("%d %d %d\n", y, cb, cr);
                int r = (int) (YUV_TO_RGB_MATRIX[0][0] * y + YUV_TO_RGB_MATRIX[0][1] * cb + 
                    YUV_TO_RGB_MATRIX[0][2] * cr);
                int g = (int) (YUV_TO_RGB_MATRIX[1][0] * y + YUV_TO_RGB_MATRIX[1][1] * cb + 
                    YUV_TO_RGB_MATRIX[1][2] * cr);
                int b = (int) (YUV_TO_RGB_MATRIX[2][0] * y + YUV_TO_RGB_MATRIX[2][1] * cb + 
                    YUV_TO_RGB_MATRIX[2][2] * cr);

                System.out.printf("%d %d %d\n", r, g, b);
                int rgb = new Color(r, g, b).getRGB();

                image.setRGB(i, j, rgb);
            }
        }

        return image;
    }

    public static WritableRaster RGBToYUV(BufferedImage original) {

        int width = original.getWidth();
        int height = original.getHeight();

        // Copy the original image into a new one
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                image.setRGB(i, j, original.getRGB(i, j));
            }
        }

        WritableRaster raster = image.getRaster();

        int[] rgb = new int[N_CHANNELS];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                rgb = raster.getPixel(i, j, rgb);
                int r = rgb[0];
                int g = rgb[1];
                int b = rgb[2];

                int y = (int) (RGB_TO_YUV_MATRIX[0][0] * r + RGB_TO_YUV_MATRIX[0][1] * g + 
                    RGB_TO_YUV_MATRIX[0][2] * b);
                int cb = (int) (RGB_TO_YUV_MATRIX[1][0] * r + RGB_TO_YUV_MATRIX[1][1] * g + 
                    RGB_TO_YUV_MATRIX[1][2] * b);
                int cr = (int) (RGB_TO_YUV_MATRIX[2][0] * r + RGB_TO_YUV_MATRIX[2][1] * g + 
                    RGB_TO_YUV_MATRIX[2][2] * b);

                int[] ycbcr = {y, cb, cr, 0};
                raster.setPixel(i, j, ycbcr);
            }
        }

        return raster;
    }
}
