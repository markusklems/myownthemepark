package manager.rest.resources;

import java.io.IOException;

import manager.aws.ec2.EC2ClusterManager;
import manager.model.Node;
import manager.zk.ZooKeeperClient;

import org.apache.zookeeper.KeeperException;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Delete;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public class NodeResource extends ServerResource {
	
	Node node;
	EC2ClusterManager ec2ClusterManager;
	
    @Override
    protected void doInit() throws ResourceException {
        try {
			ZooKeeperClient zkClient = ZooKeeperClient.getInstance();
			ec2ClusterManager = new EC2ClusterManager(zkClient);
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
     * Handle POST requests: create a new node in a cluster.
     */
    @Post
    public Representation createNode() {
        Representation toReturn = null;
        
        String clusterResourceName = (String) getRequest().getAttributes().get("clusterName");
        String nodeResourceName = (String) getRequest().getAttributes().get("nodeName");
        // TODO Save Cluster model ? MVC needed?
        
        try {
        	ec2ClusterManager.createEC2DefaultMicroNode(clusterResourceName,nodeResourceName);
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        setStatus(Status.SUCCESS_CREATED);
        Representation rep = new StringRepresentation("Node "+nodeResourceName+" in cluster "+clusterResourceName+" created",
                    MediaType.TEXT_PLAIN);

            rep.setLocationRef(getRequest().getResourceRef().getIdentifier() + "/"
                    + clusterResourceName);
            toReturn = rep;

        return toReturn;
    }
    
    /**
     * Handle DELETE requests: delete a node in a cluster.
     */
    @Delete
    public Representation deleteNode() {
        Representation toReturn = null;
        
        String clusterResourceName = (String) getRequest().getAttributes().get("clusterName");
        String nodeResourceName = (String) getRequest().getAttributes().get("nodeName");
        // TODO Save Cluster model ? MVC needed?
        
        try {
        	ec2ClusterManager.destroyEC2Node(clusterResourceName,nodeResourceName);
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        setStatus(Status.SUCCESS_CREATED);
        Representation rep = new StringRepresentation("Node "+nodeResourceName+" in cluster "+clusterResourceName+" created",
                    MediaType.TEXT_PLAIN);

            rep.setLocationRef(getRequest().getResourceRef().getIdentifier() + "/"
                    + clusterResourceName);
            toReturn = rep;

        return toReturn;
    }

}
