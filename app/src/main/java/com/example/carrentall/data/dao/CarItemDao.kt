package com.example.carrentall.data.dao

import androidx.room.*
import com.example.carrentall.data.model.CarItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CarItemDao {
    
    @Query("SELECT * FROM car_items ORDER BY brand ASC, model ASC")
    fun getAllCars(): Flow<List<CarItem>>
    
    @Query("SELECT * FROM car_items WHERE is_rented = 0 ORDER BY brand ASC, model ASC")
    fun getAvailableCars(): Flow<List<CarItem>>
    
    @Query("SELECT * FROM car_items WHERE is_rented = 1 ORDER BY brand ASC, model ASC")
    fun getRentedCars(): Flow<List<CarItem>>
    
    @Query("SELECT * FROM car_items WHERE id = :carId")
    suspend fun getCarById(carId: Long): CarItem?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCar(car: CarItem): Long
    
    @Update
    suspend fun updateCar(car: CarItem)
    
    @Delete
    suspend fun deleteCar(car: CarItem)
    
    @Delete
    suspend fun deleteCars(cars: List<CarItem>)
    
    @Query("DELETE FROM car_items")
    suspend fun deleteAllCars()
    
    @Update
    suspend fun updateMultipleCars(cars: List<CarItem>)
    
    @Query("SELECT COUNT(*) FROM car_items")
    fun getCarsCount(): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM car_items WHERE is_rented = 0")
    fun getAvailableCarsCount(): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM car_items WHERE is_rented = 1")
    fun getRentedCarsCount(): Flow<Int>
    
    @Query("SELECT SUM(daily_rate) FROM car_items WHERE is_rented = 1")
    fun getTotalDailyRevenue(): Flow<Double?>
}

