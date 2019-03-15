package netflix.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import netflix.simulation.entities.managers.UserManager;

import java.net.URL;
import java.util.ResourceBundle;

public class UserController implements Initializable {

    @FXML
    private JFXTextField username;

    @FXML
    private JFXTextField email;

    @FXML
    private JFXTextField birthday;

    @FXML
    private JFXTextField ccnumber;

    @FXML
    private JFXButton add;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXButton cancel;

    private void fadeIn() {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(300));
        fadeTransition.setNode(anchorPane);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

    private void fadeOut() {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(300));
        fadeTransition.setNode(anchorPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();
        fadeTransition.setOnFinished(actionEvent -> {
            Stage stage = (Stage) cancel.getScene().getWindow();
            stage.close();
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fadeIn();

    }

    @FXML
    public void addUser() {
        UserManager.getInstance().newUser();
    }

    @FXML
    public void cancel() {
        fadeOut();
    }
}
