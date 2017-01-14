package obernardovieira.dimusique.core;

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
        int p = 0;
        for(Playlist playlist : playlists)
        {
            if(playlist.getPlaylistName().equals(name))
            {
                if(p == playlist_id)
                {
                    playlist_id = -1;
                }
                playlists.remove(playlist);
                return true;
            }
            p ++;
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
    public Playlist getLastPlaylist()
    {
        return playlists.get(playlists.size() - 1);
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
    public void playMusic()
            throws FileNotFoundException, JavaLayerException, IOException
    {
        if( playlist_id == -1 ||
            playlists.get(playlist_id).getNames().isEmpty() ||
            isPlaying)
        {
            throw new IOException();
        }
        
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
                catch (JavaLayerException | IOException ex) { }
            }
        };
        thread_music.start();
        isPlaying = true;
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
                    catch (JavaLayerException | IOException ex) { }
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
    public void nextMusic() throws JavaLayerException, IOException
    {
        stopMusic();
        playlists.get(playlist_id).next();
        if(isPlaying == true)
        {
            playMusic();
        }
    }
    public void previousMusic() throws JavaLayerException, IOException
    {
        stopMusic();
        playlists.get(playlist_id).previous();
        if(isPlaying == true)
        {
            playMusic();
        }
    }
    public void autoNextMusic() throws JavaLayerException, IOException
    {
        nextMusic();
        playMusic();
    }
    public boolean isPlaying()
    {
        return isPlaying;
    }
    @SuppressWarnings("unchecked")
    public void loadPlaylist() throws FileNotFoundException, IOException,
            ClassNotFoundException
    {
        File file_playlist = new File(
                System.getProperty("user.home") + "/.dimusique.playlist");
        FileInputStream fin;
        fin = new FileInputStream(file_playlist);
        try (ObjectInputStream ois = new ObjectInputStream(fin))
        {
            playlist_id = 0;
            playlists = (List<Playlist>) ois.readObject();
            fin.close();
        }
    }
    public void savePlaylist() throws FileNotFoundException, IOException
    {
        //
        File file_playlist = new File(
                System.getProperty("user.home") + "/.dimusique.playlist");
        FileOutputStream fout = new FileOutputStream(file_playlist);
        try (ObjectOutputStream oos = new ObjectOutputStream(fout))
        {
            oos.writeObject(playlists);
            oos.close();
        }
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
