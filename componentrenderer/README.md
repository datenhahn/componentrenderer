# ComponentRenderer

Renders standard Vaadin components in the grid.

## Benefits
 * Plain Java server side coding, as usual
 * Standard Tooltips work
 * Components can be combined in layouts (e.g. multiple images in a horizontal layout)
 * Works with standard colums and generated columns
 * Standard ValueChange/ClickListeners

## Limitations

 * components should have fixed sizes, otherwise all browsers expect Chrome do lots of measurements
   and relayout because of these which makes everything quite slow
 
## Status: Pre-Alpha

 * Uses generated property container to have on-the-fly generation of components
 * The renderer is not tested well in production scenarios, please open issues if you encounter difficulties
 * Updates from the serverside seem not to update the UI (the workaround is to return different component instances
   e.g. in a generated column (see the details row arrows in the demo-ui)
 * When removing rows from the container by a button click, a rerender of the grid was necessary, otherwise the row striping (the alternating colors of the row) is not correct
   
## Build

    mvn clean install
  
Now you have the dependency in your local maven repo or can upload it to some repository. I will try to get
the addon to the vaadin directory/mavencentral in the near future.

To add the dependency in your code use

    <dependency>
        <groupId>de.datenhahn.vaadin</groupId>
        <artifactId>componentrenderer</artifactId>
        <version>ENTER VERSION HERE</version>
    </dependency>

then recompile the widgetset.

## Use

Have a look at the demo app, you can start it with:
    
    cd componentrenderer-demo
    mvn jetty:run
    
### The ComponentGrid
    
    * The use of the typed Component Grid subclass is strongly recommended, but not mandatory.
    * The use of value component columns is not recommended anymore


### Add a component column

The use of Java 8 is recommended but not mandatory.

    grid.addComponentColumn(FOOD, cust -> createFoodSelector(grid, cust));
    grid.addComponentColumn(FOOD_ICON, cust -> createFoodIcon(cust));

		