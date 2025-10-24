package starblaster;

public class Bullet extends Entity{
    private int damage;
    private boolean isActive;
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    
    public Bullet(int vy, int width, int height, int damage, boolean isActive){
        super(vy, width, height);
        this.damage = damage;
        this.isActive = isActive;
    }
    
    public Bullet(int x, int y, int vx, int vy, int width, int height, int damage, boolean isActive){
        super(x, y, vx, vy, width, height);
        this.damage = damage;
        this.isActive = isActive;
    }
    
    public boolean getActive(){
        return this.isActive;
    }
    
    public void setActive(boolean isActive){
        this.isActive = isActive;
    }
    
    public void onHit(Enemy enemy, Player player){
        double distance = Math.sqrt(Math.pow(enemy.getX() - this.getX(), 2) + Math.pow(enemy.getY() - this.getY(), 2));
        if(distance < 48){ 
            enemy.setHealth(enemy.getHealth() - this.damage);
            if(enemy.getHealth() <= 0){ 
                enemy.setX(WIDTH);   
                enemy.setY(HEIGHT + 2000);    
                enemy.setVy(0);  
                player.setScore(player.getScore() + enemy.getPoint()); 
            }
            this.setX(WIDTH);
            this.setY(HEIGHT + 200); 
            this.setActive(false);
        }
    }  

}
