package com.example.androidapps.ui.ustawianiehasla

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.androidapps.ui.theme.AndroidAppsTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.OffsetMapping
import kotlin.collections.joinToString
import kotlin.text.all
import kotlin.text.count
import kotlin.text.indices
import kotlin.text.isDigit
import kotlin.text.isEmpty
import kotlin.text.isLetterOrDigit
import kotlin.text.isNotBlank
import kotlin.text.lastIndex
import kotlin.text.toIntOrNull

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidAppsTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PasswordRulesScreen()
                }
            }
        }
    }
}

class LastCharVisibleTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val input = text.text

        if (input.isEmpty()) {
            return TransformedText(
                androidx.compose.ui.text.AnnotatedString(""),
                OffsetMapping.Identity
            )
        }

        val masked = buildString {
            for (i in input.indices) {
                if (i == input.lastIndex) {
                    append(input[i]) // ostatni znak widoczny
                } else {
                    append("*") // reszta ukryta
                }
            }
        }

        return TransformedText(
            androidx.compose.ui.text.AnnotatedString(masked),
            OffsetMapping.Identity
        )
    }
}

@Composable
fun PasswordRulesScreen() {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    var minLength by rememberSaveable { mutableIntStateOf(8) }
    var digitCount by rememberSaveable { mutableIntStateOf(1) }
    var specialCharCount by rememberSaveable { mutableIntStateOf(1) }

    var password by rememberSaveable { mutableStateOf("") }
    var validationMessage by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Ustawienia hasła",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Minimalna długość: $minLength")
        Text(text = "Ilość cyfr: $digitCount")
        Text(text = "Ilość znaków specjalnych: $specialCharCount")

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { showDialog = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2563EB),
                contentColor = Color.White
            )
        ) {
            Text("Ustaw limity")
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            //visualTransformation = LastCharVisibleTransformation(),
            label = { Text("Wpisz hasło do sprawdzenia") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = {
                validationMessage = validatePassword(
                    password = password,
                    minLength = minLength,
                    requiredDigits = digitCount,
                    requiredSpecialChars = specialCharCount
                )
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE39500),
                contentColor = Color.White
            )
        ) {
            Text("Sprawdź hasło")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (validationMessage.isNotBlank()) {
            Text(
                text = validationMessage,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }

    PasswordRulesDialog(
        showDialog = showDialog,
        onDismiss = { showDialog = false },
        onConfirm = { newMinLength, newDigitCount, newSpecialCharCount ->
            minLength = newMinLength
            digitCount = newDigitCount
            specialCharCount = newSpecialCharCount
            showDialog = false
        }
    )
}

@Composable
fun PasswordRulesDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (minLength: Int, digitCount: Int, specialCharCount: Int) -> Unit
) {
    if (!showDialog) return

    var minLengthText by rememberSaveable { mutableStateOf("") }
    var digitCountText by rememberSaveable { mutableStateOf("") }
    var specialCharCountText by rememberSaveable { mutableStateOf("") }

    val minLength = minLengthText.toIntOrNull()
    val digitCount = digitCountText.toIntOrNull()
    val specialCharCount = specialCharCountText.toIntOrNull()

    val isFormValid =
        minLength != null && minLength > 0 &&
                digitCount != null && digitCount >= 0 &&
                specialCharCount != null && specialCharCount >= 0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Limity hasła")
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = minLengthText,
                    onValueChange = { input ->
                        if (input.all { it.isDigit() }) {
                            minLengthText = input
                        }
                    },
                    label = { Text("Minimalna długość") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = digitCountText,
                    onValueChange = { input ->
                        if (input.all { it.isDigit() }) {
                            digitCountText = input
                        }
                    },
                    label = { Text("Ilość cyfr") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = specialCharCountText,
                    onValueChange = { input ->
                        if (input.all { it.isDigit() }) {
                            specialCharCountText = input
                        }
                    },
                    label = { Text("Ilość znaków specjalnych") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(
                        minLength ?: 0,
                        digitCount ?: 0,
                        specialCharCount ?: 0
                    )
                },
                enabled = isFormValid
            ) {
                Text("Zapisz")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Anuluj")
            }
        }
    )
}

fun validatePassword(
    password: String,
    minLength: Int,
    requiredDigits: Int,
    requiredSpecialChars: Int
): String {
    val digitCount = password.count { it.isDigit() }
    val specialCharCount = password.count { !it.isLetterOrDigit() }

    val errors = mutableListOf<String>()

    if (password.length < minLength) {
        errors.add("Hasło musi mieć co najmniej $minLength znaków.")
    }

    if (digitCount < requiredDigits) {
        errors.add("Hasło musi zawierać co najmniej $requiredDigits cyfr.")
    }

    if (specialCharCount < requiredSpecialChars) {
        errors.add("Hasło musi zawierać co najmniej $requiredSpecialChars znaków specjalnych.")
    }

    return if (errors.isEmpty()) {
        "Hasło spełnia wszystkie wymagania."
    } else {
        errors.joinToString("\n")
    }
}