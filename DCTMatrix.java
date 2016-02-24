import java.lang.Math;

public class DCTMatrix {

    private double[][] dctMatrix;
    private double[][] iDctMatrix;

    public DCTMatrix() {
        // Create the DCT matrix
        dctMatrix = new double[PixelBlock.HEIGHT][PixelBlock.WIDTH];
        iDctMatrix = new double[PixelBlock.HEIGHT][PixelBlock.WIDTH];

        int N = PixelBlock.HEIGHT;
        for (int i = 0; i < PixelBlock.HEIGHT; i++) {
            for (int j = 0; j < PixelBlock.WIDTH; j++) {
                if (i == 0)
                    dctMatrix[i][j] = 1 / Math.sqrt(N);
                else
                    dctMatrix[i][j] = (Math.sqrt(2.0/N)) * Math.cos(((2.0 * j + 1) * i * Math.PI) / (2.0* N));
            }
        }

        // Create the inverse DCT Matrix
        for (int i = 0; i < PixelBlock.HEIGHT; i++) {
            for (int j = 0; j < PixelBlock.WIDTH; j++)
                iDctMatrix[i][j] = dctMatrix[j][i];
        }
    }

    //public PixelBlock dct(PixelBlock original, boolean forwards) {
    public PixelBlock dct(PixelBlock original, boolean forwards) {
        int[][] origY = original.getYChannel();
        int[][] origU = original.getUChannel();
        int[][] origV = original.getVChannel();

        double[][] dctY = new double[PixelBlock.HEIGHT][PixelBlock.WIDTH];
        double[][] dctU = new double[PixelBlock.HEIGHT][PixelBlock.WIDTH];
        double[][] dctV = new double[PixelBlock.HEIGHT][PixelBlock.WIDTH];

        // Cast the original as a double
        for (int i = 0; i < PixelBlock.HEIGHT; i++) {
            for (int j = 0; j < PixelBlock.WIDTH; j++) {
                dctY[i][j] = (double) origY[i][j];
                dctU[i][j] = (double) origU[i][j];
                dctV[i][j] = (double) origV[i][j];
            }
        }

        if (forwards) {
            dctY = multiplyMatrices(dctMatrix, dctY);
            dctY = multiplyMatrices(dctY, iDctMatrix);

            dctU = multiplyMatrices(dctMatrix, dctU);
            dctU = multiplyMatrices(dctU, iDctMatrix);

            dctV = multiplyMatrices(dctMatrix, dctV);
            dctV = multiplyMatrices(dctV, iDctMatrix);
        }

        else {
            dctY = multiplyMatrices(iDctMatrix, dctY);
            dctY = multiplyMatrices(dctY, dctMatrix);

            dctU = multiplyMatrices(iDctMatrix, dctU);
            dctU = multiplyMatrices(dctU, dctMatrix);

            dctV = multiplyMatrices(iDctMatrix, dctV);
            dctV = multiplyMatrices(dctV, dctMatrix);
        }

        PixelBlock dctBlock = new PixelBlock();

        // Cast it back to an integer
        int[][] resultY = new int[PixelBlock.HEIGHT][PixelBlock.WIDTH];
        for (int i = 0; i < PixelBlock.HEIGHT; i++) {
            for (int j = 0; j < PixelBlock.WIDTH; j++) {
                origY[i][j] = (int) dctY[i][j];
                origU[i][j] = (int) dctU[i][j];
                origV[i][j] = (int) dctV[i][j];
            }
        }

        dctBlock.setChannel(PixelBlock.Y, origY);
        dctBlock.setChannel(PixelBlock.U, origU);
        dctBlock.setChannel(PixelBlock.V, origV);

        return dctBlock;
    }

    private double[][] multiplyMatrices(double[][] a, double[][] b) {
        int N = PixelBlock.HEIGHT;
        double[][] product = new double[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                for (int k = 0; k < N; k++) {
                    product[i][j] += (a[i][k] * b[k][j]);
                }
            }
        }
        return product;
    }
}




