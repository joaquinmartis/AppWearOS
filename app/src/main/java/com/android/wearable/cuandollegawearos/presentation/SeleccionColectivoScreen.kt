package com.android.wearable.cuandollegawearos.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.ScalingLazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.*
import com.google.android.horologist.compose.layout.AppScaffold
import com.google.android.horologist.compose.layout.ScreenScaffold

@Composable
fun SeleccionColectivoScreen(viewModel: SeleccionColectivoViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    // El estado Lineas tiene su propia UI a pantalla completa (candado numérico).
    // No usa AppScaffold/ScalingLazyColumn para poder manejar el bisel directamente.
    if (uiState is SeleccionUiState.Lineas) {
        val lineas = (uiState as SeleccionUiState.Lineas).lineas
        CandadoNumericoSelector(
            lineas = lineas,
            onLineaSeleccionada = { viewModel.seleccionarLinea(it) }
        )
        return
    }

    // ── El resto de los estados usan el scaffold y la lista normal ────────────
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
                            Button(onClick = { viewModel.cargarLineas() }) {
                                Text("Comenzar selección")
                            }
                        }
                    }

                    is SeleccionUiState.Loading -> {
                        item {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Cargando...", fontSize = 14.sp)
                        }
                    }

                    is SeleccionUiState.Lineas -> {
                        // Este branch nunca se alcanza (manejado arriba), pero el
                        // compilador lo requiere por exhaustividad del when.
                    }

                    is SeleccionUiState.Calles -> {
                        val calles = (uiState as SeleccionUiState.Calles).calles
                        item {
                            Text(
                                "Elige la calle",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
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
                        val intersecciones =
                            (uiState as SeleccionUiState.Intersecciones).intersecciones
                        item {
                            Text(
                                "Elige la intersección",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
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

                    is SeleccionUiState.SubLineas -> {
                        val sublineas = (uiState as SeleccionUiState.SubLineas).sublineas
                        item {
                            Text(
                                "Elige la dirección/sub-línea",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                        items(sublineas.size) { idx ->
                            val sub = sublineas[idx]
                            Chip(
                                onClick = { viewModel.seleccionarSubLinea(sub) },
                                label = { Text(sub.nombre) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    is SeleccionUiState.Arribos -> {
                        val arribos = (uiState as SeleccionUiState.Arribos).arribos
                        item {
                            Text(
                                "Próximos arribos",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                        if (arribos.isEmpty()) {
                            item {
                                Text("Sin arribos disponibles", color = Color.Gray)
                            }
                        } else {
                            items(arribos.size) { idx ->
                                val arribo = arribos[idx]
                                val colorCoche = arribo.precision.colorAsociado
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(60.dp)
                                            .background(colorCoche),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("🚌", fontSize = 24.sp)
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        arribo.arribo,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        arribo.descripcionLinea + " - " + arribo.descripcionBandera,
                                        fontSize = 12.sp
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        "Precisión: ${arribo.precision.descripcion}",
                                        fontSize = 10.sp
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                            }
                        }
                        item {
                            Button(onClick = { viewModel.reiniciar() }) {
                                Text("Nueva búsqueda")
                            }
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
