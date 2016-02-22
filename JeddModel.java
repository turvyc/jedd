import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

public class JeddModel {

    private BufferedImage image;
    private QuantizationTable table;
    private PixelBlock pixelBlock;

    public JeddModel() {
        table = new QuantizationTable();
    }

    public void setImage(BufferedImage img) {
        image = img;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getImageWidth() {
        return image.getWidth();
    }

    public int getImageHeight() {
        return image.getHeight();
    }

    public void setPixelBlock(int x, int y) {
        /*
        pixelBlock = image.getData(new Rectangle(x, y,
                    PixelBlock.WIDTH, PixelBlock.HEIGHT));
                    */
    }
}
