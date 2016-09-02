package dimusique;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;


public class Model
{
    private List<String> music_path_list;
    private Player player;
    private FileInputStream fis;
    private BufferedInputStream bis;
    private int orderMusic;
    private long pauseFrame;
    private long totalFrames;
    
    public Model()
    {
        music_path_list = new ArrayList<>();
        player = null;
        orderMusic = 0;
    }
    public boolean addMusicToList(String url)
    {
        File file = new File(url);
        
        if(!file.exists())
            return false;
        
        music_path_list.add(url);
        return true;
    }
    public void removeMusicFromList(String name)
    {
        
    }
    public boolean playMusic()
    {
        if(music_path_list.isEmpty())
            return false;
        
        try
        {
            fis = new FileInputStream(music_path_list.get(orderMusic));
            bis = new BufferedInputStream(fis);
            
            player = new Player(bis);
            totalFrames = fis.available();

            new Thread()
            {
                @Override
                public void run()
                {
                    try
                    {
                        player.play();
                    }
                    catch (JavaLayerException ex)
                    {
                        Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }.start();
        }
        catch (FileNotFoundException ex)
        {
        }
        catch (JavaLayerException | IOException ex)
        {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }
    public void stopMusic()
    {
        if(player != null)
        {
            player.close();
        }
    }
    public void pauseMusic()
    {
        if(player != null)
        {
            try
            {
                pauseFrame = fis.available();
                player.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public boolean resumeMusic()
    {
        if(player == null)
            return false;
        
        try
        {
            fis = new FileInputStream(music_path_list.get(orderMusic));
            bis = new BufferedInputStream(fis);
            
            player = new Player(bis);
            fis.skip(totalFrames - pauseFrame);

            new Thread()
            {
                @Override
                public void run()
                {
                    try
                    {
                        player.play();
                    }
                    catch (JavaLayerException ex)
                    {
                        Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }.start();
        }
        catch (FileNotFoundException ex)
        {
        }
        catch (JavaLayerException | IOException ex)
        {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }
}
