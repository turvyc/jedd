public class ChromaSubsampler {

    public static final int TYPE_420 = 0;
    public static final int TYPE_444 = 1;
    public static final int TYPE_411 = 2;
    public static final int TYPE_422 = 3;
    public static final int TYPE_440 = 4;

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
            case TYPE_444: 
                a = 4;
                b = 4;
                break;
            case TYPE_411: 
                a = 1;
                b = 1;
                break;
            case TYPE_422: 
                a = 2;
                b = 2;
                break;
            case TYPE_440: 
                a = 4;
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

        double[][] yChannel = original.getYChannel();
        double[][] uChannel = original.getUChannel();
        double[][] vChannel = original.getVChannel();

        // Load in the Y channel unchanged
        subsampled.setChannel(PixelBlock.Y, yChannel);

        double[][] subUChannel = new double[PixelBlock.HEIGHT][PixelBlock.WIDTH];
        double[][] subVChannel = new double[PixelBlock.HEIGHT][PixelBlock.WIDTH];

        for (int h = 0; h < PixelBlock.HEIGHT; h += PixelBlock.HEIGHT / J) {
            for (int w = 0; w < PixelBlock.WIDTH; w += J) {
                // Now we are inside the reference block.
                // The values for one row in the ref block
                double[] uVals = new double[4];
                double[] vVals = new double[4];

                int p = 0;  // Which row
                int q = 0;  // Which column
                switch (type) {
                    case TYPE_440:
                        while (p < k) {
                            while (q < J) {
                                uVals[0] = uChannel[h + p][w + q];
                                uVals[1] = uChannel[h + p + 1][w + q];
                                vVals[0] = vChannel[h + p][w + q];
                                vVals[1] = vChannel[h + p + 1][w + q];

                                double u = filter(uVals);
                                double v = filter(vVals);

                                subUChannel[h + p][w + q] = u;
                                subUChannel[h + p + 1][w + q] = u;
                                subVChannel[h + p][w + q] = v;
                                subVChannel[h + p + 1][w + q] = v;

                                q += a;
                            }
                            p += k;
                        }
                        break;
                    case TYPE_422:
                        while (p < k) {
                            while (q < J) {
                                uVals[0] = uChannel[h + p][w + q];
                                uVals[1] = uChannel[h + p][w + q + 1];
                                vVals[0] = vChannel[h + p][w + q];
                                vVals[1] = vChannel[h + p][w + q + 1];

                                double u = filter(uVals);
                                double v = filter(vVals);

                                subUChannel[h + p][w + q] = u;
                                subUChannel[h + p][w + q + 1] = u;
                                subVChannel[h + p][w + q] = v;
                                subVChannel[h + p][w + q + 1] = v;

                                q += a;
                            }
                            p++;
                        }
                        break;

                    case TYPE_411:
                        while (p < k) {
                            while (q < J) {
                                uVals[q] = uChannel[h + p][w + q];
                                vVals[q] = vChannel[h + p][w + q];
                                q++;
                            }
                            q = 0;
                            double u = filter(uVals);
                            double v = filter(vVals);
                            while (q < J) {
                                subUChannel[h + p][w + q] = u;
                                subVChannel[h + p][w + q] = v;
                            }
                            p++;
                        }
                        break;

                    case TYPE_444:
                        while (p < k) {
                            while (q < J) {
                                subUChannel[h + p][w + q] = uChannel[h + p][w + q];
                                subVChannel[h + p][w + q] = vChannel[h + p][w + q];
                                q++;
                            }
                            p++;
                        }
                        break;

                    case TYPE_420:
                        while (p < k) {
                            while (q < J) {
                                uVals[0] = uChannel[h + p][w + q];
                                uVals[1] = uChannel[h + p][w + q + 1];
                                uVals[2] = uChannel[h + p + 1][w + q];
                                uVals[3] = uChannel[h + p + 1][w + q + 1];

                                vVals[0] = vChannel[h + p][w + q];
                                vVals[1] = vChannel[h + p][w + q + 1];
                                vVals[2] = vChannel[h + p + 1][w + q];
                                vVals[3] = vChannel[h + p + 1][w + q + 1];

                                double u = filter(uVals);
                                double v = filter(vVals);

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
                        }
                        break;
                }
            }
        }

        subsampled.setChannel(PixelBlock.U, subUChannel);
        subsampled.setChannel(PixelBlock.V, subVChannel);

        return subsampled;
    }

    private double filter(double[] vals) {
        if (filter == CONSTANT_FILTER)
            return vals[0];
        else {
            double total = 0;
            for (double i : vals)
                total += i;
            return total / vals.length;
        }
    }
}
