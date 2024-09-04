import java.util.Scanner;

/**
 * FrontendInterface - CS400 Project 1: iSongify
 */
public interface FrontendInterface {

    //public FrontendInterface(Scanner in, BackendInterface backend)

    /**
     * Repeated gives the user an opportunity to issue new commands until
     * they select Q to quit.
     */
    public void runCommandLoop();
    
    /**
     * Displays the menu of command options to the user.
     */
    public void displayMainMenu();

    /**
     * Provides text-based user interface and error handling for the 
     * [R]ead Data command.
     */
    public void readFile();
    
    /**
     * Provides text-based user interface and error handling for the 
     * [G]et Songs by Liveness command.
     */
    public void getValues();

    /**
     * Provides text-based user interface and error handling for the 
     * [F]ilter New Songs (by Min Year) command.
     */
    public void setFilter();

    /**
     * Provides text-based user interface and error handling for the 
     * [D]isplay Five Loudest command.
     */
    public void topFive();

}
