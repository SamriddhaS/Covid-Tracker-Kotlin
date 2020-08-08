package com.samriddha.covid19trackerkotlin.ui.global

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.samriddha.covid19trackerkotlin.R
import com.samriddha.covid19trackerkotlin.data.db.entity.GlobalData
import com.samriddha.covid19trackerkotlin.others.ApiException
import com.samriddha.covid19trackerkotlin.others.NoInternetException
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.global_data_fragment.*
import kotlinx.coroutines.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.util.ArrayList


class GlobalDataFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory by instance<GlobalViewModelFactory>()
    private lateinit var viewModel: GlobalDataViewModel
    private lateinit var snackbar: Snackbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.global_data_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        viewModel = ViewModelProvider(this, viewModelFactory).get(GlobalDataViewModel::class.java)
        initSnackBar()
        getData()

    }

    private fun initSnackBar(){

        snackbar = Snackbar.make(
            globalDataMainView,
            resources.getText(R.string.txt_check_internet),
            Snackbar.LENGTH_INDEFINITE
        )

        snackbar.setAction(resources.getText(R.string.txt_try_again), View.OnClickListener {
            snackbar.dismiss()
            arcLoader.visibility = View.VISIBLE
            getData()
        })

    }

    private fun getData() {

        viewLifecycleOwner.lifecycleScope.launch {

            // We first check if data is needed to be updated.
            val job = async {

                try {
                    viewModel.updateGlobalData()

                } catch (e: NoInternetException) {
                    Log.e("Network", e.message.toString())
                    initSnackBar()
                    snackbar.show()
                    arcLoader.visibility = View.GONE
                } catch (e: ApiException) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                }

            }
            job.await()

            //we do the updating data by api call and getting data from local db in two different jobs because if internet is not available
            //and we could not fetch api then we will show the user local data that is present inside db even if it is outdated,it's better than
            //showing blank screen.
            val globalData = viewModel.globalData.await()
            globalData.observe(viewLifecycleOwner, Observer {
                if (it == null) return@Observer

                setViewData(it)
            })

        }

    }

    private fun setViewData(globalData: GlobalData?) {

        if (globalData != null) {
            tvTotalCases.text = globalData.cases
            tvActiveCases.text = globalData.active
            tvRecovered.text = globalData.recovered
            tvTodayCase.text = globalData.todayCases
            tvCasesPM.text = globalData.casesPerOneMillion
            tvTodayDeaths.text = globalData.todayDeaths
            tvDeathsPM.text = globalData.deathsPerOneMillion
            tvTotalDeaths.text = globalData.deaths
            tvCriticalCase.text = globalData.critical
            tvTested.text = globalData.tests
            tvTestsPM.text = globalData.testsPerOneMillion
            tvAffectedCountries.text = globalData.affectedCountries
            setPieGraph(globalData)
            arcLoader.visibility = View.GONE
            nestedScrollView!!.visibility = View.VISIBLE
        }

    }

    private fun setPieGraph(globalData: GlobalData) {
        val data = ArrayList<PieEntry>()
        data.add(PieEntry(globalData.active.toFloat(), "Active"))
        data.add(PieEntry(globalData.cases.toFloat(), "Total"))
        data.add(PieEntry(globalData.recovered.toFloat(), "Recovered"))
        data.add(PieEntry(globalData.deaths.toFloat(), "Deaths"))
        val colors = ArrayList<Int>()
        colors.add(Color.parseColor("#FFA48E"))
        colors.add(Color.parseColor("#BB86FC"))
        colors.add(Color.parseColor("#4ACFAC"))
        colors.add(Color.parseColor("#D87585"))

        //pieChart.setUsePercentValues(true);
        pieChart!!.isRotationEnabled = true
        pieChart!!.setHoleColor(Color.parseColor("#00000000"))
        pieChart!!.holeRadius = 60f
        //pieChart.setExtraOffsets(10,0,10,0);
        pieChart!!.transparentCircleRadius = 0f
        pieChart!!.setEntryLabelColor(Color.parseColor("#99FFFFFF"))
        pieChart!!.animateXY(1000, 1000)
        val description = Description()
        description.text = ""
        pieChart!!.description = description
        val pieDataSet = PieDataSet(data, null)
        pieDataSet.sliceSpace = 3f
        pieDataSet.valueTextSize = 10f
        pieDataSet.valueTextColor = Color.parseColor("#99FFFFFF")
        pieDataSet.colors = colors
        val legend = pieChart!!.legend
        legend.textColor = Color.parseColor("#99FFFFFF")
        legend.textSize = 12f
        val pieData = PieData(pieDataSet)
        pieChart!!.data = pieData
        pieChart!!.invalidate()
    }

    override fun onStop() {
        super.onStop()
        if (snackbar.isShown)
            snackbar.dismiss()
    }
}