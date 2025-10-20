package starblaster;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
    private Clip clip;
    
    public Sound(String path){
        try{
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(path));
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void play(){
        if(clip != null){
            clip.setFramePosition(0);
            clip.start();
        }
    }
    
    public void loop(){
        if(clip != null){
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    
    public void stop(){
        if(clip != null){
            clip.stop();
        }
    }
    
}
