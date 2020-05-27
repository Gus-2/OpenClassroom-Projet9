package com.openclassrooms.realestatemanager.tools;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.pojo.Photo;
import com.openclassrooms.realestatemanager.models.pojo.Room;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param dollars montant en dollars
     * @return montant en euro
     */
    public static int convertDollarToEuro(int dollars) {
        return (int) Math.round(dollars * 0.812);
    }

    /**
     * Conversion d'un prix d'un bien immobilier (Euro vers Dollars)
     *
     * @param euro montant en euro
     * @return montant en dollars
     */
    public static int convertEuroToDollars(int euro) {
        return (int) Math.round(euro / 0.812);
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @return la date d'aujourd'hui au format demandé
     */
    public static String getTodayDate() {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(new Date());
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param context le context de l'application
     * @return true si la connection wifi est disponible, false si ce n'est pas le cas
     */
    public static Boolean isInternetAvailable(Context context) {
        ConnectivityManager connectionManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectionManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
    }



    public static int getNumOrderRoom(HashMap<Uri, Photo> hashMapRoom, long roomId){
        int i = 0;
        for(Uri uri : hashMapRoom.keySet()){
            if(hashMapRoom.get(uri).getIdRoom() == roomId){
                i++;
            }
        }
        return i;
    }
}
