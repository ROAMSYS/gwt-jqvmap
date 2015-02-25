# gwt-jqvmap
GWT Wrapper for jQuery Vector Map Library (jqvmap) http://jqvmap.com

## Download vmap-JavaScript files
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