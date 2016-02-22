import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import java.util.Observer;
import java.util.Observable;

public class PixelBlockLabel extends JLabel implements Observer {

    public static final int TYPE_RGB = 0;
    public static final int TYPE_YUV = 1;
    public static final int TYPE_SUB = 2;
    public static final int TYPE_DCT = 3;
    public static final int TYPE_QT = 4;
    public static final int TYPE_QTD = 5;

    private int type;

    public PixelBlockLabel(String label, int t) {
        super();
        type = t;
        setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        setBorder(BorderFactory.createTitledBorder(new EtchedBorder(), label));
    }

    public void update(Observable o, Object arg) {
        JeddModel model = (JeddModel) o;
        switch (type) {
            case TYPE_RGB: setPixelBlock(model.getRgbBlock());
                           break;
        }
    }

    public void setPixelBlock(PixelBlock pb) {
        String text = "<html><table border='1'>";
        
        int[][][] vals = pb.getAllChannels();
        for (int i = 0; i < PixelBlock.WIDTH; i++) {
            text += "<tr>";
            for (int j = 0; j < PixelBlock.HEIGHT; j++) {
                text += "<td align='center'>";
                if (type == TYPE_RGB || type == TYPE_YUV || type == TYPE_SUB) {
                    text += String.format("%d,%d,%d", vals[i][j][0],
                            vals[i][j][1], vals[i][j][2]);
                }
                else // Display only the first channel (Y or R).
                    text += vals[i][j][0];
                text += "</td>";
            }
            text += "</tr>";
        }
        text += "</table></html>";

        setText(text);
    }
}
