package starblaster;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LobbyPanel extends JPanel {
    private int WIDTH = 800;
    private int HEIGHT = 600;
    private JLabel lbl;
    private JButton btnStart;
    private BufferedImage backgroundImg;
    private ImageIcon startIcon;
    
    public LobbyPanel(){
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setLayout(null);
        
        try{ 
            backgroundImg = ImageIO.read(getClass().getResourceAsStream("/starblaster/image/background1.jpg"));
            startIcon = new ImageIcon(getClass().getResource("/starblaster/image/playgame.png"));
        }
        catch(Exception e){ 
            e.printStackTrace(); 
        }
        
        Image scaledImage = startIcon.getImage().getScaledInstance(250, 220, Image.SCALE_SMOOTH);
        startIcon = new ImageIcon(scaledImage);
        btnStart = new JButton(startIcon);
        btnStart.setBounds(WIDTH/2 - 120, HEIGHT/2 + 10, 250, 220);
        btnStart.setOpaque(false);
        btnStart.setContentAreaFilled(false);
        btnStart.setBorderPainted(false);
        btnStart.setFocusPainted(false);
        this.add(btnStart);
        
        btnStart.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                GameFrame frameGame = new GameFrame();
                frameGame.setVisible(true);
            }     
        });
        
    }
    
    @Override
    public void paintComponent(Graphics g){ 
        super.paintComponent(g); 
        g.drawImage(backgroundImg, 0, 0, 800, 600, null);
    }
}
