public class Quantizer {
    
    public static PixelBlock quantize(PixelBlock input, QuantizationTable table) {

        PixelBlock quantizedBlock = new PixelBlock();

        int[][] origY = input.getYChannel();
        int[][] origU = input.getUChannel();
        int[][] origV = input.getVChannel();

        int[][] quantizedY = quantizedBlock.getYChannel();
        int[][] quantizedU = quantizedBlock.getUChannel();
        int[][] quantizedV = quantizedBlock.getVChannel();

        int[][] yTable = table.getYChannel();
        int[][] uTable = table.getUChannel(); // Same as V channel table

        for (int i = 0; i < PixelBlock.HEIGHT; i++) {
            for (int j = 0; j < PixelBlock.WIDTH; j++) {
                double yVal = origY[i][j] / yTable[i][j];
                double uVal = origU[i][j] / uTable[i][j];
                double vVal = origV[i][j] / uTable[i][j];

                quantizedY[i][j] = (int) yVal;
                quantizedU[i][j] = (int) uVal;
                quantizedV[i][j] = (int) vVal;
            }
        }

        return quantizedBlock;
    }

    public static PixelBlock dequantize(PixelBlock input, QuantizationTable table) {

        int[][] origY = input.getYChannel();
        int[][] origU = input.getUChannel();
        int[][] origV = input.getVChannel();

        PixelBlock dequantizedBlock = new PixelBlock();

        int[][] dequantizedY = dequantizedBlock.getYChannel();
        int[][] dequantizedU = dequantizedBlock.getUChannel();
        int[][] dequantizedV = dequantizedBlock.getVChannel();

        int[][] yTable = table.getYChannel();
        int[][] uTable = table.getUChannel(); // Same as V channel table

        for (int i = 0; i < PixelBlock.HEIGHT; i++) {
            for (int j = 0; j < PixelBlock.WIDTH; j++) {
                double yVal = origY[i][j] * yTable[i][j];
                double uVal = origU[i][j] * uTable[i][j];
                double vVal = origV[i][j] * uTable[i][j];

                dequantizedY[i][j] = (int) yVal;
                dequantizedU[i][j] = (int) uVal;
                dequantizedV[i][j] = (int) vVal;
            }
        }

        return dequantizedBlock;
    }

}    
