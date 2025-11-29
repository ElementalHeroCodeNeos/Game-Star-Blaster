package starblaster;

import java.awt.image.BufferedImage;

public class Boss extends Commander{
    private long preTimeCounter;
    private int state = 1;
    private BufferedImage bulletImg1, bulletImg2;
            
    public Boss(double vx, double vy, int width, int height, int health, int maxHealth, int power, int point, BufferedImage image, boolean isActive, int maxBullets, 
    int powerBullet, int widthBullet, int heightBullet, BufferedImage bulletImg1, BufferedImage bulletImg2) {
        super(vx, vy, width, height, health, maxHealth, power, point, image, isActive, maxBullets, powerBullet);
        this.maxBullets = maxBullets;
        this.bullets = new Bullet[maxBullets];
        this.bulletImg1 = bulletImg1;
        this.bulletImg2 = bulletImg2;
        for(int i=0; i<this.maxBullets; i++){
            this.bullets[i] = new Bullet(2, 3, 96, 96, powerBullet, bulletImg1, false);
        }
    }  
    
    @Override
    public void move(long timeCounter, double playerX){
        if(state == 1){
            super.simpleMove();
            if(this.y > this.height/2){
                preTimeCounter = timeCounter;
                state = 2;
            }
        }
        else if(state == 2){
            super.sideMoveBoss(WIDTH);
            if(timeCounter - preTimeCounter > 600){
                preTimeCounter = timeCounter;
                state = 3;
            }
        }
        else if(state == 3){
            super.sideMoveBoss(WIDTH);
            if(timeCounter - preTimeCounter > 600 && Math.abs(this.x - playerX) < this.width/2){
                this.vx = Math.abs(this.vx);
                this.vy += 3;
                state = 4;
            }
        }
        else if(state == 4){
            super.simpleMove();
            if(this.y > HEIGHT + this.height){
                this.x = WIDTH/2;
                this.vy -= 3;
                this.vy *= -1;
                state = 5;
            }
        }
        else if(state == 5){
            super.simpleMove();
            if(this.y < this.height/2){
                preTimeCounter = timeCounter;
                this.vy *= -1;
                state = 6;
            }
        }
        else if(state == 6){
            if(timeCounter - preTimeCounter > 600){
                preTimeCounter = timeCounter;
                state = 7;
            }
        }
        else if(state == 7){
            super.chasingMove(playerX);
            if(timeCounter - preTimeCounter > 600){
                this.vx += 1;
                state = 8;
            }
        }
        else super.chasingMove(playerX);
    }
    
    @Override
    public void shoot(long timeCounter, Player player, int frequent){
        if(state == 1){
            
        }
        else if(state == 2){
            for(Bullet bullet : bullets){
                if(!bullet.getActive() && timeCounter % frequent == 0){
                    bullet.setActive(true);
                    bullet.setX(this.x);
                    bullet.setY(this.y + this.height/2);
                    bullet.setMovement(new StraightMovement());
                    break;
                }
            }
        }
        else if(state == 3 || state == 4 || state == 5){
            for(int i=0; i<6; i+=2){
                if(!bullets[i].getActive() && !bullets[i + 1].getActive() && timeCounter % frequent == 0){
                    bullets[i].setActive(true);
                    bullets[i].setX(this.x - this.width/2.5);
                    bullets[i].setY(this.y + this.height/2);
                    bullets[i].setMovement(new StraightMovement());
                    
                    bullets[i + 1].setActive(true);
                    bullets[i + 1].setX(this.x + this.width/2.5);
                    bullets[i + 1].setY(this.y + this.height/2);
                    bullets[i + 1].setMovement(new StraightMovement());
                    break;
                }
            }
        }
        else if(state == 6){
            for(int i=0; i<6; i+=2){
                if(!bullets[i].getActive() && !bullets[i + 1].getActive() && timeCounter % frequent == 0){
                    bullets[i].setActive(true);
                    bullets[i].setX(this.x - this.width/2.5);
                    bullets[i].setY(this.y + this.height/2);
                    bullets[i].setImage(bulletImg2);       
                    bullets[i].setMovement(new ZigZagMovement());         
                    
                    bullets[i + 1].setActive(true);
                    bullets[i + 1].setX(this.x + this.width/2.5);
                    bullets[i + 1].setY(this.y + this.height/2);
                    bullets[i + 1].setImage(bulletImg2);
                    bullets[i + 1].setMovement(new ZigZagMovement());
                    break;
                }
            }
        }
        else if(state == 7){
            for(int i=0; i<2; i++){
                if(!bullets[i].getActive() && timeCounter % frequent == 0){
                    bullets[i].setActive(true);
                    bullets[i].setX(this.x);
                    bullets[i].setY(this.y + this.height/2);
                    bullets[i].setImage(bulletImg1);              
                    bullets[i].setMovement(new StraightMovement());
                    break;
                }
            }
            for(int i=2; i<6; i+=2){
                if(!bullets[i].getActive() && !bullets[i + 1].getActive() && timeCounter % frequent == 0){
                    bullets[i].setActive(true);
                    bullets[i].setX(this.x - this.width/2.5);
                    bullets[i].setY(this.y + this.height/2);  
                    bullets[i].setImage(bulletImg2);
                    bullets[i].setMovement(new ZigZagMovement());
                    
                    bullets[i + 1].setActive(true);
                    bullets[i + 1].setX(this.x + this.width/2.5);
                    bullets[i + 1].setY(this.y + this.height/2);
                    bullets[i].setImage(bulletImg2);
                    bullets[i + 1].setMovement(new ZigZagMovement());
                    break;
                }    
            }
        }
        else{
            for(int i=2; i<6; i+=2){
                if(!bullets[i].getActive() && !bullets[i + 1].getActive() && timeCounter % frequent == 0){
                    if(i == 2){
                        bullets[i].setMovement(new ChasingMovement());
                        bullets[i + 1].setMovement(new ChasingMovement());
                    }
                    else{
                        bullets[i].setMovement(new ZigZagMovement());
                        bullets[i + 1].setMovement(new ZigZagMovement());
                    }
                    bullets[i].setActive(true);
                    bullets[i].setX(this.x - this.width/2.5);
                    bullets[i].setY(this.y + this.height/2);  
                    bullets[i].setImage(bulletImg2);
                    
                    bullets[i + 1].setActive(true);
                    bullets[i + 1].setX(this.x + this.width/2.5);
                    bullets[i + 1].setY(this.y + this.height/2);
                    bullets[i].setImage(bulletImg2);
                    break;
                }    
            }   
        }
        
        for(Bullet bullet : bullets){
            if(bullet.getActive()){
                bullet.move(timeCounter, player.getX());
            }
        }
        // Cập nhật va chạm đạn Enemy với player
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
