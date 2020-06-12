package com.openclassrooms.realestatemanager.tools;

import com.openclassrooms.realestatemanager.models.pojo.Photo;
import java.util.Comparator;

public class IdRoomComparator implements Comparator<Photo> {
    @Override
    public int compare(Photo photo1, Photo photo2) {
        return (int) photo1.getIdRoom() - (int) photo2.getIdRoom();
    }
}