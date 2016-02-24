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
        float[][][] yuvPixels = original.getAllChannels();
        PixelBlock rgbPixels = new PixelBlock();

        for (int i = 0; i < PixelBlock.HEIGHT; i++) {
            for (int j = 0; j < PixelBlock.WIDTH; j++) {
                float y = yuvPixels[i][j][PixelBlock.Y];
                float u = yuvPixels[i][j][PixelBlock.U];
                float v = yuvPixels[i][j][PixelBlock.V];

                float r = (float) (YUV_TO_RGB_MATRIX[0][0] * y + YUV_TO_RGB_MATRIX[0][1] * u + 
                    YUV_TO_RGB_MATRIX[0][2] * v);
                float g = (float) (YUV_TO_RGB_MATRIX[1][0] * y + YUV_TO_RGB_MATRIX[1][1] * u + 
                    YUV_TO_RGB_MATRIX[1][2] * v);
                float b = (float) (YUV_TO_RGB_MATRIX[2][0] * y + YUV_TO_RGB_MATRIX[2][1] * u + 
                    YUV_TO_RGB_MATRIX[2][2] * v);

                float[] rgb = {r, g, b};
                rgbPixels.setPixel(i, j, limit(rgb));
            }
        }

        return rgbPixels;
    }

    public static PixelBlock RGBtoYUV(PixelBlock original) {
        float[][][] rgbPixels = original.getAllChannels();
        PixelBlock yuvPixels = new PixelBlock();

        for (int i = 0; i < PixelBlock.HEIGHT; i++) {
            for (int j = 0; j < PixelBlock.WIDTH; j++) {
                float r = rgbPixels[i][j][PixelBlock.R];
                float g = rgbPixels[i][j][PixelBlock.G];
                float b = rgbPixels[i][j][PixelBlock.B];

                float y = (float) (RGB_TO_YUV_MATRIX[0][0] * r + RGB_TO_YUV_MATRIX[0][1] * g + 
                    RGB_TO_YUV_MATRIX[0][2] * b);
                float u = (float) (RGB_TO_YUV_MATRIX[1][0] * r + RGB_TO_YUV_MATRIX[1][1] * g + 
                    RGB_TO_YUV_MATRIX[1][2] * b);
                float  v = (float) (RGB_TO_YUV_MATRIX[2][0] * r + RGB_TO_YUV_MATRIX[2][1] * g + 
                    RGB_TO_YUV_MATRIX[2][2] * b);

                float[] yuv = {y, u, v};
                yuvPixels.setPixel(i, j, yuv);
            }
        }
        return yuvPixels;
    }

    private static float[] limit(float[] input) {
        float UPPER_LIMIT = 255.0f;
        float LOWER_LIMIT = 0.0f;
        for (int i = 0; i < input.length; i++) {
            if (input[i] > UPPER_LIMIT)
                input[i] = UPPER_LIMIT;
            else if (input[i] < LOWER_LIMIT)
                input[i] = LOWER_LIMIT;
        }
        return input;
    }
}
