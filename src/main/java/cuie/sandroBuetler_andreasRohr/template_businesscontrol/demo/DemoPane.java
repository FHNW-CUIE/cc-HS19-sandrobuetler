package cuie.sandroBuetler_andreasRohr.template_businesscontrol.demo;

import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import cuie.sandroBuetler_andreasRohr.template_businesscontrol.liftboy.Liftboy;

class DemoPane extends BorderPane {
    private Liftboy liftboy;

    private Slider stockwerkSlider;

    private CheckBox  readOnlyBox;
    private CheckBox  mandatoryBox;
    private TextField buildingName;

    private PresentationModel model;

    DemoPane(PresentationModel model) {
        this.model = model;

        initializeControls();
        layoutControls();
        setupValueChangeListeners();
        setupBindings();
    }

    private void initializeControls() {
        setPadding(new Insets(10));

        liftboy = new Liftboy();

        stockwerkSlider = new Slider(0, 130, 0);

        buildingName = new TextField();
    }

    private void layoutControls() {
        setCenter(liftboy);
        VBox box = new VBox(10,
                            new Label("Business Control Properties"),
                            new Label("Stockwerk")      , stockwerkSlider,
                            new Label("Hausname")    , buildingName);
        box.setPadding(new Insets(10));
        box.setSpacing(10);
        setRight(box);
    }

    private void setupValueChangeListeners() {
    }

    private void setupBindings() {
        stockwerkSlider.valueProperty()      .bindBidirectional(model.ageProperty());
        buildingName.textProperty()      .bindBidirectional(model.age_LabelProperty());

        liftboy.stockwerkProperty()    .bindBidirectional(model.ageProperty());
        liftboy.buildingNameProperty()    .bind(model.age_LabelProperty());
    }

}
