/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package obernardovieira.vucor.core.data;

import java.io.Serializable;
import java.util.ArrayList;
import obernardovieira.vucor.core.Playlist;

/**
 *
 * @author user
 */
public class DataModel implements Serializable
{
    private Integer on_playlist;
    private ArrayList<Playlist> playlists;
    
    public DataModel()
    {
        this.on_playlist = -1;
        this.playlists = new ArrayList<>();
    }
    public Integer getOnPlaylist() 
    {
        return on_playlist;
    }
    public void setOnPlaylist(Integer on_playlist) 
    {
        this.on_playlist = on_playlist;
    }
    public boolean addPlaylist(Playlist playlist)
    {
        return playlists.add(playlist);
    }
    public Playlist getCurrentPlaylist()
    {
        return playlists.get(on_playlist);
    }
    public ArrayList<Playlist> getPlaylists() 
    {
        return playlists;
    }
    public void setPlaylists(ArrayList<Playlist> playlists)
    {
        this.playlists = playlists;
    }
    public void setPlaylist(int index, Playlist playlist)
    {
        this.playlists.set(index, playlist);
    }
}
