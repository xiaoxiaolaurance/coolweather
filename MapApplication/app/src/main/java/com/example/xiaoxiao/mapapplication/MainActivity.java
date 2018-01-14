package com.example.xiaoxiao.mapapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

public class MainActivity extends AppCompatActivity {
    MapView mapView;
    BaiduMap baiduMap;
    float x;
    float y;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.map_layout);
        mapView =(MapView)findViewById(R.id.map_view);
        baiduMap =mapView.getMap();
        baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        baiduMap.setTrafficEnabled(true);
        LatLng latLng = new LatLng(39.963175, 116.400244);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);
        OverlayOptions options =new MarkerOptions().position(latLng).icon(bitmapDescriptor).zIndex(9).draggable(true);
        baiduMap.addOverlay(options);
        baiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                x=marker.getAnchorX();
                y=marker.getAnchorY();
            }

            @Override
            public void onMarkerDragStart(Marker marker) {

            }
        });
//        baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
    }
    public void onPause(){
        super.onPause();
        mapView.onPause();
    }
    public void onDestroy(){
        super.onDestroy();
        mapView.onDestroy();
    }
    public void onResume(){
        super.onResume();
        mapView.onResume();
    }
}
