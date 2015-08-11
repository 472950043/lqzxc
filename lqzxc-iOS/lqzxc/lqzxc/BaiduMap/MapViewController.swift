//
//  MapViewController.swift
//  lqzxc
//
//  Created by jyb on 15/8/1.
//  Copyright (c) 2015年 jyb. All rights reserved.
//

import UIKit

class MapViewController: UIViewController, BMKMapViewDelegate, BMKLocationServiceDelegate {

    @IBOutlet var mapView: BMKMapView!
    var locService: BMKLocationService!
    var mapLayer: MapLayer!

    @IBOutlet var trafficBtn: UIButton!
    @IBOutlet var mapLayerBtn: UIButton!
    @IBOutlet var locationBtn: UIButton!
    @IBOutlet var zoominBtn: UIButton!
    @IBOutlet var zoomoutBtn: UIButton!
    //2.继承并重写用nibName初始化的init方法
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: NSBundle?){
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
    }
    
    //xib初始化时调用
    required init(coder decoder: NSCoder){
        super.init(coder: decoder)
        //fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        MapDefaults.sharedInstance.get(mapView)
        locService = BMKLocationService()
        mapLayer = MapLayer.instanceMapLayer()
        self.view.addSubview(mapLayer.initView())
    }
    
    @IBAction func trafficBtnClick(sender: AnyObject) {
        if(mapView.trafficEnabled == true){
            mapView.trafficEnabled = false
            trafficBtn.setImage(UIImage(named: "button_main_traffic_off"), forState: UIControlState.Normal)
        } else {
            mapView.trafficEnabled = true
            trafficBtn.setImage(UIImage(named: "button_main_traffic_on"), forState: UIControlState.Normal)
        }
    }
    
    @IBAction func mapLayerBtnClick(sender: AnyObject) {
        mapLayer.bounceAnimation()
    }
    
    @IBAction func locationBtnClick(sender: AnyObject) {
        locService.startUserLocationService()
    }
    
    @IBAction func zoomBtnClick(sender: AnyObject) {
        if(sender.tag == 21){
            mapView.zoomIn()
        } else if(sender.tag == 22){
            mapView.zoomOut()
        }
    }
    
    //pragma mark - BMKMapViewDelegate
    func mapStatusDidChanged(mapView: BMKMapView!) {
        //NSLog("地图状态改变完成后会调用此接口")
        MapDefaults.sharedInstance.saveMapStatus(mapView)
        zoomBtnEnabled()
    }
    
    func zoomBtnEnabled(){
        if(mapView.zoomLevel >= 19.5){
            zoominBtn.enabled = false
        } else {
            zoominBtn.enabled = true
        }
        if(mapView.zoomLevel <= 3.5){
            zoomoutBtn.enabled = false
        } else {
            zoomoutBtn.enabled = true
        }
//        println("mapView.zoomLevel:\(mapView.zoomLevel)")
    }
    
    func mapView(mapView: BMKMapView!, annotationView view: BMKAnnotationView!, didChangeDragState newState: UInt, fromOldState oldState: UInt) {
        //NSLog("拖动annotation view时，若view的状态发生变化，会调用此函数。ios3.2以后支持")
    }
    
    func mapView(mapView: BMKMapView!, annotationViewForBubble view: BMKAnnotationView!) {
        NSLog("当点击annotation view弹出的泡泡时，调用此接口")
    }

    func mapView(mapView: BMKMapView!, didAddAnnotationViews views: [AnyObject]!) {
        NSLog("当mapView新添加annotation views时，调用此接口")
    }
    
    func mapView(mapView: BMKMapView!, didAddOverlayViews overlayViews: [AnyObject]!) {
        NSLog("当mapView新添加overlay views时，调用此接口")
    }
    
    func mapView(mapView: BMKMapView!, didDeselectAnnotationView view: BMKAnnotationView!) {
        NSLog("当取消选中一个annotation views时，调用此接口")
    }
    
    func mapView(mapView: BMKMapView!, didSelectAnnotationView view: BMKAnnotationView!) {
        NSLog("当选中一个annotation views时，调用此接口")
    }
    
    func mapView(mapView: BMKMapView!, onClickedBMKOverlayView overlayView: BMKOverlayView!) {
        NSLog("点中覆盖物后会回调此接口，目前只支持点中BMKPolylineView时回调")
    }
    
    func mapView(mapView: BMKMapView!, onClickedMapBlank coordinate: CLLocationCoordinate2D) {
        NSLog("点中底图空白处会回调此接口")
    }
    
    func mapView(mapView: BMKMapView!, onClickedMapPoi mapPoi: BMKMapPoi!) {
        NSLog("点中底图标注后会回调此接口")
    }
    
    func mapview(mapView: BMKMapView!, onDoubleClick coordinate: CLLocationCoordinate2D) {
        NSLog("双击地图时会回调此接口")
    }
    
//    func mapView(mapView: BMKMapView!, onDrawMapFrame status: BMKMapStatus!) {
//        NSLog("地图渲染每一帧画面过程中，以及每次需要重绘地图时（例如添加覆盖物）都会调用此接口")
//    }
    
    func mapview(mapView: BMKMapView!, onLongClick coordinate: CLLocationCoordinate2D) {
        NSLog("长按地图时会回调此接口")
    }
    
    func mapView(mapView: BMKMapView!, regionDidChangeAnimated animated: Bool) {
        //NSLog("地图区域改变完成后会调用此接口")
    }
    
    func mapView(mapView: BMKMapView!, regionWillChangeAnimated animated: Bool) {
        //NSLog("地图区域即将改变时会调用此接口")
    }
    
//    func mapView(mapView: BMKMapView!, viewForAnnotation annotation: BMKAnnotation!) -> BMKAnnotationView! {
//        NSLog("根据anntation生成对应的View")
//    }
    
//    func mapView(mapView: BMKMapView!, viewForOverlay overlay: BMKOverlay!) -> BMKOverlayView! {
//        NSLog("根据overlay生成对应的View ")
//    }
    
    func mapViewDidFinishLoading(mapView: BMKMapView!) {
        NSLog("BMKMapView控件初始化完成")
    }
    
    //实现相关delegate 处理位置信息更新
    func didUpdateUserHeading(userLocation: BMKUserLocation!) {
        mapView.updateLocationData(userLocation)
        NSLog("用户方向更新后，会调用此函数 %@",userLocation.heading)
    }
    
    func didUpdateBMKUserLocation(userLocation: BMKUserLocation!) {
        mapView.updateLocationData(userLocation)
        mapView.setCenterCoordinate(userLocation.location.coordinate, animated: true)
        locService.stopUserLocationService()//lat 28.655701,long 121.457290
        NSLog("用户位置更新后，会调用此函数 lat %f,long %f",userLocation.location.coordinate.latitude,userLocation.location.coordinate.longitude)
    }
    
    func didFailToLocateUserWithError(error: NSError!) {
        NSLog("定位失败后，会调用此函数%@", error)
    }

    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
        mapView.delegate = self // 此处记得不用的时候需要置nil，否则影响内存的释放
        locService.delegate = self
        locService.startUserLocationService()
        addObserver()
    }
    
    override func viewWillDisappear(animated: Bool) {
        super.viewWillDisappear(animated)
        mapView.delegate = nil // 不用时，置nil
        locService.delegate = nil
        locService.stopUserLocationService()
        removeObserver()
        MapDefaults.sharedInstance.save()
    }

    func addObserver(){
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "setMapType", name: "setMapType", object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "setShowPoint", name: "setShowPoint", object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "setHeatMap", name: "setHeatMap", object: nil)
    }
    
    func removeObserver(){
        NSNotificationCenter.defaultCenter().removeObserver(self)
    }
    
    func setMapType(){
        NSLog("执行setMapType")
        mapView.mapType = UInt(MapDefaults.sharedInstance.mapLayerType)
        mapView.buildingsEnabled = MapDefaults.sharedInstance.mapBuildingsEnabled//设定地图是否现实3D楼块效果
        MapDefaults.sharedInstance.getMapStatus(mapView, flag: true)
    }
    
    func setShowPoint(){
        NSLog("执行setShowPoint")
        if(MapDefaults.sharedInstance.mapPoint){
            
        }
    }
    
    func setHeatMap(){
        NSLog("执行setHeatMapEnabled")
        mapView.baiduHeatMapEnabled = MapDefaults.sharedInstance.mapBaiduHeatMapEnabled
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
