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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.event.MouseInputAdapter;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Observer;

public class JeddFrame extends JFrame {

    private final int FRAME_HEIGHT = 800;
    private final int FRAME_WIDTH = 2048;
    private final String FRAME_TITLE = "Jedd";

    // Button texts
    public static String OPEN_BUTTON = "Open image";
    public static String UPDATE_BUTTON = "Update";

    // Combo box options
    public static String CHANNEL_1_OPTION = "Channel 1";
    public static String CHANNEL_2_OPTION = "Channel 2";
    public static String CHANNEL_3_OPTION = "Channel 3";
    public static String SUBSAMPLE_420_OPTION = "4:2:0";
    public static String SUBSAMPLE_411_OPTION = "4:2:1";
    public static String SUBSAMPLE_444_OPTION = "4:4:4";
    public static String SUBSAMPLE_440_OPTION = "4:4:0";
    public static String SUBSAMPLE_422_OPTION = "4:2:2";
    public static String QT_DEFAULT_OPTION = "Default";
    public static String QT_LOW_CONST_OPTION = "Low Constant";
    public static String QT_HIGH_CONST_OPTION = "High Constant";

    private JeddController controller;
    private JeddActionListener actionListener;
    private JeddMouseListener mouseListener;

    private BufferedImage originalImage;
    private BufferedImage compressedImage;
    private JLabel originalImageLabel;
    private JLabel compressedImageLabel;

    private JPanel originalImagePanel;
    private JPanel pixelBlocksPanel;
    private JPanel controlPanel;
    private JPanel compressedImagePanel;

    private PixelBlockLabel rgbLabel;
    private PixelBlockLabel yuvLabel;
    private PixelBlockLabel subsampleLabel;
    private PixelBlockLabel dctLabel;
    private PixelBlockLabel qtLabel;
    private PixelBlockLabel quantizedLabel;

    public JeddFrame() {
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setTitle(FRAME_TITLE);
        setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);

        // The listeners are internal classes, at bottom of class
        actionListener = new JeddActionListener();
        mouseListener = new JeddMouseListener();

        originalImagePanel = createOriginalImagePanel();
        pixelBlocksPanel = createPixelBlocksPanel();
        controlPanel = createControlPanel();
        compressedImagePanel = createCompressedImagePanel();
        JPanel masterPanel = new JPanel();

        masterPanel.add(originalImagePanel);
        masterPanel.add(pixelBlocksPanel);
        masterPanel.add(controlPanel);
        masterPanel.add(compressedImagePanel);

        add(masterPanel);
    }

    public void addController(JeddController c) {
        controller = c;
    }

    public void setOriginalImage(BufferedImage image) {
        originalImage = image;
        ImageIcon icon = new ImageIcon(image);
        originalImageLabel.setIcon(icon);
        originalImageLabel.addMouseListener(mouseListener);
    }

    public void setCompressedImage(BufferedImage image) {
        compressedImage = image;
        ImageIcon icon = new ImageIcon(image);
        compressedImageLabel.setIcon(icon);
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
        originalImageLabel.setIcon(new ImageIcon(clone));
    }
    
    private JPanel createOriginalImagePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
                    new EtchedBorder(), "Original Image"));
        originalImageLabel = new JLabel();
        panel.add(originalImageLabel);
        return panel;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
                    new EtchedBorder(), "Control Panel"));

        JButton openImageButton = new JButton(OPEN_BUTTON);
        JButton updateImageButton = new JButton(UPDATE_BUTTON);

        JComboBox<String> channelComboBox = new JComboBox<String>();
        channelComboBox.addItem(CHANNEL_1_OPTION);
        channelComboBox.addItem(CHANNEL_2_OPTION);
        channelComboBox.addItem(CHANNEL_3_OPTION);

        JComboBox<String> subsampleComboBox = new JComboBox<String>();
        subsampleComboBox.addItem(SUBSAMPLE_420_OPTION);
        subsampleComboBox.addItem(SUBSAMPLE_411_OPTION);
        subsampleComboBox.addItem(SUBSAMPLE_422_OPTION);
        subsampleComboBox.addItem(SUBSAMPLE_444_OPTION);
        subsampleComboBox.addItem(SUBSAMPLE_440_OPTION);

        JComboBox<String> qtComboBox = new JComboBox<String>();
        qtComboBox.addItem(QT_DEFAULT_OPTION);
        qtComboBox.addItem(QT_LOW_CONST_OPTION);
        qtComboBox.addItem(QT_HIGH_CONST_OPTION);

        openImageButton.addActionListener(actionListener);
        updateImageButton.addActionListener(actionListener);
        channelComboBox.addActionListener(actionListener);
        subsampleComboBox.addActionListener(actionListener);
        qtComboBox.addActionListener(actionListener);

        panel.add(openImageButton);
        panel.add(updateImageButton);
        panel.add(channelComboBox);
        panel.add(subsampleComboBox);
        panel.add(qtComboBox);

        return panel;
    }

    private JPanel createCompressedImagePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
                    new EtchedBorder(), "Compressed Image"));
        compressedImageLabel = new JLabel();
        panel.add(compressedImageLabel);
        return panel;
    }

    private JPanel createPixelBlocksPanel() {
        final int ROWS = 2;
        final int COLS = 3;
        final int PAD= 5;
        JPanel panel = new JPanel(new GridLayout(ROWS, COLS, PAD, PAD));

        rgbLabel = new PixelBlockLabel("RGB", PixelBlockLabel.TYPE_RGB);
        yuvLabel = new PixelBlockLabel("YUV", PixelBlockLabel.TYPE_YUV);
        subsampleLabel = new PixelBlockLabel("Subsample", PixelBlockLabel.TYPE_SUB);
        dctLabel = new PixelBlockLabel("DCT", PixelBlockLabel.TYPE_DCT);
        qtLabel = new PixelBlockLabel("Quantization Table", PixelBlockLabel.TYPE_QT);
        quantizedLabel = new PixelBlockLabel("Quantized", PixelBlockLabel.TYPE_QTD);

        panel.add(rgbLabel);
        panel.add(yuvLabel);
        panel.add(subsampleLabel);
        panel.add(dctLabel);
        panel.add(qtLabel);
        panel.add(quantizedLabel);

        return panel;
    }

    public ArrayList<Observer> getObservers() {
        ArrayList<Observer> labels = new ArrayList<Observer>();
        labels.add(rgbLabel);
        labels.add(yuvLabel);
        labels.add(subsampleLabel);
        labels.add(dctLabel);
        labels.add(qtLabel);
        labels.add(quantizedLabel);
        return labels;
    }
        
    public class JeddActionListener implements ActionListener {
        private String COMBOBOX = "comboBoxChanged";

        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals(COMBOBOX)) {
                JComboBox cb = (JComboBox) e.getSource();
                controller.comboBox((String)cb.getSelectedItem());
            }

            else if (command.equals(JeddFrame.OPEN_BUTTON)) {
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
