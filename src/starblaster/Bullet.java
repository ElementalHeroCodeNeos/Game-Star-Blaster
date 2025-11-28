package starblaster;

public class Bullet extends Entity{
    private int damage;
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    
    public Bullet(int vy, int width, int height, int damage, boolean isActive){
        super(vy, width, height, isActive);
        this.damage = damage;
    }
    
    public Bullet(int x, int y, int vx, int vy, int width, int height, int damage, boolean isActive){
        super(x, y, vx, vy, width, height, isActive);
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }
    
    public void onHit(Enemy enemy, Player player, Explosion[] exploList){
        double distance = Math.sqrt(Math.pow(enemy.getX() - this.x, 2) + Math.pow(enemy.getY() - this.y, 2));
        if(distance < 48){ 
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
}
