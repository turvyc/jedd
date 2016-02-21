import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ColorConverter {
    private final double[][] RGB_TO_YUV_MATRIX = {
        {0.299, 0.587, 0.114},
        {-0.14713, -0.28886, 0.436},
        {0.615, -0.51499, -0.10001}
    };

    private final double[][] YUV_TO_RGB_MATRIX = {
        {1, 0, 1.13983},
        {1, -0.39465, -0.58060},
        {1, 2.03211, 0}
    };

    private final int RED_BITMASK = 0x00ff0000;
    private final int GREEN_BITMASK = 0x0000ff00;
    private final int BLUE_BITMASK = 0x000000ff;

    public ArrayList<BufferedImage> getRGB(BufferedImage original) {
        int width = original.getWidth();
        int height = original.getHeight();
        int type = original.getType();

        redChannel = new BufferedImage(width, height, type);
        greenChannel = new BufferedImage(width, height, type);
        blueChannel = new BufferedImage(width, height, type);

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

        ArrayList<BufferedImage> list = new ArrayList<BufferedImage>;
        list.add(redChannel);
        list.add(greenChannel);
        list.add(blueChannel);

        return list;
    }

    public ArrayList<BufferedImage> convertRgbToYuv(ArrayList<BufferedImage> original) {
        BufferedImage redChannel = original.get(0);        
        BufferedImage greenChannel = original.get(1);        
        BufferedImage blueChannel = original.get(2);        

        int width = redChannel.getWidth();
        int height = redChannel.getHeight();
        int type = redChannel.getType();

        BufferedImage yChannel = new BufferedImage(width, height, type);
        BufferedImage uChannel = new BufferedImage(width, height, type);
        BufferedImage vChannel = new BufferedImage(width, height, type);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int r = redChannel.getRGB(i, j);
                int g = greenChannel.getRGB(i, j);
                int b = blueChannel.getRGB(i, j);

                int y = RGB_TO_YUV_MATRIX[0][0] * r + RGB_TO_YUV_MATRIX[0][1] * g + 
                    RGB_TO_YUV_MATRIX[0][2] * b;
                int u = RGB_TO_YUV_MATRIX[1][0] * r + RGB_TO_YUV_MATRIX[1][1] * g + 
                    RGB_TO_YUV_MATRIX[1][2] * b;
                int v = RGB_TO_YUV_MATRIX[2][0] * r + RGB_TO_YUV_MATRIX[2][1] * g + 
                    RGB_TO_YUV_MATRIX[2][2] * b;



    }
}
