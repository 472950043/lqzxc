//
//  LibraryAPI.swift
//  lqzxc
//
//  Created by jyb on 15/7/25.
//  Copyright (c) 2015年 jyb. All rights reserved.
//

import UIKit

class LibraryAPI: NSObject {
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
    
    override init() {
        list = ListModel()
        super.init()
    }
    
    func download() -> String {
        var req = NSMutableURLRequest(URL: NSURL(string: "http://www.luqiaobike.com/branch/bmap")!)//创建url创建请求
        req.HTTPMethod = "GET"
        req.timeoutInterval = 5
        //var error = NSErrorPointer()
        var datas = NSURLConnection.sendSynchronousRequest(req, returningResponse: nil, error: nil)//连接服务器
        if(datas == nil){
            return "网络连接失败"
        }
        var datastring = NSString(data: datas!, encoding: NSUTF8StringEncoding) as! String
//        println(datastring)
        datastring = substring(datastring, start: "points[", end: "nowRunlClass");
        datastring = datastring.stringByReplacingOccurrencesOfString("\r", withString: "", options: nil, range: nil)
        datastring = datastring.stringByReplacingOccurrencesOfString("\n", withString: "", options: nil, range: nil)
        datastring = datastring.stringByReplacingOccurrencesOfString("\t", withString: "", options: nil, range: nil)//.replaceAll("\r|\n|\t", "");
        var s = datastring.componentsSeparatedByString("points")
        for var i = 0; i < s.count - 1; i++ {
            list.bikeSties.addObject(initBikeStie(i, datas: s[i]))
        }
        list.count = list.bikeSties.count
        list.page = list.bikeSties.count / 20 + 1
        return "成功"
    }
    
    func initBikeStie(index: Int, datas: String) -> BikeStie{
        var mBikeStie = BikeStie()
        mBikeStie.id = index
        mBikeStie.mid = substring(datas, start: "id : \"", end: "\",gpsx")
        mBikeStie.gpsx = substring(datas, start: "gpsx : parseFloat(\"", end: "\"),gpsy")
        mBikeStie.gpsy = substring(datas, start: "gpsy : parseFloat(\"", end: "\")  ,netName")
        if(NSString(string: mBikeStie.gpsx).length + NSString(string: mBikeStie.gpsy).length > 3){
            mBikeStie.pt = stringToCLLocationCoordinate2D(mBikeStie.gpsy, lng: mBikeStie.gpsx)
        }
        mBikeStie.netName = substring(datas, start: "netName : \"", end: "\"  ,netType")
        mBikeStie.netType = substring(datas, start: "netType : \"", end: "\"  ,netStatus")
        mBikeStie.netStatus = substring(datas, start: "netStatus : \"", end: "\"  ,openDate")
        mBikeStie.netLevel = substring(datas, start: "netLevel : \"", end: "\"  ,address")
        mBikeStie.address = substring(datas, start: "address : \"", end: "\"  ,trafficInfo")
        mBikeStie.trafficInfo = substring(datas, start: "trafficInfo : \"", end: "\"  ,bicycleCapacity")
        mBikeStie.bicycleCapacity = substring(datas, start: "bicycleCapacity:\"", end: "\"  ,bicycleNum").toInt()!
        mBikeStie.bicycleNum = substring(datas, start: "bicycleNum:\"", end: "\"  ,imageAttach").toInt()!
        if datas.componentsSeparatedByString(".jpg'").count > 1 {
            mBikeStie.imageAttach = substring(datas, start: "imageAttach : '", end: ".jpg'};") + ".jpg"
        }
        return mBikeStie
    }
    
    func substring(datas: String, start: String, end: String) -> String{
        var s = advance(datas.rangeOfString(start)!.endIndex, 0)
        var e = advance(datas.rangeOfString(end)!.startIndex, 0)
        return datas.substringWithRange(Range<String.Index>(start: s, end: e))
    }
    
    func stringToCLLocationCoordinate2D(lat: String, lng: String) -> CLLocationCoordinate2D{
        return CLLocationCoordinate2D(latitude: NSString(string: lat).doubleValue, longitude: NSString(string: lng).doubleValue)
    }
    
    
}
