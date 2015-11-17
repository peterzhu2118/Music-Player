import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.util.*;

public class MusicIO {
	static {
		// Makes the Music folder if it doesnt exist
		File file = new File("Music");
		if (!file.exists())
			file.mkdirs();

		// Creates the temp file used as a solution to the bug in dispose()
		// method in MusicPlayer class
		File tempMP3 = new File("Music/temp.mp3");
		if (!tempMP3.exists())
			try {
				tempMP3.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

	}

	// Creates an album and an empty database file
	public static void addAlbum(String albumName) throws FileNotFoundException,
			IOException {
		File file = new File("Music\\" + albumName);

		// Checks if the album exists
		if (file.exists()) {
			throw new IllegalArgumentException(
					"Album already Exists : Cannot create it");
		} else {
			ObjectOutput output = null;
			try {
				// Make the album
				file.mkdirs();

				ArrayList<MusicObject> temp = new ArrayList<>();

				file = new File("Music\\" + albumName + "\\Database.ser");

				// Make the database file
				file.createNewFile();

				// Add an empty ArrayList to the
				output = new ObjectOutputStream(new BufferedOutputStream(
						new FileOutputStream(file)));

				output.writeObject(temp);
			} finally { // Closes the stream
				if (output != null) {
					output.close();
				}
			}
		}
	}

	// Gets the database file
	public static ArrayList<MusicObject> getDatabase(String albumName)
			throws FileNotFoundException, IOException, ClassNotFoundException {

		// Creates a file object
		File tempFile = new File("Music\\" + albumName + "\\Database.ser");

		// If the database exists
		if (tempFile.exists()) {
			ObjectInput input = null;
			try {
				// Creates the object reader
				input = new ObjectInputStream(new BufferedInputStream(
						new FileInputStream(tempFile)));

				// Creates the read ArrayList
				@SuppressWarnings("unchecked")
				ArrayList<MusicObject> data = (ArrayList<MusicObject>) input
						.readObject();

				return data;
			} finally {
				// Closes the stream
				if (input != null) {
					input.close();
				}
			}
		} else {
			throw new IllegalArgumentException(
					"Cannot read file: File doesn't exist");
		}
	}

	// Updates the database
	public static void updateDatabase(String albumName,
			ArrayList<MusicObject> database) throws IOException {

		File file = new File("Music\\" + albumName + "\\Database.ser");

		// If the database exists
		if (file.exists()) {
			ObjectOutput output = null;

			// Delete current database
			file.delete();

			try {
				// Create a new database
				file.createNewFile();

				// Output object
				output = new ObjectOutputStream(new BufferedOutputStream(
						new FileOutputStream(file)));

				// Writes the object
				output.writeObject(database);
			} finally {
				// Closes the stream
				if (output != null) {
					output.close();
				}
			}

		} else { // If the file doesn't exist
			throw new IllegalArgumentException(
					"Cannot update database : Database doesn't exist");
		}
	}

	// Deletes a file based on the file path taken in
	public static void deleteSong(File path) throws IOException {
		// If the path exists
		if (path.exists()) {
			MusicPlayer.stopSong();

			for (int i = 0; i < 2; i++) {
				try {
					Files.delete(path.toPath());
					break;
				} catch (FileSystemException e) {
					System.gc();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			// path.delete();
		} else {
			throw new IllegalArgumentException(
					"Cannot delete song: File doesn't exist");
		}
	}

	// Delete an album and all files in it
	public static void deleteAlbum(String albumName) {
		File file = new File("Music\\" + albumName);

		// If the file exists
		if (file.exists()) {
			deleteFile(file);
		} else {
			throw new IllegalArgumentException(
					"No Album Found : Cannot Delete Album");
		}
	}

	// Recursively deletes all files
	// Code from mkyong
	// http://www.mkyong.com/java/how-to-delete-directory-in-java/
	private static void deleteFile(File file) {
		if (file.isDirectory()) {

			// directory is empty, then delete it
			if (file.list().length == 0) {

				file.delete();

			} else {

				// list all the directory contents
				String files[] = file.list();

				for (String temp : files) {
					// construct the file structure
					File fileDelete = new File(file, temp);

					// recursive delete
					deleteFile(fileDelete);
				}

				// check the directory again, if empty then delete it
				if (file.list().length == 0) {
					file.delete();
				}
			}

		} else {
			// if file, then delete it
			file.delete();
		}
	}

	// Returns all the albums in a list
	public static ArrayList<String> getAlbums() {
		File file = new File("Music");

		ArrayList<String> albums = new ArrayList<>(Arrays.asList(file.list()));

		albums.remove("temp.mp3");

		albums.remove("Background.jpg");

		return albums;
	}

	// Copies a file from a source file to a destination folder and overrides a
	// file if it exists (shouldn't happen)
	public static void copySong(File src, String dest) throws IOException {
		Files.copy(src.toPath(),
				new File("Music/" + dest + "/" + src.getName()).toPath(),
				java.nio.file.StandardCopyOption.COPY_ATTRIBUTES);
	}

	// Copies a picture from a source file to the background folder
	public static void copyPic(File src) throws IOException {
		File dest = new File("Music/Background.jpg");

		// If there is a background, delete it
		if (dest.exists()) {
			dest.delete();
		}

		// Copy the file
		Files.copy(src.toPath(), dest.toPath(),
				java.nio.file.StandardCopyOption.COPY_ATTRIBUTES);
	}
}
