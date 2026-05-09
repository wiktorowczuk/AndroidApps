package com.example.androidapps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.ui.viewinterop.AndroidView
import com.example.androidapps.ui.menu.MenuScreen
import com.example.androidapps.ui.theme.AndroidAppsTheme
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.androidapps.ui.blokowanieznakow.BlokZnakow


import com.example.androidapps.ui.kalkulatordaty.KalkDaty
import com.example.androidapps.ui.kalkulatorroznicydat.KalkRoznic
import com.example.androidapps.ui.listy.DualListScreen
import com.example.androidapps.ui.mapa.MapaScreen
import com.example.androidapps.ui.rozneformaty.NumberFormatScreen
import com.example.androidapps.ui.ustawianiehasla.PasswordRulesScreen
import com.example.androidapps.ui.wyswietlaniestron.WebViewScreen
import com.google.android.gms.maps.MapView


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidAppsTheme {
                val navController = rememberNavController()


                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Box(modifier = Modifier.fillMaxSize()) {

                    NavHost(
                        navController = navController,
                        startDestination = "menu"
                    ) {
                        composable("menu") {
                            MenuScreen(navController)
                        }
                        composable("kalkulatordaty") {
                            KalkDaty()
                        }
                        composable("kalkulatorroznicy") {
                            KalkRoznic()
                        }
                        composable("konwerterliczb") {
                            NumberFormatScreen()
                        }
                        composable("mapa") {
                            MapaScreen()
                        }
                        composable("listy") {
                            DualListScreen()
                        }
                        composable("strony") {
                            WebViewScreen()
                        }
                        composable( "blokowanieznakow") {
                            BlokZnakow()
                        }
                        composable("haslo") {
                            PasswordRulesScreen()
                        }
                    }

                    if (currentRoute != "menu") {
                        FloatingActionButton(
                            onClick = {
                                navController.navigate("menu") {
                                    navController.popBackStack("menu", false)
                                } },
                            modifier = Modifier.align(Alignment.BottomStart).padding(24.dp)
                        ) {
                            Text("Menu")
                        }
                    }
                }

            }
        }
    }
}