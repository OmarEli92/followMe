package it.unicam.cs.followme.app.menu;

import it.unicam.cs.followme.LogicController;
import it.unicam.cs.followme.app.FileChooserUtility;
import it.unicam.cs.followme.model.Point;
import it.unicam.cs.followme.model.Robot;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/** This controller is responsible for the window that pop up once the addRobots button is pressed
 * in the main view. It allows the user to select three different ways to generate robots
 * In the case the user decide to add the robots manually , the view will generate  a table of n
 * rows where n is the number of robots, where the user can input the information of the robots
 * (position , direction and speed)**/
public class RobotMenuController {
    private Map<Robot,Point> robots;
    private File robotFile;
    @FXML
    private TextArea numberOfRobotToManuallyAddTextField;
    @FXML
    private TextArea randomRobotsTextArea;
    private int numberOfRobots;
    @FXML
    private VBox areaWhereRobotsAreManuallyAdded;
    private LogicController logicController;
    private FileChooserUtility fileChooser;

    /** Set a Logic Controller which is necessary for the adding robots operations**/
    public void setLogicController(LogicController logicController){
        this.logicController = logicController;
    }

    /** Get the txt file that contains the robots and generate them **/
    public void onLoadRobotFromFile(ActionEvent actionEvent) {
        this.fileChooser = new FileChooserUtility();
        robotFile = fileChooser.chooseFileFromFolder("Load robots from txt file",
                                            "src/main/resources/defaultSimulation");
        logicController.loadRobotsFromFile(robotFile);
        closeWindow();
    }

    /** Get the value in the textField for adding the robots randomly **/
    public void onGenerateRobotRandomly(ActionEvent actionEvent) {
        numberOfRobots = getValueFromTextArea(randomRobotsTextArea);
        logicController.addRobotsInRandomPositions(numberOfRobots);
        closeWindow();
    }

    /** Get the value in the textField for adding the robots manually
     * and generate the table where manually insert the values for the robots **/
    public void onGetNumberOfRobotsToAdd(ActionEvent actionEvent) {
        areaWhereRobotsAreManuallyAdded.getChildren().clear();
        numberOfRobots = getValueFromTextArea(numberOfRobotToManuallyAddTextField);
        System.out.println(numberOfRobots);
        GridPane table = generateTableOfRobots(numberOfRobots);
        areaWhereRobotsAreManuallyAdded.getChildren().add(table);
        generateConfirmButton(table);
    }

    /* Returns the value in the textArea ,if an invalid value is passed
     * by default the method will return 1 */
    private int getValueFromTextArea(TextArea textArea){
        int numberOfRobots = Integer.parseInt(textArea.getText());
        if(numberOfRobots > 0)
            return numberOfRobots;
        return 1;
    }

    /* This method takes an integer n that represents the number of robots
     * and generate n rows with 10 columns respectively representing
     * [initial position x, initial position y, direction x , direction y, speed]
     * and their labels.
     * */
    private GridPane generateTableOfRobots(final int n){
        GridPane grid = new GridPane();
        for(int i = 0; i < n ; i++){
            grid.addRow(i,generateRowOfRobot(i+1));
        }
        return grid;
    }

/* Create a row for the table of robots to add
 * the row is composed like this:
 * [initial position x, initial position y, direction x , direction y, speed]
*/
    private GridPane generateRowOfRobot(final int rowNumber){
        Label[] labels = generateLabels();
        setLabelSettings(labels);
        TextArea[] textAreas = generateTextAreas();
        setTextArea(textAreas);
        GridPane row = new GridPane();
        row.setPrefHeight(20);
        row.setHgap(4);
        row.addRow(rowNumber,labels[0],textAreas[0],labels[1],textAreas[1],labels[2],textAreas[2],
                   labels[3], textAreas[3], labels[4], textAreas[4]);
        row.setStyle("-fx-border-color: BLACK");
        return row;
    }

    /* After the table is being generated , create a
    * button to confirm and save the robot's information */
    private void generateConfirmButton(GridPane table){
        Button confirmButton = new Button();
        confirmButton.setAlignment(Pos.CENTER);
        confirmButton.setId("confirmGenerationButton");
        confirmButton.setText("CONFIRM");
        confirmButton.setFont(Font.font("Calibri BOLD",14));
        confirmButton.setPrefHeight(29.6);
        confirmButton.setPrefWidth(92);
        confirmButton.setOnAction((event -> { saveRobotInformationFromTable(table);}));
        areaWhereRobotsAreManuallyAdded.getChildren().add(confirmButton);
    }

    /* Generate the robots and save them in a map */
    private void saveRobotInformationFromTable(GridPane table){
        robots  = new HashMap<>();
        double[] robotValues = new double[5];
        for(int i = 0; i < table.getChildren().size(); i++){
            if(table.getChildren().get(i) instanceof GridPane pane ){
                for(int j = 0, z = 0; j < pane.getChildren().size(); j++){
                    if(pane.getChildren().get(j) instanceof TextArea textArea)
                        robotValues[z++] = Double.parseDouble(textArea.getText());
                }
                if(checkIsValidDirection(robotValues[2],robotValues[3]))
                    robots.put(new Robot(new Point(robotValues[2], robotValues[3])
                            , robotValues[4]), new Point(robotValues[0], robotValues[1]));
            }
        }
        logicController.addRobots(robots);
        closeWindow();
    }

    /* Close the RobotMenuController window */
    private void closeWindow() {
        Stage stage = (Stage) areaWhereRobotsAreManuallyAdded.getScene().getWindow();
        stage.close();
    }

    /* Check if the given direction is allowed , robot direction can be in this range[-1,1]
    * both x and y can't be contemporaneously equal to 0*/
    private boolean checkIsValidDirection(double x, double y){
        if(x == 0 && y == 0) return false;
        if(x > 1 || x < -1) return false;
        if(y > 1 || y < -1) return false;
        return true;
    }

    /* Generate the labels for the row */
    private Label[] generateLabels(){
        return new Label[]{
                new Label("X position"),
                new Label("Y position"),
                new Label("X direction"),
                new Label("Y direction"),
                new Label("Speed")
        };
    }

    /* Generate textAreas for the row*/
    private TextArea[] generateTextAreas(){
        return new TextArea[]{
                new TextArea(),
                new TextArea(),
                new TextArea(),
                new TextArea(),
                new TextArea()
        };
    }

    /* Set labels settings*/
    private void setLabelSettings(Label[] labels){
        for(Label label: labels){
            label.setFont(Font.font("Calibri BOLD",14));
            label.setAlignment(Pos.CENTER);
            label.setPrefHeight(20);
            label.setPrefWidth(80);
        }
    }

    /* Set textAreas settings*/
    private void setTextArea(TextArea[] textAreas){
        for(TextArea textArea: textAreas){
            textArea.setPrefHeight(18);
            textArea.setPrefWidth(50);
            textArea.editableProperty();
        }
    }

}

