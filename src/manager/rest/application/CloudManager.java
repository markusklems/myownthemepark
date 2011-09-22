package manager.rest.application;

import manager.rest.resources.ClusterResource;
import manager.rest.resources.NodeResource;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;



public class CloudManager extends Application {
	
	static Logger logger = Logger.getLogger(CloudManager.class);
	
    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createInboundRoot() {  	
        // Create a router Restlet that routes each call to a new instance of the resources.
        Router router = new Router(getContext());

        // Defines the route to a cluster
        router.attach("/{clusterName}", ClusterResource.class);
        // Defines the route to a server within a cluster
        router.attach("/{clusterName}/{nodeName}", NodeResource.class);

        return router;
    }

	@Override
	public synchronized void start() throws Exception {
    	logger.info("Elvis has entered the building.");
		super.start();
	}
    
    

}   