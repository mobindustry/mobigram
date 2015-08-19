package net.mobindustry.mobigram.core.service;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import net.mobindustry.mobigram.core.ApiHelper;
import net.mobindustry.mobigram.model.holder.ListFoldersHolder;
import net.mobindustry.mobigram.utils.Const;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SendGif extends Service {
    private long id;

    private ExecutorService executorService;

    public void onCreate() {
        super.onCreate();
        Log.e("Log", "Service Gif start");
        id = ListFoldersHolder.getChatID();
        executorService = Executors.newFixedThreadPool(1);
    }

    public void onDestroy() {
        Log.e("Log", "Service Gif destroy");
        ListFoldersHolder.setChatID(0);
        ListFoldersHolder.setListGif(null);
        ListFoldersHolder.setListImages(null);
        ListFoldersHolder.setListForSending(null);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        CreateGifAndImages myRun = new CreateGifAndImages();
        executorService.execute(myRun);
        return super.onStartCommand(intent, flags, startId);
    }

    public String downloadFromUrlGif(final String DownloadUrl) {
        String link = "";
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String fileName = "GIF_" + dateFormat.format(new Date()) + ".gif";

            File dir = new File(Const.PATH_TO_SAVE_GIFS);
            if (dir.exists() == false) {
                dir.mkdirs();
            }

            URL url = new URL(DownloadUrl); //you can write here any link
            File file = new File(dir, fileName);

            long startTime = System.currentTimeMillis();

           /* Open a connection to that URL. */
            URLConnection ucon = url.openConnection();

           /*
            * Define InputStreams to read from the URLConnection.
            */
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

           /*
            * Read bytes to the Buffer until there is nothing more to read(-1).
            */
            ByteArrayBuffer baf = new ByteArrayBuffer(5000);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }


           /* Convert the Bytes read to a String. */
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baf.toByteArray());
            fos.flush();
            fos.close();
            link = file.getAbsolutePath();


        } catch (IOException e) {
            Log.e("DownloadManager", "Error: " + e);
        }

        return link;
    }

    public String downloadFromUrlImages(final String DownloadUrl) {
        String link = "";
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String fileName = "IMG_" + dateFormat.format(new Date()) + ".jpg";

            File dir = new File(Const.PATH_TO_SAVE_IMAGES);
            if (dir.exists() == false) {
                dir.mkdirs();
            }

            URL url = new URL(DownloadUrl); //you can write here any link
            File file = new File(dir, fileName);

            long startTime = System.currentTimeMillis();

           /* Open a connection to that URL. */
            URLConnection ucon = url.openConnection();

           /*
            * Define InputStreams to read from the URLConnection.
            */
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

           /*
            * Read bytes to the Buffer until there is nothing more to read(-1).
            */
            ByteArrayBuffer baf = new ByteArrayBuffer(5000);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }


           /* Convert the Bytes read to a String. */
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baf.toByteArray());
            fos.flush();
            fos.close();
            if (file.canRead()) {
                Log.e("Log", "CAN READ ");
            }
            link = file.getAbsolutePath();

        } catch (IOException e) {
            Log.e("DownloadManager", "Error: " + e);
        }

        return link;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class CreateGifAndImages implements Runnable {

        public void run() {
            if (ListFoldersHolder.getListGif() != null) {
                for (int i = 0; i < ListFoldersHolder.getListGif().size(); i++) {
                    String link = downloadFromUrlGif(ListFoldersHolder.getListGif().get(i));
                    Log.e("Log", "Link Gif " + link);
                    ApiHelper.sendDocumentMessage(id, link);
                }
            }
            if (ListFoldersHolder.getListImages() != null) {
                for (int i = 0; i < ListFoldersHolder.getListImages().size(); i++) {
                    String link = downloadFromUrlImages(ListFoldersHolder.getListImages().get(i));
                    Log.e("Log", "Link send " + link);
                    ApiHelper.sendPhotoMessage(id, link);
                }
            }
            stopSelf();
        }
    }
}
