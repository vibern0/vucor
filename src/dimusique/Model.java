package dimusique;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;


public class Model
{
    private List<Playlist> playlists;
    private int playlist_id;
    private Player player;
    private FileInputStream fis;
    private BufferedInputStream bis;
    private long pauseFrame;
    private long totalFrames;
    private boolean isPlaying;
    private Thread thread_music;
    
    public Model()
    {
        playlist_id = -1;
        playlists = new ArrayList<>();
        player = null;
        isPlaying = false;
    }
    public boolean addNewPlaylist(String name)
    {
        playlist_id ++;
        return playlists.add(new Playlist(name));
    }
    public boolean removePlaylist(String name)
    {
        for(Playlist playlist : playlists)
        {
            if(playlist.getPlaylistName().equals(name))
            {
                playlists.remove(playlist);
                return true;
            }
        }
        return false;
    }
    public boolean renamePlaylist(String old_name, String new_name)
    {
        for(Playlist playlist : playlists)
        {
            if(playlist.getPlaylistName().equals(old_name))
            {
                playlist.rename(new_name);
                return true;
            }
        }
        return false;
    }
    public Playlist getPlaylist()
    {
        return playlists.get(playlist_id);
    }
    public List<Playlist> getPlaylists()
    {
        return playlists;
    }
    public boolean changeToPlaylist(String name)
    {
        int p = 0;
        for(Playlist playlist : playlists)
        {
            if(playlist.getPlaylistName().equals(name))
            {
                playlist_id = p;
                return true;
            }
            p ++;
        }
        return false;
    }
    public boolean addMusicToList(String url)
    {
        File file = new File(url);
        if(!file.exists())
        {
            return false;
        }
        if(playlists.isEmpty())
        {
            return false;
        }
        playlists.get(playlist_id).add(url);
        return true;
    }
    public boolean removeMusicFromList(String name)
    {
        return playlists.get(playlist_id).remove(name);
    }
    public List<String> getMusicList()
    {
        return playlists.get(playlist_id).getNames();
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
            fis = new FileInputStream(playlists.get(playlist_id).getCurrentPath());
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
                        if(isPlaying == true)
                        {
                            autoNextMusic();
                        }
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
            thread_music.interrupt();
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
                thread_music.interrupt();
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
            fis = new FileInputStream(playlists.get(playlist_id).getCurrentPath());
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
                        if(isPlaying == true)
                        {
                            autoNextMusic();
                        }
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
        stopMusic();
        playlists.get(playlist_id).next();
        if(isPlaying == true)
        {
            playMusic();
        }
    }
    public void previousMusic()
    {
        stopMusic();
        playlists.get(playlist_id).previous();
        if(isPlaying == true)
        {
            playMusic();
        }
    }
    public void autoNextMusic()
    {
        nextMusic();
        playMusic();
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
                playlist_id = 0;
                playlists = (List<Playlist>) ois.readObject();
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
                    oos.writeObject(playlists);
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
        if(player != null)
        {
            isPlaying = false;
            player.close();
        }
        thread_music.interrupt();
    }
}
