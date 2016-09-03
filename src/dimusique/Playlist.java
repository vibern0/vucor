/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dimusique;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bernardovieira
 */
public class Playlist
{
    private final List<String> music_path_list;
    private int order;
    
    public Playlist()
    {
        music_path_list = new ArrayList<>();
        order = 0;
    }
    public boolean add(String path)
    {
        if(music_path_list.contains(path))
        {
            return false;
        }
        return music_path_list.add(path);
    }
    public boolean remove(String path)
    {
        return music_path_list.remove(path);
    }
    public void next()
    {
        if(order == music_path_list.size() - 1) order = 0;
        else order ++;
    }
    public void previous()
    {
        if(order == 0) order = music_path_list.size() - 1;
        else order --;
    }
    public String getCurrentPath()
    {
        return music_path_list.get(order);
    }
    public String getPath(int order_)
    {
        return music_path_list.get(order_);
    }
    public String getCurrentName()
    {
        Path p = Paths.get(music_path_list.get(order));
        return p.getFileName().toString();
    }
    public String getName(int order_)
    {
        Path p = Paths.get(music_path_list.get(order_));
        return p.getFileName().toString();
    }
    public List<String> getNames()
    {
        List<String> result = new ArrayList<>();
        Path p;
        for (String item : music_path_list)
        {
            p = Paths.get(item);
            result.add(p.getFileName().toString());
        }
        return result;
    }
}
