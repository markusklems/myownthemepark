package manager.aws.ec2;

import java.util.ArrayList;
import java.util.List;

import manager.rest.application.CloudManager;
import manager.zk.ClusterManager;
import manager.zk.ZooKeeperClient;

import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;

import util.AWSUtil;
import cloud.node.zk.NodeManager;

import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;

public class EC2ClusterManager extends ClusterManager {
	
	static Logger logger = Logger.getLogger(EC2ClusterManager.class);

	private AmazonEC2Client ec2Client;

	public EC2ClusterManager(ZooKeeperClient zkClient) {
		super(zkClient);
		ec2Client = new AmazonEC2Client(AWSUtil.getAWSCredentials());
		ec2Client.setEndpoint("ec2.eu-west-1.amazonaws.com");
	}

	public void createEC2Node(String clusterName, String nodeName,
			String instanceType, String imageID) throws KeeperException,
			InterruptedException {

		RunInstancesRequest req = new RunInstancesRequest();
		req.setKeyName(AWSUtil.EC2_INSTANCE_PRIVATE_KEY_NAME_DEFAULT);
		// req.setSecurityGroups(securityGroups);
		req.setImageId(imageID);
		req.setInstanceType(instanceType);
		req.setMinCount(1);
		req.setMaxCount(1);
		RunInstancesResult result = ec2Client.runInstances(req);
		
		List<Instance> instances = result.getReservation().getInstances();
		
		for(Instance i : instances) {
			boolean ok = addNodeToCluster(clusterName, i.getInstanceId());
			if(ok)
				logger.info("Successfully created EC2 instance "+i.getInstanceId());
			else
				logger.error("Something went wrong while trying to create instance "+i.getInstanceId());
		}
	}

	public void createEC2MicroNode(String clusterName, String nodeName,
			String imageID) throws KeeperException, InterruptedException {
		createEC2Node(clusterName, nodeName, InstanceType.T1Micro.toString(),
				imageID);
	}

	public void createEC2DefaultMicroNode(String clusterName, String nodeName)
			throws KeeperException, InterruptedException {
		createEC2Node(clusterName, nodeName, InstanceType.T1Micro.toString(),
				"ami-359ea941");
	}

	public void destroyEC2Node(String clusterName, String nodeName) throws KeeperException, InterruptedException {
		boolean ok = removeNodeFromCluster(clusterName, nodeName);
		if (ok) {
			TerminateInstancesRequest req = new TerminateInstancesRequest();
			List<String> instanceIds = new ArrayList<String>();
			instanceIds.add(nodeName);
			req.setInstanceIds(instanceIds);
			ec2Client.terminateInstances(req);
		}
	}

}
