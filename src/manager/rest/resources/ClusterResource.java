package manager.rest.resources;

import java.io.IOException;

import manager.model.Cluster;
import manager.zk.ClusterManager;
import manager.zk.ZooKeeperClient;

import org.apache.zookeeper.KeeperException;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;



public class ClusterResource extends ServerResource {
	
	Cluster cluster;
	ClusterManager clusterManager;
	
    @Override
    protected void doInit() throws ResourceException {
        // Get the "clusterName" attribute value taken from the URI template
        // /{clusterName}.
        try {
			ZooKeeperClient zkClient = ZooKeeperClient.getInstance();
			clusterManager = new ClusterManager(zkClient);
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

    /**
     * Handle POST requests: create a new cluster.
     */
    @Post
    public Representation createCluster() {
        Representation toReturn = null;
        
        String clusterResourceName = (String) getRequest().getAttributes().get("clusterName");
        // TODO Save Cluster model ? MVC needed?
        
        try {
        	clusterManager.createCluster(clusterResourceName);
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  
        // TODO
//        if(true) {
        // Set the response's status and entity
        setStatus(Status.SUCCESS_CREATED);
        Representation rep = new StringRepresentation("Cluster "+clusterResourceName+" created",
                    MediaType.TEXT_PLAIN);
        // Indicates where is located the new resource.
            rep.setLocationRef(getRequest().getResourceRef().getIdentifier() + "/"
                    + clusterResourceName);
            toReturn = rep;
//        }
//        else { // Cluster is already registered.
//            setStatus(Status.CLIENT_ERROR_NOT_FOUND);
//            result = new StringRepresentation("Cluster " + clusterResourceName
//                    + " already exists.",
//                    MediaType.TEXT_PLAIN);
//        }

        return toReturn;
    }
	
}
