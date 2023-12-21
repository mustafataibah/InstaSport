package com.amt.instasport.ui.onboarding

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
    var selectedCity by remember { mutableStateOf("") }
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
                ManualLocation(onCitySelected = { city -> selectedCity = city })
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
                Text("Use Device Location", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("or", color = Color.White, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    showOverlay(false)
                    showManual(true)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                ),
                border = BorderStroke(3.dp, MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .width(300.dp)
                    .height(50.dp)
            ) {
                Text("Manually Enter Location", fontSize = 16.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualLocation(onCitySelected: (String) -> Unit) {
    val cities = arrayOf("New York", "Boston", "San Francisco")
    var selectedCity by remember { mutableStateOf(cities[0]) }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {
                expanded = !expanded
            }) {
                OutlinedTextField(
                    value = selectedCity,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Sport") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    cities.forEach { item ->
                        DropdownMenuItem(text = { Text(text = item) }, onClick = {
                            selectedCity = item
                            expanded = false
                            onCitySelected(item)
                        })
                    }
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {}, modifier = Modifier
                .fillMaxWidth(.8f)
                .height(50.dp)
        ) {
            Text("Confirm Location")
        }
    }
}