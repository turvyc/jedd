/**
 * Represents a quantization table. 
 * This is just a special case of a PixelBlock. Channel 1 is the luminance
 * QT, and channels 2 and 3 are the chrominance QTs. 
 */
public class QuantizationTable extends PixelBlock {

    // Default JPEG luminence table
    private final double[][] DEFAULT_LUMINANCE = {
        {16, 11, 10, 16, 24, 40, 51, 61},
        {12, 12, 14, 19, 26, 58, 60, 55},
        {14, 13, 16, 24, 40, 57, 69, 56},
        {14, 17, 22, 29, 51, 87, 80, 62},
        {18, 22, 37, 56, 68, 109, 103, 77},
        {24, 35, 55, 64, 81, 104, 113, 92},
        {49, 64, 78, 87, 103, 121, 120, 101},
        {72, 92, 95, 98, 112, 100, 103, 99}
    };

    // Default JPEG chrominance table
    private final double[][] DEFAULT_CHROMINANCE = {
        {17, 18, 24, 47, 99, 99, 99, 99},
        {18, 21, 26, 66, 99, 99, 99, 99},
        {24, 26, 56, 99, 99, 99, 99, 99},
        {47, 66, 99, 99, 99, 99, 99, 99},
        {99, 99, 99, 99, 99, 99, 99, 99},
        {99, 99, 99, 99, 99, 99, 99, 99},
        {99, 99, 99, 99, 99, 99, 99, 99},
        {99, 99, 99, 99, 99, 99, 99, 99}
    };

    /**
     * Creates a default quantization table.
     */
    public QuantizationTable() {
        super();
        setDefault();
    }

    /**
     * Sets the tables to the default JPEG tables.
     */
    public void setDefault() {
        for (int i = 0; i < PixelBlock.WIDTH; i++) {
            for (int j = 0; j < PixelBlock.HEIGHT; j++) {
                channel1[i][j] = DEFAULT_LUMINANCE[i][j];
                channel2[i][j] = DEFAULT_CHROMINANCE[i][j];
                channel3[i][j] = DEFAULT_CHROMINANCE[i][j];
            }
        }
    }

    /**
     * Sets the tables to a constant value.
     * @param c the value
     */
    public void setConstant(double c) {
        for (int i = 0; i < PixelBlock.WIDTH; i++) {
            for (int j = 0; j < PixelBlock.HEIGHT; j++) {
                channel1[i][j] = c;
                channel2[i][j] = c;
                channel3[i][j] = c;
            }
        }
    }

    /**
     * Sets the luminance table to a specified table.
     * @param vals the table
     */
    public void setLuminance(double[][] vals) {
        channel1 = vals;
    }

    /**
     * Sets the chrominance table to a specified table.
     * @param vals the table
     */
    public void setChrominance(double[][] vals) {
        channel2 = vals;
        channel3 = vals;
    }
}
