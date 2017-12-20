import gui.MainFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Danil on 05.03.2016.
 */
public class MainClass {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
                    JFrame frame = new MainFrame();
                    frame.setTitle("Server Imitation");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setVisible(true);
                }
        );
    }
}
