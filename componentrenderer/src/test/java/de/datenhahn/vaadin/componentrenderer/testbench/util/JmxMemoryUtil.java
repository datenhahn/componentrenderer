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

package de.datenhahn.vaadin.componentrenderer.testbench.util;

import com.sun.tools.attach.*;
import com.sun.tools.attach.spi.AttachProvider;

import javax.management.*;
import javax.management.openmbean.CompositeData;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.Set;

public class JmxMemoryUtil {
    private ObjectName memory;
    private MBeanServerConnection remote;
    private JMXServiceURL target;

    public JmxMemoryUtil() throws IOException, AttachNotSupportedException, AgentLoadException,
            AgentInitializationException, AttributeNotFoundException, MBeanException, ReflectionException,
            InstanceNotFoundException, MalformedObjectNameException {
        final AttachProvider attachProvider = AttachProvider.providers().get(0);

        VirtualMachineDescriptor descriptor = null;
        for (VirtualMachineDescriptor virtualMachineDescriptor : attachProvider.listVirtualMachines()) {
            descriptor = virtualMachineDescriptor;
            if (isJettyDescriptor(descriptor)) {
                break;
            }
        }

        if (descriptor == null) {
            throw new RuntimeException("No jetty descriptor found, did you start jetty using mvn jetty:run?");
        }

        final VirtualMachine virtualMachine = attachProvider.attachVirtualMachine(descriptor);
        virtualMachine.loadAgent(System.getProperty("java.home")
                                 + "/../jre/lib/management-agent.jar", "com.sun.management.jmxremote");
        final Object
                portObject =
                virtualMachine.getAgentProperties().get("com.sun.management.jmxremote.localConnectorAddress");

        target = new JMXServiceURL(portObject + "");


    }

    private void reconnect() throws IOException, MalformedObjectNameException {
        final JMXConnector connector = JMXConnectorFactory.connect(target);
        remote = connector.getMBeanServerConnection();
        memory = new ObjectName("java.lang:type=Memory");
    }

    public long currentMem() throws AttributeNotFoundException, MBeanException, ReflectionException,
            InstanceNotFoundException, IOException, MalformedObjectNameException
    {
        reconnect();
        CompositeData cd = (CompositeData) remote.getAttribute(memory, "HeapMemoryUsage");
        return ((Long) cd.get("used")) / 1024;
    }

    public long maxMem() throws AttributeNotFoundException, MBeanException, ReflectionException,
            InstanceNotFoundException, IOException, MalformedObjectNameException
    {
        reconnect();
        CompositeData cd = (CompositeData) remote.getAttribute(memory, "HeapMemoryUsage");
        return ((Long) cd.get("max")) / 1024;
    }

    public String memString() throws AttributeNotFoundException, MBeanException, ReflectionException,
            InstanceNotFoundException, IOException, MalformedObjectNameException
    {
        return currentMem() + " / " + maxMem() + " used";
    }

    public void forceGc() throws ReflectionException, MBeanException, InstanceNotFoundException, IOException,
            MalformedObjectNameException {
        reconnect();
        remote.invoke(memory, "gc", null, null);
        try {
            Thread.sleep(100);
            remote.invoke(memory, "gc", null, null);
            Thread.sleep(100);
            remote.invoke(memory, "gc", null, null);
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isJettyDescriptor(VirtualMachineDescriptor virtualMachineDescriptor) {
        if (virtualMachineDescriptor.displayName()
                                    .equals("org.codehaus.plexus.classworlds.launcher.Launcher jetty:run"))
        {
            System.out.println(virtualMachineDescriptor.displayName());
            return true;
        }
        return false;
    }
}
