package global.sns.configure;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoDbConfig {

    @Bean
    public DynamoDBMapper mapper(){
        return new DynamoDBMapper(dynamoDBMapper());
    }

    @Bean(name = "amazonDynamoDB")
    public AmazonDynamoDB dynamoDBMapper(){
        return AmazonDynamoDBAsyncClientBuilder.standard().withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration("http://localhost:8090","ap-south-1"))
                .withCredentials((new AWSStaticCredentialsProvider(new BasicAWSCredentials("key","key"))))
                .build();

    }
}
