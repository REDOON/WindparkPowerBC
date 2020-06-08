package cuie.project.bcwindparkfx;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

class BusinessSkin extends SkinBase<PowerValueControl> {
    private static final double ARTBOARD_WIDTH  = 800;
    private static final double ARTBOARD_HEIGHT = 120;

    private static final double ASPECT_RATIO = ARTBOARD_WIDTH / ARTBOARD_HEIGHT;

    private static final double MINIMUM_WIDTH  = 500;
    private static final double MINIMUM_HEIGHT = MINIMUM_WIDTH / ASPECT_RATIO;

    private static final double MAXIMUM_WIDTH = 800;
    private static final double MAXIMUM_HEIGHT = MAXIMUM_WIDTH / ASPECT_RATIO;


    private static final String STYLE_CSS = "style.css";


    //all new parts
    private Line bar;
    private Circle[] points;
    private Line[] lines;

    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;

    private Rectangle recOne;
    private Rectangle recTwo;
    private Rectangle recThree;
    private Rectangle recFour;

    private Label title15;
    private Label title16;
    private Label title17;
    private Label title18;

    private Label value15;
    private Label value16;
    private Label value17;
    private Label value18;

    private TextField tx15;
    private TextField tx16;
    private TextField tx17;
    private TextField tx18;

    private Boolean activated;
    private Animation invalidInputAnimation;
    private TranslateTransition moveRight;
    private TranslateTransition moveLeft;

    private Timeline timeline = new Timeline();

    private KeyValue closeValueBtn15;
    private KeyValue closeValueBtn16;
    private KeyValue closeValueBtn17;
    private KeyValue closeValueBtn18;
    private KeyValue closeValueLine15;
    private KeyValue closeValueLine16;
    private KeyValue closeValueLine17;
    private KeyValue closeValueLine18;
    private KeyValue closeValueTx15;
    private KeyValue closeValueTx16;
    private KeyValue closeValueTx17;
    private KeyValue closeValueTx18;

    //CC parts
    private Circle windCircle, wheelCircle, innerWheelCircle;
    private Polygon triangle1, triangle2, triangle3;

    //Animation parts
    Rotate rotation = new Rotate();
    Timeline rotationTimeline = new Timeline();

    private Pane drawingPane;

    BusinessSkin(PowerValueControl control) {
        super(control);
        initializeSelf();
        initializeParts();
        initializeDrawingPane();
        layoutParts();
        setupAnimations();
        setupEventHandlers();
        setupValueChangedListeners();
        setupBindings();
    }

    private void initializeSelf() {
        getSkinnable().loadFonts("/fonts/Lato/Lato-Lig.ttf",  "/fonts/Lato/Lato-Reg.ttf", "/fonts/ds_digital/DS-DIGI.TTF", "/fonts/fontawesome-webfont.ttf");
        getSkinnable().addStylesheetFiles(STYLE_CSS);
    }

    private void initializeParts() {
        bar = new Line();
        bar.setStartY(ARTBOARD_HEIGHT*0.9);
        bar.setEndY(ARTBOARD_HEIGHT*0.9);

        activated = false;

        recOne = new Rectangle();
        recOne.setHeight(ARTBOARD_HEIGHT);
        recOne.getStyleClass().add("triggers");
        recTwo = new Rectangle();
        recTwo.setHeight(ARTBOARD_HEIGHT);
        recTwo.getStyleClass().add("triggers");
        recThree = new Rectangle();
        recThree.setHeight(ARTBOARD_HEIGHT);
        recThree.getStyleClass().add("triggers");
        recFour = new Rectangle();
        recFour.setHeight(ARTBOARD_HEIGHT);
        recFour.getStyleClass().add("triggers");

        tx15 = new TextField();
        tx15.getStyleClass().add("text-field");
        tx15.setLayoutY(ARTBOARD_HEIGHT*0.275);
        tx15.setOpacity(0.0);
        tx15.setVisible(false);
        tx16 = new TextField();
        tx16.getStyleClass().add("text-field");
        tx16.setLayoutY(ARTBOARD_HEIGHT*0.275);
        tx16.setOpacity(0.0);
        tx16.setVisible(false);
        tx17 = new TextField();
        tx17.getStyleClass().add("text-field");
        tx17.setLayoutY(ARTBOARD_HEIGHT*0.275);
        tx17.setOpacity(0.0);
        tx17.setVisible(false);
        tx18 = new TextField();
        tx18.getStyleClass().add("text-field");
        tx18.setLayoutY(ARTBOARD_HEIGHT*0.275);
        tx18.setOpacity(0.0);
        tx18.setVisible(false);

        value15 = new Label();
        value15.setLayoutY(ARTBOARD_HEIGHT*0.3);
        value15.getStyleClass().add("values");
        value16 = new Label();
        value16.setLayoutY(ARTBOARD_HEIGHT*0.3);
        value16.getStyleClass().add("values");
        value17 = new Label();
        value17.setLayoutY(ARTBOARD_HEIGHT*0.3);
        value17.getStyleClass().add("values");
        value18 = new Label();
        value18.setLayoutY(ARTBOARD_HEIGHT*0.3);
        value18.getStyleClass().add("values");

        btn1 = createThumbButton();
        btn2 = createThumbButton();
        btn3 = createThumbButton();
        btn4 = createThumbButton();

        points = new Circle[6];
        lines  = new Line[6];

        int i = points.length-1;
        while(i >= 0){
            Circle c = new Circle();
            c.setLayoutY(ARTBOARD_HEIGHT*0.9);
            c.getStyleClass().add("circles");
            points[i] = c;

            Line l = new Line();
            l.setStartY(ARTBOARD_HEIGHT*0.9);
            l.setEndY(ARTBOARD_HEIGHT*0.9);
            l.getStyleClass().add("lines");
            lines[i] = l;

            i--;
        }

        title15 = new Label("2015");
        title15.setLayoutY(ARTBOARD_HEIGHT*0.75);
        title15.getStyleClass().add("title");
        title16 = new Label("2016");
        title16.setLayoutY(ARTBOARD_HEIGHT*0.75);
        title16.getStyleClass().add("title");
        title17 = new Label("2017");
        title17.setLayoutY(ARTBOARD_HEIGHT*0.75);
        title17.getStyleClass().add("title");
        title18 = new Label("2018");
        title18.setLayoutY(ARTBOARD_HEIGHT*0.75);
        title18.getStyleClass().add("title");
    }

    private void layoutParts() {
        drawingPane.getChildren().addAll(bar, points[0], points[1], points[2], points[3], points[4], points[5], lines[1], lines[2],lines[3],lines[4],btn1, btn2, btn3, btn4, title15, title16, title17, title18, recOne, recTwo, recThree, recFour, tx15, tx16, tx17, tx18, value15, value16, value17, value18);
        getChildren().add(drawingPane);
    }

    private void setupAnimations() {
        int      delta    = 5;
        Duration duration = Duration.millis(30);

        //Invalid Input Animation
        moveRight = new TranslateTransition(duration);
        moveRight.setFromX(0.0);
        moveRight.setByX(delta);
        moveRight.setAutoReverse(true);
        moveRight.setCycleCount(2);
        moveRight.setInterpolator(Interpolator.LINEAR);

        moveLeft = new TranslateTransition(duration);
        moveLeft.setFromX(0.0);
        moveLeft.setByX(-delta);
        moveLeft.setAutoReverse(true);
        moveLeft.setCycleCount(2);
        moveLeft.setInterpolator(Interpolator.LINEAR);

        invalidInputAnimation = new SequentialTransition(moveRight, moveLeft);
        invalidInputAnimation.setCycleCount(3);

        //Close Animations Values
        closeValueBtn15 = new KeyValue(btn1.layoutYProperty(), ARTBOARD_HEIGHT*0.6);
        closeValueLine15 = new KeyValue(lines[1].endYProperty(), ARTBOARD_HEIGHT*0.9);
        closeValueTx15 = new KeyValue(tx15.opacityProperty(), 0.0);
        closeValueBtn16 = new KeyValue(btn2.layoutYProperty(), ARTBOARD_HEIGHT*0.6);
        closeValueLine16 = new KeyValue(lines[2].endYProperty(), ARTBOARD_HEIGHT*0.9);
        closeValueTx16 = new KeyValue(tx16.opacityProperty(), 0.0);
        closeValueBtn17 = new KeyValue(btn3.layoutYProperty(), ARTBOARD_HEIGHT*0.6);
        closeValueLine17 = new KeyValue(lines[3].endYProperty(), ARTBOARD_HEIGHT*0.9);
        closeValueTx17 = new KeyValue(tx17.opacityProperty(), 0.0);
        closeValueBtn18 = new KeyValue(btn4.layoutYProperty(), ARTBOARD_HEIGHT*0.6);
        closeValueLine18 = new KeyValue(lines[4].endYProperty(), ARTBOARD_HEIGHT*0.9);
        closeValueTx18 = new KeyValue(tx18.opacityProperty(), 0.0);

        //CC Animations
        KeyFrame frame0 = new KeyFrame(Duration.ZERO, new KeyValue(rotation.angleProperty(), 0));
        KeyFrame frame360 = new KeyFrame(Duration.seconds(1), new KeyValue(rotation.angleProperty(), 360));

        rotationTimeline.setCycleCount(Animation.INDEFINITE);
        rotationTimeline.getKeyFrames().addAll(frame0, frame360);
        rotationTimeline.play();

    }

    private void setupEventHandlers() {
        recOne.setOnMouseClicked(event -> clickAnimation(btn1, lines[1], tx15, value15));
        recTwo.setOnMouseClicked(event -> clickAnimation(btn2, lines[2], tx16, value16));
        recThree.setOnMouseClicked(event -> clickAnimation(btn3, lines[3], tx17, value17));
        recFour.setOnMouseClicked(event -> clickAnimation(btn4, lines[4], tx18, value18));

        tx15.setOnAction(event -> getSkinnable().convert(tx15.getText(), 1));
        tx16.setOnAction(event -> getSkinnable().convert(tx16.getText(), 2));
        tx17.setOnAction(event -> getSkinnable().convert(tx17.getText(), 3));
        tx18.setOnAction(event -> getSkinnable().convert(tx18.getText(), 4));
    }

    private void setupValueChangedListeners() {
        getSkinnable().invalidProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                if(btn1.getLayoutY() < ARTBOARD_HEIGHT * 0.5) {
                    startInvalidInputAnimation(tx15);
                }
            }
        });

        getSkinnable().readOnlyProperty().addListener((observable, oldValue, newValue) -> {
                tx15.setEditable(!newValue);
                tx16.setEditable(!newValue);
                tx17.setEditable(!newValue);
                tx18.setEditable(!newValue);
        });

    }

    private void setupBindings() {
        tx15.textProperty().bindBidirectional(getSkinnable().userFacingTextOneProperty());
        tx16.textProperty().bindBidirectional(getSkinnable().userFacingTextTwoProperty());
        tx17.textProperty().bindBidirectional(getSkinnable().userFacingTextThreeProperty());
        tx18.textProperty().bindBidirectional(getSkinnable().userFacingTextFourProperty());

        value15.textProperty().bind(getSkinnable().userFacingTextOneProperty().concat(" MWh"));
        value16.textProperty().bind(getSkinnable().userFacingTextTwoProperty().concat(" MWh"));
        value17.textProperty().bind(getSkinnable().userFacingTextThreeProperty().concat(" MWh"));
        value18.textProperty().bind(getSkinnable().userFacingTextFourProperty().concat(" MWh"));

        //CC
        rotation.pivotXProperty().bind(triangle1.translateXProperty());
        rotation.pivotYProperty().bind(triangle1.translateYProperty());
        rotation.pivotXProperty().bind(triangle2.translateXProperty());
        rotation.pivotYProperty().bind(triangle2.translateYProperty());
        rotation.pivotXProperty().bind(triangle3.translateXProperty());
        rotation.pivotYProperty().bind(triangle3.translateYProperty());

    }


    private void clickAnimation(Button btn, Line l, TextField tx, Label val) {

            if (timeline.getStatus().equals(Animation.Status.RUNNING)) {
                return;
            }

            getSkinnable().reset();

            //Todo: Full, min, average wird nicht automatisch auf PropertyValue Change gewechselt.
            tx15.setText(getSkinnable().getUserFacingTextOne());
            tx16.setText(getSkinnable().getUserFacingTextTwo());
            tx17.setText(getSkinnable().getUserFacingTextThree());
            tx18.setText(getSkinnable().getUserFacingTextFour());

            if (activated && btn.getLayoutY() == ARTBOARD_HEIGHT * 0.6 && !getSkinnable().isInvalid()) {
                closeAndOpenNewOne(btn, l, tx);
                settingsForTx(tx, val);
                return;
            } else if (btn.getLayoutY() == ARTBOARD_HEIGHT * 0.6 && !activated) {      //open a single one
                timeline.getKeyFrames().setAll(new KeyFrame(Duration.millis(80),
                        new KeyValue(tx.opacityProperty(), tx.getOpacity() + 1.0),
                        new KeyValue(btn.layoutYProperty(), btn.getLayoutY() - 45),
                        new KeyValue(l.endYProperty(), l.getEndY() - 50)));
                timeline.play();
                settingsForTx(tx, val);
                activated = true;

            } else if (btn.getLayoutY() < ARTBOARD_HEIGHT * 0.5 && activated && !getSkinnable().isInvalid()) {   //close a single one
                timeline.getKeyFrames().setAll(new KeyFrame(Duration.millis(50),
                        new KeyValue(tx.opacityProperty(), tx.getOpacity() - 1.0),
                        new KeyValue(btn.layoutYProperty(), btn.getLayoutY() + 45),
                        new KeyValue(l.endYProperty(), l.getStartY())));
                timeline.play();
                settingsForTx(tx, val);
                activated = false;
            }
    }

    private void settingsForTx(TextField tx, Label l){
         if(tx.getOpacity() == 0.0){
             tx.setVisible(true);
             l.setVisible(false);
         }else{
             tx.setVisible(false);
             l.setVisible(true);
         }
    }


    private void closeAndOpenNewOne(Button btn, Line l, TextField tx){
        if (timeline.getStatus().equals(Animation.Status.RUNNING)) {
            return;
        }


        if (btn1.getLayoutY() < ARTBOARD_HEIGHT * 0.3) {
            timeline.getKeyFrames().setAll(new KeyFrame(Duration.millis(50),
                    new KeyValue(tx.opacityProperty(), tx.getOpacity() + 1.0),
                    new KeyValue(btn.layoutYProperty(), btn.getLayoutY() - 45),
                    new KeyValue(l.endYProperty(), l.getEndY() - 50), closeValueBtn15, closeValueLine15, closeValueTx15));
            settingsForTx(tx15, value15);
        } else if (btn2.getLayoutY() < ARTBOARD_HEIGHT * 0.3) {
            timeline.getKeyFrames().setAll(new KeyFrame(Duration.millis(50),
                    new KeyValue(tx.opacityProperty(), tx.getOpacity() + 1.0),
                    new KeyValue(btn.layoutYProperty(), btn.getLayoutY() - 45),
                    new KeyValue(l.endYProperty(), l.getEndY() - 50), closeValueBtn16, closeValueLine16, closeValueTx16));
            settingsForTx(tx16, value16);
        }else if (btn3.getLayoutY() < ARTBOARD_HEIGHT * 0.3) {
            timeline.getKeyFrames().setAll(new KeyFrame(Duration.millis(50),
                    new KeyValue(tx.opacityProperty(), tx.getOpacity() + 1.0),
                    new KeyValue(btn.layoutYProperty(), btn.getLayoutY() - 45),
                    new KeyValue(l.endYProperty(), l.getEndY() - 50), closeValueBtn17, closeValueLine17, closeValueTx17));
            settingsForTx(tx17, value17);
        }
        else if (btn4.getLayoutY() < ARTBOARD_HEIGHT * 0.3) {
            timeline.getKeyFrames().setAll(new KeyFrame(Duration.millis(50),
                    new KeyValue(tx.opacityProperty(), tx.getOpacity() + 1.0),
                    new KeyValue(btn.layoutYProperty(), btn.getLayoutY() - 45),
                    new KeyValue(l.endYProperty(), l.getEndY() - 50), closeValueBtn18, closeValueLine18, closeValueTx18));
            settingsForTx(tx18, value18);
        }
        timeline.play();
    }


    private void startInvalidInputAnimation(TextField tx) {
        if (invalidInputAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            invalidInputAnimation.stop();
            moveLeft.setNode(null);
            moveRight.setNode(null);
        }
        moveRight.setNode(tx);
        moveLeft.setNode(tx);
        invalidInputAnimation.play();
    }


    private void loadFonts(String... font){
        for(String f : font){
            Font.loadFont(getClass().getResourceAsStream(f), 0);
        }
    }

    private void initializeDrawingPane() {
        drawingPane = new Pane();
        drawingPane.setPadding(new Insets(10,10,10,10));
        drawingPane.setPrefSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        drawingPane.setMaxSize(MAXIMUM_WIDTH,  MAXIMUM_HEIGHT);
        drawingPane.setMinSize(MINIMUM_WIDTH,  MINIMUM_HEIGHT);
        drawingPane.getStyleClass().add("drawing-pane");
    }

    private Button createThumbButton(){
        // CustomControl parts
        windCircle = new Circle();
        windCircle.getStyleClass().add("wind-circle");
        windCircle.setRadius(1.8);
        windCircle.setFill(new Color(0,0.8,1,1));

        wheelCircle = new Circle();
        wheelCircle.getStyleClass().add("wheel-circle");
        wheelCircle.setRadius(1.8);
        wheelCircle.setFill(new Color(0.9,0.9,0.9,1));
        wheelCircle.setStroke(Color.BLACK);
        wheelCircle.setStrokeWidth(0.2);

        innerWheelCircle = new Circle();
        innerWheelCircle.getStyleClass().add("inner-wheel-circle");
        innerWheelCircle.setRadius(0.96);
        innerWheelCircle.setFill(new Color(0,0,0,1));

        triangle1 = new Polygon();
        triangle1.getStyleClass().add("triangle");
        triangle1.getPoints().addAll(new Double[]{
                -1.56, 0.0,
                1.56, 0.0,
                0.0, 12.36
        });
        triangle1.setFill(new Color(0.7,0.7,0.7,1));
        triangle1.getTransforms().add(new Affine(new Rotate(180, 0, 0)));
        triangle1.getTransforms().add(rotation);

        triangle2 = new Polygon();
        triangle2.getStyleClass().add("triangle");
        triangle2.getPoints().addAll(new Double[]{
                -1.56, 0.0,
                1.56, 0.0,
                0.0, 12.36
        });
        triangle2.setFill(new Color(0.7,0.7,0.7,1));
        triangle2.getTransforms().add(new Affine(new Rotate(300, 0, 0)));
        triangle2.getTransforms().add(rotation);

        triangle3 = new Polygon();
        triangle3.getStyleClass().add("triangle");
        triangle3.getPoints().addAll(new Double[]{
                -1.56, 0.0,
                1.56, 0.0,
                0.0, 12.36
        });
        triangle3.setFill(new Color(0.7,0.7,0.7,1));
        triangle3.getTransforms().add(new Affine(new Rotate(60, 0, 0)));
        triangle3.getTransforms().add(rotation);

        Pane wwPane = new Pane();
        wwPane.getChildren().addAll(windCircle, triangle1, triangle2, triangle3, wheelCircle, innerWheelCircle);

        Button button = new Button();
        button.getStyleClass().add("wwButton");
        button.setGraphic(wwPane);
        button.setLayoutY(ARTBOARD_HEIGHT*0.6);
        return button;
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {

        if (contentWidth > 0 && contentHeight > 0) {
            contentHeight = 130.00;
            drawingPane.setPrefSize(contentWidth, contentHeight);
            drawingPane.setLayoutX(contentX);
            drawingPane.setLayoutY(contentY);

            bar.setStartX(10);
            bar.setEndX(drawingPane.getWidth()-10);

            btn1.setLayoutX(drawingPane.getWidth()*0.1);
            btn2.setLayoutX(drawingPane.getWidth()*0.3);
            btn3.setLayoutX(drawingPane.getWidth()*0.5);
            btn4.setLayoutX(drawingPane.getWidth()*0.7);

            title15.setLayoutX(drawingPane.getWidth()*0.15);
            title16.setLayoutX(drawingPane.getWidth()*0.35);
            title17.setLayoutX(drawingPane.getWidth()*0.55);
            title18.setLayoutX(drawingPane.getWidth()*0.75);

            value15.setLayoutX(drawingPane.getWidth()*0.15);
            value16.setLayoutX(drawingPane.getWidth()*0.35);
            value17.setLayoutX(drawingPane.getWidth()*0.55);
            value18.setLayoutX(drawingPane.getWidth()*0.75);

            tx15.setLayoutX(drawingPane.getWidth()*0.138);
            tx16.setLayoutX(drawingPane.getWidth()*0.338);
            tx17.setLayoutX(drawingPane.getWidth()*0.538);
            tx18.setLayoutX(drawingPane.getWidth()*0.738);

            tx15.setPrefWidth(drawingPane.getWidth()*0.15);
            tx16.setPrefWidth(drawingPane.getWidth()*0.15);
            tx17.setPrefWidth(drawingPane.getWidth()*0.15);
            tx18.setPrefWidth(drawingPane.getWidth()*0.15);

            recOne.setWidth(contentWidth*0.2);
            recOne.setLayoutX(drawingPane.getWidth()*0.1-10);
            recTwo.setWidth(contentWidth*0.2);
            recTwo.setLayoutX(drawingPane.getWidth()*0.3-10);
            recThree.setWidth(contentWidth*0.2);
            recThree.setLayoutX(drawingPane.getWidth()*0.5-10);
            recFour.setWidth(contentWidth*0.2);
            recFour.setLayoutX(drawingPane.getWidth()*0.7-10);


            double x = 0.1;
            double r = 5;
            double w = btn1.getWidth()*0.5;
            for(int i = 0; i<points.length; i++) {
                if (i == 0) {
                    points[i].setLayoutX(10);
                    points[i].setRadius(r);
                } else if(i==points.length-1){
                    points[i].setLayoutX(drawingPane.getWidth()-10);
                    points[i].setRadius(r);
                }else {
                    points[i].setLayoutX(drawingPane.getWidth() * x + w);
                    points[i].setRadius(r*0.8);
                    lines[i].setStartX(drawingPane.getWidth() * x + w);
                    lines[i].setEndX(drawingPane.getWidth() * x + w);

                    x += 0.2;
                }
            }
        }
        super.layoutChildren(contentX,contentY, contentWidth, contentHeight);
    }
}
