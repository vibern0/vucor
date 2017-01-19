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
}
