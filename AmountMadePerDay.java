import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;


/**
 * the project produces an alert of how much pay one should recieve on a given day,
 * as well as how much time the person worked that day
 * 
 * basic rules: time must be in military time, and does not account for going into the next day.
 * 
 * 
 * @author Pierce Nufer
 * @version 1.0
 * 
 */
public class AmountMadePerDay extends Application{
    
    /**
     * used to create the GUI.
     * 
     * @param stage general variable to attach elements for GUI.
     */
    public void start(Stage stage) {
        stage.setTitle("Amount Made Today");

        TextField startTime = new TextField();
        startTime.setPromptText("Start Time");
        TextField endTime = new TextField();
        endTime.setPromptText("End Time");
        TextField hourPay = new TextField();
        hourPay.setPromptText("Hourly Pay");

        final Button calculate = new Button("Calculate");

        Label header = new Label("Please use military time for Start and End Times (i.e. \"09:24\" or \"16:45\")");

        calculate.setOnAction(event -> {
            if (!(startTime.getText().isBlank() || endTime.getText().isBlank() || hourPay.getText().isBlank())) {
                if (inMilitaryTime(startTime.getText()) && inMilitaryTime(endTime.getText())) {
                    calculatePay(startTime.getText(), endTime.getText(), hourPay.getText());
                } else if (!inMilitaryTime(startTime.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Start time is not in military time");
                    alert.showAndWait();
                } else if (!inMilitaryTime(endTime.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "End time is not in military time");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "An Input is not correct");
                    alert.showAndWait();
                }
            } else if (startTime.getText().isBlank()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please Enter a Start Time");
                alert.showAndWait();
            } else if (endTime.getText().isBlank()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please Enter an End Time");
                alert.showAndWait();
            } else if (hourPay.getText().isBlank()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please Enter an Hourly Pay");
                alert.showAndWait();
            } else { // should not happen
                throw new RuntimeException("Invalid operation");
            }
        });

        HBox addTimes = new HBox();
        addTimes.getChildren().addAll(startTime, endTime, hourPay, calculate);
        addTimes.setAlignment(Pos.BOTTOM_LEFT);

        VBox root = new VBox();
        root.getChildren().addAll(header, addTimes);

        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * used to pull the information the user put into the textfields.
     * all information is initually pulled as a string and converted into int or double
     * @param start start time user put in
     * @param end end time user put in
     * @param pay pay that user put in
     */
    private void calculatePay(String start, String end, String pay) {
        String[] startParts = start.split(":");
        String[] endParts = end.split(":");
        int startHr = Integer.parseInt(startParts[0]);
        int startMin = Integer.parseInt(startParts[1]);
        int endHr = Integer.parseInt(endParts[0]);
        int endMin = Integer.parseInt(endParts[1]);
        if (endAfterStart(startHr, startMin, endHr, endMin)) {
            doTheMath(startHr, startMin, endHr, endMin, Double.parseDouble(pay));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Start time is greater than end time");
            alert.showAndWait();
        }
    }

    /**
     * Calcutates the time that a user worked on a given day.
     * 
     * @param sHr start hour
     * @param sMin start min
     * @param eHr end hour
     * @param eMin end min
     * @param wage pay
     */
    private void doTheMath(int sHr, int sMin, int eHr, int eMin, double wage) {
        double paycheck;
        int hours = eHr - sHr;
        int min = eMin - sMin;
        if (hours < 8) {
            paycheck = (hours) * wage;
            if (eMin >= sMin) {
                paycheck += (min) / 60.0 * wage;
            } else {
                min += 60;
                paycheck += min / 60.0 * wage;
            }
        } else {
            paycheck = 8 * wage + wage * 1.5 * ((hours) - 8);
            if (eMin >= sMin) {
                paycheck += (min) / 60.0 * wage * 1.5;
            } else {
                --hours;
                min += 60;
                paycheck += min / 60.0 * wage * 1.5;
            }
        }
        String sent = "Your worked " + hours + " hours " + min + " min for a pay of $" + String.valueOf(paycheck);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, sent);
        //System.out.println(paycheck);
        alert.showAndWait();
    }

    /**
     * used to check if the information the user gave is in military time.
     * 
     * @param str input that user gave
     * @return boolean on whether the input was in military time or not
     */
    private boolean inMilitaryTime(String str) {
        String milTimeChar = "0123456789:";
        char[] strArray = str.toCharArray();
        if (!(str.length() == 5 && strArray[2] == ':')){
            return false;
        }
        for (char s: strArray) {
            if (milTimeChar.indexOf(s) == -1) {
                return false;
            }
        }
        return true;
    }

    /**
     * method that determines if the start time is after the end time.
     * @param startHr start hour
     * @param startMin start min
     * @param endHr end hour
     * @param endMin end min
     * @return boolean on whether start time is after end time
     */
    private boolean endAfterStart(int startHr, int startMin, int endHr, int endMin) {
        if (startHr > endHr) {
            return false;
        } else if (startHr == endHr && startMin > endMin) {
            return false;
        }
        return true;
    }

    /**
     * main method used to run program. 
     * @param args main method arguement
     */
    public static void main(String[] args) {
        launch(args);
    }
}