package farm.sales.transaction;

import farm.customer.Customer;
import farm.inventory.product.Product;
import farm.inventory.product.data.Barcode;
import farm.sales.ReceiptPrinter;

import java.util.*;

/**
 * A transaction type that allows products to be categorised by their
 * types, not solely as isolated individual products. The resulting
 * receipt therefore displays purchased types with an associated
 * quantity purchased and subtotal, rather than a single line for
 * each product.
 */
public class CategorisedTransaction extends Transaction {

    /**
     * Construct a new categorised transaction for an associated customer.
     * @param customer  the customer who is starting the transaction
     *                 (beginning to shop).
     */
    public CategorisedTransaction(Customer customer) {
        super(customer);
    }

    /**
     * Retrieves all unique product types of the purchases associated
     * with the transaction.
     * @return a set of all product types in the transaction.
     */
    public Set<Barcode> getPurchasedTypes() {
        Set<Barcode> purchasedTypes = new HashSet<Barcode>();

        for (Product item : getPurchases()) {
            purchasedTypes.add(item.getBarcode());
        }
        return new TreeSet<Barcode>(purchasedTypes);
    }

    /**
     * Retrieves all products associated with the transaction,
     * grouped by their type.
     * @return the products in the transaction, grouped by their type.
     */
    public Map<Barcode, List<Product>> getPurchasesByType() {
        Map<Barcode, List<Product>> byType =
                new HashMap<Barcode, List<Product>>();

        for (Barcode type : getPurchasedTypes()) {
            List<Product> mapProducts = new ArrayList<Product>();
            for (Product item : getPurchases()) {
                if (item.getBarcode().equals(type)) {
                    mapProducts.add(item);
                }
            }
            byType.put(type, mapProducts);

        }
        return byType;
    }

    /**
     * Retrieves the number of products of a particular type
     * associated with the transaction.
     * @param type the product type.
     * @return the number of products of the specified type
     * associated with the transaction.
     */
    public int getPurchaseQuantity(Barcode type) {
        if (getPurchasedTypes().contains(type)) {
            List<Product> values = getPurchasesByType().get(type);
            return values.size();
        }
        return 0;


    }

    /**
     * Determines the total price for the provided product type
     * within this transaction.
     * @param type the product type.
     * @return the total price for all instances of that product
     * type within the transaction, or 0 if no items of that type are
     * associated with the transaction.
     */
    public int getPurchaseSubtotal(Barcode type) {
        int basePrice = type.getBasePrice();
        int quantity = getPurchaseQuantity(type);

        if (quantity == 0) {
            return 0;
        }

        return basePrice * quantity;
    }

    @Override
    public String getReceipt() {
        List<String> header = List.of("Item", "Qty", "Price (ea.)", "Subtotal");
        String total = priceFormat(getTotal());

        if (isFinalised()) {
            return ReceiptPrinter.createReceipt(header, categorisedEntities(),
                    total, getAssociatedCustomer().getName());
        }
        return ReceiptPrinter.createActiveReceipt();
    }

    /**
     * Creates a list of lists of strings which contain the name, qty, price,
     * and subtotal of an item in a customers purchased cart to use as input
     * for the ReceiptPrinter.createReceipt() method.
     * @return A list of lists of strings for the ReceiptPrinter.createReceipt()
     * method.
     */
    private List<List<String>> categorisedEntities() {

        List<List<String>> entitiesList = new ArrayList<List<String>>();

        for (Barcode item : getPurchasedTypes()) {
            String name = item.getDisplayName();
            String qty = String.valueOf(getPurchaseQuantity(item));
            String price = priceFormat(item.getBasePrice());
            String subtotal = priceFormat(getPurchaseSubtotal(item));

            List<String> items = new ArrayList<String>();
            items.add(name);
            items.add(qty);
            items.add(price);
            items.add(subtotal);

            entitiesList.add(items);

        }
        return entitiesList;

    }
}
