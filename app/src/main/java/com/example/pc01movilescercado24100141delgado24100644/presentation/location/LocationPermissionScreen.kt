package com.example.pc01movilescercado24100141delgado24100644.presentation.location

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

private enum class PermissionStatus {
    PENDING, GRANTED, DENIED
}

@Composable
fun LocationPermissionScreen(navController: NavController) {
    var permissionStatus by remember { mutableStateOf(PermissionStatus.PENDING) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        permissionStatus = if (isGranted) PermissionStatus.GRANTED
        else PermissionStatus.DENIED
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Permiso de Ubicación",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = when (permissionStatus) {
                PermissionStatus.PENDING -> "Permiso pendiente de solicitud"
                PermissionStatus.GRANTED -> "Permiso concedido"
                PermissionStatus.DENIED -> "Permiso denegado"
            },
            style = MaterialTheme.typography.bodyLarge,
            color = when (permissionStatus) {
                PermissionStatus.GRANTED -> Color(0xFF4CAF50)
                PermissionStatus.DENIED -> MaterialTheme.colorScheme.error
                PermissionStatus.PENDING -> MaterialTheme.colorScheme.onSurface
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        ) {
            Text("Solicitar Permiso")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("Volver al menú principal")
        }
    }
}
