package com.ymilagros.calculadoraapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CalculadoraViewModel : ViewModel() {
    var pantalla by mutableStateOf("0")
    private var operador: String? = null
    private var numeroAnterior: String = ""
    private var numeroActual: String = "0"

    fun onBotonPresionado(texto: String) {
        when (texto) {
            "C" -> limpiar()
            "=" -> calcularResultado()
            "+", "-", "*", "/" -> asignarOperador(texto)
            else -> agregarNumero(texto)
        }
    }

    private fun limpiar() {
        pantalla = "0"
        numeroActual = "0"
        numeroAnterior = ""
        operador = null
    }

    private fun asignarOperador(nuevoOperador: String) {
        if (numeroActual.isNotEmpty()) {
            operador = nuevoOperador
            numeroAnterior = numeroActual
            numeroActual = ""
            pantalla = "$numeroAnterior $operador"
        }
    }

    private fun agregarNumero(digito: String) {
        if (numeroActual == "0") {
            numeroActual = digito
        } else {
            numeroActual += digito
        }
        pantalla = numeroActual
    }

    private fun calcularResultado() {
        if (operador != null && numeroAnterior.isNotEmpty() && numeroActual.isNotEmpty()) {
            val num1 = numeroAnterior.toFloat()
            val num2 = numeroActual.toFloat()
            val resultado = when (operador) {
                "+" -> num1 + num2
                "-" -> num1 - num2
                "*" -> num1 * num2
                "/" -> if (num2 != 0f) num1 / num2 else Float.NaN
                else -> 0f
            }
            pantalla = if (resultado.isNaN()) "Error" else resultado.toString()
            numeroActual = pantalla
            numeroAnterior = ""
            operador = null
        }
    }
}
