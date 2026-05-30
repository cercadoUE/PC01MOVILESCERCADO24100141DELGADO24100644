package com.example.pc01movilescercado24100141delgado24100644.presentation.budget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class AccommodationType(val name: String, val factor: Double)

private val accommodationTypes = listOf(
    AccommodationType("Económico", 0.8),
    AccommodationType("Estándar", 1.0),
    AccommodationType("Premium", 1.5)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetPlannerScreen(navController: NavController) {
    var daysText by remember { mutableStateOf("") }
    var budgetText by remember { mutableStateOf("") }
    var selectedAccommodation by remember { mutableStateOf(accommodationTypes[0]) }
    var expanded by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf<Double?>(null) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Planificador de Presupuesto",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = daysText,
            onValueChange = { daysText = it },
            label = { Text("Cantidad de días") },
            placeholder = { Text("Ej: 7") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = budgetText,
            onValueChange = { budgetText = it },
            label = { Text("Presupuesto diario") },
            placeholder = { Text("Ej: 100.50") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedAccommodation.name,
                onValueChange = {},
                readOnly = true,
                label = { Text("Tipo de alojamiento") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                accommodationTypes.forEach { type ->
                    DropdownMenuItem(
                        text = { Text("${type.name} (${type.factor})") },
                        onClick = {
                            selectedAccommodation = type
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Factor aplicado: ${selectedAccommodation.factor}",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                errorMessage = ""
                result = null
                val days = daysText.toDoubleOrNull()
                val budget = budgetText.toDoubleOrNull()
                when {
                    daysText.isBlank() || budgetText.isBlank() ->
                        errorMessage = "Todos los campos son obligatorios"
                    days == null || budget == null ->
                        errorMessage = "Ingrese valores numéricos válidos"
                    days <= 0 ->
                        errorMessage = "Los días deben ser mayores a cero"
                    budget <= 0 ->
                        errorMessage = "El presupuesto debe ser mayor a cero"
                    else -> result = days * budget * selectedAccommodation.factor
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calcular presupuesto")
        }

        if (errorMessage.isNotBlank()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }

        if (result != null) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Presupuesto total: S/ ${"%.2f".format(result)}",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = buildScenarioMessage(
                    daysText.toDoubleOrNull() ?: 0.0,
                    budgetText.toDoubleOrNull() ?: 0.0,
                    selectedAccommodation
                ),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("Volver al menú")
        }
    }
}

private fun buildScenarioMessage(days: Double, dailyBudget: Double, accommodation: AccommodationType): String {
    val scenario = when (accommodation.factor) {
        0.8 -> "viaje económico con alojamiento básico"
        1.0 -> "viaje estándar con alojamiento regular"
        1.5 -> "viaje premium con alojamiento de lujo"
        else -> "viaje personalizado"
    }
    return "Para $days días con un presupuesto diario de S/ ${"%.2f".format(dailyBudget)} " +
            "en un ${accommodation.name.lowercase()}, " +
            "el presupuesto total estimado para un $scenario es de " +
            "S/ ${"%.2f".format(days * dailyBudget * accommodation.factor)}."
}
