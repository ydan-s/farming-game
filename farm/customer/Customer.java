package farm.customer;

import farm.sales.Cart;

import java.util.Objects;

/**
 * A customer who interacts with the farmer's business.
 * Keeps a record of the customer's information.
 */
public class Customer extends Object {
    private String name;
    private int phoneNumber;
    private String address;
    private Cart cart = new Cart();

    /**
     * Create a new customer instance with their details.
     * @param name The name of the customer.
     * @param phoneNumber The customer's phone number.
     * @param address The address of the customer.
     */
    public Customer(String name, int phoneNumber, String address) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        cart.setEmpty();
    }

    /**
     * Retrieves the name of the customer.
     * @return The customer name.
     */
    public String getName() {
        return name;
    }

    /**
     * Update the current name of the customer with a new one.
     * @param newName  The new name to override the current name.
     */
    public void setName(String newName) {
        name = newName;
    }

    /**
     * Retrieve the phone number of the customer.
     * @return The customer's phone number.
     */
    public int getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Set the current phone number of the customer to be newPhone.
     * @param phoneNumber The phone number to override the current phone number.
     */
    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Retrieve the address of the customer.
     * @return The customer address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set the current address of the customer to be newAddress.
     * @param address The address to override the current address.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Retrieves the customers cart.
     * @return Their shopping cart.
     */
    public Cart getCart() {
        return cart;
    }


    @Override
    public String toString() {
        return "Name: " + name + " | Phone Number: "
                + phoneNumber + " | Address: " + address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer customer)) {
            return false;
        }
        return phoneNumber == customer.phoneNumber && Objects.equals(name,
                customer.name) && Objects.equals(address, customer.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phoneNumber, address);
    }
}
