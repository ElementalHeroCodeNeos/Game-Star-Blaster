package starblaster;

import java.awt.image.BufferedImage;

public class Commander extends Enemy {
    public Commander(int vx, int vy, int width, int height, int health, int maxHealth, int power, int point, BufferedImage image, boolean isActive){
        super(vy, width, height, health, maxHealth, power, point, image, isActive);
        this.vx = vx;
    }
    
    public Commander(int vx, int vy, int width, int height, int health, int maxHealth, int power, int point, BufferedImage image, boolean isActive, int maxBullets, int powerBullet){
        super(vy, width, height, health, maxHealth, power, point, image, isActive);
        this.vx = vx;
        this.maxBullets = maxBullets;
        this.bullets = new Bullet[maxBullets];
        for(int i=0; i<this.maxBullets; i++){
            this.bullets[i] = new Bullet(this.x, this.y, 0, 3, 48, 48, powerBullet, false);
        }
    }

    @Override
    public void move(long timeCounter, double playerX, double playerY){
        super.parabolMove(timeCounter, playerX, playerY);
    }
    
    @Override
    public void shoot(long timeCounter, Player player, int frequent){
        for(Bullet bullet : bullets){
            if(!bullet.getActive() && timeCounter % frequent == 0){
                bullet.setActive(true);
                bullet.setX(this.x);
                bullet.setY(this.y);
                break;
            }
        }
        for(Bullet bullet : bullets){
            if(bullet.getActive()){
                bullet.setY(bullet.getY() + bullet.getVy());
            }
        }
        for(Bullet bullet : bullets){
            if(bullet.getActive()){
                double distance = Math.sqrt(Math.pow(bullet.getX() - player.x, 2) + Math.pow(bullet.getY() - player.y, 2));
                if(distance < 48){
                    player.setHealth(player.getHealth() - bullet.getDamage());
                    bullet.setActive(false);
                }
                if(bullet.getY() > HEIGHT + 48){
                    bullet.setActive(false);
                }
            }
        }
    }
}
