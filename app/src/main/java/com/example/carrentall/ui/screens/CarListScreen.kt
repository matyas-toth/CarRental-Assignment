package com.example.carrentall.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carrentall.data.model.CarItem
import com.example.carrentall.ui.components.*
import com.example.carrentall.ui.viewmodel.CarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarListScreen(
    viewModel: CarViewModel = viewModel()
) {
    val cars by viewModel.cars.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()
    val isMultiSelectMode by viewModel.isMultiSelectMode.collectAsState()
    val selectedCarIds by viewModel.selectedCarIds.collectAsState()
    val currentFilter by viewModel.currentFilter.collectAsState()
    val currentSort by viewModel.currentSort.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    
    // Statistics
    val totalCars by viewModel.totalCarsCount.collectAsState()
    val availableCars by viewModel.availableCarsCount.collectAsState()
    val rentedCars by viewModel.rentedCarsCount.collectAsState()
    val totalRevenue by viewModel.totalDailyRevenue.collectAsState()
    
    // Dialog states
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var carToEdit by remember { mutableStateOf<CarItem?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var carToDelete by remember { mutableStateOf<CarItem?>(null) }
    var showDeleteAllDialog by remember { mutableStateOf(false) }
    var showBatchEditDialog by remember { mutableStateOf(false) }
    var showBatchDeleteDialog by remember { mutableStateOf(false) }
    var showFilterMenu by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }
    
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Handle error and success messages
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
            viewModel.clearErrorMessage()
        }
    }
    
    LaunchedEffect(successMessage) {
        successMessage?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
            viewModel.clearSuccessMessage()
        }
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Car Rental Manager")
                        if (!isMultiSelectMode) {
                            Text(
                                text = "$totalCars cars • $availableCars available • $rentedCars rented",
                                style = MaterialTheme.typography.bodySmall
                            )
                        } else {
                            Text(
                                text = "${selectedCarIds.size} selected",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    if (isMultiSelectMode) {
                        // Multi-select actions
                        if (selectedCarIds.isNotEmpty()) {
                            IconButton(onClick = { showBatchEditDialog = true }) {
                                Icon(Icons.Default.Edit, contentDescription = "Batch Edit")
                            }
                            IconButton(onClick = { showBatchDeleteDialog = true }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete Selected")
                            }
                        }
                        
                        if (cars.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    if (selectedCarIds.size == cars.size) {
                                        viewModel.deselectAllCars()
                                    } else {
                                        viewModel.selectAllCars()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (selectedCarIds.size == cars.size) 
                                        Icons.Default.CheckBoxOutlineBlank 
                                    else 
                                        Icons.Default.CheckBox,
                                    contentDescription = "Select/Deselect All"
                                )
                            }
                        }
                        
                        IconButton(onClick = { viewModel.exitMultiSelectMode() }) {
                            Icon(Icons.Default.Close, contentDescription = "Exit Multi-Select")
                        }
                    } else {
                        // Normal actions
                        IconButton(onClick = { showSortMenu = true }) {
                            Icon(Icons.Default.Sort, contentDescription = "Sort")
                        }
                        
                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Sort by Brand") },
                                onClick = {
                                    viewModel.setSort(CarViewModel.SortOption.BRAND)
                                    showSortMenu = false
                                },
                                leadingIcon = {
                                    if (currentSort == CarViewModel.SortOption.BRAND) {
                                        Icon(Icons.Default.Check, contentDescription = null)
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Sort by Year") },
                                onClick = {
                                    viewModel.setSort(CarViewModel.SortOption.YEAR)
                                    showSortMenu = false
                                },
                                leadingIcon = {
                                    if (currentSort == CarViewModel.SortOption.YEAR) {
                                        Icon(Icons.Default.Check, contentDescription = null)
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Sort by Daily Rate") },
                                onClick = {
                                    viewModel.setSort(CarViewModel.SortOption.DAILY_RATE)
                                    showSortMenu = false
                                },
                                leadingIcon = {
                                    if (currentSort == CarViewModel.SortOption.DAILY_RATE) {
                                        Icon(Icons.Default.Check, contentDescription = null)
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Sort by Rental Status") },
                                onClick = {
                                    viewModel.setSort(CarViewModel.SortOption.RENTAL_STATUS)
                                    showSortMenu = false
                                },
                                leadingIcon = {
                                    if (currentSort == CarViewModel.SortOption.RENTAL_STATUS) {
                                        Icon(Icons.Default.Check, contentDescription = null)
                                    }
                                }
                            )
                        }
                        
                        IconButton(onClick = { showFilterMenu = true }) {
                            Icon(Icons.Default.FilterList, contentDescription = "Filter")
                        }
                        
                        DropdownMenu(
                            expanded = showFilterMenu,
                            onDismissRequest = { showFilterMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("All Cars") },
                                onClick = {
                                    viewModel.setFilter(CarViewModel.CarFilter.ALL)
                                    showFilterMenu = false
                                },
                                leadingIcon = {
                                    if (currentFilter == CarViewModel.CarFilter.ALL) {
                                        Icon(Icons.Default.Check, contentDescription = null)
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Available Only") },
                                onClick = {
                                    viewModel.setFilter(CarViewModel.CarFilter.AVAILABLE)
                                    showFilterMenu = false
                                },
                                leadingIcon = {
                                    if (currentFilter == CarViewModel.CarFilter.AVAILABLE) {
                                        Icon(Icons.Default.Check, contentDescription = null)
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Rented Only") },
                                onClick = {
                                    viewModel.setFilter(CarViewModel.CarFilter.RENTED)
                                    showFilterMenu = false
                                },
                                leadingIcon = {
                                    if (currentFilter == CarViewModel.CarFilter.RENTED) {
                                        Icon(Icons.Default.Check, contentDescription = null)
                                    }
                                }
                            )
                        }
                        
                        IconButton(onClick = { viewModel.toggleMultiSelectMode() }) {
                            Icon(Icons.Default.Checklist, contentDescription = "Multi-Select")
                        }
                        
                        if (totalCars > 0) {
                            IconButton(onClick = { showDeleteAllDialog = true }) {
                                Icon(Icons.Default.DeleteSweep, contentDescription = "Delete All")
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = !isMultiSelectMode,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = MaterialTheme.colorScheme.secondary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Car")
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column {
                // Search bar
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { viewModel.setSearchQuery(it) },
                    onSearch = { },
                    active = false,
                    onActiveChange = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text("Search by brand, model, or license plate...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear search")
                            }
                        }
                    }
                ) { }
                
                // Statistics card
                if (!isMultiSelectMode && totalCars > 0) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StatItem(
                                label = "Total",
                                value = totalCars.toString(),
                                icon = Icons.Default.DirectionsCar
                            )
                            StatItem(
                                label = "Available",
                                value = availableCars.toString(),
                                icon = Icons.Default.CheckCircle
                            )
                            StatItem(
                                label = "Rented",
                                value = rentedCars.toString(),
                                icon = Icons.Default.Lock
                            )
                            StatItem(
                                label = "Revenue/Day",
                                value = "$${String.format("%.0f", totalRevenue)}",
                                icon = Icons.Default.AttachMoney
                            )
                        }
                    }
                }
                
                // Car list
                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (cars.isEmpty()) {
                    EmptyState(
                        message = if (searchQuery.isNotEmpty()) {
                            "No cars match your search"
                        } else when (currentFilter) {
                            CarViewModel.CarFilter.AVAILABLE -> "No available cars"
                            CarViewModel.CarFilter.RENTED -> "No rented cars"
                            else -> "No cars in your fleet yet"
                        },
                        actionText = if (totalCars == 0) "Add Your First Car" else null,
                        onActionClick = if (totalCars == 0) { { showAddDialog = true } } else null
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(
                            items = cars,
                            key = { it.id }
                        ) { car ->
                            CarListItem(
                                car = car,
                                isSelected = selectedCarIds.contains(car.id),
                                isMultiSelectMode = isMultiSelectMode,
                                onToggleSelection = { viewModel.toggleCarSelection(car.id) },
                                onEdit = {
                                    carToEdit = car
                                    showEditDialog = true
                                },
                                onDelete = {
                                    carToDelete = car
                                    showDeleteDialog = true
                                },
                                onToggleRental = {
                                    viewModel.toggleCarRentalStatus(car)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
    
    // Add Dialog
    if (showAddDialog) {
        CarDialog(
            title = "Add New Car",
            onDismiss = { showAddDialog = false },
            onConfirm = { brand, model, year, color, licensePlate, dailyRate, isRented, notes ->
                viewModel.addCar(brand, model, year, color, licensePlate, dailyRate, isRented, notes)
                showAddDialog = false
            }
        )
    }
    
    // Edit Dialog
    if (showEditDialog && carToEdit != null) {
        CarDialog(
            title = "Edit Car",
            car = carToEdit,
            onDismiss = {
                showEditDialog = false
                carToEdit = null
            },
            onConfirm = { brand, model, year, color, licensePlate, dailyRate, isRented, notes ->
                carToEdit?.let { car ->
                    viewModel.updateCar(
                        car.copy(
                            brand = brand,
                            model = model,
                            year = year,
                            color = color,
                            licensePlate = licensePlate,
                            dailyRate = dailyRate,
                            isRented = isRented,
                            notes = notes
                        )
                    )
                }
                showEditDialog = false
                carToEdit = null
            }
        )
    }
    
    // Delete Confirmation Dialog
    if (showDeleteDialog && carToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                carToDelete = null
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = { Text("Delete Car?") },
            text = {
                Text("Are you sure you want to delete ${carToDelete?.brand} ${carToDelete?.model}? This action cannot be undone.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        carToDelete?.let { viewModel.deleteCar(it) }
                        showDeleteDialog = false
                        carToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        carToDelete = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
    
    // Batch Edit Dialog
    if (showBatchEditDialog) {
        BatchEditDialog(
            selectedCount = selectedCarIds.size,
            onDismiss = { showBatchEditDialog = false },
            onConfirm = { dailyRate, isRented, color ->
                viewModel.updateSelectedCars(dailyRate, isRented, color)
                showBatchEditDialog = false
            }
        )
    }
    
    // Batch Delete Confirmation Dialog
    if (showBatchDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showBatchDeleteDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = { Text("Delete Selected Cars?") },
            text = {
                Text("Are you sure you want to delete ${selectedCarIds.size} selected car(s)? This action cannot be undone.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteSelectedCars()
                        showBatchDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete All")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBatchDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    
    // Delete All Confirmation Dialog
    if (showDeleteAllDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAllDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.DeleteForever,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = { Text("Delete ALL Cars?") },
            text = {
                Text("⚠️ WARNING: This will permanently delete ALL $totalCars cars from the database. This action cannot be undone!")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteAllCars()
                        showDeleteAllDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete Everything")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAllDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

