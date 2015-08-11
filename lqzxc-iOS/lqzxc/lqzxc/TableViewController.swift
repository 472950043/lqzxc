////
////  TableViewController.swift
////  lqzxc
////
////  Created by jyb on 15/7/25.
////  Copyright (c) 2015年 jyb. All rights reserved.
////
//
//import UIKit
//
//class TableViewController: UITableViewController {
//
//    var width = UIScreen.mainScreen().applicationFrame.size.width
//    var borderColor = UIColor(red: 197.0/255, green: 197.0/255, blue: 197.0/255, alpha: 1)
//    var textBlue = UIColor(red: 0, green: 124.0/255, blue: 207.0/255, alpha: 1)
//    var textRed = UIColor(red: 1, green: 0, blue: 0, alpha: 1)
//    var headView: UIView!
//    var isLoading = false
//    
//    override func viewDidLoad() {
//        super.viewDidLoad()
//        //添加刷新
//        var refreshControl = UIRefreshControl()
//        refreshControl.addTarget(self, action: "refresh", forControlEvents: UIControlEvents.ValueChanged)
//        self.refreshControl = refreshControl
//        
//        headView = UIView(frame:CGRectMake(0, 0, width, 0.5))
//        headView.layer.borderWidth = 0.5
//        headView.layer.borderColor = borderColor.CGColor
//        self.tableView.addSubview(headView)
//        
//        //尾部
//        var footerView = UIView(frame:CGRectMake(0, -0.5, width, 0.5))
//        footerView.layer.borderWidth = 0.5
//        footerView.layer.borderColor = borderColor.CGColor
//        self.tableView.tableFooterView = footerView
//        
//        refresh()
//    }
//    
//    // 下拉刷新
//    func refresh(){
////        isLoading = true
////        self.refreshControl?.beginRefreshing()
////        if(LibraryAPI.sharedInstance.list.count > 0){
////            self.refreshControl?.attributedTitle = NSAttributedString(string: "刷新中...")
////        } else {
////            headView.hidden = true
////        }
////        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), {
////            var msg = LibraryAPI.sharedInstance.download("椒江")
////            dispatch_async(dispatch_get_main_queue(), {
////                self.isLoading = false
////                self.refreshControl?.endRefreshing()
////                var dateFormatter = NSDateFormatter()
////                //dateFormatter.timeZone = NSTimeZone(forSecondsFromGMT: 8)
////                dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
////                self.refreshControl?.attributedTitle = NSAttributedString(string: dateFormatter.stringFromDate(NSDate()))//"下拉刷新"
////                if(msg == "成功"){
////                    self.tableView.reloadData()
////                    self.headView.hidden = false
////                } else if(msg == "网络连接失败"){
////                    NSThread.sleepForTimeInterval(0.1)
////                    self.refresh()
////                }
////            })
////        })
//    }
//
//    override func didReceiveMemoryWarning() {
//        super.didReceiveMemoryWarning()
//        // Dispose of any resources that can be recreated.
//    }
//
//    // MARK: - Table view data source
//
//    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
//        // #warning Potentially incomplete method implementation.
//        // Return the number of sections.
//        return 1
//    }
//
//    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
//        // #warning Incomplete method implementation.
//        // Return the number of rows in the section.
//        return LibraryAPI.sharedInstance.list.count
//    }
//
////    //定义cell高度
////    override func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat{
////        return 60
////    }
//
//    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
//        let cell = tableView.dequeueReusableCellWithIdentifier("cell", forIndexPath: indexPath) as! UITableViewCell
////        var text1 = cell.viewWithTag(1) as! UILabel
//        var mBikeStie = LibraryAPI.sharedInstance.list.bikeSties[indexPath.row] as! BikeStie
//        cell.textLabel?.text = "\(mBikeStie.id + 1) " + mBikeStie.netName
////        text2.text = "库存:\(mBikeStie.bicycleNum) 可还:\(mBikeStie.bicycleCapacity - mBikeStie.bicycleNum)"
//        return cell
//    }
//    
//    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath){
//        //消除cell选择痕迹
//        tableView.deselectRowAtIndexPath(indexPath, animated: true)
//    }
//
//    /*
//    // Override to support conditional editing of the table view.
//    override func tableView(tableView: UITableView, canEditRowAtIndexPath indexPath: NSIndexPath) -> Bool {
//        // Return NO if you do not want the specified item to be editable.
//        return true
//    }
//    */
//
//    /*
//    // Override to support editing the table view.
//    override func tableView(tableView: UITableView, commitEditingStyle editingStyle: UITableViewCellEditingStyle, forRowAtIndexPath indexPath: NSIndexPath) {
//        if editingStyle == .Delete {
//            // Delete the row from the data source
//            tableView.deleteRowsAtIndexPaths([indexPath], withRowAnimation: .Fade)
//        } else if editingStyle == .Insert {
//            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
//        }    
//    }
//    */
//
//    /*
//    // Override to support rearranging the table view.
//    override func tableView(tableView: UITableView, moveRowAtIndexPath fromIndexPath: NSIndexPath, toIndexPath: NSIndexPath) {
//
//    }
//    */
//
//    /*
//    // Override to support conditional rearranging of the table view.
//    override func tableView(tableView: UITableView, canMoveRowAtIndexPath indexPath: NSIndexPath) -> Bool {
//        // Return NO if you do not want the item to be re-orderable.
//        return true
//    }
//    */
//
//    /*
//    // MARK: - Navigation
//
//    // In a storyboard-based application, you will often want to do a little preparation before navigation
//    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
//        // Get the new view controller using [segue destinationViewController].
//        // Pass the selected object to the new view controller.
//    }
//    */
//
//}
