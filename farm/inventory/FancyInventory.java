package farm.inventory;

import farm.core.FailedTransactionException;
import farm.core.InvalidStockRequestException;
import farm.inventory.product.*;
import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;

import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

/**
 * A fancy inventory which stores products in stacks, enabling quantity
 * information. Introduces the concept of performing operations on multiple
 * Products, such as removing 4 Eggs.
 */
public class FancyInventory extends Object implements Inventory {
    private List<Product> fancyInventory = new ArrayList<Product>();

    /**
     * Constructor for Fancy Inventory class
     */
    public FancyInventory() {
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
        fancyInventory.add(product);
    }

    @Override
    public void addProduct(Barcode barcode, Quality quality, int quantity)
            throws InvalidStockRequestException {

        if (quantity < 1) {
            throw new InvalidStockRequestException("Quantity must be at least 1");
        }
        Product product = null;

        switch (barcode) {
            case EGG -> product = new Egg(quality);
            case JAM -> product = new Jam(quality);
            case MILK -> product = new Milk(quality);
            case WOOL -> product = new Wool(quality);
        }
        for (int i = 0; i < quantity; i++) {
            fancyInventory.add(product);
        }
    }

    @Override
    public boolean existsProduct(Barcode barcode) {
        for (Product item : fancyInventory) {
            if (item.getBarcode().equals(barcode)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Product> removeProduct(Barcode barcode) {
        List<Product> removed = new ArrayList<Product>();
        for (Product item : getAllProducts()) {
            if (item.getBarcode().equals(barcode)) {
                removed.add(item);
                fancyInventory.remove(item);
                break;
            }
        }
        return removed;
    }

    @Override
    public List<Product> removeProduct(Barcode barcode, int quantity)
            throws FailedTransactionException {
        List<Product> removed = new ArrayList<Product>();
        List<Product> barcodeInStock = new ArrayList<Product>();
        for (Product item : getAllProducts()) {
            if (item.getBarcode().equals(barcode)) {
                barcodeInStock.add(item);
            }
        }


        if (barcodeInStock.size() >= quantity) {
            for (int i = 0; i < quantity; i++) {
                removed.add(barcodeInStock.get(i));
                fancyInventory.remove(barcodeInStock.get(i));
            }
        } else {
            removed.addAll(barcodeInStock);
            fancyInventory.removeAll(removed);
        }
        return removed;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> sortedInventory = new ArrayList<Product>();

        for (Barcode barcode : Barcode.values()) {
            List<Product> sortByQuality = new ArrayList<Product>();
            for (Quality quality : Quality.values()) {
                for (Product item : fancyInventory) {
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

    /**
     * Get the quantity of a specific product in the inventory.
     * @param barcode The barcode of the product.
     * @return The amount of the corresponding product currently in the inventory.
     */
    public int getStockedQuantity(Barcode barcode) {
        int quantity = 0;

        for (Product item : fancyInventory) {
            if (item.getBarcode().equals(barcode)) {
                quantity++;
            }
        }
        return quantity;
    }

}
