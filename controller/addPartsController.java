package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

/** addPartsController - Controller for addpart.fxml
 *
 * @author Mark Munoz
 * */
public class addPartsController implements Initializable {
    public Label PartVariableLabel;
    public Button PartSaveButton;
    public Button PartCancelButton;
    public TextField PartIDField;
    public TextField PartNameField;
    public TextField PartInvField;
    public TextField PartPriceField;
    public TextField PartMinField;
    public TextField PartMaxField;
    public TextField PartVariableField;
    public RadioButton OutsourcedRadio;
    public RadioButton InhouseRadio;

    Stage stage;
    Parent scene;


    /** Checks to see if Stock is between the min/max amount and checks if max is less than min, gives a User a Pop-Up Alert with information of error..
     *
     * @param currentStock Integer value from the Inv field.
     * @param minStock     Integer value from the Min field.
     * @param maxStock     Integer value from the Max field
     * @return False if stock criteria not met, True if User Input is valid.
     */
    private boolean inventoryValidator(int currentStock, int minStock, int maxStock) {
        if ((currentStock > maxStock) || (currentStock < minStock)) {
            Alert inventoryAlert = new Alert(Alert.AlertType.WARNING);
            inventoryAlert.setHeaderText("Stock and Min/Max Error");
            inventoryAlert.setContentText("Inventory must be between Min and Max Stock.\n" + "And Min must be less than Max Stock!\n"
                    + "Inv.: " + currentStock + "\tMax: " + maxStock + "\tMin: " + minStock);
            Optional<ButtonType> option = inventoryAlert.showAndWait();
            return false;
        } else
            return true;
    }

    /** Generates a unique Part ID
     *
     * @return timeComposite an Integer value of (1000 *Localtime Hour) + (100 x Localtime Minute) + (Localtime Second)
     */
    private int uniquePartIdGeneration() {
        LocalDateTime currentTime = LocalDateTime.now();
        int timeComposite = (1000 * currentTime.getHour()) + (100 * currentTime.getMinute()) + currentTime.getSecond();

        if (Inventory.lookupPart(timeComposite) == null)
            return timeComposite;
        else {
            Random magicNumber = new Random();
            timeComposite += magicNumber.nextInt(0, 9);
            return timeComposite;
        }
    }

    /** Part of the JavaFX GUI Lifecycle.  When FXML is initiated, the default for the Radio Button will be set to Machine ID.
     * @param url Unused in current controller.
     * @param resourceBundle Unused in current controller.
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PartVariableLabel.setText("Machine ID");  // Defaults to "InHouse" Type.
    }

    /** Method returns to Main Screen FXML.
     * @param actionEvent  Unused in Method
     * @param resourceName The file location of the FXML to return to.
     * */
    public void backToMainScreen(ActionEvent actionEvent, String resourceName) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(resourceName));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** On Cancel button return to the Main Screen.
     * @param actionEvent Unused in method.
     * */
    public void onPartCancelButtonClick(ActionEvent actionEvent) throws IOException {
        backToMainScreen(actionEvent, "/view/mainform.fxml");
    }

    /** Create Pop-Up Warnings when the Save Button is clicked to warn Users about Fields without Data Entry.
     * @param fieldName The User Friendly name of the TextField the Pop-up.
     * */
    private void popUpWarningOnSaveClick(String fieldName)
    {
        Alert nonIntEntryAlert = new Alert(Alert.AlertType.WARNING);
        nonIntEntryAlert.setHeaderText("Text field: " + fieldName + " cannot be empty!");
        nonIntEntryAlert.setContentText("Please enter a value into the field to complete\n" + "Data Entry!");
        Optional<ButtonType> option = nonIntEntryAlert.showAndWait();
    }


    /** Save Button for saving User Data into ArrayList.  TextFields are checked for empty fields to avoid Runtime errors.
     * On completion of checks and using the appropriate ArrayList return to the Main Screen.
     * @param actionEvent cause by the firing of a mouse click on the Save Button.
     * */
    public void onPartSaveButtonClick(ActionEvent actionEvent) throws IOException {
        // Check for empty fields.
        if ( PartNameField.getText().trim().isEmpty() ) {
            popUpWarningOnSaveClick("Part Name");
            return;
        }
        else if (PartInvField.getText().trim().isEmpty()) {
            popUpWarningOnSaveClick("Inventory");
            return;
        }
        else if (PartPriceField.getText().trim().isEmpty()) {
            popUpWarningOnSaveClick("Price");
            return;
        }
        else if (PartMinField.getText().trim().isEmpty()) {
            popUpWarningOnSaveClick("Min");
            return;
        }
        else if (PartMaxField.getText().trim().isEmpty()) {
            popUpWarningOnSaveClick("Max");
            return;
        } else if (PartVariableField.getText().trim().isEmpty()) {
            if (OutsourcedRadio.isSelected()) {
                popUpWarningOnSaveClick("Outsourced");
                return;
            } else {
                popUpWarningOnSaveClick("Machine ID");
                return;
            }
        } else {
            // Parse User Data into Variables to be used in ArrayList.
            String partName = PartNameField.getText();
            int partInventory = Integer.parseInt(PartInvField.getText());
            double partPrice = Double.parseDouble(PartPriceField.getText());
            int partMin = Integer.parseInt(PartMinField.getText());
            int partMax = Integer.parseInt(PartMaxField.getText());

            if (inventoryValidator(partInventory, partMin, partMax)) {
                if (OutsourcedRadio.isSelected()) {
                    String CompanyName = PartVariableField.getText();
                    Inventory.addPart(new Outsourced(uniquePartIdGeneration(), partName, partPrice, partInventory, partMin, partMax, CompanyName));
                } else {
                    int partMachineId = Integer.parseInt(PartVariableField.getText());
                    Inventory.addPart(new InHouse(uniquePartIdGeneration(), partName, partPrice, partInventory, partMin, partMax, partMachineId));
                }
                backToMainScreen(actionEvent, "/view/mainform.fxml");
            }
        }
    }

    /** If the In House Radio button is selected, the PartVariableLabel will be changed to "Machine ID" and also wipe the field.
     * @param actionEvent caused by the User clicking on the Radio Button for "In House".
     * */
    public void onInhouseRadioSelect(ActionEvent actionEvent) {
        PartVariableLabel.setText("Machine ID");
        PartVariableField.setText("");
    }

    /** If the Out Sourced Radio button is selected, the PartVariableLabel will be changed to "Company Name" and also wipe the field.
     * @param actionEvent caused by the User clicking on the Radio Button for "Out Sourced".
     * */
    public void onOutsourcedRadioSelect(ActionEvent actionEvent) {
        PartVariableLabel.setText("Company Name");
        PartVariableField.setText("");
    }

    /** Method used to validate numerical data in the Inventory, Min, Max and Machine ID Integer TextFields, as well as the Double data for Price.
     * FUTURE_ENHANCEMENT: Future revisions will eliminate the use of try/catch as it seem ineligant a solution.  Future Revisions will instead use Regular Expressions.
     * To help parse if a field has a number of character.  Also Regex will also be able to detect Tabs and avoid the warning Pop-Ups.
     * @param  fieldName The name of the respective Textfield to be validated.
     * @param dataType  The data type used to be validated.
     * @param textFieldUsed The respective Textfield used.
     * */
    public void preventNonNumericalDataEntry(String fieldName, Class dataType, TextField textFieldUsed) {
        if (dataType == Integer.class)
        try {
            int temp = Integer.parseInt(textFieldUsed.getText());
            } catch (NumberFormatException e) {
                Alert nonIntEntryAlert = new Alert(Alert.AlertType.WARNING);
                nonIntEntryAlert.setHeaderText("Your Input is not an Integer in " + fieldName);
                nonIntEntryAlert.setContentText("Please RE-Enter only a number.");
                Optional<ButtonType> option = nonIntEntryAlert.showAndWait();

                textFieldUsed.setText("");
        }
        else if (dataType == Double.class)
            try {
            Double.parseDouble(PartPriceField.getText());
            } catch (NullPointerException | NumberFormatException ex) {
                Alert nonIntEntryAlert = new Alert(Alert.AlertType.WARNING);
                nonIntEntryAlert.setHeaderText("Your Input is not a Double in " + fieldName);
                nonIntEntryAlert.setContentText("Please RE-Enter a value in Currency Format!");
                Optional<ButtonType> option = nonIntEntryAlert.showAndWait();

                textFieldUsed.setText("");
        }
    }

    /** Method used to insure ONLY Integer values in the Part Inv field.  If a non-Integer/Number is entered a Pop-Up will warn user and clear field.
     * @param keyPress Unused in Method.
     * */
    public void onPartInvFieldKeyTyped(KeyEvent keyPress) {
        preventNonNumericalDataEntry("Inv", Integer.class, PartInvField);
    }

    /** Method used to insure ONLY Integer values in the Part Min field.  If a non-Integer/Number is entered a Pop-Up will warn user and clear field.
     * @param keyPress Unused in Method.
     * */
    public void PartMinFieldKeyTyped(KeyEvent keyPress) {
        preventNonNumericalDataEntry("Min", Integer.class, PartMinField);
    }

    /** Method used to insure ONLY Integer values in the Part Max field.  If a non-Integer/Number is entered a Pop-Up will warn user and clear field.
     * @param keyPress Unused in Method.
     * */
    public void PartMaxFieldKeyTyped(KeyEvent keyPress) {
        preventNonNumericalDataEntry("Max", Integer.class, PartMaxField);
    }

    /** Method used to insure ONLY Double values in the Part Price field.  If a non-Doube/Number is entered a Pop-Up will warn user and clear field.
     * FUTURE-ENHANCEMENT: Use of a REGEX to validate would be much better than using try/catch, it would also eliminate odd behaviors during data entry.
     * @param keyEvent  Unused in Method.
     * */
    public void onPartPriceFieldKeyTyped(KeyEvent keyEvent) {
        preventNonNumericalDataEntry("Price", Double.class, PartPriceField);
    }

    /** Method used to insure ONLY Integer values in the Part Variable field when set on the Raido Button to "In House".  If a non-Integer/Number is entered a Pop-Up will warn user and clear field.
     * @param keyEvent Unused in Method.
     * */
    public void onPartVariableFieldKeyPressed(KeyEvent keyEvent) {
        if(InhouseRadio.isSelected())
            preventNonNumericalDataEntry("Machine ID", Integer.class, PartVariableField);
    }
}

