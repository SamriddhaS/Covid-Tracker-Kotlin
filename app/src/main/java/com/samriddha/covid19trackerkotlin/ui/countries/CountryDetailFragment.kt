package com.samriddha.covid19trackerkotlin.ui.countries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.samriddha.covid19trackerkotlin.R
import com.samriddha.covid19trackerkotlin.data.db.entity.CountryData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_country_detail.*


const val KEY_TRANSFER_DATA = "DATA_TRANSFER_KEY"
class CountryDetailFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_country_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val countryDetail = arguments?.getSerializable(KEY_TRANSFER_DATA) as CountryData
        setViewCountryDetail(countryDetail)
        
    }

    private fun setViewCountryDetail(countryData: CountryData) {
        Picasso.get().load(countryData.countryInfo?.flag).into(ivCountryFlag)
        tvCountryName.text = countryData.country
        tvCountryName1.text = countryData.country
        tvContinent.text = countryData.continent
        tvActiveCase.text=  countryData.active
        tvTotalCase.text = countryData.cases
        tvListedToday.text = countryData.todayCases
        tvCasePM.text = countryData.casesPerOneMillion
        tvTotalDeath.text = countryData.deaths
        tvDeathToday.text = countryData.todayDeaths
        tvDeathPM.text = countryData.deathsPerOneMillion
        tvPeopleTested.text = countryData.tests
        tvTestPM.text = countryData.testsPerOneMillion
        tvRecovered.text = countryData.recovered
    }



}