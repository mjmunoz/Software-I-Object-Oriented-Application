package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** modifyPartsController - Controller for modifyParts.fxml
 *
 * @author Mark Munoz
 * */
public class modifyPartController implements Initializable {
    public Label ModifyVariableLabel;
    public Button ModifySaveButton;
    public Button ModifyCancelButton;
    public TextField ModifyIDField;
    public TextField ModifyNameField;
    public TextField ModifyInvField;
    public TextField ModifyPriceField;
    public TextField ModifyMinField;
    public TextField ModifyMaxField;
    public TextField ModifyVariableField;
    public RadioButton OutsourcedRadio;
    public RadioButton InhouseRadio;

    Stage stage;
    Parent scene;

    private int index;
    public Part mainviewSelectedPart;

    /** Method used to get data from the Main Screen to populated the Modify Parts FXML screen.
     * @param selectedParts Data from the Part Table View from the Main Screen.
     * */
    public void setParts(Part selectedParts)
    {
        mainviewSelectedPart = selectedParts;

        ModifyIDField.setText(Integer.toString(selectedParts.getId()));
        ModifyNameField.setText(selectedParts.getName());
        ModifyPriceField.setText(Double.toString(selectedParts.getPrice()));
        ModifyInvField.setText(Integer.toString(selectedParts.getStock()));
        ModifyMaxField.setText(Integer.toString(selectedParts.getMax()));
        ModifyMinField.setText(Integer.toString(selectedParts.getMin()));

        // To preserve index for use if change of Radio.
        for (int i =0; i < Inventory.getAllParts().size(); i++) {
            String temp = Inventory.getAllParts().get(i).getName();
            if (temp.equals(selectedParts.getName())) {
                index = i;
                break;
            }
        }
        if(selectedParts instanceof InHouse)   {
            InhouseRadio.setSelected(true);
            ModifyVariableLabel.setText("Machine ID");
            ModifyVariableField.setText(Integer.toString(((InHouse) selectedParts).getMachineId()));
        }
        if(selectedParts instanceof Outsourced)   {
            OutsourcedRadio.setSelected(true);
            ModifyVariableLabel.setText("Company Name");
            ModifyVariableField.setText(((Outsourced) selectedParts).getCompanyName());
        }
    }

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

    /** Unused in Class, but part of the JavaFX GUI Lifecycle.  When FXML is initiated, the default for the Radio Button will be set to Machine ID.
     * @param url Unused in current controller.
     * @param resourceBundle Unused in current controller.
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
    public void onModifyCancelButtonClick(ActionEvent actionEvent) throws IOException {
        backToMainScreen(actionEvent,"/view/mainform.fxml");
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

    /** Completes the Modification of the previously saved Part element.  Validation checks on fields if they are empty as well as making sure Inv, Min, and
     * Max are the appropriate numbers.
     * @param actionEvent Unused in method.
     * */
    public void onModifySaveButtonClick(ActionEvent actionEvent) throws IOException {
        // Check for empty fields.
        if ( ModifyNameField.getText().trim().isEmpty() ) {
            popUpWarningOnSaveClick("Part Name");
            return;
        }
        else if (ModifyInvField.getText().trim().isEmpty()) {
            popUpWarningOnSaveClick("Inventory");
            return;
        }
        else if (ModifyPriceField.getText().trim().isEmpty()) {
            popUpWarningOnSaveClick("Price");
            return;
        }
        else if (ModifyMinField.getText().trim().isEmpty()) {
            popUpWarningOnSaveClick("Min");
            return;
        }
        else if (ModifyMaxField.getText().trim().isEmpty()) {
            popUpWarningOnSaveClick("Max");
            return;
        } else if (ModifyVariableField.getText().trim().isEmpty()) {
            if (OutsourcedRadio.isSelected()) {
                popUpWarningOnSaveClick("Outsourced");
                return;
            } else {
                popUpWarningOnSaveClick("Machine ID");
                return;
            }
        } else {
            int id = (Integer.parseInt(ModifyIDField.getText()));

            String partName = ModifyNameField.getText();
            int partInventory = Integer.parseInt(ModifyInvField.getText());
            double partPrice = Double.parseDouble(ModifyPriceField.getText());
            int partMin = Integer.parseInt(ModifyMinField.getText());
            int partMax = Integer.parseInt(ModifyMaxField.getText());

            if (inventoryValidator(partInventory, partMin, partMax)) {
                if (OutsourcedRadio.isSelected()) {
                    String CompanyName = ModifyVariableField.getText();
                    Inventory.updatePart(index, new Outsourced(id, partName, partPrice, partInventory, partMin, partMax, CompanyName));
                } else {
                    int partMachineId = Integer.parseInt(ModifyVariableField.getText());
                    Inventory.updatePart(index, new InHouse(id, partName, partPrice, partInventory, partMin, partMax, partMachineId));
                }
                backToMainScreen(actionEvent, "/view/mainform.fxml");
            }
        }
    }

    /** If the In House Radio button is selected, the ModifyVariableLabel will be changed to "Machine ID" and also wipe the field.
     * @param actionEvent caused by the User clicking on the Radio Button for "In House".
     * */
    public void onInhouseRadioSelect(ActionEvent actionEvent) {

        ModifyVariableLabel.setText("Machine ID");
        ModifyVariableField.setText("");
    }

    /** If the Out Sourced Radio button is selected, the ModifyVariableLabel will be changed to "Company Name" and also wipe the field.
     * @param actionEvent caused by the User clicking on the Radio Button for "Out Sourced".
     * */
    public void onOutsourcedRadioSelect(ActionEvent actionEvent) {
        ModifyVariableLabel.setText("Company Name");
        ModifyVariableField.setText("");
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
            }
        else if (dataType == Double.class)
            try {
                Double.parseDouble(ModifyPriceField.getText());
            } catch (NullPointerException | NumberFormatException ex) {
                Alert nonIntEntryAlert = new Alert(Alert.AlertType.WARNING);
                nonIntEntryAlert.setHeaderText("Your Input is not a Double in " + fieldName);
                nonIntEntryAlert.setContentText("Please RE-Enter a value in Currency Format!");
                Optional<ButtonType> option = nonIntEntryAlert.showAndWait();
            }
    }

    /** Method used to insure ONLY Integer values in the Part Inv field.  If a non-Integer/Number is entered a Pop-Up will warn user and clear field.
     * @param keyPress Unused in Method.
     * */
    public void onModifyInvFieldKeyTyped(KeyEvent keyPress) {
        preventNonNumericalDataEntry("Inv", Integer.class, ModifyInvField);
    }

    /** Method used to insure ONLY Integer values in the Part Min field.  If a non-Integer/Number is entered a Pop-Up will warn user and clear field.
     * @param keyPress Unused in Method.
     * */
    public void ModifyMinFieldKeyTyped(KeyEvent keyPress) {
        preventNonNumericalDataEntry("Min", Integer.class, ModifyMinField);
    }

    /** Method used to insure ONLY Integer values in the Part Max field.  If a non-Integer/Number is entered a Pop-Up will warn user and clear field.
     * @param keyPress Unused in Method.
     * */
    public void ModifyMaxFieldKeyTyped(KeyEvent keyPress) {
        preventNonNumericalDataEntry("Max", Integer.class, ModifyMaxField);
    }

    /** Method used to insure ONLY Double values in the Part Price field.  If a non-Doube/Number is entered a Pop-Up will warn user and clear field.
     * FUTURE-ENHANCEMENT: Use of a REGEX to validate would be much better than using try/catch, it would also eliminate odd behaviors during data entry.
     * @param keyEvent Unused in Method.
     * */
    public void onModifyPriceFieldKeyTyped(KeyEvent keyEvent) {
        preventNonNumericalDataEntry("Price", Double.class, ModifyPriceField);
    }

    /** Method used to insure ONLY Integer values in the Part Variable field when set on the Raido Button to "In House".  If a non-Integer/Number is entered a Pop-Up will warn user and clear field.
     * @param keyEvent Unused in Method.
     * */
    public void onModifyVariableFieldKeyPressed(KeyEvent keyEvent) {
        if(InhouseRadio.isSelected())
            preventNonNumericalDataEntry("Machine ID", Integer.class, ModifyVariableField);
    }
}
