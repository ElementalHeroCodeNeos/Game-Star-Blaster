package starblaster;

public class Entity {
    protected double x, y, vx, vy;
    protected int width, height; // Các tham số: hoành độ, tung độ, tốc độ dịch trái, tốc độ dịch phải, chiều rộng, chiều cao của vật thể
    // Các thuộc tính này sẽ dùng làm thông số để vẽ hình ảnh của vật thể lên frame (coi điểm góc trái phía trên của hình ảnh là điểm đại diện cho vật thể)
    // Gốc toạ độ của frame là điểm phía trên góc trái, chiều dương trục hoành từ trái sang phải, chiều dương trục tung từ trên xuống dưới
    private boolean isActive;
    public Entity(int width, int height){
        this.width = width;
        this.height = height;
    }
    
    public Entity(double vy, int width, int height){
        this.vy = vy;
        this.width = width;
        this.height = height;
    }
    
    public Entity(double x, double y, double vx, double vy, int width, int height){ 
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.width = width;
        this.height = height;
    }
    
    public Entity(int width, int height, boolean isActive){
        this.width = width;
        this.height = height;
        this.isActive = isActive;
    }
    
    public Entity(double vy, int width, int height, boolean isActive){
        this.vy = vy;
        this.width = width;
        this.height = height;
        this.isActive = isActive;
    }
    
    public Entity(double x, double y, double vx, double vy, int width, int height, boolean isActive){ 
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.width = width;
        this.height = height;
        this.isActive = isActive;
    }
    
    public double getX(){
        return this.x;
    }
    
    public void setX(double x){
        this.x = x;
    }
    
    public double getY(){
        return this.y;
    }
    
    public void setY(double y){
        this.y = y;
    }
    
    public double getVx(){
        return this.vx;
    }
    
    public void setVx(double vx){
        this.vx = vx;
    }
    
    public double getVy(){
        return this.vy;
    }
    
    public void setVy(double vy){
        this.vy = vy;
    }
    
    public int getWidth(){
        return this.width;
    }
    
    public void setWidth(int width){
        this.width = width;
    }
    
    public int getHeight(){
        return this.height;
    }
    
    public void setHeight(int height){
        this.height = height;
    }
    
    public boolean getActive(){
        return this.isActive;
    }
    
    public void setActive(boolean isActive){
        this.isActive = isActive;
    }
}
