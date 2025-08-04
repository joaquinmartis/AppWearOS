package com.android.wearable.cuandollegawearos.presentation

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
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
import com.android.wearable.cuandollegawearos.presentation.FuzzySearch.Companion.buscarPorTrigramasCalles
import com.android.wearable.cuandollegawearos.presentation.FuzzySearch.Companion.buscarPorTrigramasIntersecciones
import com.android.wearable.cuandollegawearos.presentation.FuzzySearch.Companion.fuzzySearchByLinea
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign

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
    var searchTextLineas by remember { mutableStateOf("") }
    var searchTextCalles by remember { mutableStateOf("") }
    var searchTextIntersecciones by remember { mutableStateOf("") }


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
                        val lineasFiltradas = if (searchTextLineas.isBlank()) {
                            allLineas
                        } else {
                            allLineas.fuzzySearchByLinea(searchTextLineas, threshold = 80)
                        }

                        // 2) Título
                        item {
                            Text(
                                text = "Elige la línea",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        item {
                            SearchChip(
                                searchText = searchTextLineas,
                                onSearchTextChange = { searchTextLineas = it }
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
                        val allcalles = (uiState as SeleccionUiState.Calles).calles
                        // Aplica filtro fuzzy cuando el usuario escribe algo
                        val callesFiltradas = if (searchTextCalles.isBlank()) {
                            allcalles
                        } else {
                            buscarPorTrigramasCalles(searchTextCalles,allcalles)
                        }

                        // 2) Título
                        item {
                            Text(
                                text = "Elige la calle",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        item {
                            SearchChip(
                                searchText = searchTextCalles,
                                onSearchTextChange = { searchTextCalles = it }
                            )
                        }

                        // 3) Lista filtrada
                        items(callesFiltradas.size) { idx ->
                            val calle = callesFiltradas[idx]
                            Chip(
                                onClick = { viewModel.seleccionarCalle(calle) },
                                label = { Text(calle.nombreCalle) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }



                    is SeleccionUiState.Intersecciones -> {


                        val allIntersecciones = (uiState as SeleccionUiState.Intersecciones).intersecciones
                    // Aplica filtro fuzzy cuando el usuario escribe algo
                        val interseccionesFiltradas = if (searchTextIntersecciones.isBlank()) {
                            allIntersecciones
                        } else {
                            buscarPorTrigramasIntersecciones(searchTextIntersecciones,allIntersecciones)
                    }

                        // 1) Título

                        item {
                            Text(
                                text = "Elige la interseccion",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        // Boton busqueda
                        item {
                            SearchChip(
                                searchText = searchTextIntersecciones,
                                onSearchTextChange = { searchTextIntersecciones = it }
                            )
                        }
                        // 3) Lista filtrada
                        items(interseccionesFiltradas.size) { idx ->
                            val interseccion = interseccionesFiltradas[idx]
                            Chip(
                                onClick = { viewModel.seleccionarInterseccion(interseccion) },
                                label = { Text(interseccion.nombreInterseccion) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
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
                        item {
                            val msg = (uiState as SeleccionUiState.Error).message
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Error",
                                    color = Color.Red,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = msg,
                                    color = Color.Red,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Chip(
                                    onClick = { viewModel.reiniciar() },
                                    label = { Text("Reintentar") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    colors = ChipDefaults.primaryChipColors(
                                        backgroundColor = Color(0xFF1C1C1E),
                                        contentColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchChip(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
) {
    // Para poder pedir foco al campo
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(20.dp))
            .background(Color.Black, RoundedCornerShape(20.dp))
            .clickable { focusRequester.requestFocus() }  // Al tocar el chip, abre el teclado
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            BasicTextField(
                value = searchText,
                onValueChange = {
                    onSearchTextChange(it)
                    // Si quieres buscar mientras tipeas, descomenta:
                    // onSearchSubmit(it)
                },
                singleLine = true,
                textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,

                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {keyboardController?.hide() }
                ),
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
            )
        }
    }
}
