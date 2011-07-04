package brooklyn.entity.webapp.jboss

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import brooklyn.entity.webapp.JavaWebApp

import brooklyn.event.basic.BasicAttributeSensor
import brooklyn.location.basic.SshBasedJavaWebAppSetup

import brooklyn.location.basic.SshMachineLocation

/**
 * JBoss web application server.
 */
public class JBossNode extends JavaWebApp {

    private static final Logger log = LoggerFactory.getLogger(JBossNode.class)

    public static final int DEFAULT_HTTP_PORT = 8080;

    // Jboss specific
    public static final BasicAttributeSensor<Integer> PORT_INCREMENT = 
            [ Integer, "webapp.portIncrement", "Increment added to default JBoss ports" ];

    
    public JBossNode(Map properties=[:]) {
        super(properties);
        
        def portIncrement = properties.portIncrement ?: 0
        if (portIncrement < 0) {
            throw new IllegalArgumentException("JBoss port increment cannot be negative")
        }
        updateAttribute PORT_INCREMENT, portIncrement
        updateAttribute HTTP_PORT, (DEFAULT_HTTP_PORT + portIncrement)
    }

    public SshBasedJavaWebAppSetup getSshBasedSetup(SshMachineLocation loc) {
        return new JBoss6SshSetup(this, loc);
    }
    
    public void initJmxSensors() {
        jmxAdapter.addSensor(ERROR_COUNT, "jboss.web:type=GlobalRequestProcessor,name=http-*", "errorCount")
        jmxAdapter.addSensor(REQUEST_COUNT, "jboss.web:type=GlobalRequestProcessor,name=http-*", "requestCount")
        jmxAdapter.addSensor(TOTAL_PROCESSING_TIME, "jboss.web:type=GlobalRequestProcessor,name=http-*", "processingTime")
    }

    public void waitForHttpPort() {
		// TODO Groovy Auto-generated method stub
		// Only partially implemented. Perform organize imports
		// to properly import parameter and return types
	}
}
