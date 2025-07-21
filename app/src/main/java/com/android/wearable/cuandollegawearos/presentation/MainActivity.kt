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
@file:OptIn(ExperimentalHorologistApi::class, ExperimentalWearFoundationApi::class)

package com.android.wearable.cuandollegawearos.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.material.Text
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.android.wearable.cuandollegawearos.presentation.theme.WearAppTheme
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.navigation.*


/**
 * Clase Main, entrypoint de la app. Se encarga de segmentar las pantallas principales de la app.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WearApp()
        }
    }


    @Composable
    fun WearApp() {
        WearAppTheme {
            val navController = rememberSwipeDismissableNavController()

            SwipeDismissableNavHost(
                navController = navController,
                startDestination = "seleccion_colectivo_screen"
            ) {
                composable(
                    route = "arribo_screen/{idParada}/{codLineaParada}",
                    arguments = listOf(
                        navArgument("idParada") { type = NavType.StringType },
                        navArgument("codLineaParada") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val idParada = backStackEntry.arguments?.getString("idParada") ?: ""
                    val codLineaParada = backStackEntry.arguments?.getString("codLineaParada") ?: ""
                    ArriboScreen(navController, idParada, codLineaParada)
                }
                composable("seleccion_colectivo_screen") {
                    SeleccionColectivoScreen(navController)
                }
                // Agrega más pantallas aquí si las tienes
            }
        }
    }

// O si ya tienes navegación, simplemente agrega la ruta:
// composable("arribo_screen") { ArriboScreen() }

}

