package com.amt.instasport.ui.view

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GoogleMapComposable(navController: NavController) {
    val mapView = rememberMapViewWithLifecycle()
    var map: GoogleMap? by remember { mutableStateOf(null) }
    var showOverlay by remember { mutableStateOf(true) }

    val bostonCoordinates = LatLng(42.3601, -71.0589)

    val locationPermissionState =
        rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

    Box {
        AndroidView({ mapView }) { mapView ->
            mapView.getMapAsync { googleMap ->
                map = googleMap
                map?.moveCamera(CameraUpdateFactory.newLatLngZoom(bostonCoordinates, 15f))
            }
        }


        if (showOverlay) {
            Overlay(
                showOverlay = { showOverlay = it },
                locationPermissionState = locationPermissionState
            )
        } else {
            Button(
                onClick = { navController.navigate("dashboard") },
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 30.dp)
                    .width(300.dp)
                    .height(50.dp)
            ) {
                Text("Save", fontSize = 20.sp)
            }
        }
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Overlay(showOverlay: (Boolean) -> Unit, locationPermissionState: PermissionState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = {
                    locationPermissionState.launchPermissionRequest()
                    showOverlay(false)
                },
                Modifier
                    .width(300.dp)
                    .height(50.dp)
            ) {
                Text("Use Device Location", fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("or", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { showOverlay(false) },
                Modifier
                    .width(300.dp)
                    .height(50.dp)
            ) {
                Text("Manually Enter Location", fontSize = 20.sp)
            }
        }
    }
}


@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            onCreate(null)
        }
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle, mapView) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(null)
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> throw IllegalStateException("Unsupported lifecycle event $event")
            }
        }
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
            mapView.onDestroy()
        }
    }

    return mapView
}
