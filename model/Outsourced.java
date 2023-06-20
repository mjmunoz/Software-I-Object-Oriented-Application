package model;

/** Outsourced class as defined by the Project's UML for the Model Package.
 * @author Mark Munoz
 * */
public class Outsourced extends Part {
        private String companyName;

        /** Constructor for the Outsourced class.
         * @param id Integer PartId
         * @param name String PartName
         * @param price Double PartPrice
         * @param stock Integer stock/inventor
         * @param min Integer value for Minimum stock.
         * @param max Integer value for Maximum stock/inventory.
         * @param CompanyName  String of Outsourced Company Name.*/
        public Outsourced(int id, String name, double price, int stock, int min, int max, String CompanyName)
        {
            // Super Constructor for Part Class.
            super(id, name, price, stock, min, max);
            setCompanyName(CompanyName);
        }

        /** Setter method for Company Name used for Outsourced Parts
         * @param companyName String used to store Company Name
         * */
        public void setCompanyName(String companyName)
        {
            this.companyName = companyName;
        }

        /** Getter method for Company Name used for Outsourced Parts
         * @return companyName String used to store Company Name
         * */
        public String getCompanyName()
        {
            return companyName;
        }
}
