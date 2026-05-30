package com.example.pc01movilescercado24100141delgado24100644.presentation.location

import android.Manifest
import android.os.Build
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController

@Composable
fun LocationPermissionScreen(navController: NavController) {
    val context = LocalContext.current
    var permissionGranted by remember { mutableStateOf(false) }

    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.ACCESS_FINE_LOCATION
    } else {
        Manifest.permission.ACCESS_FINE_LOCATION
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        permissionGranted = isGranted
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

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (permissionGranted) "Permiso concedido"
            else "Solicitar permiso de ubicación para asistencia de viaje",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val isGranted = ContextCompat.checkSelfPermission(context, permission)
                if (isGranted == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = true
                } else {
                    permissionLauncher.launch(permission)
                }
            }
        ) {
            Text("Solicitar Permiso")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("Volver")
        }
    }
}
