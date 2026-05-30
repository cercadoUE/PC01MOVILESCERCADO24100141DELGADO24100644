package com.example.pc01movilescercado24100141delgado24100644

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.pc01movilescercado24100141delgado24100644.presentation.navigation.AppNavGraph
import com.example.pc01movilescercado24100141delgado24100644.ui.theme.PC01MOVILESCERCADO24100141DELGADO24100644Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PC01MOVILESCERCADO24100141DELGADO24100644Theme {
                Scaffold( modifier = Modifier.fillMaxSize() ) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        //RegisterScreen()
                        AppNavGraph()

                        //Integrantes
                        // Jhony Cercado Atalaya - 24100141
                        // Freddy Delgado Villanueva - 24100644
                    }


                }
            }
        }
    }
}
