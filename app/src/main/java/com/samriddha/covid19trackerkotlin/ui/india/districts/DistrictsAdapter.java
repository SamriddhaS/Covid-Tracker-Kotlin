package com.samriddha.covid19trackerkotlin.ui.india.districts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.samriddha.covid19trackerkotlin.R;
import com.samriddha.covid19trackerkotlin.pojo.Districts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DistrictsAdapter extends RecyclerView.Adapter<DistrictsAdapter.DistrictViewHolder> {


    private Context mContext;
    private Map<String, Districts> districtsHashMap ;
    private List<Map.Entry<String, Districts>> districtList ;


    public DistrictsAdapter(Context mContext) {
        this.mContext = mContext;
        districtsHashMap = new HashMap<>();
        districtList = new ArrayList<>();

    }

    public void setDistrictsHashMap(Map<String,Districts> districtsHashMap) {

        if (districtsHashMap!=null){

            this.districtsHashMap = districtsHashMap;
            Set<Map.Entry<String,Districts>> entrySet = districtsHashMap.entrySet() ;
            districtList = new ArrayList<>(entrySet) ;

        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DistrictViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new DistrictViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.district_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DistrictViewHolder holder, int position) {


        holder.districtName.setText(districtList.get(position).getKey());
        holder.recovered.setText(districtList.get(position).getValue().getRecovered());
        holder.totalCase.setText(districtList.get(position).getValue().getTotalCase());
        holder.activeCase.setText(districtList.get(position).getValue().getActiveCase());
        holder.totalDeaths.setText(districtList.get(position).getValue().getDeaths());


    }


    @Override
    public int getItemCount() {

        return districtList.size();
    }

    public class DistrictViewHolder extends RecyclerView.ViewHolder {

        TextView totalCase, activeCase, recovered, totalDeaths ,districtName ;


        public DistrictViewHolder(@NonNull View itemView) {
            super(itemView);

            totalCase = itemView.findViewById(R.id.tvDisItemTotal) ;
            activeCase = itemView.findViewById(R.id.tvDistItemActive) ;
            recovered = itemView.findViewById(R.id.tvDistItemRecovered) ;
            totalDeaths = itemView.findViewById(R.id.tvDistItemDead) ;
            districtName = itemView.findViewById(R.id.tvDistItemName) ;
        }

    }

}
