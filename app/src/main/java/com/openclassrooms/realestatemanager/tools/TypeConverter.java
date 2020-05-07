package com.openclassrooms.realestatemanager.tools;

import com.openclassrooms.realestatemanager.models.pojo.Address;
import com.openclassrooms.realestatemanager.models.pojo.HouseType;
import com.openclassrooms.realestatemanager.models.pojo.Photo;
import com.openclassrooms.realestatemanager.models.pojo.RealEstateAgent;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TypeConverter {

    public static String[] houseTypeToStringArray(List<HouseType> listHouseType){
        String[] houseTypeStringArray = new String[listHouseType.size()];
        for(int i = 0; i < listHouseType.size(); i++){
            houseTypeStringArray[i] = listHouseType.get(i).getHouseType();
        }
        return houseTypeStringArray;
    }

    public static String[] realEstateAgentToStringArray(List<RealEstateAgent> listRealEstateAgent){
        String[] realEstateAgentStringArray = new String[listRealEstateAgent.size()];
        for(int i = 0; i < listRealEstateAgent.size(); i++){
            realEstateAgentStringArray[i] = listRealEstateAgent.get(i).getName() + " " + listRealEstateAgent.get(i).getFirstname();
        }
        return realEstateAgentStringArray;
    }

    public static HashMap<Long, HouseType> convertHouseTypeListToHashMap(List<HouseType> listHouseType){
        HashMap<Long, HouseType> hashMapHouseType = new HashMap<>();
        for(HouseType houseType : listHouseType){
            hashMapHouseType.put(houseType.getIdHouseType(), houseType);
        }
        return hashMapHouseType;
    }

    public static HashMap<Long, Address> convertAddressListToHashMap(List<Address> listAddress){
        HashMap<Long, Address> hashMapAddress = new HashMap<>();
        if(listAddress!= null){
            for(Address address : listAddress){
                hashMapAddress.put(address.getIdAddress(), address);
            }
        }
        return hashMapAddress;
    }

    public static HashMap<Long, List<Photo>> convertPhotoListToHashMap(List<Photo> listPhoto){
        HashMap<Long, List<Photo>> hashMapPhoto = new HashMap<>();
        if(listPhoto!= null){
            for(Photo photo : listPhoto){
                if(hashMapPhoto.get(photo.getIdHouse()) == null){
                    List<Photo> photos = new ArrayList<>();
                    photos.add(photo);
                    hashMapPhoto.put(photo.getIdHouse(), photos);
                }else{
                    hashMapPhoto.get(photo.getIdHouse()).add(photo);
                }
            }
        }
        return hashMapPhoto;
    }


    public static String convertDoubleToStringFormat(double price){
        NumberFormat nf = DecimalFormat.getInstance(Locale.ENGLISH);
        DecimalFormat decimalFormatter = (DecimalFormat) nf;
        decimalFormatter.applyPattern("#,###,###.##");
        return decimalFormatter.format(price);
    }

    public static String convertDateToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    public static long getHouseTypeId(List<HouseType> houseTypeList, String houseType){
        for(HouseType houseTypeItem : houseTypeList){
            if(houseTypeItem.getHouseType().equals(houseType)) return houseTypeItem.getIdHouseType();
        }
        return -1;
    }

    public static long getRealEstateAgentId(List<RealEstateAgent> realEstateAgentList, String realEstateAgent){
        for(RealEstateAgent realEstateAgentItem : realEstateAgentList){
            if((realEstateAgentItem.getName() + " " + realEstateAgentItem.getFirstname()).equals(realEstateAgent)) return realEstateAgentItem.getIdRealEstateAgent();
        }
        return -1;
    }

    public static String convertPlaceTypeString(String placeType){
        String[] transformStringTable = placeType.split("_");
        String value = "";
        for(int i = 0; i < transformStringTable.length; i++){
            value += transformStringTable[i].substring(0, 1).toUpperCase() + transformStringTable[i].substring(1).toLowerCase();
            value += " ";
        }
        return value;
    }
}