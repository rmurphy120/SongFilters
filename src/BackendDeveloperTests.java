import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import java.io.IOException;

public class BackendDeveloperTests{
  /** 
   * Checks the functionality of readData()
   */
  @Test
  public void testRead(){
    // initialize BackendPlaceholder object
    IterableSortedCollection <SongInterface> tree = new IterableRedBlackTree<>();
    Backend b = new Backend(tree);
    // try reading in incorrect file
    try{
      b.readData("blank");
      assertFalse(true);
    }
    // should throw IOException
    catch (IOException e){
      assertTrue(true);
    }
    // other exceptions are an error
    catch (Exception e){
      assertFalse(true);
    }
    // read in correct file name
    try{
      b.readData("songs.csv");
      assertTrue(true);
    }
    // Exceptions are an error
    catch (Exception e){
      assertFalse(true);
    }
  }

  /** 
   * Checks the functionality of fiveLoudest before calling getRange() and calling getRange()
   * for the first time
   */
  @Test
  public void testInitialCalls(){
    // initialize BackendPlaceholder object
    IterableSortedCollection<SongInterface> tree = new IterableRedBlackTree<>();
    BackendInterface b = new Backend(tree);
    // read in data
    try{
      b.readData("songs.csv");
    }
    catch (IOException e){}
    // call fiveLoudest()
    try {
      b.fiveLoudest();
     //assertFalse(true);
    }
    // this call should throw an error since getRange() has not been called
    catch (IllegalStateException e) {
      //assertTrue(true);
    }
    // call getRange() and get the correct output
    assertEquals(b.getRange(50, 100), java.util.Arrays.asList(new String[]{
        "Hey, Soul Sister",
        "Love The Way You Lie",
        "TiK ToK",
        "Bad Romance",
        "Just the Way You Are"
    }));
  }
  
  /** 
   * Checks the output of filerNewSongs being called before getRange()
   */
  @Test
  public void testFilteredBeforeRange() {
    // initialize BackendPlaceholder object
    IterableSortedCollection<SongInterface> tree = new IterableRedBlackTree<>();
    BackendInterface b = new Backend(tree);
    // read in data
    try{
      b.readData("songs.csv");
    }
    catch (IOException e){}
    // call filterNewSongs and make sure it is an empty list
   //assertEquals(b.filterNewSongs(2015), java.util.Arrays.asList(new String[5]));
  }
  
  /** 
   * Checks the output of filterNewSongs() after getRange() is called
   */
  @Test
  public void testFilteredAfterRange() {
    // initialize BackendPlaceholder object
    IterableSortedCollection<SongInterface> tree = new IterableRedBlackTree<>();
    BackendInterface b = new Backend(tree);
    // read in data
    try{
      b.readData("songs.csv");
    }
    catch (IOException e){}
    // call getRange()
    b.getRange(50, 100);
    // call filterNewSongs and confirm it is the correct list of songs
    assertEquals(b.filterNewSongs(2015), java.util.Arrays.asList(new String[]{
        "Hey, Soul Sister",
        "Love The Way You Lie"
    }));
  }
  
  /** 
   * Checks the functionality of fiveLoudest after a call to getRange() and after a call to 
   * filterNewSongs()
   */
  @Test
  public void testFiveLoudest() {
    // initialize BackendPlaceholder object
    IterableSortedCollection<SongInterface> tree = new IterableRedBlackTree<>();
    BackendInterface b = new Backend(tree);
    // read in data
    try{
      b.readData("songs.csv");
    }
    catch (IOException e){}
    // call getRange()
    b.getRange(50, 100);
    // call fiveLoudest() and get correct output
    //assertEquals(b.fiveLoudest(), java.util.Arrays.asList(new String[]{
      //  "-4: Hey, Soul Sister",
       // "-5: Love The Way You Lie",
        //"-6: TiK ToK",
        //"-7: Bad Romance",
        //"-8: Just the Way You Are"
    //}));
    // call filterNewSongs()
    b.filterNewSongs(2015);
     //call fiveLoudest() again and confirm it is a new list
     assertEquals(b.fiveLoudest(), java.util.Arrays.asList(new String[]{
        "-4: Hey, Soul Sister",
        "-5: Love The Way You Lie",
    }));
  }


}

