import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

public class JeddModel {
    public static final int BLOCK_WIDTH = 2;
    public static final int BLOCK_HEIGHT = 2;

    private BufferedImage image;
    private QuantizationTable table;
    private Raster pixelBlock;

    private ArrayList<BufferedImage> rgbChannels;
    private ArrayList<BufferedImage> YCbCrChannels;

    public JeddModel() {
        table = new QuantizationTable();
        pixelBlock = null;
    }

    public void setImage(BufferedImage img) {
        image = img;
        WritableRaster ycbcr = ColorConverter.RGBToYCbCr(img);
        YCbCrChannels = ColorConverter.getYCbCrChannels(ycbcr);
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setPixelBlock(int x, int y) {
        pixelBlock = image.getData(new Rectangle(x, y,
                    BLOCK_WIDTH, BLOCK_HEIGHT));
    }

    public BufferedImage getYChannel() {
        return YCbCrChannels.get(0);
    }

    public BufferedImage getCbChannel() {
        return YCbCrChannels.get(1);
    }

    public BufferedImage getCrChannel() {
        return YCbCrChannels.get(2);
    }

    public BufferedImage getRChannel() {
        return rgbChannels.get(0);
    }

    public BufferedImage getGChannel() {
        return rgbChannels.get(1);
    }

    public BufferedImage getBChannel() {
        return rgbChannels.get(2);
    }

}
