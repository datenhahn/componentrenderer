/**
 * Licensed under the Apache License,Version2.0(the"License");you may not
 * use this file except in compliance with the License.You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,software
 * distributed under the License is distributed on an"AS IS"BASIS,WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package de.datenhahn.vaadin.componentrenderer.client.detailskeys;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Timer;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.connectors.GridConnector;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.client.ui.VNotification;
import com.vaadin.client.widget.escalator.ScrollbarBundle;
import com.vaadin.client.widget.grid.DataAvailableEvent;
import com.vaadin.client.widget.grid.DataAvailableHandler;
import com.vaadin.client.widget.grid.events.ScrollEvent;
import com.vaadin.client.widget.grid.events.ScrollHandler;
import com.vaadin.client.widgets.Escalator;
import com.vaadin.client.widgets.Grid;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.Connect;
import de.datenhahn.vaadin.componentrenderer.DetailsKeysExtension;

import java.util.logging.Logger;

/**
 * Handles the expansion and collapsing of the detailsrow with STRG+DOWN (expand) and STRG+UP (collapse).
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
@Connect(DetailsKeysExtension.class)
public class DetailsKeysExtensionConnector extends AbstractExtensionConnector {

    private final DetailsOpenCloseServerRpc detailsRpc = getRpcProxy(DetailsOpenCloseServerRpc.class);

    private Grid target;

    private Logger logger = Logger.getLogger("mylogger");

    @Override
    protected void extend(ServerConnector target) {
        Grid grid = ((GridConnector) target).getWidget();
        this.target = grid;
        grid.addBodyKeyDownHandler(new DetailsKeyDownHandler(detailsRpc));

        ScrollHandler handler = new DelayedScrollHandler();
        HandlerManager
                handlerManager =
                getHandlerManager(getVerticalScrollbar(DetailsKeysExtensionConnector.this.target));
        int handlerCount = handlerManager.getHandlerCount(ScrollEvent.TYPE);
        if (handlerCount == 1) {
            handlerManager.removeHandler(ScrollEvent.TYPE, handlerManager.getHandler(ScrollEvent.TYPE, 0));
            handlerManager.addHandler(ScrollEvent.TYPE, handler);
        } else {
            logger.severe("HandlerManager count is not 1: " + handlerCount);
        }


    }

    private native static HandlerManager getHandlerManager(ScrollbarBundle verticalScrollbarBundle)
        /*-{
            return verticalScrollbarBundle.@com.vaadin.client.widget.escalator.ScrollbarBundle::getHandlerManager()();
        }-*/;


    private native static double getDefaultRowHeight(com.vaadin.client.widgets.Grid<?> grid)
        /*-{
            var escalator = grid.@com.vaadin.client.widgets.Grid::escalator;
            var body = escalator.@com.vaadin.client.widgets.Escalator::getBody()();
            return body.@com.vaadin.client.widgets.Escalator$BodyRowContainerImpl::getDefaultRowHeight()();
        }-*/;


    private native static ScrollbarBundle.VerticalScrollbarBundle getVerticalScrollbar(com.vaadin.client.widgets
                                                                                               .Grid<?> grid)
        /*-{
            var escalator = grid.@com.vaadin.client.widgets.Grid::escalator;
            return escalator.@com.vaadin.client.widgets.Escalator::verticalScrollbar;
        }-*/;

    private native static Escalator getEscalator(com.vaadin.client.widgets.Grid<?> grid)
        /*-{
            return grid.@com.vaadin.client.widgets.Grid::escalator;
        }-*/;

    private native static void callOnScroll(com.vaadin.client.widgets.Grid<?> grid)
        /*-{
            var escalator = grid.@com.vaadin.client.widgets.Grid::escalator;
            var scroller = escalator.@com.vaadin.client.widgets.Escalator::scroller;
            scroller.@com.vaadin.client.widgets.Escalator$Scroller::onScroll()();
        }-*/;

    class DelayedScrollExecutor extends Timer {

        DelayedScrollHandler scrollHandler;

        public DelayedScrollExecutor(DelayedScrollHandler scrollHandler) {
            this.scrollHandler = scrollHandler;
        }

        @Override
        public void run() {
            callOnScroll(target);
            scrollHandler.setDelayedScroll(false);
        }
    }

    class DelayedScrollHandler implements ScrollHandler {
        private boolean delayedScroll = false;
        private double lastPosition = 0;
        private Timer scrollExecutor;
        private long lastTime = 0;

        public DelayedScrollHandler() {
            lastPosition = getVerticalScrollbar(target).getScrollPos();
            lastTime = System.currentTimeMillis();
            scrollExecutor = new DelayedScrollExecutor(this);
        }

        @Override
        public void onScroll(ScrollEvent event) {
            long scrollTime = System.currentTimeMillis();
            long timeDiff = scrollTime - lastTime;
            double diff = getVerticalScrollbar(target).getScrollPos() - lastPosition;
            double delta = Math.abs(diff);
            double speed = delta / timeDiff;
            lastPosition = getVerticalScrollbar(target).getScrollPos();
            lastTime = scrollTime;
            double max_delta = 15 * getDefaultRowHeight(target);

            if (delta > max_delta) {
                delayedScroll = true;
                scrollExecutor.cancel();
                scrollExecutor.schedule(200);
            }

            if (!delayedScroll) {
                showMessage("Speed: " + speed);
                callOnScroll(target);
            } else {
                //showCurrentPosition();
                showMessage("Delayed Scroll, Speed: " + speed);
            }

            fireEvent(new ScrollEvent());
        }

        private void showMessage(String message) {
            final VNotification notification = VNotification.createNotification(100, target);
            notification.setOwner(target);
            int left = target.getElement().getAbsoluteLeft();
            int top = target.getElement().getAbsoluteTop();
            int height = target.getElement().getOffsetHeight();
            int width = target.getElement().getOffsetWidth();
            int n_height = notification.getElement().getOffsetHeight();
            int n_width = notification.getElement().getOffsetWidth();
            int p_left = left + Math.round((width - n_width) / 2);
            int p_top = top + Math.round((height - n_height) / 2);
            notification.show(message, Position.MIDDLE_CENTER, null);
            notification.setPopupPosition(p_left, p_top);
            new Timer() {
                @Override
                public void run() {
                    notification.hide(true);
                }
            }.schedule(199);

        }

        private void showCurrentPosition() {
            showMessage("        " + Math.round(lastPosition / getDefaultRowHeight(target))
                              + "        ");
        }

        public void setDelayedScroll(boolean delayedScroll) {
            this.delayedScroll = delayedScroll;
        }
    }
}