package com.android.wearable.cuandollegawearos.presentation

import android.icu.lang.UCharacter.toUpperCase
import com.android.wearable.cuandollegawearos.business.Calle
import com.android.wearable.cuandollegawearos.business.Interseccion
import com.android.wearable.cuandollegawearos.business.LineaColectivo
import me.xdrop.fuzzywuzzy.FuzzySearch

class FuzzySearch {
    companion object {


        fun generarTrigramas(s: String): Set<String> {
            return s.lowercase().windowed(3, 1, partialWindows = true).toSet()
        }

        fun buscarPorTrigramasLineas(input: String, lineas: List<LineaColectivo>): List<LineaColectivo> {
            val queryTris = generarTrigramas(input)

            return lineas
                .map { it to generarTrigramas(it.nombre) }
                .sortedByDescending { (_, tris) -> tris.intersect(queryTris).size }
                .map { it.first }
        }

        fun buscarPorTrigramasCalles(input: String, calles: List<Calle>): List<Calle> {
            val queryTris = generarTrigramas(input)

            return calles
                .map { it to generarTrigramas(it.nombreCalle) }
                .sortedByDescending { (_, tris) -> tris.intersect(queryTris).size }
                .map { it.first }
        }

        fun buscarPorTrigramasIntersecciones(input: String, intersecciones: List<Interseccion>): List<Interseccion> {
            val queryTris = generarTrigramas(input)

            return intersecciones
                .map { it to generarTrigramas(it.nombreInterseccion) }
                .sortedByDescending { (_, tris) -> tris.intersect(queryTris).size }
                .map { it.first }
        }

        fun List<LineaColectivo>.fuzzySearchByLinea(
            query: String,
            threshold: Int = 75   // porcentaje mínimo de similitud (0–100)
        ): List<LineaColectivo> {
            val q = query.trim()
            return this
                .map { linea ->
                    val score = FuzzySearch.ratio(linea.nombre, q)
                    linea to score
                }
                .filter { (_, score) -> score >= threshold }
                .sortedByDescending { it.second }
                .map { it.first }
        }

    }
}
