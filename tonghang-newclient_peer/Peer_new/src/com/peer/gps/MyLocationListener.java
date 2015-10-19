package com.peer.gps;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

public class MyLocationListener implements BDLocationListener {
	
	public static Double w = 0.0;
	public static Double j = 0.0;
	
	@Override
	public void onReceiveLocation(BDLocation location) {
		// TODO Auto-generated method stub
		
		
		w = location.getLatitude();//纬度
		j = location.getLongitude();//经度
		
		
		
	}

}
