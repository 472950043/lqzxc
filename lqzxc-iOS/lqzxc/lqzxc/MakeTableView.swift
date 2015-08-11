//
//  MakeTableView.swift
//  lqzxc
//
//  Created by jyb on 15/7/28.
//  Copyright (c) 2015年 jyb. All rights reserved.
//

import UIKit

class MakeTableView: NSObject {
    var width = UIScreen.mainScreen().applicationFrame.size.width
    var backgroundColor = UIColor(red: 239.0/255, green: 239.0/255, blue: 244.0/255, alpha: 1)
    var borderColor = UIColor(red: 197.0/255, green: 197.0/255, blue: 197.0/255, alpha: 1)
    var sectionHeadTextColor = UIColor(red: 153.0/255, green: 153.0/255, blue: 153.0/255, alpha: 1)
   
    func tableHeaderStyleNone(tableView: UITableView!){
        //头部
        tableView.tableHeaderView = UIView(frame:CGRectMake(0, 0, width, 0))
    }
    
    func tableFootStyleNone(tableView: UITableView!){
        //尾部
        tableView.tableFooterView = UIView(frame:CGRectMake(0, 0, width, 0))
    }
    
    //上划线
    func borderHeadLine() -> UIView{
        var view = UIView(frame:CGRectMake(0, 0, width, 0.5))
        view.layer.borderWidth = 0.5
        view.layer.borderColor = borderColor.CGColor
        return view
    }
    
    //下划线
    func borderFootLine(height: CGFloat) -> UIView{
        var view = UIView(frame:CGRectMake(0, height, width, -0.25))// - 0.5
        view.layer.borderWidth = 0.25
        view.layer.borderColor = borderColor.CGColor
        return view
    }
    
    func sectionHeadView(text: String, height: CGFloat) -> UIView{
        //背景
        var headerView = UIView(frame:CGRectMake(0, 0, width, height))
        headerView.backgroundColor = backgroundColor
//        headerView.addSubview(borderHeadLine())
//        headerView.addSubview(borderFootLine(height))
        //标题
        var titleLabel = UILabel(frame:CGRectMake(15, 1, width - 15, height - 2))
        titleLabel.text = text
        titleLabel.textColor = sectionHeadTextColor
        titleLabel.font = UIFont.systemFontOfSize(12)
        headerView.addSubview(titleLabel)
        return headerView
    }
}
