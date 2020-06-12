package com.openclassrooms.realestatemanager.ui.realestateform;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.pojo.PointOfInterest;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterPointOfInterest extends RecyclerView.Adapter<AdapterPointOfInterest.MyViewHolder> {
    private List<PointOfInterest> listPointOfInterest;
    private OnPointOfInterestClickListener onPointOfInterestClickListener;

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name_point_of_interest)
        TextView tvNamePointOfInterest;

        @BindView(R.id.tv_address_point_of_interest)
        TextView tvAddressPointOfInterest;

        @BindView(R.id.iv_delete_point_of_interest)
        ImageView ivDeletePointOfInterest;

        OnPointOfInterestClickListener onPointOfInterestClickListener;

        MyViewHolder(View itemView, OnPointOfInterestClickListener onPointOfInterestClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.onPointOfInterestClickListener = onPointOfInterestClickListener;
            ivDeletePointOfInterest.setOnClickListener(v -> onPointOfInterestClickListener.onPointOfInterestClickListener(getAdapterPosition()));
        }
    }

    public AdapterPointOfInterest(List<PointOfInterest> listPointOfInterest, OnPointOfInterestClickListener onPointOfInterestClickListener) {
        this.listPointOfInterest = listPointOfInterest;
        this.onPointOfInterestClickListener = onPointOfInterestClickListener;
    }

    @NotNull
    @Override
    public AdapterPointOfInterest.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.i_point_of_interest, parent, false);
        return new MyViewHolder(v, onPointOfInterestClickListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvNamePointOfInterest.setText(listPointOfInterest.get(position).getName());
        holder.tvAddressPointOfInterest.setText(listPointOfInterest.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return listPointOfInterest.size();
    }

    public interface OnPointOfInterestClickListener{
        void onPointOfInterestClickListener(int position);
    }
}
