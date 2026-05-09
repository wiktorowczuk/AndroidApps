package com.example.androidapps.ui.menu


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun MenuScreen(navController: NavController) {

    Column(
        modifier = Modifier.fillMaxSize().padding(28.dp),
        verticalArrangement = Arrangement.spacedBy(48.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Menedżer aplikacji", fontSize = 22.sp)

        //1 rzad
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp), modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { navController.navigate("kalkulatordaty") },
                modifier = Modifier.weight(1f).height(100.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFF59827))
            )
            {
                Text(
                    text = "Kalkulator daty",
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp
                )
            }

            Button(
                onClick = { navController.navigate("kalkulatorroznicy") },
                modifier = Modifier.weight(1f).height(100.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFF59827))
            )
            {
                Text(
                    text = "Kalkulator Różnicy Dat",
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp
                )
            }
        }

        //2 rzad
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp), modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { navController.navigate("konwerterliczb") },
                modifier = Modifier.weight(1f).height(100.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFF59827))
            )
            {
                Text(
                    text = "Konwerter liczb całkowitych",
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp
                )
            }

            Button(
                onClick = { navController.navigate("mapa") },
                modifier = Modifier.weight(1f).height(100.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFF59827))
            )
            {
                Text(
                    text = "Eksplorator mapy",
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp
                )
            }
        }

        //3 rzad
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp), modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { navController.navigate("listy") },
                modifier = Modifier.weight(1f).height(100.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFF59827))
            )
            {
                Text(
                    text = "Listy elementów",
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp
                )
            }

            Button(
                onClick = { navController.navigate("strony") },
                modifier = Modifier.weight(1f).height(100.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFF59827))
            )
            {
                Text(
                    text = "Prezentacja stron HTML",
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp
                )
            }
        }

        //4 rzad
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp), modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { navController.navigate("blokowanieznakow") },
                modifier = Modifier.weight(1f).height(100.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFF59827))
            )
            {
                Text(
                    text = "Blokada określonych znaków",
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp
                )
            }

            Button(
                onClick = { navController.navigate("haslo") },
                modifier = Modifier.weight(1f).height(100.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFF59827))
            )
            {
                Text(
                    text = "Ustawianie hasła",
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp
                )
            }
        }
    }
}

