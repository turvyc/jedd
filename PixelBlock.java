import java.awt.Color;

public class PixelBlock {

    public static final int HEIGHT = 8;
    public static final int WIDTH = 8;
    public static final int N_CHANNELS = 3;

    public static final int Y = 0;
    public static final int U = 1;
    public static final int V = 2;

    public static final int R = 0;
    public static final int G = 1;
    public static final int B = 2;

    protected double[][] channel1;
    protected double[][] channel2;
    protected double[][] channel3;

    public PixelBlock() {
        channel1 = new double[HEIGHT][WIDTH];
        channel2 = new double[HEIGHT][WIDTH];
        channel3 = new double[HEIGHT][WIDTH];
    }

    public void loadRGB(int[] pixels) {
        assert (pixels.length == HEIGHT * WIDTH);
        double[] rgb = new double[N_CHANNELS];
        for (int i = 0; i < HEIGHT * WIDTH; i++) {
            Color c = new Color(pixels[i]);
            rgb[R] = (double) c.getRed();
            rgb[G] = (double) c.getGreen();
            rgb[B] = (double) c.getBlue();

            int w = i / WIDTH;
            int h = i - w * WIDTH;

            setPixel(w, h, rgb);
        }
    }

    public void setPixel(int h, int w, double[] channels) {
        assert channels.length == N_CHANNELS;
        channel1[h][w] = channels[0];
        channel2[h][w] = channels[1];
        channel3[h][w] = channels[2];
    }

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

    public void p(double[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j<a[0].length; j++) {
                System.out.printf("%3f ", a[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
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

