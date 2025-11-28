package starblaster;

import java.awt.image.BufferedImage;

public class Item extends Entity {
    private long effectDuration, effectCounter; // effectDuration là thời gian hiệu lực của Item, effectCounter là bộ đếm thời gian hiệu lực (để ktra Item còn hiệu lực không)
    private State type;
    private BufferedImage image;
    // isActive = true nghĩa là Item đang rơi
    
    public Item(int vy, int width, int height, State type, BufferedImage image, long effectDuration, long effectCounter, boolean isActive){
        super(vy, width, height, isActive);
        this.type = type;
        this.image = image;
        this.effectDuration = effectDuration;
        this.effectCounter = effectCounter;
    }
    
    public State getType(){
        return this.type;
    }
    
    public BufferedImage getImage(){
        return this.image;
    }
    
    public long getEffectDuration(){
        return this.effectDuration;
    }
    
    public long getEffectCounter(){
        return this.effectCounter;
    }
    
    public void setEffectCounter(long effectCounter){
        this.effectCounter = effectCounter;
    }
}
