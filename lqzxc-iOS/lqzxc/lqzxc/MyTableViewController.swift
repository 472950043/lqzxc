//
//  MyTableViewController.swift
//  lqzxc
//
//  Created by jyb on 15/8/1.
//  Copyright (c) 2015年 jyb. All rights reserved.
//

import UIKit

class MyTableViewController: UITableViewController {

    @IBAction func btnClick(sender: AnyObject) {
//        var viewController = MapViewController(nibName: "MapViewController", bundle: NSBundle.mainBundle())
//        viewController.title = "地图"
//        viewController.hidesBottomBarWhenPushed = true
//        var customLeftBarButtonItem = UIBarButtonItem()
//        customLeftBarButtonItem.title = "返回"
//        self.navigationItem.backBarButtonItem = customLeftBarButtonItem
//        self.navigationController?.pushViewController(viewController, animated: true)
        
        
        var viewController = OfflineViewController(nibName: "OfflineViewController", bundle: NSBundle.mainBundle())
        self.navigationController?.presentViewController(viewController, animated: true, completion: { () -> Void in
            
        })
        //
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

//        NSRange range = [str rangeOfString:@"sta"];
        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
//        self.navigationItem.title = "我的收藏"
//        self.hidesBottomBarWhenPushed = true
//        locService.delegate = self; // 此处记得不用的时候需要置nil，否则影响内存的释放

    }
    
    override func viewWillDisappear(animated: Bool) {
        super.viewWillDisappear(animated)
//        self.navigationItem.title = "返回"
//        self.hidesBottomBarWhenPushed = false
//        locService.delegate = nil; // 不用时，置nil
    }
    
    // MARK: - Table view data source

    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        // #warning Potentially incomplete method implementation.
        // Return the number of sections.
        return 0
    }

    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete method implementation.
        // Return the number of rows in the section.
        return 0
    }

    /*
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("reuseIdentifier", forIndexPath: indexPath) as! UITableViewCell

        // Configure the cell...

        return cell
    }
    */

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
