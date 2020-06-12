package com.openclassrooms.realestatemanager.ui.realestateedit;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ItemHousePictureBinding;
import com.openclassrooms.realestatemanager.models.pojo.Photo;
import com.openclassrooms.realestatemanager.models.pojo.Room;
import com.openclassrooms.realestatemanager.tools.ImageUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdapterPicturesHouseEdit extends RecyclerView.Adapter<AdapterPicturesHouseEdit.MyViewHolder> {

    private Context context;
    private PhotoOnClickListener photoOnClickListener;
    private List<Photo> listPhoto;
    private List<Room> listRoom;
    private String[] tabRooms;


    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPictureHouse;
        ImageView ivSetAsDefaultPicture;
        ImageView ivRemovePicture;
        AutoCompleteTextView edPhotoDescription;

        MyViewHolder(ItemHousePictureBinding binding, PhotoOnClickListener photoOnClickListener) {
            super(binding.getRoot());
            ivPictureHouse = binding.ivPictureHouse;
            ivSetAsDefaultPicture = binding.ivSetAsDefaultPicture;
            ivRemovePicture = binding.ivRemovePicture;
            edPhotoDescription = binding.tvHouseRoomPicture;

            ivRemovePicture.setOnClickListener(v -> photoOnClickListener.onDeletePhotoClickListener(getAdapterPosition()));
            ivSetAsDefaultPicture.setOnClickListener(v -> photoOnClickListener.onSetAsDefaultPictureListener(getAdapterPosition()));
        }
    }

    AdapterPicturesHouseEdit(Context context, List<Photo> listPhoto, List<Room> listRoom, String[] tabRooms, PhotoOnClickListener photoOnClickListener) {
        this.context = context;
        this.photoOnClickListener = photoOnClickListener;
        this.listPhoto = listPhoto;
        this.listRoom = listRoom;
        this.tabRooms = tabRooms;
    }

    @NotNull
    @Override
    public AdapterPicturesHouseEdit.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ItemHousePictureBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), photoOnClickListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Photo photo = listPhoto.get(position);
        Bitmap picture = ImageUtils.loadImageFromStorage(photo.getPath(), photo.getChildPath());

        holder.ivPictureHouse.setImageBitmap(picture);

        if(photo.isMainPicture()){
            holder.ivSetAsDefaultPicture.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_crop_original_green_24dp));
        }else{
            holder.ivSetAsDefaultPicture.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_crop_original_white_24dp));
        }

        if(photo.getIdRoom() == -1){
            holder.edPhotoDescription.setText(photo.getSpecificRoom());
        }else{
            for(Room room : listRoom){
                if(room.getIdRoom() == photo.getIdRoom()){
                    holder.edPhotoDescription.setText(room.getRoomType());
                }
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context.getApplicationContext(), R.layout.dropdown_menu_popup_item, tabRooms);
        holder.edPhotoDescription.setAdapter(adapter);

        holder.edPhotoDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                listPhoto.get(position).setSpecificRoom(s.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPhoto.size();
    }

    public interface PhotoOnClickListener{
        void onDeletePhotoClickListener(int position);
        void onSetAsDefaultPictureListener(int position);
    }
}