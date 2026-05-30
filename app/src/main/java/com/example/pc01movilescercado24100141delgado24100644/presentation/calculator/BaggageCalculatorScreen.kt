package com.example.pc01movilescercado24100141delgado24100644.presentation.calculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.util.Locale

private const val NATIONAL_LIMIT_KG = 23.0
private const val INTERNATIONAL_LIMIT_KG = 32.0

internal enum class FlightType(val label: String, val limitKg: Double) {
    Nacional("Nacional", NATIONAL_LIMIT_KG),
    Internacional("Internacional", INTERNATIONAL_LIMIT_KG)
}

internal data class BaggageCalculationResult(
    val weightKg: Double,
    val limitKg: Double,
    val excessKg: Double,
    val complies: Boolean,
    val flightType: FlightType
)

internal sealed class BaggageCalculationState {
    data class Error(val message: String) : BaggageCalculationState()
    data class Success(val result: BaggageCalculationResult) : BaggageCalculationState()
}

internal fun calculateBaggage(weightInput: String, flightType: FlightType): BaggageCalculationState {
    val normalized = weightInput.trim().replace(',', '.')

    if (normalized.isEmpty()) {
        return BaggageCalculationState.Error("El campo es obligatorio")
    }

    val weight = normalized.toDoubleOrNull()
        ?: return BaggageCalculationState.Error("Debe ingresar un valor numérico")

    if (weight <= 0.0) {
        return BaggageCalculationState.Error("El peso debe ser mayor a cero")
    }

    val complies = weight <= flightType.limitKg
    val excessKg = if (complies) 0.0 else weight - flightType.limitKg

    return BaggageCalculationState.Success(
        BaggageCalculationResult(
            weightKg = weight,
            limitKg = flightType.limitKg,
            excessKg = excessKg,
            complies = complies,
            flightType = flightType
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaggageCalculatorScreen(navController: NavController) {
    val focusManager = LocalFocusManager.current

    var weightInput by remember { mutableStateOf("") }
    var selectedFlightType by remember { mutableStateOf(FlightType.Nacional) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var calculationState by remember { mutableStateOf<BaggageCalculationState?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calculadora de Equipaje") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Ingresa el peso de la maleta y selecciona el tipo de vuelo.",
                style = MaterialTheme.typography.bodyLarge
            )

            OutlinedTextField(
                value = weightInput,
                onValueChange = {
                    weightInput = it
                    errorMessage = null
                },
                label = { Text("Peso de la maleta") },
                placeholder = { Text("Ej: 18.5") },
                singleLine = true,
                isError = errorMessage != null,
                supportingText = {
                    Text(errorMessage ?: "Ingrese un valor en kg")
                },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Tipo de vuelo",
                    fontWeight = FontWeight.Bold
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedFlightType == FlightType.Nacional,
                        onClick = { selectedFlightType = FlightType.Nacional }
                    )
                    Text(text = "Nacional (máximo 23 kg)")
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedFlightType == FlightType.Internacional,
                        onClick = { selectedFlightType = FlightType.Internacional }
                    )
                    Text(text = "Internacional (máximo 32 kg)")
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        val result = calculateBaggage(weightInput, selectedFlightType)
                        calculationState = result
                        errorMessage = when (result) {
                            is BaggageCalculationState.Error -> result.message
                            is BaggageCalculationState.Success -> null
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Calcular")
                }

                OutlinedButton(
                    onClick = {
                        weightInput = ""
                        selectedFlightType = FlightType.Nacional
                        errorMessage = null
                        calculationState = null
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Limpiar")
                }
            }

            when (val state = calculationState) {
                is BaggageCalculationState.Success -> {
                    val result = state.result
                    Text(
                        text = if (result.complies) {
                            "Cumple con el límite permitido"
                        } else {
                            "Excede el límite permitido"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (result.complies) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                    Text(text = "Tipo de vuelo: ${result.flightType.label}")
                    Text(text = "Peso ingresado: ${formatKg(result.weightKg)} kg")
                    Text(text = "Límite permitido: ${formatKg(result.limitKg)} kg")
                    Text(
                        text = "Kg excedidos: ${formatKg(result.excessKg)} kg",
                        color = if (result.complies) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.error
                    )
                }

                is BaggageCalculationState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Start
                    )
                }

                null -> Unit
            }

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al menú principal")
            }
        }
    }
}

private fun formatKg(value: Double): String {
    return String.format(Locale.US, "%.2f", value)
}

