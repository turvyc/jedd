import java.lang.Math;

public class DCTMatrix {

    private double[][] dctMatrix;
    private double[][] iDctMatrix;

    public DCTMatrix() {
        // Create the DCT matrix
        dctMatrix = new double[PixelBlock.HEIGHT][PixelBlock.WIDTH];
        iDctMatrix = new double[PixelBlock.HEIGHT][PixelBlock.WIDTH];

        double N = PixelBlock.HEIGHT;
        for (int i = 0; i < PixelBlock.HEIGHT; i++) {
            for (int j = 0; j < PixelBlock.WIDTH; j++) {
                if (i == 0)
                    dctMatrix[i][j] = (1.0 / Math.sqrt(N));
                else
                    dctMatrix[i][j] = ((Math.sqrt(2.0/N)) * Math.cos(((2.0 * j + 1.0) * i * Math.PI) / (2.0 * N)));
            }
        }

        // Create the inverse DCT Matrix
        for (int i = 0; i < PixelBlock.HEIGHT; i++) {
            for (int j = 0; j < PixelBlock.WIDTH; j++)
                iDctMatrix[i][j] = dctMatrix[j][i];
        }
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
    public PixelBlock dct(PixelBlock original, boolean forwards) {

        double[][] origY = original.getYChannel();
        double[][] origU = original.getUChannel();
        double[][] origV = original.getVChannel();

        double[][] dctY = new double[PixelBlock.HEIGHT][PixelBlock.WIDTH];
        double[][] dctU = new double[PixelBlock.HEIGHT][PixelBlock.WIDTH];
        double[][] dctV = new double[PixelBlock.HEIGHT][PixelBlock.WIDTH];

        if (forwards) {
            dctY = multiplyMatrices(dctMatrix, origY);
            dctY = multiplyMatrices(dctY, iDctMatrix);

            dctU = multiplyMatrices(dctMatrix, origU);
            dctU = multiplyMatrices(dctU, iDctMatrix);

            dctV = multiplyMatrices(dctMatrix, origV);
            dctV = multiplyMatrices(dctV, iDctMatrix);
        }

        else {
            dctY = multiplyMatrices(iDctMatrix, origY);
            dctY = multiplyMatrices(dctY, dctMatrix);

            dctU = multiplyMatrices(iDctMatrix, origU);
            dctU = multiplyMatrices(dctU, dctMatrix);

            dctV = multiplyMatrices(iDctMatrix, origV);
            dctV = multiplyMatrices(dctV, dctMatrix);
        }

        PixelBlock dctBlock = new PixelBlock();

        dctBlock.setChannel(PixelBlock.Y, dctY);
        dctBlock.setChannel(PixelBlock.U, dctU);
        dctBlock.setChannel(PixelBlock.V, dctV);

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




