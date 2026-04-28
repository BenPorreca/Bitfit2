package com.example.bitfit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var db: FoodDatabase
    private lateinit var adapter: FoodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = FoodDatabase.getDatabase(this)

        adapter = FoodAdapter { food ->
            lifecycleScope.launch {
                db.foodDao().deleteFood(food)
            }
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val tvTotal = findViewById<MaterialTextView>(R.id.tvTotalCalories)

        lifecycleScope.launch {
            db.foodDao().getAllFoods().collectLatest { foods ->
                adapter.submitList(foods)
                val total = foods.sumOf { it.calories }
                tvTotal.text = "Total: $total Calories"
            }
        }

        val btnAdd = findViewById<MaterialButton>(R.id.btnAddFood)
        btnAdd.setOnClickListener {
            val dialog = AddFoodDialogFragment { name, calories ->
                lifecycleScope.launch {
                    db.foodDao().insertFood(FoodEntity(name = name, calories = calories))
                }
            }
            dialog.show(supportFragmentManager, "AddFoodDialog")
        }
    }
}