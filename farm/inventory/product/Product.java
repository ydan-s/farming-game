package farm.inventory.product;

import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;

import java.util.Objects;

/**
 * An abstract class representing an instance of a product.
 * Each product is a single instance of a specific item.
 */
public abstract class Product extends Object {
    private Barcode barcode;
    private String displayName;
    private Quality quality = Quality.REGULAR;
    private int basePrice;

    protected Product(Barcode item) {
        barcode = item;
        displayName = item.getDisplayName();
        basePrice = item.getBasePrice();
    }


    protected void setProductQuality(Quality newQuality) {
        quality = newQuality;
    }

    /**
     * Accessor method for the product's identifier.
     * @return the identifying Barcode for this product.
     */
    public Barcode getBarcode() {
        return barcode;
    }

    /**
     * Retrieve the product's display name, for visual/textual representation.
     * @return the product's display name.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Retrieve the product's quality.
     * @return the quality level for this product.
     */
    public Quality getQuality() {
        return quality;
    }

    /**
     * Retrieve the products base sale price.
     * @return the price of the product. In cents.
     */
    public int getBasePrice() {
        return basePrice;
    }

    @Override
    public String toString() {
        String stringQuality = "*REGULAR*";

        switch (quality) {
            case REGULAR -> stringQuality = "*REGULAR*";
            case GOLD -> stringQuality = "*GOLD*";
            case SILVER -> stringQuality = "*SILVER*";
            case IRIDIUM -> stringQuality = "*IRIDIUM*";
        }
        return displayName + ": "
                + basePrice + "c "
                + stringQuality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product product)) {
            return false;
        }
        return barcode == product.barcode && quality == product.quality;
    }

    @Override
    public int hashCode() {
        return Objects.hash(barcode, quality);
    }
}
