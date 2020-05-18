package com.openclassrooms.realestatemanager.ui.realestateform;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.pojo.Photo;
import com.openclassrooms.realestatemanager.models.pojo.Room;
import com.openclassrooms.realestatemanager.tools.TypeConverter;
import com.openclassrooms.realestatemanager.tools.Utils;

import java.util.HashMap;
import java.util.List;
import butterknife.ButterKnife;

public class AdapterPicturesHouse extends RecyclerView.Adapter<AdapterPicturesHouse.MyViewHolder> {
    private HashMap<Uri, Photo> listPhotoHouse;
    private List<Uri> listUri;
    private List<Room> listRoom;
    private String[] tabStringRoom;
    private Context context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void OnSetAsDefaultPictureClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mListener = onItemClickListener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPictureHouse;

        ImageView ivSetAsDefaultPicture;

        AutoCompleteTextView edPhotoDescription;


        public MyViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ivPictureHouse = itemView.findViewById(R.id.iv_picture_house);
            ivSetAsDefaultPicture = itemView.findViewById(R.id.iv_set_as_default_picture);
            ivSetAsDefaultPicture.setOnClickListener(v -> {
                if(listener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.OnSetAsDefaultPictureClick(position);
                    }
                }
            });
            edPhotoDescription = itemView.findViewById(R.id.tv_house_room_picture);
        }
    }

    public AdapterPicturesHouse(List<Uri> uriPhoto, HashMap<Uri, Photo> listPhotoHouse, List<Room> listRoom, String[] tabStringRoom, Context context) {
        this.listUri = uriPhoto;
        this.listPhotoHouse = listPhotoHouse;
        this.context = context;
        this.tabStringRoom = tabStringRoom;
        this.listRoom = listRoom;
    }

    @Override
    public AdapterPicturesHouse.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.i_picture_property, parent, false);
        return new MyViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Bitmap bitmap = null;
        Uri uri = listUri.get(position);
        try {
            if(Build.VERSION.SDK_INT < 28) {
                bitmap = MediaStore.Images.Media.getBitmap(
                        this.context.getContentResolver(),
                        uri
                );
                holder.ivPictureHouse.setImageBitmap(bitmap);
            } else {
                ImageDecoder.Source source = ImageDecoder.createSource(this.context.getContentResolver(), uri);
                bitmap = ImageDecoder.decodeBitmap(source);
                holder.ivPictureHouse.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(listPhotoHouse.get(uri).isMainPicture()){
            holder.ivSetAsDefaultPicture.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_crop_original_green_24dp));
        }else{
            holder.ivSetAsDefaultPicture.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_crop_original_white_24dp));
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
                for(Room room : listRoom){
                    if(room.getRoomType().equals(s.toString())){
                        int numOrderRoom = Utils.getNumOrderRoom(listPhotoHouse, room.getIdRoom());
                        listPhotoHouse.get(listUri.get(position)).setNumOrderRoom(numOrderRoom+1);
                        listPhotoHouse.get(listUri.get(position)).setIdRoom(room.getIdRoom());
                    }
                }
                if(listPhotoHouse.get(listUri.get(position)).getIdRoom() == -1){
                    listPhotoHouse.get(listUri.get(position)).setSpecificRoom(s.toString());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPhotoHouse.size();
    }
}