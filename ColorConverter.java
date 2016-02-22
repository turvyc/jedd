import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

public class ColorConverter {

    private static final int N_CHANNELS = 4;

    /*
    private static final double[][] RGB_TO_YCbCr_MATRIX = {
        {0.299, 0.587, 0.114},
        {-0.169, -0.331, 0.5},
        {0.5, -0.419, -0.081}
    };

    private static final double[][] YCbCr_TO_RGB_MATRIX = {
        {1, 0, 1.4},
        {1, -0.343, -0.711},
        {1, 1.765, 0}
    };
    */

    private static final double[][] RGB_TO_YCbCr_MATRIX = {
        {0.299, 0.587, 0.114},
        {-0.14713, -0.28886, 0.436},
        {0.615, -0.51499, -0.10001}
    };

    private static final double[][] YCbCr_TO_RGB_MATRIX = {
        {1, 0, 1.13983},
        {1, -0.39465, -0.58060},
        {1, 2.03211, 0}
    };

    private static final int RED_BITMASK = 0xff0000;
    private static final int GREEN_BITMASK = 0xff00;
    private static final int BLUE_BITMASK = 0xff;

    public static ArrayList<BufferedImage> getRGBChannels(BufferedImage original) {
        int width = original.getWidth();
        int height = original.getHeight();
        int type = original.getType();

        BufferedImage redChannel = new BufferedImage(width, height, type);
        BufferedImage greenChannel = new BufferedImage(width, height, type);
        BufferedImage blueChannel = new BufferedImage(width, height, type);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = original.getRGB(i, j);
                int r = rgb & RED_BITMASK;
                int g = rgb & GREEN_BITMASK;
                int b = rgb & BLUE_BITMASK;
                redChannel.setRGB(i, j, r);
                greenChannel.setRGB(i, j, g);
                blueChannel.setRGB(i, j, b);
            }
        }

        ArrayList<BufferedImage> channels = new ArrayList<BufferedImage>();
        channels.add(redChannel);
        channels.add(greenChannel);
        channels.add(blueChannel);

        return channels;
    }

    public static ArrayList<BufferedImage> getYCbCrChannels(WritableRaster original) {
        int width = original.getWidth();
        int height = original.getHeight();

        BufferedImage yChannel = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        BufferedImage cbChannel = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        BufferedImage crChannel = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        WritableRaster yRaster = yChannel.getRaster();
        WritableRaster cbRaster = cbChannel.getRaster();
        WritableRaster crRaster = crChannel.getRaster();

        /*
        int[] iArray = new int[width * height];

        iArray = original.getSamples(0, 0, width, height, 0, iArray);
        yRaster.setSamples(0, 0, width, height, 0, iArray);

        iArray = original.getSamples(0, 0, width, height, 1, iArray);
        cbRaster.setSamples(0, 0, width, height, 1, iArray);

        iArray = original.getSamples(0, 0, width, height, 2, iArray);
        cbRaster.setSamples(0, 0, width, height, 2, iArray);
        */

        int[] ycbcr = new int[N_CHANNELS];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                ycbcr = original.getPixel(i, j, ycbcr);
                int[] y = {ycbcr[0], 0, 0, 0};
                int[] cb = {0, ycbcr[1], 0, 0};
                int[] cr = {0, 0, ycbcr[2], 0};
                yRaster.setPixel(i, j, y);
                cbRaster.setPixel(i, j, cb);
                crRaster.setPixel(i, j, cr);
            }
        }

        yChannel = YCbCrToRGB(yRaster);
        cbChannel = YCbCrToRGB(cbRaster);
        crChannel = YCbCrToRGB(crRaster);

        ArrayList<BufferedImage> channels = new ArrayList<BufferedImage>();
        channels.add(yChannel);
        channels.add(cbChannel);
        channels.add(crChannel);

        return channels;

    }

    public static BufferedImage YCbCrToRGB(WritableRaster original) {

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
                int r = (int) (YCbCr_TO_RGB_MATRIX[0][0] * y + YCbCr_TO_RGB_MATRIX[0][1] * cb + 
                    YCbCr_TO_RGB_MATRIX[0][2] * cr);
                int g = (int) (YCbCr_TO_RGB_MATRIX[1][0] * y + YCbCr_TO_RGB_MATRIX[1][1] * cb + 
                    YCbCr_TO_RGB_MATRIX[1][2] * cr);
                int b = (int) (YCbCr_TO_RGB_MATRIX[2][0] * y + YCbCr_TO_RGB_MATRIX[2][1] * cb + 
                    YCbCr_TO_RGB_MATRIX[2][2] * cr);

                System.out.printf("%d %d %d\n", r, g, b);
                int rgb = new Color(r, g, b).getRGB();

                image.setRGB(i, j, rgb);
            }
        }

        return image;
    }

    public static WritableRaster RGBToYCbCr(BufferedImage original) {

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

                int y = (int) (RGB_TO_YCbCr_MATRIX[0][0] * r + RGB_TO_YCbCr_MATRIX[0][1] * g + 
                    RGB_TO_YCbCr_MATRIX[0][2] * b);
                int cb = (int) (RGB_TO_YCbCr_MATRIX[1][0] * r + RGB_TO_YCbCr_MATRIX[1][1] * g + 
                    RGB_TO_YCbCr_MATRIX[1][2] * b);
                int cr = (int) (RGB_TO_YCbCr_MATRIX[2][0] * r + RGB_TO_YCbCr_MATRIX[2][1] * g + 
                    RGB_TO_YCbCr_MATRIX[2][2] * b);

                int[] ycbcr = {y, cb, cr, 0};
                raster.setPixel(i, j, ycbcr);
            }
        }

        return raster;
    }
}
