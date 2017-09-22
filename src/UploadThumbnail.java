/*
 * Copyright (c) 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.youtube.YouTube.Thumbnails.Set;
import com.google.api.services.youtube.model.ThumbnailSetResponse;
import com.google.common.collect.Lists;

import java.io.*;

/**
 * This sample uses MediaHttpUploader to upload an image and then calls the
 * API's youtube.thumbnails.set method to set the image as the custom thumbnail
 * for a video.
 *
 * @author Ibrahim Ulukaya
 */
public class UploadThumbnail {

    /**
     * Define a global variable that specifies the MIME type of the image
     * being uploaded.
     */
    private static final String IMAGE_FILE_FORMAT = "image/jpeg";

    /**
     * Prompt the user to specify a video ID and the path for a thumbnail
     * image. Then call the API to set the image as the thumbnail for the video.
     *
     * @param videoId id of YouTube video
     * @param filePath String path to jpeg file to upload
     */
    public static void run(String videoName, String videoId, String filePath) {
        try {

            File imageFile = new File(filePath);

            // Create an object that contains the thumbnail image file's
            // contents.
            InputStreamContent mediaContent = new InputStreamContent(
                    IMAGE_FILE_FORMAT, new BufferedInputStream(new FileInputStream(imageFile)));
            mediaContent.setLength(imageFile.length());

            // Create an API request that specifies that the mediaContent
            // object is the thumbnail of the specified video.
            Set thumbnailSet = Main.youtube.thumbnails().set(videoId, mediaContent);

            // Set the upload type and add an event listener.
            MediaHttpUploader uploader = thumbnailSet.getMediaHttpUploader();

            // Indicate whether direct media upload is enabled. A value of
            // "True" indicates that direct media upload is enabled and that
            // the entire media content will be uploaded in a single request.
            // A value of "False," which is the default, indicates that the
            // request will use the resumable media upload protocol, which
            // supports the ability to resume an upload operation after a
            // network interruption or other transmission failure, saving
            // time and bandwidth in the event of network failures.
            uploader.setDirectUploadEnabled(false);

            // Set the upload state for the thumbnail image.
            MediaHttpUploaderProgressListener progressListener = new MediaHttpUploaderProgressListener() {
                @Override
                public void progressChanged(MediaHttpUploader uploader) throws IOException {
                }
            };
            uploader.setProgressListener(progressListener);

            // Upload the image and set it as the specified video's thumbnail.
            ThumbnailSetResponse setResponse = thumbnailSet.execute();

            // Print the URL for the updated video's thumbnail image.
            System.out.println("Upload Completed: " + videoName);

        } catch (GoogleJsonResponseException e) {
            System.out.println("Fail");
            System.err.println(e);
            e.printStackTrace();

        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /*
     * Prompts the user to enter a YouTube video ID and return the user input.
     */
    private static String getVideoIdFromUser() throws IOException {

        String inputVideoId = "";

        System.out.print("Please enter a video Id to update: ");
        BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
        inputVideoId = bReader.readLine();

        if (inputVideoId.length() < 1) {
            // Exit if the user does not specify a video ID.
            System.out.print("Video Id can't be empty!");
            System.exit(1);
        }

        return inputVideoId;
    }

    /*
     * Prompt the user to enter the path for the thumbnail image being uploaded.
     */
    private static File getImageFromUser() throws IOException {

        String path = "";

        System.out.print("Please enter the path of the image file to upload: ");
        BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
        path = bReader.readLine();

        if (path.length() < 1) {
            // Exit if the user does not provide a path to the image file.
            System.out.print("Path can not be empty!");
            System.exit(1);
        }

        return new File(path);
    }
}
