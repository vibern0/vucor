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
        String input;
        String[] tokens;
        Scanner scanner = new Scanner(System.in);
        input = scanner.nextLine();
        
        tokens = input.split(" ");
        if(tokens[0].equals("add"))
        {
            model.addMusicToList(tokens[1]);
        }
        else if(tokens[0].equals("play"))
        {
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
