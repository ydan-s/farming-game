package farm.inventory;

import farm.core.FailedTransactionException;
import farm.core.InvalidStockRequestException;
import farm.inventory.product.*;
import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A very basic inventory that both stores and handles products individually.
 * Only supports operation on single Products at a time.
 */
public class BasicInventory extends Object implements Inventory {
    private List<Product> inventory = new ArrayList<Product>();

    /**
     * Constructor for Basic Inventory class
     */
    public BasicInventory() {
    }

    @Override
    public void addProduct(Barcode barcode, Quality quality) {
        Product product = null;

        switch (barcode) {
            case EGG -> product = new Egg(quality);
            case JAM -> product = new Jam(quality);
            case MILK -> product = new Milk(quality);
            case WOOL -> product = new Wool(quality);
        }
        inventory.add(product);
    }

    @Override
    public void addProduct(Barcode barcode, Quality quality, int quantity)
            throws InvalidStockRequestException {
        throw new InvalidStockRequestException(
                "Current inventory is not fancy enough. Please supply products one at a time.");
    }

    @Override
    public boolean existsProduct(Barcode barcode) {
        for (Product item : inventory) {
            if (item.getBarcode().equals(barcode)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Product> removeProduct(Barcode barcode) {
        List<Product> removed = new ArrayList<Product>();
        for (Product item : inventory) {
            if (item.getBarcode().equals(barcode)) {
                removed.add(item);
                inventory.remove(item);
                break;
            }
        }
        return removed;
    }

    @Override
    public List<Product> removeProduct(Barcode barcode, int quantity)
            throws FailedTransactionException {
        throw new FailedTransactionException(
                "Current inventory is not fancy enough. Please purchase products one at a time.");
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> sortedInventory = new ArrayList<Product>();

        for (Barcode barcode : Barcode.values()) {
            List<Product> sortByQuality = new ArrayList<Product>();
            for (Quality quality : Quality.values()) {
                for (Product item : inventory) {
                    if (item.getBarcode().equals(barcode)
                        && item.getQuality().equals(quality)) {
                        sortByQuality.addFirst(item);
                    }
                }
            }
            sortedInventory.addAll(sortByQuality);
        }
        return sortedInventory;
    }
}
