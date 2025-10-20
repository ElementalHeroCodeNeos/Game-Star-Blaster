package starblaster;

public class Entity {
    private int x, y, vx, vy, width, height; // Các tham số: hoành độ, tung độ, tốc độ dịch trái, tốc độ dịch phải, chiều rộng, chiều cao của vật thể
    // Các thuộc tính này sẽ dùng làm thông số để vẽ hình ảnh của vật thể lên frame (coi điểm góc trái phía trên của hình ảnh là điểm đại diện cho vật thể)
    // Gốc toạ độ của frame là điểm phía trên góc trái, chiều dương trục hoành từ trái sang phải, chiều dương trục tung từ trên xuống dưới
  
    public Entity(int vy, int width, int height){
        this.vy = vy;
        this.width = width;
        this.height = height;
    }
    
    public Entity(int x, int y, int vx, int vy, int width, int height){ 
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.width = width;
        this.height = height;
    }
    
    public int getX(){
        return this.x;
    }
    
    public void setX(int x){
        this.x = x;
    }
    
    public int getY(){
        return this.y;
    }
    
    public void setY(int y){
        this.y = y;
    }
    
    public int getVx(){
        return this.vx;
    }
    
    public int getVy(){
        return this.vy;
    }
    
    public void setVy(int vy){
        this.vy = vy;
    }
    
    public int getWidth(){
        return this.width;
    }
    
    public int getHeight(){
        return this.height;
    }

}
