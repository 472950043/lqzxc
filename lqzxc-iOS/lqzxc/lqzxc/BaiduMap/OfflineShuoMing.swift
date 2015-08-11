//
//  OfflineShuoMing.swift
//  lqzxc
//
//  Created by jyb on 15/8/10.
//  Copyright (c) 2015年 jyb. All rights reserved.
//

import UIKit

class OfflineShuoMing: UIView {
    override init(frame: CGRect){
        super.init(frame: frame)
    }
    
    required init(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
//        fatalError("init(coder:) has not been implemented")
    }
    

    func instanceOfflineShuoMing(width: CGFloat) {
        // type method implementation goes here
        self.backgroundColor = UIColor(red: 230.0/255, green: 230.0/255, blue: 230.0/255, alpha: 1)
        
        var title = initView("暂无下载，点击“城市列表”进行下载吧！")
        title.frame = CGRectMake(15, 0, width - 30, 53)
        self.addSubview(title)
        
        var label = initView("使用离线地图的好处：")
        label.frame = CGRectMake(15, 40, width - 30, 44)
        self.addSubview(label)
        
        var label1 = initView("● 节省90%的手机流量")
        label1.font = UIFont.systemFontOfSize(13)
        label1.frame = CGRectMake(35, 75, width - 30, 31)
        self.addSubview(label1)
        
        var label2 = initView("● 地图流量健步如飞")
        label2.font = UIFont.systemFontOfSize(13)
        label2.frame = CGRectMake(35, 100, width - 30, 24)
        self.addSubview(label2)
        
        var label3 = initView("离线地图下载方式：")
        label3.frame = CGRectMake(15, 134, width - 30, 43)
        self.addSubview(label3)
        
        var method1 = initView("方法1：直接在手机下载")
        method1.font = UIFont.systemFontOfSize(15)
        method1.frame = CGRectMake(15, 169, width - 30, 43)
        self.addSubview(method1)
        
        var detail1 = initView("点击“城市列表”标签，选择城市下载")
        detail1.font = UIFont.systemFontOfSize(13)
        detail1.frame = CGRectMake(35, 206, width - 30, 24)
        self.addSubview(detail1)
        
        var method2 = initView("方法2：电脑下载，iTunes导入")
        method2.font = UIFont.systemFontOfSize(15)
        method2.frame = CGRectMake(15, 229, width - 30, 43)
        self.addSubview(method2)
        
        var detail2 = initView("登陆百度地图手机官网(http://wuxian.baidu.com/map/)下载所需离线地图")
        detail2.font = UIFont.systemFontOfSize(13)
        detail2.frame = CGRectMake(35, 270, width - 50, 40)
        detail2.lineBreakMode = NSLineBreakMode.ByCharWrapping
        detail2.numberOfLines = 0
        self.addSubview(detail2)
        
        var detail3 = initView("注：导入前，建议点击“手动导入离线地图”查看导入说明")
        detail3.font = UIFont.systemFontOfSize(13)
        detail3.frame = CGRectMake(15, 315, width - 35, 40)
        detail3.lineBreakMode = NSLineBreakMode.ByCharWrapping
        detail3.numberOfLines = 0
        self.addSubview(detail3)
        
    }
    
    func initView(title: String) -> UILabel {
        var view = UILabel()
        self.addSubview(view)
        view.text = title
        view.textAlignment = NSTextAlignment.Left
        view.textColor = UIColor(red: 51.0/255, green: 51.0/255, blue: 51.0/255, alpha: 1)
        view.lineBreakMode = NSLineBreakMode.ByTruncatingTail//省略末尾
        view.font = UIFont.systemFontOfSize(16)
        view.backgroundColor = UIColor.clearColor()
        return view
    }
    /*
    // Only override drawRect: if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func drawRect(rect: CGRect) {
        // Drawing code
    }
    */

}
