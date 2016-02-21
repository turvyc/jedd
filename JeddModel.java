import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class JeddModel {
    public static final int BLOCK_WIDTH = 2;
    public static final int BLOCK_HEIGHT = 2;

    private BufferedImage image;
    private QuantizationTable table;
    private Raster pixelBlock;

    private BufferedImage redChannel;
    private BufferedImage greenChannel;
    private BufferedImage blueChannel;

    public JeddModel() {
        table = new QuantizationTable();
        pixelBlock = null;
    }

    public void setImage(BufferedImage img) {
        image = img;
    }

    public void setPixelBlock(int x, int y) {
        pixelBlock = image.getData(new Rectangle(x, y,
                    BLOCK_WIDTH, BLOCK_HEIGHT));
    }

    public BufferedImage getRedChannel() {
        return redChannel;
    }

    public BufferedImage getGreenChannel() {
        return greenChannel;
    }

    public BufferedImage getBlueChannel() {
        return blueChannel;
    }
}
