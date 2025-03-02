package com.ymilagros.calculadoraapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculadoraApp()
        }
    }
}

@Composable
fun CalculadoraApp(viewModel: CalculadoraViewModel = viewModel()) {
    val pantalla = viewModel.pantalla
    val diseñoActual = viewModel.diseñoActual

    data class DiseñoColores(
        val backgroundColor: Color,
        val buttonGray: Color,
        val buttonLightGray: Color,
        val buttonPurple: Color,
        val buttonShape: androidx.compose.ui.graphics.Shape
    )

    val diseñoColores = when (diseñoActual) {
        Diseño.Original -> DiseñoColores(
            Color(0xFF000000), // Fondo negro
            Color(0xFF505050), // Gris oscuro
            Color(0xFFB0B0B0), // Gris claro
            Color(0xFF9C27B0), // Morado
            CircleShape // Botones redondos
        )
        Diseño.Alternativo -> DiseñoColores(
            Color(0xFFFFFFFF), // Fondo blanco
            Color(0xFFCCCCCC), // Gris claro
            Color(0xFF666666), // Gris oscuro
            Color(0xFF2196F3), // Azul
            RoundedCornerShape(16.dp) // Botones cuadrados con bordes redondeados
        )
    }

    val backgroundColor = diseñoColores.backgroundColor
    val buttonGray = diseñoColores.buttonGray
    val buttonLightGray = diseñoColores.buttonLightGray
    val buttonPurple = diseñoColores.buttonPurple
    val buttonShape = diseñoColores.buttonShape

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = pantalla,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = if (diseñoActual == Diseño.Original) Color.White else Color.Black,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End
        )

        Spacer(modifier = Modifier.height(24.dp))

        val botones = listOf(
            listOf("C", "±", "%", "/"),
            listOf("7", "8", "9", "*"),
            listOf("4", "5", "6", "-"),
            listOf("1", "2", "3", "+"),
            listOf("0", "", "=", "Cambiar Diseño")
        )

        botones.forEach { fila ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                fila.forEach { texto ->
                    if (texto.isNotEmpty()) {
                        BotonCalculadora(
                            texto = texto,
                            color = when (texto) {
                                "C", "±", "%" -> buttonLightGray
                                "/", "*", "-", "+", "=" -> buttonPurple
                                "Cambiar Diseño" -> Color(0xFFFFA500) // Naranja para el botón de cambio
                                else -> buttonGray
                            },
                            onClick = { viewModel.onBotonPresionado(texto) },
                            shape = buttonShape
                        )
                    } else {
                        Spacer(modifier = Modifier.size(64.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun BotonCalculadora(texto: String, color: Color, onClick: () -> Unit, shape: androidx.compose.ui.graphics.Shape) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(64.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = shape
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(text = texto, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}