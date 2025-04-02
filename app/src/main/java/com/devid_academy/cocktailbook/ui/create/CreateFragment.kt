package com.devid_academy.cocktailbook.ui.create

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devid_academy.cocktailbook.R
import com.devid_academy.cocktailbook.data.room.model.DrinkDetailsRoom
import com.devid_academy.cocktailbook.databinding.FragmentCreateBinding
import com.devid_academy.cocktailbook.utils.picassoInsert
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateFragment : Fragment() {

    private var _binding: FragmentCreateBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreateViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        with(binding) {
            var imageUrl: String
            fgCreateEtUrl.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    imageUrl = fgCreateEtUrl.text.toString().trim()
                    if (imageUrl.isNotEmpty()) {
                        picassoInsert(imageUrl, fgCreateIv)
                    }
                }
            }

            fgCreateBtn.setOnClickListener {
                viewModel.insertRoomDrink(
                    DrinkDetailsRoom(
                        idDrink = 0,
                        strDrink = fgCreateEtName.text.toString().trim(),
                        strDrinkThumb = fgCreateEtUrl.text.toString().trim(),
                        strIngredients = fgCreateEtIngredients.text.toString().trim(),
                        strInstructions = fgCreateEtInstructions.text.toString().trim(),
                        isMine = true
                    )
                )
            }

            viewModel.isDrinkCreated.observe(viewLifecycleOwner) {
                if(it) {
                    navController.popBackStack()
                }
            }
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



}