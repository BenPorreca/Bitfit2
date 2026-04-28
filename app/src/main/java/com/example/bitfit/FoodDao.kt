package com.example.bitfit

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {

    @Query("SELECT * FROM food_table ORDER BY id DESC")
    fun getAllFoods(): Flow<List<FoodEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: FoodEntity)

    @Delete
    suspend fun deleteFood(food: FoodEntity)
}