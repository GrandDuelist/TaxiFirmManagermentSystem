package com.example.tracingtaxi;


import com.example.handle.ListenToMyLocation;
import com.example.handle.MathHandle;
import com.example.tracingtaxi.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CameraPositionCreator;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomButtonsController.OnZoomListener;

public class MainActivity extends FragmentActivity
implements
ConnectionCallbacks,
OnConnectionFailedListener,
LocationListener,
OnMyLocationButtonClickListener, OnMapClickListener, OnMapLongClickListener, OnCameraChangeListener{

public GoogleMap mMap;

public LocationClient mLocationClient;
private TextView mMessageView;
private ListenToMyLocation l;
private Button cameraLock;
private LinearLayout LeftButtons ;
private Button MapTypeButton;   //更改地图类型
public boolean cameraChange=false;
Handler handler = new Handler();
public float RecordCamera;
// These settings are the same as the settings for the map. They will in fact give you updates
// at the maximal rates currently possible.
private static final LocationRequest REQUEST = LocationRequest.create()
    .setInterval(5000)         // 5 seconds
    .setFastestInterval(16)    // 16ms = 60fps
    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main);



this.getViews();
this.listenToGetMyPosition();


}





public void getViews()
{
	mMessageView = (TextView) findViewById(R.id.message_text);

	mMessageView.setVisibility(TRIM_MEMORY_BACKGROUND);
	
	cameraLock=(Button)findViewById(R.id.lock_camera_button);

	LeftButtons= (LinearLayout) findViewById(R.id.left_buttons);
	
	MapTypeButton = (Button)findViewById(R.id.change_style_button);
}

@Override
protected void onResume() {
super.onResume();
setUpMapIfNeeded();
setUpLocationClientIfNeeded();
mLocationClient.connect();

this.setListener();
}

@Override
public void onPause() {
super.onPause();
if (mLocationClient != null) {
    mLocationClient.disconnect();
}
}

private void setUpMapIfNeeded() {
// Do a null check to confirm that we have not already instantiated the map.
if (mMap == null) {
    // Try to obtain the map from the SupportMapFragment.
    mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
            .getMap();
    
    
    // Check if we were successful in obtaining the map.
    if (mMap != null) {
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
       
    }
}
}

//坚挺视角的按钮
public void lockCameraClick( View view)
{

	
	//如果视角锁定，则释放
	if(l.getLock())
	{
		
		l.unlockCamera();
		cameraLock.setText(R.string.CameraLockText);
		
	}else
	{
		
		l.lockCamera();
		cameraLock.setText(R.string.CameraUnlockText);
		
	}
	}


//监听好友位置
public void listenToGetMyPosition()
{
	
	
	
	 l = new ListenToMyLocation(this,handler);
	 handler.post(l);
	
	
	}


private void setUpLocationClientIfNeeded() {
if (mLocationClient == null) {
    mLocationClient = new LocationClient(
            getApplicationContext(),
            this,  // ConnectionCallbacks
            this); // OnConnectionFailedListener
}
}


/**
* 复写选项菜单
*/
@Override
public boolean onCreateOptionsMenu(Menu menu) {
	// TODO Auto-generated method stub
	menu.add(0, 1, 1, R.string.exit_out);

	return super.onCreateOptionsMenu(menu);
}




//清除好友标记
public void clearMark(View view)
{
	this.l.clearMark();
	}
@Override
public boolean onOptionsItemSelected(MenuItem item) {
	// TODO Auto-generated method stub
	if(item.getItemId()==1)
	{
		
		handler.removeCallbacks(l);
		
        
        this.finish();
        System.exit(0);//退出程序
		
	}
	
	return super.onOptionsItemSelected(item);
}

//清屏
public void clearMap(View view)
{
	if(mMap!=null)
	{
		mMap.clear();
		l.FriendLocationOrder=0;
	}
	}
//切换到好友位置
public void goToFriendPostion(View view)
{
	
	l.goToFriendLocation();
	
	}
//标记好友位置
public void markFriendLocation(View view)
{
	this.l.markFriendLocation();
	}

//计算与好友绝提
public float getDistanceWithFriend(View view)
{
	return 1.1f;
	
}

/**
* Button to get current Location. This demonstrates how to get the current Location as required
* without needing to register a LocationListener.
*/
public void showMyLocation(View view) {
if (mLocationClient != null && mLocationClient.isConnected()) {
    String msg = "Location = " + mLocationClient.getLastLocation();
    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
}
}

/**
* Implementation of {@link LocationListener}.
*/
@Override
public void onLocationChanged(Location location) {
mMessageView.setText("Location = " + location);
}

/**
* Callback called when connected to GCore. Implementation of {@link ConnectionCallbacks}.
*/
@Override
public void onConnected(Bundle connectionHint) {
mLocationClient.requestLocationUpdates(
        REQUEST,
        this);  // LocationListener
}

/**
* Callback called when disconnected from GCore. Implementation of {@link ConnectionCallbacks}.
*/
@Override
public void onDisconnected() {
// Do nothing
}

/**
* Implementation of {@link OnConnectionFailedListener}.
*/
@Override
public void onConnectionFailed(ConnectionResult result) {
// Do nothing
}

@Override
public boolean onMyLocationButtonClick() {
Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
// Return false so that we don't consume the event and the default behavior still occurs
// (the camera animates to the user's current position).
return false;
}

@Override
public void onMapLongClick(LatLng arg0) {
	// TODO Auto-generated method stub
	
}

public void setListener()
{
	this.mMap.setOnMapClickListener(this);
	this.mMap.setOnCameraChangeListener(this);
	}
@Override
public void onMapClick(LatLng arg0) {
	// TODO Auto-generated method stub
	System.out.println("visible");
   if(LeftButtons.getVisibility()==LinearLayout.INVISIBLE)
   {
	   LeftButtons.setVisibility(LinearLayout.VISIBLE);
   }else
   {
	   LeftButtons.setVisibility(LinearLayout.INVISIBLE);
   }
	
}

//切换地图格式
public void changeToSiteMap(View view)
{
	if(this.mMap.getMapType()!=GoogleMap.MAP_TYPE_HYBRID)
	{
	    this.mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
	    this.MapTypeButton.setText(R.string.change_to_common);
	    
	}
	else
	{
		this.mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		this.MapTypeButton.setText(R.string.change_to_site);
	}
	
	}





	

public void distanceFromFriend(View view)
{
	try{
	double d=MathHandle.calculateTheDistance((new LatLng(mLocationClient.getLastLocation().getLatitude(),mLocationClient.getLastLocation().getLongitude())),l.getLastPosition());
	new AlertDialog.Builder(this).setTitle("好友距离").setMessage("您与好友的直线距离为:"+d).setPositiveButton("确定",null).show();
	}
	catch(Exception e)
	{
		
		new AlertDialog.Builder(this).setTitle("连接失败").setMessage("未与好友连接或本机定位失败").setPositiveButton("确定",null).show();
	}
	}
@Override
public void onCameraChange(final CameraPosition arg0) {
	// TODO Auto-generated method stub
	if(!this.l.getLock()&&arg0.zoom>this.l.MinZoom){
	this.cameraChange=true;
	this.RecordCamera=arg0.zoom;
	}
	
	
}



}
