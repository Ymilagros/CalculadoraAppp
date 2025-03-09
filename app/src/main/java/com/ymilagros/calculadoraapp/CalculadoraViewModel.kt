package com.ymilagros.calculadoraapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.math.sqrt

class CalculadoraViewModel : ViewModel() {
    var pantalla by mutableStateOf("0")
        private set

    var diseñoActual by mutableStateOf(Diseño.Original)
        private set

    // Tamaño del texto de la pantalla
    var tamañoTexto by mutableStateOf(48)
        private set

    private var operador: String? = null
    private var numeroAnterior: String = ""
    private var numeroActual: String = "0"
    private var ultimoBotonOperacion = false
    private var modoPorcentaje = false

    fun onBotonPresionado(texto: String) {
        when (texto) {
            "C" -> limpiar()
            "=" -> calcularResultado()
            "+", "-", "*", "/" -> manejarOperador(texto)
            "%" -> manejarPorcentaje()
            "±" -> cambiarSigno()
            "√" -> calcularRaizCuadrada()
            "." -> agregarPuntoDecimal()
            "Cambiar Diseño" -> cambiarDiseño()
            "A+" -> aumentarTamañoTexto()
            "A-" -> disminuirTamañoTexto()
            else -> agregarNumero(texto)
        }
        actualizarPantalla()
    }

    private fun limpiar() {
        pantalla = "0"
        numeroActual = "0"
        numeroAnterior = ""
        operador = null
        ultimoBotonOperacion = false
        modoPorcentaje = false
    }

    private fun manejarOperador(nuevoOperador: String) {
        // Si ya hay una operación pendiente, calcular primero
        if (numeroAnterior.isNotEmpty() && numeroActual.isNotEmpty() && operador != null && !ultimoBotonOperacion) {
            calcularResultado()
        }

        if (numeroActual.isNotEmpty()) {
            operador = nuevoOperador
            numeroAnterior = numeroActual
            numeroActual = ""
            ultimoBotonOperacion = true
            modoPorcentaje = false
        } else if (numeroAnterior.isNotEmpty()) {
            // Permite cambiar el operador sin afectar los valores
            operador = nuevoOperador
            ultimoBotonOperacion = true
        }
    }

    private fun manejarPorcentaje() {
        if (operador != null && numeroAnterior.isNotEmpty()) {
            // Modo porcentaje para operador
            modoPorcentaje = true
            if (numeroActual.isEmpty()) {
                numeroActual = "0"
            }
        } else {
            // Porcentaje directo sobre el número actual
            aplicarPorcentajeDirecto()
        }
    }

    private fun aplicarPorcentajeDirecto() {
        if (numeroActual.isNotEmpty() && numeroActual != "0") {
            val num = numeroActual.toDoubleOrNull() ?: 0.0
            val porcentaje = num / 100
            numeroActual = formatearResultado(porcentaje)
        }
    }

    private fun agregarNumero(digito: String) {
        if (ultimoBotonOperacion || numeroActual == "0") {
            numeroActual = digito
        } else {
            numeroActual += digito
        }
        ultimoBotonOperacion = false
    }

    private fun agregarPuntoDecimal() {
        // No agregar punto si ya existe uno
        if (!numeroActual.contains(".")) {
            if (numeroActual.isEmpty()) {
                numeroActual = "0."
            } else {
                numeroActual += "."
            }
        }
        ultimoBotonOperacion = false
    }

    private fun calcularRaizCuadrada() {
        if (numeroActual.isNotEmpty() && numeroActual != "0") {
            val num = numeroActual.toDoubleOrNull() ?: 0.0
            if (num >= 0) {
                val resultado = sqrt(num)
                numeroActual = formatearResultado(resultado)
            } else {
                numeroActual = "Error"
            }
            ultimoBotonOperacion = true
        }
    }

    private fun calcularResultado() {
        if (operador != null && numeroAnterior.isNotEmpty() && numeroActual.isNotEmpty()) {
            val num1 = numeroAnterior.toDoubleOrNull() ?: 0.0

            // Determinar si aplicamos cálculo de porcentaje
            if (modoPorcentaje) {
                calcularOperacionConPorcentaje()
            } else {
                val num2 = numeroActual.toDoubleOrNull() ?: 0.0
                val resultado = when (operador) {
                    "+" -> num1 + num2
                    "-" -> num1 - num2
                    "*" -> num1 * num2
                    "/" -> if (num2 != 0.0) num1 / num2 else Double.NaN
                    else -> 0.0
                }

                numeroActual = formatearResultado(resultado)
                numeroAnterior = ""
                operador = null
            }

            ultimoBotonOperacion = true
            modoPorcentaje = false
        }
    }

    private fun calcularOperacionConPorcentaje() {
        val num1 = numeroAnterior.toDoubleOrNull() ?: 0.0
        val porcentaje = numeroActual.toDoubleOrNull() ?: 0.0
        val valorPorcentaje = num1 * (porcentaje / 100)

        val resultado = when (operador) {
            "+" -> num1 + valorPorcentaje
            "-" -> num1 - valorPorcentaje
            "*" -> num1 * (porcentaje / 100)
            "/" -> if (porcentaje != 0.0) num1 / (porcentaje / 100) else Double.NaN
            else -> 0.0
        }

        numeroActual = formatearResultado(resultado)
        numeroAnterior = ""
        operador = null
    }

    private fun cambiarSigno() {
        if (numeroActual.isNotEmpty() && numeroActual != "0") {
            val num = numeroActual.toDoubleOrNull() ?: 0.0
            numeroActual = formatearResultado(-num)
        }
    }

    private fun formatearResultado(valor: Double): String {
        return when {
            valor.isNaN() -> "Error"
            valor == valor.toInt().toDouble() -> valor.toInt().toString()
            else -> String.format("%.6f", valor).replace(",", ".").trimEnd('0').trimEnd('.')
        }
    }

    private fun cambiarDiseño() {
        diseñoActual = when (diseñoActual) {
            Diseño.Original -> Diseño.Alternativo
            Diseño.Alternativo -> Diseño.Original
        }
    }

    private fun aumentarTamañoTexto() {
        if (tamañoTexto < 72) {
            tamañoTexto += 4
        }
    }

    private fun disminuirTamañoTexto() {
        if (tamañoTexto > 32) {
            tamañoTexto -= 4
        }
    }

    private fun actualizarPantalla() {
        pantalla = when {
            modoPorcentaje && operador != null && numeroAnterior.isNotEmpty() -> {
                val operadorSymbol = when (operador) {
                    "+" -> "+"
                    "-" -> "-"
                    "*" -> "×"
                    "/" -> "÷"
                    else -> operador ?: ""
                }
                "$numeroAnterior $operadorSymbol $numeroActual%"
            }
            operador != null && numeroAnterior.isNotEmpty() && numeroActual.isEmpty() -> {
                val operadorSymbol = when (operador) {
                    "+" -> "+"
                    "-" -> "-"
                    "*" -> "×"
                    "/" -> "÷"
                    else -> operador ?: ""
                }
                "$numeroAnterior $operadorSymbol"
            }
            numeroActual.isEmpty() -> numeroAnterior
            else -> numeroActual
        }
    }
}

enum class Diseño {
    Original,
    Alternativo
}