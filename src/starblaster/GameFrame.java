package starblaster;

import java.awt.Dimension;
import javax.swing.JFrame;

public class GameFrame extends JFrame {
    private GamePanel pnGame = new GamePanel();
    
    public GameFrame(){
        this.setTitle("Space Shooter Game");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.add(pnGame);
    }
}
