//
//  TableViewController.swift
//  lqzxc
//
//  Created by jyb on 15/7/25.
//  Copyright (c) 2015年 jyb. All rights reserved.
//

import UIKit

class TableViewController: UITableViewController {

    var width = UIScreen.mainScreen().applicationFrame.size.width
    var borderColor = UIColor(red: 197.0/255, green: 197.0/255, blue: 197.0/255, alpha: 1)
    var headView: UIView!
//    var indicatorFoot: UIActivityIndicatorView!
//    var textlabelFoot: UILabel!
    var isLoading = false
//    var page = 0
    
    override func viewDidLoad() {
        super.viewDidLoad()
        //添加刷新
        var refreshControl = UIRefreshControl()
        refreshControl.addTarget(self, action: "refresh", forControlEvents: UIControlEvents.ValueChanged)
        self.refreshControl = refreshControl
        
        headView = UIView(frame:CGRectMake(0, 0, width, 0.5))
        headView.layer.borderWidth = 0.5
        headView.layer.borderColor = borderColor.CGColor
        self.tableView.addSubview(headView)
        
        //尾部
        var footerView = UIView(frame:CGRectMake(0, -0.5, width, 0.5))
        footerView.layer.borderWidth = 0.5
        footerView.layer.borderColor = borderColor.CGColor
        self.tableView.tableFooterView = footerView
        
//        //尾部
//        var footerView = UIView(frame:CGRectMake(0, 0, width, 0))
//        indicatorFoot = UIActivityIndicatorView(frame:CGRectMake(0, 0, width, 20))
//        indicatorFoot.activityIndicatorViewStyle = UIActivityIndicatorViewStyle.Gray
//        //indicator.hidesWhenStopped = false
//        footerView.addSubview(indicatorFoot)
//        textlabelFoot = UILabel(frame:CGRectMake(0, 20, width, 20))
//        textlabelFoot.font = UIFont.systemFontOfSize(12)
//        textlabelFoot.text = ""//上拉加载更多...
//        textlabelFoot.backgroundColor = UIColor.clearColor()
//        textlabelFoot.textAlignment = NSTextAlignment.Center
//        footerView.addSubview(textlabelFoot)
//        self.tableView.tableFooterView = footerView

    }
    
//    override func scrollViewDidScroll(scrollView: UIScrollView){
//        //        println("\(scrollView.frame.size.height) \(scrollView.contentOffset.y) \(scrollView.contentSize.height) \(scrollView.contentInset.top) \(isLoading) \(self.tableView.contentInset.bottom)")
//        if(!isLoading && scrollView.frame.size.height + scrollView.contentOffset.y >= scrollView.contentSize.height){
//            if(LibraryAPI.sharedInstance.list.count > page){
//                textlabelFoot.text = "加载中..."
//                indicatorFoot.startAnimating()
//                println("调用上拉刷新方法")
//                loadMore()
//            } else {
//                if(LibraryAPI.sharedInstance.list.count == 0 && scrollView.contentOffset.y >= 0){
//                    textlabelFoot.text = "暂无应聘消息"//已加载全部
//                    //                    println("已加载全部")
//                } else {
//                    textlabelFoot.text = ""//已加载全部
//                    indicatorFoot.stopAnimating()
//                }
//            }
//        }
//    }
//    
//    override func scrollViewWillEndDragging(scrollView: UIScrollView, withVelocity velocity: CGPoint, targetContentOffset: UnsafeMutablePointer<CGPoint>){
//        if(!isLoading && scrollView.frame.size.height + scrollView.contentOffset.y > scrollView.contentSize.height + 40){
//            if(LibraryAPI.sharedInstance.list.count > page){
//                textlabelFoot.text = "加载中..."
//                indicatorFoot.startAnimating()
//                println("调用上拉刷新方法")
//                loadMore()
//            }
//        }
//    }
    
    // 下拉刷新
    func refresh(){
        isLoading = true
        self.refreshControl?.beginRefreshing()
        if(LibraryAPI.sharedInstance.list.count > 0){
            self.refreshControl?.attributedTitle = NSAttributedString(string: "刷新中...")
        } else {
            headView.hidden = true
        }
//        textlabelFoot.text = ""
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), {
//            self.page = 1
            var msg = LibraryAPI.sharedInstance.download()
            dispatch_async(dispatch_get_main_queue(), {
                self.isLoading = false
                self.refreshControl?.endRefreshing()
                self.refreshControl?.attributedTitle = NSAttributedString(string: "下拉刷新")
                if(msg == "成功"){
//                    self.textlabelFoot.text = ""//已加载全部
                    self.tableView.reloadData()
                    self.headView.hidden = false
                } else if(msg == "网络连接失败"){
                    NSThread.sleepForTimeInterval(0.1)
                    self.refresh()
                }
            })
        })
    }

//    // 加载更多
//    func loadMore(){
//        UIView.beginAnimations(nil, context: nil)
//        UIView.setAnimationDuration(0.5)
//        self.tableView.contentInset.bottom = 40 + 49
//        UIView.commitAnimations()
//        isLoading = true
//        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), {
//            NSThread.sleepForTimeInterval(0.6)
//
//            dispatch_async(dispatch_get_main_queue(), {
//                self.page = self.page + 1
//                self.candidateListModel.count = data?.count
//                var insertIndexPaths = NSMutableArray(capacity: data!.resume.count)
//                for resume in data!.resume{
//                    var newPath = NSIndexPath(forRow: self.candidateListModel.resume.count, inSection: 0)
//                    self.candidateListModel.resume.addObject(resume)
//                    insertIndexPaths.addObject(newPath)
//                }
//                self.tableView.insertRowsAtIndexPaths(insertIndexPaths as [AnyObject], withRowAnimation: UITableViewRowAnimation.Automatic)//.Fade
//                UIView.beginAnimations(nil, context: nil)
//                UIView.setAnimationDuration(0.3)
//                self.tableView.contentInset.bottom = 49
//                if(msg == "成功"){
//                    self.tableView.contentOffset.y = self.tableView.contentOffset.y + 40
//                }
//                UIView.commitAnimations()
//                self.isLoading = false
//            })
//        })
//    }


    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view data source

    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        // #warning Potentially incomplete method implementation.
        // Return the number of sections.
        return 1
    }

    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete method implementation.
        // Return the number of rows in the section.
        return LibraryAPI.sharedInstance.list.count
    }


    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("cell", forIndexPath: indexPath) as! UITableViewCell
        var text = cell.viewWithTag(1) as! UILabel
        var mBikeStie = LibraryAPI.sharedInstance.list.bikeSties[indexPath.row] as! BikeStie
        text.text = "\(mBikeStie.id + 1) " + mBikeStie.netName


        return cell
    }
    
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath){
        //消除cell选择痕迹
        tableView.deselectRowAtIndexPath(indexPath, animated: true)
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
