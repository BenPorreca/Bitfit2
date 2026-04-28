package com.example.bitfit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LogFragment : Fragment() {

    private lateinit var db: FoodDatabase
    private lateinit var adapter: FoodAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_log, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = FoodDatabase.getDatabase(requireContext())

        adapter = FoodAdapter { food ->
            lifecycleScope.launch {
                db.foodDao().deleteFood(food)
            }
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val tvTotal = view.findViewById<MaterialTextView>(R.id.tvTotalCalories)

        lifecycleScope.launch {
            db.foodDao().getAllFoods().collectLatest { foods ->
                adapter.submitList(foods)
                val total = foods.sumOf { it.calories }
                tvTotal.text = "Total: $total Calories"
            }
        }

        view.findViewById<MaterialButton>(R.id.btnAddFood).setOnClickListener {
            AddFoodDialogFragment { name, calories ->
                lifecycleScope.launch {
                    db.foodDao().insertFood(FoodEntity(name = name, calories = calories))
                }
            }.show(parentFragmentManager, "AddFoodDialog")
        }
    }
}