import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Frontend implements FrontendInterface {
    private Scanner in;
    private BackendInterface backend;
    private int min = -1;
    private int max = -1;
    private int year = -1;

    public Frontend(Scanner in, BackendInterface backend) {
        this.in = in;
        this.backend = backend;
    }

    @Override
    public void runCommandLoop() {
        String input;
        do {
            displayMainMenu();
            input = in.nextLine();
            System.out.println(" Inputted: " + input);

            if (input.equalsIgnoreCase("r"))
                readFile();
            else if (input.equalsIgnoreCase("g"))
                getValues();
            else if (input.equalsIgnoreCase("f"))
                setFilter();
            else if (input.equalsIgnoreCase("d"))
                topFive();
            else if (!input.equalsIgnoreCase("q"))
                System.out.println("Invalid input");
        } while (!input.equalsIgnoreCase("q"));
    }

    @Override
    public void displayMainMenu() {
        String menu = """
                	    
                ~~~ Command Menu ~~~
                    [R]ead Data
                    [G]et Songs by Liveness [min - max]
                    [F]ilter New Songs (by Min Year: none)
                    [D]isplay Five Loudest
                    [Q]uit
                Choose command:""";
        if (min != -1)
            menu = menu.replace("min", Integer.toString(min));
        if (max != -1)
            menu = menu.replace("max", Integer.toString(max));
        if (year != -1)
            menu = menu.replace("none", Integer.toString(year));
        System.out.print(menu + " ");
    }

    @Override
    public void readFile() {
        System.out.print("Enter path to csv file to load: ");
        String target = in.nextLine();
        System.out.println(" Inputted: " + target);

        try {
            backend.readData(target);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void getValues() {
        System.out.print("Enter range of values (MIN-MAX): ");
        String input = in.nextLine();
        System.out.println(" Inputted: " + input);

        // Parses the input into two integers
        String[] split = input.split("-");
        if (split.length != 2) {
            System.out.println("Invalid input: output using format MIN-MAX, where MIN and MAX are integers");
            return;
        }

        int bufferMin;  // Created so that if only min is valid and not max, the code will not change min
        try {
            bufferMin = Integer.parseInt(split[0]);
            max = Integer.parseInt(split[1]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input: output using format MIN-MAX, where MIN and MAX are integers");
            return;
        }
        min = bufferMin;

        // Outputs results of backend.getRange()
        List<String> songsInRange = backend.getRange(min, max);
        String output = songsInRange.size() + " songs found between " + min + " and " + max;
        if (year != -1)
            output += " from " + year + (year == 2019 ? ":" : " to 2019:");
        else
            output += ":";
        System.out.println(output);
        for (String each : songsInRange)
            System.out.println(each);
    }

    @Override
    public void setFilter() {
        System.out.print("Enter minimum year: ");
        String input = in.nextLine();
        System.out.println(" Inputted: " + input);

        // Parses input
        int bufferYear; // Created so that if the inputted year is not in the valid year range, year will not change
        try {
            bufferYear = Integer.parseInt(input);
            if (bufferYear < 2010 || bufferYear > 2019)
                throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("Invalid input: enter a valid year (2010-2019)");
            return;
        }
        year = bufferYear;

        List<String> songsInRange = backend.filterNewSongs(year);

        // Guard clause if getValues has yet to be called, confirms to user that year has been set
        if (songsInRange.size() == 0) {
            System.out.println("Minimum year set: " + year);
            return;
        }

        // Outputs result of backend.getRange() with the year constraint
        System.out.println(songsInRange.size() + " songs found between " + min + " and " + max + " from " + year +
                (year == 2019 ? ":" : " to 2019:"));
        for (String each : songsInRange)
            System.out.println(each);
    }

    @Override
    public void topFive() {
        // Instantiates the loudest sounds if getRange() was called
        List<String> loudest;
        try {
            loudest = backend.fiveLoudest();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            return;
        }

        // Outputs result of backend.getRange() with the year constraint
        String output = "Top " + loudest.size() + " loudest songs found between " + min + " and " + max;
        if (year != -1)
            output += " from " + year + (year == 2019 ? ":" : " to 2019:");
        else
            output += ":";
        System.out.println(output);
        for (String each : loudest)
            System.out.println(each);
    }
}
