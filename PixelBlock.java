public class PixelBlock {

    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;
    public static final int N_CHANNELS = 3;

    public static final int Y = 0;
    public static final int U = 1;
    public static final int V = 2;

    public static final int R = 0;
    public static final int G = 1;
    public static final int B = 2;

    private int[][] channel1;
    private int[][] channel2;
    private int[][] channel3;

    public PixelBlock() {
        channel1 = new int[WIDTH][HEIGHT];
        channel2 = new int[WIDTH][HEIGHT];
        channel3 = new int[WIDTH][HEIGHT];
    }

    public PixelBlock(int[] pixels) {
        this();
        assert (pixels.length == WIDTH * HEIGHT);
        int[] rgb = new int[N_CHANNELS];
        for (int i = 0; i < WIDTH * HEIGHT; i++) {
            rgb[R] = (pixels[i] >> 16) & 0xff;
            rgb[G] = (pixels[i] >> 8) & 0xff;
            rgb[B] = pixels[i] & 0xff;

            int w = i / WIDTH;
            int h = i - w * WIDTH;

            setPixel(w, h, rgb);
        }
    }

    public void setPixel(int w, int h, int[] pixels) {
        assert pixels.length == N_CHANNELS;
        channel1[w][h] = pixels[0];
        channel2[w][h] = pixels[1];
        channel3[w][h] = pixels[2];
    }

    public int[][] getChannel1() {
        return channel1;
    }

    public int[][] getChannel2() {
        return channel2;
    }

    public int[][] getChannel3() {
        return channel3;
    }

    public int[][][] getAllChannels() {
        int[][][] allChannels = new int[WIDTH][HEIGHT][N_CHANNELS];
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                allChannels[i][j][R] = channel1[i][j];
                allChannels[i][j][G] = channel2[i][j];
                allChannels[i][j][B] = channel3[i][j];
            }
        }
        return allChannels;
    }
}

