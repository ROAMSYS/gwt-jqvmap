# gwt-jqvmap
GWT Wrapper for jQuery Vector Map Library (jqvmap) http://jqvmap.com

## Setup
### Download vmap-JavaScript files
You need to download one of the `jquery.vmap.*.js` files from the [__GitHub__ ](https://github.com/manifestinteractive/jqvmap) repository. And at least one `jquery.vmap.{country}.js` file from the _maps/_ folder. Put those files somewhere on your webserver, so that your GWT application can fetch them. And make sure that jQuery is available.

### Maven
Add a new dependency to you pom.xml
````xml
<dependency>
    <groupId>com.roamsys.opensource</groupId>
    <artifactId>gwt-jqvmap</artifactId>
    <version>0.1.0</version>
</dependency>
````
### Inherit the GWT module
No matter if you use this library with Maven or as JAR file, you need to inherit the GWT module in your `projectName.gwt.xml`. Add the folling line to your module file.
````xml
<inherits name='com.roamsys.GWTJQVMap'/>
````
### Set path to vmap-JavaScript files
Tell the _gwt-jqvmap_ library where to find the _vmap_-Javascript files on you webserver.
````java
VMap.setJQVMapURL(staticsURL + "js/jquery.vmap.packed.js");
VMap.setMapURL(staticsURL + "js/jquery.vmap.world.js");
````
Keep in mind that fetching the files and initiating all the _vmap_ stuff will need some time. So make sure jQuery is initiated before you initate the first instance of the `VMap` class.
## Example
### The type of data for the map
````java
public class StatisticData {
    private String isoCountryCode;
    private int population;

    public StatisticData(final String isoCountryCode, final int population) {
        this.isoCountryCode = isoCountryCode;
        this.population = population;
    }

    public String getISOCountryCode() {
        return isoCountryCode;
    }

    public int getPopulation() {
        return population;
    }
}
````
### Assign some data to the map
````java
// VMap is defined in an uiBinder template
@UiField VMap<StatisticData> map;

public SettingsWidget() {
    initWidget(uiBinder.createAndBindUi(this));

    final List<StatisticData> listOfStaticData = new ArrayList<StatisticData>();
    listOfStaticData.add(new StatisticData("ru", 143600000));
    listOfStaticData.add(new StatisticData("de",  80767000));
    listOfStaticData.add(new StatisticData("lu",    550000));

    map.setValues(listOfStaticData, new VMapCountryColorResolver<SettingsWidget.StatisticData>() {

        @Override
        public String getISOCountryCode(final StatisticData item) {
            return item.getISOCountryCode();
        }

        @Override
        public String getRGBColorCode(final StatisticData item) {
            final int population = item.getPopulation();
            if (population < 100000) {
                return "#00FF00"; // green
            } else if (population < 1000000) {
                return "#FF7700"; // orange
            } else {
                return "#FF0000"; // red
            }
        }
    });

    map.addRegionClickHandler(new VMapEventHandler<SettingsWidget.StatisticData>() {

        @Override
        public void onEvent(final String isoCode, final String regionName, final Map<String, StatisticData> isoCodeToDataMapping) {
            final StatisticData item = isoCodeToDataMapping.get(isoCode);
            if (item == null) {
                Window.alert("There is no information provided for " + regionName + " (" + isoCode + ")");
            } else {
                Window.alert(regionName + " (" + isoCode + ") has a population of " + item.getPopulation());
            }
        }
    });
}
````
