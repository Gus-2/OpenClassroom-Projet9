package com.openclassrooms.realestatemanager.ui.realestatedetail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.pojo.PointOfInterest;
import com.openclassrooms.realestatemanager.models.pojo.TypePointOfInterest;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterPointOfInterestDetail extends RecyclerView.Adapter<AdapterPointOfInterestDetail.MyViewHolder> {
    private List<PointOfInterest> listPointOfInterest;

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name_point_of_interest)
        TextView tvNamePointOfInterest;

        @BindView(R.id.tv_address_point_of_interest)
        TextView tvAddressPointOfInterest;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    AdapterPointOfInterestDetail(List<PointOfInterest> listPointOfInterest) {
        this.listPointOfInterest = listPointOfInterest;
    }

    @NotNull
    @Override
    public AdapterPointOfInterestDetail.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                        int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.i_point_of_interest_detail, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PointOfInterest pointOfInterest = listPointOfInterest.get(position);
        holder.tvNamePointOfInterest.setText(pointOfInterest.getName());
        holder.tvAddressPointOfInterest.setText(listPointOfInterest.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return listPointOfInterest.size();
    }
}
