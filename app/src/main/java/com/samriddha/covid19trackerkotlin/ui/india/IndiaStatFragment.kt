package com.samriddha.covid19trackerkotlin.ui.india

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.CompoundButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.samriddha.covid19trackerkotlin.R
import com.samriddha.covid19trackerkotlin.data.db.entity.IndiaData
import com.samriddha.covid19trackerkotlin.data.db.entity.IndiaDataByTime
import com.samriddha.covid19trackerkotlin.data.db.entity.IndiaStateData
import com.samriddha.covid19trackerkotlin.others.ApiException
import com.samriddha.covid19trackerkotlin.others.NoInternetException
import com.samriddha.covid19trackerkotlin.ui.india.states.KEY_INDIA_STATES
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.india_stat_fragment.*
import kotlinx.android.synthetic.main.india_stat_fragment.nestedScrollView
import kotlinx.android.synthetic.main.india_stat_fragment.pieChart
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.io.Serializable
import kotlin.collections.ArrayList

class IndiaStatFragment : Fragment(), KodeinAware, View.OnClickListener,
    CompoundButton.OnCheckedChangeListener {

    override val kodein by closestKodein()

    private val viewModelFactory by instance<IndiaViewModelFactory>()
    private lateinit var viewModel: IndiaStatViewModel
    private lateinit var adapter: IndiaStateAdapter
    private lateinit var navController: NavController
    private lateinit var snackbar: Snackbar
    private var indiaStateDataList: List<IndiaStateData> = ArrayList<IndiaStateData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.india_stat_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(IndiaStatViewModel::class.java)
        navController = Navigation.findNavController(requireView())

        initSnackBar()
        setViewSwitcher()
        setRecyclerView()
        getDataFromViewModel()


        btnStateAndDistrict.setOnClickListener(this)
        toggleButton.setOnCheckedChangeListener(this)

    }

    private fun getDataFromViewModel() {

        viewLifecycleOwner.lifecycleScope.launch {


            val job = async {

                try {
                    viewModel.fetchIndiaData() //Fetch data from network first
                } catch (e: NoInternetException) {

                    Log.e("Network", e.message.toString())
                    initSnackBar()
                    progressBar.visibility = View.GONE
                    snackbar.show()

                } catch (e:ApiException){
                    Toast.makeText(requireContext(),e.message,Toast.LENGTH_LONG).show()
                }

            }
            job.await()

            val indiaData = viewModel.indiaData.await()
            val indiaStatesData = viewModel.indiaStateData.await()
            val indiaDataByTime = viewModel.indiaDataByTime.await()

            indiaData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

                if (it == null) return@Observer
                setViewIndiaData(it)
                setPieGraph(it)
            })

            indiaStatesData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                if (it.isNullOrEmpty()) return@Observer
                adapter.setStateDataList(it)
                setViewIndiaData1(it.first())
                if (indiaStateDataList.isEmpty()) indiaStateDataList = it

            })

            indiaDataByTime.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                if (it.isNullOrEmpty()) return@Observer
                setLineChart(it)
                progressBar.visibility = View.GONE
                nestedScrollView.visibility = View.VISIBLE
                viewSwitcher.visibility = View.VISIBLE
            })

        }
    }

    private fun initSnackBar(){

        snackbar = Snackbar.make(indiaMainLayout,resources.getText(R.string.txt_check_internet),Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(resources.getText(R.string.txt_try_again), View.OnClickListener {
            snackbar.dismiss()
            progressBar.visibility = View.VISIBLE
            getDataFromViewModel()
        })

    }

    private fun setViewIndiaData(indiaData: IndiaData?) {
        if (indiaData != null) {
            tvIndCasePM.text = indiaData.casesPerOneMillion
            tvIndDeathsPM.text = indiaData.deathsPerOneMillion
            tvIndTested.text = indiaData.tests
            tvIndTestedPM.text = indiaData.testsPerOneMillion
        }
    }

    private fun setViewIndiaData1(data: IndiaStateData?) {
        if (data != null) {
            tvIndActiveCase.text = data.activeCase
            tvIndTotalCase.text = data.totalCase
            tvIndDeaths.text = data.totalDeaths
            tvIndListedToday.text = data.todayCase
            tvIndDeathsToday.text = data.todayDeaths
            tvIndRecovered.text = data.totalRecovered
            tvIndRecoveredToday.text = data.todayRecovered
            tvIndLastUpdated.text = data.lastUpdated

        }

    }

    private fun setRecyclerView() {
        adapter = IndiaStateAdapter(context)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        indiaStateRecycler.layoutManager = layoutManager
        indiaStateRecycler.setHasFixedSize(true)
        indiaStateRecycler.itemAnimator = DefaultItemAnimator()
        indiaStateRecycler.adapter = adapter
    }

    private fun setViewSwitcher() {

        //View Switcher Animation
        val into =
            AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left) // load an animation
        val out = AnimationUtils.loadAnimation(
            context,
            android.R.anim.slide_out_right
        ) // load an animation
        viewSwitcher.inAnimation = into
        viewSwitcher.outAnimation = out

    }

    private fun setPieGraph(indiaData: IndiaData) {
        val data = ArrayList<PieEntry>()
        data.add(PieEntry(indiaData.active?.toFloat()!!, "Active"))
        data.add(PieEntry(indiaData.cases?.toFloat()!!, "Total"))
        data.add(PieEntry(indiaData.recovered?.toFloat()!!, "Recovered"))
        data.add(PieEntry(indiaData.deaths?.toFloat()!!, "Deaths"))
        val colors = ArrayList<Int>()
        colors.add(Color.parseColor("#FFA48E"))
        colors.add(Color.parseColor("#BB86FC"))
        colors.add(Color.parseColor("#4ACFAC"))
        colors.add(Color.parseColor("#D87585"))

        //pieChart.setUsePercentValues(true);
        pieChart.isRotationEnabled = true
        pieChart.setHoleColor(Color.parseColor("#00000000"))
        pieChart.holeRadius = 60f
        //pieChart.setExtraOffsets(10,0,10,0);
        pieChart.transparentCircleRadius = 0f
        pieChart.setEntryLabelColor(Color.parseColor("#99FFFFFF"))
        pieChart.animateXY(1000, 1000)
        val description =
            Description()
        description.text = ""
        pieChart.description = description
        val pieDataSet = PieDataSet(data, null)
        pieDataSet.sliceSpace = 3f
        pieDataSet.valueTextSize = 10f
        pieDataSet.valueTextColor = Color.parseColor("#99FFFFFF")
        pieDataSet.colors = colors
        val legend = pieChart.legend
        legend.textColor = Color.parseColor("#99FFFFFF")
        legend.textSize = 12f
        val pieData = PieData(pieDataSet)
        pieChart.data = pieData
        pieChart.invalidate()
    }

    private fun setLineChart(dataByTime: List<IndiaDataByTime>?) {

        //Setting Up Data For Graph
        val yValue = ArrayList<Entry>()
        val yValue1 = ArrayList<Entry>()
        val yValue2 = ArrayList<Entry>()

        if (dataByTime != null) {
            var i = 0f
            val constant = 50f
            //Inserting Data From View Model Into ArrayList
            for (data in dataByTime) {
                if (i > constant) {
                    yValue.add(Entry(i, data.totalCase?.toFloat()!!))
                    yValue1.add(Entry(i, data.totalDeath?.toFloat()!!))
                    yValue2.add(Entry(i, data.totalRecovered?.toFloat()!!))
                }
                i++
            }
        }

        // Formatting value for X-axis
        val formatter: ValueFormatter =
            object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase): String {
                    if (value == 60f) return "Mar-30"
                    if (value == 70f) return "Apr-9"
                    if (value == 80f) return "Apr-19"
                    if (value == 90f) return "Apr-29"
                    if (value == 100f) return "May-9"
                    if (value == 110f) return "May-19"
                    if (value == 120f) return "May-29"
                    if (value == 130f) return "Jun-8"
                    if (value == 140f) return "Jun-18"
                    if (value == 150f) return "Jun-28"
                    if (value == 160f) return "Jul-8"
                    if (value == 170f) return "Jul-18"
                    if (value == 180f) return "Jul-28"
                    if (value == 190f) return "Aug-7"
                    if (value == 200f) return "Aug-17"
                    if (value == 210f) return "Aug-27"
                    if (value == 220f) return "Sep-6"
                    if (value == 230f) return "Sep-16"
                    if (value == 240f) return "Sep-26"
                    if (value == 250f) return "Oct-6"
                    if (value == 260f) return "Oct-16"
                    if (value == 270f) return "Oct-26"
                    if (value == 280f) return "Nov-5"
                    if (value == 290f) return "Nov-15"
                    if (value == 300f) return "Nov-25"

                    return value.toString()
                }
            }

        //Setting XAxis Data
        val xAxis = lineChart.xAxis
        xAxis.valueFormatter = formatter
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.textColor = Color.parseColor("#99FFFFFF")


        //Modifying YAxis
        val y = lineChart.axisLeft
        y.isEnabled = false
        val yAxis = lineChart.axisRight
        yAxis.textColor = Color.parseColor("#99FFFFFF")
        val set1 = LineDataSet(yValue, "Cases")
        val set2 = LineDataSet(yValue1, "Deaths")
        val set3 = LineDataSet(yValue2, "Recovered")


        //set1.enableDashedLine(10f, 5f, 0f);
        //set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.color = Color.parseColor("#BB86FC")
        set1.valueTextColor = Color.parseColor("#99FFFFFF")
        set1.setCircleColor(Color.WHITE)
        set1.lineWidth = 2f
        set1.circleRadius = 1f
        set1.setDrawCircleHole(true)
        set1.valueTextSize = 9f
        set1.setDrawFilled(false)

        set2.color = Color.parseColor("#D87585")
        set2.valueTextColor = Color.parseColor("#99FFFFFF")
        set2.setCircleColor(Color.WHITE)
        set2.lineWidth = 2f
        set2.circleRadius = 1f
        set2.setDrawCircleHole(true)
        set2.valueTextSize = 9f
        set2.setDrawFilled(false)

        set3.color = Color.parseColor("#4ACFAC")
        set3.valueTextColor = Color.parseColor("#99FFFFFF")
        set3.setCircleColor(Color.WHITE)
        set3.lineWidth = 2f
        set3.circleRadius = 1f
        set3.setDrawCircleHole(true)
        set3.valueTextSize = 9f
        set3.setDrawFilled(false)

        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1)
        dataSets.add(set2)
        dataSets.add(set3)

        val description = Description()
        description.text = "India Covid-19 Graph"
        description.textColor = Color.parseColor("#99FFFFFF")
        description.textSize = 12f
        lineChart.description = description

        val legend = lineChart.legend
        legend.textSize = 13f
        legend.textColor = Color.parseColor("#99FFFFFF")
        legend.xEntrySpace = 12f
        legend.form = Legend.LegendForm.CIRCLE

        lineChart.setPinchZoom(true)
        lineChart.setDrawBorders(false)

        val lineData = LineData(dataSets)
        lineChart.data = lineData
        lineChart.invalidate()
    }

    override fun onClick(v: View?) {

        if (v?.id == R.id.btnStateAndDistrict) {
            val bundle = Bundle()
            bundle.putSerializable(KEY_INDIA_STATES, indiaStateDataList as Serializable)
            navController.navigate(R.id.action_indiaStatFragment_to_allStatesFragment, bundle)
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (buttonView!!.id == R.id.toggleButton) {
            if (isChecked) viewSwitcher.showNext() else viewSwitcher.showPrevious()
        }
    }

    override fun onStop() {
        super.onStop()
        if(snackbar.isShown)
            snackbar.dismiss()
    }
}