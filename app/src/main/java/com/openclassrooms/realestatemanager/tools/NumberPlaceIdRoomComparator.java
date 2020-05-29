package com.openclassrooms.realestatemanager.tools;

import com.openclassrooms.realestatemanager.models.pojo.Photo;

import java.util.Comparator;

public class NumberPlaceIdRoomComparator implements Comparator<Photo> {
 
    @Override
    public int compare(Photo photo1, Photo photo2) {
        return (int) photo1.getNumOrderRoom() - (int) photo2.getNumOrderRoom();
    }
}