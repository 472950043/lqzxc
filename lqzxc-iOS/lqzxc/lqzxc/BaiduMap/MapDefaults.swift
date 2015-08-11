//
//  MapDefaults.swift
//  lqzxc
//
//  Created by jyb on 15/8/1.
//  Copyright (c) 2015年 jyb. All rights reserved.
//

import UIKit

class MapDefaults: NSObject {
    //1 单例模式
    class var sharedInstance: MapDefaults {
        //2
        struct Singleton {
            //3 创建初始化
            static let instance = MapDefaults()
        }
        //4
        return Singleton.instance
    }
    
    var defaults: NSUserDefaults
    var mapLayerType: Int
    var mapLat: Double
    var mapLng: Double
    var mapZoomLevel: Float
    var mapScroll: Int
    var mapRotate: Int
    var mapBuildingsEnabled: Bool
    var mapTrafficEnabled: Bool
    var mapBaiduHeatMapEnabled: Bool
    var mapZoomEnabled: Bool
    var mapZoomEnabledWithTap: Bool
    var mapScrollEnabled: Bool
    var mapOverlookEnabled: Bool
    var mapRotateEnabled: Bool
    var mapShowMapScaleBar: Bool
    var mapChangeWithTouchPointCenterEnabled: Bool
    var mapIsSelectedAnnotationViewFront: Bool
    var mapShowsUserLocation: Bool
    
    var mapPoint: Bool
    var mapTakeEnabled: Bool
    var mapMesureEnabled: Bool
    var mapCity: String
    var mapCityid: Int
    
    override init() {
        defaults = NSUserDefaults.standardUserDefaults()
        
        mapLayerType = 1 //当前地图类型，可设定为标准地图BMKMapTypeStandard = 1、卫星地图BMKMapTypeSatellite = 2
        mapLat = 28.655701 //当前地图的中心点，改变该值时，地图的比例尺级别不会发生变化
        mapLng = 121.457290 //当前地图的中心点，改变该值时，地图的比例尺级别不会发生变化
        mapZoomLevel = 12.0 //地图比例尺级别，在手机上当前可使用的级别为3-19级，类型float
        mapScroll = 0 //地图旋转角度，在手机上当前可使用的范围为－180～180度，类型int，显示时转化为0,360
        
        mapRotate = 0 //地图俯视角度，在手机上当前可使用的范围为－45～0度，类型int，显示时转化为0,45
        mapBuildingsEnabled = false //设定地图是否现实3D楼块效果
        mapTrafficEnabled = false //设定地图是否打开路况图层
        mapBaiduHeatMapEnabled = false //设定地图是否打开百度城市热力图图层（百度自有数据）,注：地图层级大于11时，可显示热力图
        mapZoomEnabled = true //设定地图View能否支持用户多点缩放(双指)
        
        mapZoomEnabledWithTap = true //设定地图View能否支持用户缩放(双击或双指单击) mapTapEnabled
        mapScrollEnabled = true //设定地图View能否支持用户移动地图
        mapOverlookEnabled = true //设定地图View能否支持俯仰角
        mapRotateEnabled = true //设定地图View能否支持旋转
        mapShowMapScaleBar = true //设定是否显式比例尺 mapScaleEnabled
        
        mapChangeWithTouchPointCenterEnabled = true //设定地图View能否支持以手势中心点为轴进行旋转和缩放 mapTouchEnabled
        mapIsSelectedAnnotationViewFront = true //设定是否总让选中的annotaion置于最前面
        mapShowsUserLocation = true // 设定是否显示定位图层
        // 设定定位模式，取值为：BMKUserTrackingMode 普通定位模式BMKUserTrackingModeNone = 0,定位跟随模式BMKUserTrackingModeFollow,定位罗盘模式BMKUserTrackingModeFollowWithHeading
        
        mapPoint = true //收藏的点
        mapTakeEnabled = false //截图
        mapMesureEnabled = false //测距
        mapCity = "台州市" //默认离线下载
        mapCityid = 244 //默认离线下载
    }
    
    /**
    *读取默认地图配置
    */
    func get(mapView: BMKMapView!){
        if(defaults.valueForKey("mapLayerType") == nil ||
            defaults.valueForKey("mapLat") == nil ||
            defaults.valueForKey("mapLng") == nil ||
            defaults.valueForKey("mapZoomLevel") == nil ||
            defaults.valueForKey("mapScroll") == nil ||
            
            defaults.valueForKey("mapRotate") == nil ||
            defaults.valueForKey("mapBuildingsEnabled") == nil ||
            defaults.valueForKey("mapTrafficEnabled") == nil ||
            defaults.valueForKey("mapBaiduHeatMapEnabled") == nil ||
            defaults.valueForKey("mapZoomEnabled") == nil ||
            
            defaults.valueForKey("mapZoomEnabledWithTap") == nil ||
            defaults.valueForKey("mapScrollEnabled") == nil ||
            defaults.valueForKey("mapOverlookEnabled") == nil ||
            defaults.valueForKey("mapRotateEnabled") == nil ||
            defaults.valueForKey("mapShowMapScaleBar") == nil ||
            
            defaults.valueForKey("mapChangeWithTouchPointCenterEnabled") == nil ||
            defaults.valueForKey("mapIsSelectedAnnotationViewFront") == nil ||
            defaults.valueForKey("mapShowsUserLocation") == nil){// ||
            
//            defaults.valueForKey("mapTakeEnabled") == nil ||
//            defaults.valueForKey("mapMesureEnabled") == nil ||
//            defaults.valueForKey("mapCity") == nil ||
//            defaults.valueForKey("mapCityid") == nil){
            save()
        } else {
            mapLayerType = defaults.integerForKey("mapLayerType")
            mapLat = defaults.doubleForKey("mapLat")
            mapLng = defaults.doubleForKey("mapLng")
            mapZoomLevel = defaults.floatForKey("mapZoomLevel")
            mapScroll = defaults.integerForKey("mapScroll")
            
            mapRotate = defaults.integerForKey("mapRotate")
            mapBuildingsEnabled = defaults.boolForKey("mapBuildingsEnabled")
            mapTrafficEnabled = defaults.boolForKey("mapTrafficEnabled")
            mapBaiduHeatMapEnabled = defaults.boolForKey("mapBaiduHeatMapEnabled")
            mapZoomEnabled = defaults.boolForKey("mapZoomEnabled")
            
            mapZoomEnabledWithTap = defaults.boolForKey("mapZoomEnabledWithTap")
            mapScrollEnabled = defaults.boolForKey("mapScrollEnabled")
            mapOverlookEnabled = defaults.boolForKey("mapOverlookEnabled")
            mapRotateEnabled = defaults.boolForKey("mapRotateEnabled")
            mapShowMapScaleBar = defaults.boolForKey("mapShowMapScaleBar")
            
            mapChangeWithTouchPointCenterEnabled = defaults.boolForKey("mapChangeWithTouchPointCenterEnabled")
            mapIsSelectedAnnotationViewFront = defaults.boolForKey("mapIsSelectedAnnotationViewFront")
            mapShowsUserLocation = defaults.boolForKey("mapShowsUserLocation")
            // 设定定位模式，取值为：BMKUserTrackingMode 普通定位模式BMKUserTrackingModeNone = 0,定位跟随模式BMKUserTrackingModeFollow,定位罗盘模式BMKUserTrackingModeFollowWithHeading
            
//            mapTakeEnabled = defaults.boolForKey("mapTakeEnabled")
//            mapMesureEnabled = defaults.boolForKey("mapMesureEnabled")
//            mapCity = defaults.valueForKey("mapCity") as! String
//            mapCityid = defaults.integerForKey("mapCityid")
        }
        mapView.mapType = UInt(mapLayerType)//当前地图类型，可设定为标准地图、卫星地图
//        mapView.region// BMKCoordinateRegion 当前地图的经纬度范围，设定的该范围可能会被调整为适合地图窗口显示的范围
//        mapView.compassPosition//指南针的位置，设定坐标以BMKMapView左上角为原点，向右向下增长
        mapView.centerCoordinate = CLLocationCoordinate2D(latitude: mapLat, longitude: mapLng)//当前地图的中心点，改变该值时，地图的比例尺级别不会发生变化
        mapView.zoomLevel = mapZoomLevel//地图比例尺级别，在手机上当前可使用的级别为3-19级
//        mapView.minZoomLevel//地图的自定义最小比例尺级别
//        mapView.maxZoomLevel//地图的自定义最大比例尺级别
        mapView.rotation = Int32(mapScroll > 180 ? mapScroll - 360 : mapScroll)//地图旋转角度，在手机上当前可使用的范围为－180～180度
        mapView.overlooking = Int32(0 - mapRotate)//地图俯视角度，在手机上当前可使用的范围为－45～0度
        mapView.buildingsEnabled = mapBuildingsEnabled//设定地图是否现实3D楼块效果
        mapView.trafficEnabled = mapTrafficEnabled//设定地图是否打开路况图层
        mapView.baiduHeatMapEnabled = mapBaiduHeatMapEnabled//设定地图是否打开百度城市热力图图层（百度自有数据）,注：地图层级大于11时，可显示热力图
        mapView.zoomEnabled = mapZoomEnabled//设定地图View能否支持用户多点缩放(双指)
        mapView.zoomEnabledWithTap = mapZoomEnabled//设定地图View能否支持用户缩放(双击或双指单击)
        mapView.scrollEnabled = mapScrollEnabled//设定地图View能否支持用户移动地图
        mapView.overlookEnabled = mapOverlookEnabled//设定地图View能否支持俯仰角
        mapView.rotateEnabled = mapRotateEnabled//设定地图View能否支持旋转
        mapView.showMapScaleBar = mapShowMapScaleBar//设定是否显式比例尺
        mapView.mapScaleBarPosition = CGPoint(x: 55, y: mapView.frame.size.height - 80)//比例尺的位置，设定坐标以BMKMapView左上角为原点，向右向下增长
//        mapView.visibleMapRect = BMKMapRect//当前地图范围，采用直角坐标系表示，向右向下增长
        mapView.ChangeWithTouchPointCenterEnabled = mapChangeWithTouchPointCenterEnabled//设定地图View能否支持以手势中心点为轴进行旋转和缩放
        mapView.isSelectedAnnotationViewFront = mapIsSelectedAnnotationViewFront//设定是否总让选中的annotaion置于最前面
        mapView.showsUserLocation = mapShowsUserLocation//设定是否显示定位图层
//        mapView.userTrackingMode = BMKUserTrackingMode// 设定定位模式，取值为：BMKUserTrackingMode 普通定位模式BMKUserTrackingModeNone = 0,定位跟随模式BMKUserTrackingModeFollow,定位罗盘模式BMKUserTrackingModeFollowWithHeading
//        mapView.userLocationVisible// 返回定位坐标点是否在当前地图可视区域内
        getMapStatus(mapView, flag: false)
    }
    
    /**
    *读取地图状态
    */
    func getMapStatus(mapView: BMKMapView!, flag: Bool){
        var mapStatus = BMKMapStatus()
        mapStatus.fLevel = mapZoomLevel
        mapStatus.fRotation = Float(mapScroll > 180 ? mapScroll - 360 : mapScroll)
        mapStatus.fOverlooking = Float(0 - mapRotate)
        mapView.setMapStatus(mapStatus, withAnimation: flag)
    }
    
    /**
    *保存地图状态
    */
    func saveMapStatus(mapView: BMKMapView!){
        mapZoomLevel = mapView.zoomLevel
        mapScroll = Int(mapView.rotation >= 0 ? mapView.rotation : 360 + mapView.rotation)
        mapRotate = 0 - mapView.overlooking
        mapLat = mapView.centerCoordinate.latitude
        mapLng = mapView.centerCoordinate.longitude
    }
    
    func save(){
        defaults.setInteger(mapLayerType, forKey: "mapLayerType")
        defaults.setDouble(mapLat, forKey: "mapLat")
        defaults.setDouble(mapLng, forKey: "mapLng")
        defaults.setFloat(mapZoomLevel, forKey: "mapZoomLevel")
        defaults.setInteger(mapScroll, forKey: "mapScroll")
        
        defaults.setInteger(mapRotate, forKey: "mapRotate")
        defaults.setBool(mapBuildingsEnabled, forKey: "mapBuildingsEnabled")
        defaults.setBool(mapTrafficEnabled, forKey: "mapTrafficEnabled")
        defaults.setBool(mapBaiduHeatMapEnabled, forKey: "mapBaiduHeatMapEnabled")
        defaults.setBool(mapZoomEnabled, forKey: "mapZoomEnabled")
        
        defaults.setBool(mapZoomEnabledWithTap, forKey: "mapZoomEnabledWithTap")
        defaults.setBool(mapScrollEnabled, forKey: "mapScrollEnabled")
        defaults.setBool(mapOverlookEnabled, forKey: "mapOverlookEnabled")
        defaults.setBool(mapRotateEnabled, forKey: "mapRotateEnabled")
        defaults.setBool(mapShowMapScaleBar, forKey: "mapShowMapScaleBar")
        
        defaults.setBool(mapTrafficEnabled, forKey: "mapTrafficEnabled")
        defaults.setBool(mapZoomEnabled, forKey: "mapZoomEnabled")
        defaults.setBool(mapScrollEnabled, forKey: "mapScrollEnabled")
        
        defaults.setBool(mapChangeWithTouchPointCenterEnabled, forKey: "mapChangeWithTouchPointCenterEnabled")
        defaults.setBool(mapIsSelectedAnnotationViewFront, forKey: "mapIsSelectedAnnotationViewFront")
        defaults.setBool(mapShowsUserLocation, forKey: "mapShowsUserLocation")
        
//        defaults.setBool(mapTakeEnabled, forKey: "mapTakeEnabled")
//        defaults.setBool(mapMesureEnabled, forKey: "mapMesureEnabled")
//        defaults.setObject(mapCity, forKey: "mapCity")
//        defaults.setInteger(mapCityid, forKey: "mapCityid")
        defaults.synchronize()
    }
}
