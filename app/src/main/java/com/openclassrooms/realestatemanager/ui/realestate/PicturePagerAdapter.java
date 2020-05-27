package com.openclassrooms.realestatemanager.ui.realestate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.pojo.Photo;
import com.openclassrooms.realestatemanager.models.pojo.Room;
import com.openclassrooms.realestatemanager.tools.DataConverter;
import com.openclassrooms.realestatemanager.tools.Utils;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;

public class PicturePagerAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<Photo> listPhoto;
    private HashMap<Long, String> hashMapRoom;
    private int size;

    public PicturePagerAdapter(Context context, ArrayList<Photo> listPhoto, HashMap<Long, String> hashMapRoom){
        this.context = context;
        this.listPhoto = listPhoto;
        this.hashMapRoom = hashMapRoom;
        size =listPhoto.size();
    }
    @Override
    public int getCount() {
        return listPhoto.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView;
        TextView tvDescription;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.slider_item,container,false);

        imageView = itemView.findViewById(R.id.slider_image_view);
        tvDescription = itemView.findViewById(R.id.tv_description_picture);

        Photo photo = listPhoto.get(position);
        imageView.setImageBitmap(Utils.loadImageFromStorage(photo.getPath(), photo.getChildPath()));
        if(hashMapRoom != null){
            if(photo.getIdRoom() == -1){
                tvDescription.setText(photo.getSpecificRoom());
            }else{
                tvDescription.setText(hashMapRoom.get(photo.getIdRoom())  + " " + photo.getNumOrderRoom());
            }
        }else{
            tvDescription.setVisibility(View.GONE);
        }

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
