package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Product;

import java.io.IOException;

/** Main Class for project.
 * @author Mark Munoz
 *
 * Javadoc Folder is located in: ./Javadoc of the Zip of the project.
 *
 * */
public class InventoryManagementSystem extends Application {

    @Override
    public void init()
    {
        // For future developement.
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InventoryManagementSystem.class.getResource("/view/mainform.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Inventory Management System!");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop()
    {
        System.out.println("Program End");

    }
    /** Main Method for the Inventory Management System. First method that is called when Java program is run.
     * @param args Commandline Arguments, not used in Project.
     * */
    public static void main(String[] args) {
        InHouse defaultPartData1 = new InHouse(1, "Brakes", 15.01, 10, 1,19, 10);
        InHouse defaultPartData2 = new InHouse(2, "Wheel", 11.02, 16, 1,20, 12);
        Outsourced defaultPartData3 = new Outsourced(3, "Seat", 15.03, 10, 1,21, "HAL");

        Inventory.addPart(defaultPartData1);
        Inventory.addPart(defaultPartData2);
        Inventory.addPart(defaultPartData3);

        Product defaultProductData1 = new Product(1000,"Giant Bike", 299.99, 5, 5, 20);
        defaultProductData1.addAssociatedPart(defaultPartData2);
        Product defaultProductData2 = new Product(1001,"Tricycle", 99.99, 3, 2, 16);
        defaultProductData2.addAssociatedPart(defaultPartData2);
        defaultProductData2.addAssociatedPart(defaultPartData3);

        Inventory.addProduct(defaultProductData1);
        Inventory.addProduct(defaultProductData2);

        launch();
    }
}