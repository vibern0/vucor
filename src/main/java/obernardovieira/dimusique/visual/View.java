package obernardovieira.dimusique.visual;

import obernardovieira.dimusique.core.Playlist;
import obernardovieira.dimusique.core.Model;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
public class View
{
    private final Model model;
    private boolean finish;
    private boolean success;
    private String input;
    private String[] tokens;
    private Scanner scanner;
    private final ArrayList<String> cmd_params;
    private final HashMap<String, Integer> cmd_n_params;
    
    public View()
    {
        this.model = new Model();
        this.finish = false;
        this.cmd_params = new ArrayList<>();
        this.cmd_n_params = new HashMap<>();
        loadCommands();
    }
    public void waitForCommand()
    {
        scanner = new Scanner(System.in);
        input = scanner.nextLine();
        
        if(!checkParams(input))
        {
            System.out.println("Parameters missing!");
            return;
        }
        
        tokens = input.split(" ");
        switch (tokens[0]) {
            case "help":
                showHelp();
                break;
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
            case "list":
                for(String name : model.getMusicList()) System.out.println(name);
                break;
            case "next":
                if(model.isPlaying() == true) model.stopMusic();
                model.nextMusic();
                model.playMusic();
                break;
            case "previous":
                if(model.isPlaying() == true) model.stopMusic();
                model.previousMusic();
                model.playMusic();
                break;
            case "addpl":
                model.addNewPlaylist(tokens[1]);
                break;
            case "removepl":
                model.removePlaylist(tokens[1]);
                break;
            case "changepl":
                model.changeToPlaylist(tokens[1]);
                break;
            case "listpl":
                for(Playlist playlist : model.getPlaylists())
                    System.out.println(playlist.getPlaylistName());
                break;
            case "save":
                try
                {
                    model.savePlaylist();
                    System.out.println("Successfully saved!");
                }
                catch (IOException ex)
                {
                    System.out.println("Error! Not saved!");
                }
                break;
            case "load":
                try
                {
                    model.loadPlaylist();
                    System.out.println("Successfully saved!");
                }
                catch (IOException | ClassNotFoundException ex)
                {
                    System.out.println("Error! Not loaded!");
                }
                break;
            case "finish":
                finish = true;
                model.finish();
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
    private void loadCommands()
    {
        cmd_params.add("add <path>");
        cmd_params.add("remove <path>");
        cmd_params.add("play");
        cmd_params.add("pause");
        cmd_params.add("resume");
        cmd_params.add("stop");
        cmd_params.add("list");
        cmd_params.add("next");
        cmd_params.add("previous");
        cmd_params.add("addpl <playlist-name>");
        cmd_params.add("removepl <playlist-name>");
        cmd_params.add("changepl <playlist-name>");
        cmd_params.add("listpl");
        cmd_params.add("save");
        cmd_params.add("load");
        cmd_params.add("finnish");
        //
        cmd_n_params.put("add",         1);
        cmd_n_params.put("remove",      1);
        cmd_n_params.put("addpl",       1);
        cmd_n_params.put("removepl",    1);
        cmd_n_params.put("changepl",    1);
    }
    private boolean checkParams(String command)
    {
        String [] peaces = command.split(" ");
        if(cmd_n_params.containsKey(peaces[0]))
        {
            return (cmd_n_params.get(peaces[0]) == peaces.length);
        }
        return true;
    }
    private void showHelp()
    {
        System.out.println("Commands available:");
        cmd_params.forEach((cmd) -> {
            System.out.println(cmd);
        });
    }
}
