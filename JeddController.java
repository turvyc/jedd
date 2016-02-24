import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.Observer;

/**
 * The controller acts as the interface between the GUI and the data model.
 */
public class JeddController {

    private JeddFrame frame;
    private JeddModel model;

    // Chosen to be a nice round multiple of 8
    public static final int IMAGE_WIDTH = 800;

    public JeddController(JeddFrame f, JeddModel m) {
        frame = f;
        model = m;
        frame.addController(this);
        
        // Add the observers to the model
        for (Observer o : frame.getObservers())
            model.addObserver(o);
    }

    /**
     * Opens an image, processes it, and loads it into the GUI and data model.
     */
    public void openImage() {
        // Allow the user to open an image file
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images",
                "jpg", "jpeg", "gif", "png", "bmp", "tiff");
        chooser.setFileFilter(filter);

        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedImage image = ImageIO.read(chooser.getSelectedFile());

                // Scale the image width. -1 is used to maintain aspect ratio
                Image img = image.getScaledInstance(
                        IMAGE_WIDTH, -1, Image.SCALE_FAST);

                // Paint the Image into the more-convenient BufferedImage
                int width = img.getWidth(null);
                int height = img.getHeight(null);
                BufferedImage buff = new BufferedImage(width, height, 
                        BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = buff.createGraphics();
                g2.drawImage(img, 0, 0, null);
                g2.dispose();

                // Trim pixels off the bottom so it is a nice multiple of 8
                int trimSize = buff.getHeight() % PixelBlock.HEIGHT;
                BufferedImage trimmed = buff.getSubimage(0, 0, buff.getWidth(), buff.getHeight() - trimSize);

                // Set the image in the frame and model
                frame.enableControlPanel(true);
                frame.setOriginalImage(trimmed);
                model.setOriginalImage(trimmed);
                frame.setCompressedImage(model.getCompressedImage());
            }
            catch (IOException e) {}
        }
    }

    /**
     * Sets the user-specified pixel block (when the user clicks on the original image).
     */
    public void selectPixelBlock(int x, int y) {
        int width = model.getImageWidth();
        int height = model.getImageHeight();

        // If the user clicked too close to the border, adjust x and y so we
        // still get a full-sized block
        if (x > width - PixelBlock.WIDTH) 
            x = width - PixelBlock.WIDTH;
        if (y > height - PixelBlock.HEIGHT) 
            y = height - PixelBlock.HEIGHT;

        frame.drawPixelBlock(x, y);
        model.setPixelBlocks(x, y);
    }

    /**
     * Handles events from the GUI ComboBoxes (here be very ugly code).
     */
    public void comboBox(String command) {

        // Channel options
        if (command.equals(JeddFrame.CHANNEL_1_OPTION)) {
            model.setVisibleChannel(1);
            return;
        }
        else if (command.equals(JeddFrame.CHANNEL_2_OPTION)) {
            model.setVisibleChannel(2);
            return;
        }
        else if (command.equals(JeddFrame.CHANNEL_3_OPTION)) {
            model.setVisibleChannel(3);
            return;
        }

        // Quantization table options
        else if (command.equals(JeddFrame.QT_DEFAULT_OPTION))
            model.setQT(-1);
        else if (command.equals(JeddFrame.QT_LOW_CONST_OPTION))
            model.setQT(5);
        else if (command.equals(JeddFrame.QT_HIGH_CONST_OPTION))
            model.setQT(100);

        // Subsample algorithm options
        else if (command.equals(JeddFrame.SUBSAMPLE_420_OPTION))
            model.setSubsampler(ChromaSubsampler.TYPE_420);
        else if (command.equals(JeddFrame.SUBSAMPLE_411_OPTION))
            model.setSubsampler(ChromaSubsampler.TYPE_411);
        else if (command.equals(JeddFrame.SUBSAMPLE_444_OPTION))
            model.setSubsampler(ChromaSubsampler.TYPE_444);
        else if (command.equals(JeddFrame.SUBSAMPLE_440_OPTION))
            model.setSubsampler(ChromaSubsampler.TYPE_440);
        else if (command.equals(JeddFrame.SUBSAMPLE_422_OPTION))
            model.setSubsampler(ChromaSubsampler.TYPE_422);

        // Subsample filter options
        else if (command.equals(JeddFrame.SUBSAMPLE_FILTER_CONST))
            model.setSubsamplerFilter(ChromaSubsampler.CONSTANT_FILTER);
        else if (command.equals(JeddFrame.SUBSAMPLE_FILTER_AVG))
            model.setSubsamplerFilter(ChromaSubsampler.AVERAGE_FILTER);

        frame.setCompressedImage(model.getCompressedImage());
    }
}
