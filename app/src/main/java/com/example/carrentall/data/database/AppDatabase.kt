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
                    color = "Ezüst",
                    licensePlate = "ABC-1234",
                    dailyRate = 10000.00,
                    isRented = false,
                    notes = "Alacsony fogyasztású sedan, városon belüli közlekedéshez tökéletes"
                ),
                CarItem(
                    brand = "Ford",
                    model = "Mustang",
                    year = 2022,
                    color = "Vörös",
                    licensePlate = "XYZ-5678",
                    dailyRate = 25000.00,
                    isRented = true,
                    notes = "Sportautó V8-as motorral"
                ),
                CarItem(
                    brand = "Honda",
                    model = "Civic",
                    year = 2024,
                    color = "Kék",
                    licensePlate = "DEF-9012",
                    dailyRate = 8999.99,
                    isRented = false,
                    notes = "Kompakt és megbízható mindennapi autó"
                ),
                CarItem(
                    brand = "Tesla",
                    model = "Model 3",
                    year = 2023,
                    color = "Fehér",
                    licensePlate = "ELE-2024",
                    dailyRate = 32000.99,
                    isRented = false,
                    notes = "Elektromos autó önvezetéssel"
                ),
                CarItem(
                    brand = "Chevrolet",
                    model = "Suburban",
                    year = 2023,
                    color = "Fekete",
                    licensePlate = "SUV-7890",
                    dailyRate = 19999.00,
                    isRented = true,
                    notes = "Nagy SUV családi kirándulásokhoz"
                )
            )
            
            sampleCars.forEach { car ->
                dao.insertCar(car)
            }
        }
    }
}

