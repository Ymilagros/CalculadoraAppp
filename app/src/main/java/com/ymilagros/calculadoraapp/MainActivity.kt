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
    val tamañoTexto = viewModel.tamañoTexto

    data class DiseñoColores(
        val backgroundColor: Color,
        val buttonGray: Color,
        val buttonLightGray: Color,
        val buttonPurple: Color,
        val buttonSpecial: Color,
        val buttonFontSize: Int,
        val buttonShape: androidx.compose.ui.graphics.Shape
    )

    val diseñoColores = when (diseñoActual) {
        Diseño.Original -> DiseñoColores(
            Color(0xFF000000), // Fondo negro
            Color(0xFF505050), // Gris oscuro
            Color(0xFFB0B0B0), // Gris claro
            Color(0xFF9C27B0), // Morado
            Color(0xFF2196F3), // Azul para botones especiales
            28, // Tamaño de fuente para botones
            CircleShape // Botones redondos
        )
        Diseño.Alternativo -> DiseñoColores(
            Color(0xFFFFFFFF), // Fondo blanco
            Color(0xFFCCCCCC), // Gris claro
            Color(0xFF666666), // Gris oscuro
            Color(0xFF2196F3), // Azul
            Color(0xFF4CAF50), // Verde para botones especiales
            26, // Tamaño de fuente para botones
            RoundedCornerShape(16.dp) // Botones cuadrados con bordes redondeados
        )
    }

    val backgroundColor = diseñoColores.backgroundColor
    val buttonGray = diseñoColores.buttonGray
    val buttonLightGray = diseñoColores.buttonLightGray
    val buttonPurple = diseñoColores.buttonPurple
    val buttonSpecial = diseñoColores.buttonSpecial
    val buttonFontSize = diseñoColores.buttonFontSize
    val buttonShape = diseñoColores.buttonShape

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Pantalla de la calculadora
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = pantalla,
                fontSize = tamañoTexto.sp,
                fontWeight = FontWeight.Bold,
                color = if (diseñoActual == Diseño.Original) Color.White else Color.Black,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                maxLines = 1
            )
        }

        // Controles de tamaño de texto
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            BotonCalculadora(
                texto = "A-",
                color = diseñoColores.buttonSpecial,
                onClick = { viewModel.onBotonPresionado("A-") },
                shape = buttonShape,
                fontSize = buttonFontSize
            )
            Spacer(modifier = Modifier.width(8.dp))
            BotonCalculadora(
                texto = "A+",
                color = diseñoColores.buttonSpecial,
                onClick = { viewModel.onBotonPresionado("A+") },
                shape = buttonShape,
                fontSize = buttonFontSize
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Filas de botones de la calculadora
        val botones = listOf(
            listOf("C", "±", "%", "/"),
            listOf("7", "8", "9", "*"),
            listOf("4", "5", "6", "-"),
            listOf("1", "2", "3", "+"),
            listOf("0", ".", "=", "√")
        )

        botones.forEach { fila ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                fila.forEach { texto ->
                    BotonCalculadora(
                        texto = texto,
                        color = when (texto) {
                            "C", "±", "%" -> buttonLightGray
                            "/", "*", "-", "+", "=" -> buttonPurple
                            "√" -> buttonSpecial
                            else -> buttonGray
                        },
                        onClick = { viewModel.onBotonPresionado(texto) },
                        shape = buttonShape,
                        fontSize = buttonFontSize
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Botón para cambiar diseño
        Button(
            onClick = { viewModel.onBotonPresionado("Cambiar Diseño") },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500)),
            shape = RoundedCornerShape(32.dp)
        ) {
            Text(
                text = "Cambiar Diseño",
                fontSize = buttonFontSize.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun BotonCalculadora(
    texto: String,
    color: Color,
    onClick: () -> Unit,
    shape: androidx.compose.ui.graphics.Shape,
    fontSize: Int
) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(64.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = shape
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(
                text = texto,
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}