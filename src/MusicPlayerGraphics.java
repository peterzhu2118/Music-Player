import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MusicPlayerGraphics extends JFrame implements ActionListener,
		ListSelectionListener, ChangeListener {
	private Container cont;

	private JComboBox<String> albumMenu;

	private JMenuBar menuBar;

	private JMenu file;
	private JMenuItem addSong;
	private JMenuItem deleteSong;
	private JMenuItem changeBackground;

	private JMenu albums;
	private JMenuItem addAlbum;
	private JMenuItem deleteAlbum;

	private JMenu help;
	private JMenuItem about;

	private JSlider musicTime;
	private JButton playPause;
	private JButton backwards;
	private JButton forwards;
	private JSlider musicVolume;

	private JLabel background;

	private JTextField search;
	private JButton searchButton;

	private DefaultListModel<MusicObject> listOfSongs;
	private JList<MusicObject> songList;
	private JScrollPane scrollableSongList;

	private JCheckBox scramble;

	private JLabel songNameLabel;
	private JTextField songName;

	private JLabel artistLabel;
	private JTextField artist;

	private JLabel genresLabel;
	private JTextField genres;

	private JLabel commentsLabel;
	private JTextField comments;

	private JLabel ratingLabel;
	private StarRater rating;

	private JButton applyChanges;

	public MusicPlayerGraphics() {
		// Sets up the frame
		super("Music Player");
		this.setSize(550, 550);
		this.setLocation(100, 100);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		// Sets up the container
		cont = new Container();
		this.add(cont);

		// Sets up the drop down menu for the albums
		albumMenu = new JComboBox<>();
		albumMenu.setSize(190, 40);
		albumMenu.setLocation(340, 20);
		albumMenu.addItem("Album List");
		albumMenu.setSelectedIndex(0);
		albumMenu.addActionListener(this);
		setUpAlbums();
		cont.add(albumMenu);

		// Creates a new menu bar
		menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);

		// Creates a new sub menu for songs
		file = new JMenu("File");
		menuBar.add(file);

		// Creates a add song button in the file JMenu
		addSong = new JMenuItem("Import Song");
		addSong.setEnabled(false);
		addSong.addActionListener(this);
		file.add(addSong);

		// Creates a delete song button in the file JMenu
		deleteSong = new JMenuItem("Delete Song");
		deleteSong.setEnabled(false);
		deleteSong.addActionListener(this);
		file.add(deleteSong);

		// Adds a separator
		file.addSeparator();

		// Button for changing the background of the player
		changeBackground = new JMenuItem("Change Background");
		changeBackground.addActionListener(this);
		file.add(changeBackground);

		// Creates a album sub menu
		albums = new JMenu("Albums");
		menuBar.add(albums);

		// Add a album button
		addAlbum = new JMenuItem("Add a Album");
		addAlbum.addActionListener(this);
		albums.add(addAlbum);

		// Delete album button
		deleteAlbum = new JMenuItem("Delete Album");
		deleteAlbum.setEnabled(false);
		deleteAlbum.addActionListener(this);
		albums.add(deleteAlbum);

		// Menu for help
		help = new JMenu("Help");
		menuBar.add(help);

		// Button for about
		about = new JMenuItem("About");
		about.addActionListener(this);
		help.add(about);

		// Field for search
		search = new JTextField();
		search.setLocation(20, 20);
		search.setSize(200, 40);
		search.setFont(new Font("Arial", Font.PLAIN, 20));
		cont.add(search);

		// Button for search
		searchButton = new JButton(new ImageIcon("Resources/normal_search.png"));
		searchButton
				.setRolloverIcon(new ImageIcon("Resources/hover_search.png"));
		searchButton
				.setPressedIcon(new ImageIcon("Resources/click_search.png"));
		searchButton.setBorderPainted(false);
		searchButton.setContentAreaFilled(false);
		searchButton.setOpaque(false);
		searchButton.setLocation(230, 20);
		searchButton.setSize(80, 40);
		searchButton.addActionListener(this);
		cont.add(searchButton);

		// Songs list
		listOfSongs = new DefaultListModel<>();
		songList = new JList<>(listOfSongs);
		songList.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
		songList.setSize(300, 330);
		songList.setLocation(20, 70);
		songList.addListSelectionListener(this);
		scrollableSongList = new JScrollPane(songList,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollableSongList.setSize(300, 330);
		scrollableSongList.setLocation(20, 70);
		scrollableSongList.setOpaque(false);
		cont.add(scrollableSongList);

		// Slider for current music time
		musicTime = new JSlider(JSlider.HORIZONTAL);
		musicTime.setEnabled(false);
		musicTime.addChangeListener(this);
		musicTime.setValue(0);
		musicTime.setLocation(20, 450);
		musicTime.setSize(500, 40);
		musicTime.setOpaque(false);
		cont.add(musicTime);

		// The play/pause button
		playPause = new JButton(new ImageIcon("Resources/normal_play.png"));
		playPause.setRolloverIcon(new ImageIcon("Resources/hover_play.png"));
		playPause.setPressedIcon(new ImageIcon("Resources/click_play.png"));
		playPause.setOpaque(false);
		playPause.setContentAreaFilled(false);
		playPause.setBorderPainted(false);
		playPause.setRolloverEnabled(true);
		playPause.setSize(110, 40);
		playPause.setLocation(160, 410);
		playPause.addActionListener(this);
		cont.add(playPause);

		// Button to go to the next song
		forwards = new JButton(new ImageIcon("Resources/normal_forward.png"));
		forwards.setRolloverIcon(new ImageIcon("Resources/hover_forward.png"));
		forwards.setPressedIcon(new ImageIcon("Resources/click_forward.png"));
		forwards.setOpaque(false);
		forwards.setContentAreaFilled(false);
		forwards.setBorderPainted(false);
		forwards.setLocation(240, 410);
		forwards.setSize(100, 40);
		forwards.addActionListener(this);
		cont.add(forwards);

		// Button to go to the previous song
		backwards = new JButton(new ImageIcon("Resources/normal_backward.png"));
		backwards
				.setRolloverIcon(new ImageIcon("Resources/hover_backward.png"));
		backwards.setPressedIcon(new ImageIcon("Resources/click_backward.png"));
		backwards.setOpaque(false);
		backwards.setBorderPainted(false);
		backwards.setContentAreaFilled(false);
		backwards.setLocation(100, 410);
		backwards.setSize(60, 40);
		backwards.addActionListener(this);
		cont.add(backwards);

		// Check box for scrambling the song
		scramble = new JCheckBox("Scramble");
		scramble.setLocation(10, 410);
		scramble.setSize(80, 40);
		scramble.setOpaque(false);
		cont.add(scramble);

		// Slider for music volume
		musicVolume = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
		musicVolume.setMajorTickSpacing(50);
		musicVolume.setMinorTickSpacing(25);
		musicVolume.setPaintTicks(true);
		musicVolume.setLocation(320, 405);
		musicVolume.setSize(220, 50);
		musicVolume.addChangeListener(this);
		Hashtable<Integer, JComponent> x = new Hashtable<>();
		x.put(12, new JLabel("Volume"));
		musicVolume.setLabelTable(x);
		musicVolume.setPaintLabels(true);
		musicVolume.setOpaque(false);
		cont.add(musicVolume);

		// Label that says "Song Name"
		songNameLabel = new JLabel("Song Name");
		songNameLabel.setLocation(340, 75);
		songNameLabel.setSize(100, 20);
		cont.add(songNameLabel);

		// Field for song name
		songName = new JTextField();
		songName.setLocation(340, 95);
		songName.setSize(185, 40);
		songName.setEnabled(false);
		songName.setFont(new Font("Arial", Font.PLAIN, 20));
		cont.add(songName);

		// Label that says "Artist"
		artistLabel = new JLabel("Artist");
		artistLabel.setLocation(340, 140);
		artistLabel.setSize(100, 20);
		cont.add(artistLabel);

		// Field for artist name
		artist = new JTextField();
		artist.setLocation(340, 160);
		artist.setSize(185, 40);
		artist.setEnabled(false);
		artist.setFont(new Font("Arial", Font.PLAIN, 20));
		cont.add(artist);

		// Label that says "Genres"
		genresLabel = new JLabel("Genres");
		genresLabel.setLocation(340, 205);
		genresLabel.setSize(100, 20);
		cont.add(genresLabel);

		// Field for genres
		genres = new JTextField();
		genres.setLocation(340, 225);
		genres.setSize(185, 40);
		genres.setFont(new Font("Arial", Font.PLAIN, 20));
		genres.setEnabled(false);
		cont.add(genres);

		// Label that says "Comments"
		commentsLabel = new JLabel("Comments");
		commentsLabel.setLocation(340, 270);
		commentsLabel.setSize(70, 20);
		cont.add(commentsLabel);

		// Field for comments
		comments = new JTextField();
		comments.setLocation(340, 290);
		comments.setSize(185, 40);
		comments.setEnabled(false);
		comments.setFont(new Font("Arial", Font.PLAIN, 20));
		cont.add(comments);

		// Label that says "Rating"
		ratingLabel = new JLabel("Rating");
		ratingLabel.setLocation(340, 345);
		ratingLabel.setSize(80, 30);
		cont.add(ratingLabel);

		// Creates a star rater (created by noblemaster)
		rating = new StarRater();
		rating.setLocation(340, 370);
		rating.setSize(80, 30);
		rating.setEnabled(false);
		cont.add(rating);

		// Creates the apply changes button
		applyChanges = new JButton(new ImageIcon("Resources/normal_apply.png"));
		applyChanges
				.setRolloverIcon(new ImageIcon("Resources/hover_apply.png"));
		applyChanges.setPressedIcon(new ImageIcon("Resources/click_apply.png"));
		applyChanges.setBorderPainted(false);
		applyChanges.setContentAreaFilled(false);
		applyChanges.setOpaque(false);
		applyChanges.setLocation(440, 350);
		applyChanges.setSize(80, 40);
		applyChanges.addActionListener(this);
		applyChanges.setEnabled(false);
		cont.add(applyChanges);

		// JLabel for the background of the frame
		background = new JLabel();
		background.setLocation(0, 0);
		background.setSize(550, 550);
		cont.add(background);

		// Starts the thread that moves the time slider
		musicTimeThread();
		// Takes the background file from the folder
		updateBackground();

		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// If the source is from changing albums
		if (e.getSource().equals(albumMenu)) {
			// If the user selected album
			if (((String) albumMenu.getSelectedItem()).equals("Album List")) {
				// Prevents the user from adding a song, deleting a song,
				// deleting a album and changing the database
				addSong.setEnabled(false);
				deleteSong.setEnabled(false);
				deleteAlbum.setEnabled(false);

				songName.setEnabled(false);
				artist.setEnabled(false);
				genres.setEnabled(false);
				comments.setEnabled(false);
				rating.setEnabled(false);
				applyChanges.setEnabled(false);

				songName.setText("");
				artist.setText("");
				genres.setText("");
				comments.setText("");
				rating.setSelection(0);

				// Stops the song if its currently playing
				MusicPlayer.stopSong();

				// Clears the list of songs
				listOfSongs.clear();
			} else {
				// Prevents the user from changing the database
				songName.setEnabled(false);
				artist.setEnabled(false);
				genres.setEnabled(false);
				comments.setEnabled(false);
				rating.setEnabled(false);
				applyChanges.setEnabled(false);

				songName.setText("");
				artist.setText("");
				genres.setText("");
				comments.setText("");
				rating.setSelection(0);

				// Stops the song if its currently playing
				MusicPlayer.stopSong();

				addSong.setEnabled(true);
				deleteAlbum.setEnabled(true);

				// Update the list of songs to the album
				changeAlbum((String) albumMenu.getSelectedItem());
			}
		} else if (e.getSource().equals(addSong)) { // If the source is from
													// adding songs button
			if (!((String) albumMenu.getSelectedItem()).equals("Album List")) {
				// Opens a file chooser and gets the path of the selected item
				File path = fileChooserMP3();

				// If the path is null
				if (path == null) {

				} else if (!path.getName()
						.substring(path.getName().length() - 3).equals("mp3")) {
					JOptionPane.showMessageDialog(this, "File Not an MP3");
				} else {
					try {
						// Copies the song from the source path to the selected
						// album
						MusicIO.copySong(path,
								(String) albumMenu.getSelectedItem());

						// Adds the new song into the list of songs
						listOfSongs.addElement(new MusicObject(new File(
								"Music/" + (String) albumMenu.getSelectedItem()
										+ "/" + path.getName())));

						// Turns the DefaultListModel into a ArrayList
						ArrayList<MusicObject> x = new ArrayList<>(
								Arrays.asList(Arrays.copyOf(
										listOfSongs.toArray(),
										listOfSongs.size(), MusicObject[].class)));

						// Updates the database with the current file
						MusicIO.updateDatabase(
								(String) albumMenu.getSelectedItem(), x);
					} catch (IOException e1) { // Throw if the file exists
						JOptionPane.showMessageDialog(this,
								"File Cannot Be Copied: File Already Exists");
					}
				}
			}
		} else if (e.getSource().equals(deleteSong)) { // If the source is from
														// delete song button
			// Prevents the user from changing the database
			songName.setEnabled(false);
			artist.setEnabled(false);
			genres.setEnabled(false);
			comments.setEnabled(false);
			rating.setEnabled(false);
			applyChanges.setEnabled(false);

			songName.setText("");
			artist.setText("");
			genres.setText("");
			comments.setText("");
			rating.setSelection(0);

			// If an element was selected in the song list
			if (songList.getSelectedValue() != null
					&& !((String) albumMenu.getSelectedItem())
							.equals("Album List")) {
				// Deletes the song
				try {
					MusicIO.deleteSong(songList.getSelectedValue().getPath());
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				// Removes the song from the list of songs in the GUI
				listOfSongs.remove(songList.getSelectedIndex());
				try {
					ArrayList<MusicObject> x = new ArrayList<>(
							Arrays.asList(Arrays.copyOf(listOfSongs.toArray(),
									listOfSongs.size(), MusicObject[].class)));

					// Updates the database
					MusicIO.updateDatabase(
							(String) albumMenu.getSelectedItem(), x);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// readdAlbumList();
			}

		} else if (e.getSource().equals(changeBackground)) { // If the selection
																// was to change
																// the
																// background of
																// the Music
																// Player
			// A warning
			JOptionPane.showMessageDialog(this,
					"Pictures are recommended to be 500 x 550 pixels",
					"Information", JOptionPane.INFORMATION_MESSAGE);

			// Opens a file chooser
			File pic = fileChooserPicture();

			// If the cancel option was selected
			if (pic == null) {

			} else if (!pic.getName().substring(pic.getName().length() - 3)
					.toLowerCase().equals("jpg")) { // If its not a jpg file
				JOptionPane.showMessageDialog(this, "Not a picture file");
			} else {
				// Copy the picture
				try {
					MusicIO.copyPic(pic);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// Sets the background
				background.setIcon(new ImageIcon(pic.getAbsolutePath()));
			}

		} else if (e.getSource().equals(addAlbum)) { // If the source is from
														// the add album button
			// Asks for a input
			String albumName = JOptionPane.showInputDialog(this,
					"Please Input a Album Name");

			// If the user's input is not "cancel"
			if (albumName != null) {
				albumName = albumName.trim();

				// If the album exists
				while (comboBoxContains(albumName)) {
					albumName = JOptionPane.showInputDialog(this,
							"Album Exists, Please Input Another Album Name");
					albumName = albumName.trim();
				}

				// If the album name is empty
				while (albumName.length() < 1) {
					albumName = JOptionPane.showInputDialog(this,
							"Please Input a Valid Album Name");
				}

				try {
					// Creates a new album
					MusicIO.addAlbum(albumName);
				} catch (FileNotFoundException e1) {

					e1.printStackTrace();
				} catch (IOException e1) { // If a invalid syntax was used
					JOptionPane.showConfirmDialog(this, "Syntax Invalid");
				} catch (IllegalArgumentException e1) { // If the album exists
					JOptionPane.showConfirmDialog(this,
							"Album Exists, Cannot Create It");
				}

				// Add the item into the list
				albumMenu.addItem(albumName);
			}

		} else if (e.getSource().equals(deleteAlbum)) { // If the source is from
														// the delete album
														// button
			if (((String) albumMenu.getSelectedItem()).equals("Album List")) {

			} else {
				// Warning message
				if (JOptionPane
						.showConfirmDialog(this,
								"Are You Sure You Want to Delete This Album? This Cannot Be Undone.") == JOptionPane.OK_OPTION) {
					// Deletes the album
					MusicIO.deleteAlbum((String) albumMenu.getSelectedItem());
				}

				// Removes the item from the drop down menu
				albumMenu.removeItemAt(albumMenu.getSelectedIndex());
			}

		} else if (e.getSource().equals(about)) { // If the about button was
													// pressed
			JOptionPane
					.showMessageDialog(
							this,
							"Java Music Player. \n© Peter Zhu 2015. DO NOT DISTRIBUTE.",
							"About", JOptionPane.PLAIN_MESSAGE);
		} else if (e.getSource().equals(searchButton)) { // If the search button
															// was pressed
			// If the search bar is empty, list all the songs
			if (search.getText().equals("")) {
				listOfSongs.clear();

				if (!albumMenu.getSelectedItem().equals("Album List"))
					changeAlbum((String) albumMenu.getSelectedItem());
			} else { // Look for all songs containing that string

				DefaultListModel<MusicObject> y = search(search.getText());

				listOfSongs.clear();

				for (int i = 0; i < y.size(); i++) {
					System.out.println("ADDED "
							+ y.getElementAt(i).getSongName());
					listOfSongs.addElement(y.getElementAt(i));
				}
				songList.updateUI();
			}
		} else if (e.getSource().equals(playPause)) { // If the source is from
														// the play/pause button
			MusicPlayer.playPauseSong();

		} else if (e.getSource().equals(forwards)) { // If the source is from
														// the
														// forwards button
			playNextSong();
		} else if (e.getSource().equals(backwards)) { // If the source is from
														// the backwards button
			playLastSong();
		} else if (e.getSource().equals(applyChanges)) { // If the apply button
															// was pressed
			if (songName.getText().length() > 0 && listOfSongs != null
					&& songList.getSelectedValue() != null
					&& !albumMenu.getSelectedItem().equals("Album List")) {
				// Changes the current selected MusicObject
				songList.getSelectedValue().setSongName(songName.getText());
				songList.getSelectedValue().setArtist(artist.getText());
				songList.getSelectedValue().setGenres(genres.getText());
				songList.getSelectedValue().setComments(comments.getText());
				songList.getSelectedValue().setRating(rating.getSelection());

				// Refreshes the window
				songList.updateUI();
				try {
					// Gets the original database
					ArrayList<MusicObject> originalList = MusicIO
							.getDatabase((String) albumMenu.getSelectedItem());

					// Finds the MusicObject that needs to be changed
					for (MusicObject x : originalList) {
						if (x.getPath().equals(
								songList.getSelectedValue().getPath())) {
							x.setSongName(songName.getText());
							x.setArtist(artist.getText());
							x.setGenres(genres.getText());
							x.setComments(comments.getText());
							x.setRating(rating.getSelection());
							break;
						}
					}

					// Updates the database
					MusicIO.updateDatabase(
							(String) albumMenu.getSelectedItem(), originalList);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

	}

	// Changes the background
	private void updateBackground() {
		File pic = new File("Music/Background.jpg");
		// If the file exists then change the background
		if (pic.isFile())
			background.setIcon(new ImageIcon(pic.getAbsolutePath()));
	}

	// Plays the next song
	private void playNextSong() {
		if (songList.getSelectedValue() != null) {
			if (scramble.isSelected()) { // If scramble is selected
				// Get a random number
				Random random = new Random();
				int value = random.nextInt(listOfSongs.size() - 1) + 1
						+ songList.getSelectedIndex();

				// If the next song needs to be wrapped around
				if (value > listOfSongs.size() - 1) {
					value = value - listOfSongs.size();

					// Play song
					MusicPlayer.selectSong(listOfSongs.get(value).getPath());
					MusicPlayer.playPauseSong();
					songList.setSelectedIndex(value);
				} else {
					// Play song
					MusicPlayer.selectSong(listOfSongs.get(value).getPath());
					MusicPlayer.playPauseSong();
					songList.setSelectedIndex(value);
				}
			} else {
				// If this is the last song in the list
				if (songList.getSelectedIndex() + 1 == listOfSongs.size()) {
					songList.setSelectedIndex(0);
				} else { // Then play the next song
					MusicPlayer.selectSong(listOfSongs.get(
							songList.getSelectedIndex() + 1).getPath());
					MusicPlayer.playPauseSong();
					songList.setSelectedIndex(songList.getSelectedIndex() + 1);
				}
			}
		}
	}

	// Search for a keyword in the title of songs
	private DefaultListModel<MusicObject> search(String keyword) {
		DefaultListModel<MusicObject> tempList = new DefaultListModel<>();

		// Looks through every song name in the DefaultListModel and finds all
		// the songs contining the string
		for (int i = 0; i < listOfSongs.size(); i++) {
			if (listOfSongs.getElementAt(i).getSongName().toLowerCase()
					.contains(keyword.toLowerCase())) {
				tempList.addElement(listOfSongs.getElementAt(i));
			}
		}

		return tempList;
	}

	// Play the previous song
	private void playLastSong() {
		if (songList.getSelectedValue() != null) {
			// If the selected value was the first in the list
			if (songList.getSelectedIndex() == 0) {
				MusicPlayer.selectSong(listOfSongs.get(
						listOfSongs.getSize() - 1).getPath());
				MusicPlayer.playPauseSong();
				songList.setSelectedIndex(listOfSongs.getSize() - 1);
			} else { // Then play the last song
				MusicPlayer.selectSong(listOfSongs.get(
						songList.getSelectedIndex() - 1).getPath());
				MusicPlayer.playPauseSong();
				songList.setSelectedIndex(songList.getSelectedIndex() - 1);
			}
		}
	}

	// Checks if the combo box contains a specific element
	private boolean comboBoxContains(String x) {
		ArrayList<String> albumList = new ArrayList<>();

		// Puts all the elements in the combo box into a ArrayList
		for (int i = 0; i < albumMenu.getItemCount(); i++) {
			albumList.add(albumMenu.getItemAt(i));
		}

		// Goes through all the elements in the ArrayList
		for (String i : albumList) {
			// If the element that needs to be found is in the ArrayList then
			// return true
			if (i.equals(x))
				return true;
		}

		// Otherwise return false
		return false;
	}

	// Sets up all the albums
	private void setUpAlbums() {
		ArrayList<String> albums = MusicIO.getAlbums();

		for (String x : albums) {
			albumMenu.addItem(x);
		}
	}

	// Opens a file chooser and returns the selected file as a File
	private File fileChooserMP3() {
		JFileChooser fc = new JFileChooser();
		// Selection is limited to mp3 files
		FileNameExtensionFilter mp3filter = new FileNameExtensionFilter(
				"MP3 Files", "mp3");

		fc.setFileFilter(mp3filter);

		// Opens a file choosing dialogue
		int returnVal = fc.showOpenDialog(this);

		// If the "yes" button is pressed on a mp3 file
		// then return the path
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return fc.getSelectedFile();
		} else { // If "cancel" is selected return null
			return null;
		}
	}

	// Opens a file chooser for a picture
	private File fileChooserPicture() {
		JFileChooser fc = new JFileChooser();

		// Filters to only a JPG
		FileNameExtensionFilter picFilter = new FileNameExtensionFilter(
				"Picture Files", "jpg");

		fc.setFileFilter(picFilter);

		int returnVal = fc.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return fc.getSelectedFile();
		} else {
			return null;
		}
	}

	// Changes the album and updates the songs
	private void changeAlbum(String albumName) {
		try {
			listOfSongs.clear();

			ArrayList<MusicObject> x = MusicIO.getDatabase(albumName);

			for (MusicObject i : x) {
				listOfSongs.addElement(i);
			}
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	// A thread that keeps on updating the slider for the music time
	private void musicTimeThread() {
		Thread t = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						int time = MusicPlayer.getCurrentTime();
						int totalTime = MusicPlayer.getSongLength();

						musicTime.setMaximum(totalTime);
						musicTime.setValue(time);

						// If the song has ended, play the next song
						if (totalTime == musicTime.getValue()) {
							playNextSong();
						}
					} catch (NullPointerException e) {
						// e.printStackTrace();
					}

					// Sleep 200 miliseconds
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		t.start();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource().equals(songList)) { // If a element in the list got
												// selected
			// Play the song
			if (songList.getSelectedValue() != null) {
				// Enable the database
				deleteSong.setEnabled(true);
				songName.setEnabled(true);
				artist.setEnabled(true);
				genres.setEnabled(true);
				comments.setEnabled(true);
				rating.setEnabled(true);
				applyChanges.setEnabled(true);
				musicTime.setEnabled(true);

				musicTime.setValue(0);

				// Set the text for the database entries
				songName.setText(songList.getSelectedValue().getSongName());
				artist.setText(songList.getSelectedValue().getArtist());
				genres.setText(songList.getSelectedValue().getGenres());
				comments.setText(songList.getSelectedValue().getComments());
				rating.setSelection(songList.getSelectedValue().getRating());

				// Play the song
				MusicPlayer.selectSong(songList.getSelectedValue().getPath());
				MusicPlayer.playPauseSong();
			}
		}

	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// If the volume was changed
		if (e.getSource().equals(musicVolume)) {
			MusicPlayer.setVolume(musicVolume.getValue() / 100.0);
		} else if (e.getSource().equals(musicTime)) { // If the time was changed
			try {
				if (MusicPlayer.getCurrentTime() != musicTime.getValue())
					MusicPlayer.setCurrentTime(musicTime.getValue());
			} catch (NullPointerException x) {

			}
		}
	}

}
