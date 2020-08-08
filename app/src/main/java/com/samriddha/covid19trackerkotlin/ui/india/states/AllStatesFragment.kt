package com.samriddha.covid19trackerkotlin.ui.india.states

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.samriddha.covid19trackerkotlin.R
import com.samriddha.covid19trackerkotlin.data.db.entity.IndiaStateData
import kotlinx.android.synthetic.main.fragment_all_states.*

const val KEY_INDIA_STATES = "INDIA_ALL_STATES_KEY"

class AllStatesFragment : Fragment(),
    AllStateAdaptar.IndiaStateAdaptarInterface {

    private lateinit var adapter: AllStateAdaptar
    private lateinit var navController:NavController
    private lateinit var allStates:List<IndiaStateData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_states, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        navController = Navigation.findNavController(requireView())

        setUpRecyclerView()

        allStates = arguments?.getSerializable(KEY_INDIA_STATES) as List<IndiaStateData>

        adapter.setStateDataList(allStates)

    }

    private fun setUpRecyclerView() {
        adapter =
            AllStateAdaptar(
                context
            )
        adapter.setmInterface(this)
        indiaAllStatesRecycler.layoutManager = LinearLayoutManager(context)
        indiaAllStatesRecycler.setHasFixedSize(true)
        indiaAllStatesRecycler.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.all_states_menu,menu)

        val search = menu.findItem(R.id.searchState)

        val searchView = search.actionView as SearchView

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

    override fun OnRecyclerItemClickListener(position: Int) {

        val stateName = allStates[position].stateName
        val bundle = Bundle()
        bundle.putString(KEY_INDIA_STATES,stateName)
        navController.navigate(R.id.action_allStatesFragment_to_allDistrictsFragment,bundle)

    }

}
