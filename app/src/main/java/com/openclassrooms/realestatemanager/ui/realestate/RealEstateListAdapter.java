package com.openclassrooms.realestatemanager.ui.realestate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.pojo.Address;
import com.openclassrooms.realestatemanager.models.pojo.House;
import com.openclassrooms.realestatemanager.models.pojo.HouseType;
import com.openclassrooms.realestatemanager.models.pojo.Photo;
import com.openclassrooms.realestatemanager.tools.TypeConverter;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RealEstateListAdapter extends RecyclerView.Adapter<RealEstateListAdapter.RealEstateListAdapterViewHolder> {
    private List<House> houses;
    private HashMap<Long, HouseType> hashMapHouseType;
    private HashMap<Long, Address> hashMapAddress;
    private HashMap<Long, List<Photo>> hashMapPhoto;
    private HashMap<Long, String> hashMapRoom;
    private Context context;
    private OnItemClickListener onItemClickListener;


    static class RealEstateListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.vp_real_estate)
        ViewPager vpPicture;
        @BindView(R.id.i_tv_house_type)
        TextView tvHouseType;
        @BindView(R.id.i_tv_district)
        TextView tvDistrict;
        @BindView(R.id.i_tv_price)
        TextView tvHousePrice;
        OnItemClickListener onItemClickListener;

        RealEstateListAdapterViewHolder(View v, OnItemClickListener onItemClickListener) {
            super(v);
            ButterKnife.bind(this, itemView);
            this.onItemClickListener = onItemClickListener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClickt(getAdapterPosition());
        }
    }

    RealEstateListAdapter(Context context, List<House> houses, HashMap<Long, HouseType> hashMapHouseType, HashMap<Long, Address> hashMapAddress, HashMap<Long, List<Photo>> listPhoto, HashMap<Long, String> hashMapRoom, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.houses = houses;
        this.hashMapHouseType = hashMapHouseType;
        this.hashMapAddress = hashMapAddress;
        this.hashMapPhoto = listPhoto;
        this.hashMapRoom = hashMapRoom;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    @NonNull
    public RealEstateListAdapter.RealEstateListAdapterViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.i_real_estate, parent, false);
        return new RealEstateListAdapterViewHolder(v, onItemClickListener);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onBindViewHolder(RealEstateListAdapterViewHolder holder, int position) {

        House house = houses.get(position);
        if(house.getPrice() > -1)
            holder.tvHousePrice.setText(String.format(context.getString(R.string.house_price), TypeConverter.convertDoubleToStringFormat(house.getPrice())));
        else
            holder.tvHousePrice.setText(R.string.price_not_specified);

        String houseType = hashMapHouseType.get(house.getIdHouseType()).getHouseType();
        if(houseType == null || houseType.equals("")) holder.tvHouseType.setText(R.string.house_type_not_specified);
        else holder.tvHouseType.setText(houseType);

        String district = hashMapAddress.get(house.getIdAddress()).getDistrict();
        if(district.equals("")) holder.tvDistrict.setText(R.string.district_not_specified);
            else holder.tvDistrict.setText(district);

        if(hashMapPhoto.get(house.getIdHouse()) != null){
            PagerAdapter picturePagerAdapter = new PicturePagerAdapter(context, hashMapPhoto.get(house.getIdHouse()), hashMapRoom);
            holder.vpPicture.setAdapter(picturePagerAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return houses.size();
    }

    public interface OnItemClickListener{
        void onItemClickt(int position);
    }
}
