/**
 * Provides static methods for RGB <--> YUV conversions.
 */
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

    /**
     * Converts a YUV pixel block to RGB
     */
    public static PixelBlock YUVtoRGB(PixelBlock original) {
        double[][][] yuvPixels = original.getAllChannels();
        PixelBlock rgbPixels = new PixelBlock();

        for (int i = 0; i < PixelBlock.HEIGHT; i++) {
            for (int j = 0; j < PixelBlock.WIDTH; j++) {
                double y = yuvPixels[i][j][PixelBlock.Y];
                double u = yuvPixels[i][j][PixelBlock.U];
                double v = yuvPixels[i][j][PixelBlock.V];

                // Perform matrix multiplication
                double r = (YUV_TO_RGB_MATRIX[0][0] * y + YUV_TO_RGB_MATRIX[0][1] * u + 
                    YUV_TO_RGB_MATRIX[0][2] * v);
                double g = (YUV_TO_RGB_MATRIX[1][0] * y + YUV_TO_RGB_MATRIX[1][1] * u + 
                    YUV_TO_RGB_MATRIX[1][2] * v);
                double b = (YUV_TO_RGB_MATRIX[2][0] * y + YUV_TO_RGB_MATRIX[2][1] * u + 
                    YUV_TO_RGB_MATRIX[2][2] * v);

                // Set the values into the new pixel block
                double[] rgb = {r, g, b};
                rgbPixels.setPixel(i, j, limit(rgb));
            }
        }
        return rgbPixels;
    }

    /**
     * Converts an RGB pixel block to YUV
     */
    public static PixelBlock RGBtoYUV(PixelBlock original) {
        double[][][] rgbPixels = original.getAllChannels();
        PixelBlock yuvPixels = new PixelBlock();

        for (int i = 0; i < PixelBlock.HEIGHT; i++) {
            for (int j = 0; j < PixelBlock.WIDTH; j++) {
                double r = rgbPixels[i][j][PixelBlock.R];
                double g = rgbPixels[i][j][PixelBlock.G];
                double b = rgbPixels[i][j][PixelBlock.B];

                // Perform matrix multiplication
                double y = (RGB_TO_YUV_MATRIX[0][0] * r + RGB_TO_YUV_MATRIX[0][1] * g + 
                    RGB_TO_YUV_MATRIX[0][2] * b);
                double u = (RGB_TO_YUV_MATRIX[1][0] * r + RGB_TO_YUV_MATRIX[1][1] * g + 
                    RGB_TO_YUV_MATRIX[1][2] * b);
                double  v = (RGB_TO_YUV_MATRIX[2][0] * r + RGB_TO_YUV_MATRIX[2][1] * g + 
                    RGB_TO_YUV_MATRIX[2][2] * b);

                // Set the values in the new pixel block
                double[] yuv = {y, u, v};
                yuvPixels.setPixel(i, j, yuv);
            }
        }
        return yuvPixels;
    }

    /**
     * Ensures the values are within the proper limits (my only cheat function!)
     */
    private static double[] limit(double[] input) {
        double UPPER_LIMIT = 255.0;
        double LOWER_LIMIT = 0.0;
        for (int i = 0; i < input.length; i++) {
            if (input[i] > UPPER_LIMIT)
                input[i] = UPPER_LIMIT;
            else if (input[i] < LOWER_LIMIT)
                input[i] = LOWER_LIMIT;
        }
        return input;
    }
}
