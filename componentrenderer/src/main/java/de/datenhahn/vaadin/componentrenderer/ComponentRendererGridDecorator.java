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
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashSet;

import static com.google.gwt.thirdparty.guava.common.collect.Iterators.addAll;

/**
 * Decorates any Grid implementation (e.g. if the Grid was subclassed) with the
 * ComponentRenderer functionality.
 * If you do not have a custom base class, just use the Vaadin {@link Grid}.
 * That includes:
 * - override of the {@link Grid#iterator()} method to include additional rendererComponents
 * - method for retrieving the component renderer {@link ComponentRendererProvider#createComponentRenderer()}
 *
 * @param <T> the base class that is decorated
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
public class ComponentRendererGridDecorator<T extends Grid> implements MethodInterceptor {

    private ComponentRendererComponentTracker componentRendererComponentTracker;

    /**
     * Decorates a subclass of Grid to add the ComponentRenderer functionality.
     *
     * @param myGridClass the base class that is decorated
     * @return a targetGrid class which supports the component renderer
     */
    public T decorate(Class<T> myGridClass) {

        Enhancer gridEnhancer = new Enhancer();
        gridEnhancer.setSuperclass(myGridClass);

        // add interface with method to retrieve the component renderer
        gridEnhancer.setInterfaces(new Class[]{ComponentRendererProvider.class, GridUtilityMethods.class});

        gridEnhancer.setClassLoader(myGridClass.getClass().getClassLoader());

        // set callback which replaces the iterator method
        gridEnhancer.setCallback(this);

        T enhancedGrid = myGridClass.cast(gridEnhancer.create());

        componentRendererComponentTracker = ComponentRendererComponentTracker.linkWith(enhancedGrid);

        return enhancedGrid;
    }

    /**
     * Intercepts the following methods and overrides them or implements them:
     * - overrides  {@link Grid#iterator()} to include ComponentRenderer's component in the Grid's hierarchy
     * - overrides  {@link Grid#setContainerDataSource(Container.Indexed)} so the change listeners can be updated
     * after setting a new container
     * - implements {@link ComponentRendererProvider#createComponentRenderer()}
     *
     * @param decoratedInstance the decorated instance calling the interceptor
     * @param method            the called method
     * @param parameters        the method parameters
     * @param methodProxy       the method proxy
     * @return the return value
     * @throws Throwable
     */
    @Override
    public Object intercept(Object decoratedInstance, Method method, Object[] parameters, MethodProxy methodProxy) throws Throwable {

        if ("iterator".equals(method.getName())) {

            Iterator<Component> iterator = (Iterator) methodProxy.invokeSuper(decoratedInstance, parameters);
            LinkedHashSet<Component> allComponents = new LinkedHashSet<Component>();

            addAll(allComponents, iterator);
            allComponents.addAll(componentRendererComponentTracker.getRendererComponents());

            return allComponents.iterator();

        } else if ("createComponentRenderer".equals(method.getName())) {
            return componentRendererComponentTracker.createComponentRenderer();

        } else if ("setContainerDataSource".equals(method.getName())) {

            methodProxy.invokeSuper(decoratedInstance, parameters);
            componentRendererComponentTracker.addContainerChangeListeners();
            return null;

        } else if ("forceReRender".equals(method.getName())) {
            Grid grid = ((Grid) decoratedInstance);
            grid.setCellStyleGenerator(grid.getCellStyleGenerator());
            return null;
        } else {

            return methodProxy.invokeSuper(decoratedInstance, parameters);
        }
    }
}


