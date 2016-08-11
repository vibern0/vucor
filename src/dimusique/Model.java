package dimusique;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class Model
{
    private List<File> music_list;
    private FileInputStream fis;
    private BufferedInputStream bis;
    private boolean isPlaying;
    private Player player;
    
    public Model()
    {
        music_list = new ArrayList<>();
        isPlaying = false;
    }
    public boolean addMusicToList(String url)
    {
        File file = new File(url);
        FileInputStream tfis;
        BufferedInputStream tbis;
    
        try
        {
            tfis = new FileInputStream(file);
            tbis = new BufferedInputStream(tfis);
            if(music_list.isEmpty())
            {
                fis = tfis;
                bis = tbis;
            }
            music_list.add(file);
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
            
        return true;
    }
    public void removeMusicFromList(String name)
    {
        
    }
    public void playMusic()
    {
        try
        {
            player = new Player(bis);
            player.play();
            isPlaying = false;
        }
        catch (JavaLayerException ex)
        {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void stopMusic()
    {
        
    }
    public void pauseMusic()
    {
        
    }
    public void resumeMusic()
    {
        
    }
}
