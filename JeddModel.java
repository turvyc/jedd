import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.lang.InterruptedException;
import java.util.ArrayList;
import java.util.Observable;

public class JeddModel extends Observable {
    private int x, y;

    private BufferedImage originalImage;
    private BufferedImage compressedImage;
    private ChromaSubsampler subsampler;
    private QuantizationTable qt;
    private DCTMatrix dct;

    private PixelBlock rgbBlock;
    private PixelBlock yuvBlock;
    private PixelBlock subsampleBlock;
    private PixelBlock dctBlock;
    private PixelBlock quantizedBlock;

    private int visibleChannel;

    public JeddModel() {
        x = y = 0;
        subsampler = new ChromaSubsampler();
        qt = new QuantizationTable();
        dct = new DCTMatrix();
        visibleChannel = 0;
    }

    public void setOriginalImage(BufferedImage img) {
        originalImage = img;
        setPixelBlocks(x, y);
        compressedImage = compressImage(originalImage);
    }

    public void setPixelBlocks(int i, int j) {
        x = i;
        y = j;
        rgbBlock = getRGBPixelBlock(x, y);
        yuvBlock = ColorConverter.RGBtoYUV(rgbBlock);
        subsampleBlock = subsampler.subsample(yuvBlock);
        dctBlock = dct.dct(subsampleBlock, true);
        quantizedBlock = Quantizer.quantize(dctBlock, qt);

        setChanged();
        notifyObservers();
    }

    private PixelBlock getRGBPixelBlock(int x, int y) {
        int width = PixelBlock.WIDTH;
        int height = PixelBlock.HEIGHT;

        int[] pixels = new int[width * height];

        PixelGrabber grabber = new PixelGrabber(originalImage, x, y, width, height,
                pixels, 0, width);

        try {
            grabber.grabPixels();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        PixelBlock pb = new PixelBlock();
        pb.loadRGB(pixels);

        return pb;
    }

    public BufferedImage compressImage(BufferedImage original) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        BufferedImage compressed = new BufferedImage(width, height,
                original.getType());

        for (int i = 0; i < width; i += PixelBlock.WIDTH) {
            for (int j = 0; j < height; j += PixelBlock.HEIGHT) {
                // First go one way . . .
                PixelBlock pb = getRGBPixelBlock(i, j);
                pb = ColorConverter.RGBtoYUV(pb);
                pb = subsampler.subsample(pb);
                pb = dct.dct(pb, true);
                pb = Quantizer.quantize(pb, qt);

                // . . . then undo it all.
                pb = Quantizer.dequantize(pb, qt);
                pb = dct.dct(pb, false);
                pb = ColorConverter.YUVtoRGB(pb);

                // Paint the pixel block into the new image
                int[] pixels = pb.getRgbArray();
                compressed.setRGB(i, j, PixelBlock.WIDTH, PixelBlock.HEIGHT, pixels, 0,
                        PixelBlock.WIDTH);
            }
        }
        return compressed;
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

    public QuantizationTable getQuantizationTable() {
        return qt;
    }

    public PixelBlock getQuantizedBlock() {
        return quantizedBlock;
    }

    public int getImageWidth() {
        return originalImage.getWidth();
    }

    public int getImageHeight() {
        return originalImage.getHeight();
    }

    public BufferedImage getOriginalImage() {
        return originalImage;
    }

    public BufferedImage getCompressedImage() {
        return compressedImage;
    }

    public int getVisibleChannel() {
        return visibleChannel;
    }

    public void setVisibleChannel(int c) {
        visibleChannel = c - 1;
        setChanged();
        notifyObservers();
    }

    public void setQT(int i) {
        if (i == -1)
            qt.setDefault();
        else
            qt.setConstant((double) i);
        setPixelBlocks(x, y);
        compressedImage = compressImage(originalImage);
    }
}
