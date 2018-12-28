package com.siboren.android.foodofchina;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiAddrInfo;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements MyOrientationListener.OnOrientationListener{

    private static final String EXTRA_USER_ID=
            "com.siboren.android.foodname.user_id";

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private List<String> permissionList;
    private PoiSearch mPoiSearch;
    private OnGetPoiSearchResultListener poiListener;
    private Button mRecipeButton,mMissionButton,mSearchButton;
    private MissionLab mMissionLab;

    private Boolean isFirstLocate=true;
    private float mCurrentAccracy;
    private double mCurrentLantitude;
    private double mCurrentLongitude;
    private int mXDirection;
    public LocationClient mlocation;
    private MyLocationData.Builder builder;
    private MyOrientationListener myOrientationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mlocation = new LocationClient(getApplicationContext());
        mlocation.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.setCoordType(CoordType.BD09LL);
        setContentView(R.layout.activity_map);
        //初始化方向传感器
        myOrientationListener = new MyOrientationListener(this);
        myOrientationListener.setOnOrientationListener(this);
        //获取地图控件
        mMapView = findViewById(R.id.mapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        mRecipeButton=findViewById(R.id.recipe_button);
        mRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MapActivity.this,RecipeListActivity.class);
                intent.putExtra(EXTRA_USER_ID,getIntent().getStringExtra(EXTRA_USER_ID));
                startActivity(intent);
            }
        });
        mMissionButton=findViewById(R.id.mission_button);
        mMissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MapActivity.this,MissionListActivity.class);
                intent.putExtra(EXTRA_USER_ID,getIntent().getStringExtra(EXTRA_USER_ID));
                startActivity(intent);
            }
        });
        mSearchButton=findViewById(R.id.search_button);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng point = new LatLng(mCurrentLantitude,mCurrentLongitude);
                String key = "银行";
                PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption();
                nearbySearchOption.location(point);
                nearbySearchOption.keyword(key);
                nearbySearchOption.radius(50000);
                nearbySearchOption.pageNum(1);
                mPoiSearch.searchNearby(nearbySearchOption);
            }
        });
        mMissionLab= MissionLab.get(this);
        //Poi检索
        mPoiSearch = PoiSearch.newInstance();
        poiListener = new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                if ((poiResult!=null) && poiResult.error == PoiResult.ERRORNO.NO_ERROR)
                {
                    List<PoiAddrInfo> PoiAddrInfos = poiResult.getAllAddr();
                    for (PoiAddrInfo mInfo:PoiAddrInfos) {
                        Mission mission = new Mission();
                        mission.setTitle(mInfo.name);
                        mission.setSolved(false);
                        mission.setNeedFood("Fish");
                        mission.setDistance(1.0);
                        mission.setAward("money * 5000 元");
                        mMissionLab.addMission(mission);
                      }
                }else{
                    Toast.makeText(getApplication(), "搜索不到你需要的信息！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }

            @Override
            public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        };
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);

        //申请权限
        initPermission();
    }

    private void initPermission() {

        permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MapActivity.this, permissions, 1);
        } else {
            //请求定位
            requestLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(MapActivity.this, "必须同意所有权限", Toast.LENGTH_LONG).show();
                            finish();
                            return;
                        }
                    }
                    //请求定位
                    requestLocation();
                } else {
                    finish();
                }
                break;
        }
    }

    private void requestLocation() {
        //设置定位属性
        initLocation();
        //开始定位
        mlocation.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //设置百度坐标
        option.setCoorType("bd09ll");
        option.setOpenGps(true); // 打开gps
        //设置定位延迟
        option.setScanSpan(3000);
        mlocation.setLocOption(option);
    }

    @Override
    public void onOrientationChanged(float x) {
        mXDirection = (int) x;
        builder = new MyLocationData.Builder();
        builder.accuracy(mCurrentAccracy);
        builder.direction(mXDirection);
        builder.latitude(mCurrentLantitude);
        builder.longitude(mCurrentLongitude);
        MyLocationData locationData = builder.build();
        mBaiduMap.setMyLocationData(locationData);
        // 设置自定义图标
        //BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.arrow);
        MyLocationConfiguration configuration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null);
        mBaiduMap.setMyLocationConfiguration(configuration);
    }

    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            mCurrentAccracy=bdLocation.getRadius();
            mCurrentLantitude=bdLocation.getLatitude();
            mCurrentLongitude=bdLocation.getLongitude();
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                //设置地图显示
                navigateTo(bdLocation);
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }

        private void navigateTo(BDLocation location) {
            if (isFirstLocate) {
                //定位坐标
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(update);
                //设置缩放级别
                update = MapStatusUpdateFactory.zoomTo(21f);
                mBaiduMap.animateMapStatus(update);
                isFirstLocate = false;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mlocation.stop();
        mMapView.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        myOrientationListener.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myOrientationListener.stop();
    }

    public static Intent newIntent(Context packageContext, String user_id){
        Intent i=new Intent(packageContext,MapActivity.class);
        i.putExtra(EXTRA_USER_ID,user_id);
        return i;
    }

}