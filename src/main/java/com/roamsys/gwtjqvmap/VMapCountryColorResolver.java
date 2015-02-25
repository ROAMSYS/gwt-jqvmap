package com.roamsys.gwtjqvmap;

/**
 * Resolves the ISO country code from an object of type T
 * @param <T> the type of object that the resolver resolves the ISO code from
 * @author mbartel
 */
public interface VMapCountryColorResolver<T> {

    /**
     * Returns the ISO country code from the item
     * @param item the item for the country code
     * @return the resolved ISO country code (2 letters)
     */
    public String getISOCountryCode(final T item);

    /**
     * Returns the RGB color code from the item
     * @param item the item for the color code
     * @return the resolved RGB color code
     */
    public String getRGBColorCode(final T item);
}