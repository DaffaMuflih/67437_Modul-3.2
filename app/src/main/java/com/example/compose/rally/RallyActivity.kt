/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.compose.rally

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.compose.rally.ui.components.RallyTabRow
import com.example.compose.rally.ui.theme.RallyTheme
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.compose.rally.ui.overview.OverviewScreen

@Composable
fun RallyApp() {
    RallyTheme {
        val navController = rememberNavController()

        val currentBackStack = navController.currentBackStackEntryAsState().value

        val currentDestination = currentBackStack?.destination

        // Change the variable to this and use Overview as a backup screen if this returns null
        val currentScreen =
            rallyTabRowScreens.find { it.route == currentDestination?.route } ?: Overview
        Scaffold(
            topBar = {
                RallyTabRow(
                    allScreens = rallyTabRowScreens,
                    onTabSelected = { newScreen ->
                        navController.navigateSingleTopTo(newScreen.route)
                    },
                    currentScreen = currentScreen,
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Overview.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Overview.route) { OverviewScreen() }
                // Tambahkan composable lainnya jika perlu
            }
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) {
    this.navigate(route) {
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) {
            inclusive = false
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}