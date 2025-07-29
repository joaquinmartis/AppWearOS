package com.android.wearable.cuandollegawearos.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.*
import com.google.android.horologist.compose.layout.AppScaffold
import com.google.android.horologist.compose.layout.ScreenScaffold
import androidx.navigation.NavHostController
import com.android.wearable.cuandollegawearos.business.SeleccionRepository

/**
 * SeleccionColectivoScreen es una funcion que al igual que ArriboScreen controla la UI. Tiene mucha mas logica, pues maneja listas
 * y muchos mas mensajes entre capas.
 *
 *
 */

@Composable
fun SeleccionColectivoScreen(
    navController: NavHostController,
    viewModel: SeleccionColectivoViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    AppScaffold {
        val columnState = rememberScalingLazyListState()
        ScreenScaffold(scrollState = columnState) {
            ScalingLazyColumn(
                state = columnState,
                modifier = Modifier.fillMaxSize()
            ) {
                when (uiState) {
                    is SeleccionUiState.Empty -> {
                        item {
                            Chip(
                                onClick = { viewModel.cargarLineas() },
                                label = {
                                    Text(
                                        text = "Comenzar selección",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                    )
                                },
                                colors = ChipDefaults.primaryChipColors(
                                    backgroundColor = Color(0xFF1C1C1E),
                                    contentColor = Color.White
                                ),
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                    is SeleccionUiState.Loading -> {
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
                    is SeleccionUiState.Lineas -> {
                        val lineas = (uiState as SeleccionUiState.Lineas).lineas
                        item {
                            Text("Elige la línea de colectivo", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        items(lineas.size) { idx ->
                            val linea = lineas[idx]
                            Chip(
                                onClick = { viewModel.seleccionarLinea(linea) },
                                label = { Text(linea.nombre) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    is SeleccionUiState.Calles -> {
                        val calles = (uiState as SeleccionUiState.Calles).calles
                        item {
                            Text("Elige la calle", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        items(calles.size) { idx ->
                            val calle = calles[idx]
                            Chip(
                                onClick = { viewModel.seleccionarCalle(calle) },
                                label = { Text(calle.descripcion) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    is SeleccionUiState.Intersecciones -> {
                        val intersecciones = (uiState as SeleccionUiState.Intersecciones).intersecciones
                        item {
                            Text("Elige la intersección", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        items(intersecciones.size) { idx ->
                            val inter = intersecciones[idx]
                            Chip(
                                onClick = { viewModel.seleccionarInterseccion(inter) },
                                label = { Text(inter.descripcion) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    is SeleccionUiState.Destinos -> {
                        val destinos = (uiState as SeleccionUiState.Destinos).destinos
                        item {
                            Text("Elige el destino", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        items(destinos.size) { idx ->
                            val destino = destinos[idx]
                            Chip(
                                onClick = {
                                    viewModel.seleccionarDestino(destino)
                                    navController.navigate("arribo_screen")
                                },
                                label = { Text(destino.abreviaturaAmpliadaBandera) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    is SeleccionUiState.Error -> {
                        val msg = (uiState as SeleccionUiState.Error).message
                        item {
                            Text("Error: $msg", color = Color.Red)
                            Button(onClick = { viewModel.reiniciar() }) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
            }
        }
    }
}
