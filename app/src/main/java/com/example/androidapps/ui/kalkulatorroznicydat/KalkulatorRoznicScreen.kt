package com.example.androidapps.ui.kalkulatorroznicydat

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import com.example.androidapps.ui.theme.AndroidAppsTheme
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidAppsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        KalkRoznic(modifier = Modifier.padding(24.dp).widthIn(max = 500.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun KalkRoznic(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val startCal = remember { Calendar.getInstance() }
    val endCal = remember { Calendar.getInstance() }

    //format daty/czasu do wyświetlenia (bez sekund)
    val sdf = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

    //czytelne stringi pokazujące wybrane daty
    var startDateTimeString by remember { mutableStateOf(sdf.format(startCal.time)) }
    var endDateTimeString by remember { mutableStateOf(sdf.format(endCal.time)) }

    //pola sekund
    var startSeconds by remember {
        mutableStateOf(
            startCal.get(Calendar.SECOND).toString().padStart(2, '0')
        )
    }
    var endSeconds by remember {
        mutableStateOf(
            endCal.get(Calendar.SECOND).toString().padStart(2, '0')
        )
    }

    var resultDays by remember { mutableStateOf<Long?>(null) }
    var resultHours by remember { mutableStateOf<Long?>(null) }
    var resultMinutes by remember { mutableStateOf<Long?>(null) }
    var resultSeconds by remember { mutableStateOf<Long?>(null) }

    var errorText by remember { mutableStateOf<String?>(null) }

    // Pokazuje DatePicker, potem TimePicker; NIE ustawia sekund — te ustawimy z pola tekstowego przy obliczaniu
    fun showDateTimePicker(targetCal: Calendar, updateString: (String) -> Unit) {
        val y = targetCal.get(Calendar.YEAR)
        val m = targetCal.get(Calendar.MONTH)
        val d = targetCal.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(context, { _: DatePicker, ny: Int, nm: Int, nd: Int ->
            targetCal.set(Calendar.YEAR, ny)
            targetCal.set(Calendar.MONTH, nm)
            targetCal.set(Calendar.DAY_OF_MONTH, nd)

            val hour = targetCal.get(Calendar.HOUR_OF_DAY)
            val minute = targetCal.get(Calendar.MINUTE)

            val timePicker = TimePickerDialog(context, { _, h: Int, min: Int ->
                targetCal.set(Calendar.HOUR_OF_DAY, h)
                targetCal.set(Calendar.MINUTE, min)
                // nie nadpisujemy sekund tutaj — zostaną ustawione z pola seconds przy obliczaniu
                updateString(sdf.format(targetCal.time))
            }, hour, minute, true)

            timePicker.show()
        }, y, m, d)

        datePicker.show()
    }

  
    fun displayWithSeconds(baseFormatted: String, secondsStr: String): String {
        val s = secondsStr.padStart(2, '0')
        return "$baseFormatted:$s"
    }

    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Kalkulator różnicy dat",
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

        Spacer(modifier = Modifier.height(36.dp))


        Row(
            modifier = Modifier.fillMaxWidth(0.9f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    errorText = null
                    showDateTimePicker(startCal) { newStr ->
                        startDateTimeString = newStr
                        resultDays = null; resultHours = null; resultMinutes = null; resultSeconds =
                        null
                    }
                },
                modifier = Modifier.weight(1f).height(65.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
            ) {
                Text(
                    text = "Start: ${displayWithSeconds(startDateTimeString, startSeconds)}",
                    fontSize = 14.sp
                )
            }

            // pole sekund start
            TextField(
                value = startSeconds,
                onValueChange = {
                    // tylko cyfry, max 2 znaki
                    val filtered = it.filter { ch -> ch.isDigit() }.take(2)
                    startSeconds = if (filtered.isEmpty()) "" else filtered
                },
                modifier = Modifier.widthIn(min = 88.dp),
                label = { Text("Sekundy") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))


        Row(
            modifier = Modifier.fillMaxWidth(0.9f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    errorText = null
                    showDateTimePicker(endCal) { newStr ->
                        endDateTimeString = newStr
                        resultDays = null; resultHours = null; resultMinutes = null; resultSeconds =
                        null
                    }
                },
                modifier = Modifier.weight(1f).height(65.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF26C6DA))
            ) {
                Text(
                    text = "End: ${displayWithSeconds(endDateTimeString, endSeconds)}",
                    fontSize = 14.sp
                )
            }

            //sek
            TextField(
                value = endSeconds,
                onValueChange = {
                    val filtered = it.filter { ch -> ch.isDigit() }.take(2)
                    endSeconds = if (filtered.isEmpty()) "" else filtered
                },
                modifier = Modifier.widthIn(min = 88.dp),
                label = { Text("Sekundy") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        //oblicz
        Button(
            onClick = {
                errorText = null
                resultDays = null; resultHours = null; resultMinutes = null; resultSeconds = null


                val sSec = startSeconds.toIntOrNull() ?: 0
                val eSec = endSeconds.toIntOrNull() ?: 0

                if (sSec !in 0..59 || eSec !in 0..59) {
                    errorText = "Sekundy muszą być liczbą w zakresie 0–59"
                    return@Button
                }


                startCal.set(Calendar.SECOND, sSec)
                startCal.set(Calendar.MILLISECOND, 0)
                endCal.set(Calendar.SECOND, eSec)
                endCal.set(Calendar.MILLISECOND, 0)

                val diffMillis = endCal.timeInMillis - startCal.timeInMillis

                if (diffMillis < 0) {
                    errorText = "Błąd: data końcowa jest wcześniejsza niż początkowa."
                    return@Button
                }

                var diffSec = diffMillis / 1000

                val days = diffSec / (24 * 3600)
                diffSec %= (24 * 3600)

                val hours = diffSec / 3600
                diffSec %= 3600

                val minutes = diffSec / 60
                val seconds = diffSec % 60

                resultDays = days
                resultHours = hours
                resultMinutes = minutes
                resultSeconds = seconds
            },
            modifier = Modifier.fillMaxWidth(0.75f).height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF46A216))
        ) {
            Text("Oblicz różnicę", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(14.dp))


        if (errorText != null) {
            Text(
                text = errorText!!,
                color = Color.Red,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        } else if (resultDays != null) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Różnica:", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    InfoBox(label = "Dni", value = resultDays.toString())
                    InfoBox(label = "Godz.", value = resultHours.toString())
                    InfoBox(label = "Min.", value = resultMinutes.toString())
                    InfoBox(label = "Sek.", value = resultSeconds.toString())
                }
            }
        } else {
            Text(
                text = "Wybierz daty, uzupełnij sekundy, a następnie naciśnij 'Oblicz różnicę'.",
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(36.dp))
    }
}

@Composable
fun InfoBox(label: String, value: String) {
    Column(
        modifier = Modifier.padding(6.dp).widthIn(min = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = label, fontSize = 14.sp)
    }
}

