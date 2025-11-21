package starblaster;

import java.awt.image.BufferedImage;

public class Enemy extends Entity {
    private int health, maxHealth, power, point;
    private BufferedImage image;
    
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
}
