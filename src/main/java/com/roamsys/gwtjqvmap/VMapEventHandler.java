package com.roamsys.gwtjqvmap;

import java.util.Map;

/**
 * Event handler for onRegionOver, onRegionOut and onRegionClick
 * @author mbartel (ROAMSYS S.A.)
 * @param <T> the type of data assigned to the map
 */
public interface VMapEventHandler<T> {

    /**
     * Callback function which will be called when the mouse cursor enters, leaves or clicks on the region path
     * @param isoCode the ISO country code of the region
     * @param regionName the region name of the region the
     * @param isoCodeToDataMapping the data assigned to the region
    */
    public void onEvent(final String isoCode, final String regionName, final Map<String, T> isoCodeToDataMapping);
}
