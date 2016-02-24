import java.awt.Color;

/**
 * Represents a block of pixels.
 */
public class PixelBlock {

    // Size of the pixel block
    public static final int HEIGHT = 8;
    public static final int WIDTH = 8;

    // The number of color channels
    public static final int N_CHANNELS = 3;

    // Color channel constants
    public static final int Y = 0;
    public static final int U = 1;
    public static final int V = 2;

    public static final int R = 0;
    public static final int G = 1;
    public static final int B = 2;

    // The three color channels
    protected double[][] channel1;
    protected double[][] channel2;
    protected double[][] channel3;

    public PixelBlock() {
        channel1 = new double[HEIGHT][WIDTH];
        channel2 = new double[HEIGHT][WIDTH];
        channel3 = new double[HEIGHT][WIDTH];
    }

    /**
     * Loads RGB pixels (represented as a single integer in Java) into 
     * the pixel block.
     */
    public void loadRGB(int[] pixels) {
        assert (pixels.length == HEIGHT * WIDTH);

        // Seperate each pixel into individual channels
        double[] rgb = new double[N_CHANNELS];
        for (int i = 0; i < HEIGHT * WIDTH; i++) {
            Color c = new Color(pixels[i]);
            rgb[R] = (double) c.getRed();
            rgb[G] = (double) c.getGreen();
            rgb[B] = (double) c.getBlue();

            // Calculate the spot in the 2d array
            int w = i / WIDTH;
            int h = i - w * WIDTH;

            // Set the pixel in the pixel block
            setPixel(w, h, rgb);
        }
    }

    /**
     * Sets the three channels of a single pixel in the block.
     */
    public void setPixel(int h, int w, double[] channels) {
        assert channels.length == N_CHANNELS;
        channel1[h][w] = channels[0];
        channel2[h][w] = channels[1];
        channel3[h][w] = channels[2];
    }

    /**
     * Returns the pixel block as a single array of RGB-encoded integers,
     * so Java can paint it back into a BufferedImage.
     */
    public int[] getRgbArray() {
        int[] rgb = new int[HEIGHT * WIDTH];

        int n = 0;
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                Color c = new Color((int)channel1[i][j], (int)channel2[i][j], (int)channel3[i][j]);
                rgb[n] = c.getRGB();
                n++;
            }
        }
        return rgb;
    }

    public double[][] getYChannel() {
        return channel1;
    }

    public double[][] getUChannel() {
        return channel2;
    }

    public double[][] getVChannel() {
        return channel3;
    }

    /**
     * Returns the pixel block as a 3d array
     */
    public double[][][] getAllChannels() {
        double[][][] allChannels = new double[HEIGHT][WIDTH][N_CHANNELS];
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                allChannels[i][j][R] = channel1[i][j];
                allChannels[i][j][G] = channel2[i][j];
                allChannels[i][j][B] = channel3[i][j];
            }
        }
        return allChannels;
    }

    /**
     * Sets the specified channel to the specified values.
     */
    public void setChannel(int channel, double[][] vals) {
        for (int i = 0; i < PixelBlock.HEIGHT; i++) {
            for (int j = 0; j < PixelBlock.WIDTH; j++) {
                switch (channel) {
                    case Y: channel1[i][j] = vals[i][j]; break;
                    case U: channel2[i][j] = vals[i][j]; break;
                    case V: channel3[i][j] = vals[i][j]; break;
                }
            }
        }
    }

    /**
     * Returns a formatted representation of the pixel block.
     */
    public String toString() {
        String s = "";
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                s += String.format("%-5f ", channel1[i][j]); 
            }
            s += String.format("\n");
        }

        return s;
    }
}

