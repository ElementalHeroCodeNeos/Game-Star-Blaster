package starblaster;

public class ChasingMovement implements BulletMovement{
    @Override
    public void move(Bullet bullet, long timeCounter, double playerX){
        bullet.y += bullet.vy;
        if(bullet.x < playerX) bullet.x += bullet.vx;
        else if(bullet.x > playerX) bullet.x -= bullet.vx;
    }
}
