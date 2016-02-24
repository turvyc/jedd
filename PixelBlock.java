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

    protected float[][] channel1;
    protected float[][] channel2;
    protected float[][] channel3;

    public PixelBlock() {
        channel1 = new float[HEIGHT][WIDTH];
        channel2 = new float[HEIGHT][WIDTH];
        channel3 = new float[HEIGHT][WIDTH];
    }

    public void loadRGB(int[] pixels) {
        assert (pixels.length == HEIGHT * WIDTH);
        float[] rgb = new float[N_CHANNELS];
        for (int i = 0; i < HEIGHT * WIDTH; i++) {
            Color c = new Color(pixels[i]);
            rgb[R] = (float) c.getRed();
            rgb[G] = (float) c.getGreen();
            rgb[B] = (float) c.getBlue();

            int w = i / WIDTH;
            int h = i - w * WIDTH;

            setPixel(w, h, rgb);
        }
    }

    public void setPixel(int h, int w, float[] channels) {
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


    public float[][] getYChannel() {
        return channel1;
    }

    public float[][] getUChannel() {
        return channel2;
    }

    public float[][] getVChannel() {
        return channel3;
    }

    public float[][][] getAllChannels() {
        float[][][] allChannels = new float[HEIGHT][WIDTH][N_CHANNELS];
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                allChannels[i][j][R] = channel1[i][j];
                allChannels[i][j][G] = channel2[i][j];
                allChannels[i][j][B] = channel3[i][j];
            }
        }
        return allChannels;
    }

    public void setChannel(int channel, float[][] vals) {
        switch (channel) {
            case Y: channel1 = vals; break;
            case U: channel2 = vals; break;
            case V: channel3 = vals; break;
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

