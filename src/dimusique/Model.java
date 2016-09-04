package dimusique;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;


public class Model
{
    private Playlist playlist;
    private Player player;
    private FileInputStream fis;
    private BufferedInputStream bis;
    private long pauseFrame;
    private long totalFrames;
    private boolean isPlaying;
    private Thread thread_music;
    
    public Model()
    {
        playlist = new Playlist();
        player = null;
        isPlaying = false;
    }
    public boolean addMusicToList(String url)
    {
        File file = new File(url);
        if(!file.exists())
        {
            return false;
        }
        
        playlist.add(url);
        return true;
    }
    public boolean removeMusicFromList(String name)
    {
        return playlist.remove(name);
    }
    public List<String> getMusicList()
    {
        return playlist.getNames();
    }
    public boolean playMusic()
    {
        if(getMusicList().isEmpty())
        {
            return false;
        }
        if(isPlaying == true)
        {
            return false;
        }
        
        try
        {
            fis = new FileInputStream(playlist.getCurrentPath());
            bis = new BufferedInputStream(fis);
            
            player = new Player(bis);
            totalFrames = fis.available();

            thread_music = new Thread()
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
            };
            thread_music.start();
            
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
            fis = new FileInputStream(playlist.getCurrentPath());
            bis = new BufferedInputStream(fis);
            
            player = new Player(bis);
            fis.skip(totalFrames - pauseFrame);

            thread_music = new Thread()
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
            };
            thread_music.start();
            
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
        playlist.next();
    }
    public void previousMusic()
    {
        playlist.previous();
    }
    public boolean isPlaying()
    {
        return isPlaying;
    }
    public boolean loadPlaylist()
    {
        try
        {
            File file_playlist = new File("/home/" + System.getProperty("user.name") + "/.dimusique.playlist");
            FileInputStream fin;
            fin = new FileInputStream(file_playlist);
            try (ObjectInputStream ois = new ObjectInputStream(fin))
            {
                playlist = (Playlist) ois.readObject();
                fin.close();
                return true;
            }
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException | ClassNotFoundException ex)
        {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    public boolean savePlaylist()
    {
        //
        String OS = System.getProperty("os.name").toLowerCase();
        if(OS.contains("nix") || OS.contains("nux") || OS.contains("aix") )
        {
            try
            {
                File file_playlist = new File("/home/" + System.getProperty("user.name") + "/.dimusique.playlist");
                
                FileOutputStream fout = new FileOutputStream(file_playlist);
                try (ObjectOutputStream oos = new ObjectOutputStream(fout))
                {
                    oos.writeObject(playlist);
                    oos.close();
                    return true;
                }
            }
            catch (IOException ex)
            {
                Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    public void finish()
    {
        thread_music.interrupt();
    }
}
