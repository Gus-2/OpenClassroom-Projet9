package com.openclassrooms.realestatemanager.tools;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PictureDownloader {

    public static final String HOUSE_PICTURES = "house_pictures";
    public static final String MAP_IMAGE = "map_image";

    public static String saveToInternalStorage(String childPath, Bitmap bitmapImage, Context context, String pictureType){
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        FileOutputStream fos = null;
        File directory = null;
        if(pictureType.equals(HOUSE_PICTURES)) directory = cw.getDir("HOUSES_PICTURES", Context.MODE_PRIVATE);
        else if(pictureType.equals(MAP_IMAGE)) directory = cw.getDir("MAP_IMAGE", Context.MODE_PRIVATE);

        File mypath=new File(directory,childPath);
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public static Bitmap loadImageFromStorage(String path, String childPath) {
        try {
            File f=new File(path, childPath);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
