package com.lumosshop.common.constant;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

import java.net.URI;

public class Constants {

    public static final String B2_ADDRESS = initializeB2Address();
    public static final String B2_Region = System.getenv("B2_REGION");

    private static final String B2_ACCOUNT_ID = System.getenv("AWS_ACCESS_KEY_ID");
    private static final String B2_APP_ID = System.getenv("AWS_SECRET_ACCESS_KEY");
    private static final String END_POINT = "https://s3.us-east-005.backblazeb2.com";


    private static String initializeB2Address() {
        String b2BucketName = System.getenv("B2_BUCKETNAME");
        String b2Region = System.getenv("B2_REGION");

        if (b2BucketName == null) {
            return "";
        }

        String pattern = "https://%s.s3.%s.backblazeb2.com";
        return String.format(pattern, b2BucketName, b2Region);
    }

    public static void main(String[] args) {
        System.out.println("B2 Address: " + B2_ADDRESS);
        System.out.println("B2_ACCOUNT_ID: " + B2_ACCOUNT_ID);
        System.out.println("B2_APP_ID: " + B2_APP_ID);
    }

}
