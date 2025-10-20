package starblaster;

public class Enemy extends Entity {
    private int health, maxHealth, point;
    private String type;
    
    public Enemy(int vy, int width, int height, int health, int maxHealth, int point, String type){
        super(vy, width, height);
        this.health = health;
        this.maxHealth = maxHealth;
        this.point = point;
        this.type = type;
    }
    
    public Enemy(int x, int y, int vx, int vy, int width, int height, int health, int maxHealth, int point, String type){
        super(x, y, vx, vy, width, height);
        this.health = health;
        this.maxHealth = maxHealth;
        this.point = point;
        this.type = type;
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
    
    public int getPoint(){
        return this.point;
    }
    
    public String getType(){
        return this.type;
    } 
    
    public void setType(String type){
        this.type = type;
    }
}
