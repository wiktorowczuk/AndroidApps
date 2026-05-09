package com.example.androidapps.ui.blokowanieznakow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.androidapps.ui.theme.AndroidAppsTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.material3.Switch
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.let
import kotlin.text.any
import kotlin.text.isLetterOrDigit
import kotlin.text.isLowerCase
import kotlin.text.isUpperCase
import kotlin.text.substring


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidAppsTheme {
                BlokZnakow()
            }
        }
    }
}

@Composable
fun BlokZnakow() {
    var text by remember { mutableStateOf("") }
    var blockUppercase by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var errorId by remember { mutableStateOf(0) }

    var blockLowercase by remember { mutableStateOf(false) }
    var blockNonAlph by remember { mutableStateOf(false) }

    val gradientColors = listOf(Color.Red, Color.Green, Color.Blue, Color.Cyan, Color.Black)

    LaunchedEffect(errorId) {
        if (errorMessage != null) {
            delay(3000)
            errorMessage = null
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                modifier = Modifier.fillMaxWidth(0.65f),
                style = TextStyle(
                    brush = Brush.linearGradient(colors = gradientColors),
                    fontSize = 24.sp
                ),
                text = "Aplikacja pozwalająca blokować poszczególne znaki przy wpisywaniu tekstu",
                textAlign = TextAlign.Center
            )


            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = text,
                onValueChange = { newText ->
                    if (blockUppercase) {

                        val oldText = text

                        val addedPart = if (newText.length > oldText.length) {
                            newText.substring(oldText.length)
                        } else {
                            ""
                        }
                        if (addedPart.any { it.isUpperCase() }) {
                            errorMessage = "Nie można dodać wielkiej litery"
                            errorId++
                            return@TextField
                        }
                    }
                    if (blockLowercase) {

                        val oldText = text

                        val addedPart = if (newText.length > oldText.length) {
                            newText.substring(oldText.length)
                        } else {
                            ""
                        }
                        if (addedPart.any { it.isLowerCase() }) {
                            errorMessage = "Nie można dodać małej litery"
                            errorId++
                            return@TextField
                        }
                    }

                    if (blockNonAlph) {

                        val oldText = text

                        val addedPart = if (newText.length > oldText.length) {
                            newText.substring(oldText.length)
                        } else {
                            ""
                        }
                        if (addedPart.any { !it.isLetterOrDigit() }) {
                            errorMessage = "Nie można dodać znaku specjalnego"
                            errorId++
                            return@TextField
                        }
                    }

                    text = newText
                    //errorMessage = null
                },
                label = { Text("Wprowadź tekst") },
                isError = errorMessage != null,
                supportingText = {
                    errorMessage?.let { Text(it) }
                }
            )


            Row(
                modifier = Modifier.fillMaxWidth(0.65f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Switch(
                    checked = blockNonAlph,
                    onCheckedChange = {
                        blockNonAlph = it
                        errorMessage = null
                    }
                )
                Text(" Blokuj znaki specjalne", modifier = Modifier.weight(1f))
            }

            Row(
                modifier = Modifier.fillMaxWidth(0.65f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Switch(
                    checked = blockLowercase,
                    onCheckedChange = {
                        blockLowercase = it
                        errorMessage = null
                    }
                )
                Text(" Blokuj małe litery", modifier = Modifier.weight(1f))
            }

            Row(
                modifier = Modifier.fillMaxWidth(0.65f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Switch(
                    checked = blockUppercase,
                    onCheckedChange = {
                        blockUppercase = it
                        errorMessage = null
                    }
                )
                Text(" Blokuj wielkie litery", modifier = Modifier.weight(1f))
            }


        }
    }
}

