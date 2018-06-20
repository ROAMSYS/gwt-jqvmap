package com.roamsys.gwtjqvmap;

/**
 * Resolves the ISO country code from an object of type T
 * @param <T> the type of object that the resolver resolves the ISO code from
 * @author mbartel
 */
public abstract class VMapCountryResolver<T> implements VMapCountryColorResolver<T> {

    private final String colorCode;

    /**
     * Creates a new country resolver were each country from the list of values has the same color
     * @param colorCode the color code for all countries in the list of values assigned to the map
     */
    public VMapCountryResolver(final String colorCode) {
        this.colorCode = colorCode;
    }

    @Override
    public String getRGBColorCode(final T item) {
        return colorCode;
    }
}
