package starblaster;

import java.awt.Dimension;
import javax.swing.JFrame;

public class LobbyFrame extends JFrame {
    private LobbyPanel pnLobby = new LobbyPanel();
    
    public LobbyFrame(){
        this.setTitle("Star Blaster");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       
        this.setLocationRelativeTo(null);
        this.add(pnLobby);
    }
}
