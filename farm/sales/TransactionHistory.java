package farm.sales;

import farm.inventory.product.Product;
import farm.inventory.product.data.Barcode;
import farm.sales.transaction.SpecialSaleTransaction;
import farm.sales.transaction.Transaction;

import java.util.*;

/**
 * A record of all past transactions.
 * Handles retrieval of statistics about past transactions,
 * such as earnings and popular products.
 */
public class TransactionHistory extends Object {
    private List<Transaction> transactionHistory = new ArrayList<Transaction>();

    /**
     * Constructor for Transaction History class.
     */
    public TransactionHistory() {
    }

    /**
     * Adds the given transaction to the record of all past transactions.
     * @param transaction the transaction to add to the record.
     */
    public void recordTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }

    /**
     * Retrieves the most recent transaction.
     * @return the most recent transaction added to the record.
     */
    public Transaction getLastTransaction() {
        return transactionHistory.getLast();
    }

    /**
     * Calculates the gross earnings, i.e. total income, from all transactions.
     * @return the gross earnings from all transactions in history, in cents.
     */
    public int getGrossEarnings() {
        int grossEarnings = 0;
        for (Transaction transaction : transactionHistory) {
            grossEarnings += transaction.getTotal();
        }
        return grossEarnings;
    }

    /**
     * Calculates the gross earnings, i.e. total income, from all
     * sales of a particular product type. Total income is as defined
     * in getGrossEarnings().
     * @param type the Barcode of the item of interest.
     * @return the gross earnings from all sales of the product type, in cents.
     */
    public int getGrossEarnings(Barcode type) {
        int totalPerBarcode = 0;
        for (Transaction transaction : transactionHistory) {
            for (Product item : transaction.getPurchases()) {
                if (item.getBarcode().equals(type)) {
                    totalPerBarcode += item.getBasePrice();
                }
            }
        }
        return totalPerBarcode;
    }


    /**
     * Calculates the number of transactions made.
     * @return the number of transactions in total.
     */
    public int getTotalTransactionsMade() {
        return transactionHistory.size();
    }

    /**
     * Calculates the number of products sold over all transactions.
     * @return the total number of products sold.
     */
    public int getTotalProductsSold() {
        int totalProducts = 0;
        for (Transaction transaction : transactionHistory) {
            totalProducts += transaction.getPurchases().size();
        }
        return totalProducts;
    }

    /**
     * Calculates the number of sold of a particular product type,
     * over all transactions.
     * @param type the Barcode for the product of interest
     * @return the total number of products sold, for that particular product.
     */
    public int getTotalProductsSold(Barcode type) {
        int totalProducts = 0;
        for (Transaction transaction : transactionHistory) {
            for (Product item : transaction.getPurchases()) {
                if (item.getBarcode().equals(type)) {
                    totalProducts++;
                }
            }
        }
        return totalProducts;
    }

    /**
     * Retrieves the transaction with the highest gross earnings,
     * i.e. reported total. If there are multiple return the one that
     * first was recorded.
     * @return the transaction with the highest gross earnings.
     */
    public Transaction getHighestGrossingTransaction() {
        Map<Transaction, Integer> transactionIntegerMap = new HashMap<Transaction, Integer>();
        for (Transaction transaction : transactionHistory) {
            transactionIntegerMap.put(transaction, transaction.getTotal());
        }

        int highestGrossingTotal = Collections.max(transactionIntegerMap.values());
        for (Map.Entry<Transaction, Integer> entry : transactionIntegerMap.entrySet()) {
            if (entry.getValue() == highestGrossingTotal) {
                return entry.getKey();
            }
        }
        return transactionHistory.getLast();
    }


    /**
     * Calculates which type of product has had the highest quantity
     * sold overall. If two products have sold the same quantity resulting
     * in a tie, return the one appearing first in the Barcode enum
     * @return the identifier for the product type of most popular product.
     */
    public Barcode getMostPopularProduct() {
        Map<Barcode, Integer> quantityPerProduct = new HashMap<Barcode, Integer>();

        for (Barcode product : Barcode.values()) {
            int total = 0;
            for (Transaction transaction : transactionHistory) {
                for (Product item : transaction.getPurchases()) {
                    if (item.getBarcode().equals(product)) {
                        total++;
                    }
                }
            }
            quantityPerProduct.put(product, total);
        }

        int mostPopular = Collections.max(quantityPerProduct.values());
        for (Map.Entry<Barcode, Integer> entry : quantityPerProduct.entrySet()) {
            if (entry.getValue() == mostPopular) {
                return entry.getKey();
            }
        }
        return Barcode.EGG;
    }

    /**
     * Calculates the average amount spent by customers across all transactions.
     * @return the average amount spent overall, in cents (with decimals).
     */
    public double getAverageSpendPerVisit() {
        double transactionsCount = getTotalTransactionsMade();
        double total = getGrossEarnings();
        return total / transactionsCount;
    }

    /**
     * Calculates the average amount a product has been discounted by,
     * across all sales of that product.
     * @param type identifier of the product of interest.
     * @return the average discount for the product, in cents (with decimals).
     */
    public double getAverageProductDiscount(Barcode type) {
        double sumOfDiscounted = 0;
        for (Transaction transaction : transactionHistory) {
            SpecialSaleTransaction discountTransaction =
                    (SpecialSaleTransaction) transaction;
            for (Product item : discountTransaction.getPurchases()) {
                if (item.getBarcode().equals(type)) {
                    sumOfDiscounted += discountTransaction.getDiscountAmount(type);
                }
            }

        }
        return sumOfDiscounted / getTotalProductsSold(type);
    }




}
