package com.example.carrentall.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "car_items")
data class CarItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    
    @ColumnInfo(name = "brand")
    val brand: String,
    
    @ColumnInfo(name = "model")
    val model: String,
    
    @ColumnInfo(name = "year")
    val year: Int,
    
    @ColumnInfo(name = "color")
    val color: String,
    
    @ColumnInfo(name = "license_plate")
    val licensePlate: String,
    
    @ColumnInfo(name = "daily_rate")
    val dailyRate: Double,
    
    @ColumnInfo(name = "is_rented")
    val isRented: Boolean = false,
    
    @ColumnInfo(name = "notes")
    val notes: String = ""
) {
    companion object {
        // Validation result class
        data class ValidationResult(
            val isValid: Boolean,
            val errorMessage: String = ""
        )
        
        fun validateBrand(brand: String): ValidationResult {
            return when {
                brand.isBlank() -> ValidationResult(false, "Brand cannot be empty")
                brand.length < 2 -> ValidationResult(false, "Brand must be at least 2 characters")
                brand.length > 50 -> ValidationResult(false, "Brand must be at most 50 characters")
                else -> ValidationResult(true)
            }
        }
        
        fun validateModel(model: String): ValidationResult {
            return when {
                model.isBlank() -> ValidationResult(false, "Model cannot be empty")
                model.length < 1 -> ValidationResult(false, "Model must be at least 1 character")
                model.length > 50 -> ValidationResult(false, "Model must be at most 50 characters")
                else -> ValidationResult(true)
            }
        }
        
        fun validateYear(year: Int): ValidationResult {
            val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
            return when {
                year < 1900 -> ValidationResult(false, "Year must be 1900 or later")
                year > currentYear + 1 -> ValidationResult(false, "Year cannot be in the future")
                else -> ValidationResult(true)
            }
        }
        
        fun validateColor(color: String): ValidationResult {
            return when {
                color.isBlank() -> ValidationResult(false, "Color cannot be empty")
                color.length < 2 -> ValidationResult(false, "Color must be at least 2 characters")
                color.length > 30 -> ValidationResult(false, "Color must be at most 30 characters")
                else -> ValidationResult(true)
            }
        }
        
        fun validateLicensePlate(licensePlate: String): ValidationResult {
            return when {
                licensePlate.isBlank() -> ValidationResult(false, "License plate cannot be empty")
                licensePlate.length < 5 -> ValidationResult(false, "License plate must be at least 5 characters")
                licensePlate.length > 10 -> ValidationResult(false, "License plate must be at most 10 characters")
                !licensePlate.matches(Regex("^[A-Za-z0-9-]+$")) -> ValidationResult(false, "License plate can only contain letters, numbers, and hyphens")
                else -> ValidationResult(true)
            }
        }
        
        fun validateDailyRate(dailyRate: Double): ValidationResult {
            return when {
                dailyRate <= 0 -> ValidationResult(false, "Daily rate must be greater than 0")
                dailyRate > 10000 -> ValidationResult(false, "Daily rate cannot exceed $10,000")
                else -> ValidationResult(true)
            }
        }
        
        fun validateAll(
            brand: String,
            model: String,
            year: Int,
            color: String,
            licensePlate: String,
            dailyRate: Double
        ): ValidationResult {
            val validations = listOf(
                validateBrand(brand),
                validateModel(model),
                validateYear(year),
                validateColor(color),
                validateLicensePlate(licensePlate),
                validateDailyRate(dailyRate)
            )
            
            val firstError = validations.firstOrNull { !it.isValid }
            return firstError ?: ValidationResult(true)
        }
    }
}

