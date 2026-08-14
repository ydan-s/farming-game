package farm.sales.transaction;

import farm.customer.Customer;
import farm.inventory.product.Product;
import farm.sales.Cart;
import farm.sales.ReceiptPrinter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Transactions keeps track of what items are to be
 * (or have been) purchased and by whom.
 */
public class Transaction extends Object {
    private String status;
    private Customer customer;
    private List<Product> purchases;
    private List<Product> cart;

    protected String getStatus() {
        return status;
    }

    /**
     * Constructs a new transaction for an associated customer.
     * @param customer the customer who is starting the
     *                  transaction (beginning to shop).
     */
    public Transaction(Customer customer) {
        this.customer = customer;
        this.status = "Active";
        this.cart = customer.getCart().getContents();
    }

    /**
     * Retrieves the customer associated with this transaction.
     * @return the customer of the transaction.
     */
    public Customer getAssociatedCustomer() {
        return customer;
    }

    /**
     * Retrieves all products associated with the transaction.
     * @return the list of purchases comprising the transaction.
     */
    public List<Product> getPurchases() {
        if (isFinalised()) {
            return purchases;
        } else {
            return cart;
        }
    }

    /**
     * Calculates the total price of all the current
     * products in the transaction.
     * @return the total price calculated.
     */
    public int getTotal() {
        int totalPrice = 0;
        if (isFinalised()) {
            for (Product item : purchases) {
                totalPrice = item.getBasePrice() + totalPrice;
            }
            return totalPrice;
        }
        for (Product item2 : cart) {
            totalPrice = item2.getBasePrice() + totalPrice;
        }
        return totalPrice;

    }

    /**
     * Determines if the transaction is finalised
     * (i.e. sale completed) or not.
     * @return true iff the transaction is over, else false.
     */
    public boolean isFinalised() {
        return status.equals("Finalised");
    }

    /**
     * Mark a transaction as finalised and update the
     * transaction's internal state accordingly.
     * This locks in all pending purchases previously added,
     * such that they are now treated as final purchases and
     * no additional modification can be made, and empties
     * the customer's cart.
     */
    public void finalise() {
        status = "Finalised";
        purchases = List.copyOf(cart);
        customer.getCart().setEmpty();
    }

    @Override
    public String toString() {
        if (isFinalised()) {
            return "Transaction {Customer: "
                    + customer.getName() + " | "
                    + "Phone Number: " + customer.getPhoneNumber()
                    + " | Address: " + customer.getAddress()
                    + ", Status: " + status
                    + ", Associated Products: "
                    + purchases + "}";
        }
        return "Transaction {Customer: "
                + customer.getName() + " | "
                + "Phone Number: " + customer.getPhoneNumber()
                + " | Address: " + customer.getAddress()
                + ", Status: " + status
                + ", Associated Products: " + cart + "}";

    }

    /**
     * Converts the transaction into a formatted receipt for
     * display, using the ReceiptPrinter.
     * @return the styled receipt representation of this transaction
     */
    public String getReceipt() {
        List<String> header = List.of("Item", "Price");
        List<Product> contents = getPurchases();
        String total = priceFormat(getTotal());
        if (isFinalised()) {
            return ReceiptPrinter.createReceipt(header, entities(contents),
                    total, customer.getName());
        }
        return ReceiptPrinter.createActiveReceipt();
    }

    /**
     * Returns the entities for the ReceiptPrinter.createReceipt() method.
     * It creates a list which contains another list that contains the
     * displayName and basePrice of each item in the cart/ purchased cart.
     * @param contents a list of products containing the items in the
     *                 purchased cart.
     * @return returns the list containing the lists of items (in displayName
     * and basePrice order)
     */
    private List<List<String>> entities(List<Product> contents) {
        List<List<String>> entitiesList = new ArrayList<List<String>>();
        for (Product item : contents) {
            String name = item.getDisplayName();
            String value = priceFormat(item.getBasePrice());

            List<String> items = new ArrayList<String>();
            items.add(name);
            items.add(value);

            entitiesList.add(items);
        }
        return entitiesList;
    }

    /**
     * Formats the basePrice of an item in standard price format rather
     * than in cents, e.g. 157c becomes $1.57.
     * @param basePrice the basePrice of the item
     * @return returns the String version of the formatted basePrice.
     */
    protected String priceFormat(int basePrice) {

        return '$' + String.format("%.2f", (double) basePrice / 100);
    }

}






