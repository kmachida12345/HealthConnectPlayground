package com.github.kmachida12345.healthconnectplayground

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.kmachida12345.healthconnectplayground.ui.theme.DailyRecordsScreen

@Composable
fun HcpNavHost(

    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "main"

) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("main") {
            Greeting("hogeee", {navController.navigate("hoge")}, {navController.navigate("annual")})
        }
        composable("hoge") {
            DailyRecordsScreen()
        }
        composable("annual") {
            AnnualRecordsScreen()
        }

    }
}
