package com.example.carrentall.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.carrentall.data.database.AppDatabase
import com.example.carrentall.data.model.CarItem
import com.example.carrentall.data.repository.CarRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CarViewModel(application: Application) : AndroidViewModel(application) {
    
    // Initialize repository and database
    private val repository: CarRepository = CarRepository(
        AppDatabase.getInstance(application).carItemDao()
    )
    
    // Filter state
    enum class CarFilter {
        ALL, AVAILABLE, RENTED
    }
    
    private val _currentFilter = MutableStateFlow(CarFilter.ALL)
    val currentFilter: StateFlow<CarFilter> = _currentFilter.asStateFlow()
    
    // Sort state
    enum class SortOption {
        BRAND, YEAR, DAILY_RATE, RENTAL_STATUS
    }
    
    private val _currentSort = MutableStateFlow(SortOption.BRAND)
    val currentSort: StateFlow<SortOption> = _currentSort.asStateFlow()
    
    // Search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    // Cars from database
    private val allCarsFromDb: Flow<List<CarItem>> = repository.getAllCars()
    
    // Filtered and sorted cars
    val cars: StateFlow<List<CarItem>> = combine(
        allCarsFromDb,
        _currentFilter,
        _currentSort,
        _searchQuery
    ) { cars, filter, sort, query ->
        var filteredCars = when (filter) {
            CarFilter.ALL -> cars
            CarFilter.AVAILABLE -> cars.filter { !it.isRented }
            CarFilter.RENTED -> cars.filter { it.isRented }
        }
        
        // Apply search filter
        if (query.isNotBlank()) {
            filteredCars = filteredCars.filter {
                it.brand.contains(query, ignoreCase = true) ||
                it.model.contains(query, ignoreCase = true) ||
                it.licensePlate.contains(query, ignoreCase = true)
            }
        }
        
        // Apply sorting
        when (sort) {
            SortOption.BRAND -> filteredCars.sortedBy { it.brand.lowercase() }
            SortOption.YEAR -> filteredCars.sortedByDescending { it.year }
            SortOption.DAILY_RATE -> filteredCars.sortedByDescending { it.dailyRate }
            SortOption.RENTAL_STATUS -> filteredCars.sortedByDescending { it.isRented }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    
    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // Error message
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    // Success message
    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()
    
    // Multi-select mode
    private val _isMultiSelectMode = MutableStateFlow(false)
    val isMultiSelectMode: StateFlow<Boolean> = _isMultiSelectMode.asStateFlow()
    
    private val _selectedCarIds = MutableStateFlow<Set<Long>>(emptySet())
    val selectedCarIds: StateFlow<Set<Long>> = _selectedCarIds.asStateFlow()
    
    // Statistics
    val totalCarsCount: StateFlow<Int> = repository.getCarsCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    
    val availableCarsCount: StateFlow<Int> = repository.getAvailableCarsCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    
    val rentedCarsCount: StateFlow<Int> = repository.getRentedCarsCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    
    val totalDailyRevenue: StateFlow<Double> = repository.getTotalDailyRevenue()
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
    
    // Filter functions
    fun setFilter(filter: CarFilter) {
        _currentFilter.value = filter
    }
    
    fun setSort(sort: SortOption) {
        _currentSort.value = sort
    }
    
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    // Multi-select functions
    fun toggleMultiSelectMode() {
        _isMultiSelectMode.value = !_isMultiSelectMode.value
        if (!_isMultiSelectMode.value) {
            _selectedCarIds.value = emptySet()
        }
    }
    
    fun toggleCarSelection(carId: Long) {
        val currentSelection = _selectedCarIds.value.toMutableSet()
        if (currentSelection.contains(carId)) {
            currentSelection.remove(carId)
        } else {
            currentSelection.add(carId)
        }
        _selectedCarIds.value = currentSelection
    }
    
    fun selectAllCars() {
        _selectedCarIds.value = cars.value.map { it.id }.toSet()
    }
    
    fun deselectAllCars() {
        _selectedCarIds.value = emptySet()
    }
    
    fun exitMultiSelectMode() {
        _isMultiSelectMode.value = false
        _selectedCarIds.value = emptySet()
    }
    
    // CRUD operations
    fun addCar(
        brand: String,
        model: String,
        year: Int,
        color: String,
        licensePlate: String,
        dailyRate: Double,
        isRented: Boolean,
        notes: String
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                
                // Validate all fields
                val validationResult = CarItem.validateAll(
                    brand, model, year, color, licensePlate, dailyRate
                )
                
                if (!validationResult.isValid) {
                    _errorMessage.value = validationResult.errorMessage
                    return@launch
                }
                
                val car = CarItem(
                    brand = brand.trim(),
                    model = model.trim(),
                    year = year,
                    color = color.trim(),
                    licensePlate = licensePlate.trim().uppercase(),
                    dailyRate = dailyRate,
                    isRented = isRented,
                    notes = notes.trim()
                )
                
                repository.insertCar(car)
                _successMessage.value = "Car added successfully!"
            } catch (e: Exception) {
                _errorMessage.value = "Failed to add car: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updateCar(car: CarItem) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                
                // Validate all fields
                val validationResult = CarItem.validateAll(
                    car.brand, car.model, car.year, 
                    car.color, car.licensePlate, car.dailyRate
                )
                
                if (!validationResult.isValid) {
                    _errorMessage.value = validationResult.errorMessage
                    return@launch
                }
                
                repository.updateCar(car)
                _successMessage.value = "Car updated successfully!"
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update car: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun deleteCar(car: CarItem) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.deleteCar(car)
                _successMessage.value = "Car deleted successfully!"
            } catch (e: Exception) {
                _errorMessage.value = "Failed to delete car: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun deleteSelectedCars() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val carsToDelete = cars.value.filter { _selectedCarIds.value.contains(it.id) }
                repository.deleteCars(carsToDelete)
                _successMessage.value = "${carsToDelete.size} car(s) deleted successfully!"
                exitMultiSelectMode()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to delete cars: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun deleteAllCars() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.deleteAllCars()
                _successMessage.value = "All cars deleted successfully!"
                exitMultiSelectMode()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to delete all cars: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updateSelectedCars(
        dailyRate: Double? = null,
        isRented: Boolean? = null,
        color: String? = null
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val carsToUpdate = cars.value
                    .filter { _selectedCarIds.value.contains(it.id) }
                    .map { car ->
                        car.copy(
                            dailyRate = dailyRate ?: car.dailyRate,
                            isRented = isRented ?: car.isRented,
                            color = color ?: car.color
                        )
                    }
                
                repository.updateMultipleCars(carsToUpdate)
                _successMessage.value = "${carsToUpdate.size} car(s) updated successfully!"
                exitMultiSelectMode()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update cars: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun toggleCarRentalStatus(car: CarItem) {
        viewModelScope.launch {
            try {
                val updatedCar = car.copy(isRented = !car.isRented)
                repository.updateCar(updatedCar)
                _successMessage.value = if (updatedCar.isRented) {
                    "Car marked as rented"
                } else {
                    "Car marked as available"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update rental status: ${e.message}"
            }
        }
    }
    
    // Clear messages
    fun clearErrorMessage() {
        _errorMessage.value = null
    }
    
    fun clearSuccessMessage() {
        _successMessage.value = null
    }
}

