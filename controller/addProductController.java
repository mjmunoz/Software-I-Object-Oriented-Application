package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import model.*;


import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;


/** addProductController - Controller for the addproductform.fxml
 *
 * @author Mark Munoz
 * */
public class addProductController implements Initializable
{
    private ObservableList<Part> userSelectedParts = FXCollections.observableArrayList();
    private ObservableList<Product> userCreatedProduct = FXCollections.observableArrayList();

    public TextField ProductNameField;
    public TextField ProductInvField;
    public TextField ProductPriceField;
    public TextField ProductMaxField;
    public TextField ProductMinField;
    public TextField AddPartsSearchField;
    public TableView <Part> availablePartsTableView;
    public TableColumn <Part, Integer> PartID;
    public TableColumn <Part, String> PartName;
    public TableColumn <Part, Integer> PartInventoryLevel;
    public TableColumn <Part, Double> PartPrice;
    public TableView <Part> associatedPartsTableView;
    public TableColumn <Part, Integer> AssociatedPartID;
    public TableColumn <Part, String> AssociatedPartName;
    public TableColumn <Part, Integer> AssociatedPartInv;
    public TableColumn <Part, Double> AssociatedPartPrice;

    Stage stage;
    Parent scene;

    /** Method returns to Main Screen FXML.
     * @param actionEvent  Unused in Method
     * @param resourceName The file location of the FXML to return to.
     * */
    private void backToMainScreen(ActionEvent actionEvent, String resourceName) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/mainform.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Generates a unique Part ID
     *
     * @return timeComposite an Integer value of (10000 *Localtime Hour) + (100 x Localtime Minute) + (Localtime Second)
     */
    private int uniqueProductIdGeneration() {
        LocalDateTime currentTime = LocalDateTime.now();
        int timeComposite = (10000 * currentTime.getHour()) + (100 * currentTime.getMinute()) + currentTime.getSecond();

        if (Inventory.lookupProduct(timeComposite) == null)
            return timeComposite;
        else {
            Random magicNumber = new Random();
            timeComposite += magicNumber.nextInt(0, 9);
            return timeComposite;
        }
    }

    /** Checks to see if Stock is between the min/max amount and checks if max is less than min, gives a User a Pop-Up Alert with information of error..
     *
     * @param currentStock Integer value from the Inv field.
     * @param minStock     Integer value from the Min field.
     * @param maxStock     Integer value from the Max field
     * @return False if stock criteria not met, True if User Input is valid.
     */
    private boolean inventoryValidator(int currentStock, int minStock, int maxStock)
    {
        if ((minStock > maxStock) || (currentStock > maxStock) || (currentStock < minStock)) {
            Alert deleteAlert = new Alert(Alert.AlertType.WARNING);
            deleteAlert.setHeaderText("Stock and Min/Max Error");
            deleteAlert.setContentText("Inventory must be between Min and Max Stock.\n" +"And Min must be less than Max Stock!\n"
                    + "Inv.: " + currentStock + "\tMax: " + maxStock + "\tMin: "+minStock);

            Optional<ButtonType> option = deleteAlert.showAndWait();
            return false;
        }
        else
            return true;
    }

    /** Part of the JavaFX GUI Lifecycle.  When Add Product FXML is initiated, the AvailablePartTable will be populated.
     * @param url Unused in current controller.
     * @param resourceBundle Unused in current controller.
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        availablePartsTableView.setItems(Inventory.getAllParts());
        PartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        PartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        PartInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        PartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        associatedPartsTableView.setItems(userSelectedParts);
        AssociatedPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        AssociatedPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        AssociatedPartInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        AssociatedPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
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

    /** Save Button for saving User Data into the Product ArrayList.  TextFields are checked for empty fields to avoid Runtime errors.
     * On completion of checks and using the appropriate ArrayList return to the Main Screen.
     * @param actionEvent cause by the firing of a mouse click on the Save Button.
     * */
    public void onProductSaveButtonClick(ActionEvent actionEvent) throws IOException {
        // Check for empty fields.
        if ( ProductNameField.getText().trim().isEmpty() ) {
            popUpWarningOnSaveClick("Part Name");
            return;
        }
        else if (ProductInvField.getText().trim().isEmpty()) {
            popUpWarningOnSaveClick("Inventory");
            return;
        }
        else if (ProductPriceField.getText().trim().isEmpty()) {
            popUpWarningOnSaveClick("Price");
            return;
        }
        else if (ProductMinField.getText().trim().isEmpty()) {
            popUpWarningOnSaveClick("Min");
            return;
        }
        else if (ProductMaxField.getText().trim().isEmpty()) {
            popUpWarningOnSaveClick("Max");
            return;
        } else {
            String productName = ProductNameField.getText();
            int productInv = Integer.parseInt(ProductInvField.getText());
            double productPrice = Double.parseDouble(ProductPriceField.getText());
            int productMin = Integer.parseInt(ProductMinField.getText());
            int productMax = Integer.parseInt(ProductMaxField.getText());

            if (inventoryValidator(productInv, productMin, productMax)) {
                Product tempProduct = new Product( uniqueProductIdGeneration(), productName, productPrice, productInv, productMin, productMax);
                userCreatedProduct.add(tempProduct);
                for (Part tempPart: userSelectedParts) {
                    System.out.println("Parts Added:" + tempPart.getName());
                    tempProduct.addAssociatedPart(tempPart);
                }
                Inventory.addProduct(tempProduct);

                backToMainScreen(actionEvent, "/view/mainform.fxml");
            } else return;
        }
    }


    /** On Cancel button return to the Main Screen.
     * @param actionEvent Unused in method.
     * */
    public void onProductCancelButtonClick(ActionEvent actionEvent) throws IOException {
        backToMainScreen(actionEvent,"/view/mainform.fxml");
    }

    /** Adds a Part from the Available Parts Table and onto the userSelectedPart List.
     * @param actionEvent Unused in method.
     * */
    public void onProductPartAddButtonClick(ActionEvent actionEvent)  {
        Part chosenPart = availablePartsTableView.getSelectionModel().getSelectedItem();
        if ( !(chosenPart == null) ) {
            userSelectedParts.add(chosenPart);
            associatedPartsTableView.setItems(userSelectedParts);
        }
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
                Double.parseDouble(ProductPriceField.getText());
            } catch (NullPointerException | NumberFormatException ex) {
                Alert nonIntEntryAlert = new Alert(Alert.AlertType.WARNING);
                nonIntEntryAlert.setHeaderText("Your Input is not a Double in " + fieldName);
                nonIntEntryAlert.setContentText("Please RE-Enter a value in Currency Format!");
                Optional<ButtonType> option = nonIntEntryAlert.showAndWait();

                textFieldUsed.setText("");
            }
    }

    /** Method used to insure ONLY Integer values in the Product Inv field.  If a non-Integer/Number is entered a Pop-Up will warn user and clear field.
     * @param keyEvent  Unused in Method.
     * */
    public void onProductInvFieldKeyPress(KeyEvent keyEvent) {
        preventNonNumericalDataEntry("Inv", Integer.class, ProductInvField);
    }

    /** Method used to insure ONLY Integer values in the Product Min field.  If a non-Integer/Number is entered a Pop-Up will warn user and clear field.
     * @param keyEvent Unused in Method.
     * */
    public void onProductMinFieldKeyPress(KeyEvent keyEvent) {
        preventNonNumericalDataEntry("Min", Integer.class, ProductMinField);
    }

    /** Method used to insure ONLY Integer values in the Product Max field.  If a non-Integer/Number is entered a Pop-Up will warn user and clear field.
     * @param keyEvent  Unused in Method.
     * */
    public void onProductMaxFieldKeyPress(KeyEvent keyEvent) {
        preventNonNumericalDataEntry("Max", Integer.class, ProductMaxField);
    }

    /** Method used to insure ONLY Double values in the Product Price field.  If a non-Doube/Number is entered a Pop-Up will warn user and clear field.
     * FUTURE-ENHANCEMENT: Use of a REGEX to validate would be much better than using try/catch, it would also eliminate odd behaviors during data entry.
     * @param keyEvent s Unused in Method.
     * */
    public void onProductPriceKeyFieldPress(KeyEvent keyEvent) {
        preventNonNumericalDataEntry("Price", Double.class, ProductPriceField);
    }

    /** User selected Part from the AssociatedPartTableView will then be "deleted" from only the AssociatePartTableView List.  The actual Inventory.allParts will be untouched.
     * @param actionEvent  Unused by method
     * */
    public void onPartRemoveAssociatedPart(ActionEvent actionEvent) {
        if(!associatedPartsTableView.getSelectionModel().isEmpty()) {
            Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
            deleteAlert.setContentText("Press OK to Delete Product, else CANCEL");
            deleteAlert.setHeaderText("Deletion Confirmation?");

            Optional<ButtonType> option = deleteAlert.showAndWait();
            if (option.get().equals(ButtonType.OK)) {
                int chosenProduct = associatedPartsTableView.getSelectionModel().getSelectedIndex();
                if (!associatedPartsTableView.getSelectionModel().isEmpty()) {
                    associatedPartsTableView.getItems().remove(chosenProduct);
                }
            }
        }
    }

    /** Part Search will either take a Part Number or the Part Name, which will then be posted on the availablePartsTableView.
     * LOGICAL ERROR: Similarly to the "mainFormController" Class, originally onKeyTyped was used to capture User Input.  It would constantly
     * trigger a Invalid Input Warning while typing. The correction was to attempt using a "Search Button", but required a second click from
     * User in an empty Search Bar to "reset" the Table, that was not acceptable.  The final correction used onKeyTyped.  This resolved the
     * Invalid Input Warnings while typing while maintaining the User expectation of Backspacing/Deleting the Search Textfield to restore
     * the items in the Table View.
     * @param keyEvent Used to get detect 'ENTER' Key Press to process Search.  A second 'ENTER' Press in an empty field repopulates PartTableView.
     * */
    public void AddPartsSearchOkKeyPress(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(keyEvent.getCode().ENTER)) {
            String userInput = AddPartsSearchField.getText().trim();
            try {
                ObservableList<Part> foundPartWithId = FXCollections.observableArrayList();
                int partNumber = Integer.parseInt(userInput);

                Part partIdSearch = Inventory.lookupPart(partNumber);
                if (!(partIdSearch == null)) {
                    foundPartWithId.add(partIdSearch);
                    availablePartsTableView.setItems(foundPartWithId);
                } else {
                    Alert idSearchFail = new Alert(Alert.AlertType.ERROR);
                    idSearchFail.setContentText("Unable to Find Part ID with that Number!");
                    idSearchFail.setHeaderText("Press OK to continue!");
                    Optional<ButtonType> DeleteFailOption = idSearchFail.showAndWait();
                }
            } catch (NumberFormatException e) {
                ObservableList<Part> foundPartWithName = Inventory.lookupPart(userInput);

                if (!(foundPartWithName.isEmpty())) {
                    availablePartsTableView.setItems(foundPartWithName);
                } else {
                    Alert nameSearchFail = new Alert(Alert.AlertType.ERROR);
                    nameSearchFail.setContentText("Unable to Find Part with that Name!");
                    nameSearchFail.setHeaderText("Press OK to continue!");
                    Optional<ButtonType> DeleteFailOption = nameSearchFail.showAndWait();
                }
            }
        }
    }
}
