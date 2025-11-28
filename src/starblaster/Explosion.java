package starblaster;

public class Explosion extends Entity {
    private int displayTime;
    
    public Explosion(int width, int height, boolean isActive){
        super(width, height, isActive);
    }
    
    public int getDisplayTime(){
        return this.displayTime;
    }
    
    public void setDisplayTime(int displayTime){
        this.displayTime = displayTime;
    }
}
