package com.samriddha.covid19trackerkotlin.ui.countries

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.samriddha.covid19trackerkotlin.R
import com.samriddha.covid19trackerkotlin.data.db.entity.CountryData
import com.samriddha.covid19trackerkotlin.others.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.countries_fragment.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CountriesFragment : Fragment(), KodeinAware, AllCountryAdapter.CountryAdapterListener {

    override val kodein by closestKodein()

    private val viewModelFactory by instance<CountriesViewModelFactory>()
    private val sharedPrefManager by instance<SharedPrefManager>()
    private lateinit var viewModel: CountriesViewModel
    private lateinit var adapter: AllCountryAdapter
    private lateinit var navController: NavController
    private lateinit var snackbar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.countries_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CountriesViewModel::class.java)
        navController = Navigation.findNavController(requireView())
        snackbar = Snackbar.make(allCountriesMainLayout, resources.getText(R.string.txt_check_internet),Snackbar.LENGTH_INDEFINITE)

        initRecyclerView()
        fetchDataFromNetwork(false)
        getData()

    }


    private fun initRecyclerView() {

        adapter = AllCountryAdapter(context, this)
        countriesRecyclerView.layoutManager = LinearLayoutManager(context)
        countriesRecyclerView.setHasFixedSize(true)
        countriesRecyclerView.adapter = adapter
    }

    private fun getData() {

        viewLifecycleOwner.lifecycleScope.launch {

            val allCountry = viewModel.allCountryList.await()
            allCountry.observe(viewLifecycleOwner, Observer {

                if (it == null) return@Observer

                adapter.setCountryData(it)
                setProgressBar(false)
            })
        }
    }

    private fun fetchDataFromNetwork(keyChanged: Boolean) {

        viewLifecycleOwner.lifecycleScope.launch {
            val job = async {

                try {
                    viewModel.updateAllCountry(keyChanged)

                } catch (e:NoInternetException) {
                    Log.e("Network", e.message.toString())

                    simpleArcLoader1.visibility = View.GONE
                    snackbar = Snackbar.make(allCountriesMainLayout, resources.getText(R.string.txt_check_internet),Snackbar.LENGTH_INDEFINITE)
                    snackbar.setAction(resources.getText(R.string.txt_try_again), View.OnClickListener {
                        snackbar.dismiss()
                        simpleArcLoader1.visibility = View.VISIBLE
                        fetchDataFromNetwork(keyChanged)
                    })
                    snackbar.show()

                } catch (e:ApiException){
                    Toast.makeText(requireContext(), e.message ,Toast.LENGTH_LONG).show()
                }
            }
            job.await()

        }

    }

    private fun setProgressBar(setLoading: Boolean) {
        if (setLoading) {
            simpleArcLoader1.visibility = View.VISIBLE
            countriesRecyclerView.visibility = View.GONE
        } else {
            simpleArcLoader1.visibility = View.GONE
            countriesRecyclerView.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.country_menu, menu)

        when (sharedPrefManager.read(API_SORT_KEY)) {

            sharedPrefManager.read(KEY_A_Z) -> menu.findItem(R.id.sortA_Z).isChecked = true
            sharedPrefManager.read(KEY_TOTAL_CASE) -> {
                menu.findItem(R.id.sortTotalCase).isChecked = true
            }
            sharedPrefManager.read(KEY_ACTIVE_CASE) -> {
                menu.findItem(R.id.sortActiveCase).isChecked = true
            }
            sharedPrefManager.read(KEY_TODAY_CASE) -> {
                menu.findItem(R.id.sortTodayCase).isChecked = true
            }
            sharedPrefManager.read(KEY_TOTAL_DEATHS) -> {
                menu.findItem(R.id.sortDeaths).isChecked = true
            }
            sharedPrefManager.read(KEY_TODAY_DEATHS) -> {
                menu.findItem(R.id.sortTodayDeaths).isChecked = true
            }
            sharedPrefManager.read(KEY_RECOVERED) -> {
                menu.findItem(R.id.sortRecovered).isChecked = true
            }

        }


        //Implementing Search Function
        val searchItem = menu.findItem(R.id.searchCountry)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.sortActiveCase -> {
                setProgressBar(true)
                item.isChecked = true
                sharedPrefManager.write(API_SORT_KEY, sharedPrefManager.read(KEY_ACTIVE_CASE))
                fetchDataFromNetwork(true)
                true
            }
            R.id.sortTotalCase -> {
                setProgressBar(true)
                item.isChecked = true
                sharedPrefManager.write(API_SORT_KEY, sharedPrefManager.read(KEY_TOTAL_CASE))
                fetchDataFromNetwork(true)
                true
            }
            R.id.sortRecovered -> {
                setProgressBar(true)
                item.isChecked = true
                sharedPrefManager.write(API_SORT_KEY, sharedPrefManager.read(KEY_RECOVERED))
                fetchDataFromNetwork(true)
                true
            }
            R.id.sortDeaths -> {
                setProgressBar(true)
                item.isChecked = true
                sharedPrefManager.write(API_SORT_KEY, sharedPrefManager.read(KEY_TOTAL_DEATHS))
                fetchDataFromNetwork(true)
                true
            }
            R.id.sortA_Z -> {
                setProgressBar(true)
                item.isChecked = true
                sharedPrefManager.write(API_SORT_KEY, sharedPrefManager.read(KEY_A_Z))
                fetchDataFromNetwork(true)
                true
            }
            R.id.sortTodayCase -> {
                setProgressBar(true)
                item.isChecked = true
                sharedPrefManager.write(API_SORT_KEY, sharedPrefManager.read(KEY_TODAY_CASE))
                fetchDataFromNetwork(true)
                true
            }
            R.id.sortTodayDeaths -> {
                setProgressBar(true)
                item.isChecked = true
                sharedPrefManager.write(API_SORT_KEY, sharedPrefManager.read(KEY_TODAY_DEATHS))
                fetchDataFromNetwork(true)
                true
            }
            else -> false
        }
    }

    override fun onCountryItemClickListener(data: CountryData?) {

        val bundle = Bundle()
        bundle.putSerializable(KEY_TRANSFER_DATA, data)
        navController.navigate(R.id.action_countriesFragment_to_countryDetailFragment, bundle)
    }

    override fun onStop() {
        super.onStop()
        if(snackbar.isShown)
            snackbar.dismiss()
    }

}