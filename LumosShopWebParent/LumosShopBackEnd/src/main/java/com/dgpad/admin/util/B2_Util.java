package com.dgpad.admin.util;


import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class B2_Util {


    private static final String END_POINT = "https://s3.us-east-005.backblazeb2.com";
    private static final String BUCKET_NAME;
    private static final String ACCESS_KEY_ID;
    private static final String SECRET_ACCESS_KEY;
    private static final String B2_REGION;

    static {
        BUCKET_NAME =  System.getenv("B2_BUCKETNAME");
        ACCESS_KEY_ID = System.getenv("AWS_ACCESS_KEY_ID");
        SECRET_ACCESS_KEY = System.getenv("AWS_SECRET_ACCESS_KEY");
        B2_REGION = System.getenv("B2_REGION");
    }


    public static S3Client createB2() {
        AwsBasicCredentials credentialsProvider = AwsBasicCredentials.create(ACCESS_KEY_ID, SECRET_ACCESS_KEY);
        S3Client client = S3Client
                .builder()
                .endpointOverride(URI.create(END_POINT))
                .credentialsProvider(StaticCredentialsProvider.create(credentialsProvider))
                .build();
        return client;
    }

    public static ListObjectsRequest listingAllFilesInBackBlazeB2(String folderName) {
        ListObjectsRequest ReqList = ListObjectsRequest.builder().bucket(BUCKET_NAME).prefix(folderName).build();
        return ReqList;
    }

    public static ListObjectsRequest listAllFilesInBackBlazeB2ForRemove(String folderName) {
        ListObjectsRequest ReqList = ListObjectsRequest.builder()
                .bucket(BUCKET_NAME).prefix(folderName + "/").build();

        return ReqList;
    }
    public static PutObjectRequest putFileInBackBlazeB2(String folderName, String fileName, InputStream inputStream) {
        PutObjectRequest req = PutObjectRequest.builder().bucket(BUCKET_NAME)
                .key(folderName + "/" + fileName).acl("public-read").build();

        return req;
    }

    public static DeleteObjectRequest deleteFileFromBackBlazeB2(String fileName) {
        DeleteObjectRequest req = DeleteObjectRequest.builder().bucket(BUCKET_NAME)
                .key(fileName).build();

        return req;
    }


    public static void uploadFile(String folderName, String fileName, InputStream inputStream) {
        S3Client client = createB2();

        PutObjectRequest request = putFileInBackBlazeB2(folderName, fileName, inputStream);

        try {
            int contentLength = inputStream.available();
            client.putObject(request, RequestBody.fromInputStream(inputStream, contentLength));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static List<String> listDir(String dir) {

        S3Client client = S3Client.builder().endpointOverride(URI.create(END_POINT)).build();

        ListObjectsRequest listObjectsRequest
                = ListObjectsRequest.builder()
                .bucket(BUCKET_NAME).prefix(dir).build();

        ListObjectsResponse response
                = client.listObjects(listObjectsRequest);
        List<S3Object> contents
                = response.contents();
        ListIterator<S3Object> listIterator = contents.listIterator();
        List<String> keylist = new ArrayList<>();

        while (listIterator.hasNext()) {
            S3Object s3Object = listIterator.next();
            keylist.add(s3Object.key());

        }

        return keylist;
    }
    public static void removeFolder(String folderName) {

        S3Client client = createB2();
        ListObjectsRequest listRequest = listAllFilesInBackBlazeB2ForRemove(folderName);
        ListObjectsResponse response = client.listObjects(listRequest);
        List<S3Object> contents = response.contents();

        ListIterator<S3Object> listIterator = contents.listIterator();
        while (listIterator.hasNext()) {
            S3Object object = listIterator.next();

            DeleteObjectRequest request = deleteFileFromBackBlazeB2(object.key());
            client.deleteObject(request);
        }
    }
    public static void deleteFile(String fileName) {
        S3Client client = createB2();
        DeleteObjectRequest request = deleteFileFromBackBlazeB2(fileName);
        client.deleteObject(request);
    }


}
