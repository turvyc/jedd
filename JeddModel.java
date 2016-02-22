import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.lang.InterruptedException;
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
        int width = PixelBlock.WIDTH;
        int height = PixelBlock.HEIGHT;

        int[] pixels = new int[width * height];

        PixelGrabber grabber = new PixelGrabber(image, x, y, width, height,
                pixels, 0, width);

        try {
            grabber.grabPixels();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        pixelBlock = new PixelBlock(pixels);
    }
}
