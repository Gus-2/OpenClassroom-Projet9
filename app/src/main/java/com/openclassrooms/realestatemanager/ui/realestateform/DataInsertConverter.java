package com.openclassrooms.realestatemanager.ui.realestateform;

public class DataInsertConverter {

    public static String getStringFromEditTextString(String string){
        if(string.equals("Undefined")){
            return "";
        }else if(string.equals("")){
            return "";
        }else{
            return string;
        }
    }

    public static String getHousePrice(String string){
        if(string.equals("NS")){
            return "-1";
        }else if(string.equals("")){
            return "-1";
        }else{
            return string;
        }
    }

    public static String getDescription(String string){
        if(string.equals("Description Unspecified")){
            return "";
        }else if(string.equals("")){
            return "";
        }else{
            return string;
        }
    }

}
