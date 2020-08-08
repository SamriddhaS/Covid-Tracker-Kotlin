package com.samriddha.covid19trackerkotlin.ui.india.districts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.samriddha.covid19trackerkotlin.R
import com.samriddha.covid19trackerkotlin.others.ApiException
import com.samriddha.covid19trackerkotlin.others.NoInternetException
import com.samriddha.covid19trackerkotlin.ui.india.states.KEY_INDIA_STATES
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.all_districts_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class AllDistrictsFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory by instance<AllDistrictsViewModelFactory>()
    private lateinit var viewModel: AllDistrictsViewModel
    private lateinit var stateName: String
    private lateinit var adapter: DistrictsAdapter
    private lateinit var snackbar: Snackbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.all_districts_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AllDistrictsViewModel::class.java)

        stateName = arguments?.getString(KEY_INDIA_STATES)!!

        initSnackBar()
        setUpRecyclerView()
        getDataFromViewModel()

    }

    private fun initSnackBar() {

        snackbar = Snackbar.make(allDistLayoutMain, resources.getText(R.string.txt_check_internet), Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(resources.getText(R.string.txt_try_again), View.OnClickListener {
            snackbar.dismiss()
            progressBar.visibility = View.VISIBLE
            getDataFromViewModel()
        })

    }

    private fun getDataFromViewModel() {

        viewLifecycleOwner.lifecycleScope.launch {

            try {
                viewModel.fetchDistrictsData()?.observe(viewLifecycleOwner, Observer {
                    if (it == null) return@Observer
                    adapter.setDistrictsHashMap(it.districtValue[stateName]?.districts)
                    linearLayout.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    tvStateName.text = stateName

                })
            } catch (e: NoInternetException) {
                Log.e("Network", e.message.toString())
                initSnackBar()
                progressBar.visibility = View.GONE
                snackbar.show()
            } catch (e:ApiException){
                Toast.makeText(requireContext(),e.message,Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun setUpRecyclerView() {
        adapter = DistrictsAdapter(context)
        districtRecycler.layoutManager = LinearLayoutManager(context)
        districtRecycler.setHasFixedSize(true)
        districtRecycler.adapter = adapter
    }

    override fun onStop() {
        super.onStop()
        if (snackbar.isShown)
            snackbar.dismiss()
    }

}