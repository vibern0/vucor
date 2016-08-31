package dimusique;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javazoom.jl.decoder.JavaLayerException;


public class Model
{
    private List<File> music_list;
    private FileInputStream fis;
    private BufferedInputStream bis;
    private boolean isPlaying;
    private MusicPlayer mplayer;
    
    public Model()
    {
        music_list = new ArrayList<>();
        isPlaying = false;
    }
    public boolean addMusicToList(String url)
    {
        File file = new File(url);
        
        if(!file.exists())
            return false;
        
        music_list.add(file);
        return true;
    }
    public void removeMusicFromList(String name)
    {
        
    }
    public int playMusic()
    {
        if(music_list.isEmpty())
            return 1;
        
        try
        {
            fis = new FileInputStream(music_list.get(0));
            bis = new BufferedInputStream(fis);
            
            mplayer = new MusicPlayer(fis);
            mplayer.start();
            isPlaying = true;
        }
        catch (FileNotFoundException ex)
        {
            return 2;
        }
        
        return 0;
    }
    public void stopMusic()
    {
        
    }
    public void pauseMusic()
    {
        mplayer.pause();
    }
    public void resumeMusic()
    {
        mplayer.play();
    }
}
