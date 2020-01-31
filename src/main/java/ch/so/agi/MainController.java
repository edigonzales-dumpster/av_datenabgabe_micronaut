package ch.so.agi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Value;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@Introspected
@Controller("/av_datenabgabe") 
public class MainController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    //@Value("${app.dataserviceurl:6}")
    @Property(name = "app.dataServiceUrl")
    private String dataServiceUrl;

    @Property(name = "app.pdfMapUrl")    
    private String pdfMapUrl;

    @Property(name = "app.s3BaseUrl")    
    private String s3BaseUrl;

    @Property(name = "app.itfsoBucketName")    
    private String itfsoBucketName;

    @Property(name = "app.itfchBucketName") 
    private String itfchBucketName;

    @Property(name = "app.dxfBucketName") 
    private String dxfBucketName;
    
    @Property(name = "app.shpUrl")    
    private String shpUrl;

    @Property(name = "app.awsAccessKey")    
    private String awsAccessKey;
    
    @Property(name = "app.awsSecretKey")    
    private String awsSecretKey;    
    
    private AWSCredentials credentials;

    @PostConstruct
    public void print() {
        System.out.println("Hello!");
        System.out.println(awsAccessKey);
        System.out.println(awsSecretKey);
        System.out.println(this.dataServiceUrl);
        
        credentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);        
    }
    
    @Get 
    @Produces(MediaType.TEXT_PLAIN) 
    public String index() {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

        LOGGER.info("Start S3 request");
        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_CENTRAL_1)
                .build();
        
        ObjectListing objectListing = s3client.listObjects(itfsoBucketName);        
        Map<String, Date> objectMap = objectListing.getObjectSummaries().stream().collect(
                Collectors.toMap(S3ObjectSummary::getKey, S3ObjectSummary::getLastModified));
        LOGGER.info("End S3 request");


        
        return "Hello World"; 
    }

}
