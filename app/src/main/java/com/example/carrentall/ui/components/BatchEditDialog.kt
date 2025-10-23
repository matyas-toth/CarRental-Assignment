package com.example.carrentall.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun BatchEditDialog(
    selectedCount: Int,
    onDismiss: () -> Unit,
    onConfirm: (dailyRate: Double?, isRented: Boolean?, color: String?) -> Unit
) {
    var dailyRateText by remember { mutableStateOf("") }
    var colorText by remember { mutableStateOf("") }
    var rentalStatus by remember { mutableStateOf<Boolean?>(null) }
    
    var dailyRateError by remember { mutableStateOf<String?>(null) }
    var colorError by remember { mutableStateOf<String?>(null) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Batch Edit $selectedCount Car(s)")
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Leave fields empty to keep existing values",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Daily Rate
                OutlinedTextField(
                    value = dailyRateText,
                    onValueChange = {
                        dailyRateText = it
                        dailyRateError = null
                    },
                    label = { Text("New Daily Rate ($)") },
                    leadingIcon = {
                        Icon(Icons.Default.AttachMoney, contentDescription = null)
                    },
                    placeholder = { Text("Leave empty to skip") },
                    isError = dailyRateError != null,
                    supportingText = dailyRateError?.let { { Text(it) } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Color
                OutlinedTextField(
                    value = colorText,
                    onValueChange = {
                        colorText = it
                        colorError = null
                    },
                    label = { Text("New Color") },
                    leadingIcon = {
                        Icon(Icons.Default.ColorLens, contentDescription = null)
                    },
                    placeholder = { Text("Leave empty to skip") },
                    isError = colorError != null,
                    supportingText = colorError?.let { { Text(it) } },
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Rental Status
                Text(
                    text = "Rental Status",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = rentalStatus == null,
                        onClick = { rentalStatus = null },
                        label = { Text("No Change") },
                        modifier = Modifier.weight(1f)
                    )
                    
                    FilterChip(
                        selected = rentalStatus == false,
                        onClick = { rentalStatus = false },
                        label = { Text("Available") },
                        leadingIcon = {
                            if (rentalStatus == false) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    
                    FilterChip(
                        selected = rentalStatus == true,
                        onClick = { rentalStatus = true },
                        label = { Text("Rented") },
                        leadingIcon = {
                            if (rentalStatus == true) {
                                Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(18.dp))
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    var hasError = false
                    
                    val dailyRate = if (dailyRateText.isBlank()) {
                        null
                    } else {
                        val rate = dailyRateText.toDoubleOrNull()
                        if (rate == null) {
                            dailyRateError = "Please enter a valid rate"
                            hasError = true
                            null
                        } else {
                            val validation = com.example.carrentall.data.model.CarItem.validateDailyRate(rate)
                            if (!validation.isValid) {
                                dailyRateError = validation.errorMessage
                                hasError = true
                                null
                            } else {
                                rate
                            }
                        }
                    }
                    
                    val color = if (colorText.isBlank()) {
                        null
                    } else {
                        val validation = com.example.carrentall.data.model.CarItem.validateColor(colorText)
                        if (!validation.isValid) {
                            colorError = validation.errorMessage
                            hasError = true
                            null
                        } else {
                            colorText.trim()
                        }
                    }
                    
                    if (!hasError) {
                        onConfirm(dailyRate, rentalStatus, color)
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Update All")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

