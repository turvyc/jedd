import java.lang.Math;

public class DCT {

    public static PixelBlock dct(PixelBlock original) {
        int[][] origY = original.getYChannel();
        int[][] origU = original.getUChannel();
        int[][] origV = original.getVChannel();

        int[][] dctY = new int[PixelBlock.HEIGHT][PixelBlock.WIDTH];
        int[][] dctU = new int[PixelBlock.HEIGHT][PixelBlock.WIDTH];
        int[][] dctV = new int[PixelBlock.HEIGHT][PixelBlock.WIDTH];

        // For each pixel in the block . . .
        for (int u = 0; u < PixelBlock.HEIGHT; u++) {
            for (int v = 0; v < PixelBlock.WIDTH; v++) {
                double ySum = 0.0;
                double uSum = 0.0;
                double vSum = 0.0;

                // The sums
                for (int i = 0; i < PixelBlock.HEIGHT; i++) {
                    for (int j = 0; j < PixelBlock.WIDTH; j++) {
                        // A gnar-town equation. Deal with it.
                        double cosine = Math.cos(( (2 * i + 1) * u * Math.PI) / 16) *
                            Math.cos(( (2 * j + 1) * v * Math.PI) / 16);
                        ySum += cosine * origY[i][j];
                        uSum += cosine * origU[i][j];
                        vSum += cosine * origV[i][j];
                    }
                }

                double coefficient = C(u) * C(v) / 4;
                ySum *= coefficient;
                uSum *= coefficient;
                vSum *= coefficient;

                dctY[u][v] = (int) ySum;
                dctU[u][v] = (int) uSum;
                dctV[u][v] = (int) vSum;
            }
        }

        PixelBlock dctBlock = new PixelBlock();

        dctBlock.setChannel(PixelBlock.Y, dctY);
        dctBlock.setChannel(PixelBlock.U, dctU);
        dctBlock.setChannel(PixelBlock.V, dctV);

        return dctBlock;
    }

    private static double C(int i) {
        return (i == 0) ? Math.sqrt(2.0) / 2 : 1;
    }
}
