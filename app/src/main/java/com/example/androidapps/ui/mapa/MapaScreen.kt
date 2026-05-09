package com.example.androidapps.ui.mapa

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.*



@Composable
fun MapaScreen() {

    val context = LocalContext.current

    var latText by remember { mutableStateOf("") }
    var lngText by remember { mutableStateOf("") }

    var markerPosition by remember { mutableStateOf<LatLng?>(null) }

    val cameraPositionState = rememberCameraPositionState()

    Column(modifier = Modifier.fillMaxSize()) {

        GoogleMap(
            modifier = Modifier.fillMaxWidth().weight(1f),
            cameraPositionState = cameraPositionState
        ) {
            markerPosition?.let { position ->
                Marker(
                    state = MarkerState(position = position),
                    title = "Wybrany punkt"
                )
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {

            OutlinedTextField(
                value = latText,
                onValueChange = { latText = it },
                label = { Text("Szerokość geograficzna") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = lngText,
                onValueChange = { lngText = it },
                label = { Text("Długość geograficzna") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {

                    val lat = latText.toDoubleOrNull()
                    val lng = lngText.toDoubleOrNull()

                    if (lat == null || lat !in -90.0..90.0 ||
                        lng == null || lng !in -180.0..180.0
                    ) {
                        Toast.makeText(
                            context,
                            "Podaj poprawne współrzędne",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    val location = LatLng(lat, lng)

                    markerPosition = location

                    cameraPositionState.move(
                        CameraUpdateFactory.newLatLngZoom(location, 12f)
                    )
                }
            ) {
                Text("Pokaż na mapie")
            }
        }
    }
}

