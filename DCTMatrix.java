import java.lang.Math;

public class DCTMatrix {

    private float[][] dctMatrix;
    private float[][] iDctMatrix;

    public DCTMatrix() {
        // Create the DCT matrix
        dctMatrix = new float[PixelBlock.HEIGHT][PixelBlock.WIDTH];
        iDctMatrix = new float[PixelBlock.HEIGHT][PixelBlock.WIDTH];

        int N = PixelBlock.HEIGHT;
        for (int i = 0; i < PixelBlock.HEIGHT; i++) {
            for (int j = 0; j < PixelBlock.WIDTH; j++) {
                if (i == 0)
                    dctMatrix[i][j] = (float) (1.0 / Math.sqrt(N));
                else
                    dctMatrix[i][j] = (float) ((Math.sqrt(2.0/N)) * Math.cos(((2.0 * j + 1) * i * Math.PI) / (2.0 * N)));
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
        float[][] origY = original.getYChannel();
        float[][] origU = original.getUChannel();
        float[][] origV = original.getVChannel();

        float[][] dctY = new float[PixelBlock.HEIGHT][PixelBlock.WIDTH];
        float[][] dctU = new float[PixelBlock.HEIGHT][PixelBlock.WIDTH];
        float[][] dctV = new float[PixelBlock.HEIGHT][PixelBlock.WIDTH];

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

        dctBlock.setChannel(PixelBlock.Y, origY);
        dctBlock.setChannel(PixelBlock.U, origU);
        dctBlock.setChannel(PixelBlock.V, origV);

        return dctBlock;
    }

    private float[][] multiplyMatrices(float[][] a, float[][] b) {
        int N = PixelBlock.HEIGHT;
        float[][] product = new float[N][N];

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




