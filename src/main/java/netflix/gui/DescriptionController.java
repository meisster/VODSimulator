package netflix.gui;

import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import netflix.simulation.entities.Video;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

public class DescriptionController implements Initializable {
    private static Video video;
    final CategoryAxis xAxis = new CategoryAxis();
    final NumberAxis yAxis = new NumberAxis();
    @FXML
    private final LineChart<String, Number> descChart = new LineChart<String, Number>(xAxis, yAxis);
    @FXML
    private ImageView imageView;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label title;
    @FXML
    private Label description;
    @FXML
    private JFXButton cancel;
    @FXML
    private Hyperlink link;
    @FXML
    private Label genre;

    public static void passProduct(Video video) {
        DescriptionController.video = video;
    }

    @FXML
    private void exit() {
        fadeOut();
    }

    private void fadeIn() {
        Map<Integer, String> months = new HashMap<>();
        descChart.setTitle("Product watch chart");
        xAxis.setLabel("Month");
        yAxis.setLabel("Users watching");
        months.put(0, "January");
        months.put(1, "February");
        months.put(2, "March");
        months.put(3, "April");
        months.put(4, "May");
        months.put(5, "June");
        months.put(6, "July");
        months.put(7, "August");
        months.put(8, "September");
        months.put(9, "October");
        months.put(10, "November");
        months.put(11, "December");
        FadeTransition fadeTransition = new FadeTransition();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        fadeTransition.setDuration(Duration.millis(300));
        fadeTransition.setNode(anchorPane);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
        fadeTransition.setOnFinished(actionEvent -> {
            IntStream.range(0, video.getWatchData().size()).forEach(nbr -> {
                series.getData().add(new XYChart.Data<String, Number>(months.get(nbr), video.getWatchData().get(nbr)));
            });
            series.setName("Watches over time");
            descChart.getData().add(series);
            title.setText(video.getID());
            description.setText(String.valueOf(video.getWatchCount()));
            imageView.setImage(new Image(video.getImagePath()));
            genre.setText(video.getGenre());
        });

    }

    private void fadeOut() {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(100));
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
}
