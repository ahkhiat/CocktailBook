package com.devid_academy.cocktailbook.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.devid_academy.cocktailbook.R
import com.devid_academy.cocktailbook.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressBar: ProgressBar
    private lateinit var drinkAdapter: DrinkAdapter
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
       }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModel.remoteDrinksListLiveData.observe(viewLifecycleOwner) {
//            Log.i("MAIN FG", "MAIN FG List RetroFit : " + it)
//        }
//
//        viewModel.roomDrinksListLiveData.observe(viewLifecycleOwner) {
//            Log.i("MAIN FG", "MAIN FG List Room : " + it)
//        }

        viewModel.combinedDrinksListLiveData.observe(viewLifecycleOwner) {
            Log.i("MAIN FG", "Liste Combinée : " + it)
        }
        viewModel.roomDrinksListLiveData.observe(viewLifecycleOwner) {
            Log.i("DEBUG", "Taille Liste Room : ${viewModel.getRoomListSize()}")
        }
        viewModel.remoteDrinksListLiveData.observe(viewLifecycleOwner) {
            Log.i("DEBUG", "Taille Liste Retrofit : ${viewModel.getRemoteListSize()}")
        }
        viewModel.combinedDrinksListLiveData.observe(viewLifecycleOwner) {
            Log.i("DEBUG", "Taille Liste Fusionnée : ${viewModel.getCombinedListSize()}")
        }

        val navController = findNavController()

        drinkAdapter = DrinkAdapter(
            onItemClick = { drink ->
                val action = MainFragmentDirections.actionMainFragmentToDetailsFragment(drink)
                navController.navigate(action)
            }
        )

        with(binding) {

            rvDrinks.layoutManager = LinearLayoutManager(requireContext())
            rvDrinks.adapter = drinkAdapter

            viewModel.combinedDrinksListLiveData.observe(viewLifecycleOwner) {
                drinkAdapter.submitList(it)
            }

            viewModel.isLoading.observe(viewLifecycleOwner) {
                progressBar.visibility = if (it) View.VISIBLE else View.GONE
            }

            mainFgBtn.setOnClickListener {
                navController.navigate(R.id.action_mainFragment_to_createFragment)
            }


        }




    }


}