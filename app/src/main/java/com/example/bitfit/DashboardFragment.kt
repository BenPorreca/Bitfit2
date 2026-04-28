package com.example.bitfit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {

    private lateinit var db: FoodDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_dashboard, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = FoodDatabase.getDatabase(requireContext())

        val tvAverage = view.findViewById<TextView>(R.id.tvAverage)
        val tvMin = view.findViewById<TextView>(R.id.tvMin)
        val tvMax = view.findViewById<TextView>(R.id.tvMax)

        lifecycleScope.launch {
            db.foodDao().getAllFoods().collectLatest { foods ->
                if (foods.isEmpty()) {
                    tvAverage.text = "--"
                    tvMin.text = "--"
                    tvMax.text = "--"
                } else {
                    val calories = foods.map { it.calories }
                    tvAverage.text = "${calories.average().toInt()}"
                    tvMin.text = "${calories.min()}"
                    tvMax.text = "${calories.max()}"
                }
            }
        }

        view.findViewById<MaterialButton>(R.id.btnClearData).setOnClickListener {
            lifecycleScope.launch {
                db.foodDao().deleteAll()
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