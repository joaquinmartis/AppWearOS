package com.android.wearable.cuandollegawearos.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.*
import com.google.android.horologist.compose.layout.AppScaffold
import com.google.android.horologist.compose.layout.ScreenScaffold
import androidx.navigation.NavHostController
import com.android.wearable.cuandollegawearos.business.SeleccionRepository
import com.android.wearable.cuandollegawearos.presentation.FuzzySearch.Companion.fuzzySearchByLinea

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
    // Estado local para el texto de búsqueda de líneas
    var searchText by remember { mutableStateOf("") }
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
                        val allLineas = (uiState as SeleccionUiState.Lineas).lineas
                        // Aplica filtro fuzzy cuando el usuario escribe algo
                        val lineasFiltradas = if (searchText.isBlank()) {
                            allLineas
                        } else {
                            allLineas.fuzzySearchByLinea(searchText, threshold = 80)
                        }

                        // 1) Campo de búsqueda
                        item {
                            OutlinedTextField(
                                value = searchText,
                                onValueChange = { searchText = it },
                                label = { Text("Buscar línea") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        // 2) Título
                        item {
                            Text(
                                text = "Elige la línea de colectivo",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        // 3) Lista filtrada
                        items(lineasFiltradas.size) { idx ->
                            val linea = lineasFiltradas[idx]
                            Chip(
                                onClick = { viewModel.seleccionarLinea(linea) },
                                label = { Text(linea.nombre) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
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
