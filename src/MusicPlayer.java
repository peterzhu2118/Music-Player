import java.io.File;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.*;
import javafx.util.Duration;

public class MusicPlayer {
	private static JFXPanel panel;
	private static MediaPlayer currentMusic;
	private static double volume;

	// Default volume is set to max
	static {
		volume = 1.0;
	}

	// Starts a new song
	public static void selectSong(File path) {
		// Stops the current music

		if (currentMusic != null) {
			currentMusic.stop();
		}

		// Creates a JFXPanel and starts the music
		panel = new JFXPanel();
		Media media = new Media(path.toPath().toUri().toString());
		currentMusic = new MediaPlayer(media);
		currentMusic.setVolume(volume);
	}

	// Stops the song, frees up the RAM used by it and frees up the file
	// currently being read
	public static void stopSong() {
		if (currentMusic != null) {
			// Disposes the current music player that is reading the MP3
			// This method has a bug and doesn't work properly, but is still needed
			// This bug is in Java itself so I cannot fix it
			currentMusic.dispose();
			currentMusic = null;
			
			// Calls garbage collect
			System.gc();

			// Solution to .dispose() bug
			// Link the MediaPlayer to a useless file
			currentMusic = new MediaPlayer(new Media(new File("Music/temp.mp3")
					.toPath().toUri().toString()));
			// Calls garbage collect again
			System.gc();
		}
	}

	// If the song is playing, pause it. If the song is paused, then play it
	public static void playPauseSong() {
		// System.out.println(currentMusic.getStatus());
		if (currentMusic != null) {
			if (currentMusic.getStatus().equals(MediaPlayer.Status.PLAYING)) {
				currentMusic.pause();
				// System.out.println("Paused");
			} else if (currentMusic.getStatus().equals(
					MediaPlayer.Status.PAUSED)
					|| currentMusic.getStatus().equals(
							MediaPlayer.Status.UNKNOWN)) {
				// System.out.println("Play");
				currentMusic.play();
			}
		}
	}

	public static void pauseSong() {
		// If the current music is not null
		// and the music is playing then pause it
		if (currentMusic != null
				&& currentMusic.getStatus().equals(MediaPlayer.Status.PLAYING)) {
			currentMusic.pause();
		}
	}

	// Returns the current time as seconds as a long
	public static int getCurrentTime() {
		return (int) currentMusic.getCurrentTime().toSeconds();
	}

	// Sets the current time as a long in seconds
	public static void setCurrentTime(long seconds) {
		currentMusic.seek(new Duration(seconds * 1000.0));
	}

	// Gets the length of the song in seconds
	public static int getSongLength() {
		return (int) currentMusic.getStopTime().toSeconds();
	}

	// Sets the volume as a double between 0 and 1
	public static void setVolume(double volume) {
		if (volume >= 0 && volume <= 1) {
			MusicPlayer.volume = volume;

			if (currentMusic != null)
				currentMusic.setVolume(volume);
		}
	}

	// Gets the volume as a double between 0 and 1
	public static double getVolume() {
		return currentMusic.getVolume();
	}

}
