package cuie.sandroBuetler_andreasRohr.template_businesscontrol;

import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


class
DropDownChooser extends BorderPane {
    private static final String FONTS_CSS = "/fonts/fonts.css";
    private static final String STYLE_CSS = "dropDownChooser.css";

    private final BusinessControl businessControl;


    private VBox terminal = new VBox();
    private HBox firstRow = new HBox();
    private HBox secondRow = new HBox();
    private HBox thirdRow = new HBox();
    private HBox fourthRow = new HBox();


    private Image liftboy;
    private ImageView liftboyView;


    private Label nameLabel;
    private Label floorLabel;
    private Button zeroButton;
    private Button oneButton;
    private Button twoButton;
    private Button threeButton;
    private Button fourButton;
    private Button fiveButton;
    private Button sixButton;
    private Button sevenButton;
    private Button eightButton;
    private Button nineButton;
    private Button alarmButton;
    private Button clearButton;





    DropDownChooser(BusinessControl businessControl) {
        this.businessControl = businessControl;
        initializeSelf();
        initializeParts();
        layoutParts();
        setupEventHandlers();
        setupBindings();
    }

    private void initializeSelf() {
        getStyleClass().add("drop-down-chooser");

        String fonts = getClass().getResource(FONTS_CSS).toExternalForm();
        getStylesheets().add(fonts);

        String stylesheet = getClass().getResource(STYLE_CSS).toExternalForm();
        getStylesheets().add(stylesheet);
    }

    private void initializeParts() {
        nameLabel = new Label("name");
        floorLabel = new Label("0");
        zeroButton = new Button("0");
        oneButton = new Button("1");
        twoButton = new Button("2");
        threeButton = new Button("3");
        fourButton = new Button("4");
        fiveButton = new Button("5");
        sixButton = new Button("6");
        sevenButton = new Button("7");
        eightButton = new Button("8");
        nineButton = new Button("9");
        alarmButton = new Button();
        clearButton = new Button("C");

        liftboy = new Image("pictures/liftboy.jpg");
        liftboyView = new ImageView(liftboy);

    }

    private void layoutParts() {




        firstRow.getChildren().addAll(sevenButton, eightButton, nineButton);
        secondRow.getChildren().addAll(fourButton, fiveButton, sixButton);
        thirdRow.getChildren().addAll(oneButton, twoButton, threeButton);
        fourthRow.getChildren().addAll(alarmButton, zeroButton, clearButton);



        terminal.getChildren().addAll(nameLabel, floorLabel, firstRow, secondRow, thirdRow, fourthRow);


        setLeft(liftboyView);
        setRight(terminal);

        //getChildren().addAll(terminal);
    }


    private void setupEventHandlers(){
        oneButton.setOnAction(event -> {
            buttonPush("1");
        });

        twoButton.setOnAction(event -> {
            buttonPush("2");
        });

        threeButton.setOnAction(event -> {
            buttonPush("3");
        });


        fourButton.setOnAction(event -> {
            buttonPush("4");
        });

        fiveButton.setOnAction(event -> {
            buttonPush("5");
        });

        sixButton.setOnAction(event -> {
            buttonPush("6");
        });

        sevenButton.setOnAction(event -> {
            buttonPush("7");
        });

        eightButton.setOnAction(event -> {
            buttonPush("8");
        });

        nineButton.setOnAction(event -> {
            buttonPush("9");
        });

        zeroButton.setOnAction(event -> {
            buttonPush("0");
        });


        clearButton.setOnAction(event -> {
            businessControl.userFacingTextProperty().setValue("0");
        });


        alarmButton.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.schindler.com/ch/internet/de/ueber-schindler-schweiz/kontakt.html#button"));
            } catch (IOException e1){
                e1.printStackTrace();
            }catch (URISyntaxException e2){
                e2.printStackTrace();
            }

        });
    }


    private void setupBindings() {
        floorLabel.textProperty().bind(businessControl.userFacingTextProperty());
    }


    private void buttonPush(String number){
        if (businessControl.userFacingTextProperty().getValue() =="0"){
            businessControl.userFacingTextProperty().setValue(number);
        }else {
            businessControl.userFacingTextProperty().setValue(businessControl.userFacingTextProperty().getValue() + number);
        }
    }
}