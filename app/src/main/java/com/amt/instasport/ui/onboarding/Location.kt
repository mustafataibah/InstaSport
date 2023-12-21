package com.amt.instasport.ui.onboarding

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.amt.instasport.ui.component.MapComposable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationScreen(navController: NavController) {
    var showOverlay by remember { mutableStateOf(true) }
    var showManual by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var userLocation by remember { mutableStateOf<Location?>(null) }
    val context = LocalContext.current
    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    val locationPermissionState =
        rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)


    LaunchedEffect(key1 = locationPermissionState.status) {
        if (locationPermissionState.status.isGranted) {
            fetchLocation(fusedLocationClient) { location ->
                userLocation = location
                Log.d(
                    "LocationScreen", "User location: ${location.latitude}, ${location.longitude}"
                )
            }
        }
    }

    Box {
        if (showOverlay) {
            Overlay(showOverlay = { showOverlay = it },
                locationPermissionState = locationPermissionState,
                showManual = { showManual = it })
        } else if (showManual) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ManualLocation { latLng ->
                    userLocation = Location("").apply {
                        latitude = latLng.latitude
                        longitude = latLng.longitude
                    }
                    showManual = false
                }
            }
        } else {
            Column(
                Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.weight(1f))
                userLocation?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    ElevatedCard(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                            .height(600.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        ),
                    ) {
                        MapComposable(location = latLng)
                    }
                }
                Button(
                    onClick = {
                        navController.navigate("dashboard")
                    },
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text("Finish", fontSize = 20.sp)
                }
                Spacer(Modifier.weight(1f))
            }
        }
    }
}

@SuppressLint("MissingPermission")
private fun fetchLocation(
    fusedLocationClient: FusedLocationProviderClient, onResult: (Location) -> Unit
) {
    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
        location?.let { onResult(it) }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Overlay(
    showOverlay: (Boolean) -> Unit,
    locationPermissionState: PermissionState,
    showManual: (Boolean) -> Unit
) {
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
                onClick = {
                    showOverlay(false)
                    showManual(true)
                },
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
fun ManualLocation(onCitySelected: (LatLng) -> Unit) {
    val cities = listOf("New York", "Boston", "San Francisco")
    var selectedCity by remember { mutableStateOf(cities.first()) }
    var expanded by remember { mutableStateOf(false) }
    val cityCoordinates = mapOf(
        "New York" to LatLng(40.7128, -74.0060),
        "Boston" to LatLng(42.3601, -71.0589),
        "San Francisco" to LatLng(37.7749, -122.4194)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selectedCity)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            // TODO: DropDown Items
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val selectedCoordinates = cityCoordinates[selectedCity] ?: return@Button
                onCitySelected(selectedCoordinates)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirm Location")
        }
    }
}