import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;

public class PixelBlockLabel extends JLabel {

    boolean threeChannels;
    
    public PixelBlockLabel(String label, boolean tc) {
        super();
        threeChannels = tc;
        setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        setBorder(BorderFactory.createTitledBorder(new EtchedBorder(), label));
    }

    public void setPixelBlock(PixelBlock pb) {
        String text = "<html><table border='1'>";
        
        int[][][] vals = pb.getAllChannels();
        for (int i = 0; i < PixelBlock.WIDTH; i++) {
            text += "<tr>";
            for (int j = 0; j < PixelBlock.HEIGHT; j++) {
                text += "<td>";
                if (threeChannels) {
                    text += String.format("(%d, %d, %d)", vals[i][j][0],
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
