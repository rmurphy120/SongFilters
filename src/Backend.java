import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class Backend implements BackendInterface{
  private IterableSortedCollection<SongInterface> tree;
  private int year = -1;
  private int low = -1;
  private int high = -1;
  
  public Backend(IterableSortedCollection<SongInterface> tree) {
    this.tree = tree;
  }
  
  /**
   * Loads data from the .csv file referenced by filename.
   * @param filename is the name of the csv file to load data from
   * @throws IOException when there is trouble finding/reading file
   */
  public void readData(String filename) throws IOException{
    try {
      // use scanner
      Scanner scanner = new Scanner(new File(filename));

      String song;
      // skips the first line
      if (scanner.hasNextLine())
        song = scanner.nextLine();

      while (scanner.hasNextLine()) {
        // each line is a song
        song = scanner.nextLine();
  
        // make a list by seperating by ","
        ArrayList<String> attributesList = new ArrayList<>();
        String[] attributes = song.split(",");
  
        for (String attribute : attributes) {
          attributesList.add(attribute);
        }
  
        // if a song starts with a quote and the list is longer than 14 items, 
        // then we seperated a field with commas
        // merge the items in the list until the size is 14
        // assuming there are no quotes in any other field other than title
        while (attributesList.size() > 14) {
          for (int i = 0; i < attributesList.size() - 1; i++) {
            if (attributesList.get(i).startsWith("\"")) {
              attributesList.set(i, attributesList.get(i) + "," + attributesList.get(i + 1));
              attributesList.remove(i + 1);
              break;
            }
          }
        }
        // if any field starts with double quotes, replace it with single quotes
        for (int i = 0; i < attributesList.size(); i ++) {
          if (attributesList.get(i).startsWith("\"")){
            attributesList.set(i, attributesList.get(i).substring
                (1, attributesList.get(i).length()-1).replace("\"\"", "\""));
            
          }
        }
        // add song to tree
        tree.insert(new Song(attributesList));
      }
    } 
    catch (Exception e){
      throw new IOException();
    }
  }
  
  /**
   * Helper method for getRange()
   * @param low - low range for liveness
   * @param high - high range for liveness
   * @returns list of song Interfaces that are within this range and 
   * year specifications
   */
  private List<SongInterface> getOutput(int low, int high){
    this.low = low;
    this.high = high;
    List<SongInterface> output = new ArrayList<SongInterface>();
    
    // create random song input with the low value to use in tree
    ArrayList<String> info = new ArrayList<String>();
    while (info.size() <= 14) {
      info.add(Integer.toString(low));
    }
    Song start = new Song(info);
    
    // iterate through tree starting at our song
    this.tree.setIterationStartPoint(start);
    Iterator<SongInterface> iterator = this.tree.iterator();
    // add songs to output if they are within liveness range
    while(iterator.hasNext()) {
      SongInterface curr = iterator.next();
      if (curr.getLiveness() >= high) {
        break;
      }
      if (curr.getYear() >= this.year) {
        output.add(curr);
      }
    }
    return output;
  }
  
  /**
   * Retrieves a list of song titles for songs that have a Liveness
   * within the specified range (sorted by Liveness in ascending order).  If
   * a minYear filter has been set using filterNewSongs(), then only songs
   * on Billboard during or after that minYear should be included in the
   * list that is returned by this method.
   *
   * Note that either this liveness range, or the resulting unfiltered list
   * of songs should be saved for later use by the other methods defined in
   * this class.
   *
   * @param low is the minimum Liveness of songs in the returned list
   * @param hight is the maximum Liveness of songs in the returned list
   * @return List of titles for all songs in specified range
   */
  public List<String> getRange(int low, int high){
    List<String> output = new ArrayList<String>();
    for (SongInterface song : getOutput(low, high)) {
      output.add(song.getTitle());
    }
    return output;
  }
  
  /**
   * Filters the list of songs returned by future calls of getRange() and
   * fiveLoudest() to only include newer songs.  If getRange() was
   * previously called, then this method will return a list of song titles
   * (sorted in ascending order by Liveness) that only includes songs on
   * Billboard on or after the specified minYear.  If getRange() was not
   * previously called, then this method should return an empty list.
   *
   * Note that this minYear threshold should be saved for later use by the
   * other methods defined in this class.
   *
   * @param minYear is the minimum year that a returned song was on Billboard
   * @return List of song titles, empty if getRange was not previously called
   */
  public List<String> filterNewSongs(int minYear) {
    // if getRange() hasnt been called, throw error
    if (this.high == -1) {
      return new ArrayList<String>();
    }
    this.year = minYear;
    return getRange(this.low, this.high);
  }
  
  /**
   * This method makes use of the attribute range specified by the most
   * recent call to getRange().  If a minYear threshold has been set by
   * filterNewSongs() then that will also be utilized by this method.
   * Of those songs that match these criteria, the five loudest will be
   * returned by this method as a List of Strings in increasing order of
   * liveness.  Each string contains the loudness in dB followed by a colon,
   * a space, and then the song's title.
   * If fewer than five such songs exist, display all of them.
   *
   * @return List of five loundest song titles and their respective dB
   * @throws IllegalStateException when getRange() was not previously called.
   */
  public List<String> fiveLoudest(){
    // throw error if getRange() not called
    if (this.low == -1) {
      throw new IllegalStateException("getRange() not called yet");
    }
    List<String> output = new ArrayList<String>();
    List<SongInterface> temp = new ArrayList<SongInterface>();
    List<SongInterface> songs = getOutput(this.low, this.high);
    
    // find song in tree which is the loudest, append it to temp and remove it 
    // from the tree. repeat this process for the five loudest songs or if there
    // are fewer than five songs, return these songs
    for (int j = 0; j < 5 && songs.size() != 0; j++) {
      int loudest = -1000;
      int index = -1;

      for (int i = 0; i < songs.size(); i++) {
        if (songs.get(i).getLoudness() >= loudest) {
          loudest = songs.get(i).getLoudness();
          index = i;
        }
      }
      temp.add(songs.get(index));
      songs.remove(index);
    }
    // make a new tree and go through the output to put the five loudest songs in
    // order of liveness
    List<SongInterface> songs2 = getOutput(this.low, this.high);
    for (SongInterface song : songs2) {
      for (SongInterface loud : temp) {
        if (loud.compareTo(song) == 0) {
          output.add(Integer.toString(loud.getLoudness()) + ": " + loud.getTitle());
          break;
        }
      }
    }
    return output;
  }
}

