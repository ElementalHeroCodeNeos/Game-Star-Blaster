package starblaster;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable, KeyListener {
    private int WIDTH = 800;
    private int HEIGHT = 600;
    private State gameState = State.PLAY;
    private boolean gameOver = false;
    private BufferedImage backgroundImg;
    private BufferedImage playerImg, starImg, bulletImg, lazerImg, enemyImg1, enemyImg2;
    private Entity player, bullet, lazer;
    private int playerScore = 0, playerHealth = 100;
    private ArrayList<Entity> star = new ArrayList<>();
    private int starNumber = 3;
    private ArrayList<Enemy> enemy = new ArrayList<>();
    private int enemyNumber = 1;
    private long timeCounter = 0;
    private Thread thread;
    private int move = 0; // move = -1 là di chuyển sang trái, move = 1 là di chuyển sang phải, move = 0 là đứng yên
    private int shoot = 0, beam = 0; // shoot = 0 là bắn, shoot = 1 
    private Random random = new Random();
    private Sound bgMusic;
    
    public GamePanel(){
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        //this.setBackground(new Color(150, 255, 245));
        this.addKeyListener(this); 
        
        try{ 
            /* Hàm getClass() trả về 1 đối tượng kiểu Class (1 đối tượng đại diện cho lớp đang xét - GamePanel, mang thông tin của lớp đó như package, tên lớp,...)
            Hàm getResourceAsStream() là hàm của lớp Class, dùng để lấy dữ liệu của một tệp dưới dạng Stream -> Trả về kiểu InputStream
            ImageIO là lớp dùng để thao tác đọc ghi với file hình ảnh. Hàm ImageIO.read() để đọc InputStream đó và trả về kiểu BufferedImage
            */
            backgroundImg = ImageIO.read(getClass().getResourceAsStream("/starblaster/image/background2.jpg"));
            playerImg = ImageIO.read(getClass().getResourceAsStream("/starblaster/image/alpha.png"));
            starImg = ImageIO.read(getClass().getResourceAsStream("/starblaster/image/star.png"));
            bulletImg = ImageIO.read(getClass().getResourceAsStream("/starblaster/image/firebullet.png"));
            lazerImg = ImageIO.read(getClass().getResourceAsStream("/starblaster/image/lazer.png"));
            // Hàm getSubimage() dùng để cắt một phần ảnh từ ảnh gốc. Tham số: hoành độ bắt đầu cắt, tung độ bắt đầu cắt, độ dài muốn cắt chiều ngang, độ dài muốn cắt chiều dọc
            enemyImg1 = ImageIO.read(getClass().getResourceAsStream("/starblaster/image/assaultspaceship.png"));
            enemyImg2 = ImageIO.read(getClass().getResourceAsStream("/starblaster/image/ufo.png"));
        }
        catch(Exception e){ // biến e lưu thông tin của lỗi bắt được
            e.printStackTrace(); // In ra dấu vết của lỗi (vị trí lỗi,...)
        }
        // Khởi tạo dữ liệu cho player và bullet
        player = new Entity(WIDTH/2 - 48/2, HEIGHT - 90, 5, 5, 48, 48); // Ta để width và height bằng 48 dù trong ảnh gốc, mỗi vật thể chỉ là 16, 16 vì 16 thì nhỏ quá :)
        bullet = new Entity(WIDTH, HEIGHT + 1000, 0, 5, 48, 48); // Ban đầu, khi chưa nhấn Space để bắn, bullet nằm tại điểm đạn (chứ mà ko thiết lập thì mặc định là (0, 0) thì có thể bị gần toạ độ enemy mới tạo -> playerScore tăng dù ko bắn
        lazer = new Entity(WIDTH, HEIGHT + 1000, 0, 5, 48, 350);
        for(int i=0; i<starNumber; i++){
            Entity newStar = new Entity(3, 10, 10);
            newStar.setX(random.nextInt(WIDTH - 48) + 0);
            newStar.setY(-random.nextInt(48));
            star.add(newStar);
        }
        for(int i=0; i<enemyNumber; i++){
            Enemy newEnemy = new Enemy(1, 48, 48, 20, 20, 1, "Assault Spaceship"); // Loại enemy này có maxHealth = 20  
            newEnemy.setX(random.nextInt(WIDTH - 48) + 0);
            newEnemy.setY(-random.nextInt(48));
            enemy.add(newEnemy);
        }
        // Java cho phép mỗi ctrinh chạy nhiều luồng cùng lúc -> giúp không làm đơ GUI, thực hiện nhiều tác vụ đồng thời và game loop chạy mượt mà 
        thread = new Thread(this); // Lớp Thread dùng để tạo và chạy 1 luồng riêng biệt
        thread.start(); // Tạo 1 luồng mới và chạy hàm run() của đối tượng thuộc lớp Runnable đã truyền vào. Dòng trên truyền vào this - đối tượng GamePanel cũng được vì lớp GamePanel đã kế thừa lớp Runnable  
        
        bgMusic = new Sound("/starblaster/newtypeofhero.wav");
        bgMusic.loop();
    }
    
    @Override // Hàm paintComponent() của lớp JComponent, dùng để vẽ lên component (JPanel, Jbutton,...). Ở đây, lớp JPanel có thể kế thừa phương thức này và ghi đè lên
    public void paintComponent(Graphics g){ // Lớp Graphics giống như 1 cây cọ vẽ, dùng để vẽ các hình ảnh và vật thể lên các Container và được cấp phát tài nguyên hệ thống (GPU,...)
        super.paintComponent(g); // Vẽ nền (như kiểu phác thảo, phân bố cục của tranh). Sau đó ta bổ sung lệnh để vẽ chi tiết (kiểu các vật thể, hình ảnh và tô màu)
        g.drawImage(backgroundImg, 0, 0, 800, 600, null);
        g.drawImage(playerImg, player.getX(), player.getY(), player.getWidth(), player.getHeight(), null);
        if(shoot == 1){ // Nếu shoot = 1 thì mới vẽ hình ảnh viên đạn
            g.drawImage(bulletImg, bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight(), null);
        }
        if(beam == 1){
            g.drawImage(lazerImg, lazer.getX(), lazer.getY(), lazer.getWidth(), lazer.getHeight(), null);
        }
        if(bullet.getY() < -48){ // Nếu hình ảnh viên đạn vượt ra khỏi phần nhìn thấy của frame
            shoot = 0; // Không hiện hình ảnh viên đạn đó nữa (nhìn if bên trên). Cho phép bắn viên đạn mới nếu đã nhấn phím Space
        }
        for(int i=0; i<starNumber; i++){
            g.drawImage(starImg, star.get(i).getX(), star.get(i).getY(), star.get(i).getWidth(), star.get(i).getHeight(), null);
        }
        for(int i=0; i<enemyNumber; i++){
            if(enemy.get(i).getY() != HEIGHT + 100){    // Nếu enemy chưa chết thì mới vẽ, để đỡ tốn bộ nhớ
                if(enemy.get(i).getType().equals("Assault Spaceship")){
                    g.drawImage(enemyImg1, enemy.get(i).getX(), enemy.get(i).getY(), enemy.get(i).getWidth(), enemy.get(i).getHeight(), null);
                }
                else g.drawImage(enemyImg2, enemy.get(i).getX(), enemy.get(i).getY(), enemy.get(i).getWidth(), enemy.get(i).getHeight(), null);
            }
        }
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 30));
        g.drawString("SCORE " + playerScore, 5, 40);
        g.drawString("HEALTH " + playerHealth, 5, 85);
        if(gameOver){
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("GAME OVER", WIDTH/2 - 150, HEIGHT/2);
        }
        if(gameState == State.PAUSE){
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("PAUSE", WIDTH/2 - 100, HEIGHT/2);
        }
        //g.dispose(); // giải phóng tài nguyên mà đối tượng Graphics đang sử dụng 
    }
    
    public void update(){ // Hàm dùng để cập nhật vị trí của player
        if(gameState == State.PLAY){
            timeCounter++;
        }
        if(move == 1){
            player.setX(player.getX() + player.getVx());
        }
        else if(move == -1){
            player.setX(player.getX() - player.getVx());
        }
        if(shoot == 1){
            bullet.setY(bullet.getY() - bullet.getVy());
        }
        if(beam == 1){
            lazer.setX(player.getX());
        }
        else{
            lazer.setX(WIDTH);
            lazer.setY(HEIGHT + 1000);
        }
        if(playerHealth < 0){
            playerHealth = 0;
            gameOver = true;
            bgMusic.stop();
        }
        if(timeCounter % 50 == 0){
            starNumber++;
            Entity newStar = new Entity(2, 10, 10);
            newStar.setX(random.nextInt(WIDTH - 48) + 0);
            newStar.setY(-random.nextInt(48));
            star.add(newStar);
        }
        for(int i=0; i<starNumber; i++){
            star.get(i).setY(star.get(i).getY() + star.get(i).getVy());
        }
        if((timeCounter <= 3600 && timeCounter % 100 == 0) || (timeCounter > 3600 && timeCounter < 7200 && timeCounter % 80 == 0) || (timeCounter >= 7200 && timeCounter % 50 == 0)){
            enemyNumber++;
            Enemy newEnemy = new Enemy(1, 48, 48, 20, 20, 1, "Assault Spaceship");
            newEnemy.setX(random.nextInt(WIDTH - 48) + 0);
            newEnemy.setY(-random.nextInt(48));
            enemy.add(newEnemy);
        }
        if(timeCounter > 1800 && timeCounter % 800 == 0){
            enemyNumber++;
            Enemy newEnemy = new Enemy(1, 48, 48, 40, 40, 5, "UFO"); // Loại enemy này có maxHealth = 40
            newEnemy.setX(random.nextInt(WIDTH - 48) + 0);
            newEnemy.setY(-random.nextInt(48));
            enemy.add(newEnemy);
        }
        for(int i=0; i<enemyNumber; i++){
            enemy.get(i).setY(enemy.get(i).getY() + enemy.get(i).getVy());
            double distance1 = Math.sqrt(Math.pow(enemy.get(i).getX() - bullet.getX(), 2) + Math.pow(enemy.get(i).getY() - bullet.getY(), 2));
            double distance2 = Math.sqrt(Math.pow(enemy.get(i).getX() - player.getX(), 2) + Math.pow(enemy.get(i).getY() - player.getY(), 2));
            double distance3 = Math.sqrt(Math.pow(enemy.get(i).getX() - lazer.getX(), 2) + Math.pow(enemy.get(i).getY() - lazer.getY(), 2));
            if(distance1 < 48){ // Nếu đạn bắn trúng enemy -> enemy chết. Đáng lẽ phải xoá enemy khỏi ArrayList, nhưng làm vậy rất phức tạp. Nên ta làm như sau:
                enemy.get(i).setHealth(enemy.get(i).getHealth() - 20);
                if(enemy.get(i).getHealth() <= 0){ 
                    enemy.get(i).setX(WIDTH);   // Đưa tất cả enemy bị chết vào 1 điểm có toạ độ (WIDTH, HEIGHT + 2000) (gọi là điểm chết - địa ngục)
                    enemy.get(i).setY(HEIGHT + 2000);    
                    enemy.get(i).setVy(0);  // Cho vận tốc của enemy đó xuống 0, vì ng chết ko di chuyển. Để toạ độ enemy đó luôn luôn cố định ở điểm chết
                    playerScore += enemy.get(i).getPoint();
                }
                bullet.setX(WIDTH);
                bullet.setY(HEIGHT + 200); // Quan trọng: nếu ko thiết lập lại vị trí của bullet sau khi trúng địch, bullet sẽ luôn ở vị trí đó cho đến lần bấm Space để bắn tiếp theo
                // dẫn tới việc enemy đi đến vị trí đó sẽ chết do kích hoạt điều kiện if này dù màn hình ko hiển thị hình ảnh bullet do biến shoot = 0
                // Tương tự enemy, ta cũng thiết lập để sau khi trúng địch, bullet sẽ nằm tại 1 điểm (WIDTH, HEIGHT + 200) gọi là điểm đạn
                shoot = 0; 
            }
            if(distance2 < 48){ // Nếu enemy đâm vào player -> thì thôi, cho enemy tái sinh đi, mình chỉ cần enemy chết khi bắn trúng để không xuất hiện quá nhiều enemy thôi!
                playerHealth -= 10;
                if(enemy.get(i).getType().equals("Assault Spaceship")){ // Chỉ cho loại enemy này tái sinh, còn enemy kia thì mạnh hơn nên cho die luôn
                    enemy.get(i).setX(random.nextInt(WIDTH - 48) + 0);
                    enemy.get(i).setY(-random.nextInt(48));
                    enemy.get(i).setHealth(enemy.get(i).getMaxHealth());
                }
                else{
                    enemy.get(i).setX(WIDTH); // 3 dòng code này nghĩa là enemy die hẳn
                    enemy.get(i).setY(HEIGHT + 2000);
                    enemy.get(i).setVy(0);
                }
            }
            if(distance3 < 48){
                enemy.get(i).setHealth(enemy.get(i).getHealth() - 20);
                if(enemy.get(i).getHealth() <= 0){ 
                    enemy.get(i).setX(WIDTH);   
                    enemy.get(i).setY(HEIGHT + 2000);    
                    enemy.get(i).setVy(0);  
                    playerScore += enemy.get(i).getPoint();
                }
                // dẫn tới việc enemy đi đến vị trí đó sẽ chết do kích hoạt điều kiện if này dù màn hình ko hiển thị hình ảnh bullet do biến shoot = 0
                // Tương tự enemy, ta cũng thiết lập để sau khi trúng địch, bullet sẽ nằm tại 1 điểm (WIDTH, HEIGHT + 200) gọi là điểm đạn
            }
            if(enemy.get(i).getY() > HEIGHT + 48 && enemy.get(i).getY() < HEIGHT + 100){ // Thêm điều kiện sau vì khi enemy trúng đạn -> bị xuống điểm chết -> playerHealth bị trừ, sau đó enemy tái sinh
                playerHealth -= 5;
                if(enemy.get(i).getType().equals("Assault Spaceship")){
                    enemy.get(i).setX(random.nextInt(WIDTH - 48) + 0);
                    enemy.get(i).setY(-random.nextInt(48));
                    enemy.get(i).setHealth(enemy.get(i).getMaxHealth());
                }
                else{
                    enemy.get(i).setX(WIDTH);
                    enemy.get(i).setY(HEIGHT + 2000);
                    enemy.get(i).setVy(0);
                }
            }
        }
    }
    
    @Override
    public void run(){ // Hàm chứa đoạn lệnh mà thread sẽ thực thi
        long t1 = System.nanoTime(), t2; /* Hàm nanoTime() của lớp System: trả về khoảng thời gian (tính bằng ns) kể từ 1 mốc thời gian bất kỳ (không quan tâm). Nó chỉ dùng tính
        chênh lệch thời gian chứ ko biết thời gian hiện tại */
        double delta = 0; // khoảng thời gian delta, dùng để quyết định khi nào cập nhật và vẽ lại frame
        double ticks = 60; // số lần cập nhật trong 1 giây
        while(thread != null){
            t2 = System.nanoTime();
            delta += (t2 - t1) / 1e9 * ticks; // Chia 1e9 để đổi từ ns sang s
            t1 = t2; 
            if(delta >= 1){ // Nếu delta cộng dồn đến khi >= 1 thì cập nhật và vẽ lại frame
                if(!gameOver && gameState == State.PLAY){
                    update();
                }
                repaint();
                delta--; 
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) { // Hàm chạy khi một key đã được gõ
        
    }
    // KeyEvent là lớp sự kiện liên quan đến phím, được tạo ra khi nhập ký tự
    @Override
    public void keyPressed(KeyEvent e) { // Hàm chạy khi một key (phím) đã được nhấn
        switch(e.getKeyCode()){ // Hàm getKeyCode() trả về mã số ứng với phím được nhấn, kiểu int
            case KeyEvent.VK_LEFT: // Các hằng số của lớp KeyEvent, mỗi hằng số tương ứng với 1 phím được nhấn -> Nhấn phím mũi tên trái  thì di chuyển sang trái
                move = -1;
                break;
            case KeyEvent.VK_RIGHT:
                move = 1;
                break;
            case KeyEvent.VK_SPACE:
                if(shoot == 0 && gameState == State.PLAY){ // Nếu shoot = 0 thì khởi tạo lại vị trí viên đạn (bắn viên đạn mới). Có thêm đkien gameState == State.PLAY vì nút bắn trùng với nút tiếp tục
                    bullet.setX(player.getX());
                    bullet.setY(player.getY() + 20);
                    shoot = 1;
                }
                else if(gameState == State.PAUSE){
                    gameState = State.PLAY;
                    bgMusic.loop();
                }
                break;
            case KeyEvent.VK_ESCAPE:
                if(gameState == State.PLAY){
                    gameState = State.PAUSE;
                    bgMusic.stop();
                }
                break;
            case KeyEvent.VK_L:
                lazer.setX(player.getX());
                lazer.setY(player.getY() - 350);
                beam = 1;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { // Hàm chạy khi một key đã được nhả ra -> Không di chuyển hình ảnh của vật thể player
        switch(e.getKeyCode()){ 
            case KeyEvent.VK_LEFT:
                move = 0; 
                break;
            case KeyEvent.VK_RIGHT:
                move = 0;
                break;
            case KeyEvent.VK_L:
                beam = 0;
                break;
        }
    }
}
