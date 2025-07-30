package com.android.wearable.cuandollegawearos.presentation

import com.android.wearable.cuandollegawearos.business.Calle
import com.android.wearable.cuandollegawearos.business.Interseccion
import com.android.wearable.cuandollegawearos.business.LineaColectivo
import me.xdrop.fuzzywuzzy.FuzzySearch

class FuzzySearch {
    companion object {
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

        fun List<Calle>.fuzzySearchByCalle(
            query: String,
            threshold: Int = 75   // porcentaje mínimo de similitud (0–100)
        ): List<Calle> {
            val q = query.trim()
            return this
                .map { calle ->
                    val score = FuzzySearch.ratio(calle.descripcion, q)
                    calle to score
                }
                .filter { (_, score) -> score >= threshold }
                .sortedByDescending { it.second }
                .map { it.first }
        }

        fun List<Interseccion>.fuzzySearchByInterseccion(
            query: String,
            threshold: Int = 75   // porcentaje mínimo de similitud (0–100)
        ): List<Interseccion> {
            val q = query.trim()
            return this
                .map { interseccion ->
                    val score = FuzzySearch.ratio(interseccion.descripcion, q)
                    interseccion to score
                }
                .filter { (_, score) -> score >= threshold }
                .sortedByDescending { it.second }
                .map { it.first }
        }

    }
}
