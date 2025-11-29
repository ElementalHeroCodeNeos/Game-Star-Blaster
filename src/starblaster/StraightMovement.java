package starblaster;

public class StraightMovement implements BulletMovement{
    @Override
    public void move(Bullet bullet, long timeCounter, double playerX){
        bullet.y += bullet.vy;
    }
}
