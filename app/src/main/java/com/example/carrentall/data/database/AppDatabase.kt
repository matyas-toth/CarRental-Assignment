package com.example.carrentall.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.carrentall.data.dao.CarItemDao
import com.example.carrentall.data.model.CarItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [CarItem::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun carItemDao(): CarItemDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "car_rental_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                
                // Pre-populate database with sample data on first creation
                CoroutineScope(Dispatchers.IO).launch {
                    val dao = instance.carItemDao()
                    // Only populate if database is empty
                    if (dao.getCarById(1) == null) {
                        populateSampleData(dao)
                    }
                }
                
                INSTANCE = instance
                instance
            }
        }
        
        private suspend fun populateSampleData(dao: CarItemDao) {
            val sampleCars = listOf(
                CarItem(
                    brand = "Toyota",
                    model = "Camry",
                    year = 2023,
                    color = "Silver",
                    licensePlate = "ABC-1234",
                    dailyRate = 45.99,
                    isRented = false,
                    notes = "Fuel efficient sedan, perfect for city driving"
                ),
                CarItem(
                    brand = "Ford",
                    model = "Mustang",
                    year = 2022,
                    color = "Red",
                    licensePlate = "XYZ-5678",
                    dailyRate = 89.99,
                    isRented = true,
                    notes = "Sports car with powerful V8 engine"
                ),
                CarItem(
                    brand = "Honda",
                    model = "Civic",
                    year = 2024,
                    color = "Blue",
                    licensePlate = "DEF-9012",
                    dailyRate = 39.99,
                    isRented = false,
                    notes = "Compact and reliable daily driver"
                ),
                CarItem(
                    brand = "Tesla",
                    model = "Model 3",
                    year = 2023,
                    color = "White",
                    licensePlate = "ELE-2024",
                    dailyRate = 129.99,
                    isRented = false,
                    notes = "Electric vehicle with autopilot features"
                ),
                CarItem(
                    brand = "Chevrolet",
                    model = "Suburban",
                    year = 2023,
                    color = "Black",
                    licensePlate = "SUV-7890",
                    dailyRate = 95.00,
                    isRented = true,
                    notes = "Large SUV, great for family trips"
                )
            )
            
            sampleCars.forEach { car ->
                dao.insertCar(car)
            }
        }
    }
}

