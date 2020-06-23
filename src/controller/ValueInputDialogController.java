package controller;

import exception.InvalidReplyException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.*;

public class ValueInputDialogController {
    @FXML
    private Label messageLabel;
    @FXML
    private TextField inputValue;
    @FXML
    private Label errorLabel;

    private Post post;


    public void initialize() {
        messageLabel.setText("Please input your offer.");
        errorLabel.setText("");
    }

    public void initData(Post post) {
        this.post = post;
    }

    @FXML
    public void confirmInput(ActionEvent event) throws InvalidReplyException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (isValueValid()) {
            Reply reply = new Reply(post.getId(), Double.parseDouble(inputValue.getText()), LoginWindowController.getCurrentUserID());
            if (isOfferValid(reply)) {
                stage.close();

            }
        }
    }

    private boolean isOfferValid(Reply reply) throws InvalidReplyException {
        if (post instanceof Sale) {
            if (post.handleReply(reply)) {
                if (reply.getValue() < ((Sale) post).getAskingPrice()) {
                    Util.AlertBox("UniLInk", "However, you offer is below the asking price.\nThis item is still on sale.");
                }
                if (reply.getValue() >= ((Sale) post).getAskingPrice()) {
                    Util.AlertBox("UniLink", "Congratulation!\nThe " + post.getTitle() + " has been sold to you." +
                            "\nPlease contact the owner " + post.getCreatorId() + " for more details.");
                }
                return true;
            } else {
                if (reply.getValue() - ((Sale) post).getHighestOffer() < ((Sale) post).getMinRaise()) {
                    errorLabel.setText("Your offer should higher or equal than \nhighest offer + minimal raise.");
                    return false;
                }
            }

        } else if (post instanceof Job) {
            if(post.handleReply(reply)){

                Util.AlertBox("UniLink", "\nOffer accepted!");
                return true;
            }else{
                errorLabel.setText("Invalid offer.");
            }


        }
        return false;
    }

    private boolean isValueValid() {
//        if (inputValue.getText() == null) {
//            errorLabel.setText("You should input something!");
//            return false;
//        }

        try {
            Double.parseDouble(inputValue.getText());
        } catch (NumberFormatException e) {
            errorLabel.setText("The offer should be a number.");
            return false;
        }

        return true;
    }


}
