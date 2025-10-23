package com.example.carrentall.data.repository

import com.example.carrentall.data.dao.CarItemDao
import com.example.carrentall.data.model.CarItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CarRepository(private val carItemDao: CarItemDao) {
    
    // Observe all cars
    fun getAllCars(): Flow<List<CarItem>> = carItemDao.getAllCars()
    
    // Observe available cars
    fun getAvailableCars(): Flow<List<CarItem>> = carItemDao.getAvailableCars()
    
    // Observe rented cars
    fun getRentedCars(): Flow<List<CarItem>> = carItemDao.getRentedCars()
    
    // Get single car
    suspend fun getCarById(carId: Long): CarItem? = withContext(Dispatchers.IO) {
        carItemDao.getCarById(carId)
    }
    
    // Insert car
    suspend fun insertCar(car: CarItem): Long = withContext(Dispatchers.IO) {
        carItemDao.insertCar(car)
    }
    
    // Update car
    suspend fun updateCar(car: CarItem) = withContext(Dispatchers.IO) {
        carItemDao.updateCar(car)
    }
    
    // Delete single car
    suspend fun deleteCar(car: CarItem) = withContext(Dispatchers.IO) {
        carItemDao.deleteCar(car)
    }
    
    // Delete multiple cars
    suspend fun deleteCars(cars: List<CarItem>) = withContext(Dispatchers.IO) {
        carItemDao.deleteCars(cars)
    }
    
    // Delete all cars
    suspend fun deleteAllCars() = withContext(Dispatchers.IO) {
        carItemDao.deleteAllCars()
    }
    
    // Update multiple cars
    suspend fun updateMultipleCars(cars: List<CarItem>) = withContext(Dispatchers.IO) {
        carItemDao.updateMultipleCars(cars)
    }
    
    // Statistics
    fun getCarsCount(): Flow<Int> = carItemDao.getCarsCount()
    
    fun getAvailableCarsCount(): Flow<Int> = carItemDao.getAvailableCarsCount()
    
    fun getRentedCarsCount(): Flow<Int> = carItemDao.getRentedCarsCount()
    
    fun getTotalDailyRevenue(): Flow<Double?> = carItemDao.getTotalDailyRevenue()
}

