//
//  LibraryAPI.swift
//  lqzxc
//
//  Created by jyb on 15/7/25.
//  Copyright (c) 2015年 jyb. All rights reserved.
//

import UIKit

class LibraryAPI: NSObject, UIAlertViewDelegate {
    //1 单例模式
    class var sharedInstance: LibraryAPI {
        //2
        struct Singleton {
            //3 创建初始化
            static let instance = LibraryAPI()
        }
        //4
        return Singleton.instance
    }
    
    var list: ListModel
    var times = 0 // 自动重试次数2
    var Retry = 2 // 自动重试次数2
    var isLoading = false

    override init() {
        list = ListModel()
        
        var area = AreaModel()
        area.name = "椒江"
        area.url = "http://www.zjtzpb.com/tzmap/ibikestation.asp"
        list.areas.addObject(area)
        area = AreaModel()
        area.name = "黄岩"
        area.url = "http://www.tzhypb.com/hymap/ibikestation.asp"
        list.areas.addObject(area)
        area = AreaModel()
        area.name = "路桥"
        area.url = "http://www.luqiaobike.com/branch/bmap"
        list.areas.addObject(area)
        area = AreaModel()
        area.name = "温岭"
        area.url = "http://www.zjwlpb.com/wlmap/ibikestation.asp"
        list.areas.addObject(area)
        super.init()
    }
    
    //椒江 zjtzpb.com/tzmap/ibikestation.asp zjtzpb.com/tzmap/index.htm
    //黄岩 tzhypb.com/hymap/index.htm
    //路桥 luqiaobike.com/branch/bmap
    //温岭 zjwlpb.com/map.asp //zjwlpb.com/wlmap
    func download(tag: String){
        if(!isLoading){
            dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), {
                for var i = 0; i < self.list.areas.count; i++ {
                    var name = self.list.areas[i].name
                    if(tag.componentsSeparatedByString(name).count > 1){
                        self.isLoading = true
                        var msg = self.download(name, index: i)
                        self.isLoading = false
                        if(msg == "成功"){
                            self.times = 0
                        } else if(msg == "网络连接失败" && self.times < self.Retry){
                            NSThread.sleepForTimeInterval(0.1)
                            self.download(name)
                            self.times = self.times + 1
                        }
                    }
                }
                dispatch_async(dispatch_get_main_queue(), {
                    NSNotificationCenter.defaultCenter().postNotificationName("finish", object: self.list)
                    self.list.count = 0
                    for var i = 0; i < self.list.areas.count; i++ {
                        self.list.count = self.list.count + (self.list.areas[i] as! AreaModel).sites.count//总数
                    }
                    println("下载完成，总数\(self.list.count)")
                })
            })
        }
    }
    
    func download(tag: String, index: Int) -> String {
        var req = NSMutableURLRequest(URL: NSURL(string: list.areas[index].url)!)//创建url创建请求
        req.HTTPMethod = "GET"
        req.timeoutInterval = 5
        //var error = NSErrorPointer()
        var datas = NSURLConnection.sendSynchronousRequest(req, returningResponse: nil, error: nil)//连接服务器
        if(datas == nil){
            return "网络连接失败"
        }
        if(tag == "椒江" || tag == "黄岩" || tag == "温岭"){
            var datastring = NSString(data: datas!, encoding: NSUTF8StringEncoding) as! String
            datastring = substring(datastring, start: "var ibike = ", end: "} ] }") + "} ] }"
            // 将JSON串转化为字典或者数组
            var json: AnyObject? = NSJSONSerialization.JSONObjectWithData(datastring.dataUsingEncoding(NSUTF8StringEncoding)! , options: NSJSONReadingOptions.AllowFragments, error: nil)
//            println(json)
            var stations = json?.objectForKey("station") as! NSArray
            (list.areas[index] as! AreaModel).sites.removeAllObjects()
            for var i = 0; i < stations.count; i++ {
                var bikeStie = BikeStie()
                bikeStie.id = i
                var mid = stations[i].objectForKey("id") as! Int
                bikeStie.mid = "\(mid)"
                var lng = stations[i].objectForKey("lng") as! Double
                bikeStie.gpsx = "\(lng)"
                var lat = stations[i].objectForKey("lat") as! Double
                bikeStie.gpsy = "\(lat)"
                if(NSString(string: bikeStie.gpsx).length + NSString(string: bikeStie.gpsy).length > 3){
                    bikeStie.pt = stringToCLLocationCoordinate2D(bikeStie.gpsy, lng: bikeStie.gpsx)
                }
                bikeStie.netName = stations[i].objectForKey("name") as! String
                bikeStie.headAlphabet = getHeadAlphabet(bikeStie.netName)
                bikeStie.address = stations[i].objectForKey("address") as! String
                (list.areas[index] as! AreaModel).sites.addObject(bikeStie)
            }
            (list.areas[index] as! AreaModel).count = (list.areas[index] as! AreaModel).sites.count
            (list.areas[index] as! AreaModel).page = (list.areas[index] as! AreaModel).sites.count / 20 + 1
            println((list.areas[index] as! AreaModel).name + ":\((list.areas[index] as! AreaModel).sites.count)")
            return "成功"
        }         
        var datastring = NSString(data: datas!, encoding: NSUTF8StringEncoding) as! String
        //println(datastring)
        datastring = substring(datastring, start: "points[", end: "nowRunlClass")
        datastring = datastring.stringByReplacingOccurrencesOfString("\r", withString: "", options: nil, range: nil)
        datastring = datastring.stringByReplacingOccurrencesOfString("\n", withString: "", options: nil, range: nil)
        datastring = datastring.stringByReplacingOccurrencesOfString("\t", withString: "", options: nil, range: nil)//.replaceAll("\r|\n|\t", "")
        var s = datastring.componentsSeparatedByString("points")
        (list.areas[index] as! AreaModel).sites.removeAllObjects()
        for var i = 0; i < s.count - 1; i++ {
            (list.areas[index] as! AreaModel).sites.addObject(initLqBikeStie(i, datas: s[i]))
        }
        (list.areas[index] as! AreaModel).count = (list.areas[index] as! AreaModel).sites.count
        (list.areas[index] as! AreaModel).page = (list.areas[index] as! AreaModel).sites.count / 20 + 1
        println((list.areas[index] as! AreaModel).name + ":\((list.areas[index] as! AreaModel).sites.count)")
        return "成功"
    }
    
    func initLqBikeStie(index: Int, datas: String) -> BikeStie {
        var bikeStie = BikeStie()
        bikeStie.id = index
        bikeStie.mid = substring(datas, start: "id : \"", end: "\",gpsx")
        bikeStie.gpsx = substring(datas, start: "gpsx : parseFloat(\"", end: "\"),gpsy")
        bikeStie.gpsy = substring(datas, start: "gpsy : parseFloat(\"", end: "\")  ,netName")
        if(NSString(string: bikeStie.gpsx).length + NSString(string: bikeStie.gpsy).length > 3){
            bikeStie.pt = stringToCLLocationCoordinate2D(bikeStie.gpsy, lng: bikeStie.gpsx)
        }
        bikeStie.netName = substring(datas, start: "netName : \"", end: "\"  ,netType")
        bikeStie.headAlphabet = getHeadAlphabet(bikeStie.netName)
        bikeStie.netType = substring(datas, start: "netType : \"", end: "\"  ,netStatus")
        bikeStie.netStatus = substring(datas, start: "netStatus : \"", end: "\"  ,openDate")
        bikeStie.netLevel = substring(datas, start: "netLevel : \"", end: "\"  ,address")
        bikeStie.address = substring(datas, start: "address : \"", end: "\"  ,trafficInfo")
        bikeStie.trafficInfo = substring(datas, start: "trafficInfo : \"", end: "\"  ,bicycleCapacity")
        var bicycleCapacity = substring(datas, start: "bicycleCapacity:\"", end: "\"  ,bicycleNum")
        if(count(bicycleCapacity) > 1){//Elements
            bikeStie.bicycleCapacity = bicycleCapacity.toInt()!
            bikeStie.bicycleNum = substring(datas, start: "bicycleNum:\"", end: "\"  ,imageAttach").toInt()!
        }

        if datas.componentsSeparatedByString(".jpg'").count > 1 {
            bikeStie.imageAttach = substring(datas, start: "imageAttach : '", end: ".jpg'};") + ".jpg"
        }
        return bikeStie
    }
    
    func getHeadAlphabet(data: String) -> String{
        var str = NSMutableString(string: data)
        CFStringTransform(str, nil, kCFStringTransformMandarinLatin, Boolean(0))
        CFStringTransform(str, nil, kCFStringTransformStripCombiningMarks, Boolean(0))
        var res = str as String
        res = res.stringByReplacingOccurrencesOfString(" ", withString: "")//(" ", withString: "", options: nil, range: nil)
//        println(str)
        return res
    }
    
    //var isinglebike = { "station":[{"id": 86, "name": "七号泵站", "lat": 28.631094, "lng": 121.256409, "capacity": 30, "availBike": 14,"address": ""} ] }
    //var isinglebike = { "station":[{"id": 149, "name": "东辉公园（西）", "lat": 28.363535, "lng": 121.376351, "capacity": 22, "availBike": 10,"address": "东辉中路东辉公园西入口"} ] }
    func getBikeSite(tag: String, url: String) -> BikeStie {//黄岩温岭
        var req = NSMutableURLRequest(URL: NSURL(string: url)!)//创建url创建请求
        req.HTTPMethod = "GET"
        req.timeoutInterval = 5
        //var error = NSErrorPointer()
        var datas = NSURLConnection.sendSynchronousRequest(req, returningResponse: nil, error: nil)//连接服务器
        var bikeStie = BikeStie()
        if(datas == nil){
            return bikeStie
        }
        var datastring = NSString(data: datas!, encoding: NSUTF8StringEncoding) as! String
        datastring = substring(datastring, start: "var isinglebike = ", end: "} ] }") + "} ] }"
        // 将JSON串转化为字典或者数组
        var json: AnyObject? = NSJSONSerialization.JSONObjectWithData(datastring.dataUsingEncoding(NSUTF8StringEncoding)! , options: NSJSONReadingOptions.AllowFragments, error: nil)
//        println(json)
        var stations = json?.objectForKey("station") as! NSArray
        for var i = 0; i < stations.count; i++ {
            var mid = stations[i].objectForKey("id") as! Int
            bikeStie.mid = "\(mid)"
            bikeStie.netName = stations[i].objectForKey("name") as! String
            var lng = stations[i].objectForKey("lng") as! Double
            bikeStie.gpsx = "\(lng)"
            var lat = stations[i].objectForKey("lat") as! Double
            bikeStie.gpsy = "\(lat)"
            if(tag == "黄岩" || tag == "温岭"){
                bikeStie.bicycleCapacity = stations[i].objectForKey("capacity") as! Int
                bikeStie.bicycleNum = stations[i].objectForKey("availBike") as! Int
            }
            bikeStie.address = stations[i].objectForKey("address") as! String
        }
        return bikeStie
    }
    
    
    func substring(datas: String, start: String, end: String) -> String{
        var s = advance(datas.rangeOfString(start)!.endIndex, 0)
        var e = advance(datas.rangeOfString(end)!.startIndex, 0)
        return datas.substringWithRange(Range<String.Index>(start: s, end: e))
    }
    
    func stringToCLLocationCoordinate2D(lat: String, lng: String) -> CLLocationCoordinate2D{
        return CLLocationCoordinate2D(latitude: NSString(string: lat).doubleValue, longitude: NSString(string: lng).doubleValue)
    }
    
    func showBikeStie(tag: String, bikeStie: BikeStie){
        //椒江 http://www.zjtzpb.com/tzmap/ibikegif.asp?id=6&flag=1
        //温岭 http://www.zjwlpb.com/wlmap/img/16.jpg
        if(tag ==  "路桥" || tag == "黄岩" || tag == "温岭"){
            var alertView = UIAlertView()
            alertView.delegate = self
            alertView.title = bikeStie.netName
            alertView.message = "可借：\(bikeStie.bicycleNum) 可还：\(bikeStie.bicycleCapacity - bikeStie.bicycleNum)"
            alertView.addButtonWithTitle("收藏")
            alertView.addButtonWithTitle("关闭")
            alertView.show()
        } else {
            var detail = DetailView.instanceGridView()
            detail.initImgView(bikeStie.netName, text1: "可借：", text3: "可还：", text5: "收藏", text6: "关闭")
            //            [UIImage imageWithData:[NSDatadataWithContentsOfURL:[NSURLURLWithString:[picsURL objectAtIndex:choice]]]];
            //
            //            [self.imageView setImage:image];
            detail.img1.image = UIImage(data: NSData(contentsOfURL: NSURL(string: "http://www.zjtzpb.com/tzmap/ibikegif.asp?id=\(bikeStie.mid)&flag=1")!)!)
            detail.img2.image = UIImage(data: NSData(contentsOfURL: NSURL(string: "http://www.zjtzpb.com/tzmap/ibikegif.asp?id=\(bikeStie.mid)&flag=2")!)!)
            //.img1.sd_setImageWithURL(NSURL(string: ))
            //            detail.img2.sd_setImageWithURL(NSURL(string: "http://www.zjtzpb.com/tzmap/ibikegif.asp?id=\(bikeStie.mid)&flag=2"))
            detail.show()
            detail.btn1.addTarget(self, action:"butClick:", forControlEvents:UIControlEvents.TouchUpInside)
            detail.btn2.addTarget(self, action:"butClick:", forControlEvents:UIControlEvents.TouchUpInside)
        }
    }
    
    
    func butClick(sender: UIButton) {
        if(sender.tag == 1){
            
        }
    }
    
}
