package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Event;
import model.Post;
import model.UniLinkDatabase;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class CreateEventFormController {
	@FXML private TextField eventName;
	@FXML private TextField eventVenue;
	@FXML private TextField eventCapacity;
	@FXML private DatePicker eventDate;
	@FXML private TextArea eventDescription;
	@FXML private Label fileName;
	@FXML private ImageView imageView;
	@FXML private Label errorLabel;

	private String currentUserID;
	private final int MAX_DESCRIPTION = 80;
	private FileChooser fileChooser;
	private File filePath = new File("Default_Image.png");

	public void initialize() {
		//set default image before user choose one
		imageView.setImage(new Image("Default_Image.png"));
	}

	// pass current user id to this window
	public void initData(String currentUserID) {
		this.currentUserID = currentUserID;
	}

	// Allowed user to choose image from computer
	@FXML
	public void chooseImageFromComputer(ActionEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

		fileChooser = new FileChooser();
		fileChooser.setTitle("Choose image");

		// Set to user's directory or go to the default C drive if cannot access
		String userDirectoryString = System.getProperty("user.home");
		File userDirectory = new File(userDirectoryString);

		if (!userDirectory.canRead())
			userDirectory = new File("c:/");

		fileChooser.setInitialDirectory(userDirectory);

		// Set restrict file chooser only can choose *.png *.jpg *.svg
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
				"All Images", "*.jpg", "*.png", "*.svg","*.jpeg"));

		this.filePath = fileChooser.showOpenDialog(stage);

		if (filePath != null && filePath.isFile()) {
			imageView.setImage(new Image(filePath.toURI().toString()));
			fileName.setText(filePath.getName());
		}

	}

	@FXML
	public void createNewEvent(ActionEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		if (eventReadyToCreate()) {

			//set date pattern
			String pattern = "dd/MM/yyyy";
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);

			try {
				// save image to image directory
				String imageDir = String.valueOf(filePath);
				String saveDir = "images" + File.separator + filePath.getName();
				Util.saveImageFromDir(imageDir, saveDir);

				Post post = new Event(eventName.getText(),
						eventDescription.getText(), currentUserID,
						filePath.getName(),
						eventVenue.getText(),
						dateTimeFormatter.format(eventDate.getValue()),
						Integer.parseInt(eventCapacity.getText()));

				UniLinkDatabase.getPosts().put(post.getId(), post);
				stage.close();
			} catch (IllegalArgumentException | FileNotFoundException e) {
				errorLabel.setText(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}



	private boolean eventReadyToCreate() {
		StringBuilder errorMessage = new StringBuilder("");

		if (eventName.getText().length() <= 0)
			errorMessage.append("Please input event name.\n");

		if (eventVenue.getText().length() <= 0)
			errorMessage.append("Please input event venue.\n");

		try {
			if (Integer.parseInt(eventCapacity.getText()) <= 0)
				errorMessage.append("Invalid capacity.\n");
		} catch (NumberFormatException e) {
			errorMessage.append("Capacity should be an integer\n");
		}

		try {
			if (eventDate.getValue().isBefore(LocalDate.now()))
				errorMessage.append("Invalid datetime.\n");
		} catch (NullPointerException e) {
			errorMessage.append("You should choose a datetime.\n");
		}

		if (eventDescription.getText().length() > MAX_DESCRIPTION)
			errorMessage.append("Event description is too long.\n");

		errorLabel.setText(errorMessage.toString());

		if (errorMessage.length() == 0)
			return true;
		else
			return false;
	}
}
