import java.util.List;
import java.io.IOException;

/**
 * BackendInterface - CS400 Project 1: iSongify
 */
public interface BackendInterface {

    //public BackendInterface(IterableSortedCollection<SongInterface> tree)

    /**
     * Loads data from the .csv file referenced by filename.
     * @param filename is the name of the csv file to load data from
     * @throws IOException when there is trouble finding/reading file
     */
    public void readData(String filename) throws IOException;

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
    public List<String> getRange(int low, int high);

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
    public List<String> filterNewSongs(int minYear);

    /**
     * This method makes use of the attribute range specified by the most
     * recent call to getRange().  If a minYear threshold has been set by
     * filterNewSongs() then that will also be utilized by this method.
     * Of those songs that match these criteria, the five loudest will be 
     * returned by this method as a List of Strings in increasing order of 
     * loudness.  Each string contains the loudness in dB followed by a colon,
     * a space, and then the song's title.
     * If fewer than five such songs exist, return all of them.
     *
     * @return List of five loundest song titles and their respective dB
     * @throws IllegalStateException when getRange() was not previously called.
     */
    public List<String> fiveLoudest();
}
