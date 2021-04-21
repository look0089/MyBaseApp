package com.wf;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by abhinav on 6/11/18.
 */

public class AssetFileManager {
    private Context context;
    private static String filesAssests[] = {"b1.bin", "f160tm.bin", "p0.bin",
            "p1tc.bin", "p2tc.bin", "p3tc.bin", "p4tc.bin", "q103tm.bin", "q205tm.bin", "s11tm.bin", "fmgttm.bin",
            "s510tc.bin", "s511tc.bin", "s520tc.bin", "s521tc.bin"};

    public AssetFileManager(Context context) {
        this.context = context;
    }

    public boolean checkFilesExist() {
        for (int i = 0; i < filesAssests.length; i++) {
            File file = new File(getFilePath() + "/" + filesAssests[i]);
            if (!file.exists()) {
                return false;
            }
        }
        return true;
    }

    public void copyFilesFromAssets() {
        AssetManager assetManager = context.getAssets();
        for (String filesAssest : filesAssests) {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            File file = null;
            if (filesAssest.equals("db.dat") || filesAssest.equals("db.dat.dat")) {
                file = new File(getFilePath() + "/wffrdb", filesAssest);
            } else {
                file = new File(getFilePath(), filesAssest);
            }
            try {
                if (file.exists() && file.length() > 0) {
                    continue;
                } else {
                    file.createNewFile();
                    Log.d("JNI File", "Creating new file " + filesAssest);
                }
                inputStream = assetManager.open(filesAssest);
                outputStream = new FileOutputStream(file);

                copyFile(inputStream, outputStream);

                inputStream.close();
                inputStream = null;

                outputStream.flush();
                outputStream.close();
                outputStream = null;

            } catch (Exception e) {
                Log.e("JNI ", "Error while copying filesAssets getAssets()");
                e.printStackTrace();
            }
        }
    }

    private void copyFile(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[10 * 1024];
        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }
    }

    public static String wffrBasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/dualcam";
    public static String wffrdbPath = wffrBasePath + "/wffrdb";
    public static String wffrdbpcPath = wffrBasePath + "/wffrdbpc";
    public static String wffrIDPhotoPath = wffrBasePath + "/IdPhoto";
    public static String wffrEXPath = wffrBasePath + "/wffrdbExtract";

    public String getFilePath() {
        String filePath = null;
        try {
            String basePath = wffrBasePath;
            filePath = wffrBasePath;
            File dir = new File(basePath);
            if (!dir.isDirectory()) {
                dir.mkdir();
            }
            String databasePath = wffrdbPath;
            File databaseDir = new File(databasePath);
            if (!databaseDir.isDirectory()) {
                databaseDir.mkdir();
            }
            String idPhoto = wffrIDPhotoPath;
            File idPhotoDir = new File(idPhoto);
            if (!idPhotoDir.isDirectory()) {
                idPhotoDir.mkdir();
            }
            String exPath = wffrEXPath;
            File ex = new File(exPath);
            if (!ex.isDirectory()) {
                ex.mkdir();
            }
            Log.d("JNI File Path", basePath);
        } catch (Exception e) {
            Log.e("JNI ", "Error while getting file path");
            e.printStackTrace();
            return null;
        }
        return filePath;
    }
}
