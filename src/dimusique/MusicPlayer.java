/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dimusique;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

/**
 *
 * @author bernardovieira
 */
public class MusicPlayer extends Thread
{
    private AdvancedPlayer player;
    private FileInputStream fis;
    private int pausedOnFrame;
    
    public MusicPlayer(FileInputStream fis)
    {
        this.fis = fis;
        pausedOnFrame = 0;
    }
    
    public void play()
    {
        try
        {
            player = new AdvancedPlayer(fis);
            
            player.setPlayBackListener(new PlaybackListener()
            {
                @Override
                public void playbackFinished(PlaybackEvent event)
                {
                    pausedOnFrame = event.getFrame();
                }
                @Override
                public void playbackStarted(PlaybackEvent event)
                {
                    event.setFrame(pausedOnFrame);
                }
            });
            
            player.play();
        }
        catch (JavaLayerException ex)
        {
            System.out.println("Error when play MusicPlayer!");
        }
    }
    
    public void pause()
    {
        player.stop();
    }
    
    
    @Override
    public void run ()
    {
        play();
    }
}
