package com.openclassrooms.realestatemanager.tools;

import android.content.Context;

import com.openclassrooms.realestatemanager.models.pojo.Address;
import com.openclassrooms.realestatemanager.models.pojo.House;
import com.openclassrooms.realestatemanager.models.pojo.HousePointOfInterest;
import com.openclassrooms.realestatemanager.models.pojo.Photo;
import com.openclassrooms.realestatemanager.models.pojo.PointOfInterest;
import com.openclassrooms.realestatemanager.models.pojo.TypePointOfInterest;
import com.openclassrooms.realestatemanager.ui.realestate.SearchDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchUtils {

    public static final String STATION_KEY = "TRAIN_STATION";
    public static final String PARK_KEY = "PARK";
    public static final String SHOP_KEY = "SUPERMARKET";
    public static final String SCHOOL_KEY = "SCHOOL";

    @SuppressWarnings("all")
    public static List<House> getListHouseFiltered(List<House> listHouses, HashMap<Long, Address> hashMapAddress,
                                                   HashMap<Long, ArrayList<Photo>> hashMapPhoto, long houseType, long minSurface, long maxSurface, long minPrice, long maxPrice,
                                                   long availabilityDate, String district, long numberPhoto, HashMap<String, Boolean> nearbyTypesHashMap, Context context,HashMap<Long, List<HousePointOfInterest>> hashMapHouseTypePointOfInterest, HashMap<Long, PointOfInterest> hashMapPointOfInterest,
                                                   HashMap<Long, TypePointOfInterest> hashMapTypePointOfInterest){
        List<House> listHouseFiltered = new ArrayList<>(listHouses);

        if(houseType == -1 && minSurface == 0 && maxSurface == -1 && maxPrice == -1
                && minPrice == 0 && availabilityDate == 0 && district.equals("") && numberPhoto == -1 && !nearbyTypesHashMap.get(SearchDialog.PARK)
        && !nearbyTypesHashMap.get(SearchDialog.SCHOOL) && !nearbyTypesHashMap.get(SearchDialog.STATION) && !nearbyTypesHashMap.get(SearchDialog.SHOP)){
            return listHouseFiltered;
        }

        if(houseType != -1){
            for(House house : listHouseFiltered)
                if(house.getIdHouseType() != houseType)
                    listHouseFiltered.remove(house);
        }

        if(maxSurface != -1){
            for(House house : listHouseFiltered)
                if(house.getSurface() > maxSurface || house.getSurface() < minSurface)
                    listHouseFiltered.remove(house);
        }

        if(maxPrice != -1){
            for(House house : listHouseFiltered)
                if(house.getPrice() > maxPrice || house.getPrice() > minPrice)
                    listHouseFiltered.remove(house);
        }

        if(availabilityDate > 0){
            for(House house : listHouseFiltered)
                if(house.getAvailableDate() < availabilityDate)
                    listHouseFiltered.remove(house);
        }

        if(!district.equals("")){
            for(House house : listHouseFiltered)
                if(!hashMapAddress.get(house.getIdAddress()).getDistrict().equals(district))
                    listHouseFiltered.remove(house);
        }

        if(numberPhoto > 0){
            for(House house : listHouseFiltered)
                if(hashMapPhoto.get(house.getIdHouse()) != null && hashMapPhoto.get(house.getIdHouse()).size() < numberPhoto)
                    listHouseFiltered.add(house);
        }

        ArrayList<House> houseToRemove = new ArrayList<>();
        boolean containPark = false;
        if(nearbyTypesHashMap.get(SearchDialog.PARK)){
            for(House house : listHouseFiltered){
                if(hashMapHouseTypePointOfInterest.get(house.getIdHouse()) != null){
                    for(HousePointOfInterest housePointOfInterest : hashMapHouseTypePointOfInterest.get(house.getIdHouse())){
                        long typePointOfInterest = hashMapPointOfInterest.get(housePointOfInterest.getIdPointOfInterest()).getTypePointOfInterest();
                        if(hashMapTypePointOfInterest.get(typePointOfInterest).getTypePointOfInterest().equals(PARK_KEY)){
                            containPark = true;
                            break;
                        }
                    }
                    if(!containPark){
                        houseToRemove.add(house);
                        containPark = false;
                    }
                }
            }
        }

        boolean containSchool = false;
        if(nearbyTypesHashMap.get(SearchDialog.SCHOOL)){
            for(House house : listHouseFiltered){
                if(hashMapHouseTypePointOfInterest.get(house.getIdHouse()) != null){
                    for(HousePointOfInterest housePointOfInterest : hashMapHouseTypePointOfInterest.get(house.getIdHouse())){
                        long typePointOfInterest = hashMapPointOfInterest.get(housePointOfInterest.getIdPointOfInterest()).getTypePointOfInterest();
                        String stringTypePointOfInterest = hashMapTypePointOfInterest.get(typePointOfInterest).getTypePointOfInterest();
                        if(stringTypePointOfInterest.equals(SCHOOL_KEY)){
                            containSchool= true;
                            break;
                        }
                    }
                    if(!containSchool){
                        houseToRemove.add(house);
                    }
                    containSchool = false;
                }
            }
        }

        boolean containStation = false;
        if(nearbyTypesHashMap.get(SearchDialog.STATION)){
            for(House house : listHouseFiltered){
                if(hashMapHouseTypePointOfInterest.get(house.getIdHouse()) != null) {
                    for (HousePointOfInterest housePointOfInterest : hashMapHouseTypePointOfInterest.get(house.getIdHouse())) {
                        long typePointOfInterest = hashMapPointOfInterest.get(housePointOfInterest.getIdPointOfInterest()).getTypePointOfInterest();
                        if (hashMapTypePointOfInterest.get(typePointOfInterest).getTypePointOfInterest().equals(STATION_KEY)) {
                            containStation = true;
                            break;
                        }
                    }
                    if (!containStation) {
                        houseToRemove.add(house);
                    }
                    containStation = false;
                }
            }
        }

        boolean containShop = false;
        if(nearbyTypesHashMap.get(SearchDialog.SHOP)){
            for(House house : listHouseFiltered){
                if(hashMapHouseTypePointOfInterest.get(house.getIdHouse()) != null) {
                    for(HousePointOfInterest housePointOfInterest : hashMapHouseTypePointOfInterest.get(house.getIdHouse())){
                        long typePointOfInterest = hashMapPointOfInterest.get(housePointOfInterest.getIdPointOfInterest()).getTypePointOfInterest();
                        if(hashMapTypePointOfInterest.get(typePointOfInterest).getTypePointOfInterest().equals(SHOP_KEY)){
                            containShop = true;
                            break;
                        }
                    }
                    if(!containShop){
                        houseToRemove.add(house);
                    }
                    containShop = false;
                }
            }
        }

        for(House house : houseToRemove){
            listHouseFiltered.remove(house);
        }

        return listHouseFiltered;
    }

}
