package com.devid_academy.cocktailbook.ui.create

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devid_academy.cocktailbook.R
import com.devid_academy.cocktailbook.databinding.FragmentCreateBinding
import com.devid_academy.cocktailbook.utils.picassoInsert


class CreateFragment : Fragment() {

    private var _binding: FragmentCreateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
//            createBtnInsert.setOnClickListener {
//                viewModel.addArticle(
//                    createEtArticleTitle.text.toString().trim(),
//                    createEtArticleContent.text.toString().trim(),
//                    createEtUrlImage.text.toString().trim(),
//                    (returnCreateCategoryInt(groupRadio.checkedRadioButtonId)).toString()
//                )
//            }
        }

    }



}