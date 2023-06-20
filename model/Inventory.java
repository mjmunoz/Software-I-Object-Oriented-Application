package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Locale;

/** Inventory Class as defined by the Project's UML for the Model Package.
 * @author Mark Munoz
 * */
public class Inventory
{

    /** A JavaFX Arraylist that will hold the Parts List.
     * */
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();

    /** A JavaFX Arraylist that will hold the Product List.
     * */
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /** Method adds Part element to allParts ObservableList.
     * @param newPart Part element to be added allParts ObservableList.
     * */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /** Method adds Product element to allProducts ObservableList.
     * @param newProduct Product element to be added to allProducts ObservableList.
     * */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /** Method looks/searches for PartID in the allParts ObservableList.
     * @param partId An Integer value of the Unique Part ID number.
     * @return Returns result of search or returns null if no match found.
     * */
    public static Part lookupPart(int partId)
    {
        Part temp = null;
        for(Part loopParts: getAllParts())
        {
            if( loopParts.getId() == partId) {
                 temp = loopParts;
                 return temp;
            }
        }
        return temp;
    }

    /** Method looks/searches through the allParts ObservableList using a String.
     * @param nameSearch A String that will "contain" part of or all of the Name to be searched with Part.getName() method.
     * @return Return value will be a Part element with the searched for String, or an empty Part element.
     * */
    public static ObservableList<Part> lookupPart(String nameSearch)
    {
        ObservableList<Part> foundParts = FXCollections.observableArrayList();
        ObservableList<Part> copyOfAllParts = Inventory.getAllParts();

        for(Part temp: copyOfAllParts)
        {
            if (temp.getName().toLowerCase().contains(nameSearch.toLowerCase()))
                foundParts.add(temp);
        }
        return foundParts;
    }

    /** Method looks/searches for PartID in the allParts ObservableList.
     * @param productId An Integer value of the Unique Product ID number.
     * @return Returns Product result of search or returns null if no match found.
     * */
    public static Product lookupProduct(int productId)
    {
        Product temp = null;
        for(Product loopProducts: getAllProducts())
        {
            if( loopProducts.getId() == productId) {
                temp = loopProducts;
            }
        }
        return temp;
    }

    /** Method looks/searches through the allProducts ObservableList using a String.
     * @param nameSearch A String that will "contain" part of or all of the Name to be searched with Product.getName() method.
     * @return Return value will be a Part element with the searched for String, or an empty Part element.
     * */
    public static ObservableList<Product> lookupProduct(String nameSearch)
    {
        ObservableList<Product> foundProducts = FXCollections.observableArrayList();
        ObservableList<Product> copyOfAllProducts = Inventory.getAllProducts();

        for(Product temp: copyOfAllProducts)
        {
            if (temp.getName().toLowerCase().contains(nameSearch.toLowerCase()))
                foundProducts.add(temp);
        }
        return foundProducts;
    }

    /** Method "sets"/updates Part element in allParts ObservableList.
     * @param index Index in Parts ObservableList.
     * @param selectedPart Part element to be modified on the allParts ObservableList.
     * */
    public static void updatePart (int index, Part selectedPart)
    {
        allParts.set(index, selectedPart);
    }

    /** Method "sets"/updates Part element in allParts ObservableList.
     * @param index Index in Product ObservableList.
     * @param selectedProduct Product element to be modified on the allProducts ObservableList.
     * */
    public static void updateProduct (int index, Product selectedProduct)
    {
        allProducts.set(index, selectedProduct);
    }

    /** Method deletes/removes Part element from allParts ObservableList.
     * @param selectedPart Part element to be removed from allParts ObservableList.
     * @return True if remove is successful, or False if failed.
     * */
    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);  // Remove returns T/F on removal of element
    }

    /** Method deletes/removes Product element from allProducts ObservableList.
     * @param selectedProduct Product element to be removed from allProducts ObservableList.
     * @return True if remove is successful, or False if failed.
     * */
    public static boolean deleteProduct(Product selectedProduct) {
     boolean state;
        state = allProducts.remove(selectedProduct);
        if(state == true)
            return true;
        else
            return false;
    }

    /** Method returns the allParts ObservableList.
     * @return allParts ObservableList is returned.
     * */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /** Method returns the allProducts ObservableList.
     * @return allProducts ObservableList is returned.
     * */
    public static ObservableList<Product> getAllProducts()
    {
        return allProducts;
    }
}
