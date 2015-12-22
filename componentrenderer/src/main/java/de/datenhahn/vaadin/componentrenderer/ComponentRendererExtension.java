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
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import de.datenhahn.vaadin.componentrenderer.client.connectors.ComponentRendererExtensionClientRpc;

import java.util.HashSet;
import java.util.LinkedHashSet;

import static com.google.gwt.thirdparty.guava.common.collect.Sets.newLinkedHashSet;

/**
 * Extension for Vaadin Grid to add the necessary component tracking
 * functionality for supporting the {@link ComponentRenderer}.
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class ComponentRendererExtension extends Grid.AbstractGridExtension {

    private final LinkedHashSet<Component> rendererComponents = new LinkedHashSet<Component>();
    private final HashSet<Object> componentColumnPropertyIds = new HashSet<Object>();
    private Grid targetGrid;
    private ComponentRendererExtensionClientRpc clientRpcProxy;

    public HashSet<Object> getComponentColumnPropertyIds() {
        return componentColumnPropertyIds;
    }
    private ComponentRendererContainerItemSetChangeListener componentRendererContainerItemSetChangeListener;
    private ComponentRendererContainerPropertySetChangeListener componentRendererContainerPropertySetChangeListener;
    public Grid getTargetGrid() {
        return targetGrid;
    }

    /**
     * Extends a grid and adds the ComponentRenderer's tracking functionality.
     *
     * @param targetGrid the target grid
     */
    private ComponentRendererExtension(Grid targetGrid) {
        super(targetGrid);
        this.targetGrid = targetGrid;
        clientRpcProxy = getRpcProxy(ComponentRendererExtensionClientRpc.class);
        componentRendererContainerItemSetChangeListener = new ComponentRendererContainerItemSetChangeListener(this);
        componentRendererContainerPropertySetChangeListener = new ComponentRendererContainerPropertySetChangeListener(this);
        addChangeListeners();
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
    public static ComponentRendererExtension extend(Grid targetGrid) {
        return new ComponentRendererExtension(targetGrid);
    }

    /**
     * Add the necessary change listeners to keep track of added/removed
     * items or columns to free the unused memory.
     */
    public void addChangeListeners() {

        if (targetGrid.getContainerDataSource() instanceof IndexedContainer) {

            IndexedContainer indexedContainer = ((IndexedContainer) targetGrid.getContainerDataSource());
            indexedContainer.addItemSetChangeListener(componentRendererContainerItemSetChangeListener);
            indexedContainer.addPropertySetChangeListener(componentRendererContainerPropertySetChangeListener);

        } else if (targetGrid.getContainerDataSource() instanceof GeneratedPropertyContainer) {

            GeneratedPropertyContainer generatedPropertyContainer = (GeneratedPropertyContainer) targetGrid.getContainerDataSource();
            ((IndexedContainer)generatedPropertyContainer.getWrappedContainer()).removeItemSetChangeListener(componentRendererContainerItemSetChangeListener);
            ((IndexedContainer)generatedPropertyContainer.getWrappedContainer()).removePropertySetChangeListener(componentRendererContainerPropertySetChangeListener);
            generatedPropertyContainer.addItemSetChangeListener(componentRendererContainerItemSetChangeListener);
            generatedPropertyContainer.addPropertySetChangeListener(componentRendererContainerPropertySetChangeListener);
        }

    }

    /**
     * Check if the grid's container contains the component (in a ComponentRenderer).
     *
     * @param component the component
     *
     * @return true if the grid contains the component, false if not
     */
    private boolean containsComponent(Component component) {
        for (Object itemId : targetGrid.getContainerDataSource().getItemIds()) {
            for (Object column : componentColumnPropertyIds) {
                if (component == targetGrid.getContainerDataSource().getContainerProperty(itemId, column).getValue()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Adds a component to the extension's component tracking
     * and sets the grid as parent on server- and client-side.
     *
     * @param component the component
     */
    public void addComponent(Component component) {
        rendererComponents.add(component);
        component.setParent(getParentGrid());
        getParentGrid().markAsDirty();
        clientRpcProxy.addComponentConnector(component.getConnectorId());
        markAsDirty();
    }

    /**
     * Removes a component from the extension's component tracking,
     * unsets the parent on client-side and detaches the component on
     * server-side.
     *
     * @param component the component
     */
    public void removeComponent(Component component) {
        if (component.getParent() != null) {
            clientRpcProxy.removeComponentConnector(component.getConnectorId());
            getParentGrid().markAsDirty();
            component.detach();
            markAsDirty();
        }
        rendererComponents.remove(component);
    }

    /**
     * ItemsetChangeListener which removes components from the grid and tracking,
     * which are not longer part of the grid's container.
     *
     * The adding of the components to the tracking and grid is done in the renderer itself.
     */
    private static class ComponentRendererContainerItemSetChangeListener implements Container.ItemSetChangeListener {
        private ComponentRendererExtension extension;

        public ComponentRendererContainerItemSetChangeListener(ComponentRendererExtension extension) {
            this.extension = extension;
        }

        @Override
        public void containerItemSetChange(Container.ItemSetChangeEvent event) {
            LinkedHashSet<Component> tempComponents = newLinkedHashSet(extension.getRendererComponents());
            for (Component component : tempComponents) {
                if (!extension.containsComponent(component)) {
                    extension.removeComponent(component);
                }
            }
        }
    }

    /**
     * Updates the Set of columns which contain Components on property changes.
     */
    private static class ComponentRendererContainerPropertySetChangeListener implements Container.PropertySetChangeListener {
        private ComponentRendererExtension extension;

        public ComponentRendererContainerPropertySetChangeListener(ComponentRendererExtension extension) {
            this.extension = extension;
        }

        @Override
        public void containerPropertySetChange(Container.PropertySetChangeEvent event) {
            extension.getComponentColumnPropertyIds().clear();

            for (Object containerPropertyId : extension.getTargetGrid().getContainerDataSource().getContainerPropertyIds()) {
                if (extension.getTargetGrid().getContainerDataSource().getType(containerPropertyId) == Component.class) {
                    extension.getComponentColumnPropertyIds().add(containerPropertyId);
                }
            }
        }
    }
}
