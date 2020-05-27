package com.openclassrooms.realestatemanager.tools;

import com.openclassrooms.realestatemanager.models.pojo.Address;
import com.openclassrooms.realestatemanager.models.pojo.HouseType;
import com.openclassrooms.realestatemanager.models.pojo.Photo;
import com.openclassrooms.realestatemanager.models.pojo.RealEstateAgent;
import com.openclassrooms.realestatemanager.models.pojo.Room;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
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

    public static HashMap<Long, ArrayList<Photo>> getPhotoListToHashMap(List<Photo> listPhoto){

        HashMap<Long, ArrayList<Photo>> hashMapPhoto = new HashMap<>();
        if(listPhoto != null){
            for(Photo photo : listPhoto){
                if(hashMapPhoto.get(photo.getIdHouse()) == null){
                    ArrayList<Photo> photos = new ArrayList<>();
                    photos.add(photo);
                    hashMapPhoto.put(photo.getIdHouse(), photos);
                }else{
                    if(photo.isMainPicture()){
                        hashMapPhoto.get(photo.getIdHouse()).add(0, photo);
                    }else{
                        hashMapPhoto.get(photo.getIdHouse()).add(photo);
                    }
                }
            }
        }
        return hashMapPhoto;
    }

    public static String[] listToTableRoom(List<Room> listRoom){
        String[] listRoomString = new String[listRoom.size()];
        for(int i = 0; i < listRoom.size(); i++){
            listRoomString[i] = listRoom.get(i).getRoomType();
        }
        return listRoomString;
    }

    public static HashMap<String, Long> listRoom(List<Room> listRoom){
        HashMap<String, Long> listRoomId = new HashMap<>();
        for(Room room : listRoom){
            listRoomId.put(room.getRoomType(), room.getIdRoom());
        }
        return listRoomId;
    }

    public static long getRoomId(HashMap<String, Long> listRoomId, String room){
        for(String key : listRoomId.keySet()){
            if(room.equals(key)) return listRoomId.get(key);
        }
        return -1;
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

    public static HashMap<Long, String> listRoomToHashMap(List<Room> listRoom){
        HashMap<Long, String> hashMapIdRoom = new HashMap<>();
        for(Room room : listRoom){
            hashMapIdRoom.put(room.getIdRoom(), room.getRoomType());
        }
        return hashMapIdRoom;
    }

}
