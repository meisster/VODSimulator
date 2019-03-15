package netflix.gui;

import animatefx.animation.SlideInLeft;
import animatefx.animation.SlideOutDown;
import animatefx.animation.SlideOutLeft;
import animatefx.animation.ZoomInDown;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import javafx.util.Duration;
import netflix.simulation.Simulation;
import netflix.simulation.entities.*;
import netflix.simulation.entities.managers.DistributorManager;
import netflix.simulation.entities.managers.NetflixBudgetManager;
import netflix.simulation.entities.managers.ProductManager;
import netflix.simulation.entities.managers.UserManager;
import netflix.simulation.subscription.SubscriptionHolder;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;

@SuppressWarnings("Duplicates")
public class Controller implements Initializable {

    final MyNumber myNum = new MyNumber();
    @FXML
    public AnchorPane anchorPane;
    @FXML
    public MaterialIconView simulationDone;
    @FXML
    public JFXSpinner spinner;
    @FXML
    private StackedAreaChart<Integer, Integer> testChart;
    @FXML
    private JFXButton buttonHome;
    @FXML
    private JFXButton buttonUsers;
    @FXML
    private JFXButton buttonAll;
    @FXML
    private ContextMenu userContextMenu = new ContextMenu();
    @FXML
    private JFXButton buttonControlPanel;
    @FXML
    private JFXButton buttonSettings;
    @FXML
    private Label labelStatus;
    @FXML
    private JFXButton buttonReloadTable;
    @FXML
    private MaterialIconView mainIcon;
    @FXML
    private JFXTabPane tabPane;
    @FXML
    private JFXToggleButton simulationToggle;
    @FXML
    private JFXMasonryPane masonryPane;
    @FXML
    private JFXTreeTableView<Person> userTreeTableView;
    @FXML
    private JFXTreeTableView<Video> productTreeTableView;
    @FXML
    private TreeTableColumn<Video, String> productColumn = new TreeTableColumn<>("Product Type");
    @FXML
    private TreeTableColumn<Video, String> titleColumn = new TreeTableColumn<>("Title");
    @FXML
    private TreeTableColumn<Video, String> ratingColumn = new TreeTableColumn<>("Rating");
    @FXML
    private TreeTableColumn<Video, String> genreColumn = new TreeTableColumn<>("Genre");
    @FXML
    private TreeTableColumn<Video, String> countryColumn = new TreeTableColumn<>("Country");
    @FXML
    private TreeTableColumn<Video, String> priceColumn = new TreeTableColumn<>("Price");
    @FXML
    private TreeTableColumn<Person, String> userColumn = new TreeTableColumn<>("User ID");
    @FXML
    private TreeTableColumn<Person, String> subscriptionColumn = new TreeTableColumn<>("Subscription");
    @FXML
    private TreeTableColumn<Person, String> birthdayColumn = new TreeTableColumn<>("Birthday");
    @FXML
    private TreeTableColumn<Person, String> emailColumn = new TreeTableColumn<>("E-mail");
    @FXML
    private TreeTableColumn<Person, String> creditCardColumn = new TreeTableColumn<>("Credit Card");
    @FXML
    private JFXTextField productSearchInput;
    @FXML
    private StackPane stackPane;
    @FXML
    private JFXTextField userSearchInput;
    @FXML
    private MaterialIconView homeIcon;
    @FXML
    private MaterialIconView userIcon;
    @FXML
    private MaterialIconView productIcon;
    @FXML
    private MaterialIconView chartIcon;
    @FXML
    private MaterialIconView settingsIcon;
    @FXML
    private JFXButton distributorButton;
    @FXML
    private JFXButton userButton;
    @FXML
    private Pane AddUserPane;
    @FXML
    private JFXButton netflixButton;
    private Timeline timeline = new Timeline();
    @FXML
    private JFXProgressBar timeProgress;
    @FXML
    private JFXSlider simulationSpeed;
    @FXML
    private JFXTextField usersGenerated;
    @FXML
    private Pane mainPane;
    @FXML
    private JFXTextField productsGenerated;
    @FXML
    private Label date;
    @FXML
    private JFXButton cancel;
    @FXML
    private JFXButton buttonInitialize;
    @FXML
    private JFXButton buttonSave;
    @FXML
    private JFXTreeTableView<Person> distributorsTableView;
    @FXML
    private TreeTableColumn<Person, String> distNameColumn;
    @FXML
    private TreeTableColumn<Person, String> bankAccountColumn;
    @FXML
    private TreeTableColumn<Person, String> watchCountColumn;
    @FXML
    private TreeTableColumn<Person, String> watchPriceColumn;
    @FXML
    private JFXButton buttonLoad;
    @FXML
    private Label userWarning;
    @FXML
    private Label productWarning;
    @FXML
    private Label timeElapsedLabel;
    @FXML
    private Label budgetLabel;
    @FXML
    private TextField basicPriceLabel;
    @FXML
    private TextField familyPriceLabel;
    @FXML
    private TextField premiumPriceLabel;
    private double x, y;

    private ObservableList<Person> users = FXCollections.observableArrayList();

    private ObservableList<Video> videos = FXCollections.observableArrayList();

    private ObservableList<Person> distributors = FXCollections.observableArrayList();

    @FXML
    private void setPrice() {
        try {
            SubscriptionHolder.getInstance()
                    .getBasicSubscription()
                    .setPrice(Integer.valueOf(basicPriceLabel.getText()));
            SubscriptionHolder.getInstance()
                    .getFamilySubscription()
                    .setPrice(Integer.valueOf(familyPriceLabel.getText()));
            SubscriptionHolder.getInstance()
                    .getPremiumSubscription()
                    .setPrice(Integer.valueOf(premiumPriceLabel.getText()));
        } catch (NumberFormatException err) {
            labelStatus.setText("Only integers allowed!");
        }
        labelStatus.setText("Subscription price set!");
    }

    @FXML
    private void showCurrentSubscription() {
        basicPriceLabel.setText(SubscriptionHolder.getInstance().getBasicSubscription().getPrice().toPlainString());
        familyPriceLabel.setText(SubscriptionHolder.getInstance().getFamilySubscription().getPrice().toPlainString());
        premiumPriceLabel.setText(SubscriptionHolder.getInstance().getPremiumSubscription().getPrice().toPlainString());
    }

    @FXML
    private void addUsersCount() {
        usersGenerated.setText(String.valueOf(Integer.valueOf(usersGenerated.getText()) + 1));
    }

    @FXML
    private void minusUsersCount() {
        if (!usersGenerated.getText().equals("0"))
            usersGenerated.setText(String.valueOf(Integer.valueOf(usersGenerated.getText()) - 1));
    }

    @FXML
    private void addProductCount() {
        productsGenerated.setText(String.valueOf(Integer.valueOf(productsGenerated.getText()) + 1));
    }

    @FXML
    private void minusProductCount() {
        if (!productsGenerated.getText().equals("0"))
            productsGenerated.setText(String.valueOf(Integer.valueOf(productsGenerated.getText()) - 1));
    }

    private void fadeOut() {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(1000));
        fadeTransition.setNode(anchorPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(1);
        new SlideOutDown(stackPane).play();
        fadeTransition.play();
        fadeTransition.setOnFinished(actionEvent -> exit());
        if (Simulation.getInstance().isRunning()) {
            Simulation.getInstance().endSimulation();
        }
    }

    @FXML
    private void initializeSimulation() {
        DistributorManager.getInstance().setProductCount(Integer.valueOf(productsGenerated.getText()));
        Simulation.getInstance().setUsersCount(Integer.valueOf(usersGenerated.getText()));
        Simulation.getInstance().initializeSimulation(BigDecimal.valueOf(simulationSpeed.getValue()));
        NetflixBudgetManager.setBudget(0);
        simulationToggle.setDisable(false);
        handleTable();
        labelStatus.setText("Simulation initialized!");
    }

    @FXML
    private void showExitDialog() {
        BoxBlur blur = new BoxBlur(3, 3, 3);
        anchorPane.setEffect(blur);
        JFXDialogLayout content = new JFXDialogLayout();
        Text text = new Text("Do you really want to exit?");
        text.setFill(Color.WHITE);
        text.setFont(new Font("Century Gothic", 30));
        content.setHeading(text);
        content.setBackground(new Background(new BackgroundFill(Color.web("#27293D"), CornerRadii.EMPTY,
                Insets.EMPTY)));
        final Region opaqueLayer = new Region();
        opaqueLayer.setStyle("-fx-background-color: #00000000;");
        opaqueLayer.setVisible(true);
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton btn1 = new JFXButton("Exit");
        JFXButton btn2 = new JFXButton("Cancel");
        btn1.setButtonType(JFXButton.ButtonType.RAISED);
        btn1.getStylesheets().add("/buttonDark.css");
        btn1.setRipplerFill(Color.DARKRED);
        btn1.setPrefSize(100, 30);
        btn2.setPrefSize(100, 30);
        btn1.setOnAction(actionEvent -> {
            dialog.close();
            fadeOut();
        });
        btn2.setButtonType(JFXButton.ButtonType.RAISED);
        btn2.getStylesheets().add("/buttonDark.css");
        btn2.setRipplerFill(Color.DARKRED);
        btn2.setOnAction(actionEvent -> {
            dialog.close();
        });
        content.setActions(btn1, btn2);
        content.autosize();
        dialog.setOnDialogClosed(jfxDialogEvent -> anchorPane.setEffect(null));
        dialog.show();
    }

    @FXML
    public void exit() {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    public void addDistributor(ActionEvent event) {
        DistributorManager.getInstance().newDistributor();
        //handleTable();
        labelStatus.setText("Distributor added successfully!");
    }

    @FXML
    public void addUserAction() {
        UserManager.getInstance().newUser();
        handleTable();
        cancel();
    }

    @FXML
    public void addUser() {
        AddUserPane.setVisible(true);
        new SlideInLeft(AddUserPane).play();
    }

    @FXML
    public void handleTable() {

        users = FXCollections.observableArrayList();
        users.addAll(UserManager.getInstance().getUserList().values());
        userTreeTableView.setRoot(new RecursiveTreeItem<>(users, RecursiveTreeObject::getChildren));
        videos = FXCollections.observableArrayList();
        videos.addAll(ProductManager.getInstance().getProductList());
        productTreeTableView.setRoot(new RecursiveTreeItem<>(videos, RecursiveTreeObject::getChildren));
        distributors = FXCollections.observableArrayList();
        distributors.addAll(DistributorManager.getInstance().getDistributors());
        distributorsTableView.setRoot(new RecursiveTreeItem<>(distributors, RecursiveTreeObject::getChildren));
        productTreeTableView.refresh();
        userTreeTableView.refresh();
        distributorsTableView.refresh();
    }

    @FXML
    public void handleClicks(ActionEvent event) {
        if (event.getSource() == buttonHome) {
            mainIcon.setGlyphName("HOME");
            tabPane.getSelectionModel().select(0);
            buttonHome.getStylesheets().set(0, "/chosenButton.css");
            buttonUsers.getStylesheets().set(0, "/fullpackstylingdark.css");
            buttonControlPanel.getStylesheets().set(0, "/fullpackstylingdark.css");
            buttonAll.getStylesheets().set(0, "/fullpackstylingdark.css");
            buttonSettings.getStylesheets().set(0, "/fullpackstylingdark.css");
            homeIcon.setFill(Color.WHITE);
            userIcon.setFill(Color.web("#b20710"));
            productIcon.setFill(Color.web("#b20710"));
            chartIcon.setFill(Color.web("#b20710"));
            settingsIcon.setFill(Color.web("#b20710"));


        } else if (event.getSource() == buttonSettings) {
            mainIcon.setGlyphName("SETTINGS");
            tabPane.getSelectionModel().selectLast();
            buttonSettings.getStylesheets().set(0, "/chosenButton.css");
            buttonUsers.getStylesheets().set(0, "/fullpackstylingdark.css");
            buttonControlPanel.getStylesheets().set(0, "/fullpackstylingdark.css");
            buttonAll.getStylesheets().set(0, "/fullpackstylingdark.css");
            buttonHome.getStylesheets().set(0, "/fullpackstylingdark.css");
            settingsIcon.setFill(Color.WHITE);
            userIcon.setFill(Color.web("#b20710"));
            productIcon.setFill(Color.web("#b20710"));
            chartIcon.setFill(Color.web("#b20710"));
            homeIcon.setFill(Color.web("#b20710"));

        } else if (event.getSource() == buttonAll) {
            mainIcon.setGlyphName("APPS");
            tabPane.getSelectionModel().select(2);
            buttonAll.getStylesheets().set(0, "/chosenButton.css");
            buttonUsers.getStylesheets().set(0, "/fullpackstylingdark.css");
            buttonControlPanel.getStylesheets().set(0, "/fullpackstylingdark.css");
            buttonHome.getStylesheets().set(0, "/fullpackstylingdark.css");
            buttonSettings.getStylesheets().set(0, "/fullpackstylingdark.css");
            productIcon.setFill(Color.WHITE);
            userIcon.setFill(Color.web("#b20710"));
            homeIcon.setFill(Color.web("#b20710"));
            chartIcon.setFill(Color.web("#b20710"));
            settingsIcon.setFill(Color.web("#b20710"));


        } else if (event.getSource() == buttonControlPanel) {
            mainIcon.setGlyphName("ASSESSMENT");
            tabPane.getSelectionModel().select(3);
            buttonControlPanel.getStylesheets().set(0, "/chosenButton.css");
            buttonUsers.getStylesheets().set(0, "/fullpackstylingdark.css");
            buttonHome.getStylesheets().set(0, "/fullpackstylingdark.css");
            buttonAll.getStylesheets().set(0, "/fullpackstylingdark.css");
            buttonSettings.getStylesheets().set(0, "/fullpackstylingdark.css");
            chartIcon.setFill(Color.WHITE);
            userIcon.setFill(Color.web("#b20710"));
            productIcon.setFill(Color.web("#b20710"));
            homeIcon.setFill(Color.web("#b20710"));
            settingsIcon.setFill(Color.web("#b20710"));
        } else if (event.getSource() == buttonUsers) {
            mainIcon.setGlyphName("ACCOUNT_CIRCLE");
            tabPane.getSelectionModel().select(1);
            buttonUsers.getStylesheets().set(0, "/chosenButton.css");
            buttonHome.getStylesheets().set(0, "/fullpackstylingdark.css");
            buttonControlPanel.getStylesheets().set(0, "/fullpackstylingdark.css");
            buttonAll.getStylesheets().set(0, "/fullpackstylingdark.css");
            buttonSettings.getStylesheets().set(0, "/fullpackstylingdark.css");
            userIcon.setFill(Color.WHITE);
            homeIcon.setFill(Color.web("#b20710"));
            productIcon.setFill(Color.web("#b20710"));
            chartIcon.setFill(Color.web("#b20710"));
            settingsIcon.setFill(Color.web("#b20710"));

        } else if (event.getSource() == simulationToggle) {
            if (simulationToggle.isSelected()) {
                Simulation.getInstance().startSimulation();
                spinner.setVisible(true);
                simulationDone.setVisible(false);
                timeline.playFromStart();
                labelStatus.setText("Simulation started!");

            } else if (!simulationToggle.isSelected()) {
                Simulation.getInstance().endSimulation();
                spinner.setVisible(false);
                timeline.stop();
                labelStatus.setText("Simulation stopped!");
            }
        }
    }

    @FXML
    private void saveSimulation() {
        Simulation.getInstance().save();
        labelStatus.setText("Simulation state saved to \"simulation.bin\"!");

    }

    @FXML
    private void loadSimulation() {
        Simulation.getInstance().endSimulation();
        simulationToggle.setSelected(false);
        spinner.setVisible(false);
        Simulation.getInstance().load();
        labelStatus.setText("Simulation state loaded from \"simulation.bin\"!");
        handleTable();
        myNum.setNumber(Simulation.getInstance().getTime() / 360);
        budgetLabel.setText(NetflixBudgetManager.getBudget().intValue() + "$");
        timeElapsedLabel.setText(Simulation.getInstance().getCurrentTime() + "th day");
    }

    @FXML
    private void showUserDetailsDialog(User user) {
        BoxBlur blur = new BoxBlur(3, 3, 3);
        JFXDialogLayout content = new JFXDialogLayout();
        Text heading = new Text("Displaying user details");
        heading.setFont(new Font("Century Gothic", 20));
        heading.setFill(Color.WHITE);
        content.setBackground(new Background(new BackgroundFill(Color.web("#1E1E2E"), CornerRadii.EMPTY,
                Insets.EMPTY)));
        content.setHeading(heading);
        Text body = new Text("User ID: " + user.getID() + "\nSubscription Type: " + user.getSubscription()
                .getType()
                .name() + "\nBirthday: " + user.getBirthDay() + "\nEmail: " + user.getEmail() + "\nCreditCard: " + user.getCreditCardNumber() + "\nWatched products: " + user
                .getWatchedProducts());
        body.setWrappingWidth(400);
        body.setFont(new Font("Century Gothic", 15));
        body.setFill(Color.WHITE);
        content.setBody(body);
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton btn = new JFXButton("Exit");
        anchorPane.setEffect(blur);
        btn.setButtonType(JFXButton.ButtonType.RAISED);
        btn.getStylesheets().add("/fullpackstylingdark.css");
        btn.setRipplerFill(Color.BLACK);
        btn.setPrefSize(100, 30);
        btn.setOnAction(actionEvent -> {
            dialog.close();
        });
        JFXButton btn2 = new JFXButton("Delete user");
        btn2.setButtonType(JFXButton.ButtonType.RAISED);
        btn2.getStylesheets().add("fullpackstylingdark.css");
        btn.setRipplerFill(Color.BLACK);
        btn2.setOnAction(actionEvent -> {
            UserManager.getInstance().removeFromList(user.getID());
            UserManager.getInstance().serialize("runUsers.bin");
            handleTable();
            labelStatus.setText("User removed!");
            dialog.close();
        });
        content.setActions(btn, btn2);
        dialog.setOnDialogClosed(jfxDialogEvent -> anchorPane.setEffect(null));
        dialog.show();
    }

    private void showProductDetailsDialog(Video video) throws IOException {
        BoxBlur blur = new BoxBlur(3, 3, 3);
        JFXDialogLayout content = new JFXDialogLayout();
        Text heading = new Text("Displaying product details");
        heading.setFill(Color.WHITE);
        heading.setFont(new Font("Century Gothic", 20));
        content.setHeading(heading);
        content.setBackground(new Background(new BackgroundFill(Color.web("#1E1E2E"), CornerRadii.EMPTY,
                Insets.EMPTY)));
        if (video.getClass() == Movie.class) {
            Text text = new Text(String.format("%s %s%n", "Title: ", video.getTitle()) + String.format("%s %s%n",
                    "Rating: ", video
                    .getRating()) + String.format("%s %s%n", "Genre: ", video.getGenre()) + String.format("%s %s%n",
                    "Cast: ", Arrays
                    .toString(((Movie) video).getCast())) + String.format("%s %s%n", "Time available: ",
                    (((Movie) video)
                    .getTimeAvailable() + " days")) + String.format("%s %s%n", "TrailerLink: ",
                    ((Movie) video).getTrailerLink()) + String
                    .format("%s %s%n", "Description: ", video.getDescription()) + String.format("%s %s%n",
                    "CountryOfOrigin: ", video
                    .getCountryOfOrigin()) + String.format("%s %s%n", "Distributor: ", video.getDistributor()) + String.format("%s %s%n", "Price: ", (video
                    .getPrice()
                    .intValue()) + "$"));
            text.setWrappingWidth(400);
            text.setTextAlignment(TextAlignment.LEFT);
            text.setBoundsType(TextBoundsType.LOGICAL);
            text.setFont(new Font("Century Gothic", 15));
            content.setBody(text);
            text.setFill(Color.WHITE);
        } else {
            Text text = new Text(String.format("%s %s%n", "Title: ", video.getTitle()) + String.format("%s %s%n",
                    "Rating: ", video
                    .getRating()) + String.format("%s %s%n", "Genre: ", video.getGenre()) + String.format("%s %s%n",
                    "Seasons: ", ((Series) video)
                    .getListOfSeasons()
                    .size()) + String.format("%s %s%n", "Cast: ", Arrays.toString(((Series) video).getCast())) +
                    //String.format("%-46s %-25s %-20s", "", "Cast: ", Arrays.toString(((Series)video).getCast())) +
                    // "\n" +
                    String.format("%s %s%n", "Description: ", video.getDescription()) + String.format("%s %s%n",
                    "CountryOfOrigin: ", video
                    .getCountryOfOrigin()) + String.format("%s %s%n", "Distributor: ", video.getDistributor()) + String.format("%s %s%n", "Price: ", (video
                    .getPrice()
                    .intValue()) + "$")

            );
            text.setWrappingWidth(400);
            text.setTextAlignment(TextAlignment.LEFT);
            text.setBoundsType(TextBoundsType.LOGICAL);
            text.setFont(new Font("Century Gothic", 15));
            text.setFill(Color.WHITE);
            content.setBody(text);
        }
        anchorPane.setEffect(blur);
        content.setBackground(new Background(new BackgroundFill(Color.web("#1E1E2E"), CornerRadii.EMPTY,
                Insets.EMPTY)));
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton btn = new JFXButton("Exit");
        JFXButton btn2 = new JFXButton("Show chart");
        btn2.setButtonType(JFXButton.ButtonType.RAISED);
        btn2.getStylesheets().add("/fullpackstylingdark.css");
        btn2.setRipplerFill(Color.DARKRED);
        btn.setButtonType(JFXButton.ButtonType.RAISED);
        btn.getStylesheets().add("/fullpackstylingdark.css");
        btn.setRipplerFill(Color.DARKRED);
        btn.setPrefSize(100, 40);
        dialog.setPrefSize(600, 400);
        btn.setOnAction(actionEvent -> {
            dialog.close();
        });
        content.setActions(btn, btn2);
        dialog.setOnDialogClosed(jfxDialogEvent -> anchorPane.setEffect(null));
        dialog.show();

    }

    private void fadeIn() {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(1000));
        fadeTransition.setNode(anchorPane);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

    public void deleteProduct() {
        ProductManager.getInstance()
                .removeFromList(productTreeTableView.getSelectionModel().getSelectedItem().getValue());
        productTreeTableView.getSelectionModel()
                .getSelectedItem()
                .getParent()
                .getChildren()
                .remove(productTreeTableView.getSelectionModel().getSelectedItem());
        ProductManager.getInstance().serialize("runProducts.bin");
        handleTable();
        labelStatus.setText("Product removed!");
    }

    public void deleteUser() {
        UserManager.getInstance()
                .removeFromList(userTreeTableView.getSelectionModel().getSelectedItem().getValue().getID());
        userTreeTableView.getSelectionModel()
                .getSelectedItem()
                .getParent()
                .getChildren()
                .remove(userTreeTableView.getSelectionModel().getSelectedItem());
        UserManager.getInstance().serialize("runUsers.bin");
        handleTable();
        labelStatus.setText("User removed!");

    }

    private void deleteDistributor() {
        Distributor distributor = (Distributor) distributorsTableView.getSelectionModel().getSelectedItem().getValue();
        DistributorManager.getInstance().removeFromList(distributor);
        distributorsTableView.getSelectionModel()
                .getSelectedItem()
                .getParent()
                .getChildren()
                .remove(distributorsTableView.getSelectionModel().getSelectedItem());
        DistributorManager.getInstance().serialize("runDistributors.bin");

        handleTable();
        labelStatus.setText("Distributor removed!");

    }

    public void initializeProductTable() {
        productTreeTableView.setRowFactory(product -> {
            TreeTableRow<Video> productTreeTableRow = new TreeTableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            contextMenu.getStyleClass().add("/contextStyle.css");
            MenuItem editItem = new MenuItem();
            editItem.textProperty().bind(Bindings.format("Edit product"));
            editItem.setOnAction(actionEvent -> {
            });
            MenuItem deleteItem = new MenuItem();
            deleteItem.textProperty().bind(Bindings.format("Delete product"));
            deleteItem.setOnAction(event -> deleteProduct());
            MenuItem showDetails = new MenuItem();
            showDetails.textProperty().bind(Bindings.format("Show details"));
            showDetails.setOnAction(actionEvent -> {
                try {
                    showProductDetailsDialog(productTreeTableView.getSelectionModel().getSelectedItem().getValue());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            contextMenu.getItems().addAll(editItem, deleteItem, showDetails);
            productTreeTableRow.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    productTreeTableRow.setContextMenu(null);
                } else {
                    productTreeTableRow.setContextMenu(contextMenu);
                }
            });
            productTreeTableRow.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2 && !productTreeTableRow.isEmpty()) {
                    try {
                        showProductDetailsDialog(productTreeTableRow.getItem());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            return productTreeTableRow;
        });
        productTreeTableView.setOnKeyReleased(keyEvent -> {
            final TreeItem<Video> selectedItem = productTreeTableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                if (keyEvent.getCode().equals(KeyCode.DELETE)) {
                    deleteProduct();
                }
            }
        });
        productColumn.setMinWidth(100);
        productColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()
                .getValue()
                .getClass()
                .getSimpleName()));
        titleColumn.setMinWidth(250);
        titleColumn.setCellValueFactory(param -> new SimpleStringProperty((param.getValue().getValue().getTitle())));
        ratingColumn.setMinWidth(50);
        ratingColumn.setCellValueFactory(param -> new SimpleStringProperty((param.getValue().getValue().getRating())));
        genreColumn.setMinWidth(100);
        genreColumn.setCellValueFactory(param -> new SimpleStringProperty((param.getValue().getValue().getGenre())));
        countryColumn.setMinWidth(100);
        countryColumn.setCellValueFactory(param -> new SimpleStringProperty((param.getValue()
                .getValue()
                .getCountryOfOrigin())));
        priceColumn.setPrefWidth(50);
        priceColumn.setMinWidth(50);
        priceColumn.setCellValueFactory(param -> new SimpleStringProperty(Integer.toString(param.getValue()
                .getValue()
                .getPrice()
                .intValue())));
        MenuItem item1 = new MenuItem("Show details");
        MenuItem item2 = new MenuItem("Delete");
        userContextMenu.getItems().addAll(item1, item2);
    }

    public void initializeUserTable() {
        userTreeTableView.setOnKeyReleased(keyEvent -> {
            final TreeItem<Person> selectedItem = userTreeTableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                if (keyEvent.getCode().equals(KeyCode.DELETE)) {
                    deleteUser();
                }
            }
        });

        userTreeTableView.setRowFactory(userColumn -> {
            TreeTableRow<Person> userTreeTableRow = new TreeTableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            contextMenu.getStyleClass().add("/contextStyle.css");
            MenuItem editItem = new MenuItem();
            editItem.textProperty().bind(Bindings.format("Edit user"));
            editItem.setOnAction(actionEvent -> {
            });
            MenuItem deleteItem = new MenuItem();
            deleteItem.textProperty().bind(Bindings.format("Delete user"));
            deleteItem.setOnAction(event -> {
                if (event.getEventType().getName().equals("KeyEvent")) {
                    System.out.println("cos");
                }
                deleteUser();
            });
            MenuItem showDetails = new MenuItem();
            showDetails.textProperty().bind(Bindings.format("Show details"));
            showDetails.setOnAction(actionEvent -> {
                showUserDetailsDialog((User) userTreeTableView.getSelectionModel().getSelectedItem().getValue());
            });
            contextMenu.getItems().addAll(editItem, deleteItem, showDetails);
            userTreeTableRow.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    userTreeTableRow.setContextMenu(null);
                } else {
                    userTreeTableRow.setContextMenu(contextMenu);
                }
            });
            userTreeTableRow.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2 && !userTreeTableRow.isEmpty()) {
                    showUserDetailsDialog((User) userTreeTableRow.getItem());
                }
            });

            return userTreeTableRow;
        });
        userColumn.setPrefWidth(150);
        userColumn.setCellValueFactory(param -> new SimpleStringProperty((param.getValue().getValue().getID())));
        subscriptionColumn.setPrefWidth(150);
        subscriptionColumn.setCellValueFactory(param -> new SimpleStringProperty(((User) param.getValue()
                .getValue()).getSubscription().getType().name()));
        birthdayColumn.setPrefWidth(100);
        birthdayColumn.setCellValueFactory(param -> new SimpleStringProperty(((User) param.getValue()
                .getValue()).getBirthDay()));
        emailColumn.setPrefWidth(100);
        emailColumn.setCellValueFactory(param -> new SimpleStringProperty(((User) param.getValue()
                .getValue()).getEmail()));
        creditCardColumn.setPrefWidth(150);
        creditCardColumn.setCellValueFactory(param -> new SimpleStringProperty(((User) param.getValue()
                .getValue()).getCreditCardNumber()));
        MenuItem item1 = new MenuItem("Show details");
        MenuItem item2 = new MenuItem("Delete");
        userContextMenu.getItems().addAll(item1, item2);
    }

    public void initializeDistributorTable() {
        distributorsTableView.setOnKeyReleased(keyEvent -> {
            final TreeItem<Person> selectedItem = distributorsTableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                if (keyEvent.getCode().equals(KeyCode.DELETE)) {
                    deleteDistributor();
                }
            }
        });

        distributorsTableView.setRowFactory(userColumn -> {
            TreeTableRow<Person> distributorTreeTableRow = new TreeTableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            contextMenu.getStyleClass().add("/contextStyle.css");
            MenuItem editItem = new MenuItem();
            editItem.textProperty().bind(Bindings.format("Edit distributor"));
            editItem.setOnAction(actionEvent -> {
            });
            MenuItem deleteItem = new MenuItem();
            deleteItem.textProperty().bind(Bindings.format("Delete distributor"));
            deleteItem.setOnAction(event -> {
                if (event.getEventType().getName().equals("KeyEvent")) {
                    System.out.println("cos");
                }
                deleteDistributor();
            });
            MenuItem showDetails = new MenuItem();
            showDetails.textProperty().bind(Bindings.format("Show details"));
            contextMenu.getItems().addAll(editItem, deleteItem, showDetails);
            distributorTreeTableRow.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    distributorTreeTableRow.setContextMenu(null);
                } else {
                    distributorTreeTableRow.setContextMenu(contextMenu);
                }
            });

            return distributorTreeTableRow;
        });
        distNameColumn.setPrefWidth(150);
        distNameColumn.setCellValueFactory(param -> new SimpleStringProperty(((Distributor) param.getValue().getValue())
                .getName()));
        bankAccountColumn.setPrefWidth(150);
        bankAccountColumn.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(((Distributor) param.getValue()
                .getValue()).getBankAccount().longValue())));
        watchCountColumn.setPrefWidth(150);
        watchCountColumn.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(((Distributor) param.getValue()
                .getValue()).getWatchCount())));
        watchPriceColumn.setPrefWidth(150);
        watchPriceColumn.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(((Distributor) param.getValue()
                .getValue()).getWatchPrice())));
        MenuItem item1 = new MenuItem("Show details");
        MenuItem item2 = new MenuItem("Delete");
        userContextMenu.getItems().addAll(item1, item2);
    }

    public void initializeTables() {
        users.clear();
        users.addAll(UserManager.getInstance().getUserList().values());
        videos.clear();
        videos.addAll(ProductManager.getInstance().getProductList());
        distributors.clear();
        distributors.addAll(DistributorManager.getInstance().getDistributors());
        final TreeItem<Person> root = new RecursiveTreeItem<>(users, RecursiveTreeObject::getChildren);
        final TreeItem<Video> root2 = new RecursiveTreeItem<>(videos, RecursiveTreeObject::getChildren);
        final TreeItem<Person> root3 = new RecursiveTreeItem<>(distributors, RecursiveTreeObject::getChildren);
        ObservableList<TreeTableColumn<Person, ?>> columns = userTreeTableView.getColumns();
        columns.setAll(userColumn, subscriptionColumn, birthdayColumn, emailColumn, creditCardColumn);
        userTreeTableView.setRoot(root);
        userTreeTableView.setShowRoot(false);
        userSearchInput.textProperty()
                .addListener((obs, oldValue, newValue) -> userTreeTableView.setPredicate(userTreeItem -> ((User) userTreeItem
                        .getValue()).contains(newValue)));
        productTreeTableView.getColumns()
                .setAll(productColumn, titleColumn, genreColumn, ratingColumn, countryColumn, priceColumn);
        productTreeTableView.setRoot(root2);
        productTreeTableView.setShowRoot(false);
        productSearchInput.textProperty()
                .addListener((obs, oldValue, newValue) -> productTreeTableView.setPredicate(videoTreeItem -> videoTreeItem
                        .getValue()
                        .contains(newValue)));
        distributorsTableView.getColumns()
                .setAll(distNameColumn, bankAccountColumn, watchCountColumn, watchPriceColumn);
        distributorsTableView.setRoot(root3);
        distributorsTableView.setShowRoot(false);
    }

    public void addProgressListener() {
        usersGenerated.textProperty().addListener(((observableValue, s, t1) -> {
            if (!t1.isEmpty()) {
                try {
                    Integer.parseInt(t1);
                    userWarning.setVisible(false);
                    if (userWarning.isVisible() || productWarning.isVisible() || productsGenerated.getText()
                            .isEmpty()) {
                        buttonInitialize.setDisable(true);
                    } else buttonInitialize.setDisable(false);
                } catch (NumberFormatException ex) {
                    userWarning.setVisible(true);
                    if (userWarning.isVisible() || productWarning.isVisible()) {
                        buttonInitialize.setDisable(true);
                    } else buttonInitialize.setDisable(false);
                }
            } else userWarning.setVisible(true);
        }));
        productsGenerated.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.isEmpty()) {
                try {
                    Integer.parseInt(t1);
                    productWarning.setVisible(false);
                    if (userWarning.isVisible() || productWarning.isVisible() || usersGenerated.getText().isEmpty()) {
                        buttonInitialize.setDisable(true);
                    } else buttonInitialize.setDisable(false);
                } catch (NumberFormatException ex) {
                    productWarning.setVisible(true);
                    if (userWarning.isVisible() || productWarning.isVisible()) {
                        buttonInitialize.setDisable(true);
                    } else buttonInitialize.setDisable(false);
                }
            } else productWarning.setVisible(true);

        });
        myNum.setNumber(0);
        myNum.numberProperty().addListener((observableValue, number, t1) -> {
            timeProgress.progressProperty().bind(myNum.numberProperty());
        });
        timeProgress.progressProperty().bind(new SimpleDoubleProperty(Simulation.getInstance().getTime()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(600), actionEvent -> {
            myNum.setNumber(Simulation.getInstance().getTime() / 360);
            budgetLabel.setText(NetflixBudgetManager.getBudget().intValue() + "$");
            timeElapsedLabel.setText(Simulation.getInstance().getCurrentTime() + "th day");
            handleTable();
            if (myNum.getNumber() >= 1 || Simulation.getInstance().getLossCount() > 2) {
                Simulation.getInstance().endSimulation();
                spinner.setVisible(false);
                simulationDone.setVisible(true);
                simulationToggle.setSelected(false);
                timeline.stop();
            }
        });
        timeline.getKeyFrames().add(keyFrame);
    }

    @FXML
    public void cancel() {
        new SlideOutLeft(AddUserPane).play();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //anchorPane.setOpacity(0);
        //fadeIn();
        Tooltip tooltip = new Tooltip("Search by email, subscription and watched products");
        tooltip.setShowDelay(Duration.millis(300));
        userSearchInput.setTooltip(tooltip);
        Tooltip tooltip2 = new Tooltip("Search by genre or title");
        tooltip2.setShowDelay(Duration.millis(300));
        productSearchInput.setTooltip(tooltip2);
        stackPane.setBackground(Background.EMPTY);
        new ZoomInDown(anchorPane).play();
        initializeUserTable();
        initializeProductTable();
        initializeDistributorTable();
        initializeTables();
        addProgressListener();
        Timeline dateTimeline = new Timeline();
        dateTimeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame frame = new KeyFrame(Duration.millis(1000),
                actionEvent -> date.setText(new SimpleDateFormat("yyyy" + "-MM-dd HH:mm:ss")
                .format(new Date())));
        dateTimeline.getKeyFrames().add(frame);
        dateTimeline.playFromStart();
    }

    class MyNumber {
        private DoubleProperty number;

        public final double getNumber() {
            if (number != null) {
                return number.get();
            }
            return 0;
        }

        public final void setNumber(double number) {
            this.numberProperty().set(number);
        }

        public final DoubleProperty numberProperty() {
            if (number == null) {
                number = new SimpleDoubleProperty(0);
            }
            return number;
        }
    }
}