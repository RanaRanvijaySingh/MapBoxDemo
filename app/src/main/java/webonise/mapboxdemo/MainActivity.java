package webonise.mapboxdemo;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;

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
    private Polyline mPolyline;
    private GeometryFactory geometryFactory;

    //These coordinates are of BHAVDAN, PUNE, MAHARASHTRA, INDIA
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

    final List<LatLng> latLngPolygon = new ArrayList<>();

    {
        latLngPolygon.add(new LatLng(28.6139, 77.2090));//delhi
        latLngPolygon.add(new LatLng(22.2587, 71.1924));//gujarat
        latLngPolygon.add(new LatLng(18.5204, 73.8567));//pune
        latLngPolygon.add(new LatLng(12.9716, 77.5946));//banglore
        latLngPolygon.add(new LatLng(25.5941, 85.1376));//patna
        latLngPolygon.add(new LatLng(28.6139, 77.2090));//delhi
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

    /**
     * Function to initialize mapbox
     *
     * @param mapboxMap MapboxMap
     */
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

    /**
     * * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * * ++++++++++++++++++++++++++ TOGGLE POLYLINE EXAMPLE++++++++++++++++++++++++++++++++++++++
     * * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /**
     * Function to toggle polyline drawn
     *
     * @param view View
     */
    public void onClickShowPolyLineButton(View view) {
        if (isShowEnable) {
            isShowEnable = false;
            drawPolyline();
        } else {
            isShowEnable = true;
            mapboxMap.removePolyline(mPolyline);
        }
        Toast.makeText(this, "Zoom to area of BHAVDAN, PUNE, MAHARASHTRA, INDIA",
                Toast.LENGTH_SHORT).show();
    }

    private void drawPolyline() {
        final LatLng[] points = latLngList.toArray(new LatLng[latLngList.size()]);
        polylineOptions = new PolylineOptions()
                .add(points)
                .color(Color.parseColor("#3bb2d0"))
                .width(2);
        if (mPolyline != null) {
            mPolyline.remove();
            mPolyline = null;
        }
        mPolyline = mapboxMap.addPolyline(polylineOptions);
    }

    /**
     * * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * * ++++++++++++++++++++++++++ ADD NEW POINT EXAMPLE ++++++++++++++++++++++++++++++++++++++++
     * * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /**
     * Function is called on click of add new point button
     * This function adds a new point and links the previous
     * line with new created point.
     *
     * @param view View
     */
    public void onClickAdNewPointButton(View view) {
        latIncrementConstants = latIncrementConstants + 0.0005;
        longIncrementConstants = longIncrementConstants - 0.0005;
        //Location - Bhavdan, Pune, Maharashtra, India
        latLngList.add(new LatLng(18.515600 + latIncrementConstants,
                73.781914 + longIncrementConstants));
        drawPolyline();
        Toast.makeText(this, "Zoom to area of BHAVDAN, PUNE, MAHARASHTRA, INDIA",
                Toast.LENGTH_SHORT).show();    }

    /**
     * * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * * ++++++++++++++++++++++++++ BUFFERED AREA CODE +++++++++++++++++++++++++++++++++++++++++++
     * * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /**
     * Function is called on click of Buffer Example button
     *
     * @param view View
     */
    public void onClickBufferExample(View view) {
        //Initialize geometry factory object to get Geometry object.
        geometryFactory = new GeometryFactory();
        //Create geometry object using your own lat lang points
        //TODO : latLngPolygon - Used in this example is to show a bigger picture. Replace it
        //TODO : with your requirement.
        Geometry geometryOriginal = getGeometryForPolygon(latLngPolygon);
        //Draw polygon on map
        createPolygon(geometryOriginal);
        /**
         * Create geometry object with given buffer distance
         * Now buffer distance will vary on your requirement
         * Range could be anything
         * Hit and try
         */
        Geometry geometryBuffered = geometryOriginal.buffer(1);
        //Draw buffer polygon
        createPolygon(geometryBuffered);
    }

    /**
     * Function to get Geometry object (Class from vividsolutions)
     * from given list of latlng
     *
     * @param bounds List
     * @return Geometry (Class from vividsolutions)
     */
    public Geometry getGeometryForPolygon(List<LatLng> bounds) {
        List<Coordinate> coordinates = getCoordinatesList(bounds);
        if (!coordinates.isEmpty()) {
            return geometryFactory.createPolygon(getLinearRing(coordinates), null);
        }
        return null;
    }

    /**
     * Function to create a list of coordinates from a list of lat lng
     *
     * @param listLatLng list<LatLng>
     * @return List<Coordinate> (Class from vividsolutions)
     */
    private List<Coordinate> getCoordinatesList(List<LatLng> listLatLng) {
        List<Coordinate> coordinates = new ArrayList<>();
        for (int i = 0; i < listLatLng.size(); i++) {
            coordinates.add(new Coordinate(
                    listLatLng.get(i).getLatitude(), listLatLng.get(i).getLongitude()));
        }
        return coordinates;
    }

    /**
     * Function to create a polygon on the map
     *
     * @param geometry Geometry Class from vividsolutions
     */
    private void createPolygon(Geometry geometry) {
        LatLng[] points = getPoints(geometry.getCoordinates());
        mapboxMap.addPolyline(new PolylineOptions()
                .add(points)
                .width(4)
                .color(Color.parseColor("#FF0000")));
    }

    /**
     * Function to convert array of Coordinates (Class from vividsolutions)
     * to Android LatLng array
     *
     * @param coordinates Coordinates (Class from vividsolutions)
     * @return LatLng[]
     */
    @NonNull
    private LatLng[] getPoints(Coordinate[] coordinates) {
        List<LatLng> listPoints = new ArrayList<>();
        for (Coordinate coordinate : coordinates) {
            listPoints.add(new LatLng(coordinate.x, coordinate.y));
        }
        return listPoints.toArray(new LatLng[listPoints.size()]);
    }

    /**
     * Function to create LinearRing (Class from vividsolutions) from a list of
     * Coordinate (Class from vividsolutions)
     *
     * @param coordinates List
     * @return LinearRing
     */
    @NonNull
    private LinearRing getLinearRing(List<Coordinate> coordinates) {
        return new LinearRing(getPoints(coordinates), geometryFactory);
    }

    /**
     * Function to get points of CoordinateArraySequence (Class from vividsolutions)
     *
     * @param coordinates List (Class from vividsolutions)
     * @return CoordinateArraySequence (Class from vividsolutions)
     */
    @NonNull
    private CoordinateArraySequence getPoints(List<Coordinate> coordinates) {
        return new CoordinateArraySequence(getCoordinates(coordinates));
    }

    /**
     * Function to get coordinates array from a list of coordinates
     *
     * @param coordinates List<Coordinate> (Class from vividsolutions)
     * @return Coordinate [] (Class from vividsolutions)
     */
    @NonNull
    private Coordinate[] getCoordinates(List<Coordinate> coordinates) {
        return coordinates.toArray(new Coordinate[coordinates.size()]);
    }
}
