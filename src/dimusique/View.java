package dimusique;

import java.util.Scanner;

public class View
{
    private final Model model;
    private boolean finish;
    private boolean success;
    private String input;
    private String[] tokens;
    private Scanner scanner;
    
    public View()
    {
        model = new Model();
        finish = false;
    }
    public void waitForCommand()
    {
        scanner = new Scanner(System.in);
        input = scanner.nextLine();
        
        tokens = input.split(" ");
        switch (tokens[0]) {
            case "add":
                success = model.addMusicToList(tokens[1]);
                if(success) System.out.println("Successfully added!");
                else System.out.println("Error! Not added!");
                break;
            case "remove":
                success = model.removeMusicFromList(tokens[1]);
                if(success) System.out.println("Successfully removed!");
                else System.out.println("Error! Not removed!");
                break;
            case "play":
                success = model.playMusic();
                if(success == false) System.out.println("Playlist is empty!");
                break;
            case "pause":
                model.pauseMusic();
                break;
            case "resume":
                model.resumeMusic();
                break;
            case "stop":
                model.stopMusic();
                break;
            case "next":
                if(model.isPlaying() == true)
                {
                    model.stopMusic();
                }   model.nextMusic();
                model.playMusic();
                break;
            case "previous":
                if(model.isPlaying() == true)
                {
                    model.stopMusic();
                }   model.previousMusic();
                model.playMusic();
                break;
            case "save":
                success = model.savePlaylist();
                if(success) System.out.println("Successfully saved!");
                else System.out.println("Error! Not saved!");
                break;
            case "load":
                success = model.loadPlaylist();
                if(success) System.out.println("Successfully loaded!");
                else System.out.println("Error! Not loaded!");
                break;
            case "finish":
                finish = true;
                break;
            default:
                break;
        }
    }
    public void run()
    {
        while(finish == false)
        {
            System.out.println("Command: ");
            waitForCommand();
        }
    }
}
