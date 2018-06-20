package com.roamsys.gwtjqvmap;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

/**
 * jQuery Vector Map properties
 * @author mbartel (ROAMSYS S.A.)
 */
public final class VMapProperties extends JavaScriptObject {

    protected VMapProperties() {};

    /**
     * Map you want to load. Must include the javascript file with the name of the map you want.
     * Available maps with this library are world_en, usa_en, europe_en and germany_en
     * @param map name of the map to use
     */
    public native void setMap(final String map) /*-{
        this.map = map;
    }-*/;

    /**
     * Background color of map container in any CSS compatible format.
     * @param backgroundColor the RGG color code
     */
    public native void setBackgroundColor(final String backgroundColor) /*-{
        this.backgroundColor = backgroundColor;
    }-*/;

    /**
     * Border Color to use to outline map objects
     * @param borderColor the RGB color code
     */
    public native void setBorderColor(final String borderColor) /*-{
        this.borderColor = borderColor;
    }-*/;

    /**
     * Border Opacity to use to outline map objects
     * @param borderOpacity use anything from 0-1, e.g. 0.5, defaults to 0.25
     */
    public native void setBorderOpacity(final double borderOpacity) /*-{
        this.borderOpacity = borderOpacity;
    }-*/;

    /**
     * Border Width to use to outline map objects
     * @param borderWidth the border with (default is 1)
     */
    public native void setBorderWidth(final int borderWidth) /*-{
        this.borderWidth = borderWidth;
    }-*/;

    /**
     * Color of map regions
     * @param color the RGB color code
     */
    public native void setColor(final String color) /*-{
        this.color = color;
    }-*/;

    /**
     * Returns the color of unselected map regions
     * @return the RGB color code
     */
    public native String getColor() /*-{
        return this.color;
    }-*/;

    /**
     * Colors of individual map regions. Keys of the colors objects are country codes according to ISO 3166-1 alpha-2 standard. Keys of colors must be in lower case.
     * @param colors map of ISO code to RGB color code
     */
    private native void setColors(final JavaScriptObject colors) /*-{
        this.colors = colors;
    }-*/;

    /**
     * Whether to Enable Map Zoom
     * @param enableZoom true or false, defaults to true
     */
    public native void setEnableZoom(final boolean enableZoom) /*-{
        this.enableZoom = enableZoom;
    }-*/;

    /**
     * Color of the region when mouse pointer is over it
     * @param hoverColor the RGB color code
     */
    public native void setHoverColor(final String hoverColor) /*-{
        this.hoverColor = hoverColor;
    }-*/;

    /**
     * Opacity of the region when mouse pointer is over it
     * @param hoverOpacity use anything from 0-1, defaults to 0.5
     */
    public native void setHoverOpacity(final double hoverOpacity) /*-{
        this.hoverOpacity = hoverOpacity;
    }-*/;

    /**
     * This option defines colors, with which regions will be painted when you set option values.
     * Array scaleColors can have more then two elements. Elements should be strings representing colors in RGB hex format.
     * @param scaleColors list of RGB colors
     */
    public void setScaleColors(final Iterable<String> scaleColors) {
        final JavaScriptObject array = JsArrayString.createArray();
        for (final String color : scaleColors) {
            ((JsArrayString) array).push(color);
        }
        setScaleColors(array);
    }

    private native void setScaleColors(final JavaScriptObject scaleColors) /*-{
        this.scaleColors = scaleColors;
    }-*/;

    /**
     * The color to use to highlight the selected region
     * @param selectedColor the RGB color code
     */
    public native void setSelectedColor(final String selectedColor) /*-{
        this.selectedColor = selectedColor;
    }-*/;

    /**
     * This is the Region that you are looking to have preselected
     * @param selectedRegion two letter ISO code, defaults to null
     */
    public native void setSelectedRegion(final String selectedRegion) /*-{
        this.selectedRegion = selectedRegion;
    }-*/;

    /**
     * Whether to show Tooltips on Mouseover
     * @param showTooltip true or false, defaults to true
     */
    public native void showTooltip(final boolean showTooltip) /*-{
        this.showTooltip = showTooltip;
    }-*/;

    /**
     * Creates a new instance with standard properties
     * @return a new world map properties instance
     */
    public static VMapProperties create() {
        return create(VMap.ON_REGION_OVER_PREFIX, VMap.ON_REGION_OUT_PREFIX, VMap.ON_REGION_CLICK_PREFIX);
    }

    /**
     * Creates a new instance with standard properties
     * @param over prefix for onRegionOver event handlers
     * @param out prefix for onRegionOut event handlers
     * @param click prefix for onRegionClick event handlers
     * @return a new world map properties instance
     */
    private static native VMapProperties create(final String over, final String out, final String click) /*-{
        var uuid = Math.floor((1 + Math.random()) * 0x100000000).toString(16);
        var handleVMapEvents = $entry(@com.roamsys.gwtjqvmap.VMap::handleVMapRegionEvents(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;));
        return {
            uuid: uuid,
            map: 'world_en',
            showTooltip: false,
            color: '#f4f3f0',
            enableZoom: true,
            onRegionOver: function(event, code, region) {
                handleVMapEvents(uuid, over, code, region);
            },
            onRegionOut: function(event, code, region) {
                handleVMapEvents(uuid, out, code, region);
            },
            onRegionClick: function(event, code, region) {
                handleVMapEvents(uuid, click, code, region);
            }
        };
    }-*/;

    /**
     * Returns the unique ID, that will be provided by each callback execution
     * @return the unique ID
     */
    public native String getUUID() /*-{
        return this.uuid;
    }-*/;
}