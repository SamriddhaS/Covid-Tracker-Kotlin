package com.samriddha.covid19trackerkotlin.ui.india;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.samriddha.covid19trackerkotlin.R;
import com.samriddha.covid19trackerkotlin.data.db.entity.IndiaStateData;

import java.util.ArrayList;
import java.util.List;

public class IndiaStateAdapter extends RecyclerView.Adapter<IndiaStateAdapter.StateViewHolder> {

    private List<IndiaStateData> stateDataList;
    private Context mContext;


    public IndiaStateAdapter(Context mContext) {
        this.mContext = mContext;
        stateDataList = new ArrayList<>();

    }

    public void setStateDataList(List<IndiaStateData> stateDataList) {

        this.stateDataList = stateDataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new StateViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.state_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StateViewHolder holder, int position) {


        holder.stateName.setText(stateDataList.get(position).getStateName());
        holder.totalCase.setText(stateDataList.get(position).getTotalCase());
        holder.activeCase.setText(stateDataList.get(position).getActiveCase());
        holder.recovered.setText(stateDataList.get(position).getTotalRecovered());


    }


    @Override
    public int getItemCount() {

        if (stateDataList != null && stateDataList.size() > 0)
            return 7;
        else
            return 0;
    }

    public class StateViewHolder extends RecyclerView.ViewHolder {

        TextView totalCase, activeCase, todayCase, recovered, totalDeaths, deathsToday, stateName, updated;

        public StateViewHolder(@NonNull View itemView) {
            super(itemView);

            totalCase = (TextView) itemView.findViewById(R.id.tvItemTotalCase);
            activeCase = (TextView) itemView.findViewById(R.id.tvItemActiveCase);
            todayCase = (TextView) itemView.findViewById(R.id.tvItemTodayCase);
            recovered = (TextView) itemView.findViewById(R.id.tvItemRecovered);
            totalDeaths = (TextView) itemView.findViewById(R.id.tvItemTotalDeath);
            deathsToday = (TextView) itemView.findViewById(R.id.tvItemDeathToday);
            stateName = (TextView) itemView.findViewById(R.id.tvItemIndStateName);
            updated = (TextView) itemView.findViewById(R.id.tvItemIndStateUpdated);

        }


    }

}
