package starblaster;

import java.awt.image.BufferedImage;

public class Bullet extends Entity{
    private int damage;
    private BufferedImage image;
    private BulletMovement movement;
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    
    public Bullet(double vy, int width, int height, int damage, boolean isActive){
        super(vy, width, height, isActive);
        this.damage = damage;
    }
    
    public Bullet(double vx, double vy, int width, int height, int damage, BufferedImage image, boolean isActive){
        super(vy, width, height, isActive);
        this.vx = vx;
        this.damage = damage;
        this.image = image;
    }

    public int getDamage() {
        return damage;
    }

    public BufferedImage getImage() {
        return image;
    }
    
    public void setImage(BufferedImage image) {
        this.image = image;
    }
    

    public void setMovement(BulletMovement movement) {
        this.movement = movement;
    }
    
    public void onHit(Enemy enemy, Player player, Explosion[] exploList, double radius){
        double distance = Math.sqrt(Math.pow(enemy.getX() - this.x, 2) + Math.pow(enemy.getY() - this.y, 2));
        if(distance < radius){ 
            enemy.setHealth(enemy.getHealth() - this.damage);
            if(enemy.getHealth() <= 0){ 
                player.setScore(player.getScore() + enemy.getPoint()); 
                enemy.setActive(false);
                for(Explosion explosion : exploList){
                    if(!explosion.getActive()){
                        explosion.setX(enemy.getX());
                        explosion.setY(enemy.getY());
                        explosion.setDisplayTime(10);
                        explosion.setActive(true);
                        break;
                    }
                }
            }
            /*this.setX(WIDTH);
            this.setY(HEIGHT + 200);*/ 
            this.setActive(false);
        }
    }  
    
    public void move(long timeCounter, double playerX){
        movement.move(this, timeCounter, playerX);
    }
}
