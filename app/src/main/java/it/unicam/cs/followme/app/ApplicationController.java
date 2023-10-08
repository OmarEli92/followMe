package it.unicam.cs.followme.app;


import it.unicam.cs.followme.LogicController;
import it.unicam.cs.followme.app.menu.RobotMenuController;
import it.unicam.cs.followme.model.Point;
import it.unicam.cs.followme.model.Robot;
import it.unicam.cs.followme.model.Shape;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/** This class is responsible for the simulation of the swarm of robot with JAVAFX.
 * Initialize all the elements in the view and manage the simulation from begin to end**/
public class ApplicationController {
    //DEFAULT VALUES
    private final Color DEFAULT_CIRCLE_AREA_COLOR = Color.valueOf("FFC436");
    private final Color DEFAULT_RECTANGLE_AREA_COLOR = Color.valueOf("FF8303");
    private final double DEFAULT_ZOOM_FACTOR = 1.2;      // applied when ZoomIn or ZoomOut button are pressed
    private final double DEFAULT_ROBOT_RADIUS = 5;
    private  double currentScale = 1.0;             // current scale of the robotView and areaView
    private Scale viewScale = new Scale(1, 1);
    private final Color DEFAULT_ROBOT_COLOR = Color.valueOf("00B8A9");
    private final double DEFAULT_SCROLLING_VIEW_MOVEMENT = 20;
    private LogicController logicController;
    private FileChooserUtility fileChooser;
    private Map<Robot, StackPane> robotsInTheview;
    private Set<Robot> signalingRobots;
    // View elements
    @FXML
    private AnchorPane environmentArea;
    @FXML
    private TextArea ProgramTextField;
    @FXML
    private TextArea commandExecTextArea;
    @FXML
    private TextArea simulationTimeTextArea;
    @FXML
    private Label timerLabel; // timer label
    @FXML
    private Timeline simulationTimeline; // simulation timeline
    private int timePassed = 0;

    public ApplicationController(){
        this.logicController = new LogicController();
        this.fileChooser = new FileChooserUtility();
        this.robotsInTheview = new HashMap<>();
        this.signalingRobots = new HashSet<>();
    }

    /** Method called when the add robot button is pressed**/
    public void onAddRobot(ActionEvent actionEvent) {
        openAddRobotsWindow();
        addRobotsToView();
        logicController.setAreRobotsAdded(true);
    }

    /* Add robots in the view, by default the robot is represented as a circle with
     * a default radius and color*/
    private void addRobotsToView(){
        for(Map.Entry<Robot,Point> entry: logicController.getRobots().entrySet()){
            if(!robotsInTheview.containsKey(entry.getKey())) {
                Point position = getTransformedViewPoint(entry.getValue());
                Circle robot = createCircularShape(position, DEFAULT_ROBOT_RADIUS, DEFAULT_ROBOT_COLOR);
                Label robotLabel = new Label(entry.getKey().getCondition());
                robotLabel.setFont(new Font("Calibri", 15));
                StackPane robotStack = new StackPane();
                robotStack.getChildren().addAll(robot, robotLabel);
                robotStack.setLayoutX(position.getX());
                robotStack.setLayoutY(position.getY());
                robotLabel.setTranslateY(-DEFAULT_ROBOT_RADIUS - 5);
                environmentArea.getChildren().add(robotStack);
                robotStack.toFront();
                robotsInTheview.put(entry.getKey(), robotStack);
            }
        }
    }

    /* Open the add RobotsMenu window and at the end of the operation is automatically closed */
    private void openAddRobotsWindow(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().
                    getResource("/fxml/robotsMenu.fxml"));
            Parent root = loader.load();
            RobotMenuController robotMenuController = loader.getController();
            robotMenuController.setLogicController(logicController);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add robots");
            stage.showAndWait();
        }
        catch(IOException e) {
            System.out.println(e);
        }
    }

    /** Method called when the add environment button is pressed
     * select and load the environmentr from a txt file **/
    public void onAddEnvironment(ActionEvent actionEvent) {
        File environmentFile = fileChooser.chooseFileFromFolder("Select the environment",
                "src/main/resources/defaultSimulation");
        try {
            if(fileChooser.checkIfIsTextFile(environmentFile)) {
                logicController.loadEnvironment(environmentFile);
                addAreasToEnvironmentView();
                logicController.setEnvironmentAdded(true);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /*Add areas in the environment view , the default implementation offers
     * circle and rectangle areas , but it can be extended to other shapes,
     *  also add the label to the area*/
    private void addAreasToEnvironmentView() {
        for (Shape shape : logicController.getAreas()) {
            Point shapeCenter = getTransformedViewPoint(shape.getCenter());
            Node areaNode = obtainTheRightShape(shape,shapeCenter);
            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(areaNode, createLabelForShape(shape.getCondition()));
            environmentArea.getChildren().add(stackPane);
            stackPane.setLayoutX(shapeCenter.getX() - stackPane.getWidth() );
            stackPane.setLayoutY(shapeCenter.getY() - stackPane.getHeight());
            stackPane.toBack();
        }
        environmentArea.requestLayout();
    }

    /* Return the right shape with the default settings*/
    private Node obtainTheRightShape(final Shape shape,final Point centerOfShape){
        Node area = null;
        if (shape instanceof it.unicam.cs.followme.model.Circle circleShape) {
            Circle circle = createCircularShape(centerOfShape, circleShape.getRadius(), DEFAULT_CIRCLE_AREA_COLOR);
            circle.setOpacity(0.35);
            circle.toBack();
            area = circle;
        } else if (shape instanceof it.unicam.cs.followme.model.Rectangle rectangleShape) {
            Rectangle rectangle = createRectangularShape(centerOfShape, rectangleShape.getWidth(),
                                        rectangleShape.getHeight(), DEFAULT_RECTANGLE_AREA_COLOR);
            rectangle.setOpacity(0.35);
            rectangle.toBack();
            area = rectangle;}
        return area;
    }

    /* Create a label for the shape*/
    private Text createLabelForShape(final String labelText) {
        Text label = new Text(labelText);
        label.setFill(Color.BLACK);
        return label;
    }

    /* This method takes the object position , and it returns the object position for the view
     since the point (0,0) in the AnchorPane is the top left corner, the position represented
      in the view must be recalculated */
    private Point getTransformedViewPoint(final Point shapeCenter){
        double[] centerOfPane = getCenterOfPane(environmentArea);
        return new Point(shapeCenter.getX() + centerOfPane[0] ,
                - shapeCenter.getY() + centerOfPane[1]);

    }

    /* This method returns the center (x,y) of a given AnchorPane, by default the point (0,0)
    in an anchorPane is situated at the top left cornere of the Pane therefore
    this method supply the correct center when adding elements in the Pane*/
    private double[] getCenterOfPane(final AnchorPane area){
        double[] center = new double[2];
        center[0] = area.getWidth() / 2;
        center[1] = area.getHeight() / 2;
        return center;
    }

    /* Create a Circular shape */
    private Circle createCircularShape(final Point center,final double radius,final Color color){
        Circle area = new Circle(center.getX(),center.getY(),radius);
        area.setFill(color);
        return area;
    }

    /* Create a rectangular shape */
    private Rectangle createRectangularShape(final Point center,final double width, final double height,
                                             final Color color){
        Rectangle area = new Rectangle(center.getX(),center.getY(),width,height);
        area.setFill(color);
        return area;
    }

    /** Method called when the add program button is pressed
     * select and load the program from a txt file **/
    public void onAddProgram(ActionEvent actionEvent) {
        File programFile = fileChooser.chooseFileFromFolder("Select the program",
                "src/main/resources/defaultSimulation");
        try{
                logicController.loadProgram(programFile);
                setProgramAdded(true);
                String programContent = loadProgramContent(programFile);
                ProgramTextField.setText(programContent);
                logicController.setProgramParsed(true);

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /* Method used to load the content of the file in a string, that string that represent the program
     will be used in ProgramTextField so the user  can see the program that the robot is executing*/
    private String loadProgramContent(final File programFile) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(programFile));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();
            return content.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /* Open an alert window */
    private void popUpAlertWindow(final String title,final String message){
        Alert alertWindows = new Alert(Alert.AlertType.INFORMATION);
        alertWindows.setTitle(title);
        alertWindows.setContentText(message);
        alertWindows.showAndWait();
    }

    /** Method called when the reset button is pressed
     * reset the robots,environment and the program**/
    public void onResetButton(ActionEvent actionEvent) {
        logicController.deleteRobots();
        logicController.deleteProgram();
        logicController.resetEnvironment();
        logicController.setAreRobotsAdded(false);
        logicController.setProgramParsed(false);
        logicController.setEnvironmentAdded(false);
        environmentArea.getChildren().clear();
        ProgramTextField.clear();
    }

    /* Returns the value in the textArea ,if an invalid value is passed
     * by default the method will return 1 */
    private int getValueFromTextArea(final TextArea textArea){
        int valueFromArea = Integer.parseInt(textArea.getText().trim());
        if(valueFromArea > 0)
            return valueFromArea;
        return 1;
    }

    /** Method called when the start simulation button is pressed**/
    public void onStartSimulation(ActionEvent actionEvent) {
        if (logicController.isSimulationReady()) {
            setTimings(getValueFromTextArea(simulationTimeTextArea),getValueFromTextArea(commandExecTextArea));
            setupSimulationTimeline();
                simulationTimeline.play();
                firstStepSimulation();
        }
        else {
            popUpAlertWindow("Elements needed","The simulation requires the list of robots," +
                    " an environment and a program");
        }
    }

    /* The method is called at the very beginning of the simulation and start the simulation,
     * check for signaled conditions, and update the positions of the robot in the view*/
    private void firstStepSimulation(){
        logicController.nextStepInTheSimulation();
        signalCondition();
        updateRobotPositionsInView();
    }

    /*  Setup a new timeline for the simulation, this method creates the timeline
     * and call a method for updating the simulation timer every second also
     * call a method that execute the next command every(CommandExecutionTime) seconds*/
    private void setupSimulationTimeline() {
        simulationTimeline = new Timeline( new KeyFrame(Duration.seconds(1),
                event -> {
                    updateSimulationTimer();
                    if(timePassed % logicController.getCommandExecutionTime() == 0){
                        logicController.nextStepInTheSimulation();
                        signalCondition();
                        unsignalCondition();
                        updateRobotPositionsInView();
                    }
                }));
        simulationTimeline.setCycleCount(getValueFromTextArea(simulationTimeTextArea));
    }

    /* Update the timer of the simulation */
    private void updateSimulationTimer() {
        timePassed++;
        int minutes = timePassed / 60;
        int seconds = timePassed % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    /* Set the simulation time and the command execution time*/
    private void setTimings(final int simulationTime, final int commandExecutionTime){
        logicController.setSimulationTime(simulationTime);
        logicController.setCommandExecutionTime(commandExecutionTime);
    }

    /* Update the positions of the robots during the simulation*/
    private void updateRobotPositionsInView() {
        for(Map.Entry<Robot,Point> entry: logicController.getRobots().entrySet()){
            StackPane robotStack = getRobotsFromView(entry.getKey());
            Circle robotCircle = getCircleFromStackPane(robotStack);
            if(robotCircle != null) {
                Point newRobotPosition = getTransformedViewPoint(entry.getValue());
                TranslateTransition transition = new TranslateTransition(
                        Duration.seconds(logicController.getCommandExecutionTime()), robotStack);
                double newX = newRobotPosition.getX() * currentScale;
                double newY = (newRobotPosition.getY() * currentScale);
                transition.setToX(newX - robotCircle.getCenterX());
                transition.setToY((newY - robotCircle.getCenterY()));
                transition.play();
            }
        }
    }

    /* Get the circle representation of the robot from the stackPane that includes the circle
     * and the condition label if it is present*/
    private Circle getCircleFromStackPane(final StackPane stackPane) {
        Node firstChild = stackPane.getChildren().get(0);
        if (firstChild instanceof Circle) {
            return (Circle) firstChild;
        }
        return null;
    }

    /* Set the condition label that is associated with the robot*/
    private void setRobotLabelFromStackPane(final StackPane robotStack,final String condition ) {
        for (Node node : robotStack.getChildren()) {
            if (node instanceof Label conditionLabel)
                conditionLabel.setText(condition);
        }
    }

    /* Returns the StackPane that represents the robot and its label */
    private StackPane getRobotsFromView(final Robot robot) {
        StackPane robotInView = robotsInTheview.get(robot);
        return robotInView;
    }

    /* This method check if the robots are signaling a condition int that case
    update the robot's state in the view in the case a condition is being signaled*/
    private void signalCondition(){
        signalingRobots = logicController.getSignalingRobots();
        for(Robot robot: signalingRobots){
            StackPane robotStack = getRobotsFromView(robot);
            setRobotLabelFromStackPane(robotStack,robot.getCondition());
        }
    }

    /* This method remove the condition from the robots that are currently signaling */
    private void unsignalCondition(){
        signalingRobots = logicController.getSignalingRobots();
        for(Map.Entry<Robot,StackPane> entry: robotsInTheview.entrySet()){
            if(!signalingRobots.contains(entry.getKey())){
                StackPane robotStack = getRobotsFromView(entry.getKey());
                setRobotLabelFromStackPane(robotStack,"");
            }
        }
    }

    public void onStopSimulation(ActionEvent actionEvent) {
        simulationTimeline.stop();
        simulationTimeline = null;
        timePassed = 0;
        timerLabel.setText("00:00");
    }

    /** Method called when the move up button is pressed**/
    public void onMoveUp(ActionEvent actionEvent) {
        updateScrollingViewVertically(DEFAULT_SCROLLING_VIEW_MOVEMENT);
    }

    /** Method called when the move left button is pressed**/
    public void onMoveLeft(ActionEvent actionEvent) {
        updateScrollingViewHorizzontally(DEFAULT_SCROLLING_VIEW_MOVEMENT);
    }

    /** Method called when the move down button is pressed**/
    public void onMoveDown(ActionEvent actionEvent) {
        updateScrollingViewVertically(-DEFAULT_SCROLLING_VIEW_MOVEMENT);
    }

    /** Method called when the move right button is pressed**/
    public void onMoveRight(ActionEvent actionEvent) {
        updateScrollingViewHorizzontally(-DEFAULT_SCROLLING_VIEW_MOVEMENT);
    }

    /* The method encapsulated the logic for scrolling the Pane vertically where the robots
    and the areas reside, it is called whenever the move up,down buttons
    are clicked*/
    private void updateScrollingViewVertically(final double Ydirection){
        for (Node node : environmentArea.getChildren()) {
            node.setLayoutY(node.getLayoutY() + Ydirection);
        }
        environmentArea.requestLayout();
    }

    /* The method encapsulated the logic for scrolling the Pane vertically where the robots
    and the areas reside, it is called whenever the move up,down buttons
    are clicked*/
     void updateScrollingViewHorizzontally(final double Xdirection){
        for (Node node : environmentArea.getChildren()) {
            node.setLayoutX(node.getLayoutX() + Xdirection);
        }
        environmentArea.requestLayout();
    }

    /** Method called when the zoom in button is pressed **/
    public void onZoomIn(ActionEvent actionEvent) {
        currentScale *= DEFAULT_ZOOM_FACTOR;
        applyScale();
    }

    /** Method called when the zoom out button is pressed **/
    public void onZoomOut(ActionEvent actionEvent) {
        currentScale /= DEFAULT_ZOOM_FACTOR;
        applyScale();
    }


    /* The method remove the existent transformation inthe Pane and
     * add a new Scale with the new currentSCale value and add the transformation
     * the method is used to zoomIn and zoomOut*/
    private void applyScale() {
        // PATCH NEEDED!!! the robots loose their correct position when zoomIn and zoomOut are pressed
        /*
        double vValueRobot = environmentScrollPane.getVvalue();
        double hValueRobot = environmentScrollPane.getHvalue();
        viewScale.setX(currentScale);
        viewScale.setY(currentScale);
        for (Node node : environmentArea.getChildren()) {
            node.getTransforms().setAll(viewScale);
        }
        environmentScrollPane.setVvalue(vValueRobot);
        environmentScrollPane.setHvalue(hValueRobot);
         */
    }


    private void setRobotsAdded(final boolean robotsAdded) {
        this.logicController.setAreRobotsAdded(true);
    }

    private void setEnvironmentAdded(final boolean environmentAdded) {
        this.logicController.setEnvironmentAdded(true);
    }

    private void setProgramAdded(final boolean programAdded) {
        this.logicController.setProgramParsed(true);
    }



}
