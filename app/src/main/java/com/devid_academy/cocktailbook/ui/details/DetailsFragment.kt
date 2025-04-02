package com.devid_academy.cocktailbook.ui.details

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devid_academy.cocktailbook.R
import com.devid_academy.cocktailbook.databinding.FragmentDetailsBinding
import com.devid_academy.cocktailbook.ui.main.MainFragmentDirections
import com.devid_academy.cocktailbook.utils.picassoInsert
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: DetailsFragmentArgs by navArgs()
    private val viewModel: DetailsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        Log.i("DETAILS FG", "Args :" + args)
        viewModel.getIfRemoteOrRoom(args.drink)

        binding.fgDetailsBtn.visibility = if (args.drink.isMine) View.VISIBLE else View.GONE
        Log.i("DETAILS FG", "ARGS DRINKS IS MINE " + args.drink.isMine)


        viewModel.drinkLiveData.observe(viewLifecycleOwner) { drink ->
            drink?.let {
                binding.apply {
                    fgDetailsTvTitle.text = it.strDrink
                    fgDetailsIvItemImage.apply {
                        if(!it.strDrinkThumb.isNullOrEmpty()) {
                            picassoInsert(it.strDrinkThumb, this)
                        }
                    }
                    fgDetailsTvInsctructionsList.text = drink.strInstructions
                }
            }
        }

        viewModel.ingredientsLiveData.observe(viewLifecycleOwner) { ingredients ->
            var ingredientListToShow = ""
            ingredients.forEach {
                Log.i("DETAILS FG", "Ingredient :" + it)
                ingredientListToShow += "$it \n"
                binding.fgDetailsTvIngredientsList.text = ingredientListToShow
            }
        }

        viewModel.drinkRoomLiveData.observe(viewLifecycleOwner) { drink ->
            drink?.let {
                binding.apply {
                    fgDetailsTvTitle.text = it.strDrink
                    fgDetailsIvItemImage.apply {
                        if(!it.strDrinkThumb.isNullOrEmpty()) {
                            picassoInsert(it.strDrinkThumb, this)
                        }
                    }
                    fgDetailsTvIngredientsList.text = drink.strIngredients
                    fgDetailsTvInsctructionsList.text = drink.strInstructions

                    fgDetailsBtn.setOnClickListener {
                        val action = DetailsFragmentDirections.actionDetailsFragmentToEditFragment(drink)
                        navController.navigate(action)
                    }

                }
            }
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}