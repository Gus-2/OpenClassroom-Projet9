package com.openclassrooms.realestatemanager.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DataConverter {

    public static byte[] convertImageToByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream);
        return stream.toByteArray();
    }

    public static Bitmap convertByteArrayToImage(byte[] array){
        return BitmapFactory.decodeByteArray(array, 0, array.length);
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
