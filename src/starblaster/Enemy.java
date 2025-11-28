package starblaster;

import java.awt.image.BufferedImage;

public class Enemy extends Entity {
    private int health, maxHealth, power, point;
    private BufferedImage image;
    private double dx, dy;
    protected Bullet[] bullets;
    protected int maxBullets;
    
    private static final int WIDTH = 800;
    protected static final int HEIGHT = 600;
    
    public Enemy(int vy, int width, int height, int health, int maxHealth, int power, int point, BufferedImage image, boolean isActive){
        super(vy, width, height, isActive);
        this.health = health;
        this.maxHealth = maxHealth;
        this.power = power;
        this.point = point;
        this.image = image;
    }
    
    public Enemy(int x, int y, int vx, int vy, int width, int height, int health, int maxHealth, int power, int point, BufferedImage image, boolean isActive){
        super(x, y, vx, vy, width, height, isActive);
        this.health = health;
        this.maxHealth = maxHealth;
        this.power = power;
        this.point = point;
        this.image = image;
    }
    
    public int getHealth(){
        return this.health;
    }
    
    public void setHealth(int health){
        this.health = health;
    }
    
    public int getMaxHealth(){
        return this.maxHealth;
    }
    
    public void setMaxHealth(int maxHealth){
        this.maxHealth = maxHealth;
    }
    
    public int getPower(){
        return this.power;
    }
    
    public int getPoint(){
        return this.point;
    }
    
    public BufferedImage getImage(){
        return this.image;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public Bullet[] getBullets() {
        return bullets;
    }

    public void move(long timeCounter, double playerX, double playerY){
        this.y += vy;
    }
    
    public void sinMove(long timeCounter, int frameWidth, int frameHeight, double playerX, double playerY){
        double freq = 0.02; // Tần số dao động -> tần số càng lớn, dao động càng nhanh (mạnh).
        double amplitude = 2.5; // Biên độ dao động ngang (độ lệch tối đa so với vị trí cân bằng). 
        this.x += (int)(Math.sin(timeCounter * freq) * amplitude);
        if(this.x < 0) this.x = 0;
        if(this.x > frameWidth - this.width) this.x = frameWidth - this.width;
        this.y += this.vy;
    }
    
    public void parabolMove(long timeCounter, double playerX, double playerY){
        double a = 0.00035; 
        this.x += this.vx; // Ban đầu, dx = dy = 0 nên parabol có đỉnh O(0,0)
        if(this.x < 0){
            this.x = 0;
            this.vx *= -1; // Để enemy di chuyển theo chiều ngược lại (xét theo chiều ngang)
            dx = this.x;
            dy = this.y;
        }
        if(this.x > WIDTH - this.width){ // Khi enemy chạm cạnh phải frame thì nó sẽ di chuyển theo 1 parabol mới có đỉnh I(xi,yi). Trong đó, xi = xo + dx, yi = yo + dy
            this.x = WIDTH - this.width;
            this.vx *= -1;
            dx = this.x;
            dy = this.y;
        }
        this.y = (int)(a * (this.x - dx) * (this.x - dx) + dy);
        if(this.y < 0) this.y = 0;
    }
    
    public void chasingMove(long timeCounter, double playerX, double playerY){
        if(this.x < playerX) this.x += this.vx;
        else if(this.x > playerX) this.x -= this.vx;
        this.y += this.vy;
    }
    
    public void shoot(long timeCounter, Player player){
        
    }
}
