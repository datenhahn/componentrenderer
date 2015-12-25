/**
 * Licensed under the Apache License,Version2.0(the"License");you may not
 * use this file except in compliance with the License.You may obtain a copy of
 * the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing,software
 * distributed under the License is distributed on an"AS IS"BASIS,WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package de.datenhahn.vaadin.componentrenderer;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.util.*;

import static com.google.gwt.thirdparty.guava.common.collect.Sets.newHashSet;

/**
 * Extension for Vaadin Grid to add the necessary component tracking
 * functionality for supporting the {@link ComponentRenderer}.
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class ComponentRendererComponentStore {

    private final LinkedHashSet<Component> rendererComponents = new LinkedHashSet<Component>();
    private final HashSet<Object> componentColumnPropertyIds = new HashSet<Object>();
    private final HashMap<Object, Set<Component>> itemIdComponentMapping = new HashMap<Object, Set<Component>>();
    private final Grid grid;

    private final ComponentRendererContainerItemSetChangeListener componentRendererContainerItemSetChangeListener;
    private final ComponentColumnContainerPropertySetChangeListener componentColumnContainerPropertySetChangeListener;
    private final ItemIdComponentMappingValueChangeListener itemIdComponentMappingValueChangeListener;

    /**
     * Extends a grid and adds the ComponentRenderer's tracking functionality.
     *
     * @param grid the target grid
     */
    private ComponentRendererComponentStore(Grid grid) {
        this.grid = grid;
        componentRendererContainerItemSetChangeListener = new ComponentRendererContainerItemSetChangeListener();
        componentColumnContainerPropertySetChangeListener = new ComponentColumnContainerPropertySetChangeListener();
        itemIdComponentMappingValueChangeListener = new ItemIdComponentMappingValueChangeListener();
        addContainerChangeListeners();
    }

    /**
     * Get all components used in ComponentRenderers in the extended grid.
     *
     * @return set of components
     */
    public LinkedHashSet<Component> getRendererComponents() {
        return rendererComponents;
    }

    /**
     * Creates a new instance of the {@link ComponentRenderer} which will use
     * this extension to track and add components.
     *
     * @return a new instance of the {@link ComponentRenderer}
     */
    public ComponentRenderer createComponentRenderer() {
        return new ComponentRenderer(this);
    }

    /**
     * Extends a grid and adds the ComponentRenderer's tracking functionality.
     *
     * @param targetGrid the target grid
     */
    public static ComponentRendererComponentStore linkWith(Grid targetGrid) {
        return new ComponentRendererComponentStore(targetGrid);
    }

    private void addContainerChangeListeners(IndexedContainer indexedContainer) {
        indexedContainer.addItemSetChangeListener(componentRendererContainerItemSetChangeListener);
        indexedContainer.addPropertySetChangeListener(componentColumnContainerPropertySetChangeListener);
        indexedContainer.addValueChangeListener(itemIdComponentMappingValueChangeListener);
    }

    private void addContainerChangeListeners(GeneratedPropertyContainer generatedPropertyContainer) {

        // Item and PropertySet Change events are now handled by the wrapping GeneratedPropertyContainer
        // so remove them from the wrapped container
        IndexedContainer wrapped = (IndexedContainer) generatedPropertyContainer.getWrappedContainer();
        wrapped.removeItemSetChangeListener(componentRendererContainerItemSetChangeListener);
        wrapped.removePropertySetChangeListener(componentColumnContainerPropertySetChangeListener);

        generatedPropertyContainer.addItemSetChangeListener(componentRendererContainerItemSetChangeListener);
        generatedPropertyContainer.addPropertySetChangeListener(componentColumnContainerPropertySetChangeListener);
    }

    /**
     * Add the necessary change listeners to keep track of added/removed
     * items or columns to free the unused memory.
     */
    public void addContainerChangeListeners() {

        if (grid.getContainerDataSource() instanceof IndexedContainer) {
            addContainerChangeListeners((IndexedContainer) grid.getContainerDataSource());

        } else if (grid.getContainerDataSource() instanceof GeneratedPropertyContainer) {
            addContainerChangeListeners((GeneratedPropertyContainer) grid.getContainerDataSource());
        }
    }

    /**
     * Adds a component to the extension's component tracking
     * and sets the grid as parent on server- and client-side.
     *
     * @param component the component
     */
    public void addComponent(Component component) {
        rendererComponents.add(component);
        component.setParent(grid);
        grid.markAsDirty();
    }

    /**
     * Removes a component from the extension's component tracking,
     * unsets the parent on client-side and detaches the component on
     * server-side.
     *
     * @param component the component
     */
    private void removeComponent(Component component) {
        if (component.getParent() != null) {
            grid.markAsDirty();
            component.detach();
        }
        rendererComponents.remove(component);
    }

    private void addItemIdComponentMapping(Object itemId, Component component) {
        if (component != null) {
            Set<Component> components = itemIdComponentMapping.get(itemId);
            if (components == null) {
                itemIdComponentMapping.put(itemId, newHashSet(component));
            } else {
                components.add(component);
            }
        }
    }

    /**
     * ItemsetChangeListener which removes components from the grid and tracking,
     * which are not longer part of the grid's container.
     * The adding of the components to the tracking and grid is done in the renderer itself.
     */
    private class ComponentRendererContainerItemSetChangeListener implements Container.ItemSetChangeListener {

        private void addRowComponents(Object itemId, Item item) {
            for (Object columnProperty : componentColumnPropertyIds) {
                addItemIdComponentMapping(itemId, (Component) item.getItemProperty(columnProperty).getValue());
            }
        }

        @Override
        public void containerItemSetChange(Container.ItemSetChangeEvent event) {

            if (event instanceof Container.Indexed.ItemAddEvent) {
                handleItemAdd((Container.Indexed.ItemAddEvent) event);
            } else if (event instanceof Container.Indexed.ItemRemoveEvent) {

                handleItemRemove((Container.Indexed.ItemRemoveEvent) event);
            }

        }

        private void handleItemRemove(Container.Indexed.ItemRemoveEvent itemRemoveEvent) {
            int endItemId = (Integer) itemRemoveEvent.getFirstItemId() + itemRemoveEvent.getRemovedItemsCount();
            for (int currentId = (Integer) itemRemoveEvent.getFirstItemId(); currentId < endItemId; currentId++) {
                Set<Component> removeComponents = itemIdComponentMapping.get(currentId);
                if (removeComponents != null) {
                    for (Component component : removeComponents) {
                        if (component != null) {
                            removeComponent(component);
                        }
                    }
                    itemIdComponentMapping.remove(currentId);
                }
            }
        }

        private void handleItemAdd(Container.Indexed.ItemAddEvent itemAddEvent) {
            int endItemId = (Integer) itemAddEvent.getFirstItemId() + itemAddEvent.getAddedItemsCount();
            for (int currentId = (Integer) itemAddEvent.getFirstItemId(); currentId < endItemId; currentId++) {
                addRowComponents(currentId, itemAddEvent.getContainer().getItem(currentId));
            }
        }
    }

    /**
     * Updates the Set of columns which contain Components on property changes.
     */
    private class ComponentColumnContainerPropertySetChangeListener implements Container.PropertySetChangeListener {

        @Override
        public void containerPropertySetChange(Container.PropertySetChangeEvent event) {
            componentColumnPropertyIds.clear();

            for (Object containerPropertyId : grid.getContainerDataSource().getContainerPropertyIds()) {
                if (grid.getContainerDataSource().getType(containerPropertyId) == Component.class) {
                    componentColumnPropertyIds.add(containerPropertyId);
                }
            }
        }
    }

    private class ItemIdComponentMappingValueChangeListener implements Property.ValueChangeListener {

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            if (event.getProperty().getType() == Component.class) {
                try {
                    Object eventSource = ((EventObject) event).getSource();
                    Object itemId = FieldUtils.readField(eventSource, "itemId", true);
                    addItemIdComponentMapping(itemId, (Component) event.getProperty().getValue());
                } catch (IllegalAccessException e) {
                    // if reading the event field fails do nothing
                }
            }
        }
    }
}
