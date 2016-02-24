import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.lang.InterruptedException;
import java.util.ArrayList;
import java.util.Observable;

public class JeddModel extends Observable {

    private BufferedImage originalImage;
    private BufferedImage compressedImage;
    private ChromaSubsampler subsampler;
    private QuantizationTable qt;

    private PixelBlock rgbBlock;
    private PixelBlock yuvBlock;
    private PixelBlock subsampleBlock;
    private PixelBlock dctBlock;
    private PixelBlock quantizedBlock;

    public JeddModel() {
        subsampler = new ChromaSubsampler();
        qt = new QuantizationTable();
    }

    public void setImage(BufferedImage img) {
        originalImage = img;
        compressedImage = compressImage(originalImage);
    }

    public void setPixelBlocks(int x, int y) {
        rgbBlock = getRGBPixelBlock(x, y);
        yuvBlock = ColorConverter.RGBtoYUV(rgbBlock);
        subsampleBlock = subsampler.subsample(yuvBlock);
        dctBlock = DCT.dct(subsampleBlock);
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
                System.out.println("RGB");
                System.out.print(pb);
                System.out.println();
                pb = ColorConverter.RGBtoYUV(pb);
                System.out.println("YUV");
                System.out.print(pb);
                System.out.println();
                pb = subsampler.subsample(pb);
                System.out.println("Subsample");
                System.out.print(pb);
                System.out.println();
                pb = DCT.dct(pb);
                System.out.println("DCT");
                System.out.print(pb);
                System.out.println();
                pb = Quantizer.quantize(pb, qt);
                System.out.println("Quantized");

                System.out.print(pb);
                System.out.println();
                // . . . then undo it all.
                pb = Quantizer.dequantize(pb, qt);
                System.out.println("Dequantized");
                System.out.print(pb);
                System.out.println();
                pb = DCT.idct(pb);
                System.out.println("iDCT");
                System.out.print(pb);
                System.out.println();
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

    public BufferedImage getImage() {
        return originalImage;
    }

}
