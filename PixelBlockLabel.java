import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import java.util.Observer;
import java.util.Observable;

/**
 * Displays a pixel block in the GUI.
 */
public class PixelBlockLabel extends JLabel implements Observer {

    // The types of pixel blocks we want to show
    public static final int TYPE_RGB = 0;
    public static final int TYPE_YUV = 1;
    public static final int TYPE_SUB = 2;
    public static final int TYPE_DCT = 3;
    public static final int TYPE_QT = 4;
    public static final int TYPE_QTD = 5;

    private int type;       // The type of this one
    private int channel;    // The channel this is currently displaying

    /**
     * Creates the label with the specified type and titled border.
     */
    public PixelBlockLabel(String label, int t) {
        super();
        type = t;
        channel = 0;
        setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        setBorder(BorderFactory.createTitledBorder(new EtchedBorder(), label));
    }

    /**
     * Required by Observer class, called when the model is updated.
     */
    public void update(Observable o, Object arg) {
        JeddModel model = (JeddModel) o;        // Cast the model
        channel = model.getVisibleChannel();    // Check which channel should be shown
        // Depending on the type, get the pixel block from the model
        switch (type) {
            case TYPE_RGB: setPixelBlock(model.getRgbBlock());
                           break;
            case TYPE_YUV: setPixelBlock(model.getYuvBlock());
                           break;
            case TYPE_SUB: setPixelBlock(model.getSubsampleBlock());
                           break;
            case TYPE_DCT: setPixelBlock(model.getDctBlock());
                           break;
            case TYPE_QT: setPixelBlock(model.getQuantizationTable());
                           break;
            case TYPE_QTD: setPixelBlock(model.getQuantizedBlock());
                           break;
        }
    }

    /**
     * Updates the label text using an HTML table.
     */
    public void setPixelBlock(PixelBlock pb) {
        String text = "<html><table border='1'>";
        
        double[][][] vals = pb.getAllChannels();
        for (int i = 0; i < PixelBlock.WIDTH; i++) {
            text += "<tr>";
            for (int j = 0; j < PixelBlock.HEIGHT; j++) {
                text += "<td align='center'>";
                text += String.format("%d", (int)vals[i][j][channel]);
                text += "</td>";
            }
            text += "</tr>";
        }
        text += "</table></html>";

        setText(text);
    }
}
