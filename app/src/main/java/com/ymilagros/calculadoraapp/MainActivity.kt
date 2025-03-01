package com.ymilagros.calculadoraapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
    val backgroundColor = Color(0xFF000000)
    val buttonGray = Color(0xFF505050)
    val buttonLightGray = Color(0xFFB0B0B0)
    val buttonPurple = Color(0xFF9C27B0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = viewModel.pantalla,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(24.dp))

        val botones = listOf(
            listOf("C", "±", "%", "/"),
            listOf("7", "8", "9", "*"),
            listOf("4", "5", "6", "-"),
            listOf("1", "2", "3", "+"),
            listOf("0", "", "=", "")
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
                                else -> buttonGray
                            },
                            onClick = { viewModel.onBotonPresionado(texto) }
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
fun BotonCalculadora(texto: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(64.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = CircleShape
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(text = texto, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
