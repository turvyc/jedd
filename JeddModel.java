import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.lang.InterruptedException;
import java.util.ArrayList;
import java.util.Observable;

public class JeddModel extends Observable {

    private BufferedImage image;
    private ChromaSubsampler subsampler;
    private QuantizationTable table;

    private PixelBlock rgbBlock;
    private PixelBlock yuvBlock;
    private PixelBlock subsampleBlock;
    private PixelBlock dctBlock;
    private PixelBlock quantizedBlock;

    public JeddModel() {
        table = new QuantizationTable();
        subsampler = new ChromaSubsampler();
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

        rgbBlock = new PixelBlock(pixels);
        yuvBlock = ColorConverter.RGBtoYUV(rgbBlock);
        subsampleBlock = subsampler.subsample(yuvBlock);
        dctBlock = DCT.dct(subsampleBlock);

        setChanged();
        notifyObservers();
    }

    public PixelBlock getRgbBlock() {
        return rgbBlock;
    }

    public PixelBlock getYuvBlock() {
        return yuvBlock;
    }

    public PixelBlock getSubsampleBlock() {
        return subsampleBlock;
    }

    public PixelBlock getDctBlock() {
        return dctBlock;
    }
}
