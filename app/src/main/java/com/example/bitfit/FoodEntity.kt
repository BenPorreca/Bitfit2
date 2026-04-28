package com.example.bitfit

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_table")
data class FoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val calories: Int
)