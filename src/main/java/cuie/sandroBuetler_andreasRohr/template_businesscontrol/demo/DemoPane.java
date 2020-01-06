package cuie.sandroBuetler_andreasRohr.template_businesscontrol.demo;

import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import cuie.sandroBuetler_andreasRohr.template_businesscontrol.Liftboy;

class DemoPane extends BorderPane {
    private Liftboy liftboy;

    private Slider ageSlider;

    private CheckBox  readOnlyBox;
    private CheckBox  mandatoryBox;
    private TextField labelField;

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

        ageSlider = new Slider(0, 130, 0);

        readOnlyBox = new CheckBox();
        readOnlyBox.setSelected(false);

        mandatoryBox = new CheckBox();
        mandatoryBox.setSelected(true);

        labelField = new TextField();
    }

    private void layoutControls() {
        setCenter(liftboy);
        VBox box = new VBox(10,
                            new Label("Business Control Properties"),
                            new Label("Age")      , ageSlider,
                            new Label("readOnly") , readOnlyBox,
                            new Label("mandatory"), mandatoryBox,
                            new Label("Label")    , labelField);
        box.setPadding(new Insets(10));
        box.setSpacing(10);
        setRight(box);
    }

    private void setupValueChangeListeners() {
    }

    private void setupBindings() {
        ageSlider.valueProperty()      .bindBidirectional(model.ageProperty());
        labelField.textProperty()      .bindBidirectional(model.age_LabelProperty());
        readOnlyBox.selectedProperty() .bindBidirectional(model.age_readOnlyProperty());
        mandatoryBox.selectedProperty().bindBidirectional(model.age_mandatoryProperty());


        liftboy.valueProperty()    .bindBidirectional(model.ageProperty());
        liftboy.labelProperty()    .bind(model.age_LabelProperty());
    }

}
