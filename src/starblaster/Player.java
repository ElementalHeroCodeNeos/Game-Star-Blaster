package starblaster;

public class Player extends Entity{
    private int health, maxHealth, score;
    
    public Player(int x, int y, int vx, int vy, int width, int height, int health, int maxHealth, int score){
        super(x, y, vx, vy, width, height);
        this.health = health;
        this.maxHealth = maxHealth;
        this.score = score;
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
    
    public int getScore(){
        return this.score;
    }
    
    public void setScore(int score){
        this.score = score;
    }
}
