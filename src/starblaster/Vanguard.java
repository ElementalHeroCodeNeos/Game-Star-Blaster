package starblaster;

import java.awt.image.BufferedImage;

public class Vanguard extends Commander {
    private int state = 1; // Trạng thái di chuyển của Vanguard
    
    public Vanguard(int vx, int vy, int width, int height, int health, int maxHealth, int power, int point, BufferedImage image, boolean isActive){
        super(vx, vy, width, height, health, maxHealth, power, point, image, isActive);
    }
    
    public Vanguard(int vx, int vy, int width, int height, int health, int maxHealth, int power, int point, BufferedImage image, boolean isActive, int maxBullets, int powerBullet, int type){
        super(vx, vy, width, height, health, maxHealth, power, point, image, isActive, maxBullets, powerBullet);
        this.type = type;
    }

    @Override
    public void move(long timeCounter, double playerX){
        if(state == 1){
            super.sideMove(WIDTH);
            if(this.direction == -1){ // Khi Vanguard chạm vào cạnh thẳng đứng (điều kiện dùng để xác định thời điểm chuyển state)
                state = 2;
                this.dx = this.x;
                this.dy = this.y;
            }
        }
        else if(state == 2){
            super.parabolMove();
            if(this.y > HEIGHT + this.height){
                state = 3;
                this.vx = 0;
                this.vy *= -1;
            }
        }
        else if(state == 3){
            super.simpleMove();
            if(this.y < HEIGHT / 3.5) state = 4;
        }
        else if(state == 4){
            super.sinMove(timeCounter);
            if(this.y < 0) this.vy *= -1;
            else if(this.y > HEIGHT / 3){
                state = 5;
                this.vx = 2;
            }
        }
        else{ // type = 0: Vanguard bên trái, type = 1: Vanguard bên phải
            if(type == 0) super.chasingMove(playerX);
            else{
                this.vy = 0;
                super.sinMove(timeCounter);
            }
        }
    }
}
    