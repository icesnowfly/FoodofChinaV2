package com.siboren.android.foodofchina;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroupOverlay;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
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
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.mapapi.utils.DistanceUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
    private BitmapDescriptor bitmap;
    private MissionLab mMissionLab;
    private List<Mission> MissionCan = new ArrayList<>();
    private boolean isActive=true;

    private Boolean isFirstLocate=true;
    private float mCurrentAccracy;
    private double mCurrentLantitude;
    private double mCurrentLongitude;
    private int mXDirection;
    private int searchN=0;
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
        //初始化数据库
        mMissionLab=MissionLab.get(getApplicationContext());
        //初始化方向传感器
        myOrientationListener = new MyOrientationListener(this);
        myOrientationListener.setOnOrientationListener(this);
        //生成Marker图标
         bitmap = BitmapDescriptorFactory
                .fromBitmap(getMapMarker(BitmapFactory.decodeResource(getResources(),R.drawable.ic_redmarker)));
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
                isActive=false;
                startActivity(intent);
            }
        });
        mMissionButton=findViewById(R.id.mission_button);
        mMissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MapActivity.this,MissionListActivity.class);
                intent.putExtra(EXTRA_USER_ID,getIntent().getStringExtra(EXTRA_USER_ID));
                isActive=false;
                startActivity(intent);
            }
        });
        mSearchButton=findViewById(R.id.search_button);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMissionLab.getSize()>=5)
                {
                    Toast.makeText(getApplication(), "任务数量已达上限！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getApplication(), "请稍等...", Toast.LENGTH_LONG).show();
                LatLng point = new LatLng(mCurrentLantitude,mCurrentLongitude);
                String key = "美食";
                PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption();
                nearbySearchOption.location(point);
                nearbySearchOption.keyword(key);
                nearbySearchOption.radius(1000);
                nearbySearchOption.pageNum(1);
                nearbySearchOption.radiusLimit(true);
                mPoiSearch.searchNearby(nearbySearchOption);
                key = "娱乐";
                nearbySearchOption.keyword(key);
                mPoiSearch.searchNearby(nearbySearchOption);
                key = "超市";
                nearbySearchOption.keyword(key);
                mPoiSearch.searchNearby(nearbySearchOption);
                key = "便利店";
                nearbySearchOption.keyword(key);
                mPoiSearch.searchNearby(nearbySearchOption);
                key = "公交站";
                nearbySearchOption.keyword(key);
                mPoiSearch.searchNearby(nearbySearchOption);
                key = "公司";
                nearbySearchOption.keyword(key);
                mPoiSearch.searchNearby(nearbySearchOption);
                key = "住宅";
                nearbySearchOption.keyword(key);
                mPoiSearch.searchNearby(nearbySearchOption);
                key = "店铺";
                nearbySearchOption.keyword(key);
                mPoiSearch.searchNearby(nearbySearchOption);
            }
        });
        //Poi检索
        mPoiSearch = PoiSearch.newInstance();
        poiListener = new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                if ((poiResult!=null) && poiResult.error == PoiResult.ERRORNO.NO_ERROR) {
                    LatLng mPoint;
                    LatLng position = new LatLng(mCurrentLantitude, mCurrentLongitude);
                    List<PoiInfo> PoiInfos = poiResult.getAllPoi();
                    for (PoiInfo mInfo : PoiInfos) {
                        mPoint = mInfo.getLocation();
                        Mission mission = new Mission();
                        mission.setTitle(mInfo.name);
                        mission.setLocation(mPoint);
                        mission.setDistance(DistanceUtil.getDistance(mPoint, position));
                        MissionCan.add(mission);
                    }
                    searchN++;
                    if (searchN>=5) {
                        if (MissionCan.size() == 0) {
                            Toast.makeText(getApplication(), "附近没有可接受的任务！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Collections.sort(MissionCan);
                        int r;
                        int size = mMissionLab.getSize();
                        for (int i = 0; i < 5 - size; i++) {
                            r = (int) (Math.random() * MissionCan.size());
                            mMissionLab.addMission(MissionCan.get(r));
                            MissionCan.remove(r);
                            if (MissionCan.size() == 0) break;
                        }
                        Toast.makeText(getApplication(), "已添加任务", Toast.LENGTH_SHORT).show();
                        MissionCan.clear();
                        searchN=0;
                    }
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

    private void updateMarker(){
        mBaiduMap.clear();
        List<Mission> mMissions = mMissionLab.getMissions();
        for (Mission mMission : mMissions)
        {
            if (mMission.isAccepted()) {
                setMarker(mMission);
            }
        }
    }

    private void setMarker(Mission mMission){
       // BitmapDescriptor bitmap = BitmapDescriptorFactory
       //         .fromResource(R.drawable.ic_redmarker);
        OverlayOptions option = new MarkerOptions()
                .position(mMission.getLocation())
                .icon(bitmap)
                .perspective(true);
        mBaiduMap.addOverlay(option);
    }

    private Bitmap getMapMarker(Bitmap bm){
        int width = bm.getWidth();
        int height = bm.getHeight();
        int newWidth = 128;
        int newHeight = 128;
        float scaleWidth = (float) newWidth/width;
        float scaleHeight = (float) newHeight/height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);
        return Bitmap.createBitmap(bm,0,0,width,height,matrix,true);
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
        MyLocationConfiguration configuration =
                new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,
                        true, null);
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
            List<Mission> missions = mMissionLab.getMissions();
            if (missions.size()>0 && isActive) {
                LatLng mission_pos;
                boolean isChanged=false;
                LatLng mypos = new LatLng(mCurrentLantitude,mCurrentLongitude);
                double distance;
                for (int i=missions.size()-1;i>=0;i--) {
                    Mission mission=missions.get(i);
                    if (mission.isAccepted()) {
                        mission_pos = mission.getLocation();
                        distance = DistanceUtil.getDistance(mission_pos, mypos);
                        if (distance < 30) {
                            if (mMissionLab.checkMission(mission)) {
                                Toast.makeText(getApplication(), "任务已完成", Toast.LENGTH_SHORT).show();
                                mMissionLab.completeMission(mission);
                                mMissionLab.deleteMission(mission.getId());
                                updateMarker();
                                missions.remove(i);
                                isChanged = true;
                            }
                        }
                    }
                }
                if (isChanged) updateMarker();
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
                update = MapStatusUpdateFactory.zoomTo(19f);
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
        isActive=true;
        myOrientationListener.start();
        updateMarker();
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