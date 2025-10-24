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
    private State bulletState = State.POWER;
    private boolean gameOver = false;
    private BufferedImage backgroundImg;
    private BufferedImage playerImg, starImg, bulletImg, blueBulletImg, randomImg, heartImg, thunderImg, enemyImg1, enemyImg2;
    private Player player;
    private Item[] item = new Item[5];
    private Bullet[] basicBullet = new Bullet[2];
    private Bullet[] doubleBullet = new Bullet[4];
    private Bullet[] powerBullet = new Bullet[3];
    private ArrayList<Entity> star = new ArrayList<>();
    private int starNumber = 3;
    private ArrayList<Enemy> enemy = new ArrayList<>();
    private int enemyNumber = 1;
    private Thread thread;
    private long timeCounter = 0; // Bộ đếm thời gian (tính bằng ticks)
    private int move = 0; // move = -1 là di chuyển sang trái, move = 1 là di chuyển sang phải, move = 0 là đứng yên
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
            ImageIO là lớp dùng để thao tác đọc ghi với file hình ảnh. Hàm ImageIO.read() để đọc InputStream đó và trả về kiểu BufferedImage */
            backgroundImg = ImageIO.read(getClass().getResourceAsStream("/starblaster/image/background2.jpg"));
            playerImg = ImageIO.read(getClass().getResourceAsStream("/starblaster/image/alpha.png"));
            starImg = ImageIO.read(getClass().getResourceAsStream("/starblaster/image/star.png"));
            bulletImg = ImageIO.read(getClass().getResourceAsStream("/starblaster/image/firebullet.png"));
            blueBulletImg = ImageIO.read(getClass().getResourceAsStream("/starblaster/image/blueFireBullet.png"));
            // Hàm getSubimage() dùng để cắt một phần ảnh từ ảnh gốc. Tham số: hoành độ bắt đầu cắt, tung độ bắt đầu cắt, độ dài muốn cắt chiều ngang, độ dài muốn cắt chiều dọc
            enemyImg1 = ImageIO.read(getClass().getResourceAsStream("/starblaster/image/assaultspaceship.png"));
            enemyImg2 = ImageIO.read(getClass().getResourceAsStream("/starblaster/image/ufo.png"));
            randomImg = ImageIO.read(getClass().getResourceAsStream("/starblaster/image/randomitem.png"));
            heartImg = ImageIO.read(getClass().getResourceAsStream("/starblaster/image/heart.png"));
            thunderImg = ImageIO.read(getClass().getResourceAsStream("/starblaster/image/thunder.png"));
        }
        catch(Exception e){ // biến e lưu thông tin của lỗi bắt được
            e.printStackTrace(); // In ra dấu vết của lỗi (vị trí lỗi,...)
        }
        // Khởi tạo dữ liệu cho player và bullet
        player = new Player(WIDTH/2 - 48/2, HEIGHT - 90, 5, 5, 48, 48, 100, 100, 0); // Ta để width và height bằng 48 dù trong ảnh gốc, mỗi vật thể chỉ là 16, 16 vì 16 thì nhỏ quá :)
        for(int i=0; i<2; i++){
            basicBullet[i] = new Bullet(WIDTH, HEIGHT + 200, 0, 5, 48, 48, 20, false);
        } // Ban đầu, khi chưa nhấn Space để bắn, bullet nằm tại điểm đạn (chứ mà ko thiết lập thì mặc định là (0, 0) thì có thể bị gần toạ độ enemy mới tạo -> playerScore tăng dù ko bắn
        for(int i=0; i<4; i++){
            doubleBullet[i] = new Bullet(WIDTH, HEIGHT + 200, 0, 5, 48, 48, 20, false);
        }
        for(int i=0; i<3; i++){
            powerBullet[i] = new Bullet(WIDTH, HEIGHT + 200, 0, 8, 48, 48, 40, false);
        }
        item[0] = new Item(2, 48, 48, State.RANDOM, randomImg, 1200, -1, false);
        item[1] = new Item(2, 48, 48, State.DOUBLE, randomImg, 1200, -1, false);
        item[2] = new Item(2, 48, 48, State.POWER, randomImg, 1200, -1, false);
        item[3] = new Item(2, 48, 48, State.RECOVER, heartImg, 1200, -1, false);
        item[4] = new Item(2, 48, 48, State.SPEEDUP, thunderImg, 1200, -1, false);
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
        switch(bulletState){
            case State.BASIC:
                for(int i=0; i<2; i++){
                    if(basicBullet[i].getActive()){
                        g.drawImage(bulletImg, basicBullet[i].getX(), basicBullet[i].getY(), basicBullet[i].getWidth(), basicBullet[i].getHeight(), null);
                    }
                }
                break;
            case State.DOUBLE:
                for(int i=0; i<4; i++){
                    if(doubleBullet[i].getActive()){
                        g.drawImage(bulletImg, doubleBullet[i].getX(), doubleBullet[i].getY(), doubleBullet[i].getWidth(), doubleBullet[i].getHeight(), null);
                    }
                }
                break;
            case State.POWER:
                for(int i=0; i<3; i++){
                    if(powerBullet[i].getActive()){
                        g.drawImage(blueBulletImg, powerBullet[i].getX(), powerBullet[i].getY(), powerBullet[i].getWidth(), powerBullet[i].getHeight(), null);
                    }
                }
                break;
        }
        for(int i=0; i<5; i++){
            if(item[i].getDrop()){
                g.drawImage(item[i].getImage(), item[i].getX(), item[i].getY(), item[i].getWidth(), item[i].getHeight(), null);
            }
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
        g.drawString("SCORE " + player.getScore(), 5, 40);
        g.drawString("HEALTH " + player.getHealth(), 5, 85);
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
        g.dispose(); // giải phóng tài nguyên mà đối tượng Graphics đang sử dụng 
    }
    
    public void update(){ // Hàm dùng để cập nhật thông số cho các vật thể ở mỗi frame mới (vị trí của player, enemy, sinh thêm enemy, xử lý va chạm vât thể,...)
        if(gameState == State.PLAY){ // Cập nhật biến đếm thời gian timeCounter
            timeCounter++;
        }
        if(player.getHealth() <= 0){ // Cập nhật trạng thái gameOver
            player.setHealth(0);
            gameOver = true;
            bgMusic.stop();
        }
        if(move == 1){ // Cập nhật di chuyển player 
            player.setX(player.getX() + player.getVx());
        }
        else if(move == -1){
            player.setX(player.getX() - player.getVx());
        }
        switch(bulletState){ // Cập nhật di chuyển của đạn
            case State.BASIC:
                for(int i=0; i<2; i++){
                    if(basicBullet[i].getActive()){
                        basicBullet[i].setY(basicBullet[i].getY() - basicBullet[i].getVy());
                        if(basicBullet[i].getY() < -48){
                            basicBullet[i].setActive(false);
                        }
                    }
                }
                break;
            case State.DOUBLE:
                for(int i=0; i<4; i++){
                    if(doubleBullet[i].getActive()){
                        doubleBullet[i].setY(doubleBullet[i].getY() - doubleBullet[i].getVy());
                        if(doubleBullet[i].getY() < -48){
                            doubleBullet[i].setActive(false);
                        }
                    }
                }
                break;
            case State.POWER:
                for(int i=0; i<3; i++){
                    if(powerBullet[i].getActive()){
                        powerBullet[i].setY(powerBullet[i].getY() - powerBullet[i].getVy());
                        if(powerBullet[i].getY() < -48){
                            powerBullet[i].setActive(false);
                        }
                    }
                }
                break;
        }
        for(int i=0; i<5; i++){
            if(item[i].getDrop()){
                item[i].setY(item[i].getY() + item[i].getVy());
                double distance = Math.sqrt(Math.pow(item[i].getX() - player.getX(), 2) + Math.pow(item[i].getY() - player.getY(), 2));
                if(distance < 48){
                    item[i].setEffectCounter(item[i].getEffectDuration());
                    item[i].setDrop(false);
                    item[i].setX(WIDTH);
                    item[i].setY(HEIGHT + 200);
                    switch(item[i].getType()){
                        case State.RANDOM:
                            int tmp = random.nextInt(2);
                            if(tmp == 0) bulletState = State.DOUBLE;
                            else bulletState = State.POWER;
                            break;
                        case State.DOUBLE, State.POWER:
                            bulletState = item[i].getType();
                            break;
                        case State.RECOVER:
                            player.setHealth(player.getMaxHealth());
                            break;
                        case State.SPEEDUP:
                            player.setVx(8);
                            break;
                    }     
                }
            }
        }
        
        for(int i=0; i<5; i++){
            if(item[i].getEffectCounter() > 0){
                item[i].setEffectCounter(item[i].getEffectCounter() - 1);
            }
            else if(i == 4){
                player.setVx(5);
            }
        }
        
        boolean checkEffect = false;
        for(int i=0; i<3; i++){
            if(item[i].getEffectCounter() > 0){
                checkEffect = true;
                // Vấn đề: khi ăn nhiều item khi item cũ chưa hết hiệu lực và sự kiểm tra item cũ
                /* Nếu bulletState khác State.BASIC thì mới gán lại = State.BASIC vì khi 1 item đã nhận sắp hết thời gian, ta ăn thêm 1 item khác thì nếu item cũ hết hiệu 
                lực thì trạng thái đạn sẽ trở về BASIC dù item mới chưa hết hiệu lực. Do đó, dùng thêm biến checkEffect để đảm bảo chỉ set bulletState = BASIC khi tất cả item đều hết hiệu lực */
            }
        }
        if(!checkEffect) bulletState = State.BASIC;
        
        if(timeCounter % 500 == 0){
            int tmp = random.nextInt(5);
            if(!item[tmp].getDrop()){
                item[tmp].setX(random.nextInt(WIDTH - 48) + 0);
                item[tmp].setY(-random.nextInt(48));
                item[tmp].setEffectCounter(item[tmp].getEffectDuration());
                item[tmp].setDrop(true);
            }
        }
        if(timeCounter % 50 == 0){ // Sinh các ngôi sao rơi xuống theo chu kỳ thời gian
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
            double distance2 = Math.sqrt(Math.pow(enemy.get(i).getX() - player.getX(), 2) + Math.pow(enemy.get(i).getY() - player.getY(), 2));
            for(int j=0; j<2; j++){
                basicBullet[j].onHit(enemy.get(i), player);
            }
            for(int j=0; j<4; j++){
                doubleBullet[j].onHit(enemy.get(i), player);
            }
            for(int j=0; j<3; j++){
                powerBullet[j].onHit(enemy.get(i), player);
            }
            if(distance2 < 48){ // Nếu enemy đâm vào player -> thì thôi, cho enemy tái sinh đi, mình chỉ cần enemy chết khi bắn trúng để không xuất hiện quá nhiều enemy thôi!
                player.setHealth(player.getHealth() - 10);
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
            if(enemy.get(i).getY() > HEIGHT + 48 && enemy.get(i).getY() < HEIGHT + 100){ // Thêm điều kiện sau vì khi enemy trúng đạn -> bị xuống điểm chết -> playerHealth bị trừ, sau đó enemy tái sinh
                player.setHealth(player.getHealth() - 5);
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
                if(gameState == State.PLAY){
                    switch(bulletState){
                        case State.BASIC:
                            for(int i=0; i<2; i++){
                                if(!basicBullet[i].getActive()){
                                    basicBullet[i].setX(player.getX());
                                    basicBullet[i].setY(player.getY() + 20);
                                    basicBullet[i].setActive(true);
                                    break;
                                }
                            }
                            break;
                        case State.DOUBLE:
                            for(int i=0; i<4; i+=2){
                                if(!doubleBullet[i].getActive() && !doubleBullet[i + 1].getActive()){
                                    doubleBullet[i].setX(player.getX() - 20);
                                    doubleBullet[i].setY(player.getY() + 20);
                                    doubleBullet[i].setActive(true);
                                    doubleBullet[i + 1].setX(player.getX() + 20);
                                    doubleBullet[i + 1].setY(player.getY() + 20);
                                    doubleBullet[i + 1].setActive(true);
                                    break;
                                }
                            }
                            break;
                        case State.POWER:
                            for(int i=0; i<3; i++){
                                if(!powerBullet[i].getActive()){
                                    powerBullet[i].setX(player.getX());
                                    powerBullet[i].setY(player.getY() + 20);
                                    powerBullet[i].setActive(true);
                                    break;
                                }
                            }
                            break;
                    }
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
        }
    }
}
