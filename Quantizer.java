public class Quantizer {
    
    public static PixelBlock quantize(PixelBlock input, QuantizationTable table) {

        PixelBlock quantizedBlock = new PixelBlock();

        float[][] origY = input.getYChannel();
        float[][] origU = input.getUChannel();
        float[][] origV = input.getVChannel();

        float[][] quantizedY = quantizedBlock.getYChannel();
        float[][] quantizedU = quantizedBlock.getUChannel();
        float[][] quantizedV = quantizedBlock.getVChannel();

        float[][] yTable = table.getYChannel();
        float[][] uTable = table.getUChannel(); // Same as V channel table

        for (int i = 0; i < PixelBlock.HEIGHT; i++) {
            for (int j = 0; j < PixelBlock.WIDTH; j++) {
                float yVal = origY[i][j] / yTable[i][j];
                float uVal = origU[i][j] / uTable[i][j];
                float vVal = origV[i][j] / uTable[i][j];

                quantizedY[i][j] = yVal;
                quantizedU[i][j] = uVal;
                quantizedV[i][j] = vVal;
            }
        }

        return quantizedBlock;
    }

    public static PixelBlock dequantize(PixelBlock input, QuantizationTable table) {

        float[][] origY = input.getYChannel();
        float[][] origU = input.getUChannel();
        float[][] origV = input.getVChannel();

        PixelBlock dequantizedBlock = new PixelBlock();

        float[][] dequantizedY = dequantizedBlock.getYChannel();
        float[][] dequantizedU = dequantizedBlock.getUChannel();
        float[][] dequantizedV = dequantizedBlock.getVChannel();

        float[][] yTable = table.getYChannel();
        float[][] uTable = table.getUChannel(); // Same as V channel table

        for (int i = 0; i < PixelBlock.HEIGHT; i++) {
            for (int j = 0; j < PixelBlock.WIDTH; j++) {
                float yVal = origY[i][j] * yTable[i][j];
                float uVal = origU[i][j] * uTable[i][j];
                float vVal = origV[i][j] * uTable[i][j];

                dequantizedY[i][j] = yVal;
                dequantizedU[i][j] = uVal;
                dequantizedV[i][j] = vVal;
            }
        }

        return dequantizedBlock;
    }

}    
