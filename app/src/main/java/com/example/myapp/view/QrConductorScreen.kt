
package com.example.myapp.view

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

private val AzulBelmonte = Color(0xFF193E58)
private val FondoBelmonte = Color(0xFFF0F5FA)
private val VerdeBelmonte = Color(0xFF16845B)

@Composable
fun QrConductorScreen(
    onConfirmar: () -> Unit,
    onVolver: () -> Unit
) {
    val context = LocalContext.current

    var permisoConcedido by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var qrReconocido by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    val permisoCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        permisoConcedido = concedido
    }

    LaunchedEffect(Unit) {
        if (!permisoConcedido) {
            permisoCamara.launch(Manifest.permission.CAMERA)
        }
    }

    Scaffold(
        containerColor = FondoBelmonte
    ) { innerPadding ->

        if (qrReconocido) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    modifier = Modifier.size(110.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFFF8F7FC),
                    shadowElevation = 2.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "B",
                            fontSize = 94.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF08234A),
                            lineHeight = 96.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                Text(
                    text = "Transportes Belmonte",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AzulBelmonte,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(30.dp))

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = FondoBelmonte,
                            shape = RoundedCornerShape(50)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✓",
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerdeBelmonte
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "¡Gracias por usar nuestra aplicación!",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AzulBelmonte,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(22.dp))

                Text(
                    text = "Te deseamos un feliz viaje.",
                    fontSize = 18.sp,
                    color = Color(0xFF40516A),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Pulsa el botón para continuar.",
                    fontSize = 16.sp,
                    color = Color(0xFF687D98),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onConfirmar,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulBelmonte
                    )
                ) {
                    Text(
                        text = "Continuar",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Escanear QR de Belmonte",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AzulBelmonte
                )

                Text(
                    text = "Apunta la cámara al código QR de Transportes Belmonte.",
                    style = MaterialTheme.typography.bodyLarge
                )

                if (!permisoConcedido) {
                    Text("Se necesita permiso para utilizar la cámara.")

                    Button(
                        onClick = {
                            permisoCamara.launch(Manifest.permission.CAMERA)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Permitir cámara")
                    }
                } else {
                    QrCameraPreview(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(360.dp),
                        onQrLeido = { contenido ->
                            if (contenido.trim() == "BELMONTE_APP") {
                                qrReconocido = true
                                mensajeError = null
                            } else {
                                mensajeError =
                                    "Este código QR no corresponde a Transportes Belmonte."
                            }
                        }
                    )
                }

                mensajeError?.let { mensaje ->
                    Text(
                        text = mensaje,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = onVolver,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulBelmonte
                    )
                ) {
                    Text("Volver al área del pasajero")
                }
            }
        }
    }
}

@OptIn(markerClass = [ExperimentalGetImage::class])
@Composable
private fun QrCameraPreview(
    modifier: Modifier = Modifier,
    onQrLeido: (String) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val scanner = remember {
        BarcodeScanning.getClient()
    }

    val cameraExecutor = remember {
        Executors.newSingleThreadExecutor()
    }

    val qrEncontrado = remember {
        AtomicBoolean(false)
    }

    DisposableEffect(scanner, cameraExecutor) {
        onDispose {
            scanner.close()
            cameraExecutor.shutdown()
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { viewContext ->
            val previewView = PreviewView(viewContext)
            val cameraProviderFuture =
                ProcessCameraProvider.getInstance(viewContext)

            cameraProviderFuture.addListener(
                {
                    try {
                        val cameraProvider = cameraProviderFuture.get()

                        val preview = Preview.Builder()
                            .build()
                            .also {
                                it.surfaceProvider =
                                    previewView.surfaceProvider
                            }

                        val imageAnalysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(
                                ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
                            )
                            .build()

                        imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                            val mediaImage = imageProxy.image

                            if (mediaImage == null || qrEncontrado.get()) {
                                imageProxy.close()
                            } else {
                                val image = InputImage.fromMediaImage(
                                    mediaImage,
                                    imageProxy.imageInfo.rotationDegrees
                                )

                                scanner.process(image)
                                    .addOnSuccessListener { barcodes ->
                                        val codigo = barcodes.firstOrNull {
                                            it.format == Barcode.FORMAT_QR_CODE
                                        }

                                        val contenido = codigo?.rawValue

                                        if (
                                            contenido != null &&
                                            qrEncontrado.compareAndSet(
                                                false,
                                                true
                                            )
                                        ) {
                                            onQrLeido(contenido)
                                        }
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.e(
                                            "QrConductorScreen",
                                            "Error al leer el QR",
                                            exception
                                        )
                                    }
                                    .addOnCompleteListener {
                                        imageProxy.close()
                                    }
                            }
                        }

                        cameraProvider.unbindAll()

                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_BACK_CAMERA,
                            preview,
                            imageAnalysis
                        )
                    } catch (exception: Exception) {
                        Log.e(
                            "QrConductorScreen",
                            "No se pudo iniciar la cámara",
                            exception
                        )
                    }
                },
                ContextCompat.getMainExecutor(viewContext)
            )

            previewView
        }
    )
}
