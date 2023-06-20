package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Product Class as defined by the Project's UML for the Model Package.
 * @author Mark Munoz
 * */
public class Product {

    private final ObservableList<Part> associateParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /** Constructor for the Product class.
     * @param id Integer PartId
     * @param name String PartName
     * @param price Double PartPrice
     * @param stock Integer stock/inventor
     * @param min Integer value for Minimum stock.
     * @param max Integer value for Maximum stock/inventory.*/
    public Product (int id, String name, double price, int stock, int min, int max)
    {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /** ID Setter for Product Class.
     * @param id Integer for Product ID.
     * */
    public void setId(int id)
    {
        this.id = id;
    }

    /** Name Setter for Product Class.
     * @param name String for Product Name.
     * */
    public void setName(String name)
    {
        this.name = name;
    }

    /** Price Setter for Product Class.
     * @param price Double for Product Price.
     * */
    public void setPrice(double price)
    {
        this.price = price;
    }

    /** Current Stock/Inventory Setter for Product Class.
     * @param stock Integer for Product Current Inventory/Stock amount.
     * */
    public void setStock(int stock)
    {
        this.stock = stock;
    }

    /** Minimum Stock/Inventory Setter for Product Class.
     * @param min  Integer for Product Minimum Inventory/Stock amount.
     * */
    public void setMin(int min)
    {
        this.min = min;
    }

    /** Maximum Stock/Inventory Setter for Product Class.
     * @param max  Integer for Product Maximum Inventory/Stock amount.
     * */
    public void setMax(int max)
    {
        this.max = max;
    }
    /** ID Getter for Product Class.
     * @return id Integer value that holds the Product ID.
     * */
    public int getId()
    {
        return id;
    }

    /** Name Getter for Product Class.
     * @return name String value that holds the Product Name.
     * */
    public String getName()
    {
        return name;
    }

    /** Price Getter for Product Class.
     * @return price Double value that holds the Product Price.
     * */
    public double getPrice()
    {
        return price;
    }

    /** Current Stock/Inventory Getter for Product Class.
     * @return stock Integer Value that returns the Product Current Inventory/Stock amount.
     * */
    public int getStock()
    {
        return stock;
    }

    /** Minimum Stock/Inventory Getter for Product Class.
     * @return min Integer value that returns the Product Minimum Inventory/Stock amount.
     * */
    public int getMin()
    {
        return min;
    }

    /** Maximum Stock/Inventory Getter for Product Class.
     * @return max Integer value that returns the Product Maximum Inventory/Stock amount.
     * */
    public int getMax()
    {
        return max;
    }

    /**
     * Add a Part Object into the Product ObservableList.
     * @param part Arguement takes a Part object to be added to ArrayList.
     */
    public void addAssociatedPart(Part part)
    {
        associateParts.add(part);
    }

    /** Delete an associated Part Object from the Product ObservableList.
     * @param associatedPart  Part Object selected by User to be deleted from the Product.
     * @return True/False, if successfully deleted report True.
     * */
    public boolean deleteAssociatedPart(Part associatedPart)
    {
        return getAllAssociatedParts().remove(associatedPart);
    }

    /** Get all Part Objects in the Product ObservableList.
     * @return Part Object for all Parts associated with the Product.
     * */
    public ObservableList <Part> getAllAssociatedParts()
    {
        return associateParts;
    }
}
