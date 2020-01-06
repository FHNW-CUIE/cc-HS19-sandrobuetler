package cuie.sandroBuetler_andreasRohr.template_businesscontrol;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.regex.Pattern;

import javafx.animation.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.PseudoClass;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Popup;
import javafx.util.Duration;

public class Liftboy extends Control {
    private static final PseudoClass INVALID_CLASS   = PseudoClass.getPseudoClass("invalid");

    static final String FORMATTED_INTEGER_PATTERN = "%,d";

    private static final String INTEGER_REGEX    = "[+-]?[\\d']{1,14}";
    private static final Pattern INTEGER_PATTERN = Pattern.compile(INTEGER_REGEX);

    private final IntegerProperty value = new SimpleIntegerProperty();
    private final StringProperty userFacingText = new SimpleStringProperty();

    private final BooleanProperty invalid = new SimpleBooleanProperty(false) {
        @Override
        protected void invalidated() {
            pseudoClassStateChanged(INVALID_CLASS, get());
        }
    };

    private final StringProperty  label        = new SimpleStringProperty();
    private final StringProperty  errorMessage = new SimpleStringProperty();


    public Liftboy() {
        initializeSelf();
        addValueChangeListener();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new LiftboySkin(this);
    }

    public void increase() {
        setValue(getValue() + 1);
    }

    public void decrease() {
        setValue(getValue() - 1);
    }

    private void initializeSelf() {
         getStyleClass().add("liftboy");

         setUserFacingText(convertToString(getValue()));
    }

    private void addValueChangeListener() {
        userFacingText.addListener((observable, oldValue, userInput) -> {
            if (isInteger(userInput)) {
                setInvalid(false);
                setErrorMessage(null);
                setValue(convertToInt(userInput));
            } else {
                setInvalid(true);
                setErrorMessage("Nur Zahlen erlaubt!");
            }
        });

        valueProperty().addListener((observable, oldValue, newValue) -> {
            setInvalid(false);
            setErrorMessage(null);
            setUserFacingText(convertToString(newValue.intValue()));
        });
    }

    private boolean isInteger(String userInput) {
        return INTEGER_PATTERN.matcher(userInput).matches();
    }

    private int convertToInt(String userInput) {
        return Integer.parseInt(userInput);
    }

    private String convertToString(int newValue) {
        return String.format(FORMATTED_INTEGER_PATTERN, newValue);
    }

    // all the getters and setters

    public int getValue() {
        return value.get();
    }

    public IntegerProperty valueProperty() {
        return value;
    }

    public void setValue(int value) {
        this.value.set(value);
    }

    public StringProperty labelProperty() {
        return label;
    }

    public BooleanProperty invalidProperty() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid.set(invalid);
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage.set(errorMessage);
    }

    public StringProperty userFacingTextProperty() {
        return userFacingText;
    }

    public void setUserFacingText(String userFacingText) {
        this.userFacingText.set(userFacingText);
    }
}

class LiftboySkin extends SkinBase<Liftboy> {
    private static final int IMG_SIZE   = 12;
    private static final int IMG_OFFSET = 4;

    private static final String ANGLE_DOWN = "\uf107";
    private static final String ANGLE_UP   = "\uf106";

    private enum State {
        VALID("Valid",      "valid.png"),
        INVALID("Invalid",  "invalid.png");

        public final String    text;
        public final ImageView imageView;

        State(final String text, final String file) {
            this.text = text;
            String url = LiftboySkin.class.getResource("/icons/" + file).toExternalForm();
            this.imageView = new ImageView(new Image(url,
                    IMG_SIZE, IMG_SIZE,
                    true, false));
        }
    }

    private static final String FONTS_CSS = "/fonts/fonts.css";
    private static final String STYLE_CSS = "style.css";

    // all parts
    private TextField editableNode;
    private Popup popup;
    private Pane dropDownChooser;
    private Button chooserButton;

    private StackPane drawingPane;

    private Animation invalidInputAnimation;
    private FadeTransition fadeOutValidIconAnimation;


    LiftboySkin(Liftboy control) {
        super(control);
        initializeSelf();
        initializeParts();
        layoutParts();
        setupAnimations();
        setupEventHandlers();
        setupValueChangedListeners();
        setupBindings();
    }

    private void initializeSelf() {
        String fonts = getClass().getResource(FONTS_CSS).toExternalForm();
        getSkinnable().getStylesheets().add(fonts);

        String stylesheet = getClass().getResource(STYLE_CSS).toExternalForm();
        getSkinnable().getStylesheets().add(stylesheet);
    }

    private void initializeParts() {
        editableNode = new TextField();
        editableNode.promptTextProperty().setValue("Stockwerk eingeben!");
        editableNode.getStyleClass().add("editable-node");

        State.VALID.imageView.setOpacity(0.0);

        chooserButton = new Button(ANGLE_DOWN);
        chooserButton.getStyleClass().add("chooser-button");

        dropDownChooser = new LiftboyDropDown(getSkinnable());

        popup = new Popup();
        popup.getContent().addAll(dropDownChooser);

        drawingPane = new StackPane();
        drawingPane.getStyleClass().add("drawing-pane");
    }

    private void layoutParts() {
        StackPane.setAlignment(chooserButton, Pos.CENTER_RIGHT);
        drawingPane.getChildren().addAll(editableNode, chooserButton);

        Arrays.stream(State.values())
                .map(state -> state.imageView)
                .forEach(imageView -> {
                    imageView.setManaged(false);
                    drawingPane.getChildren().add(imageView);
                });

        StackPane.setAlignment(editableNode, Pos.CENTER_LEFT);
        getChildren().add(drawingPane);
    }

    private void setupAnimations() {
        int      delta    = 5;
        Duration duration = Duration.millis(30);

        TranslateTransition moveRight = new TranslateTransition(duration, editableNode);
        moveRight.setFromX(0.0);
        moveRight.setByX(delta);
        moveRight.setAutoReverse(true);
        moveRight.setCycleCount(2);
        moveRight.setInterpolator(Interpolator.LINEAR);

        TranslateTransition moveLeft = new TranslateTransition(duration, editableNode);
        moveLeft.setFromX(0.0);
        moveLeft.setByX(-delta);
        moveLeft.setAutoReverse(true);
        moveLeft.setCycleCount(2);
        moveLeft.setInterpolator(Interpolator.LINEAR);

        invalidInputAnimation = new SequentialTransition(moveRight, moveLeft);
        invalidInputAnimation.setCycleCount(3);

        fadeOutValidIconAnimation = new FadeTransition(Duration.millis(500), State.VALID.imageView);
        fadeOutValidIconAnimation.setDelay(Duration.seconds(1));
        fadeOutValidIconAnimation.setFromValue(1.0);
        fadeOutValidIconAnimation.setToValue(0.0);
    }

    private void setupEventHandlers() {
        chooserButton.setOnAction(event -> {
            if (popup.isShowing()) {
                popup.hide();
            } else {
                popup.show(editableNode.getScene().getWindow());
            }
        });

        popup.setOnHidden(event -> chooserButton.setText(ANGLE_DOWN));

        popup.setOnShown(event -> {
            chooserButton.setText(ANGLE_UP);
            Point2D location = editableNode.localToScreen(editableNode.getWidth() - dropDownChooser.getPrefWidth() - 3,
                    editableNode.getHeight() -3);

            popup.setX(location.getX());
            popup.setY(location.getY());
        });

        editableNode.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    getSkinnable().increase();
                    event.consume();
                    break;
                case DOWN:
                    getSkinnable().decrease();
                    event.consume();
                    break;
            }
        });
    }

    private void setupValueChangedListeners() {
        getSkinnable().invalidProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                startInvalidInputAnimation();
            } else {
                State.VALID.imageView.setOpacity(1.0);
                startFadeOutValidIconTransition();
            }
        });
    }

    private void setupBindings() {
        editableNode.textProperty().bindBidirectional(getSkinnable().userFacingTextProperty());
        State.INVALID.imageView.visibleProperty().bind(getSkinnable().invalidProperty());

        State.INVALID.imageView.xProperty().bind(editableNode.translateXProperty().add(editableNode.layoutXProperty()).subtract(IMG_OFFSET));
        State.INVALID.imageView.yProperty().bind(editableNode.translateYProperty().add(editableNode.layoutYProperty()).subtract(IMG_OFFSET));
        State.VALID.imageView.xProperty().bind(editableNode.layoutXProperty().subtract(IMG_OFFSET));
        State.VALID.imageView.yProperty().bind(editableNode.layoutYProperty().subtract(IMG_OFFSET));
    }

    private void startFadeOutValidIconTransition() {
        if (fadeOutValidIconAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            return;
        }
        fadeOutValidIconAnimation.play();
    }

    private void startInvalidInputAnimation() {
        if (invalidInputAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            invalidInputAnimation.stop();
        }
        invalidInputAnimation.play();
    }
}

class LiftboyDropDown extends BorderPane {
    private static final String FONTS_CSS = "/fonts/fonts.css";
    private static final String STYLE_CSS = "style.css";

    private final Liftboy liftboy;


    private VBox terminal = new VBox();
    private HBox firstRow = new HBox();
    private HBox secondRow = new HBox();
    private HBox thirdRow = new HBox();
    private HBox fourthRow = new HBox();


    private Image elevatorBoy;
    private ImageView elevatorBoyView;



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


    LiftboyDropDown(Liftboy liftboy) {
        this.liftboy = liftboy;
        initializeSelf();
        initializeParts();
        layoutParts();
        setupEventHandlers();
        setupBindings();
    }

    private void initializeSelf() {
        getStyleClass().add("liftboy-drop-down-chooser");



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

        elevatorBoy = new Image("pictures/elevatorBoy.png");
        elevatorBoyView = new ImageView(elevatorBoy);




        floorLabel.getStyleClass().add("floor-label");
        nameLabel.getStyleClass().add("name-label");

        zeroButton.getStyleClass().add("button");
        oneButton.getStyleClass().add("button");
        twoButton.getStyleClass().add("button");
        threeButton.getStyleClass().add("button");
        fourButton.getStyleClass().add("button");
        fiveButton.getStyleClass().add("button");
        sixButton.getStyleClass().add("button");
        sevenButton.getStyleClass().add("button");
        eightButton.getStyleClass().add("button");
        nineButton.getStyleClass().add("button");
        clearButton.getStyleClass().add("button");
        alarmButton.getStyleClass().add("button-alarm");

        terminal.setSpacing(10);
        firstRow.setSpacing(10);
        secondRow.setSpacing(10);
        thirdRow.setSpacing(10);
        fourthRow.setSpacing(10);

//        nameLabel.setMinWidth();

    }

    private void layoutParts() {


        firstRow.getChildren().addAll(sevenButton, eightButton, nineButton);
        secondRow.getChildren().addAll(fourButton, fiveButton, sixButton);
        thirdRow.getChildren().addAll(oneButton, twoButton, threeButton);
        fourthRow.getChildren().addAll(alarmButton, zeroButton, clearButton);

        terminal.getChildren().addAll(floorLabel, firstRow, secondRow, thirdRow, fourthRow);

        setTop(nameLabel);
        setLeft(elevatorBoyView);
        setRight(terminal);

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
            liftboy.userFacingTextProperty().setValue("0");
        });


        alarmButton.setOnAction(event -> {

            String musicFile = "src/main/resources/music/elevatorMusic.wav";

            Media sound = new Media(new File(musicFile).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();


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
        floorLabel.textProperty().bind(liftboy.userFacingTextProperty());
        nameLabel.textProperty().bind(liftboy.labelProperty());

    }


    private void buttonPush(String number){
        if (liftboy.userFacingTextProperty().getValue() =="0"){
            liftboy.userFacingTextProperty().setValue(number);
        }else {
            liftboy.userFacingTextProperty().setValue(liftboy.userFacingTextProperty().getValue() + number);
        }
    }
}
