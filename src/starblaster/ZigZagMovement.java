package starblaster;

import static starblaster.Enemy.WIDTH;

public class ZigZagMovement implements BulletMovement{
    @Override
    public void move(Bullet bullet, long timeCounter, double playerX){
        double freq = 0.015; // Tần số dao động -> tần số càng lớn, dao động càng nhanh (mạnh).
        double amplitude = 3.5; // Biên độ dao động ngang (độ lệch tối đa so với vị trí cân bằng). 
        bullet.x += Math.sin(timeCounter * freq) * amplitude;
        if(bullet.x < 0) bullet.x = 0;
        if(bullet.x > WIDTH - bullet.width) bullet.x = WIDTH - bullet.width;
        bullet.y += bullet.vy;
    }
}
