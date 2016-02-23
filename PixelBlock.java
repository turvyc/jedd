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

    protected int[][] channel1;
    protected int[][] channel2;
    protected int[][] channel3;

    public PixelBlock() {
        channel1 = new int[HEIGHT][WIDTH];
        channel2 = new int[HEIGHT][WIDTH];
        channel3 = new int[HEIGHT][WIDTH];
    }

    public void loadRGB(int[] pixels) {
        assert (pixels.length == HEIGHT * WIDTH);
        int[] rgb = new int[N_CHANNELS];
        for (int i = 0; i < HEIGHT * WIDTH; i++) {
            rgb[R] = (pixels[i] >> 16) & 0xff;
            rgb[G] = (pixels[i] >> 8) & 0xff;
            rgb[B] = pixels[i] & 0xff;

            int w = i / WIDTH;
            int h = i - w * WIDTH;

            setPixel(w, h, rgb);
        }
    }

    public void setPixel(int h, int w, int[] channels) {
        assert channels.length == N_CHANNELS;
        channel1[h][w] = channels[0];
        channel2[h][w] = channels[1];
        channel3[h][w] = channels[2];
    }

    public int[] getRgbArray() {
        int[] rgb = new int[HEIGHT * WIDTH];

        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                Color c = new Color(channel1[i][j], channel2[i][j], channel3[i][j]);
                rgb[i + j] = c.getRGB();
            }
        }
        
        return rgb;
    }


    public int[][] getYChannel() {
        return channel1;
    }

    public int[][] getUChannel() {
        return channel2;
    }

    public int[][] getVChannel() {
        return channel3;
    }

    public int[][][] getAllChannels() {
        int[][][] allChannels = new int[HEIGHT][WIDTH][N_CHANNELS];
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                allChannels[i][j][R] = channel1[i][j];
                allChannels[i][j][G] = channel2[i][j];
                allChannels[i][j][B] = channel3[i][j];
            }
        }
        return allChannels;
    }

    public void setChannel(int channel, int[][] vals) {
        switch (channel) {
            case Y: channel1 = vals; break;
            case U: channel2 = vals; break;
            case V: channel3 = vals; break;
        }
    }

}

