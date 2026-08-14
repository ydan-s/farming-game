package farm.sales;

import farm.inventory.product.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * A shopping cart that stores the customer products until they check out.
 */
public class Cart extends Object {
    private List<Product> cartItems = new ArrayList<Product>();

    /**
     * Constructor for Cart class.
     */
    public Cart() {
    }

    /**
     * Adds a given product to the shopping cart.
     * @param product the product to add.
     */
    public void addProduct(Product product) {
        cartItems.add(product);
    }

    /**
     * Retrieves all the products in the Cart in the order they were added.
     * @return a list of all products in the cart
     * @emsures the returned list is a shallow copy and cannot modify the original cart
     */
    public List<Product> getContents() {
        return cartItems;
    }

    /**
     * Empty out the shopping cart.
     */
    public void setEmpty() {
        cartItems.clear();
    }

    /**
     * Returns if the cart is empty
     * @return true iff there is nothing in the cart, else false.
     */
    public boolean isEmpty() {
        return cartItems.isEmpty();
    }
}
