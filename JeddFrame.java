import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.event.MouseInputAdapter;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class JeddFrame extends JFrame {

    private final int FRAME_HEIGHT = 800;
    private final int FRAME_WIDTH = 1024;
    private final String FRAME_TITLE = "Jedd";

    public static String OPEN_BUTTON = "Open image";
    public static String SELECT_BUTTON = "Select entire image";

    private JeddController controller;
    private JeddActionListener actionListener;
    private JeddMouseListener mouseListener;

    private BufferedImage originalImage;

    private JLabel imageLabel;
    private JLabel rChannelLabel;
    private JLabel gChannelLabel;
    private JLabel bChannelLabel;
    private JLabel yChannelLabel;
    private JLabel uChannelLabel;
    private JLabel vChannelLabel;

    public JeddFrame() {
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setTitle(FRAME_TITLE);

        actionListener = new JeddActionListener();
        mouseListener = new JeddMouseListener();

        JPanel originalImagePanel = createOriginalImagePanel();
        JPanel colorChannelPanel = createColorChannelPanel();
        JPanel pixelBlockPanel = createPixelBlockPanel();
        JPanel decodedYUVPanel = createDecodedYUVPanel();
        JPanel decodedImagePanel = createDecodedImagePanel();

        JPanel masterPanel = new JPanel();

        masterPanel.add(originalImagePanel);
        masterPanel.add(colorChannelPanel);

        add(masterPanel);
    }

    public void addController(JeddController c) {
        controller = c;
    }

    public void setImage(BufferedImage image) {
        originalImage = image;
        ImageIcon icon = new ImageIcon(image);
        imageLabel.setIcon(icon);
        imageLabel.addMouseListener(mouseListener);
    }

    /**
     * Draws a border around the selected pixel block on the original image.
     */
    public void drawPixelBlock(int x, int y, int width, int height) {
        // Copy the original image 
        BufferedImage clone = new BufferedImage(originalImage.getWidth(),
                originalImage.getHeight(), originalImage.getType());
        Graphics2D g2 = clone.createGraphics();
        g2.drawImage(originalImage, 0, 0, null);

        // Draw the pixel block. The x, y, width, and height offsets
        // are because we want to enclose the pixel block.
        g2.draw(new Rectangle(x - 1, y - 1, width + 2, height + 2));
        g2.dispose();

        // Set the icon
        imageLabel.setIcon(new ImageIcon(clone));
    }

    public void updateChannels(BufferedImage redChannel,
            BufferedImage greenChannel, BufferedImage blueChannel) {
        rChannelLabel.setIcon(new ImageIcon(redChannel));
        gChannelLabel.setIcon(new ImageIcon(greenChannel));
        bChannelLabel.setIcon(new ImageIcon(blueChannel));
    }

    private JPanel createOriginalImagePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
                    new EtchedBorder(), "Original Image"));
        imageLabel = new JLabel();

        JButton openImageButton = new JButton(OPEN_BUTTON);
        JButton selectEntireImageButton = new JButton(SELECT_BUTTON);

        openImageButton.addActionListener(actionListener);
        selectEntireImageButton.addActionListener(actionListener);

        panel.add(imageLabel);
        panel.add(openImageButton);
        panel.add(selectEntireImageButton);
        return panel;
    }

    private JPanel createColorChannelPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
                    new EtchedBorder(), "Original Color Channels"));
        rChannelLabel = new JLabel();
        gChannelLabel = new JLabel();
        bChannelLabel = new JLabel();

        panel.add(rChannelLabel);
        panel.add(gChannelLabel);
        panel.add(bChannelLabel);
        return panel;
    }

    private JPanel createPixelBlockPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
                    new EtchedBorder(), "Pixel Block"));
        return panel;
    }

    private JPanel createDecodedYUVPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
                    new EtchedBorder(), "Decoded YUV"));
        return panel;
    }

    private JPanel createDecodedImagePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
                    new EtchedBorder(), "Decoded Image"));
        return panel;
    }

    public class JeddActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals(JeddFrame.OPEN_BUTTON)) {
                controller.openImage();
            }

            else if (command.equals(JeddFrame.SELECT_BUTTON)) {
                controller.selectEntireImage();
            }
        }
    }

    public class JeddMouseListener extends MouseInputAdapter {

        public void mouseClicked(MouseEvent e) {
            controller.selectPixelBlock(e.getX(), e.getY());
        }
    }
}
