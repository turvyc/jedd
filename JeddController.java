import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.Observer;

public class JeddController {

    private JeddFrame frame;
    private JeddModel model;

    // Chosen to be a nice round multiple of 8
    public static final int IMAGE_WIDTH = 512;

    public JeddController(JeddFrame f, JeddModel m) {
        frame = f;
        model = m;
        frame.addController(this);
        
        // Add the observers to the model
        for (Observer o : frame.getObservers())
            model.addObserver(o);
    }

    public void openImage() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images",
                "jpg", "jpeg", "gif", "png", "bmp", "tiff");
        chooser.setFileFilter(filter);
        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedImage image = ImageIO.read(chooser.getSelectedFile());

                // Scale the image to 512 pixel width. -1 is used to maintain aspect ratio
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
                frame.setOriginalImage(trimmed);
                model.setOriginalImage(trimmed);
                frame.setCompressedImage(model.getCompressedImage());
            }
            catch (IOException e) {}
        }
    }

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

    public void comboBox(String command) {

        if (command.equals(JeddFrame.CHANNEL_1_OPTION))
            model.setVisibleChannel(1);
        if (command.equals(JeddFrame.CHANNEL_2_OPTION))
            model.setVisibleChannel(2);
        if (command.equals(JeddFrame.CHANNEL_3_OPTION))
            model.setVisibleChannel(3);
            /*
    SUBSAMPLE_420_OPTION 
    SUBSAMPLE_411_OPTION 
    SUBSAMPLE_444_OPTION 
    SUBSAMPLE_440_OPTION 
    SUBSAMPLE_422_OPTION 
    QT_DEFAULT_OPTION 
    QT_LOW_CONST_OPTION 
    QT_HIGH_CONST_OPTION 
    */
    }
}
