package manager.aws.ec2;

import java.util.*;

import manager.zk.*;

import org.apache.log4j.*;
import org.apache.zookeeper.*;

import util.*;

import com.amazonaws.*;
import com.amazonaws.services.ec2.*;
import com.amazonaws.services.ec2.model.*;


public class EC2ClusterManager
    extends ClusterManager
{

    static Logger           logger = Logger.getLogger( EC2ClusterManager.class );

    private AmazonEC2Client ec2Client;


    public EC2ClusterManager( ZooKeeperClient zkClient )
    {
        super( zkClient );
        ec2Client = new AmazonEC2Client( AWSUtil.getAWSCredentials() );
        ec2Client.setEndpoint( "ec2.eu-west-1.amazonaws.com" );
    }

    public String createEC2Node( String clusterName,
                                 String instanceType,
                                 String imageID ) throws KeeperException, InterruptedException
    {
        String toReturn=null;

        // Try to create a new unique key pair.
        String uuidKeyPairName = UUID.randomUUID().toString();
        CreateKeyPairRequest createKeypairReq = new CreateKeyPairRequest( uuidKeyPairName );
        try {
            CreateKeyPairResult createKeyPairResult = ec2Client.createKeyPair( createKeypairReq );
            final KeyPair keypair = createKeyPairResult.getKeyPair();

            RunInstancesRequest req = new RunInstancesRequest();
            req.setKeyName( keypair.getKeyName() );
            // req.setSecurityGroups(securityGroups);
            req.setImageId( imageID );
            req.setInstanceType( instanceType );
            req.setMinCount( 1 );
            req.setMaxCount( 1 );
            RunInstancesResult result = ec2Client.runInstances( req );

            List<Instance> instances = result.getReservation().getInstances();

            for ( Instance i : instances ) {
                // Node name = instance id
                boolean ok = addInstanceNodeToCluster( clusterName, i.getInstanceId() );
                if ( ok ) {
                    toReturn = i.getInstanceId();
                    logger.info( "Successfully created EC2 instance with id " + i.getInstanceId());
                    // Poll EC2 instance until it has been created and connected with ZooKeeper.
                    // TODO clean up this mess
                    final int PING_INTERVAL = 5000;
                    int timeoutCountdown = 20; // #intervals
                    final String EC2_INSTANCE_RUNNING_STATE = "running";
                    boolean ec2InstanceHasBeenCreated = false;
                    boolean ec2InstanceHasConnectedToZooKeeper = false;
                    DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
                    ArrayList<String> instanceIds = new ArrayList<String>();
                    instanceIds.add( i.getInstanceId() );
                    describeInstancesRequest.setInstanceIds( instanceIds );
                    while ( !(ec2InstanceHasBeenCreated && ec2InstanceHasConnectedToZooKeeper) && timeoutCountdown-- > 0 ) {
                        Thread.sleep( PING_INTERVAL );
                        DescribeInstancesResult describeInstancesResult = ec2Client.describeInstances( describeInstancesRequest );
                        for ( Reservation res : describeInstancesResult.getReservations() ) {
                            for ( Instance resInstance : res.getInstances() ) {
                                InstanceState state = resInstance.getState();
                                if ( resInstance.getInstanceId().equals( i.getInstanceId() ) && state.getName().equals( EC2_INSTANCE_RUNNING_STATE ) ) {
                                    ec2InstanceHasBeenCreated = true;
                                    if ( doesNodeExist( clusterName, i.getInstanceId() ) )
                                        ec2InstanceHasConnectedToZooKeeper = true;
                                }
                            }
                        }
                    }
                    if ( !ec2InstanceHasBeenCreated )
                        logger.error( "Failed to launch EC2 instance." );
                    else {
                        if ( !ec2InstanceHasConnectedToZooKeeper )
                            logger.error( "EC2 instance failed to connect to ZooKeepeer." );
                        else {
                            saveDataOnEC2Node( clusterName, i.getInstanceId(), keypair );
                        }
                    }

                } else
                    logger.error( "Something went wrong while trying to create zNode with name " + i.getInstanceId() );
            }
        } catch ( AmazonServiceException ex ) {
            // TODO throw exception, saying that zNode creation failed for some reason.
        }

        return toReturn;
    }

    public void saveDataOnEC2Node( String clusterName,
                                   String nodeName,
                                   KeyPair keypair ) throws KeeperException, InterruptedException
    {
        // store the key pair on the instance node
        // TODO security problem: transfer key material as plain text
        saveDataOnInstanceNode( clusterName, nodeName, keypair.getKeyMaterial().getBytes() );
    }

    public String createEC2MicroNode( String clusterName,
                                      String imageID ) throws KeeperException, InterruptedException
    {
        return createEC2Node( clusterName, InstanceType.T1Micro.toString(), imageID );
    }

    public String createEC2DefaultMicroNode( String clusterName ) throws KeeperException, InterruptedException
    {
        return createEC2Node( clusterName, InstanceType.T1Micro.toString(), "ami-359ea941" );
    }

    public void destroyEC2Node( String clusterName,
                                String nodeName ) throws KeeperException, InterruptedException
    {
        boolean ok = removeNodeFromCluster( clusterName, nodeName );
        if ( ok ) {
            TerminateInstancesRequest req = new TerminateInstancesRequest();
            List<String> instanceIds = new ArrayList<String>();
            instanceIds.add( nodeName );
            req.setInstanceIds( instanceIds );
            ec2Client.terminateInstances( req );
        }
    }

}
