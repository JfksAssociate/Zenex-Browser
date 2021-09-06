package sample;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Optional;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.show();
    }
    //Custom function for creation of New Tabs.
    private Tab createAndSelectNewTab(final TabPane tabPane, final String title) {
        Tab tab = new Tab(title);
        Label aboutLabel = new Label();
        aboutLabel.setText("\n\n\t\tThe Zenex Browser.\n\n\t\t\tA Javafx Browser" +
                "\n\t\t\tTo start browsing, click on +");
        aboutLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
        tab.setContent(aboutLabel);
        final ObservableList<Tab> tabs = tabPane.getTabs();
        tab.closableProperty().bind(Bindings.size(tabs).greaterThan(2));
        tabs.add(tabs.size() - 1, tab);
        tabPane.getSelectionModel().select(tab);
        return tab;
    }
    //Initialization function of the program.
    private void init(Stage primaryStage) {
        Image image = new Image("/ico.png");
        primaryStage.setTitle("Zenex Browser");
        Group root = new Group();
        primaryStage.setScene(new Scene(root));
        primaryStage.getIcons().add(image);
        //To enable Full Screen mode - By Default.
        //primaryStage.setFullScreen(true);
        primaryStage.setMaximized(true);
        BorderPane borderPane = new BorderPane();
        final TabPane tabPane = new TabPane();
        //Preferred Size of TabPane.
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        tabPane.setPrefSize(screenWidth, screenHeight);
        //Placement of TabPane.
        tabPane.setSide(Side.TOP);
        /* To disable closing of tabs.
         * tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);*/
        final Tab newtab = new Tab();
        newtab.setText("+");
        newtab.setClosable(false);
        //Addition of New Tab to the tabpane.
        tabPane.getTabs().addAll(newtab);
        createAndSelectNewTab(tabPane, "main");
        //Function to add and display new tabs with default URL display.
        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable,
                                Tab oldSelectedTab, Tab newSelectedTab) {
                if (newSelectedTab == newtab) {
                    Tab tab = new Tab();

                    //WebView - to display, browse web pages.
                    WebView webView = new WebView();
                    final WebEngine webEngine = webView.getEngine();
                    webEngine.setJavaScriptEnabled(true);
                    webEngine.executeScript("location.reload(true);");
                    String title = webEngine.getTitle();
                    if (title != null) {
                        tab.setText(title);
                    }
                    else {
                        tab.setText("New Tab");
                    }
                    webEngine.load("https://JavaSearch.shared221.repl.co");
                    final TextField urlField = new TextField("Search...");
                    webEngine.locationProperty().addListener(new ChangeListener<String>() {
                        @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                            urlField.setText(newValue);
                        }
                    });
                    //Action definition for the Button Go.
                    EventHandler<ActionEvent> goAction = new EventHandler<ActionEvent>() {
                        @Override public void handle(ActionEvent event) {
                            webEngine.load(urlField.getText().startsWith("http://")
                                    ? urlField.getText()
                                    : "https://www.google.com/search?q=" + urlField.getText());
                        }
                    };
                    urlField.setOnAction(goAction);
                    Button goButton = new Button("Go");
                    goButton.setDefaultButton(true);
                    goButton.setOnAction(goAction);
                    // Layout logic
                    HBox hBox = new HBox(5);
                    hBox.getChildren().setAll(urlField, goButton);
                    HBox.setHgrow(urlField, Priority.ALWAYS);
                    final VBox vBox = new VBox(5);
                    vBox.getChildren().setAll(hBox, webView);
                    VBox.setVgrow(webView, Priority.ALWAYS);
                    tab.setContent(vBox);
                    final ObservableList<Tab> tabs = tabPane.getTabs();
                    tab.closableProperty().bind(Bindings.size(tabs).greaterThan(2));
                    tabs.add(tabs.size() - 1, tab);
                    tabPane.getSelectionModel().select(tab);
                }
            }
        });
        borderPane.setCenter(tabPane);
        root.getChildren().add(borderPane);
    }
    public static void main(String args[]){
        launch(args);
    }

}
