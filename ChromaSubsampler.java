public class ChromaSubsampler {

    public static final int TYPE_420 = 0;

    public static final int CONSTANT_FILTER = 0;
    public static final int AVERAGE_FILTER = 1;

    private int type;
    private final int J = 4;    // Width of reference block
    private final int k = 2;    // Height of reference block
    private int a;              // Number of samples in first row
    private int b;              // Number of samples in second row
    private int filter;         // How the samples are filtered

    public ChromaSubsampler() {
        // Set default values
        setType(TYPE_420);
        setFilter(CONSTANT_FILTER);
    }

    public ChromaSubsampler(int t) {
        setType(t);
    }

    public void setType(int t) {
        type = t;
        switch (t) {
            case TYPE_420: 
                a = 2;
                b = 0;
                break;
            default:
                a = 2;
                b = 0;
                break;
        }
    }

    public void setFilter(int f) {
        assert (f == CONSTANT_FILTER) || (f == AVERAGE_FILTER);
        filter = f;
    }

    public PixelBlock subsample(PixelBlock original) {
        PixelBlock subsampled = new PixelBlock();

        int[][] yChannel = original.getYChannel();
        int[][] uChannel = original.getUChannel();
        int[][] vChannel = original.getVChannel();

        // Load in the Y channel unchanged
        subsampled.setChannel(PixelBlock.Y, yChannel);

        int[][] subUChannel = new int[PixelBlock.HEIGHT][PixelBlock.WIDTH];
        int[][] subVChannel = new int[PixelBlock.HEIGHT][PixelBlock.WIDTH];

        for (int h = 0; h < PixelBlock.HEIGHT; h += PixelBlock.HEIGHT / J) {
            for (int w = 0; w < PixelBlock.WIDTH; w += J) {
                // Now we are inside the reference block.
                int p; int q; p = q = 0;
                while (p < k) {
                    switch (type) {
                        case TYPE_420:
                            while (q < J) {
                                int[] uVals = new int[4];
                                int[] vVals = new int[4];
                                uVals[0] = uChannel[h + p][w + q];
                                uVals[1] = uChannel[h + p][w + q + 1];
                                uVals[2] = uChannel[h + p + 1][w + q];
                                uVals[3] = uChannel[h + p + 1][w + q + 1];

                                vVals[0] = vChannel[h + p][w + q];
                                vVals[1] = vChannel[h + p][w + q + 1];
                                vVals[2] = vChannel[h + p + 1][w + q];
                                vVals[3] = vChannel[h + p + 1][w + q + 1];

                                int u = filter(uVals);
                                int v = filter(vVals);

                                subUChannel[h + p][w + q] = u;
                                subUChannel[h + p][w + q + 1] = u;
                                subUChannel[h + p + 1][w + q] = u;
                                subUChannel[h + p + 1][w + q + 1] = u;

                                subVChannel[h + p][w + q] = v;
                                subVChannel[h + p][w + q + 1] = v;
                                subVChannel[h + p + 1][w + q] = v;
                                subVChannel[h + p + 1][w + q + 1] = v;

                                q += a;
                            }
                            p += k;
                            break;
                    }
                }
            }
        }

        subsampled.setChannel(PixelBlock.U, subUChannel);
        subsampled.setChannel(PixelBlock.V, subVChannel);

        return subsampled;
    }

    private int filter(int[] vals) {
        if (filter == CONSTANT_FILTER)
            return vals[0];
        else {
            int total = 0;
            for (int i : vals)
                total += i;
            return total / vals.length;
        }
    }
}
