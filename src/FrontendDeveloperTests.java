import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class FrontendDeveloperTests {
    /**
     * Tests the runCommandLoop() and readData() methods
     */
    @Test
    public void test1() {
        TextUITester testTextInput = new TextUITester("apricot\nr\nsongs.csv\nq\n");

        BackendInterface backend = new Backend(new IterableRedBlackTree<>());
        FrontendInterface frontend = new Frontend(new Scanner(System.in), backend);

        frontend.runCommandLoop();

        String output = testTextInput.checkOutput();
        // System.out.println(output);

        // Does the command loop correctly display min, max, and year when they aren't instantiated
        Assertions.assertTrue(output.contains("[min - max]") && output.contains("(by Min Year: none)"));
        // Does the command loop correctly reject faulty inputs?
        Assertions.assertTrue(output.contains("Invalid input"));
        // Does readData() prompt the user to enter a cvs file path?
        Assertions.assertTrue(output.contains("Enter path to csv file to load: "));
    }

    /**
     * Tests the getRange() method
     */
    @Test
    public void test2() {
        TextUITester testTextInput = new TextUITester("g\n20\ng\n(20-40)\ng\n20-forty\ng\n20-40\nq\n");

        BackendInterface backend = new Backend(new IterableRedBlackTree<>());
        FrontendInterface frontend = new Frontend(new Scanner(System.in), backend);

        frontend.runCommandLoop();

        String output = testTextInput.checkOutput();
        // System.out.println(output);

        // Does getRange() get called?
        Assertions.assertTrue(output.contains("Enter range of values (MIN-MAX)"));
        // Does getRange() correctly reject invalid inputs?
        Assertions.assertTrue(output.contains("Invalid input: output using format MIN-MAX, where MIN and MAX are integers"));
        // Does getRange() output when correctly given an input?
        Assertions.assertTrue(output.contains("songs found between 20 and 40:"));
        // Does min and max get updated in the command loop?
        Assertions.assertTrue(output.contains("[G]et Songs by Liveness [20 - 40]"));
    }

    /**
     * Tests the setFilter() method
     */
    @Test
    public void test3() {
        TextUITester testTextInput = new TextUITester(
                "f\ntwo thousand nineteen\nf\n1990\nf\n2015\nf\n2019\nq\n");

        BackendInterface backend = new Backend(new IterableRedBlackTree<>());
        FrontendInterface frontend = new Frontend(new Scanner(System.in), backend);

        frontend.runCommandLoop();

        String output = testTextInput.checkOutput();
        // System.out.println(output);

        // Does setFilter() get called?
        Assertions.assertTrue(output.contains("Enter minimum year:"));
        // Does setFilter() correctly reject invalid inputs?
        Assertions.assertTrue(output.contains("Invalid input: enter a valid year (2010-2019)"));
        // Does setFilter() output when correctly given an input?
        Assertions.assertTrue(output.contains("Minimum year set: 2015"));
        // Does year get updated in the command loop?
        Assertions.assertTrue(output.contains("(by Min Year: 2015)"));
        // Does setFilter() output when correctly given the input "2019"?
        Assertions.assertTrue(output.contains("Minimum year set: 2019"));
    }

    /**
     * Tests the topFive method
     */
    @Test
    public void test4() {
        TextUITester testTextInput = new TextUITester("d\nq\n");

        BackendInterface backend = new Backend(new IterableRedBlackTree<>());
        FrontendInterface frontend = new Frontend(new Scanner(System.in), backend);

        frontend.runCommandLoop();

        String output = testTextInput.checkOutput();
        // System.out.println(output);

        // Does getRange() get called?
        Assertions.assertTrue(output.contains("getRange() not called yet"));
    }

    /**
     * Tests multiple methods called in a single command loop
     */
    @Test
    public void test5() {
        TextUITester testTextInput = new TextUITester("r\nsongs.csv\ng\n5-10\nd\nf\n2018\nd\ng\n45-50\nq\n");
        BackendInterface backend = new Backend(new IterableRedBlackTree<>());
        FrontendInterface frontend = new Frontend(new Scanner(System.in), backend);

        frontend.runCommandLoop();

        String output = testTextInput.checkOutput();
        System.out.println(output);

        // Does setFilter() output correctly when min, max, and year are already set?
        Assertions.assertTrue(output.contains("songs found between 5 and 10 from 2018 to 2019:"));
        // Does topFive() output correctly when min, max, and year are already set?
        Assertions.assertTrue(output.contains("loudest songs found between 5 and 10 from 2018 to 2019:"));
        // Does getRange() output when year is already set?
        Assertions.assertTrue(output.contains("songs found between 45 and 50 from 2018 to 2019"));
    }

    /**
     * Tests filterNewSongs() that it doesn't return any songs if getRange hasn't been called
     */
    @Test
    public void testIntegration1() {
        TextUITester testTextInput = new TextUITester("f\n2016\nq\n");

        BackendInterface backend = new Backend(new IterableRedBlackTree<>());
        FrontendInterface frontend = new Frontend(new Scanner(System.in), backend);

        frontend.runCommandLoop();

        String output = testTextInput.checkOutput();
        // System.out.println(output);

        // Does setFilter() output correctly when min, max, and year are already set?
        Assertions.assertTrue(output.contains("Minimum year set: 2016"));
    }

    /**
     * Tests multiple methods called in a single command loop
     */
    @Test
    public void testIntegration2() {
        TextUITester testTextInput = new TextUITester("r\nsongs.csv\ng\n15-40\nd\nf\n2018\nd\ng\n5-10\nq\n");

        BackendInterface backend = new Backend(new IterableRedBlackTree<>());
        FrontendInterface frontend = new Frontend(new Scanner(System.in), backend);

        frontend.runCommandLoop();

        String output = testTextInput.checkOutput();
        // System.out.println(output);

        // Does setFilter() output correctly when min, max, and year are already set?
        Assertions.assertTrue(output.contains("songs found between 15 and 40 from 2018 to 2019:"));
        // Does topFive() output correctly when min, max, and year are already set?
        Assertions.assertTrue(output.contains("loudest songs found between 15 and 40 from 2018 to 2019:"));
        // Does getRange() output when year is already set?
        Assertions.assertTrue(output.contains("songs found between 5 and 10 from 2018 to 2019"));
    }

    /**
     * Tests read data and getRange()
     */
    @Test
    public void testPartner1() {
        IterableSortedCollection <SongInterface> tree = new IterableRedBlackTree<>();
        Backend backend = new Backend(tree);

        try{
            backend.readData("file");
            Assertions.fail();
        } catch (IOException e) { }

        try{
            backend.readData("songs.csv");
        } catch (IOException e) {
            Assertions.fail();
        }

        List<String> output = backend.getRange(5, 10);
        Assertions.assertTrue(output != null && output.size() > 0);
    }

    /**
     * Tests filterNewSongs() and fiveLoudest() before and after getRange is called
     */
    @Test
    public void testPartner2() {
        IterableSortedCollection <SongInterface> tree = new IterableRedBlackTree<>();
        Backend backend = new Backend(tree);

        try{
            backend.readData("songs.csv");
        }
        catch (IOException e) {
            Assertions.fail();
        }

        List<String> filteredBefore = backend.filterNewSongs(2015);
        Assertions.assertTrue(filteredBefore.size() == 0);

        try {
            backend.fiveLoudest();
            Assertions.fail();
        } catch (IllegalStateException e) { }

        backend.getRange(5, 10);

        List<String> filteredAfter = backend.filterNewSongs(2015);
        Assertions.assertTrue(filteredAfter.size() > 0);

        List<String> loadestAfter = backend.fiveLoudest();
        Assertions.assertTrue(loadestAfter.size() > 0);
    }
}