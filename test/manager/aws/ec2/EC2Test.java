package manager.aws.ec2;

import util.AWSUtil;

import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Reservation;

public class EC2Test {

	public static void main(String[] args) {
		System.out.println("DESCRIBE EC2 INSTANCES");
		AmazonEC2Client ec2Client = new AmazonEC2Client(
				AWSUtil.getAWSCredentials());
		ec2Client.setEndpoint("ec2.eu-west-1.amazonaws.com");
		DescribeInstancesResult result = ec2Client.describeInstances();

		for (Reservation r : result.getReservations())
			System.out.println(r);
	}

}
