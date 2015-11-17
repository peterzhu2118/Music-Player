
import java.io.*;

public class MusicObject implements Serializable{
	private File path;
	private String songName;
	private String artist;
	private String genres;
	private String comments;
	private int rating;

	// Constructor that takes in the file path of the songs
	public MusicObject(File path) {
		this(path, path.getName().substring(0, path.getName().length() - 4),
				"", "", "", 0);
	}

	// Private helper constructor that initializes all the fields
	private MusicObject(File path, String songName, String artist,
			String genres, String comments, int rating) {
		this.path = path;
		this.songName = songName;
		this.artist = artist;
		this.genres = genres;
		this.comments = comments;
		this.rating = rating;
	}

	// Gets the path of the song
	public File getPath() {
		return path;
	}

	// Gets the song name
	public String getSongName() {
		return songName;
	}

	// Sets the song name
	public void setSongName(String songName) {
		this.songName = songName;
	}

	// Gets the artist name
	public String getArtist() {
		return artist;
	}

	// Sets the artist name
	public void setArtist(String artist) {
		this.artist = artist;
	}

	// Gets the genres of the song
	public String getGenres() {
		return genres;
	}

	// Sets the genres of the song
	public void setGenres(String genres) {
		this.genres = genres;
	}

	// Gets the comments for the song
	public String getComments() {
		return comments;
	}

	// Sets the comments for the song
	public void setComments(String comments) {
		this.comments = comments;
	}

	// Gets the rating for the song
	public int getRating() {
		return rating;
	}

	// Sets the rating for the song
	public void setRating(int rating) {
		// Checks if the rating is smaller than 0 or larger then 5
		if (rating > 5 || rating < 0) {
			throw new IllegalArgumentException(
					"Rating cannot be larger than 5 or smaller than 0");
		} else {
			this.rating = rating;
		}
	}
	
	// Returns the song name
	public String toString(){
		return songName;
	}

}
