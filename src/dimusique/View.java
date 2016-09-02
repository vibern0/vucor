package dimusique;

import java.util.Scanner;

public class View
{
    private Model model;
    public View()
    {
        model = new Model();
    }
    public void waitForCommand()
    {
        boolean success;
        String input;
        String[] tokens;
        Scanner scanner = new Scanner(System.in);
        input = scanner.nextLine();
        
        tokens = input.split(" ");
        if(tokens[0].equals("add"))
        {
            success = model.addMusicToList(tokens[1]);
            if(success)
                System.out.println("Successfully added!");
            else
                System.out.println("Error! Not added!");
        }
        else if(tokens[0].equals("play"))
        {
            success = model.playMusic();
            
            if(success == false)
                System.out.println("Playlist is empty!");
        }
        else if(tokens[0].equals("pause"))
        {
            model.pauseMusic();
        }
        else if(tokens[0].equals("resume"))
        {
            model.resumeMusic();
        }
        else if(tokens[0].equals("stop"))
        {
            model.stopMusic();
        }
        else if(tokens[0].equals("next"))
        {
            if(model.isPlaying() == true)
            {
                model.stopMusic();
            }
            model.nextMusic();
            model.playMusic();
        }
        else if(tokens[0].equals("previous"))
        {
            if(model.isPlaying() == true)
            {
                model.stopMusic();
            }
            model.previousMusic();
            model.playMusic();
        }
    }
    public void run()
    {
        int x = 1;
        while(x == 1)
        {
            System.out.println("Command: ");
            waitForCommand();
        }
    }
}
