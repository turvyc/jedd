import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.GridLayout;
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
    public static String UPDATE_BUTTON = "Update";

    private JeddController controller;
    private JeddActionListener actionListener;
    private JeddMouseListener mouseListener;

    private BufferedImage originalImage;
    private JLabel imageLabel;

    private JPanel originalImagePanel;
    private JPanel pixelBlocksPanel;
    private JPanel newImagePanel;

    private PixelBlockLabel rgbLabel;
    private PixelBlockLabel yuvLabel;
    private PixelBlockLabel subsampleLabel;
    private PixelBlockLabel dctLabel;
    private PixelBlockLabel qtLabel;
    private PixelBlockLabel finalLabel;

    public JeddFrame() {
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setTitle(FRAME_TITLE);

        // The listeners are internal classes, at bottom of class
        actionListener = new JeddActionListener();
        mouseListener = new JeddMouseListener();

        originalImagePanel = createOriginalImagePanel();
        pixelBlocksPanel = createPixelBlocksPanel();
        JPanel masterPanel = new JPanel();

        masterPanel.add(originalImagePanel);

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
    public void drawPixelBlock(int x, int y) {
        // Expand the size so the border encloses the pixels, not cover them
        int width = PixelBlock.WIDTH + 2;
        int height = PixelBlock.HEIGHT + 2;

        // Copy the original image 
        BufferedImage clone = new BufferedImage(originalImage.getWidth(),
                originalImage.getHeight(), originalImage.getType());
        Graphics2D g2 = clone.createGraphics();
        g2.drawImage(originalImage, 0, 0, null);

        // Draw the pixel block. The x, y, width, and height offsets
        // are because we want to enclose the pixel block.
        g2.draw(new Rectangle(x - 1, y - 1, width, height));
        g2.dispose();

        // Set the icon
        imageLabel.setIcon(new ImageIcon(clone));
    }

    private JPanel createOriginalImagePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
                    new EtchedBorder(), "Original Image"));
        imageLabel = new JLabel();

        JButton openImageButton = new JButton(OPEN_BUTTON);

        openImageButton.addActionListener(actionListener);

        panel.add(imageLabel);
        panel.add(openImageButton);
        return panel;
    }

    private JPanel createPixelBlocksPanel() {
        final int ROWS = 2;
        final int COLS = 3;
        final int PAD= 5;
        JPanel panel = new JPanel(new GridLayout(ROWS, COLS, PAD, PAD));

        rgbLabel = new PixelBlockLabel("RGB", true);
        yuvLabel = new PixelBlockLabel("YUV", true);
        subsampleLabel = new PixelBlockLabel("Subsample", true);
        dctLabel = new PixelBlockLabel("DCT", false);
        qtLabel = new PixelBlockLabel("Quantization Table", false);
        finalLabel = new PixelBlockLabel("Quantized", false);

        panel.add(rgbLabel);
        panel.add(yuvLabel);
        panel.add(subsampleLabel);
        panel.add(dctLabel);
        panel.add(qtLabel);
        panel.add(finalLabel);

        return panel;
    }

    public class JeddActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals(JeddFrame.OPEN_BUTTON)) {
                controller.openImage();
            }
        }
    }

    public class JeddMouseListener extends MouseInputAdapter {

        public void mouseClicked(MouseEvent e) {
            controller.selectPixelBlock(e.getX(), e.getY());
        }
    }
}
