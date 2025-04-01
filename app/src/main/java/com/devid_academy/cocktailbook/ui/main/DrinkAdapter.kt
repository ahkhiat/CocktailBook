package com.devid_academy.cocktailbook.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devid_academy.cocktailbook.data.room.model.DrinkLiteModel
import com.devid_academy.cocktailbook.databinding.ItemRvDrinkBinding
import com.devid_academy.cocktailbook.utils.picassoInsert

class DrinkAdapter(
    private val onItemClick: (DrinkLiteModel) -> Unit
): ListAdapter<DrinkLiteModel,
        DrinkAdapter.DrinkHolder>(ContactDiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DrinkHolder
    {
        val binding = ItemRvDrinkBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DrinkHolder(binding)
    }
    override fun onBindViewHolder(holder: DrinkAdapter.DrinkHolder, position: Int) {
        val drink = getItem(position)
        with(holder.binding) {
            itemRvTvTitle.text = drink.strDrink

            if (!drink.strDrinkThumb.isNullOrEmpty()) {
                picassoInsert(drink.strDrinkThumb,itemRvIv)
            }

            itemRvDrink.setOnClickListener {
                onItemClick(drink)
            }
        }
    }
    object ContactDiffCallback : DiffUtil.ItemCallback<DrinkLiteModel>() {
        override fun areItemsTheSame(oldItem: DrinkLiteModel, newItem: DrinkLiteModel): Boolean {
            return oldItem.idDrink == newItem.idDrink && oldItem.isMine == newItem.isMine
        }

        override fun areContentsTheSame(oldItem: DrinkLiteModel, newItem: DrinkLiteModel): Boolean {
            return oldItem == newItem
        }
    }
    // méthode 1
    class DrinkHolder(val binding: ItemRvDrinkBinding): RecyclerView.ViewHolder(binding.root)

    // méthode 2
//    inner class ArticleHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
//        val binding = ItemRvArticleBinding.bind(itemView)
//    }

}