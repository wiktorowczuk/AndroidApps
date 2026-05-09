package com.example.androidapps.ui.wyswietlaniestron

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.androidapps.ui.theme.AndroidAppsTheme
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import kotlin.apply
import kotlin.text.startsWith


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidAppsTheme {
                WebViewScreen()
            }
        }
    }
}


fun formatUrl(input: String): String {
    return if (input.startsWith("http://") || input.startsWith("https://")) {
        input
    } else {
        "https://$input"
    }
}

@Composable
fun WebViewScreen() {

    var url by remember { mutableStateOf("https://www.google.com") }
    var textFieldValue by remember { mutableStateOf(url) }
    var webViewRef by remember { mutableStateOf<WebView?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(top = 32.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                onClick = {
                    if (webViewRef?.canGoBack() == true) {
                        webViewRef?.goBack()
                    }
                },
                //colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.height(54.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Wstecz"
                )
            }

            OutlinedTextField(
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                label = { Text("Wpisz URL strony") },
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onDone = { url = formatUrl(textFieldValue) }
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.weight(1f).padding(4.dp)
            )

            Button(
                onClick = {
                    url = formatUrl(textFieldValue)
                },
                modifier = Modifier.height(54.dp)


            ) {
                Text(
                    text = "Przejdź\ndo strony",
                    textAlign = TextAlign.Center
                )
            }
        }

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    webViewClient = WebViewClient()
                    loadUrl(url)
                    webViewRef = this
                }
            },
            update = { webView ->
                if (webView.url != url) {
                    webView.loadUrl(url)
                }
            }
        )
    }



}

