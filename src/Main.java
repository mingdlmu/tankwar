import javax.swing.*;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author lim-ad
 */
public class Main extends JFrame {

    public static void main(String[] args) {
        new Main().launchFrame();
    }

    public void launchFrame(){

        this.setTitle("tankwar");
        this.setLocation(300,400);
        this.setSize(300,400);
        this.setBackground(Color.RED);
        this.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });

        this.setResizable(false);
        this.setVisible(true);

    }
}
