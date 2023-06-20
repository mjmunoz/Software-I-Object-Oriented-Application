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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** mainFormController - Main Form Controller Class for the Start/Initial FXML Screen of the Applications.
 * @author Mark Munoz
 * */
public class mainFormController  implements Initializable {
    public TableView  <Part> PartTableView;
    public TableColumn  <Part,Integer> PartID;
    public TableColumn  <Part,String> PartName;
    public TableColumn <Part,Integer>PartInventoryLevel;
    public TableColumn <Part,Double> PartPrice;
    public TableView <Product> ProductTableView;
    public TableColumn <Product, Integer> ProductID;
    public TableColumn <Product,String>ProductName;
    public TableColumn <Product, Integer>ProductInventoryLevel;
    public TableColumn <Product, Double> ProductPrice;
    public TextField PartsSearchField;
    public TextField ProductSearchField;

    Stage stage;
    Parent scene;

    /** Part of the JavaFX GUI Lifecycle.  When the Main Form FXML is initiated, the two TableViews will be populated.
     * @param url Unused in current controller.
     * @param resourceBundle Unused in current controller.
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PartTableView.setItems(Inventory.getAllParts());
        PartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        PartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        PartInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        PartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        ProductTableView.setItems(Inventory.getAllProducts());
        ProductID.setCellValueFactory(new PropertyValueFactory<>("id"));
        ProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        ProductInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        ProductPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /** Closes the app.
     * @param actionEvent Unused by method.
     * */
    public void onExitButtonClick(ActionEvent actionEvent) {
         System.exit(0);
    }

    /** Add Part Button will transition into another Window that will perform data entry for the Part List.
     * @param actionEvent Unused by method.
     * */
    public void onAddPartButtonClick(ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/addpart.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** User selected Part via Part Table View will then be sent to the Modify Part Window that will perform modification on the Part List on the selected part.
     * @param actionEvent  Unused by method
     * */
    public void onModifyPartButtonClick(ActionEvent actionEvent) throws IOException {
        Part chosenPart = PartTableView.getSelectionModel().getSelectedItem();
        if ( chosenPart == null) {
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/modifyparts.fxml"));
            Parent root = loader.load();

            modifyPartController dataToModifyPartController = loader.getController();
            dataToModifyPartController.setParts(chosenPart);

            Stage goModifyPartsStage = new Stage();
            goModifyPartsStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            goModifyPartsStage.setScene(new Scene(root));
            goModifyPartsStage.show();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /** User selected Part via Part Table View will then be deleted from the Inventory.allParts List as well as the Part Table View List.
     * RUNTIME ERROR: Similar to the Product Delete Method, if there is no checking if a selection in the TableView isn't made, a
     * Runtime error occurs.  The solution for the problem was to test if a selection is made with the isEmpty method of the TableView class.
     * The Deletion Method cannot be attempted without first checking if the isEmpty is "NOT" true.
     * @param actionEvent  Unused by method
     * */
    public void onDeletePartButtonClick(ActionEvent actionEvent) {
        if(!PartTableView.getSelectionModel().isEmpty()) {
            // Give warning about Delete Operation.
            Alert deletePartAlert = new Alert(Alert.AlertType.CONFIRMATION);
            deletePartAlert.setContentText("Press OK to Delete Product, else CANCEL");
            deletePartAlert.setHeaderText("Deletion Confirmation?");
            Optional<ButtonType> option = deletePartAlert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                Part tempPart = PartTableView.getSelectionModel().getSelectedItem();
                int selectedPartID = tempPart.getId();

                // Give Pop Up Warning if unable to delete Part.
                if( !Inventory.deletePart(Inventory.lookupPart(selectedPartID)) ) {
                    Alert deleteFail = new Alert(Alert.AlertType.ERROR);
                    deletePartAlert.setContentText("Error! Unable to Delete Part");
                    deletePartAlert.setHeaderText("Press OK to continue!");
                    Optional<ButtonType> DeleteFailOption = deletePartAlert.showAndWait();
                }
            }
        }
    }

    /** Add Product Button will transition into another Window that will perform data entry for the Product List.
     * @param actionEvent Unused by method.
     * @throws IOException
     * */
    public void onAddProductButtonClick(ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/addproductform.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Method for the Modify Product Button
     * @param actionEvent Unused by method.
     * */
    public void onModifyProductButtonClick(ActionEvent actionEvent) {
        Product chosenProduct = ProductTableView.getSelectionModel().getSelectedItem();
        if ( chosenProduct == null) {
            return;
        }
        try {
            FXMLLoader loaderModProductForm = new FXMLLoader(getClass().getResource("/view/modifyproductform.fxml"));
            Parent root = loaderModProductForm.load();

            modProductController dataToModifyProductController = loaderModProductForm.getController();
            dataToModifyProductController.setProducts(chosenProduct);

            Stage goModifyPartsStage = new Stage();
            goModifyPartsStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            goModifyPartsStage.setScene(new Scene(root));
            goModifyPartsStage.show();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /** User selected Product via Product Table View will then be deleted from the Inventory.allProducts List as well as the Product Table View List.
     * RUNTIME ERROR: If there is no checking if a selection in the TableView isn't made, a Runtime error occurs.  The solution for the problem was
     * to test if a selection is made with the isEmpty method of the TableView class.  The Deletion Method cannot be attempted without first checking
     * if the isEmpty is "NOT" true.
     * @param actionEvent  Unused by method
     * */
    public void onDeleteProductButtonClick(ActionEvent actionEvent) {
        if(!ProductTableView.getSelectionModel().isEmpty())  {
            // Give warning about Delete Operation.
            Alert deleteProductAlert = new Alert(Alert.AlertType.CONFIRMATION);
            deleteProductAlert.setContentText("Press OK to Delete Product, else CANCEL");
            deleteProductAlert.setHeaderText("Deletion Confirmation?");
            Optional<ButtonType> option = deleteProductAlert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                Product tempProduct = ProductTableView.getSelectionModel().getSelectedItem();
                int selectedProdID = tempProduct.getId();

                if(tempProduct.getAllAssociatedParts().isEmpty()) {
                    // Give Pop Up Warning if unable to delete Part.
                    if (!Inventory.deleteProduct(Inventory.lookupProduct(selectedProdID))) {
                        Alert deleteProductFail = new Alert(Alert.AlertType.ERROR);
                        deleteProductFail.setContentText("Error! Unable to Delete Product");
                        deleteProductFail.setHeaderText("Press OK to continue!");
                        Optional<ButtonType> DeleteProductFailOption = deleteProductFail.showAndWait();
                    }
                } else {
                    Alert deleteAssociatedPartFail = new Alert(Alert.AlertType.ERROR);
                    deleteAssociatedPartFail.setContentText("Error! Part Associated with Product! \nCannont Delete!");
                    deleteAssociatedPartFail.setHeaderText("Press OK to continue!");
                    Optional<ButtonType> DeleteAssociatedPartFailOption = deleteAssociatedPartFail.showAndWait();
                }
            }
        }
    }

    /** Product Search will either take a Product Number or the Product Name, which will then be posted on the Product Table View.
     * LOGICAL ERROR: Originally the method used onKeyTyped to capture User Input.  It would constantly trigger a Invalid Input Warning while typing.
     * The correction was to attempt using a Button, but required a second click from User in an empty Search Bar to "reset" the Table,
     * that was not acceptable.  The final correction used onKeyTyped.  This resolved the Invalid Input Warnings while typing while maintaining
     * the User's expectation of Backspacing/Deleting the Search Textfield to restore the items in the Table View.
     * @param keyEvent Used to get detect 'ENTER' Key Press to process Search.  A second 'ENTER' Press in an empty field repopulates ProductsTableView.
     * */
    public void ProductsSearchOnKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(keyEvent.getCode().ENTER)) {
            String userInput = ProductSearchField.getText().trim();
            try {
                ObservableList<Product> foundProductWithId = FXCollections.observableArrayList();
                int productNumber = Integer.parseInt(userInput);

                Product productIdSearch = Inventory.lookupProduct(productNumber);

                if (!(productIdSearch == null)) {
                    foundProductWithId.add(productIdSearch);
                    ProductTableView.setItems(foundProductWithId);
                } else {
                    Alert idSearchFail = new Alert(Alert.AlertType.ERROR);
                    idSearchFail.setContentText("Unable to Find Product ID with that Number!");
                    idSearchFail.setHeaderText("Press OK to continue!");
                    Optional<ButtonType> DeleteFailOption = idSearchFail.showAndWait();
                }
            } catch (NumberFormatException e) {
                ObservableList<Product> foundProductWithName = Inventory.lookupProduct(userInput);

                if (!(foundProductWithName.isEmpty())) {
                    ProductTableView.setItems(foundProductWithName);
                } else {
                    Alert nameSearchFail = new Alert(Alert.AlertType.ERROR);
                    nameSearchFail.setContentText("Unable to Find Product with that Name!");
                    nameSearchFail.setHeaderText("Press OK to continue!");
                    Optional<ButtonType> DeleteFailOption = nameSearchFail.showAndWait();
                }
            }
        }
    }

    /** Part Search will either take a Part Number or the Part Name, which will then be posted on the Part Table View
     * LOGICAL ERROR: Similarly to the ProductTable View Fix, originally using onKeyTyped to capture User Input.  It would constantly trigger a Invalid
     * Input Warning while typing. The correction was to attempt using a "Search Button", but required a second click from User in an empty Search Bar to
     * "reset" the Table, that was not acceptable.  The final correction used onKeyTyped.  This resolved the Invalid Input Warnings while typing while
     * maintaining the User's expectation of Backspacing/Deleting the Search Textfield to restore the items in the Table View.
     * @param keyEvent Used to get detect 'ENTER' Key Press to process Search.  A second 'ENTER' Press in an empty field repopulates PartTableView.
     * */
    public void PartsSearchOnKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(keyEvent.getCode().ENTER)) {
            String userInput = PartsSearchField.getText().trim();
            try {
                ObservableList<Part> foundPartWithId = FXCollections.observableArrayList();
                int partNumber = Integer.parseInt(userInput);

                Part partIdSearch = Inventory.lookupPart(partNumber);
                if (!(partIdSearch == null)) {
                    foundPartWithId.add(partIdSearch);
                    PartTableView.setItems(foundPartWithId);
                } else {
                    Alert idSearchFail = new Alert(Alert.AlertType.ERROR);
                    idSearchFail.setContentText("Unable to Find Part ID with that Number!");
                    idSearchFail.setHeaderText("Press OK to continue!");
                    Optional<ButtonType> DeleteFailOption = idSearchFail.showAndWait();
                }
            } catch (NumberFormatException e) {
                ObservableList<Part> foundPartWithName = Inventory.lookupPart(userInput);

                if (!(foundPartWithName.isEmpty())) {
                    PartTableView.setItems(foundPartWithName);
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
