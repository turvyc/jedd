/**
 * Represents a quantization table. 
 * This is just a special case of a PixelBlock. Channel 1 is the luminance
 * QT, and channels 2 and 3 are the chrominance QTs. 
 */
public class QuantizationTable extends PixelBlock {

    private final int[][] DEFAULT_LUMINANCE = {
        {16, 11, 10, 16, 24, 40, 51, 61},
        {12, 12, 14, 19, 26, 58, 60, 55},
        {14, 13, 16, 24, 40, 57, 69, 56},
        {14, 17, 22, 29, 51, 87, 80, 62},
        {18, 22, 37, 56, 68, 109, 103, 77},
        {24, 35, 55, 64, 81, 104, 113, 92},
        {49, 64, 78, 87, 103, 121, 120, 101},
        {72, 92, 95, 98, 112, 100, 103, 99}
    };

    private final int[][] DEFAULT_CHROMINANCE = {
        {17, 18, 24, 47, 99, 99, 99, 99},
        {18, 21, 26, 66, 99, 99, 99, 99},
        {24, 26, 56, 99, 99, 99, 99, 99},
        {47, 66, 99, 99, 99, 99, 99, 99},
        {99, 99, 99, 99, 99, 99, 99, 99},
        {99, 99, 99, 99, 99, 99, 99, 99},
        {99, 99, 99, 99, 99, 99, 99, 99},
        {99, 99, 99, 99, 99, 99, 99, 99}
    };

    public QuantizationTable() {
        super();
        channel1 = DEFAULT_LUMINANCE;
        channel2 = DEFAULT_CHROMINANCE;
        channel3 = DEFAULT_CHROMINANCE;
    }

    public void setConstant(int c) {
        for (int i = 0; i < PixelBlock.WIDTH; i++) {
            for (int j = 0; j < PixelBlock.HEIGHT; j++)
                channel1[i][j] = c;
        }
    }

    public void setLuminance(int[][] vals) {
        channel1 = vals;
    }

    public void setChrominance(int [][] vals) {
        channel2 = vals;
        channel3 = vals;
    }
}
