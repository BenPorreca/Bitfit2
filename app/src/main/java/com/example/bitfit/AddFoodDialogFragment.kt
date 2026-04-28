package com.example.bitfit

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class AddFoodDialogFragment(
    private val onFoodAdded: (String, Int) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = layoutInflater.inflate(R.layout.dialog_add_food, null)
        val etName = view.findViewById<EditText>(R.id.etFoodName)
        val etCalories = view.findViewById<EditText>(R.id.etCalories)

        return AlertDialog.Builder(requireContext())
            .setTitle("Add Food")
            .setView(view)
            .setPositiveButton("Add") { _, _ ->
                val name = etName.text.toString().trim()
                val caloriesStr = etCalories.text.toString().trim()
                if (name.isEmpty() || caloriesStr.isEmpty()) {
                    Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                } else {
                    val calories = caloriesStr.toIntOrNull()
                    if (calories == null || calories < 0) {
                        Toast.makeText(requireContext(), "Enter a valid calorie amount", Toast.LENGTH_SHORT).show()
                    } else {
                        onFoodAdded(name, calories)
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
    }
}