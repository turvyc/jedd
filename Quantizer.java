public class Quantizer {
    
    public static PixelBlock quantize(PixelBlock input, QuantizationTable table) {

        PixelBlock quantizedBlock = new PixelBlock();

        double[][] origY = input.getYChannel();
        double[][] origU = input.getUChannel();
        double[][] origV = input.getVChannel();

        double[][] quantizedY = quantizedBlock.getYChannel();
        double[][] quantizedU = quantizedBlock.getUChannel();
        double[][] quantizedV = quantizedBlock.getVChannel();

        double[][] yTable = table.getYChannel();
        double[][] uTable = table.getUChannel(); // Same as V channel table

        for (int i = 0; i < PixelBlock.HEIGHT; i++) {
            for (int j = 0; j < PixelBlock.WIDTH; j++) {
                // We have to do integer division for it to work
                double yVal = (double)((int) origY[i][j] / (int)yTable[i][j]);
                double uVal = (double)((int)origU[i][j] / (int)uTable[i][j]);
                double vVal = (double)((int)origV[i][j] / (int)uTable[i][j]);

                quantizedY[i][j] = yVal;
                quantizedU[i][j] = uVal;
                quantizedV[i][j] = vVal;
            }
        }

        return quantizedBlock;
    }

    public static PixelBlock dequantize(PixelBlock input, QuantizationTable table) {

        double[][] origY = input.getYChannel();
        double[][] origU = input.getUChannel();
        double[][] origV = input.getVChannel();

        PixelBlock dequantizedBlock = new PixelBlock();

        double[][] dequantizedY = dequantizedBlock.getYChannel();
        double[][] dequantizedU = dequantizedBlock.getUChannel();
        double[][] dequantizedV = dequantizedBlock.getVChannel();

        double[][] yTable = table.getYChannel();
        double[][] uTable = table.getUChannel(); // Same as V channel table

        for (int i = 0; i < PixelBlock.HEIGHT; i++) {
            for (int j = 0; j < PixelBlock.WIDTH; j++) {
                double yVal = origY[i][j] * yTable[i][j];
                double uVal = origU[i][j] * uTable[i][j];
                double vVal = origV[i][j] * uTable[i][j];

                dequantizedY[i][j] = yVal;
                dequantizedU[i][j] = uVal;
                dequantizedV[i][j] = vVal;
            }
        }

        return dequantizedBlock;
    }

}    
