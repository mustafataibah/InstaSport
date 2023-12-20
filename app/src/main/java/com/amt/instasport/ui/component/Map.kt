package com.amt.instasport.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.viewinterop.AndroidView
import com.amt.instasport.ui.view.rememberMapViewWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

@Composable
fun MapComposable() {
    val mapView = rememberMapViewWithLifecycle()
    var map: GoogleMap? by remember { mutableStateOf(null) }
    val bostonCoordinates = LatLng(42.3601, -71.0589)

    Box {
        AndroidView({ mapView }) { mapView ->
            mapView.getMapAsync { googleMap ->
                map = googleMap
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bostonCoordinates, 15f))
            }
        }
    }
}
