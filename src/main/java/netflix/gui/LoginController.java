package netflix.gui;

import animatefx.animation.SlideOutRight;
import animatefx.animation.ZoomInUp;
import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private JFXPasswordField passwordText;

    @FXML
    private JFXTextField loginText;

    @FXML
    private JFXButton loginButton;

    @FXML
    private Label statusLabel;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXCheckBox rememberMe;

    @FXML
    private MaterialIconView statusIcon;

    @FXML
    private StackPane stackPane;

    private double x, y;


    @FXML
    void enterLogin(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            if (loginText.getText().equals("admin") && passwordText.getText().equals("admin")) {
                statusLabel.setText("Login successful");
                statusLabel.setTextFill(Color.web("#0f9d58"));
                statusIcon.setGlyphName("DONE");
                statusIcon.setFill(Color.web("#0f9d58"));
                //statusLabel.setVisible(false);
                this.fadeOut();
            } else {
                statusLabel.setVisible(true);
            }
        }
    }

    @FXML
    private void showHelp() {
        JFXDialogLayout content = new JFXDialogLayout();
        Text text = new Text("Forgot your password?");
        text.setFill(Color.WHITE);
        text.setFont(new Font("Century Gothic", 30));
        Text body = new Text("Try admin:admin");
        body.setFill(Color.WHITE);
        body.setFont(new Font("Century Gothic", 20));
        content.setHeading(text);
        content.setBody(body);
        content.setBackground(new Background(new BackgroundFill(Color.web("#1E1E2E"), CornerRadii.EMPTY,
                Insets.EMPTY)));
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton btn1 = new JFXButton("OK");
        btn1.setButtonType(JFXButton.ButtonType.RAISED);
        btn1.getStylesheets().add("/buttonDark.css");
        btn1.setRipplerFill(Color.DARKRED);
        btn1.setPrefSize(100, 30);
        btn1.setOnAction(actionEvent -> dialog.close());
        content.setActions(btn1);
        content.autosize();
        dialog.show();
    }

    private void fadeOut() {
        new SlideOutRight(stackPane).play();
        statusLabel.setVisible(true);
        statusLabel.setText("Login successful");
        statusLabel.setTextFill(Color.web("#0f9d58"));
        statusIcon.setGlyphName("DONE");
        statusIcon.setFill(Color.web("#0f9d58"));
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(1000));
        fadeTransition.setNode(anchorPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(1);
        fadeTransition.play();
        fadeTransition.setOnFinished(actionEvent -> {
            try {
                changeScreen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void changeScreen() throws IOException {
        Parent homeParent = FXMLLoader.load(getClass().getResource("/gui2darkTheme.fxml"));
        Scene homeScene = new Scene(homeParent);
        homeScene.setFill(Color.TRANSPARENT);
        Stage window = (Stage) (anchorPane.getScene()).getWindow();
        window.setScene(homeScene);
        window.show();
        homeParent.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });
        homeParent.setOnMouseDragged(mouseEvent -> {
            window.setX(mouseEvent.getScreenX() - x);
            window.setY(mouseEvent.getScreenY() - y);
        });
    }

    @FXML
    void handleLogin() throws IOException {
        if (loginText.getText().equals("admin") && passwordText.getText().equals("admin")) {
            statusLabel.setText("Login successful");
            statusLabel.setTextFill(Color.web("#0f9d58"));
            statusIcon.setGlyphName("DONE");
            statusIcon.setFill(Color.web("#0f9d58"));
            this.fadeOut();
        } else {
            statusLabel.setVisible(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stackPane.setBackground(Background.EMPTY);
        new ZoomInUp(anchorPane).play();
    }
}
