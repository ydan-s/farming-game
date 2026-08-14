package farm.core;

import farm.customer.AddressBook;
import farm.customer.Customer;
import farm.inventory.BasicInventory;
import farm.inventory.Inventory;
import farm.inventory.product.Product;
import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;
import farm.sales.TransactionHistory;
import farm.sales.TransactionManager;
import farm.sales.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Top-level model class responsible for storing and
 * making updates to the data and smaller model entities
 * that make up the internal state of a farm.
 */
public class Farm extends Object {
    private Inventory inventory;
    private AddressBook addressBook;
    private TransactionHistory transactionHistory = new TransactionHistory();
    private TransactionManager transactionManager = new TransactionManager();

    /**
     * Creates a new farm instance with an inventory
     * and address book supplied.
     * @param inventory The inventory through which access to
     *                  the farm's stock is provisioned.
     * @param addressBook The address book storing the farm's
     *                    customer records.
     */
    public Farm(Inventory inventory,
                AddressBook addressBook) {
        this.inventory = inventory;
        this.addressBook = addressBook;
    }

    /**
     * Retrieves all customer records currently stored in the farm's address book.
     * @return a list of all customers in the address book
     */
    public List<Customer> getAllCustomers() {
        return addressBook.getAllRecords();
    }

    /**
     * Retrieves all products currently stored in the farm's inventory.
     * @return a list of all products in the inventory
     * @ensures the returned list is a shallow copy and cannot
     * modify the original inventory
     */
    public List<Product> getAllStock() {
        return new ArrayList<Product>(inventory.getAllProducts());
    }

    /**
     * Retrieves the farm's transaction manager.
     * @return the farm's transaction manager
     */
    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    /**
     * Retrieves the farm's transaction history.
     * @return the farm's transaction history
     */
    public TransactionHistory getTransactionHistory() {
        return transactionHistory;
    }

    /**
     * Saves the supplied customer in the farm's address book.
     * @param customer The customer to add to the address book.
     * @throws DuplicateCustomerException if the address book already
     * contains this customer
     */
    public void saveCustomer(Customer customer)
            throws DuplicateCustomerException {
        if (addressBook.containsCustomer(customer)) {
            throw new DuplicateCustomerException();
        }
        addressBook.addCustomer(customer);
    }

    /**
     * Adds a single product of the specified type and quality
     * to the farm's inventory.
     * @param barcode the product type to add to the inventory.
     * @param quality the quality of the product to add to the inventory.
     */
    public void stockProduct(Barcode barcode,
                             Quality quality) {
        inventory.addProduct(barcode, quality);
    }

    /**
     * Adds some quantity of products of the specified type and quality
     * to the farm's inventory. If quantity is less than 1, an
     * IllegalArgumentException should be thrown
     * @param barcode the product type to add to the inventory.
     * @param quality the quality of the product to add to the inventory.
     * @param quantity the number of products to add to the inventory.
     * @throws InvalidStockRequestException if the quantity is greater than 1
     * when a FancyInventory is not in use.
     */
    public void stockProduct(Barcode barcode, Quality quality, int quantity)
            throws InvalidStockRequestException {
        if (quantity < 1) {
            throw new IllegalArgumentException(
                    "Quantity must be at least 1.");
        }
        inventory.addProduct(barcode, quality, quantity);
    }

    /**
     * Sets the provided transaction as the current ongoing transaction.
     * @param transaction the transaction to set as ongoing.
     * @throws FailedTransactionException if the farm's transaction
     * manager rejects the request to begin managing this transaction.
     */
    public void startTransaction(Transaction transaction)
            throws FailedTransactionException {
        getTransactionManager().setOngoingTransaction(transaction);
    }

    /**
     * Attempts to add a single product of the given type to the
     * customer's shopping cart.
     * @param barcode the product type to add.
     * @return the number of products successfully added to the cart.
     * i.e. if no products of this type exist in the inventory, this
     * method will return 0.
     * @throws FailedTransactionException if no transaction is ongoing.
     */
    public int addToCart(Barcode barcode)
            throws FailedTransactionException {
        if (!(getTransactionManager().hasOngoingTransaction())) {
            throw new FailedTransactionException(
                    "Cannot add to cart when no customer has started shopping.");
        }
        int success = 0;
        for (Product item : inventory.getAllProducts()) {
            if (item.getBarcode().equals(barcode)) {
                getTransactionManager().registerPendingPurchase(item);
                inventory.removeProduct(barcode);
                success = 1;
                break;
            }
        }
        return success;
    }

    /**
     * Attempts to add the specified number of products of the
     * given type to the customer's shopping cart.
     * @param barcode the product type to add.
     * @param quantity the number of products to add.
     * @return the number of products successfully added to the cart.
     * @throws FailedTransactionException if no transaction is ongoing,
     * or if the quantity is greater than 1 when a FancyInventory is not
     * in use.
     */
    public int addToCart(Barcode barcode,
                         int quantity)
            throws FailedTransactionException {
        if (!(getTransactionManager().hasOngoingTransaction())) {
            throw new FailedTransactionException(
                    "Cannot add to cart when no customer has started shopping.");
        }

        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1.");
        }

        if (inventory instanceof BasicInventory && quantity > 1) {
            throw new FailedTransactionException(
                    "Current inventory is not fancy enough. "
                            + "Please purchase products one at a time.");
        }
        List<Product> amountInStock = new ArrayList<Product>();
        List<Product> inventoryCopy = getAllStock();
        for (Product item : inventory.getAllProducts()) {
            if (item.getBarcode().equals(barcode)) {
                amountInStock.add(item);
            }
        }
        if (amountInStock.size() >= quantity) {
            for (int i = 0; i < quantity; i++) {
                getTransactionManager().registerPendingPurchase(
                        amountInStock.get(i));
            }
            inventory.removeProduct(barcode, quantity);
            return quantity;
        } else {
            for (Product product : amountInStock) {
                getTransactionManager().registerPendingPurchase(
                        product);
            }
            inventory.removeProduct(barcode, amountInStock.size());
            return amountInStock.size();

        }
    }

    /**
     * Closes the ongoing transaction. If items have been purchased
     * in this transaction, records the transaction in the farm's history.
     * @return true iff the finalised transaction contained products.
     * @throws FailedTransactionException if transaction cannot be closed.
     */
    public boolean checkout()
            throws FailedTransactionException {

        if (!(getTransactionManager().hasOngoingTransaction())) {
            throw new FailedTransactionException();
        }

        Transaction closingTransaction = getTransactionManager().closeCurrentTransaction();
        if (!closingTransaction.getPurchases().isEmpty()) {
            transactionHistory.recordTransaction(closingTransaction);
            return true;
        }
        return false;
    }

    /**
     * Retrieves the receipt associated with the most recent transaction.
     * @return the receipt associated with the most recent transaction.
     */
    public String getLastReceipt() {
        return transactionHistory.getLastTransaction().getReceipt();
    }

    /**
     * Retrieves a customer from the address book.
     * @param name the name of the customer.
     * @param phoneNumber the phone number of the customer.
     * @return the customer instance matching the name and phone number.
     * @throws CustomerNotFoundException if the customer does not exist in the address book.
     */
    public Customer getCustomer(String name, int phoneNumber)
            throws CustomerNotFoundException {
        return addressBook.getCustomer(name, phoneNumber);
    }

}
