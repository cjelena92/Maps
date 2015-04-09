package com.mycompany.maps;


import android.media.session.PlaybackState;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.google.android.gms.maps.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    GoogleMap googleMap;
    SharedPreferences sharedPreferences;
    int locationCount = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        createMapView();
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);

        //addMarker();
        // addMarkerTouch();

        removeMarkerInfoWindow();
        sharedPreferences = getSharedPreferences("location", 0);
        locationCount = sharedPreferences.getInt("locationCount", 0);
        String zoom = sharedPreferences.getString("zoom", "0");

        if (locationCount != 0) {
            String lat = "";
            String lng = "";

            for (int i = 0; i < locationCount; i++) {
                lat = sharedPreferences.getString("lat" + i, "0");
                lng = sharedPreferences.getString("lng" + i, "0");
                drawMarker(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(lat),
                    Double.parseDouble(lng))));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(Float.parseFloat(zoom)));
        }

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override

            public void onMapClick(LatLng point) {
                locationCount++;
                drawMarker(point);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("lat" + Integer.toString((locationCount - 1)),
                        Double.toString(point.latitude));
                editor.putString("lng" + Integer.toString((locationCount - 1)),
                        Double.toString(point.longitude));
                editor.putString("zoom", Float.toString(googleMap.getCameraPosition().zoom));
                Toast.makeText(getBaseContext(), "Marker is added to the Map", Toast.LENGTH_SHORT)
                        .show();
             }
        });

        //brisanje svih markera
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                googleMap.clear();
                Toast.makeText(getBaseContext(), "Markers is deleted", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                locationCount = 0;
             }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.map_types) {

            return true;
        } else if (id == R.id.action_hibrid) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        } else if (id == R.id.action_normal) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        } else if (id == R.id.action_satellite) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        } else if (id == R.id.action_terrain) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        }else if(id==R.id.action_settings){
            Intent intent=new Intent(this,Search.class);
            startActivity(intent);
            return true;


        }else if(id==R.id.directions){
            Intent intent=new Intent(this,Directions.class);
            startActivity(intent);
            return true;

        }else if(id==R.id.btn_next){
            Intent intent=new Intent(this,SearchPlaces.class);
            startActivity(intent);
            return true;


        }

        return super.onOptionsItemSelected(item);
 }

    private void createMapView() {

        try {
            if (null == googleMap) {
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapView))
                        .getMap();

                if (null == googleMap) {
                    Toast.makeText(getApplicationContext(),
                            "Error creating map", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException exception) {
            Log.e("maps", exception.toString());
        }
    }

    private void drawMarker(LatLng point) {
        ArrayList<Marker> markersList= new ArrayList<Marker>();
              for(int i=1;i<100;i++) {
                  String name="Marker";
                  Marker marker = googleMap.addMarker(new MarkerOptions().position(point)
                          .title(name + i).snippet("" + new LatLng(point.latitude,
                                  point.longitude)));

                  markersList.add(marker);
              }



        }


    //uklananje markera kad kliknem na info window
    public void removeMarkerInfoWindow() {

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                marker.remove();
                Toast.makeText(getBaseContext(), "Marker is deleted", Toast.LENGTH_SHORT).show();
                locationCount--;
            }
        });
    }

    //automatsko dodavanje markera
 /*  private void addMarker() {
        if (null != googleMap) {
            googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker")
            .snippet("" + new LatLng(0,0)).draggable(true)
            );
        }
     }
prikaz lokacije trenutne
    private void setUpMap() {

    LocationManager locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);

    Criteria criteria=new Criteria();
    criteria.setAccuracy(Criteria.ACCURACY_FINE);

    String provider=locationManager.getBestProvider(criteria,true);

    LocationListener locationListener=new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            showCurrentLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
    locationManager.requestLocationUpdates(provider,2000,0,locationListener);

    Location location=locationManager.getLastKnownLocation(provider);
    if(location!=null)
    {
        showCurrentLocation(location);
    }
}

 prikaz trenutne lokacije
    private void showCurrentLocation(Location location){
        googleMap.clear();
        LatLng currentPosition=new LatLng(location.getLatitude(),location.getLongitude());

        googleMap.addMarker(new MarkerOptions().position(currentPosition).snippet("Lat:"+
                location.getLatitude()+" ,Lng: "+location.getLongitude())
                .icon(BitmapDescriptorFactory
                .fromResource(R.drawable.ic_neko)).flat(true).title("I'am here!!"));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition,18));

    }

    //dodavanje markera dodirom
    private void addMarkerTouch() {
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override

            public void onMapClick(LatLng point) {
                //dodavanje markera za grad Melburne
              /*  final LatLng MELBOURNE = new LatLng(-37.81319, 144.96298);
                Marker melbourne = googleMap.addMarker(new MarkerOptions().position(MELBOURNE)
                .title("Melbourne").
                        snippet("Population:4,137,400"));

                MarkerOptions marker = new MarkerOptions().position(
                        new LatLng(point.latitude, point.longitude)).title("New Marker").snippet("" + new LatLng(point.latitude, point.longitude));
                googleMap.addMarker(marker);
            }
        });
    }
    //uklananje markera kad kliknem na marker
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){

             @Override
             public boolean onMarkerClick(Marker marker) {
                 marker.remove();
                 return false;
             }
         });
        uklananje markera kad kliknem na info window
         public void removeMarkerInfoWindow(){

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                marker.remove();
            }
        });
    }
*/

    }



























