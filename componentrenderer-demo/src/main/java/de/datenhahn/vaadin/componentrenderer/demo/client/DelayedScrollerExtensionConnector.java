/*
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

package de.datenhahn.vaadin.componentrenderer.demo.client;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Timer;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.connectors.GridConnector;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.client.ui.VNotification;
import com.vaadin.client.widget.escalator.ScrollbarBundle;
import com.vaadin.client.widget.grid.events.ScrollEvent;
import com.vaadin.client.widget.grid.events.ScrollHandler;
import com.vaadin.client.widgets.Grid;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.Connect;
import de.datenhahn.vaadin.componentrenderer.demo.DelayedScrollerExtension;

import java.util.logging.Logger;

/**
 * Enables table like scrolling.
 *
 * @author Jonas Hahn (jonas.hahn@datenhahn.de)
 */
@Connect(DelayedScrollerExtension.class)
public class DelayedScrollerExtensionConnector extends AbstractExtensionConnector {

    private transient Grid target;

    private transient Logger logger = Logger.getLogger(DelayedScrollerExtensionConnector.class.getName());

    @Override
    protected void extend(ServerConnector target) {
        this.target = (Grid) ((GridConnector) target).getWidget();

        ScrollHandler handler = new DelayedScrollHandler();
        overwriteCurrentScrollHandler(handler);


    }

    private void overwriteCurrentScrollHandler(ScrollHandler handler) {
        HandlerManager
                handlerManager =
                getHandlerManager(getVerticalScrollbar(DelayedScrollerExtensionConnector.this.target));
        int handlerCount = handlerManager.getHandlerCount(ScrollEvent.TYPE);
        if (handlerCount == 1) {
            handlerManager.removeHandler(ScrollEvent.TYPE, handlerManager.getHandler(ScrollEvent.TYPE, 0));
            handlerManager.addHandler(ScrollEvent.TYPE, handler);
        } else {
            logger.severe("HandlerManager count is not 1 but : " + handlerCount + " ");
        }
    }

    @Override
    public DelayedScrollerState getState() {
        return (DelayedScrollerState) super.getState();
    }

    private native static HandlerManager getHandlerManager(ScrollbarBundle verticalScrollbarBundle)
        /*-{
            return verticalScrollbarBundle.@com.vaadin.client.widget.escalator.ScrollbarBundle::getHandlerManager()();
        }-*/;


    private native static double getDefaultRowHeight(Grid<?> grid)
        /*-{
            var escalator = grid.@com.vaadin.client.widgets.Grid::escalator;
            var body = escalator.@com.vaadin.client.widgets.Escalator::getBody()();
            return body.@com.vaadin.client.widgets.Escalator$BodyRowContainerImpl::getDefaultRowHeight()();
        }-*/;


    private native static ScrollbarBundle.VerticalScrollbarBundle getVerticalScrollbar(Grid<?> grid)
        /*-{
            var escalator = grid.@com.vaadin.client.widgets.Grid::escalator;
            return escalator.@com.vaadin.client.widgets.Escalator::verticalScrollbar;
        }-*/;

    private native static void callOnScroll(Grid<?> grid)
        /*-{
            var escalator = grid.@com.vaadin.client.widgets.Grid::escalator;
            var scroller = escalator.@com.vaadin.client.widgets.Escalator::scroller;
            scroller.@com.vaadin.client.widgets.Escalator$Scroller::onScroll()();
        }-*/;

    private class DelayedScrollExecutor extends Timer {

        private DelayedScrollHandler scrollHandler;

        public DelayedScrollExecutor(DelayedScrollHandler scrollHandler) {
            this.scrollHandler = scrollHandler;
        }

        @Override
        public void run() {
            callOnScroll(target);
            scrollHandler.setDelayScroll(false);
        }
    }

    private class DelayedScrollHandler implements ScrollHandler {
        private boolean delayScroll = false;
        private double lastPosition = 0;
        private Timer scrollExecutor;
        private int SCROLL_DELAY_MS = 200;

        public DelayedScrollHandler() {
            lastPosition = getScrollPosition();
            scrollExecutor = new DelayedScrollExecutor(this);
        }

        private double getMaxDelta() {
            return getState().maxDeltaInRows * getDefaultRowHeight(target);
        }

        private int getRowPosition() {
            return (int) Math.round(lastPosition / getDefaultRowHeight(target)) + 1;
        }

        private String injectRowPosition(String template) {
            return template.replaceAll("%d", Integer.toString(getRowPosition()));
        }

        @Override
        public void onScroll(ScrollEvent event) {
            double diff = getScrollPosition() - lastPosition;
            double delta = Math.abs(diff);
            lastPosition = getScrollPosition();

            if (delta > getMaxDelta()) {
                delayScroll = true;
                scrollExecutor.cancel();
                scrollExecutor.schedule(SCROLL_DELAY_MS);
            }

            if (!delayScroll) {
                callOnScroll(target);
            } else {
                showMessage(injectRowPosition(getState().scrollMessage));
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
            int p_left = left + Math.round((width - message.length() * 4 - 20) / 2);
            int p_top = top + Math.round((height - 30) / 2);
            notification.show(message, Position.MIDDLE_CENTER, null);
            notification.setPopupPosition(p_left, p_top);
            new Timer() {
                @Override
                public void run() {
                    notification.hide(true);
                }
            }.schedule(SCROLL_DELAY_MS - 1);

        }

        public void setDelayScroll(boolean delayScroll) {
            this.delayScroll = delayScroll;
        }
    }

    private double getScrollPosition() {
        return getVerticalScrollbar(target).getScrollPos();
    }
}