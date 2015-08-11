//
//  MapLayer.swift
//  lqzxc
//
//  Created by jyb on 15/8/2.
//  Copyright (c) 2015年 jyb. All rights reserved.
//

import UIKit

class MapLayer: UIView {
    
    
    @IBOutlet var bg: UIButton!
    @IBOutlet var layerImage: UIImageView!
    @IBOutlet var layerView: UIView!

    @IBOutlet var selectImage1: UIImageView!
    @IBOutlet var satelliteButton: UIButton!
    @IBOutlet var selectImage2: UIImageView!
    @IBOutlet var normalButton: UIButton!
    @IBOutlet var selectImage3: UIImageView!
    @IBOutlet var DButton: UIButton!
    @IBOutlet var favoriteSwitc: UISwitch!
    @IBOutlet var hotSwitc: UISwitch!
    
    //类方法（静态方法）加载xib
    class func instanceMapLayer() -> MapLayer{
        // type method implementation goes here
        var nibView = NSBundle.mainBundle().loadNibNamed("MapLayer", owner: nil, options: nil) as [AnyObject]
        return nibView[0] as! MapLayer
    }
    
    //xib初始化时调用
    required init(coder decoder: NSCoder){
        super.init(coder: decoder)
        //fatalError("init(coder:) has not been implemented")
    }
    
    //普通方法，设置属性
    func initView() -> MapLayer{
        //w76,h43(top: CGFloat, left: CGFloat, bottom: CGFloat, right: CGFloat)
        layerImage.image = UIImage(named: "button_main_sel_background_2")?.resizableImageWithCapInsets(UIEdgeInsetsMake(20, 20, 20, 38), resizingMode: UIImageResizingMode.Stretch)
        selectImage1.image = UIImage(named: "map_setting_view_btn_highlighted")?.resizableImageWithCapInsets(UIEdgeInsetsMake(8, 8, 8, 8), resizingMode: UIImageResizingMode.Stretch)
        selectImage2.image = UIImage(named: "map_setting_view_btn_highlighted")?.resizableImageWithCapInsets(UIEdgeInsetsMake(8, 8, 8, 8), resizingMode: UIImageResizingMode.Stretch)
        selectImage3.image = UIImage(named: "map_setting_view_btn_highlighted")?.resizableImageWithCapInsets(UIEdgeInsetsMake(8, 8, 8, 8), resizingMode: UIImageResizingMode.Stretch)
        if(MapDefaults.sharedInstance.mapLayerType == BMKMapTypeSatellite){
            selectImage1.tag = 21
            selectImage(selectImage1)
        } else if(MapDefaults.sharedInstance.mapRotate == 0){
            selectImage2.tag = 22
            selectImage(selectImage2)
        } else {
            selectImage3.tag = 23
            selectImage(selectImage3)
        }
        favoriteSwitc.on = MapDefaults.sharedInstance.mapPoint
        hotSwitc.on = MapDefaults.sharedInstance.mapBaiduHeatMapEnabled
        self.hidden = true
        return self
    }
    
    // 弹跳动画效果
    func bounceAnimation(){
        // 设置缩放参考点为右上角
        var frame = layerView.frame
        layerView.layer.anchorPoint = CGPointMake(0.98, 0)
        layerView.frame = frame
        
        self.hidden = false
        self.bg.alpha = 0
        layerView.transform = CGAffineTransformScale(CGAffineTransformIdentity, 0.1, 0.1)
        UIView.animateWithDuration(0.2, animations: { () -> Void in
            self.bg.alpha = 0.4
            self.layerView.transform = CGAffineTransformScale(CGAffineTransformIdentity, 1.1, 1.1)
            }) { (finished: Bool) -> Void in
            self.bounceAnimationStoped()
        }
    }
    
    func bounceAnimationStoped(){
        UIView.animateWithDuration(0.1, animations: { () -> Void in
            self.layerView.transform = CGAffineTransformScale(CGAffineTransformIdentity, 1, 1)
        })
    }
    
    @IBAction func closeLayer(sender: AnyObject) {
        self.layerView.transform = CGAffineTransformScale(CGAffineTransformIdentity, 1, 1)
        UIView.animateWithDuration(0.2, animations: { () -> Void in
            self.bg.alpha = 0
            self.layerView.transform = CGAffineTransformScale(CGAffineTransformIdentity, 0.1, 0.1)
            }) { (finished: Bool) -> Void in
                self.hidden = true
        }
    }
    
    /**
    *保存地图类型
    */
    @IBAction func selectImage(sender: AnyObject) {
        if(sender.tag == 21){
            selectImage1.hidden = false
            selectImage2.hidden = true
            selectImage3.hidden = true
            MapDefaults.sharedInstance.mapLayerType = BMKMapTypeSatellite
            MapDefaults.sharedInstance.mapRotate = 0
            MapDefaults.sharedInstance.mapBuildingsEnabled = false
        } else if(sender.tag == 22){
            selectImage1.hidden = true
            selectImage2.hidden = false
            selectImage3.hidden = true
            MapDefaults.sharedInstance.mapLayerType = BMKMapTypeStandard
            MapDefaults.sharedInstance.mapRotate = 0
            MapDefaults.sharedInstance.mapBuildingsEnabled = false
        } else if(sender.tag == 23){
            selectImage1.hidden = true
            selectImage2.hidden = true
            selectImage3.hidden = false
            MapDefaults.sharedInstance.mapLayerType = BMKMapTypeStandard
            MapDefaults.sharedInstance.mapRotate = 45
            MapDefaults.sharedInstance.mapBuildingsEnabled = true
        }
//        println("执行setMapType\(MapDefaults.sharedInstance.mapLayerType) \(MapDefaults.sharedInstance.mapRotate) \(MapDefaults.sharedInstance.mapBuildingsEnabled)")
        NSNotificationCenter.defaultCenter().postNotificationName("setMapType", object: nil)
    }
    

    @IBAction func layerSwitch(sender: AnyObject) {
        if(sender.tag == 24){
            MapDefaults.sharedInstance.mapPoint = favoriteSwitc.on
//            println("执行setShowPoint\(MapDefaults.sharedInstance.mapPoint)")
            NSNotificationCenter.defaultCenter().postNotificationName("setShowPoint", object: nil)
        } else if(sender.tag == 25){
            MapDefaults.sharedInstance.mapBaiduHeatMapEnabled = hotSwitc.on
//            println("执行setHeatMap\(MapDefaults.sharedInstance.mapBaiduHeatMapEnabled)")
            NSNotificationCenter.defaultCenter().postNotificationName("setHeatMap", object: nil)
        }
    }
    
    /*
    // Only override drawRect: if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func drawRect(rect: CGRect) {
        // Drawing code
    }
    */

}
