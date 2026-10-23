
package com.example.myapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.myapp.ui.theme.Naranjo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onPasajeroClick: () -> Unit,
    onAdminClick: () -> Unit,
    onConductorClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Belmonte")
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Bienvenido",
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "¿A qué área deseas ingresar?",
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onPasajeroClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Naranjo
                )
            ) {
                Text("Pasajero")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onAdminClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Administrador")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onConductorClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Conductor")
            }
        }
    }
}
