import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.lang.InterruptedException;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Represents the data and processes of the JPEG codec.
 */
public class JeddModel extends Observable {
    // The coordinates of the currently highlighted pixel block
    private int x, y;

    // Objects for encoding/decoding
    private BufferedImage originalImage;
    private BufferedImage compressedImage;
    private ChromaSubsampler subsampler;
    private QuantizationTable qt;
    private DCTMatrix dct;

    // Pixel blocks displaying each step of the encoding/decoding process
    private PixelBlock rgbBlock;
    private PixelBlock yuvBlock;
    private PixelBlock subsampleBlock;
    private PixelBlock dctBlock;
    private PixelBlock quantizedBlock;

    // The channel shown in the pixel box labels
    private int visibleChannel;

    public JeddModel() {
        x = y = 0;
        subsampler = new ChromaSubsampler();
        qt = new QuantizationTable();
        dct = new DCTMatrix();
        visibleChannel = 0;
    }

    /**
     * Sets the original image, updates the pixel boxes, and does the
     * initial encoding/decoding.
     */
    public void setOriginalImage(BufferedImage img) {
        originalImage = img;
        setPixelBlocks(x, y);
        compressedImage = compressImage(originalImage);
    }

    /**
     * Runs through the encoding process a specific pixel block, saving
     * each step of the process.
     */
    public void setPixelBlocks(int i, int j) {
        x = i;
        y = j;
        rgbBlock = getRGBPixelBlock(x, y);
        yuvBlock = ColorConverter.RGBtoYUV(rgbBlock);
        subsampleBlock = subsampler.subsample(yuvBlock);
        dctBlock = dct.dct(subsampleBlock, true);
        quantizedBlock = Quantizer.quantize(dctBlock, qt);

        // Update the GUI
        setChanged();
        notifyObservers();
    }

    /**
     * Returns the RGB pixel block when a user clicks on the original image.
     */
    private PixelBlock getRGBPixelBlock(int x, int y) {
        int width = PixelBlock.WIDTH;
        int height = PixelBlock.HEIGHT;

        int[] pixels = new int[width * height];

        // Grabs an array of RGB encoded pixels of length width x height
        PixelGrabber grabber = new PixelGrabber(originalImage, x, y, width, height,
                pixels, 0, width);

        try {
            grabber.grabPixels();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Load the RGB array into the pixel block
        PixelBlock pb = new PixelBlock();
        pb.loadRGB(pixels);

        return pb;
    }

    /**
     * The actual encoding/decoding of the image happens here.
     */
    public BufferedImage compressImage(BufferedImage original) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        // Create the new, compressed image
        BufferedImage compressed = new BufferedImage(width, height,
                original.getType());

        // Cycle through every pixel block
        for (int i = 0; i < width; i += PixelBlock.WIDTH) {
            for (int j = 0; j < height; j += PixelBlock.HEIGHT) {
                // First encode . . .
                PixelBlock pb = getRGBPixelBlock(i, j);
                pb = ColorConverter.RGBtoYUV(pb);
                pb = subsampler.subsample(pb);
                pb = dct.dct(pb, true);
                pb = Quantizer.quantize(pb, qt);

                // . . . then decode
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

    /**
     * Sets the channel displayed by the pixel block labels
     */
    public void setVisibleChannel(int c) {
        visibleChannel = c - 1;
        setChanged();
        notifyObservers();
    }

    /**
     * Sets the quantization table and updates the compressed image.
     */
    public void setQT(int i) {
        if (i == -1)
            qt.setDefault();
        else
            qt.setConstant((double) i);
        setPixelBlocks(x, y);
        compressedImage = compressImage(originalImage);
    }

    /**
     * Sets the subsampling algorithm and updates the compressed image.
     */
    public void setSubsampler(int i) {
        subsampler.setType(i);
        setPixelBlocks(x, y);
        compressedImage = compressImage(originalImage);
    }

    /**
     * Sets the subsampling filter and updates the compressed image.
     */
    public void setSubsamplerFilter(int i) {
        subsampler.setFilter(i);
        setPixelBlocks(x, y);
        compressedImage = compressImage(originalImage);
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
}
