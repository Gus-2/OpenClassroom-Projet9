package com.openclassrooms.realestatemanager.tools;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class ImageUtils {

    public static final String HOUSE_PICTURES = "house_pictures";
    public static final String MAP_IMAGE = "map_image";
    public static final String NAME_HOUSE_PICTURES_DIRECTORY = "HOUSES_PICTURES";
    public static final String NAME_HOUSE_MAP_PICTURE_DIRECTORY = "MAP_IMAGE";


    public static String saveToInternalStorage(String childPath, Bitmap bitmapImage, Context context, String pictureType){
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        FileOutputStream fos = null;
        File directory = null;
        if(pictureType.equals(HOUSE_PICTURES)) directory = cw.getDir(NAME_HOUSE_PICTURES_DIRECTORY, Context.MODE_PRIVATE);
        else if(pictureType.equals(MAP_IMAGE)) directory = cw.getDir(NAME_HOUSE_MAP_PICTURE_DIRECTORY, Context.MODE_PRIVATE);

        File mypath = new File(directory,childPath);
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(fos != null) fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(directory != null)
            return directory.getAbsolutePath();
        else
            return null;
    }

    public static Bitmap loadImageFromStorage(String path, String childPath) {
        try {
            File f = new File(path, childPath);
            return BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String saveVideoToInternalStorage (Uri filePath, Context context) {
        File newFile;
        try {
            File currentFile = new File(filePath.getPath());
            String fileName = currentFile.getName() + ".mp4";
            ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
            File directory = cw.getDir("videoDir", Context.MODE_PRIVATE);
            newFile = new File(directory, fileName);
            InputStream in = context.getContentResolver().openInputStream(filePath);
            OutputStream out = new FileOutputStream(newFile);

            byte[] buf = new byte[1024];
            int len;

            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            in.close();
            out.close();
            return newFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void deleteFile(String path){
        File fDelete = new File(path);
        if (fDelete.exists()) {
            fDelete.delete();
        }
    }

    public static void deleteFileFromStorageChildPathParentPath(String path, String childPath) {
        File f = new File(path, childPath);
        if(f.exists()){
            f.delete();
        }
    }

    public static File createPhotoFile(Context context){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try{
            image = File.createTempFile(imageFileName, ".jpg", storageDir);

        } catch (IOException e){
            e.printStackTrace();
        }
        return image;
    }

    public static Bitmap convertUriToBitmap(Uri uri, Context context){
        Bitmap bitmap = null;
        try{
            if(Build.VERSION.SDK_INT < 28) {
                bitmap = MediaStore.Images.Media.getBitmap(
                        context.getContentResolver(),
                        uri
                );
            } else {
                ImageDecoder.Source source = ImageDecoder.createSource(context.getContentResolver(), uri);
                bitmap = ImageDecoder.decodeBitmap(source);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return bitmap;
    }
}
