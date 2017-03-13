package obernardovieira.vucor.core;

import obernardovieira.vucor.core.data.DataModel;
import obernardovieira.vucor.core.data.DataFiles;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;


public class Model
{
    private final DataModel dataModel;
    private Player player;
    private FileInputStream fis;
    private BufferedInputStream bis;
    private long pauseFrame;
    private long totalFrames;
    private boolean isPlaying;
    private Thread thread_music;
    
    public Model() throws IOException,
            FileNotFoundException, ClassNotFoundException
    {
        dataModel = DataFiles.loadData();
        if(dataModel != null)
        {
            
        }
        player = null;
        isPlaying = false;
    }
    public DataModel getDataModel()
    {
        return dataModel;
    }
    public boolean addNewPlaylist(String name)
    {
        return dataModel.addPlaylist(new Playlist(name));
    }
    public boolean addNewPlaylist(Playlist playlist)
    {
        return dataModel.addPlaylist(playlist);
    }
    public boolean removePlaylist(String name)
    {
        ArrayList<Playlist> playlists =  dataModel.getPlaylists();
        Integer on_playlist = dataModel.getOnPlaylist();
        int p = 0;
        for(Playlist playlist : playlists)
        {
            if(playlist.getPlaylistName().equals(name))
            {
                if(p == on_playlist)
                {
                    dataModel.setOnPlaylist(-1);
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
        ArrayList<Playlist> playlists =  dataModel.getPlaylists();
        for(Playlist playlist : playlists)
        {
            if(playlist.getPlaylistName().equals(old_name))
            {
                playlist.rename(new_name);
                dataModel.setPlaylists(playlists);
                return true;
            }
        }
        return false;
    }
    public boolean changeToPlaylist(String name)
    {
        ArrayList<Playlist> playlists =  dataModel.getPlaylists();
        int p = 0;
        for(Playlist playlist : playlists)
        {
            if(playlist.getPlaylistName().equals(name))
            {
                dataModel.setOnPlaylist(p);
                return true;
            }
            p ++;
        }
        return false;
    }
    public void playMusic()
            throws FileNotFoundException, JavaLayerException, IOException
    {
        if( dataModel.getOnPlaylist() == -1 ||
            dataModel.getCurrentPlaylist().getNames().isEmpty())
        {
            throw new IOException();
        }
        if(isPlaying) return;
        
        fis = new FileInputStream(dataModel.getCurrentPlaylist().getCurrentPath());
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
            fis = new FileInputStream(dataModel.getCurrentPlaylist().getCurrentPath());
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
        if( dataModel.getOnPlaylist() == -1 ||
            dataModel.getCurrentPlaylist().getNames().isEmpty())
        {
            return;
        }
        stopMusic();
        dataModel.getCurrentPlaylist().next();
        if(isPlaying == true)
        {
            playMusic();
        }
    }
    public void previousMusic() throws JavaLayerException, IOException
    {
        if( dataModel.getOnPlaylist() == -1 ||
            dataModel.getCurrentPlaylist().getNames().isEmpty())
        {
            return;
        }
        stopMusic();
        dataModel.getCurrentPlaylist().previous();
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
