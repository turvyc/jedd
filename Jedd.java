import javax.swing.JFrame;

public class Jedd {

    public static void main(String[] args) {
        JeddModel model = new JeddModel();
        JeddFrame frame = new JeddFrame();
        JeddController controller = new JeddController(frame, model);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
