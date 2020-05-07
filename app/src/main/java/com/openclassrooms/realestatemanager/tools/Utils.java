package com.openclassrooms.realestatemanager.tools;

import android.content.Context;
import android.net.ConnectivityManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars montant en dollars
     * @return montant en euro
     */
    public static int convertDollarToEuro(int dollars){
        return (int) Math.round(dollars * 0.812);
    }

    /**
     * Conversion d'un prix d'un bien immobilier (Euro vers Dollars)
     * @param euro montant en euro
     * @return montant en dollars
     */
    public static int convertEuroToDollars(int euro){
        return (int) Math.round(euro / 0.812);
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return la date d'aujourd'hui au format demandé
     */
    public static String getTodayDate(){
        return new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(new Date());
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context le context de l'application
     * @return true si la connection wifi est disponible, false si ce n'est pas le cas
     */
    public static Boolean isInternetAvailable(Context context){
        ConnectivityManager connectionManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectionManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
    }
}
