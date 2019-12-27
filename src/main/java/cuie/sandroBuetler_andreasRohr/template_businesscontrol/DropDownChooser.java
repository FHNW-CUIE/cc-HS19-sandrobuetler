package cuie.sandroBuetler_andreasRohr.template_businesscontrol;


import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;


class DropDownChooser extends VBox {
    private static final String FONTS_CSS = "/fonts/fonts.css";
    private static final String STYLE_CSS = "dropDownChooser.css";

    private final BusinessControl businessControl;

    private Label tobeReplacedLabel;

    private Image liftboy;
    private ImageView boy;


    DropDownChooser(BusinessControl businessControl) {
        this.businessControl = businessControl;
        initializeSelf();
        initializeParts();
        layoutParts();
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
        tobeReplacedLabel = new Label("to be replaced");
        liftboy = new Image("pictures/liftboy.jpg");
        boy = new ImageView(liftboy);


    }

    private void layoutParts() {
        getChildren().addAll(tobeReplacedLabel, boy);
    }

    private void setupBindings() {
    }
}
