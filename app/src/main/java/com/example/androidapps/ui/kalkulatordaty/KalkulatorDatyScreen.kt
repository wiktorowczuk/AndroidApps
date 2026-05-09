package com.example.androidapps.ui.kalkulatordaty

import com.example.androidapps.ui.theme.AndroidAppsTheme
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import java.util.Calendar
import androidx.compose.ui.graphics.Brush
import kotlin.collections.forEach
import kotlin.let
import kotlin.text.filter
import kotlin.text.format
import kotlin.text.isDigit
import kotlin.text.take
import kotlin.text.toIntOrNull


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidAppsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center)
                    {
                        KalkDaty(modifier = Modifier.padding(24.dp).widthIn(max = 500.dp))
                    }
                }
            }
        }
    }
}



@Composable
fun KalkDaty(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    //stan wybranej daty jako calendar+ jako czytelny string
    val selectedCalendar = remember { Calendar.getInstance() }
    var selectedDateString by remember {
        mutableStateOf(
            "${selectedCalendar.get(Calendar.DAY_OF_MONTH)}/${
                selectedCalendar.get(
                    Calendar.MONTH
                ) + 1
            }/${selectedCalendar.get(Calendar.YEAR)}"
        )
    }

    val year = selectedCalendar.get(Calendar.YEAR)
    val month = selectedCalendar.get(Calendar.MONTH)
    val day = selectedCalendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog( //zmienna okna wyboru daty
        context,
        { _: DatePicker, y: Int, m: Int, d: Int -> selectedCalendar.set(y, m, d)
            selectedDateString = "${d}/${m + 1}/$y" },
        year, month, day //domyslna data
    )

    // stan pola liczby i jednostki
    var numberText by remember { mutableStateOf("") }
    var selectedUnit by remember { mutableStateOf("dni") }

    // wynik i ew. blad
    var resultDateString by remember { mutableStateOf<String?>(null) }
    var errorText by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()), //responsywnie
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(
            text = "Kalkulator daty",
            fontSize = 32.sp,
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


        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = { datePickerDialog.show() },
            modifier = Modifier.fillMaxWidth(0.6f).height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
        ) { Text(selectedDateString, fontSize = 20.sp) }

        Spacer(modifier = Modifier.height(16.dp))

        // pole "wpisz liczbe" razem z polem do wyboru jednostki (nizej)
        PolaDoWpisywania(
            numberText_ = numberText,
            onNumberTextChange = { input ->
                numberText = input.filter { it.isDigit() }.take(6)
            },//same liczby,
            selectedUnit_ = selectedUnit,
            onUnitChange = { selectedUnit = it })

        Spacer(modifier = Modifier.height(16.dp))

        //przycisk przesuwajacy date
        Button(
            onClick =
                {
                    errorText = null
                    resultDateString = null

                    val amount = numberText.toIntOrNull()
                    if (amount == null || amount < 0 || amount > 500_000) {
                        errorText = "Wpisz poprawną nieujemną liczbę \n (z zakresu 0 - 500 000)"
                        return@Button
                    }


                    val resultCal = selectedCalendar.clone() as Calendar
                    when (selectedUnit) {
                        "dni" -> resultCal.add(Calendar.DAY_OF_MONTH, amount)
                        "tygodnie" -> resultCal.add(Calendar.WEEK_OF_YEAR, amount)
                        "miesiące" -> resultCal.add(Calendar.MONTH, amount)
                        else -> resultCal.add(Calendar.DAY_OF_MONTH, amount)
                    }

                    val d = resultCal.get(Calendar.DAY_OF_MONTH)
                    val m = resultCal.get(Calendar.MONTH) + 1
                    val y = resultCal.get(Calendar.YEAR)
                    resultDateString = String.format("%02d/%02d/%04d", d, m, y)
                },
            modifier = Modifier.fillMaxWidth(0.75f).height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
        )
        { Text("Przesuń datę do przodu", fontSize = 20.sp) }

        Spacer(modifier = Modifier.height(12.dp))

        //pokazanie bledu lub wyniku
        if (errorText != null) {
            Text(
                text = errorText!!,
                color = Color.Red,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        } else if (resultDateString != null) {
            Text(text = resultDateString?.let { "Nowa data: $it" } ?: "Wynik:",
                modifier = Modifier.padding(top = 8.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PolaDoWpisywania(numberText_: String,
                     onNumberTextChange: (String) -> Unit,
                     selectedUnit_: String,
                     onUnitChange: (String) -> Unit,
                     modifier: Modifier = Modifier)
{
    val units = listOf("dni", "tygodnie", "miesiące")
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        TextField(
            value = numberText_,
            onValueChange = onNumberTextChange,
            placeholder = { Text("Wpisz liczbę", fontSize = 18.sp) },
            modifier = Modifier.weight(0.4f).widthIn(min = 96.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            singleLine = true,
            maxLines = 1
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.weight(0.6f)
        )
        {
            TextField(
                value = selectedUnit_,
                onValueChange = {},
                readOnly = true,
                label = { Text("Jednostka") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                singleLine = true,
                maxLines = 1
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                units.forEach { unit ->
                    DropdownMenuItem(
                        text = { Text(unit) },
                        onClick = { onUnitChange(unit); expanded = false })
                }
            }
        }
    }
}

