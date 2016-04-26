package webonise.mapboxdemo;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MapboxMap mapboxMap;
    private MapView mapview;
    private boolean hasInternetPermission;
    private boolean hasAccessNetworkStatePermission;
    private boolean hasAccessCoarseLocationPermission;
    private boolean hasAccessFineLocationPermission;
    final List<LatLng> latLngList = new ArrayList<>();
    private boolean isShowEnable = true;
    private PolylineOptions polylineOptions;
    private double latIncrementConstants = 0;
    private double longIncrementConstants = 0;

    {
        latLngList.add(new LatLng(18.515600, 73.781900));
        latLngList.add(new LatLng(18.515700, 73.781901));
        latLngList.add(new LatLng(18.515800, 73.781902));
        latLngList.add(new LatLng(18.515900, 73.781903));
        latLngList.add(new LatLng(18.516000, 73.781904));
        latLngList.add(new LatLng(18.516100, 73.781905));
        latLngList.add(new LatLng(18.516200, 73.781906));
        latLngList.add(new LatLng(18.516300, 73.781907));
        latLngList.add(new LatLng(18.516400, 73.781908));
        latLngList.add(new LatLng(18.516500, 73.781909));
        latLngList.add(new LatLng(18.516600, 73.781910));
        latLngList.add(new LatLng(18.516700, 73.781911));
        latLngList.add(new LatLng(18.516800, 73.781912));
        latLngList.add(new LatLng(18.516900, 73.781913));
        latLngList.add(new LatLng(18.517000, 73.781914));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestForPermissions();
        initializeMapView(savedInstanceState);
        mapview.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                initMapBox(mapboxMap);
            }
        });
    }

    public void initMapBox(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        Toast.makeText(MainActivity.this, "Map box object initialized", Toast.LENGTH_SHORT).show();
    }

    private void requestForPermissions() {
        final PermissionUtil permissionUtil = new PermissionUtil(this);
        permissionUtil.checkPermission(Manifest.permission.INTERNET, new PermissionUtil.OnPermissionGranted() {
            @Override
            public void permissionGranted() {
                hasInternetPermission = true;
            }
        }, "Need INTERNET permission.");
        permissionUtil.checkPermission(Manifest.permission.ACCESS_NETWORK_STATE, new PermissionUtil.OnPermissionGranted() {
            @Override
            public void permissionGranted() {
                hasAccessNetworkStatePermission = true;
            }
        }, "Need ACCESS_NETWORK_STATE permission.");
        permissionUtil.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, new PermissionUtil.OnPermissionGranted() {
            @Override
            public void permissionGranted() {
                hasAccessCoarseLocationPermission = true;
            }
        }, "Need ACCESS_COARSE_LOCATION permission.");
        permissionUtil.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, new PermissionUtil.OnPermissionGranted() {
            @Override
            public void permissionGranted() {
                hasAccessFineLocationPermission = true;
            }
        }, "Need ACCESS_FINE_LOCATION permission.");
    }

    /**
     * Function to initialize mapbox view
     *
     * @param savedInstanceState
     */
    private void initializeMapView(Bundle savedInstanceState) {
        if (hasInternetPermission) {
            mapview = (MapView) findViewById(R.id.mapview);
            mapview.onCreate(savedInstanceState);
            mapview.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(MapboxMap mapboxMap) {

                    // Customize map with markers, polylines, etc.
                }
            });
        } else {
            requestForPermissions();
        }
    }

    public void onClickShowPolyLineButton(View view) {
        if (isShowEnable) {
            isShowEnable = false;
            drawPolyline();
        } else {
            isShowEnable = true;
            mapboxMap.removePolyline(polylineOptions.getPolyline());
        }
    }

    private void drawPolyline() {
        final LatLng[] points = latLngList.toArray(new LatLng[latLngList.size()]);
        polylineOptions = new PolylineOptions()
                .add(points)
                .color(Color.parseColor("#3bb2d0"))
                .width(2);
        mapboxMap.addPolyline(polylineOptions);
    }

    public void onClickAdNewPointButton(View view) {
        latIncrementConstants = latIncrementConstants + 0.0005;
        longIncrementConstants = longIncrementConstants - 0.0005;
        latLngList.add(new LatLng(18.515600 + latIncrementConstants,
                73.781914 + longIncrementConstants));
        drawPolyline();
    }
}
