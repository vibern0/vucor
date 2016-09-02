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
    private boolean isPlaying;
    
    public Model()
    {
        music_path_list = new ArrayList<>();
        player = null;
        orderMusic = 0;
        isPlaying = false;
    }
    public boolean addMusicToList(String url)
    {
        File file = new File(url);
        if(!file.exists())
        {
            return false;
        }
        
        music_path_list.add(url);
        return true;
    }
    public void removeMusicFromList(String name)
    {
        
    }
    public List<String> getMusicList()
    {
        return music_path_list;
    }
    public boolean playMusic()
    {
        if(music_path_list.isEmpty())
        {
            return false;
        }
        if(isPlaying == true)
        {
            return false;
        }
        
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
            
            isPlaying = true;
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("playMusic() -> File not found!");
        }
        catch (JavaLayerException | IOException ex)
        {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }
    public void stopMusic()
    {
        if(player != null && isPlaying == true)
        {
            isPlaying = false;
            player.close();
        }
    }
    public void pauseMusic()
    {
        if(isPlaying == false)
        {
            return;
        }
        
        if(player != null)
        {
            try
            {
                isPlaying = false;
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
        {
            return false;
        }
        if(isPlaying == true)
        {
            return false;
        }
        
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
            
            isPlaying = true;
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("resumeMusic() -> File not found!");
        }
        catch (JavaLayerException | IOException ex)
        {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }
    public void nextMusic()
    {
        if(orderMusic == music_path_list.size() - 1)
        {
            orderMusic = 0;
        }
        else
        {
            orderMusic ++;
        }
    }
    public void previousMusic()
    {
        if(orderMusic == 0)
        {
            orderMusic = music_path_list.size() - 1;
        }
        else
        {
            orderMusic --;
        }
    }
    public boolean isPlaying()
    {
        return isPlaying;
    }
}
