package com.example.pc01movilescercado24100141delgado24100644.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pc01movilescercado24100141delgado24100644.presentation.calculator.BaggageCalculatorScreen
import com.example.pc01movilescercado24100141delgado24100644.presentation.budget.BudgetPlannerScreen
import com.example.pc01movilescercado24100141delgado24100644.presentation.destinations.TouristDestinationsScreen
import com.example.pc01movilescercado24100141delgado24100644.presentation.location.LocationPermissionScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") { MainScreen(navController) }
        composable("baggage_calculator") { BaggageCalculatorScreen(navController) }
        composable("budget_planner") { BudgetPlannerScreen(navController) }
        composable("tourist_destinations") { TouristDestinationsScreen(navController) }
        composable("location_permission") { LocationPermissionScreen(navController) }
    }
}
