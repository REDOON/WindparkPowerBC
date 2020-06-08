package cuie.project.bcwindparkfx;

import java.util.regex.Pattern;

import javafx.beans.property.*;
import javafx.css.PseudoClass;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.text.Font;
import org.apache.commons.validator.routines.DoubleValidator;

public class PowerValueControl extends Control {
    private static final PseudoClass MANDATORY_CLASS = PseudoClass.getPseudoClass("mandatory");
    private static final PseudoClass INVALID_CLASS   = PseudoClass.getPseudoClass("invalid");
    private static final PseudoClass EDITABLE_CLASS   = PseudoClass.getPseudoClass("editable");
    private static final PseudoClass CONVERTIBLE_CLASS   = PseudoClass.getPseudoClass("convertible");

    private static final String STRING_REGEX = "average|Full|full|FULL|Minimum|minimum|MINIMUM|MIN|min|Min|Average|AVERAGE|Null|null|NULL";
    private static final Pattern STRING_PATTERN = Pattern.compile(STRING_REGEX);

    private final Double MAX_VALUE;
    private final Double MIN_VALUE;

    private final StringProperty  userFacingTextOne = new SimpleStringProperty();
    private final StringProperty  userFacingTextTwo = new SimpleStringProperty();
    private final StringProperty  userFacingTextThree = new SimpleStringProperty();
    private final StringProperty  userFacingTextFour = new SimpleStringProperty();
    private final DoubleProperty  powerValueOne = new SimpleDoubleProperty();
    private final DoubleProperty  powerValueTwo = new SimpleDoubleProperty();
    private final DoubleProperty  powerValueThree = new SimpleDoubleProperty();
    private final DoubleProperty  powerValueFour = new SimpleDoubleProperty();

    private final IntegerProperty value = new SimpleIntegerProperty();
    private final StringProperty userFacingText = new SimpleStringProperty();

    private final BooleanProperty mandatory = new SimpleBooleanProperty() {
        @Override
        protected void invalidated() {
            pseudoClassStateChanged(MANDATORY_CLASS, get());
        }
    };

    private final BooleanProperty invalid = new SimpleBooleanProperty(false) {
        @Override
        protected void invalidated() {
            pseudoClassStateChanged(INVALID_CLASS, get());
        }
    };

    private final BooleanProperty convertible = new SimpleBooleanProperty(false){
        @Override
        protected void invalidated() {
            pseudoClassStateChanged(CONVERTIBLE_CLASS, get());
        }
    };
    private final BooleanProperty readOnly     = new SimpleBooleanProperty(false){
         @Override
         protected void invalidated() {
             pseudoClassStateChanged(EDITABLE_CLASS, get());
         }
    };
    private final StringProperty  label        = new SimpleStringProperty();
    private final StringProperty  errorMessage = new SimpleStringProperty();


    public PowerValueControl(Double max, Double min) {
        initializeSelf();
        addValueChangeListener();
        MAX_VALUE = max;
        MIN_VALUE = min;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new BusinessSkin(this);
    }

    private void initializeSelf() {
         getStyleClass().add("business-control");

         setUserFacingText(convertToString(getValue()));
    }


    private void addValueChangeListener() {
        userFacingTextOne.addListener((observable, oldValue, newValue) -> {validateAndSetPowerValue(newValue, 1);});
        userFacingTextTwo.addListener((observable, oldValue, newValue) -> {validateAndSetPowerValue(newValue, 2);});
        userFacingTextThree.addListener((observable, oldValue, newValue) -> {validateAndSetPowerValue(newValue, 3);});
        userFacingTextFour.addListener((observable, oldValue, newValue) -> {validateAndSetPowerValue(newValue, 4);});

        powerValueOneProperty().addListener((observable, oldValue, newValue) -> {
            setInvalid(false);
            setErrorMessage(null);
            setUserFacingTextOne(convertToString(newValue.doubleValue()));
        });

        powerValueTwoProperty().addListener((observable, oldValue, newValue) -> {
            setInvalid(false);
            setErrorMessage(null);
            setUserFacingTextTwo(convertToString(newValue.doubleValue()));
        });

        powerValueThreeProperty().addListener((observable, oldValue, newValue) -> {
            setInvalid(false);
            setErrorMessage(null);
            setUserFacingTextThree(convertToString(newValue.doubleValue()));
        });

        powerValueFourProperty().addListener((observable, oldValue, newValue) -> {
            setInvalid(false);
            setErrorMessage(null);
            setUserFacingTextFour(convertToString(newValue.doubleValue()));
        });

        powerValueOne.addListener((observable, oldValue, newValue) -> {
            String s = newValue.toString();
            setUserFacingTextOne(s);
        });

        powerValueTwo.addListener((observable, oldValue, newValue) -> {
            String s = newValue.toString();
            setUserFacingTextTwo(s);
        });

        powerValueThree.addListener((observable, oldValue, newValue) -> {
            String s = newValue.toString();
            setUserFacingTextThree(s);
        });

        powerValueFour.addListener((observable, oldValue, newValue) -> {
            String s = newValue.toString();
            setUserFacingTextFour(s);
        });
    }

    private void validateAndSetPowerValue(String s, int x){
        if(STRING_PATTERN.matcher(s).matches()){
            convert(s, x);
            setInvalid(false);
            setConvertible(true);

        }else if(DoubleValidator.getInstance().isValid(s)){
            double d = DoubleValidator.getInstance().validate(s);
            if(s.length() > MAX_VALUE.toString().length()+2) {
                setInvalid(true);
                setErrorMessage("Number to big");
            }else if(DoubleValidator.getInstance().isInRange(d, MIN_VALUE.doubleValue(), MAX_VALUE.doubleValue())){
                setInvalid(false);
                setErrorMessage(null);
                setUserFacingText(d, x);
            }else{
                setInvalid(true);
                setErrorMessage("Number to big");
            }
        }else if(s.isEmpty()&&!isMandatory()){
            setInvalid(false);
            setErrorMessage(null);
            setUserFacingText(MIN_VALUE, x);
        }else {
            setInvalid(true);
            setErrorMessage(null);
        }
    }

    public void reset(){
        setUserFacingTextOne(String.valueOf(getPowerValueOne()));
        setUserFacingTextTwo(String.valueOf(getPowerValueTwo()));
        setUserFacingTextThree(String.valueOf(getPowerValueThree()));
        setUserFacingTextFour(String.valueOf(getPowerValueFour()));
        setConvertible(false);
    }

    private void setUserFacingText(Double s, int x){
        switch (x){
            case 1: setPowerValueOne(s);
                break;
            case 2: setPowerValueTwo(s);
                break;
            case 3: setPowerValueThree(s);
                break;
            case 4: setPowerValueFour(s);
                break;
            default:
                break;
        }
    }

    public void convert(String s, int x){
        String input = s.toLowerCase();

        switch (input){
            case "full": setUserFacingText(MAX_VALUE,x);
                break;
            case "minimum": setUserFacingText(MIN_VALUE,x);
                break;
            case "min": setUserFacingText(MIN_VALUE,x);
                break;
            case "average": setUserFacingText(getAverageOfPowerValues(),x);
                break;
            case "null": validateAndSetPowerValue("0", x);
                break;
            default:
                break;
        }
    }

    private double getAverageOfPowerValues(){
        return (getPowerValueOne()+getPowerValueTwo()+getPowerValueThree()+getPowerValueFour())/4;
    }

    public void loadFonts(String... font){
        for(String f : font){
            Font.loadFont(getClass().getResourceAsStream(f), 0);
        }
    }

    public void addStylesheetFiles(String... stylesheetFile){
        for(String file : stylesheetFile){
            String stylesheet = getClass().getResource(file).toExternalForm();
            getStylesheets().add(stylesheet);
        }
    }


    /*Hilfsmethoden*/
    public double convertToDouble(String userInput) {
        return Double.parseDouble(userInput);
    }

    private String convertToString(double newValue) {
        return String.format("%.2f", newValue);
    }

    /********************************************************************************************************************/
    // alle  Getter und Setter

    public boolean isConvertible() {
        return convertible.get();
    }

    public BooleanProperty convertibleProperty() {
        return convertible;
    }

    public void setConvertible(boolean convertible) {
        this.convertible.set(convertible);
    }

    public String getUserFacingTextTwo() {
        return userFacingTextTwo.get();
    }

    public StringProperty userFacingTextTwoProperty() {
        return userFacingTextTwo;
    }

    public void setUserFacingTextTwo(String userFacingTextTwo) {
        this.userFacingTextTwo.set(userFacingTextTwo);
    }

    public String getUserFacingTextThree() {
        return userFacingTextThree.get();
    }

    public StringProperty userFacingTextThreeProperty() {
        return userFacingTextThree;
    }

    public void setUserFacingTextThree(String userFacingTextThree) {
        this.userFacingTextThree.set(userFacingTextThree);
    }

    public String getUserFacingTextFour() {
        return userFacingTextFour.get();
    }

    public StringProperty userFacingTextFourProperty() {
        return userFacingTextFour;
    }

    public void setUserFacingTextFour(String userFacingTextFour) {
        this.userFacingTextFour.set(userFacingTextFour);
    }

    public double getPowerValueTwo() {
        return powerValueTwo.get();
    }

    public DoubleProperty powerValueTwoProperty() {
        return powerValueTwo;
    }

    public void setPowerValueTwo(double powerValueTwo) {
        this.powerValueTwo.set(powerValueTwo);
    }

    public double getPowerValueThree() {
        return powerValueThree.get();
    }

    public DoubleProperty powerValueThreeProperty() {
        return powerValueThree;
    }

    public void setPowerValueThree(double powerValueThree) {
        this.powerValueThree.set(powerValueThree);
    }

    public double getPowerValueFour() {
        return powerValueFour.get();
    }

    public DoubleProperty powerValueFourProperty() {
        return powerValueFour;
    }

    public void setPowerValueFour(double powerValueFour) {
        this.powerValueFour.set(powerValueFour);
    }

    public String getUserFacingTextOne() {
        return userFacingTextOne.get();
    }

    public StringProperty userFacingTextOneProperty() {
        return userFacingTextOne;
    }

    public void setUserFacingTextOne(String userFacingTextOne) {
        this.userFacingTextOne.set(userFacingTextOne);
    }

    public double getPowerValueOne() {
        return powerValueOne.get();
    }

    public DoubleProperty powerValueOneProperty() {
        return powerValueOne;
    }

    public void setPowerValueOne(double powerValueOne) {
        this.powerValueOne.set(powerValueOne);
    }

    public int getValue() {
        return value.get();
    }

    public IntegerProperty valueProperty() {
        return value;
    }

    public void setValue(int value) {
        this.value.set(value);
    }

    public boolean isReadOnly() {
        return readOnly.get();
    }

    public BooleanProperty readOnlyProperty() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly.set(readOnly);
    }

    public boolean isMandatory() {
        return mandatory.get();
    }

    public BooleanProperty mandatoryProperty() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory.set(mandatory);
    }

    public String getLabel() {
        return label.get();
    }

    public StringProperty labelProperty() {
        return label;
    }

    public void setLabel(String label) {
        this.label.set(label);
    }

    public boolean getInvalid() {
        return invalid.get();
    }

    public BooleanProperty invalidProperty() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid.set(invalid);
    }

    public String getErrorMessage() {
        return errorMessage.get();
    }

    public StringProperty errorMessageProperty() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage.set(errorMessage);
    }

    public String getUserFacingText() {
        return userFacingText.get();
    }

    public StringProperty userFacingTextProperty() {
        return userFacingText;
    }

    public void setUserFacingText(String userFacingText) {
        this.userFacingText.set(userFacingText);
    }

    public boolean isInvalid() {
        return invalid.get();
    }


}
