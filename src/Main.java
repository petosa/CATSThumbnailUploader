import com.google.api.client.auth.oauth2.Credential;
import com.google.common.collect.Lists;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.api.services.youtube.YouTube;

import javax.swing.*;

/**
 * Created by Nick on 5/13/2016.
 */
public class Main {

    public static YouTube youtube = null;
    public static CountDownLatch cdl = null;

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        disableLogging();
        System.out.println("Checking authentication...");
        authorization();
        List<Video> arr = MyUploads.run();
        HashMap<String, Video> videoHash = new HashMap<>();
        for(Video v : arr){
            videoHash.put(v.getTitle(), v);
        }
        System.out.println("\nNow you must select the source folder for your image(s).");
        System.out.println("I will find all jpeg files in this folder and all its subfolders.");
        String folder = getFolder();
        if(folder == null) {
            System.out.println("\nNo selection made. Quitting.");
            System.exit(0);
        }
        System.out.println(folder);
        List<File> fileList = getImagePaths(folder);
        HashMap<String, String> imageHash = new HashMap<>();
        for(File file : fileList) {
            String nameNoExt = getImageName(file.toString());
            imageHash.put(nameNoExt, file.toString());
        }
        Set<String> crossover = imageHash.keySet();
        crossover.retainAll(videoHash.keySet());
        HashMap<Video, String> thumbHash = new HashMap<>();
        for(String s : crossover) {
            thumbHash.put(videoHash.get(s), imageHash.get(s));
        }
        System.out.println();
        if(thumbHash.keySet().size() == 0) {
            System.out.println("No matches found! Quitting.");
            System.exit(0);
        }
        String decision = "";
        while(!decision.equalsIgnoreCase("y") && !decision.equalsIgnoreCase("n")) {
            System.out.println(thumbHash.keySet().size() + " thumbnails paired! List them? (y/n)");
            decision = in.nextLine();
        }
        if(decision.equalsIgnoreCase("y")) {
            System.out.println("Thumbnails detected for these videos:");
            for(Video v : thumbHash.keySet()) {
                System.out.println(v);
            }
            System.out.println();
        }
        System.out.println("Submit any input to begin thumbnail upload.");
        in.next();
        System.out.println("Uploading thumbnails...");
        for(Video v : thumbHash.keySet()) {
            UploadThumbnail.run(v.getTitle(), v.getId(), thumbHash.get(v));
        }

        System.out.println("\n\n=============================\n" +
                "UPLOADS COMPLETED. YOU MAY CLOSE THIS WINDOW.");
        System.out.println("Changes may take a few minutes to propagate.");
    }

    private static void authorization() {
        //Attempt oauth
        List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube");
        try {
            Credential c = Auth.authorize(scopes, "uploadthumbnail");
            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, c).setApplicationName(
                    "CATS-Thumbnail-Uploader").build();
        } catch (IOException e) {
            System.out.println("Could not authorize, access denied. Quitting.");
            System.exit(0);
        }
        System.out.println("Success!");
    }


    private static void disableLogging() {
        //Disable jetty logging
        org.mortbay.log.Log.setLog(null);
        //Disable Google logging
        Logger logger = Logger.getLogger("com.google");
        logger.setLevel(Level.SEVERE);
    }

    private static String getFolder() {
        JFrame frame = new JFrame("");
        FolderChooser panel = new FolderChooser();
        cdl = new CountDownLatch(1);
        panel.show();
        return panel.getResult();
    }

    private static List<File> getImagePaths(String p) {
        ArrayList<File> paths = new ArrayList<>();
        listf(p, paths);
        return paths;
    }

    private static String getImageName(String p) {
        int i = p.lastIndexOf("\\");
        if(i == -1)
            return p;
        else
            return p.substring(i + 1, p.length() - 4);
    }

    private static void listf(String directoryName, ArrayList<File> files) {
        File directory = new File(directoryName);
        // get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                String f = file.toString();
                String fileType = f.substring(f.length() - 4, f.length());
                if (fileType.equalsIgnoreCase(".jpg") || fileType.equalsIgnoreCase(".JPG")){
                    files.add(file);
                }
            } else if (file.isDirectory()) {
                listf(file.getAbsolutePath(), files);
            }
        }
    }

}
