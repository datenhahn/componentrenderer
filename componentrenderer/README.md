# ComponentRenderer

Renders standard Vaadin components in the grid.

## Benefits
 * Plain Java server side coding, as usual
 * Standard Tooltips work
 * Components can be combined in layouts (e.g. multiple images in a horizontal layout)
 * Works with standard colums and generated columns
 * Standard ValueChange/ClickListeners
 
## Status Experimental

 * All components for all rows are held in memory, which is not as bad as it sounds and works well for smaller grids,
   but may not work for bigger grids
 * The renderer is not tested well in production scenarios, please open issues if you encounter difficulties
 * Updates from the serverside seem not to update the UI (the workaround is to return different component instances
   e.g. in a generated column (see the details row arrows in the demo-ui)
 * When removing rows from the container by a button click, a rerender of the grid was necessary, otherwise
   there were rendering problems (some buttons vanishing) (see the forceReRender function)
 * A cglib decorator or a grid subclass is necessary to override two functions, that may have side-effects
   I didn't take into account
   
## Build

    mvn clean install
  
Now you have the dependency in your local maven repo or can upload it to some repository. I will try to get
the addon to the vaadin directory/mavencentral in the near future.

To add the dependency in your code use

    <dependency>
        <groupId>de.datenhahn.vaadin</groupId>
        <artifactId>componentrenderer</artifactId>
        <version>0.1.1</version>
    </dependency>

then recompile the widgetset.

## Use

Have a look at the demo app, you can start it with:
    
    cd componentrenderer-demo
    mvn jetty:run
    
### Activate the ComponentRenderer Extension
    
Decorate your own Grid subclass

    ComponentRendererGridDecorator<Grid> foo = new ComponentRendererGridDecorator<Grid>();
    final Grid myGrid = (foo).decorate(Grid.class);
    
Or use the ComponentGrid subclass delivered in the package

    final ComponentGrid myGrid = new ComponentGrid();
    
Alternativley you can add the methods to your own subclass

### Add a component column

    myGrid.addColumn(COL_FONT_LABEL_1, Component.class).setRenderer(((ComponentRendererProvider) myGrid).createComponentRenderer());

### Or generate one

    GeneratedPropertyContainer generatedPropertyContainer = new GeneratedPropertyContainer(myGrid.getContainerDataSource());
    myGrid.setContainerDataSource(generatedPropertyContainer);
    generatedPropertyContainer.addGeneratedProperty(COL_DELETE, new DeleteButtonValueGenerator(myGrid));
   

		