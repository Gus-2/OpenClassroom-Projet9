package com.openclassrooms.realestatemanager.ui.realestateform;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.pojo.Photo;
import com.openclassrooms.realestatemanager.models.pojo.Room;

import java.util.HashMap;
import java.util.List;
import butterknife.ButterKnife;

public class AdapterPicturesHouse extends RecyclerView.Adapter<AdapterPicturesHouse.MyViewHolder> {
    private HashMap<Uri, Photo> uriPhotoHashMap;
    private HashMap<Uri, Bitmap> uriBitmapHashMap;
    private List<Uri> listUri;
    private List<Room> listRoom;
    private String[] tabStringRoom;
    private Context context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onDeletePhotoClickListener(int position);
        void onSetAsDefaultPictureListener(int position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPictureHouse;
        ImageView ivSetAsDefaultPicture;
        ImageView ivDeletePhoto;
        AutoCompleteTextView edPhotoDescription;

        public MyViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ivPictureHouse = itemView.findViewById(R.id.iv_picture_house);
            ivSetAsDefaultPicture = itemView.findViewById(R.id.iv_set_as_default_picture);
            ivDeletePhoto = itemView.findViewById(R.id.iv_remove_picture);
            ivSetAsDefaultPicture.setOnClickListener(v -> {
                listener.onSetAsDefaultPictureListener(getAdapterPosition());
            });
            ivDeletePhoto.setOnClickListener(v -> {
                listener.onDeletePhotoClickListener(getAdapterPosition());
            });

            edPhotoDescription = itemView.findViewById(R.id.tv_house_room_picture);
        }
    }

    public AdapterPicturesHouse(List<Uri> uriPhoto, HashMap<Uri, Photo> uriPhotoHashMap, HashMap<Uri, Bitmap> uriBitmapHashMap, List<Room> listRoom, String[] tabStringRoom, Context context, OnItemClickListener mListener) {
        this.listUri = uriPhoto;
        this.uriPhotoHashMap = uriPhotoHashMap;
        this.uriBitmapHashMap = uriBitmapHashMap;
        this.context = context;
        this.tabStringRoom = tabStringRoom;
        this.listRoom = listRoom;
        this.mListener = mListener;
    }

    @Override
    public AdapterPicturesHouse.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_house_picture, parent, false);
        return new MyViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Uri uri = listUri.get(position);
        Bitmap picture = uriBitmapHashMap.get(uri);
        holder.ivPictureHouse.setImageBitmap(picture);
        Photo photo = uriPhotoHashMap.get(uri);

        if(photo.isMainPicture()){
            holder.ivSetAsDefaultPicture.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_crop_original_green_24dp));
        }else{
            holder.ivSetAsDefaultPicture.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_crop_original_white_24dp));
        }


        if(photo.getIdRoom() == -1){
            holder.edPhotoDescription.setText(uriPhotoHashMap.get(uri).getSpecificRoom());
        }else{
            for(Room room : listRoom){
                if(room.getIdRoom() == photo.getIdRoom()){
                    holder.edPhotoDescription.setText(room.getRoomType());
                }
            }
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>( context.getApplicationContext(), R.layout.dropdown_menu_popup_item, tabStringRoom);
        holder.edPhotoDescription.setAdapter(adapter);

        holder.edPhotoDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                uriPhotoHashMap.get(listUri.get(position)).setSpecificRoom(s.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return uriPhotoHashMap.size();
    }
}