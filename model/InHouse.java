package model;

/** InHouse class as defined by the Project's UML for the Model Package.
 * @author Mark Munoz
 * */
public class InHouse extends Part{
    private int machineId;

    /** Constructor for the InHouse class.
     * @param id Integer PartId
     * @param name String PartName
     * @param price Double PartPrice
     * @param stock Integer stock/inventor
     * @param min Integer value for Minimum stock.
     * @param max Integer value for Maximum stock/inventory.
     * @param machineId Integer value for MachineID*/
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId)
    {
        // Super Constructor for Part Class.
        super(id, name, price, stock, min, max);
        setMachineId(machineId);
    }

    /** Setter method for MachineID unique to class
     * @param machineId Integer value for machineID
     * */
    public void setMachineId(int machineId)
    {
        this.machineId = machineId;
    }

    /** Getter method for MachineID unique to class
     * @return machineId Integer value for machineID
     * */
    public int getMachineId()
    {
        return this.machineId;
    }
}
