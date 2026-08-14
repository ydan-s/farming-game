package farm.sales.transaction;

import farm.customer.Customer;
import farm.inventory.product.Product;
import farm.inventory.product.data.Barcode;
import farm.sales.ReceiptPrinter;

import java.util.*;

/**
 * A transaction type that builds on the functionality of a categorised
 * transaction, allowing store-wide discounts to be applied to all products
 * of a nominated type.
 */
public class SpecialSaleTransaction extends CategorisedTransaction {
    private Map<Barcode, Integer> discountedItems = new HashMap<Barcode,
            Integer>();

    /**
     * Construct a new special sale transaction for an associated customer,
     * with an empty set of discounts (i.e. no products are to be sold at
     * a discount)
     * @param customer  the customer who the starting the transaction
     *                  (beginning to shop).
     */
    public SpecialSaleTransaction(Customer customer) {
        super(customer);
        discountedItems.clear();
    }

    /**
     * Construct a new special sale transaction for an associated customer,
     * with a set of discounts to be applied to nominated product types
     * on purchasing.
     * @param customer the customer who is starting the transaction
     *                 (beginning to shop).
     * @param discounts  a mapping from product barcodes to the associated
     *                   discount applied on purchasing, where discount amounts
     *                   are specified as an integer percentage (e.g. for a 10%
     *                   discount, the value stored is 10).
     */
    public SpecialSaleTransaction(Customer customer,
                                  Map<Barcode, Integer> discounts) {
        super(customer);
        discountedItems = discounts;


    }

    @Override
    public int getPurchaseSubtotal(Barcode type) {
        if (discountedItems.containsKey(type)) {
            int discountPercent = discountedItems.get(type);
            int oldPrice = super.getPurchaseSubtotal(type);
            return (oldPrice - ((oldPrice * discountPercent) / 100));
        }
        return super.getPurchaseSubtotal(type);
    }

    /**
     * Retrieves the discount percentage that will be applied for a particular
     * product type, as an integer (e.g. for a 10% discount, this method should
     * return 10).
     * If there is no discount percentage for that Product, returns 0.
     * @param type  the product type.
     * @return the amount the product is discounted by, as an integer percentage.
     */
    public int getDiscountAmount(Barcode type) {
        if (discountedItems.containsKey(type)) {
            return discountedItems.get(type);
        }
        return 0;
    }

    @Override
    public int getTotal() {
        int total = 0;
        for (Barcode item : getPurchasedTypes()) {
            total += getPurchaseSubtotal(item);
        }
        return total;
    }

    /**
     * Calculates how much the customer has saved from discounts.
     * @return the numerical savings from discounts.
     */
    public int getTotalSaved() {
        int discountedTotal = getTotal();
        int baseTotal = super.getTotal();

        return baseTotal - discountedTotal;
    }

    @Override
    public String toString() {
        return "Transaction {Customer: "
                + getAssociatedCustomer().getName() + " | "
                + "Phone Number: " + getAssociatedCustomer().getPhoneNumber()
                + " | Address: " + getAssociatedCustomer().getAddress()
                + ", Status: " + getStatus()
                + ", Associated Products: " + getPurchases()
                + ", Discounts: " + discountedItems + "}";

    }

    @Override
    public String getReceipt() {
        if (!isFinalised()) {
            return ReceiptPrinter.createActiveReceipt();
        }
        if (filterDiscountedItems(discountedItems).isEmpty() || getTotalSaved() == 0) {
            return super.getReceipt();
        }

        List<String> header = List.of("Item", "Qty", "Price (ea.)",
                "Subtotal");

        return ReceiptPrinter.createReceipt(header, specialEntities(),
                priceFormat(getTotal()), getAssociatedCustomer().getName(),
                priceFormat(getTotalSaved()));



    }

    /**
     * Creates a list of lists of strings containing the variables required
     * the 'entries' component for ReceiptPrinter.createReceipt(), which is the
     * @return the required 'entries' component for ReceiptPrinter.createReceipt().
     */
    private List<List<String>> specialEntities() {

        List<List<String>> entitiesList = new ArrayList<List<String>>();
        ;
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
            if (filterDiscountedItems(discountedItems).containsKey(item)) {
                String discount = "Discount applied! " + String.valueOf(
                        getDiscountAmount(item)) + "% off " + item.getDisplayName();
                items.add(discount);
            }
            entitiesList.add(items);

        }
        return entitiesList;

    }

    /**
     * Filters through the discounted list to remove any discounts with value of zero.
     * @param discountList the list to be checked from values containing zero
     * @return a new mapping for discounts that do not contain value with zero.
     */
    private Map<Barcode, Integer> filterDiscountedItems(Map<Barcode, Integer> discountList) {
        Map<Barcode, Integer> filteredDiscounts = new HashMap<Barcode, Integer>();
        for (Map.Entry<Barcode, Integer> set : discountList.entrySet()) {
            if (!(set.getValue() == 0)) {
                filteredDiscounts.put(set.getKey(), set.getValue());
            }
        }
        return filteredDiscounts;
    }
}
