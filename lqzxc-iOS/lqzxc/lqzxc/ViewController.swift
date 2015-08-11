//
//  ViewController.swift
//  lqzxc
//
//  Created by jyb on 15/7/25.
//  Copyright (c) 2015年 jyb. All rights reserved.
//

import UIKit

class ViewController: UIViewController, UITableViewDataSource, UITableViewDelegate, UISearchBarDelegate {

//    , BMKMapViewDelegate
//    @IBOutlet var mapview: BMKMapView!
    
    @IBOutlet var searchBar: UISearchBar!
    @IBOutlet var resultTableView: UITableView!
    @IBOutlet var coverView: UIView!
    @IBOutlet var searchTableView: UITableView!
    
    var tabbar: UITabBarController!
    var searchKey = ""
    var searchlist = ListModel()
    var mMake = MakeTableView()

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.

        coverView.hidden = true
        searchBar.setShowsCancelButton(false, animated: false)
        tabbar = self.parentViewController?.parentViewController as! UITabBarController
        
        resultTableView.tableHeaderView = mMake.borderHeadLine()
        searchTableView.tableHeaderView = mMake.borderHeadLine()

        mMake.tableFootStyleNone(resultTableView)
        mMake.tableFootStyleNone(searchTableView)
        
        self.navigationController?.navigationBar
    }
    
    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "refresh", name: "finish", object: LibraryAPI.sharedInstance.list)
    }
    
    override func viewWillDisappear(animated: Bool) {
        super.viewWillDisappear(animated)
        NSNotificationCenter.defaultCenter().removeObserver(self)
    }
    
    @IBAction func redown(sender: AnyObject) {
        LibraryAPI.sharedInstance.download("椒江黄岩路桥温岭")
    }
    
    func refresh(){
        if(resultTableView != nil){
            resultTableView.reloadData()
        }
    }
    
    func searchBarTextDidBeginEditing(searchBar: UISearchBar){
        searchBar.text = searchKey
        showCoverView(true)
    }
    
    func searchBarTextDidEndEditing(searchBar: UISearchBar){
        if (NSString(string: searchBar.text).length > 0) {
        } else{
            showCoverView(false)
        }
    }
    
    func searchBarCancelButtonClicked(searchBar: UISearchBar){
        searchBar.resignFirstResponder()
        searchKey = searchBar.text
        searchBar.text = ""
        showCoverView(false)
    }
    
    func showCoverView(flag: Bool){
        if(flag) {
            tabbar.tabBar.hidden = true//隐藏系统默认的样式
            coverView.hidden = false
            searchBar.setShowsCancelButton(true, animated: true)
            for view in searchBar.subviews[0].subviews {
                if view.isKindOfClass(UIButton){
                    var cancel = view as! UIButton
                    cancel.setTitle("取消", forState: UIControlState.Normal)
                }
            }
            searchlist.count = 0
            searchlist.areas.removeAllObjects()
            searchTableView.reloadData()
            self.navigationController?.setNavigationBarHidden(true, animated: true)
        } else {
            tabbar.tabBar.hidden = false//隐藏系统默认的样式
            coverView.hidden = true
            searchBar.setShowsCancelButton(false, animated: true)
            self.navigationController?.setNavigationBarHidden(false, animated: true)
        }
    }
    
    func searchBar(searchBar: UISearchBar, textDidChange searchText: String){
        if (NSString(string: searchText).length > 0) {
            searchByKey()
        } else {
        }
    }
    
    func searchBarSearchButtonClicked(searchBar: UISearchBar){
        searchBar.resignFirstResponder()
//        searchByKey()
    }
    
    func searchByKey(){
        searchlist.count = 0
        searchlist.areas.removeAllObjects()
        for var i = 0; i < LibraryAPI.sharedInstance.list.areas.count; i++ {
            var area = LibraryAPI.sharedInstance.list.areas[i] as! AreaModel
            var searchArea = AreaModel()
            for var j = 0; j < area.sites.count; j++ {
                var bikeStie = area.sites[j] as! BikeStie
                if(bikeStie.netName.componentsSeparatedByString(searchBar.text).count > 1 || bikeStie.headAlphabet.lowercaseString.componentsSeparatedByString(searchBar.text).count > 1){//componentsSeparatedByString(searchBar.text).count > 1
                    searchArea.sites.addObject(bikeStie)
                }
            }
            if(searchArea.sites.count > 0){
                searchArea.name = area.name
                searchArea.url = area.url
                searchArea.count = searchArea.sites.count
                searchArea.page = searchArea.sites.count / 20 + 1
                searchlist.areas.addObject(searchArea)
                searchlist.count = searchlist.count + searchArea.count
                println(searchArea.name + ":\(searchArea.sites.count)")
            }
        }
        println("搜索完成，总数\(searchlist.count)")
        searchTableView.reloadData()
    }
    
    func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        // #warning Potentially incomplete method implementation.
        // Return the number of sections.
        if(resultTableView == tableView){
            return LibraryAPI.sharedInstance.list.areas.count
        }
        return searchlist.areas.count
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete method implementation.
        // Return the number of rows in the section.
        if(resultTableView == tableView){
            var area = LibraryAPI.sharedInstance.list.areas[section] as! AreaModel
            return area.sites.count
        } else if(searchlist.areas.count > 0){
            var area = searchlist.areas[section] as! AreaModel
            return area.sites.count
        }
        return 0
    }
    
    //自定义section标题
    func tableView(tableView: UITableView, viewForHeaderInSection section: Int) -> UIView?{
        var height = CGFloat(22)
        if(resultTableView == tableView){
            var area = LibraryAPI.sharedInstance.list.areas[section] as! AreaModel
            return mMake.sectionHeadView(area.name + ":\(area.count)", height: height)
        }
        var area = searchlist.areas[section] as! AreaModel
        return mMake.sectionHeadView(area.name + ":\(area.count)", height: height)
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        if(resultTableView == tableView){
            let cell = tableView.dequeueReusableCellWithIdentifier("cell", forIndexPath: indexPath) as! UITableViewCell
            //        var text1 = cell.viewWithTag(1) as! UILabel
            var area = LibraryAPI.sharedInstance.list.areas[indexPath.section] as! AreaModel
            var bikeStie = area.sites[indexPath.row] as! BikeStie
            cell.textLabel?.text = "\(bikeStie.id + 1) " + bikeStie.netName//bikeStie.mid +
            //        text2.text = "库存:\(mBikeStie.bicycleNum) 可还:\(mBikeStie.bicycleCapacity - mBikeStie.bicycleNum)"
            return cell
        }
        let cell = tableView.dequeueReusableCellWithIdentifier("cell", forIndexPath: indexPath) as! UITableViewCell
        //        var text1 = cell.viewWithTag(1) as! UILabel
        var area = searchlist.areas[indexPath.section] as! AreaModel
        var bikeStie = area.sites[indexPath.row] as! BikeStie
        cell.textLabel?.text = "\(bikeStie.id + 1) " + bikeStie.netName//bikeStie.mid +
        //        text2.text = "库存:\(mBikeStie.bicycleNum) 可还:\(mBikeStie.bicycleCapacity - mBikeStie.bicycleNum)"
        return cell
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath){
        //消除cell选择痕迹
        tableView.deselectRowAtIndexPath(indexPath, animated: true)
        var area: AreaModel
        if(resultTableView == tableView){
            area = LibraryAPI.sharedInstance.list.areas[indexPath.section] as! AreaModel
        } else {
            searchBar.resignFirstResponder()
            area = searchlist.areas[indexPath.section] as! AreaModel
        }
        var bikeStie = area.sites[indexPath.row] as! BikeStie
        if(area.name ==  "路桥" || area.name ==  "椒江"){
            LibraryAPI.sharedInstance.showBikeStie(area.name, bikeStie: bikeStie)
        } else {
            dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), {
                bikeStie = LibraryAPI.sharedInstance.getBikeSite(area.name, url: area.url + "?id=" + bikeStie.mid)
                dispatch_async(dispatch_get_main_queue(), {
                    LibraryAPI.sharedInstance.showBikeStie(area.name, bikeStie: bikeStie)
                })
            })
        }
           // ?id=86
    }
    
        
//    func alertView(alertView: UIAlertView, clickedButtonAtIndex buttonIndex: Int){
//        
//        
//        for window in UIApplication.sharedApplication().windows {
//            println(window)
//            if(window.windowLevel == UIWindowLevelNormal){
////                window.makeKeyAndVisible()
//            }
//        }
//    }
    
//    override func viewWillAppear(animated: Bool) {
//        super.viewWillAppear(animated)
//        mapview.delegate = self; // 此处记得不用的时候需要置nil，否则影响内存的释放
//    }
//    
//    override func viewWillDisappear(animated: Bool) {
//        super.viewWillDisappear(animated)
//        mapview = nil; // 不用时，置nil
//    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    
}

