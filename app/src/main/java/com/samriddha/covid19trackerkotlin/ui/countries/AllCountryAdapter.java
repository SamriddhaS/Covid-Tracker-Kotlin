package com.samriddha.covid19trackerkotlin.ui.countries;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.samriddha.covid19trackerkotlin.R;
import com.samriddha.covid19trackerkotlin.data.db.entity.CountryData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AllCountryAdapter extends RecyclerView.Adapter<AllCountryAdapter.CountryViewHolder> implements Filterable {

    private Context mContext;
    private List<CountryData> countryData;
    private List<CountryData> allCountryData;
    private CountryAdapterListener mListener;

    public AllCountryAdapter(Context mContext, CountryAdapterListener mListener) {
        this.mContext = mContext;
        this.mListener = mListener;
        countryData = new ArrayList<>();
    }

    public void setCountryData(List<CountryData> countryData) {

        if (countryData != null) {
            this.countryData.clear();
            this.countryData = countryData;
            allCountryData = new ArrayList<>(countryData);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new CountryViewHolder(LayoutInflater.from(mContext).inflate(R.layout.all_country_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, final int position) {

        holder.countryName.setText(countryData.get(position).getCountry());
        holder.totalCase.setText(countryData.get(position).getCases());
        holder.activeCase.setText(countryData.get(position).getActive());
        holder.todayCase.setText(countryData.get(position).getTodayCases());
        holder.recovered.setText(countryData.get(position).getRecovered());
        holder.totalDeaths.setText(countryData.get(position).getDeaths());
        holder.deathsToday.setText(countryData.get(position).getTodayDeaths());
        Picasso.get().load(countryData.get(position).getCountryInfo().getFlag()).into(holder.countryImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mListener != null) {
                    mListener.onCountryItemClickListener(countryData.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        if (countryData != null && countryData.size() > 0)
            return countryData.size();
        else
            return 0;
    }

    class CountryViewHolder extends RecyclerView.ViewHolder {

        TextView totalCase, activeCase, todayCase, recovered, totalDeaths, deathsToday, countryName;
        ImageView countryImage;

        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);

            totalCase = (TextView) itemView.findViewById(R.id.tvItemTotalCase);
            activeCase = (TextView) itemView.findViewById(R.id.tvItemActiveCase);
            todayCase = (TextView) itemView.findViewById(R.id.tvItemTodayCase);
            recovered = (TextView) itemView.findViewById(R.id.tvItemRecovered);
            totalDeaths = (TextView) itemView.findViewById(R.id.tvItemTotalDeath);
            deathsToday = (TextView) itemView.findViewById(R.id.tvItemDeathToday);
            countryName = (TextView) itemView.findViewById(R.id.tvItemCountryName);
            countryImage = (ImageView) itemView.findViewById(R.id.ivItemCountryFlag);

        }
    }

    public interface CountryAdapterListener {

        void onCountryItemClickListener(CountryData data);
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private Filter mFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<CountryData> filteredData = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredData.addAll(allCountryData);
            } else {

                String filterInputText = constraint.toString().toLowerCase().trim();
                for (CountryData data : allCountryData) {
                    if (data.getCountry().toLowerCase().contains(filterInputText)) {
                        filteredData.add(data);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredData;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            countryData.clear();
            countryData.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
