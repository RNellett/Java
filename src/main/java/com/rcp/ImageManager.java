package com.rcp;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;

public class ImageManager {
    private static final String BUCKET_PREFIX = "rcp_";
    private static Storage _storage = null;

    //	Static constructor, instantiate the client
    static {
        try {
            _storage = StorageOptions.getDefaultInstance().getService();
        } catch (Exception ex) {
            System.out.println("The following exception occurred while attempting to instantiate the client...");
            System.out.println(ex);
        }
    }

    //	Get a particular bucket from the store
    public static Bucket getBucket(String bucketName) {
        return _storage.get(bucketName);
    }

    //	Get a list of buckets
    public static Page<Bucket> getBuckets() {
        return _storage.list();
    }

    //	Get a particular bucket from the store
    public static Page<Blob> listBlobs(String bucketName) {
        //BlobListOptions options = new BlobListOptions();

        return _storage.get(bucketName).list();
    }

    //	Upload a file to a bucket
    public void uploadFile(String bucketName, String filePath) throws Exception {
        Bucket bucket = getBucket(bucketName);

        if (bucket != null) {

        } else
            throw new Exception("The destination bucket could not be found");
    }

    public static Blob createFolder(String bucketName, String folder) throws Exception {
        Bucket bucket = getBucket(bucketName);

        if (bucket != null)
            return bucket.create(folder + "/", "".getBytes());
        else
            throw new Exception("A folder with the name " + folder + " already exists");
    }

    //	Create a bucket in the store
    public static Bucket createBucket(String bucketName) throws Exception {
        try {
            if (getBucket(getBucketName(bucketName)) != null)
                return _storage.create(BucketInfo.of(getBucketName(bucketName)));
            else
                throw new Exception("A bucket with the name " + bucketName + " already exists");
        } catch (StorageException sEx) {
            if (sEx.getCode() == 403)
                throw new Exception(String.format("A bucket with the name %s already exists", bucketName));
            else
                throw new Exception("An unexpected error occurred while attempting to create a bucket: " + sEx.getMessage());
        }
    }

    //	Get a unique name for the bucket
    private static String getBucketName(String keyName) {
        return BUCKET_PREFIX.concat(keyName);
    }

    public static void main(String... args) throws Exception {
        try {
            Page<Bucket> buckets = getBuckets();
            Page<Blob> blobs = listBlobs("rcp-test-default");

            //	Iterate through the buckets
            for (Bucket bucket : buckets.iterateAll())
                System.out.println(bucket.getName());

            //	Create a new bucket
            System.out.println("Creating a new folder");

            //	Iterate through the buckets
            for (Blob blob : blobs.iterateAll())
                System.out.println(blob.getName());
        } catch (Exception ex) {
            System.out.println("An exception occurred");
            System.out.println(ex);
        }
    }
}
