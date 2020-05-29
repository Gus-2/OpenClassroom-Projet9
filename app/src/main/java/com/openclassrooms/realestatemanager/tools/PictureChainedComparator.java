package com.openclassrooms.realestatemanager.tools;

import com.openclassrooms.realestatemanager.models.pojo.Photo;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class PictureChainedComparator implements Comparator<Photo> {
 
    private List<Comparator<Photo>> listComparators;
 
    @SafeVarargs
    public PictureChainedComparator(Comparator<Photo>... comparators) {
        this.listComparators = Arrays.asList(comparators);
    }
 
    @Override
    public int compare(Photo photo1, Photo photo2) {
        for (Comparator<Photo> comparator : listComparators) {
            int result = comparator.compare(photo1, photo2);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }
}