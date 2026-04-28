package com.example.bitfit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class FoodAdapter(
    private val onDeleteClick: (FoodEntity) -> Unit
) : ListAdapter<FoodEntity, FoodAdapter.FoodViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<FoodEntity>() {
        override fun areItemsTheSame(oldItem: FoodEntity, newItem: FoodEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: FoodEntity, newItem: FoodEntity) =
            oldItem == newItem
    }

    inner class FoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.tvFoodName)
        val caloriesText: TextView = view.findViewById(R.id.tvCalories)
        val deleteBtn: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = getItem(position)
        holder.nameText.text = food.name
        holder.caloriesText.text = "${food.calories}\nCalories"
        holder.deleteBtn.setOnClickListener { onDeleteClick(food) }
    }
}