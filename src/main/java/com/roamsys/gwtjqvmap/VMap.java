package com.roamsys.gwtjqvmap;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.SimplePanel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * jQuery Vector Map Wrapper
 * You need to add some script tags to your projects HTML pageas described athttps://github.com/manifestinteractive/jqvmap
 * @param <T> the type of the data, that will be assigned to the map
 * @author mbartel (ROAMSYS S.A.)
 * @see https://github.com/manifestinteractive/jqvmap and http://jqvmap.com
 */
public class VMap<T> extends SimplePanel {

    final static String ON_REGION_OVER_PREFIX = "over";
    final static String ON_REGION_OUT_PREFIX = "out";
    final static String ON_REGION_CLICK_PREFIX = "click";

    final static String DEFAULT_WIDTH = "640px";
    final static String DEFAULT_HEIGHT = "400px";

    /**
     * The jQuery wrapped object for the map
     */
    private JavaScriptObject map;

    /**
     * The unique ID, that will be provided by each callback execution
     */
    private final String uuid;

    /**
     * The map with all event handler lists
     */
    private static final Map<String, List<VMapEventHandler>> eventHandlerMap = new HashMap<String, List<VMapEventHandler>>(1);

    /**
     * Mapping from ISO country code to data items
     */
    private static final Map<String, HashMap> uuidToCountryDataMapping = new HashMap<String, HashMap>(1);

    /**
     * The URL to the jQuery Vector Map JavaScript file
     */
    private static String jqvmapURL;

    /**
     * The URL to the map file
     */
    private static String defaultMapURL;

    /**
     * The RGB color code for an unselected country
     */
    private String color = "#f4f3f0";

    /**
     * Create a new world map with default properties and default dimensions 640x480 pixels
     */
    public VMap() {
        this(VMapProperties.create());
    }

    /**
     * Create a new world map (with default dimensions 640x480 pixels)
     * @param properties the initialization properties
     */
    public VMap(final VMapProperties properties) {
        this(properties, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Create a new world map
     * @param properties the initialization properties
     * @param width the width of the map
     * @param height the height of the map
     */
    public VMap(final VMapProperties properties, final String width, final String height) {
        this(properties, defaultMapURL, width, height);
    }

    /**
     * Create a new world map
     * @param properties the initialization properties
     * @param mapURL the URL to the map file
     * @param width the width of the map
     * @param height the height of the map
     */
    public VMap(final VMapProperties properties, final String mapURL, final String width, final String height) {
        uuid = properties.getUUID();
        uuidToCountryDataMapping.put(uuid, new HashMap<String, T>());
        color = properties.getColor();

        setWidth(width);
        setHeight(height);

        init(getElement(), properties, jqvmapURL, mapURL);
    }

    /**
     * Sets the URL to the jQuery Vector Map JavaScript file
     * @param jqvmapURL the URL to the JavaScript file
     */
    public static void setJQVMapURL(final String jqvmapURL) {
        VMap.jqvmapURL = jqvmapURL;
    }

    /**
     * The URL to the map file
     * @param defaultMapURL the URL to the map file
     */
    public static void setMapURL(final String defaultMapURL) {
        VMap.defaultMapURL = defaultMapURL;
    }

    /**
     * Add an click handler to the map
     * @param regionClickHandler the new click handler
     */
    public void addRegionClickHandler(final VMapEventHandler<T> regionClickHandler) {
        addRegionEventHandler(uuid + ON_REGION_CLICK_PREFIX, regionClickHandler);
    }

    /**
     * Add an mouse over handler to the map
     * @param regionMouseOverHandler the new mouse over handler
     */
    public void addRegionMouseOverHandler(final VMapEventHandler<T> regionMouseOverHandler) {
        addRegionEventHandler(uuid + ON_REGION_OVER_PREFIX, regionMouseOverHandler);
    }

    /**
     * Add an mouse out handler to the map
     * @param regionMouseOutHandler the new mouse out handler
     */
    public void addRegionMouseOutHandler(final VMapEventHandler<T> regionMouseOutHandler) {
        addRegionEventHandler(uuid + ON_REGION_OUT_PREFIX, regionMouseOutHandler);
    }

    private void addRegionEventHandler(final String key, final VMapEventHandler<T> handler) {
        List<VMapEventHandler> events = eventHandlerMap.get(key);
        if (events == null) {
            events = new ArrayList<VMapEventHandler>(1);
        }
        events.add(handler);
        eventHandlerMap.put(key, events);
    }

    /**
     * JSNI callback function for region events (DO NOT RENAME WITHOUT AJUSTING JSNI CODE IN {@link VMapProperties#create(java.lang.String, java.lang.String, java.lang.String) )
     * @param uuid the UUID for the VMap instance
     * @param event the name of the event (one of {@link VMap#ON_REGION_CLICK_PREFIX}, {@link VMap#ON_REGION_OUT_PREFIX} or {@link VMap#ON_REGION_OVER_PREFIX})
     * @param code the ISO country code
     * @param region the region name
     */
    private static void handleVMapRegionEvents(final String uuid, final String event, final String code, final String region) {
        final List<VMapEventHandler> handlerList = eventHandlerMap.get(uuid + event);
        final HashMap values = uuidToCountryDataMapping.get(uuid);
        if (handlerList != null) {
            for (final VMapEventHandler handler : handlerList) {
                handler.onEvent(code, region, values);
            }
        }
    }

    /**
     * Set the list of items to colorize the different countries
     * @param values the list of values
     * @param countryCodeResolver the resolver for the country- and color code
     */
    public void setValues(final Iterable<T> values, final VMapCountryColorResolver<T> countryCodeResolver) {
        // the colors object, that will be used to update the vector map via JSNI
        final VMapCountryColors colors = VMapCountryColors.create();

        // the internal value cache for event handlers
        final HashMap<String, T> valueCache = uuidToCountryDataMapping.get(uuid);

        // if countries are removed from the list of values, the vector map does not redraw the country on the map
        // we need to collect these countries and set their color to the RGB value of the unselected countries
        final List<String> cleanUpISOCodes = new LinkedList<String>(valueCache.keySet());

        for (final T value : values) {
            final String isoCountryCode = countryCodeResolver.getISOCountryCode(value);
            if (isoCountryCode != null) {
                final String lowerISOCountryCode = isoCountryCode.toLowerCase();
                colors.set(lowerISOCountryCode, countryCodeResolver.getRGBColorCode(value));
                valueCache.put(lowerISOCountryCode, value);
                cleanUpISOCodes.remove(lowerISOCountryCode);
            }
        }

        // put countries that needed a cleanup into the colors object
        for (final String isoCode : cleanUpISOCodes) {
            valueCache.remove(isoCode);
            colors.set(isoCode, color);
        }
        set("colors", colors);
    }

    /**
     * Background color of map container in any CSS compatible format.
     * @param backgroundColor the RGG color code
     */
    public void setBackgroundColor(final String backgroundColor) {
        set("backgroundColor", backgroundColor);
    }

    /**
     * Border Color to use to outline map objects
     * @param borderColor the RGB color code
     */
    public void setBorderColor(final String borderColor) {
        set("borderColor", borderColor);
    };

    /**
     * Border Opacity to use to outline map objects
     * @param borderOpacity use anything from 0-1, e.g. 0.5, defaults to 0.25
     */
    public void setBorderOpacity(final double borderOpacity) {
        set("borderOpacity", borderOpacity);
    }

    /**
     * Border Width to use to outline map objects
     * @param borderWidth the border with (default is 1)
     */
    public void setBorderWidth(final int borderWidth) {
        set("borderWidth", borderWidth);
    }

    /**
     * Color of map regions
     * @param color the RGB color code
     */
    public void setColor(final String color) {
        this.color = color;
        set("color", color);
    }

    /**
     * Color of the region when mouse pointer is over it
     * @param hoverColor the RGB color code
     */
    public void setHoverColor(final String hoverColor) {
        set("hoverColor", hoverColor);
    }

    /**
     * Opacity of the region when mouse pointer is over it
     * @param hoverOpacity use anything from 0-1, defaults to 0.5
     */
    public void setHoverOpacity(final double hoverOpacity) {
        set("hoverOpacity", hoverOpacity);
    }

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
        set("scaleColors", scaleColors);
    }

    /**
     * The color to use to highlight the selected region
     * @param selectedColor the RGB color code
     */
    public void setSelectedColor(final String selectedColor) {
        set("selectedColor", selectedColor);
    }

    /**
     * This is the Region that you are looking to have preselected
     * @param selectedRegion two letter ISO code, defaults to null
     */
    public void setSelectedRegion(final String selectedRegion) {
        set("selectedRegion", selectedRegion);
    }

    /**
     * Whether to show Tooltips on Mouseover
     * @param showTooltip true or false, defaults to true
     */
    public void showTooltip(final boolean showTooltip) {
        set("showTooltip", showTooltip);
    }

    private native void set(final String key, final Object value) /*-{
        if (this.@com.roamsys.gwtjqvmap.VMap::map && this.@com.roamsys.gwtjqvmap.VMap::map.data('mapObject')) {
            this.@com.roamsys.gwtjqvmap.VMap::map.vectorMap('set', key, value);
        } else {
            var that = this;
            $wnd.setTimeout(function() {
              that.@com.roamsys.gwtjqvmap.VMap::set(Ljava/lang/String;Ljava/lang/Object;)(key, value);
            }, 1000);
        }
    }-*/;

    /**
     * Initialize the jQuery Vector Map
     * @param element the DOM element of this widget
     * @param properties the properties object
     * @param jqvmapURL the URL to the jQuery Vector Map JavaScript file
     * @param mapURL the URL to the map file
     */
    private native void init(final Element element, final VMapProperties properties, final String jqvmapURL, final String mapURL) /*-{
        var that = this;
        var vmap = $doc.createElement("script");
        vmap.type = 'text/javascript';
        vmap.src = jqvmapURL;
        vmap.onload = vmap.onreadystatechange = function() {
            var vmapWorld = $doc.createElement("script");
            vmapWorld.type = 'text/javascript';
            vmapWorld.src = mapURL;
            vmapWorld.onload = vmapWorld.onreadystatechange = function() {
              that.@com.roamsys.gwtjqvmap.VMap::map = $wnd.$(element);
              that.@com.roamsys.gwtjqvmap.VMap::map.vectorMap(properties);
            };
            $doc.body.appendChild(vmapWorld);
        };
        $doc.body.appendChild(vmap);
    }-*/;
}