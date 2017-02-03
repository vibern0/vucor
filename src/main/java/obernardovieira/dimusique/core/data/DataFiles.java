/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package obernardovieira.dimusique.core.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 *
 * @author user
 */
public class DataFiles
{ 
    @SuppressWarnings("unchecked")
    public static DataModel loadData() throws FileNotFoundException,
            IOException, ClassNotFoundException
    {
        File file_playlist = new File(
                System.getProperty("user.home") + "/.dimusique.playlist");
        if(file_playlist.exists())
        {
            DataModel dataModel;
            FileInputStream fin;
            fin = new FileInputStream(file_playlist);
            try (ObjectInputStream ois = new ObjectInputStream(fin))
            {
                dataModel = (DataModel) ois.readObject();
                fin.close();
            }
            return dataModel;
        }
        else
        {
            return new DataModel();
        }
    }
    
    public static void saveData(DataModel data)
            throws FileNotFoundException, IOException
    {
        File file_playlist = new File(
                System.getProperty("user.home") + "/.dimusique.playlist");
        FileOutputStream fout = new FileOutputStream(file_playlist);
        try (ObjectOutputStream oos = new ObjectOutputStream(fout))
        {
            oos.writeObject(data);
            oos.close();
        }
    }
    
    public static String selectMusicFromDisk()
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));

        chooser.setFileFilter(new javax.swing.filechooser.FileFilter()
        {
            @Override
            public boolean accept(File f)
            {
                return f.getName().toLowerCase().endsWith(".mp3")
                    || f.isDirectory();
            }

            @Override
            public String getDescription()
            {
                return "MP3 files";
            }
        });

        int r = chooser.showOpenDialog(new JFrame());
        if (r == JFileChooser.APPROVE_OPTION)
        {
            String name = chooser.getSelectedFile().getAbsolutePath();
            String format = name.substring(name.lastIndexOf("."));
            if(format.equals(".mp3") || format.equals(".MP3"))
            {
                return name;
            }
        }
        return null;
    }
}
