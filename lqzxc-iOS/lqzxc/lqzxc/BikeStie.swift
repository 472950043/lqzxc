//
//  BikeStie.swift
//  lqzxc
//
//  Created by jyb on 15/7/25.
//  Copyright (c) 2015年 jyb. All rights reserved.
//

import UIKit

class BikeStie: NSObject {
    var id: Int // 顺序号(外部编号)
    var mid: String// 网点编号(内部编号)
    var gpsx: String// 经度
    var gpsy: String// 纬度
    var pt: CLLocationCoordinate2D// 经纬度标识
    var netName: String// 网点名
    var netType: String// 网点类型
    var netStatus: String// 网点状态
    var netLevel: String// 网点等级
    var address: String// 地址
    var trafficInfo: String// 周边公交信息
    var bicycleCapacity: Int// 总数
    var bicycleNum: Int// 库存
    var imageAttach: String// 图片相对路径
    var visited: Int// 访问数
    var time: String// 访问时间
    
    override init() {
        id = 0
        mid = ""
        gpsx = ""
        gpsy = ""
        pt = CLLocationCoordinate2D()
        netName = ""
        netType = ""
        netStatus = ""
        netLevel = ""
        address = ""
        trafficInfo = ""
        bicycleCapacity = 0
        bicycleNum = 0
        imageAttach = ""
        visited = 0
        time = ""
    }
}


class ListModel: NSObject {
    var count: Int
    var page: Int
    var bikeSties: NSMutableArray
    
    override init() {
        count = 0
        page = 0
        bikeSties = NSMutableArray()
    }
}
