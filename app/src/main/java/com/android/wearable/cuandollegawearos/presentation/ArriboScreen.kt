@file:OptIn(ExperimentalHorologistApi::class)

package com.android.wearable.cuandollegawearos.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.*
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.layout.AppScaffold
import com.google.android.horologist.compose.layout.ScalingLazyColumn
import com.google.android.horologist.compose.layout.ScalingLazyColumnDefaults
import com.google.android.horologist.compose.layout.ScreenScaffold
import com.google.android.horologist.compose.layout.rememberResponsiveColumnState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

/**
 * ArriboScreen es una clase de la UI que se encarga de maquetar y mostrar todo lo que se va a ver en la pantalla. Se comunica
 * con ArribosViewModel en forma de MVC siendo este ultimo el controlador.
 *
 */

@Composable
fun ArriboScreen(
    navController: NavHostController,
    viewModel: ArribosViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarArribos()
    }

    // Booleano para el indicador de refresco
    val isRefreshing = uiState is ArribosUiState.Loading
    val swipeState = rememberSwipeRefreshState(isRefreshing)

    AppScaffold {
        val columnState = rememberResponsiveColumnState(
            contentPadding = ScalingLazyColumnDefaults.padding(
                first = ScalingLazyColumnDefaults.ItemType.Text,
                last = ScalingLazyColumnDefaults.ItemType.Chip
            )
        )
        SwipeRefresh(
            state     = swipeState,
            onRefresh = { viewModel.actualizarArribos() },
            modifier  = Modifier.fillMaxSize()
        ) {
        ScreenScaffold(scrollState = columnState) {
            ScalingLazyColumn(
                columnState = columnState
            ) {

                when (uiState) {
                    is ArribosUiState.Loading -> {
                        item {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Cargando...",
                                    color = MaterialTheme.colors.onBackground,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                    is ArribosUiState.Error -> {
                        val error = (uiState as ArribosUiState.Error).message
                        item {
                            Text(
                                text = "Error",
                                color = Color.Red,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = error,
                                color = Color.Red,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    is ArribosUiState.Success -> {
                        val arribos = (uiState as ArribosUiState.Success).arribos
                        if (arribos.isEmpty()) {
                            item {
                                Text(
                                    text = "Sin arribos",
                                    color = MaterialTheme.colors.onBackground,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            items(arribos.size) { idx ->
                                val arribo = arribos[idx]
                                val colorCoche = arribo.color
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                ) {
                                    Text(
                                        text = arribo.linea,
                                        color = MaterialTheme.colors.onBackground,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = arribo.tiempo, // Ej: "4 min"
                                        color = colorCoche,
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = arribo.sentido,
                                        color = MaterialTheme.colors.onBackground,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Precisión: ${arribo.precision}",
                                        color = MaterialTheme.colors.onBackground,
                                        fontSize = 10.sp
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                }

                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
                item {
                    Chip(
                        onClick = { viewModel.actualizarArribos() },
                        label = {
                            Text(
                                text = "Actualizar",
                                fontSize = 14.sp
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = uiState !is ArribosUiState.Loading
                    )
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item {
                    Chip(
                        onClick = { navController.navigate("seleccion_colectivo_screen") },
                        label = {
                            Text(
                                text = "Seleccionar colectivo",
                                fontSize = 14.sp
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        }
    }
}
