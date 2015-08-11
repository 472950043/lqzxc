//
//  LocationTableViewController.swift
//  lqzxc
//
//  Created by jyb on 15/7/30.
//  Copyright (c) 2015年 jyb. All rights reserved.
//

import UIKit

class LocationTableViewController: UITableViewController, BMKLocationServiceDelegate {

    var locService: BMKLocationService!
    var searchlist = ListModel()
    var mMake = MakeTableView()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
        
        locService = BMKLocationService()
    }
    
    @IBAction func reStart(sender: AnyObject) {
        locService.startUserLocationService()
    }
    
    //实现相关delegate 处理位置信息更新
    //处理方向变更信息
    func didUpdateUserHeading(userLocation: BMKUserLocation){
        NSLog("heading is %@",userLocation.heading)
    }

    //处理位置坐标更新
    func didUpdateBMKUserLocation(userLocation: BMKUserLocation){
        NSLog("didUpdateUserLocation lat %f,long %f",userLocation.location.coordinate.latitude,userLocation.location.coordinate.longitude)
        locService.stopUserLocationService()
        searchlist.count = 0
        searchlist.areas.removeAllObjects()
        for var i = 0; i < LibraryAPI.sharedInstance.list.areas.count; i++ {
            var area = LibraryAPI.sharedInstance.list.areas[i] as! AreaModel
            var searchArea = AreaModel()
            for var j = 0; j < area.sites.count; j++ {
                var bikeStie = area.sites[j] as! BikeStie
                bikeStie.distance = BMKMetersBetweenMapPoints(BMKMapPointForCoordinate(bikeStie.pt), BMKMapPointForCoordinate(userLocation.location.coordinate))
                if(BMKCircleContainsCoordinate(bikeStie.pt, userLocation.location.coordinate, 1000)){
                    println(bikeStie.netName + "\(bikeStie.pt.latitude) \(userLocation.location.coordinate.latitude) $$ \(bikeStie.pt.longitude) \(userLocation.location.coordinate.longitude) \(bikeStie.distance)")
                    searchArea.sites.addObject(bikeStie)
                }
            }
            //排序
            for var i = 0; i < searchArea.sites.count; i++ {
                for var j = i + 1; j < searchArea.sites.count; j++ {
                    var a = searchArea.sites[j] as! BikeStie
                    var b = searchArea.sites[i] as! BikeStie
                    if a.distance < b.distance {
                        var temp: AnyObject = searchArea.sites[j]
                        searchArea.sites[j] = searchArea.sites[i]
                        searchArea.sites[i] = temp
                    }
                }
            }
            
            if(searchArea.sites.count > 0){
                searchArea.name = area.name
                searchArea.url = area.url
                searchArea.count = searchArea.sites.count
                searchArea.page = searchArea.sites.count / 20 + 1
                searchlist.areas.addObject(searchArea)
                searchlist.count = searchlist.count + searchArea.count
//                println(searchArea.name + ":\(userLocation.title)\(userLocation.location)搜索完成，总数\(searchArea.sites.count)")
            }
        }
        self.tableView.reloadData()
    }
    //;
    
    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
        locService.delegate = self // 此处记得不用的时候需要置nil，否则影响内存的释放
        locService.startUserLocationService()
    }
    
    override func viewWillDisappear(animated: Bool) {
        super.viewWillDisappear(animated)
        locService.delegate = nil // 不用时，置nil
        locService.stopUserLocationService()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view data source

    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        // #warning Potentially incomplete method implementation.
        // Return the number of sections.
        return searchlist.areas.count
    }

    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete method implementation.
        // Return the number of rows in the section.
        if(searchlist.areas.count > 0) {
            var area = searchlist.areas[section] as! AreaModel
            return area.sites.count
        }
        return 0
    }

    //自定义section标题
    override func tableView(tableView: UITableView, viewForHeaderInSection section: Int) -> UIView?{
        var height = CGFloat(22)
        var area = searchlist.areas[section] as! AreaModel
        return mMake.sectionHeadView(area.name + ":\(area.count)", height: height)
    }
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("cell", forIndexPath: indexPath) as! UITableViewCell
        var area = searchlist.areas[indexPath.section] as! AreaModel
        var bikeStie = area.sites[indexPath.row] as! BikeStie
        var distance = Int(bikeStie.distance)
        cell.textLabel?.text = "\(bikeStie.id + 1) " + bikeStie.netName + " 距离\(distance)米"
        return cell
    }
    
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath){
        //消除cell选择痕迹
        tableView.deselectRowAtIndexPath(indexPath, animated: true)
        var area = searchlist.areas[indexPath.section] as! AreaModel
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
    
    /*
    // Override to support conditional editing of the table view.
    override func tableView(tableView: UITableView, canEditRowAtIndexPath indexPath: NSIndexPath) -> Bool {
        // Return NO if you do not want the specified item to be editable.
        return true
    }
    */

    /*
    // Override to support editing the table view.
    override func tableView(tableView: UITableView, commitEditingStyle editingStyle: UITableViewCellEditingStyle, forRowAtIndexPath indexPath: NSIndexPath) {
        if editingStyle == .Delete {
            // Delete the row from the data source
            tableView.deleteRowsAtIndexPaths([indexPath], withRowAnimation: .Fade)
        } else if editingStyle == .Insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }    
    }
    */

    /*
    // Override to support rearranging the table view.
    override func tableView(tableView: UITableView, moveRowAtIndexPath fromIndexPath: NSIndexPath, toIndexPath: NSIndexPath) {

    }
    */

    /*
    // Override to support conditional rearranging of the table view.
    override func tableView(tableView: UITableView, canMoveRowAtIndexPath indexPath: NSIndexPath) -> Bool {
        // Return NO if you do not want the item to be re-orderable.
        return true
    }
    */

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using [segue destinationViewController].
        // Pass the selected object to the new view controller.
    }
    */

}
