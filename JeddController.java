import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class JeddController {

    private JeddFrame frame;
    private JeddModel model;

    public static final int IMAGE_WIDTH = 5;

    public JeddController(JeddFrame f, JeddModel m) {
        frame = f;
        model = m;
        frame.addController(this);
    }

    public void openImage() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images",
                "jpg", "jpeg", "gif", "png", "bmp", "tiff");
        chooser.setFileFilter(filter);
        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedImage image = ImageIO.read(chooser.getSelectedFile());

                // Scale the image. -1 is used to maintain aspect ratio
                Image img = image.getScaledInstance(
                        IMAGE_WIDTH, -1, Image.SCALE_FAST);

                // Paint it back into the more-convenient BufferedImage
                int width = img.getWidth(null);
                int height = img.getHeight(null);
                BufferedImage buff = new BufferedImage(width, height, 
                        BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = buff.createGraphics();
                g2.drawImage(img, 0, 0, null);
                g2.dispose();

                // Set the image in the frame and model
                frame.setImage(buff);
                model.setImage(buff);
                frame.updateChannels(model.getYChannel(), model.getCbChannel(),
                        model.getCrChannel());
            }
            catch (IOException e) {}
        }
    }

    public void selectEntireImage() {
        System.out.println("Select entire image");
    }

    public void selectPixelBlock(int x, int y) {
        frame.drawPixelBlock(x, y, JeddModel.BLOCK_WIDTH, JeddModel.BLOCK_HEIGHT);
        model.setPixelBlock(x, y);
    }
}
