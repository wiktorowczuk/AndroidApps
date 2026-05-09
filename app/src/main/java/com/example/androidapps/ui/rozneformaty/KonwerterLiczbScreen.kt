package com.example.androidapps.ui.rozneformaty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.androidapps.ui.theme.AndroidAppsTheme
import kotlin.collections.forEach
import kotlin.collections.joinToString
import kotlin.collections.toList
import kotlin.text.digitToInt
import kotlin.text.forEachIndexed
import kotlin.text.isDigit
import kotlin.text.map
import kotlin.text.padStart
import kotlin.text.take
import kotlin.text.toIntOrNull
import kotlin.text.toString
import kotlin.text.uppercase

enum class NumberFormatOption(val label: String) {
    BINARY("Binarny"),
    OCTAL("Ósemkowy"),
    HEXADECIMAL("Szesnastkowy"),
    BCD("BCD (Dwójkowo-dziesiętny)")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidAppsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        NumberFormatScreen(
                            modifier = Modifier
                                .padding(24.dp)
                                .widthIn(max = 500.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NumberFormatScreen(modifier: Modifier = Modifier) {

    var inputText by remember { mutableStateOf("") }
    var selectedFormat by remember { mutableStateOf(NumberFormatOption.BINARY) }
    var resultText by remember { mutableStateOf<String?>(null) }
    var errorText by remember { mutableStateOf<String?>(null) }

    val formats = NumberFormatOption.values().toList()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Konwerter liczby całkowitej na różne formaty",
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            style = LocalTextStyle.current.copy(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF7B1FA2),
                        Color(0xFFD3C331),
                        Color(0xFF1976D2),
                        Color(0xFF26C6DA),
                        Color(0xFFFF9800),
                        Color(0xFF59FF00)
                    )
                )
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = inputText,
            onValueChange = { newValue ->

                val filtered = buildString {
                    newValue.forEachIndexed { index, ch ->
                        if (ch.isDigit() || (ch == '-' && index == 0)) {
                            append(ch)
                        }
                    }
                }.take(9)

                inputText = filtered
            },
            label = { Text("Wpisz liczbę całkowitą") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Wybierz format:",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup()
        ) {

            formats.forEach { format ->

                Row(
                    modifier = Modifier.fillMaxWidth().selectable(
                        selected = selectedFormat == format,
                        onClick = { selectedFormat = format },
                        role = Role.RadioButton
                    ).padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    RadioButton(
                        selected = selectedFormat == format,
                        onClick = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(text = format.label)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                9
                errorText = null
                resultText = null

                val number = inputText.toIntOrNull()

                if (number == null) {
                    errorText = "Wpisz poprawną liczbę całkowitą w zakresie 0 - 99999999."
                    return@Button
                }

                resultText = when (selectedFormat) {
                    NumberFormatOption.BINARY -> number.toString(2)

                    NumberFormatOption.OCTAL -> number.toString(8)

                    NumberFormatOption.HEXADECIMAL -> number.toString(16).uppercase()

                    NumberFormatOption.BCD -> {
                        if (number < 0) {
                            errorText =
                                "standardowe BCD obsługuje tylko liczby nieujemne. Wpisz liczbę nieujemną."
                            return@Button
                        }

                        number.toString().map {
                            it.digitToInt().toString(2).padStart(4, '0')
                        }.joinToString(" ")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(0.7f).height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {

            Text("Pokaż wynik", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = resultText ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Wynik") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (errorText != null) {
            Text(
                text = errorText!!,
                color = Color.Red,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

