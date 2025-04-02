package com.devid_academy.cocktailbook.ui.edit

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
import com.devid_academy.cocktailbook.data.room.model.DrinkDetailsRoom
import com.devid_academy.cocktailbook.databinding.FragmentCreateBinding
import com.devid_academy.cocktailbook.databinding.FragmentEditBinding
import com.devid_academy.cocktailbook.ui.details.DetailsFragmentArgs
import com.devid_academy.cocktailbook.ui.details.DetailsViewModel
import com.devid_academy.cocktailbook.utils.picassoInsert
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditFragment : Fragment() {

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!
    private val args: EditFragmentArgs by navArgs()
    private val viewModel: EditViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        viewModel.getRoomDrink(args.drink.idDrink)

        viewModel.drinkRoomLiveData.observe(viewLifecycleOwner) { drink ->
            drink?.let {
                with(binding) {
                    fgEditTvName.setText(it.strDrink)
                    fgEditCocktailUrlImg.setText(it.strDrinkThumb)
                    fgEditEtIngredientsList.setText(it.strIngredients)
                    fgEditEtInstructionsList.setText(it.strInstructions)
                    fgEditIvItemImage.apply {
                        if(!it.strDrinkThumb.isNullOrEmpty()) {
                            picassoInsert(it.strDrinkThumb, this)
                        }
                    }
                }
            }
        }

        with(binding) {

            var imageUrl: String
            fgEditCocktailUrlImg.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    imageUrl = fgEditCocktailUrlImg.text.toString().trim()
                    if (imageUrl.isNotEmpty()) {
                        picassoInsert(imageUrl, fgEditIvItemImage)
                    }
                }
            }
//

            fgEditBtnEdit.setOnClickListener {
                viewModel.editRoomDrink(
                    DrinkDetailsRoom(
                        idDrink = args.drink.idDrink,
                        strDrink = fgEditTvName.text.toString().trim(),
                        strDrinkThumb = fgEditCocktailUrlImg.text.toString().trim(),
                        strIngredients = fgEditEtIngredientsList.text.toString().trim(),
                        strInstructions = fgEditEtInstructionsList.text.toString().trim(),
                        isMine = true
                    )
                )
            }
        }

        viewModel.isDrinkUpdated.observe(viewLifecycleOwner) {
            if(it) {
                navController.popBackStack()
            }
        }


    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}