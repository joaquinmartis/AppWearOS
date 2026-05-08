@file:OptIn(ExperimentalAnimationApi::class)

package com.android.wearable.cuandollegawearos.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.android.wearable.cuandollegawearos.network.LineaColectivo
import kotlin.math.sign

// ── Paleta visual del candado ─────────────────────────────────────────────────
private val LockBg         = Color(0xFF0D0D1A)
private val ColNormalBg    = Color(0xFF1A1A2E)
private val ColActiveBg    = Color(0xFF251845)
private val AccentColor    = Color(0xFFBB86FC)
private val TextPrimary    = Color(0xFFFFFFFF)
private val TextGhost      = Color(0xFF4A4A6E)
private val SepColor       = Color(0xFF252540)

/** Devuelve el dígito en posición [col]: 0=centenas, 1=decenas, 2=unidades */
private fun digitAt(num: Int, col: Int): Int = when (col) {
    0    -> num / 100
    1    -> (num / 10) % 10
    else -> num % 10
}

// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun CandadoNumericoSelector(
    lineas: List<LineaColectivo>,
    onLineaSeleccionada: (LineaColectivo) -> Unit
) {
    // ─── PARSEO DE LÍNEAS ────────────────────────────────────────────────────
    // NOTA: Se asume que linea.nombre es un número puro como String (ej: "500", "242").
    // Si el formato de la API cambia (ej: "Línea 500 - Ramal A"), modificar SOLO esta lambda.
    val validas: List<Pair<Int, LineaColectivo>> = remember(lineas) {
        lineas
            .mapNotNull { l -> l.nombre.trim().toIntOrNull()?.let { n -> n to l } }
            .sortedBy { it.first }
    }

    if (validas.isEmpty()) {
        Box(Modifier.fillMaxSize().background(LockBg), contentAlignment = Alignment.Center) {
            Text("Sin líneas disponibles", color = TextGhost, fontSize = 14.sp)
        }
        return
    }

    // ─── ESTADO ──────────────────────────────────────────────────────────────
    var idx by remember { mutableStateOf(0) }
    // columnaActiva: -1 = lista completa | 0 = centenas | 1 = decenas | 2 = unidades
    var colActiva by remember { mutableStateOf(-1) }
    var scrollDir by remember { mutableStateOf(0) }

    val lineaActual = validas[idx]
    val num = lineaActual.first

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    // ─── NAVEGACIÓN ──────────────────────────────────────────────────────────

    fun prevIdx(): Int = when (colActiva) {
        -1 -> (idx - 1).coerceAtLeast(0)
        0  -> {
            val cs = validas.map { it.first / 100 }.distinct().sorted()
            val pc = cs[((cs.indexOf(num / 100) - 1 + cs.size) % cs.size)]
            validas.indexOfFirst { it.first / 100 == pc }.coerceAtLeast(0)
        }
        1  -> {
            val c = num / 100
            val ds = validas.filter { it.first / 100 == c }.map { (it.first / 10) % 10 }.distinct().sorted()
            val pd = ds[((ds.indexOf((num / 10) % 10) - 1 + ds.size) % ds.size)]
            validas.indexOfFirst { it.first / 100 == c && (it.first / 10) % 10 == pd }
                .takeIf { it >= 0 } ?: idx
        }
        else -> {
            val c = num / 100; val d = (num / 10) % 10
            val us = validas.filter { it.first / 100 == c && (it.first / 10) % 10 == d }
                .map { it.first % 10 }.distinct().sorted()
            val pu = us[((us.indexOf(num % 10) - 1 + us.size) % us.size)]
            validas.indexOfFirst { it.first / 100 == c && (it.first / 10) % 10 == d && it.first % 10 == pu }
                .takeIf { it >= 0 } ?: idx
        }
    }

    fun nextIdx(): Int = when (colActiva) {
        -1 -> (idx + 1).coerceAtMost(validas.lastIndex)
        0  -> {
            val cs = validas.map { it.first / 100 }.distinct().sorted()
            val nc = cs[((cs.indexOf(num / 100) + 1) % cs.size)]
            validas.indexOfFirst { it.first / 100 == nc }.coerceAtLeast(0)
        }
        1  -> {
            val c = num / 100
            val ds = validas.filter { it.first / 100 == c }.map { (it.first / 10) % 10 }.distinct().sorted()
            val nd = ds[((ds.indexOf((num / 10) % 10) + 1) % ds.size)]
            validas.indexOfFirst { it.first / 100 == c && (it.first / 10) % 10 == nd }
                .takeIf { it >= 0 } ?: idx
        }
        else -> {
            val c = num / 100; val d = (num / 10) % 10
            val us = validas.filter { it.first / 100 == c && (it.first / 10) % 10 == d }
                .map { it.first % 10 }.distinct().sorted()
            val nu = us[((us.indexOf(num % 10) + 1) % us.size)]
            validas.indexOfFirst { it.first / 100 == c && (it.first / 10) % 10 == d && it.first % 10 == nu }
                .takeIf { it >= 0 } ?: idx
        }
    }

    val pIdx = prevIdx()
    val nIdx = nextIdx()
    val numPrev = validas[pIdx].first
    val numNext = validas[nIdx].first

    // ─── UI ──────────────────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LockBg)
            .focusRequester(focusRequester)
            .focusable()
            .onRotaryScrollEvent { event ->
                val paso = event.verticalScrollPixels.sign.toInt()
                if (paso != 0) {
                    scrollDir = paso
                    idx = if (paso > 0) nextIdx() else prevIdx()
                }
                true
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Indicador de modo activo
            Text(
                text = when (colActiva) {
                    0    -> "● ○ ○  centenas"
                    1    -> "○ ● ○  decenas"
                    2    -> "○ ○ ●  unidades"
                    else -> "bisel → cambiar línea"
                },
                color = if (colActiva >= 0) AccentColor else TextGhost,
                fontSize = 9.sp,
                letterSpacing = 0.5.sp
            )

            Spacer(Modifier.height(10.dp))

            // ── Las 3 columnas del candado ────────────────────────────────────
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (col in 0..2) {
                    if (col > 0) {
                        Box(
                            Modifier
                                .width(5.dp)
                                .height(60.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(Modifier.width(1.5.dp).height(32.dp).background(SepColor))
                        }
                    }
                    DigitColumn(
                        prev      = digitAt(numPrev, col),
                        curr      = digitAt(num, col),
                        next      = digitAt(numNext, col),
                        isActive  = colActiva == col,
                        scrollDir = scrollDir,
                        onClick   = { colActiva = if (colActiva == col) -1 else col }
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            // ── Zona de selección (tap sobre el número = seleccionar línea) ───
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.horizontalGradient(listOf(Color(0xFF3A1C72), Color(0xFF1A1A4E)))
                    )
                    .clickable { onLineaSeleccionada(lineaActual.second) }
                    .padding(horizontal = 20.dp, vertical = 7.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("▶", color = AccentColor, fontSize = 9.sp)
                    Text(
                        text = "Línea $num",
                        color = AccentColor,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun DigitColumn(
    prev: Int,
    curr: Int,
    next: Int,
    isActive: Boolean,
    scrollDir: Int,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = if (isActive) ColActiveBg else ColNormalBg,
        animationSpec = tween(250),
        label = "col_bg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isActive) AccentColor else Color.Transparent,
        animationSpec = tween(250),
        label = "col_border"
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .border(1.5.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Dígito anterior (fantasma)
            Text(
                text = "$prev",
                color = TextGhost,
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.alpha(0.5f)
            )

            Spacer(Modifier.height(2.dp))

            // Dígito actual (animado)
            AnimatedContent(
                targetState = curr,
                transitionSpec = {
                    if (scrollDir >= 0)
                        (slideInVertically { it } + fadeIn()) togetherWith
                        (slideOutVertically { -it } + fadeOut())
                    else
                        (slideInVertically { -it } + fadeIn()) togetherWith
                        (slideOutVertically { it } + fadeOut())
                },
                label = "digit"
            ) { digit ->
                Text(
                    text = "$digit",
                    color = if (isActive) AccentColor else TextPrimary,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.widthIn(min = 26.dp)
                )
            }

            Spacer(Modifier.height(2.dp))

            // Dígito siguiente (fantasma)
            Text(
                text = "$next",
                color = TextGhost,
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.alpha(0.5f)
            )
        }
    }
}
