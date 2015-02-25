package com.roamsys.gwtjqvmap;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * World map colors
 * @author mbartel (ROAMSYS S.A.)
 */
public final class VMapCountryColors extends JavaScriptObject {

    protected VMapCountryColors() {};

    /**
     * Returns the RGB color code for the ISO country code
     * @param isoCode the ISO country code (2 letters)
     * @return the RGB color code
     */
    public native String getColorCode(final String isoCode) /*-{
        return this[isoCode];
    }-*/;

    /**
     * Sets the RGB color code for the country with the given ISO country code
     * @param isoCode the ISO country code
     * @param colorCode the RGB color code
     */
    public native void set(final String isoCode, final String colorCode) /*-{
        this[isoCode] = colorCode;
    }-*/;

    /**
     * Creates a new instance
     * @return a new empty country colors instance
     */
    public static native VMapCountryColors create() /*-{
        return {};
    }-*/;
}