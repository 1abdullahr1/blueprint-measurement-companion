package com.blueprintcompanion.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.blueprintcompanion.app.ui.AppViewModel
import com.blueprintcompanion.app.ui.CalculatorScreen
import com.blueprintcompanion.app.ui.PropertyDetailScreen
import com.blueprintcompanion.app.ui.PropertyListScreen
import com.blueprintcompanion.app.ui.theme.BlueprintTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BlueprintTheme {
                CompanionApp()
            }
        }
    }
}

@Composable
private fun CompanionApp(viewModel: AppViewModel = viewModel()) {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "properties") {
        composable("properties") {
            PropertyListScreen(
                viewModel = viewModel,
                onOpen = { id -> nav.navigate("property/$id") },
                onCalculator = { nav.navigate("calculator") }
            )
        }
        composable(
            route = "property/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { entry ->
            val id = entry.arguments?.getLong("id") ?: return@composable
            PropertyDetailScreen(
                propertyId = id,
                viewModel = viewModel,
                onBack = { nav.popBackStack() }
            )
        }
        composable("calculator") {
            CalculatorScreen(onBack = { nav.popBackStack() })
        }
    }
}
