package webonise.mapboxdemo;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

public class MainActivity extends AppCompatActivity {

    private MapView mapview;
    private boolean hasInternetPermission;
    private boolean hasAccessNetworkStatePermission;
    private boolean hasAccessCoarseLocationPermission;
    private boolean hasAccessFineLocationPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestForPermissions();
        initializeMapView(savedInstanceState);
    }

    private void requestForPermissions() {
        final PermissionUtil permissionUtil = new PermissionUtil(this);
        permissionUtil.checkPermission(Manifest.permission.INTERNET, new PermissionUtil.OnPermissionGranted() {
            @Override
            public void permissionGranted() {
                hasInternetPermission = true;
            }
        },"Need INTERNET permission.");

        permissionUtil.checkPermission(Manifest.permission.ACCESS_NETWORK_STATE, new PermissionUtil.OnPermissionGranted() {
            @Override
            public void permissionGranted() {
                hasAccessNetworkStatePermission = true;
            }
        },"Need ACCESS_NETWORK_STATE permission.");
        permissionUtil.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, new PermissionUtil.OnPermissionGranted() {
            @Override
            public void permissionGranted() {
                hasAccessCoarseLocationPermission = true;
            }
        },"Need ACCESS_COARSE_LOCATION permission.");
        permissionUtil.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, new PermissionUtil.OnPermissionGranted() {
            @Override
            public void permissionGranted() {
                hasAccessFineLocationPermission = true;
            }
        },"Need ACCESS_FINE_LOCATION permission.");
    }

    /**
     * Function to initialize mapbox view
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
        }else{
            requestForPermissions();
        }
    }
}
