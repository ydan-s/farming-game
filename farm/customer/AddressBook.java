package farm.customer;

import farm.core.CustomerNotFoundException;
import farm.core.DuplicateCustomerException;

import java.util.ArrayList;
import java.util.List;

/**
 * The address book is where the farmer stores their customers' details.
 * Keeps track of all the customers that come and visit the farm.
 */
public class AddressBook extends Object {
    private List<Customer> entries = new ArrayList<Customer>();

    /**
     * Constructor for AddressBook.
     */
    public AddressBook() {
    }

    /**
     * Adds a new customer to the address book.
     * @param customer The customer to be added
     * @throws DuplicateCustomerException If the customer already exists
     * in the address book, this exception ensures the address book contains
     * no duplicate customers.
     */
    public void addCustomer(Customer customer) throws DuplicateCustomerException {

        if (!entries.contains(customer)) {
            entries.add(customer);
        } else {
            throw new DuplicateCustomerException(customer.toString());
        }

    }

    /**
     * Retrieves all customer records stored in the address book.
     * @return A list of all customers in the address book.
     */
    public List<Customer> getAllRecords() {
        return entries;
    }

    /**
     * Checks to see if a customer is already in the address book.
     * @param customer Ther customer to check.
     * @return True iff the customer already exists, else false.
     */
    public boolean containsCustomer(Customer customer) {
        for (Customer c : entries) {
            if (customer.equals(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Looks up a customer in the address book, if they exist using
     * their details.
     * @param name The name of the customer to lookup.
     * @param phoneNumber The phone number of the customer.
     * @return The customer iff they exist in the address book.
     * @throws CustomerNotFoundException if there is no customer matching
     * the information in the address book.
     */
    public Customer getCustomer(String name, int phoneNumber)
            throws CustomerNotFoundException {

        for (Customer c : entries) {
            if (c.getName().equals(name)) {
                if (c.getPhoneNumber() == phoneNumber) {
                    return c;
                }
            }
        }

        throw new CustomerNotFoundException();
    }
}
