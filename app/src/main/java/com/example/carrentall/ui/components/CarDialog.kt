package com.example.carrentall.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.carrentall.data.model.CarItem

@Composable
fun CarDialog(
    title: String,
    car: CarItem? = null,
    onDismiss: () -> Unit,
    onConfirm: (brand: String, model: String, year: Int, color: String, licensePlate: String, dailyRate: Double, isRented: Boolean, notes: String) -> Unit
) {
    var brand by remember { mutableStateOf(car?.brand ?: "") }
    var model by remember { mutableStateOf(car?.model ?: "") }
    var yearText by remember { mutableStateOf(car?.year?.toString() ?: "") }
    var color by remember { mutableStateOf(car?.color ?: "") }
    var licensePlate by remember { mutableStateOf(car?.licensePlate ?: "") }
    var dailyRateText by remember { mutableStateOf(car?.dailyRate?.toString() ?: "") }
    var isRented by remember { mutableStateOf(car?.isRented ?: false) }
    var notes by remember { mutableStateOf(car?.notes ?: "") }
    
    // Error states
    var brandError by remember { mutableStateOf<String?>(null) }
    var modelError by remember { mutableStateOf<String?>(null) }
    var yearError by remember { mutableStateOf<String?>(null) }
    var colorError by remember { mutableStateOf<String?>(null) }
    var licensePlateError by remember { mutableStateOf<String?>(null) }
    var dailyRateError by remember { mutableStateOf<String?>(null) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (car == null) Icons.Default.AddCircle else Icons.Default.Edit,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(title)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Brand
                OutlinedTextField(
                    value = brand,
                    onValueChange = {
                        brand = it
                        brandError = null
                    },
                    label = { Text("Márka *") },
                    leadingIcon = {
                        Icon(Icons.Default.DirectionsCar, contentDescription = null)
                    },
                    isError = brandError != null,
                    supportingText = brandError?.let { { Text(it) } },
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Model
                OutlinedTextField(
                    value = model,
                    onValueChange = {
                        model = it
                        modelError = null
                    },
                    label = { Text("Modell *") },
                    leadingIcon = {
                        Icon(Icons.Default.CarRental, contentDescription = null)
                    },
                    isError = modelError != null,
                    supportingText = modelError?.let { { Text(it) } },
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Year
                OutlinedTextField(
                    value = yearText,
                    onValueChange = {
                        yearText = it
                        yearError = null
                    },
                    label = { Text("Évjárat *") },
                    leadingIcon = {
                        Icon(Icons.Default.CalendarToday, contentDescription = null)
                    },
                    isError = yearError != null,
                    supportingText = yearError?.let { { Text(it) } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Color
                OutlinedTextField(
                    value = color,
                    onValueChange = {
                        color = it
                        colorError = null
                    },
                    label = { Text("Szín *") },
                    leadingIcon = {
                        Icon(Icons.Default.ColorLens, contentDescription = null)
                    },
                    isError = colorError != null,
                    supportingText = colorError?.let { { Text(it) } },
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // License Plate
                OutlinedTextField(
                    value = licensePlate,
                    onValueChange = {
                        licensePlate = it.uppercase()
                        licensePlateError = null
                    },
                    label = { Text("Rendszám *") },
                    leadingIcon = {
                        Icon(Icons.Default.CreditCard, contentDescription = null)
                    },
                    isError = licensePlateError != null,
                    supportingText = licensePlateError?.let { { Text(it) } },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Daily Rate
                OutlinedTextField(
                    value = dailyRateText,
                    onValueChange = {
                        dailyRateText = it
                        dailyRateError = null
                    },
                    label = { Text("Napidíj (HUF) *") },
                    leadingIcon = {
                        Icon(Icons.Default.AttachMoney, contentDescription = null)
                    },
                    isError = dailyRateError != null,
                    supportingText = dailyRateError?.let { { Text(it) } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Is Rented Switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isRented) Icons.Default.Lock else Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Kikölcsönözve",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Switch(
                        checked = isRented,
                        onCheckedChange = { isRented = it }
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Jegyzetek (Nem kötelező)") },
                    leadingIcon = {
                        Icon(Icons.Default.Notes, contentDescription = null)
                    },
                    minLines = 3,
                    maxLines = 5,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "* Kötelező mezők",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Validate all fields
                    var hasError = false
                    
                    val brandValidation = CarItem.validateBrand(brand)
                    if (!brandValidation.isValid) {
                        brandError = brandValidation.errorMessage
                        hasError = true
                    }
                    
                    val modelValidation = CarItem.validateModel(model)
                    if (!modelValidation.isValid) {
                        modelError = modelValidation.errorMessage
                        hasError = true
                    }
                    
                    val year = yearText.toIntOrNull()
                    if (year == null) {
                        yearError = "Please enter a valid year"
                        hasError = true
                    } else {
                        val yearValidation = CarItem.validateYear(year)
                        if (!yearValidation.isValid) {
                            yearError = yearValidation.errorMessage
                            hasError = true
                        }
                    }
                    
                    val colorValidation = CarItem.validateColor(color)
                    if (!colorValidation.isValid) {
                        colorError = colorValidation.errorMessage
                        hasError = true
                    }
                    
                    val licensePlateValidation = CarItem.validateLicensePlate(licensePlate)
                    if (!licensePlateValidation.isValid) {
                        licensePlateError = licensePlateValidation.errorMessage
                        hasError = true
                    }
                    
                    val dailyRate = dailyRateText.toDoubleOrNull()
                    if (dailyRate == null) {
                        dailyRateError = "Please enter a valid rate"
                        hasError = true
                    } else {
                        val dailyRateValidation = CarItem.validateDailyRate(dailyRate)
                        if (!dailyRateValidation.isValid) {
                            dailyRateError = dailyRateValidation.errorMessage
                            hasError = true
                        }
                    }
                    
                    if (!hasError && year != null && dailyRate != null) {
                        onConfirm(
                            brand.trim(),
                            model.trim(),
                            year,
                            color.trim(),
                            licensePlate.trim().uppercase(),
                            dailyRate,
                            isRented,
                            notes.trim()
                        )
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (car == null) "Autó Hozzáadása" else "Autó Frissítése")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Mégse")
            }
        }
    )
}

