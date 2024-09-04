import java.util.ArrayList;

public class Song implements SongInterface{
  private ArrayList<String> attributes;
  
  
  public Song(ArrayList<String> attributes) {
    this.attributes = attributes;
  }
  public String getTitle() {
    // returns this song's title
    return attributes.get(0);
  }

  public String getArtist() {
    // returns this song's artist
    return attributes.get(1);
  }

  public String getGenres() {
    // returns string containing each of this song's genres
    return attributes.get(2);
  }

  public int getYear() {
    // returns this song's year in the Billboard
    return Integer.parseInt(attributes.get(3));
  }

  public int getBPM() {
    // returns this song's speed/tempo in beats per minute
    return Integer.parseInt(attributes.get(4));
  }

  public int getEnergy() {
    // returns this song's energy rating
    return Integer.parseInt(attributes.get(5));
  }

  public int getDanceability() {
    // returns this song's danceability rating
    return Integer.parseInt(attributes.get(6));
  }

  public int getLoudness() {
    // returns this song's loudness in dB
    return Integer.parseInt(attributes.get(7));
  }

  public int getLiveness() {
    return Integer.parseInt(attributes.get(8));
  }
  
  // dont know what to compare yet
  public int compareTo(SongInterface b) {
    if (this.getLiveness() > b.getLiveness()) {
      return 1;
    }
    else if (this.getLiveness() < b.getLiveness()){
      return -1;
    }
    return this.getTitle().compareTo(b.getTitle());
  }
}
