//
//  OfflineViewController.swift
//  lqzxc
//
//  Created by jyb on 15/8/9.
//  Copyright (c) 2015年 jyb. All rights reserved.
//

import UIKit

class OfflineViewController: UIViewController, UITableViewDataSource, UITableViewDelegate, UIAlertViewDelegate, BMKOfflineMapDelegate {
    
    @IBOutlet var segment: UISegmentedControl!
    
    @IBOutlet var downloadView: UIView!
    @IBOutlet var downloadTableView: UITableView!
    @IBOutlet var btnView: UIView!
    @IBOutlet var 全部更新: UIButton!
    @IBOutlet var 全部下载: UIButton!
    @IBOutlet var 全部暂停: UIButton!
    @IBOutlet var shuoming: OfflineShuoMing!
    
    @IBOutlet var cityView: UIView!
    @IBOutlet var cityTableView: UITableView!
    
    var grayImage: UIImage!
    
    var offlineMap: BMKOfflineMap!
    var arraylocalDownLoadMapInfo: NSMutableArray! //本地下载的离线地图
    var arrayHotCityData: NSArray! //本地下载的离线地图
    var arrayOfflineCityData: NSMutableArray! //本地下载的离线地图
    
    //2.继承并重写用nibName初始化的init方法
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: NSBundle?){
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
    }
    
    //xib初始化时调用
    required init(coder decoder: NSCoder){
        super.init(coder: decoder)
        //fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        var width = UIScreen.mainScreen().applicationFrame.size.width
        var borderColor = UIColor(red: 197.0/255, green: 197.0/255, blue: 197.0/255, alpha: 1)
        //尾部
        var footerView = UIView(frame:CGRectMake(0, -0.5, width, 0.5))
        footerView.layer.borderWidth = 0.5
        footerView.layer.borderColor = borderColor.CGColor
        downloadTableView.tableFooterView = footerView
        cityTableView.tableFooterView = footerView
        
        grayImage = createImageWithColor(UIColor(red: 0, green: 0, blue: 0, alpha: 0.1))
        全部更新.setBackgroundImage(grayImage, forState: UIControlState.Highlighted)
        全部下载.setBackgroundImage(grayImage, forState: UIControlState.Highlighted)
        全部暂停.setBackgroundImage(grayImage, forState: UIControlState.Highlighted)
        shuoming.instanceOfflineShuoMing(width)
        
        var line1 = UIView(frame:CGRectMake(width / 3 - 1, 14, 0.5, 16))
        line1.backgroundColor = UIColor.whiteColor()
        btnView.addSubview(line1)
        
        var line2 = UIView(frame:CGRectMake(width / 3 * 2 - 1, 14, 0.5, 16))
        line2.backgroundColor = UIColor.whiteColor()
        btnView.addSubview(line2)
        
        
        //初始化离线地图服务
        offlineMap = BMKOfflineMap()
        //左 获取当前城市
        getDownLoad()
        //右 获取热门城市
        arrayHotCityData = offlineMap.getHotCityList()
        if(arrayHotCityData == nil){
            arrayHotCityData = NSArray()
        }
        //右 获取支持离线下载城市列表
        arrayOfflineCityData = NSMutableArray(array: offlineMap.getOfflineCityList())
        if(arrayOfflineCityData == nil){
            arrayOfflineCityData = NSMutableArray()
        }
        println(arrayHotCityData)
        

        downloadView.hidden = false
        cityView.hidden = true
//        // 左 获取各城市离线地图更新信息
//        getLocateCitys()
//        // 右 获取当前城市
//        getLoadCitys()
    }
    
    @IBAction func 返回(sender: AnyObject) {
        self.dismissViewControllerAnimated(true, completion: { () -> Void in
        })
    }

    @IBAction func switchView(sender: AnyObject) {
        var control = sender as! UISegmentedControl
        switch (control.selectedSegmentIndex) {
        case 0:
            downloadView.hidden = false
            cityView.hidden = true
            getDownLoad()
            downloadTableView.reloadData()
            println("downloadTableView.reloadData")

        case 1:
            downloadView.hidden = true
            cityView.hidden = false
            cityTableView.reloadData()
            println("cityTableView.reloadData")
            
        default:
            break;
        }
    }
    
    func getDownLoad(){
        var temp = offlineMap.getAllUpdateInfo()
        if(temp != nil && temp.count > 0){
            shuoming.hidden = true
            arraylocalDownLoadMapInfo = NSMutableArray(array: offlineMap.getAllUpdateInfo())//获取各城市离线地图更新信息
        } else {
            shuoming.hidden = false
        }
    }
    
    func onGetOfflineMapState(type: Int32, withState state: Int32) {
        if (Int(type) == TYPE_OFFLINE_UPDATE) {
            //id为state的城市正在下载或更新，start后会毁掉此类型
            var updateInfo = offlineMap.getUpdateInfo(state)
            NSLog("城市名：%@,下载比例:%d", updateInfo.cityName, updateInfo.ratio)
        }
        if (Int(type) == TYPE_OFFLINE_NEWVER) {
            //id为state的state城市有新版本,可调用update接口进行更新
            var updateInfo = offlineMap.getUpdateInfo(state)
            NSLog("是否有更新%d", updateInfo.update)
        }
        if (Int(type) == TYPE_OFFLINE_UNZIP) {
            //正在解压第state个离线包，导入时会回调此类型
        }
        if (Int(type) == TYPE_OFFLINE_ZIPCNT) {
            //检测到state个离线包，开始导入时会回调此类型
            NSLog("检测到%d个离线包", state)
            if(state == 0) {
                showImportMesg(state)
            }
        }
        if (Int(type) == TYPE_OFFLINE_ERRZIP) {
            //有state个错误包，导入完成后会回调此类型
            NSLog("有%d个离线包导入错误", state)
        }
        if (Int(type) == TYPE_OFFLINE_UNZIPFINISH) {
            NSLog("成功导入%d个离线包", state)
            //导入成功state个离线包，导入成功后会回调此类型
            showImportMesg(state)
        }
    }
    
    //导入提示框
    func showImportMesg(count: Int32){
        var showmeg = NSString(format: "成功导入离线地图包个数:%d", count)
        var myAlertView = UIAlertView(title: "导入离线地图", message: showmeg as String, delegate: self, cancelButtonTitle: "确定")
        myAlertView.show()
    }
    
    @IBAction func 全部(sender: AnyObject) {
        if(sender.tag == 1){

        } else if(sender.tag == 2){

        } else if(sender.tag == 3){

        }
    }
    
    func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        // #warning Potentially incomplete method implementation.
        // Return the number of sections.
        if(downloadTableView == tableView){
            return 1
        }
        return 3
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete method implementation.
        // Return the number of rows in the section.
        if(downloadTableView == tableView){
            return 0
        }
        if(section == 0){
            return 2
        } else if(section == 1){
            if(arrayHotCityData == nil){
                return 0
            }
            return arrayHotCityData.count
        } else{
            if(arrayOfflineCityData == nil){
                return 0
            }
            return arrayOfflineCityData.count
        }
//        return 0
    }
    
    //自定义section标题
    func tableView(tableView: UITableView, viewForHeaderInSection section: Int) -> UIView?{
        // 大小提示
        var width = tableView.frame.size.width
        var headerView = UIView(frame: CGRectMake(0, 0, width, 20))
        headerView.backgroundColor = UIColor(red: 190.0/255, green: 190.0/255, blue: 190.0/255, alpha: 1)
        
        var title = UILabel(frame: CGRectMake(12, 0, width - 12, 20))
        title.font = UIFont.systemFontOfSize(13)
        title.backgroundColor = UIColor.clearColor()
        title.textColor = UIColor(red: 248.0/255, green: 248.0/255, blue: 248.0/255, alpha: 1)
        //定义每个section的title
        if(tableView == downloadTableView) {
            if(section == 0){
                title.text = "正在下载"
            } else if(section == 1){
                title.text = "下载完成"
            }
        } else {
            if(section == 0){
                title.text = "当前城市"
            } else if(section == 1){
                title.text = "热门城市"
            } else if(section == 2){
                title.text = "按省份查找"
            }
        }
        headerView.addSubview(title)
        return headerView
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
//        if(resultTableView == tableView){
//            let cell = tableView.dequeueReusableCellWithIdentifier("cell", forIndexPath: indexPath) as! UITableViewCell
//            //        var text1 = cell.viewWithTag(1) as! UILabel
//            var area = LibraryAPI.sharedInstance.list.areas[indexPath.section] as! AreaModel
//            var bikeStie = area.sites[indexPath.row] as! BikeStie
//            cell.textLabel?.text = "\(bikeStie.id + 1) " + bikeStie.netName//bikeStie.mid +
//            //        text2.text = "库存:\(mBikeStie.bicycleNum) 可还:\(mBikeStie.bicycleCapacity - mBikeStie.bicycleNum)"
//            return cell
//        }

        if(tableView == downloadTableView) {

            return UITableViewCell()
        }
        var cell = (NSBundle.mainBundle().loadNibNamed("CityTableViewCell", owner: nil, options: nil) as [AnyObject])[0] as! CityTableViewCell
        var text1 = cell.viewWithTag(1) as! UILabel
        var view2 = cell.viewWithTag(2)
        var img3 = cell.viewWithTag(3) as! UIImageView
        var btn4 = cell.viewWithTag(4) as! UIButton
        var text5 = cell.viewWithTag(5) as! UILabel
        
        if(indexPath.section == 0){
            view2?.hidden = true
            if(indexPath.row == 0){
                text1.text = MapDefaults.sharedInstance.mapCity
                text5.text = getCitySize(MapDefaults.sharedInstance.mapCityid)
            } else if(indexPath.row == 1){
                text1.text = "全国概略地图包"
                text5.text = getCitySize(1)
            }
        } else if(indexPath.section == 1){
            view2?.hidden = true
            var item = arrayHotCityData.objectAtIndex(indexPath.row) as! BMKOLSearchRecord
            text1.text = item.cityName
            text5.text = getSizeText(Int(item.cityID)) + (getDataSizeString(Int(item.size)) as String)
        } else if(indexPath.section == 2){
            var item = arrayOfflineCityData.objectAtIndex(indexPath.row) as! BMKOLSearchRecord
            if(item.cityType != 1){
                view2?.hidden = true
                text1.text = item.cityName
                text5.text = getSizeText(Int(item.cityID)) + (getDataSizeString(Int(item.size)) as String)
            } else {
                btn4.hidden = true
                text5.hidden = true
                text1.text = item.cityName
                if(展开 && 选中行.row == indexPath.row){
                    img3.transform = CGAffineTransformMakeRotation(CGFloat(M_PI))
                }
            }
        }
//        text1.text = "\(indexPath.row)"

        return cell
    }
    
    var 展开 = false
    var 选中行 = NSIndexPath(forRow: 0, inSection: 0)
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath){
        //消除cell选择痕迹
        tableView.deselectRowAtIndexPath(indexPath, animated: true)
        if(tableView == downloadTableView) {
            

        } else {
            if(indexPath.section == 2){
                var item = arrayOfflineCityData.objectAtIndex(indexPath.row) as! BMKOLSearchRecord
                if(item.cityType == 1){
                    if(展开){
                        if(indexPath == 选中行){//点击这行是只收缩
                            展开 = false
                            cityTableView.reloadRowsAtIndexPaths([选中行], withRowAnimation: UITableViewRowAnimation.Automatic)
                            shrinkTableView(UITableViewRowAnimation.Fade)
                            选中行 = NSIndexPath(forRow: 0, inSection: 0)
                        } else {
                            var 上次选中 = 选中行
                            if(indexPath.row > 选中行.row){//如果点击的是展开的列表下方
                                //先根据“上次选中”收缩列表
                                var index = shrinkTableView(UITableViewRowAnimation.Fade)
                                //计算出收缩后的“选中行”
                                选中行 = NSIndexPath(forRow: indexPath.row - index, inSection: indexPath.section)
                                //再根据“选中行”展开列表
                                spreadTableView(UITableViewRowAnimation.Fade)
                            } else {//如果点击的是展开的列表上方
                                //先根据“上次选中”收缩列表
                                shrinkTableView(UITableViewRowAnimation.Fade)
                                //计算出收缩后的“选中行”
                                选中行 = NSIndexPath(forRow: indexPath.row, inSection: indexPath.section)
                                //再根据“选中行”展开列表
                                var index = spreadTableView(UITableViewRowAnimation.Fade)
                                //更新“上次选中”的位置
                                上次选中 = NSIndexPath(forRow: indexPath.row + index + 上次选中.row - indexPath.row, inSection: indexPath.section)
                            }
                            //更新“选中行”, “上次选中”的列表小图标
                            cityTableView.reloadRowsAtIndexPaths([选中行, 上次选中], withRowAnimation: UITableViewRowAnimation.Automatic)
                        }
                    } else {
                        展开 = true
                        选中行 = NSIndexPath(forRow: indexPath.row, inSection: indexPath.section)
                        cityTableView.reloadRowsAtIndexPaths([选中行], withRowAnimation: UITableViewRowAnimation.Automatic)
                        spreadTableView(UITableViewRowAnimation.Automatic)
                    }
                }
            }
        }
        // ?id=86
    }
    
    ///收缩列表
    func shrinkTableView(animation: UITableViewRowAnimation) -> Int {
        var childCities = (arrayOfflineCityData.objectAtIndex(选中行.row) as! BMKOLSearchRecord).childCities
        //先删除列表元素
        arrayOfflineCityData.removeObjectsAtIndexes(NSIndexSet(indexesInRange: NSMakeRange(选中行.row + 1, childCities.count)))
        //计算删除的位置列表
        var indexPaths = NSMutableArray()
        for i in 1...childCities.count{
            indexPaths.addObject(NSIndexPath(forRow: 选中行.row + i, inSection: 选中行.section))
        }
        //再执行删除动画
        cityTableView.deleteRowsAtIndexPaths(indexPaths as [AnyObject], withRowAnimation: animation)
        return childCities.count
    }
    
    ///根据选中行展开列表
    func spreadTableView(animation: UITableViewRowAnimation) -> Int {
        var childCities = (arrayOfflineCityData.objectAtIndex(选中行.row) as! BMKOLSearchRecord).childCities
        //先插入列表元素
        arrayOfflineCityData.insertObjects(childCities, atIndexes: NSIndexSet(indexesInRange: NSMakeRange(选中行.row + 1, childCities.count)))
        //计算插入的位置列表
        var indexPaths = NSMutableArray()
        for i in 1...childCities.count{
            indexPaths.addObject(NSIndexPath(forRow: 选中行.row + i, inSection: 选中行.section))
        }
        //再执行插入动画
        cityTableView.insertRowsAtIndexPaths(indexPaths as [AnyObject], withRowAnimation: animation)
        
        //展开完成定位动画
        var rect = cityTableView.rectForRowAtIndexPath(选中行)
        cityTableView.scrollToRowAtIndexPath(选中行, atScrollPosition: UITableViewScrollPosition.Top, animated: true)
        return childCities.count
    }
    
    
    
//    ///获取已下载城市列表，用于各城市离线地图更新信息
//    func getLoadCitys(){
//        
//    }
//    -(void)getLoadCitys{
//    _arraylocalDownLoadMapInfo = [[NSMutableArray arrayWithArray:[_offlineMap getAllUpdateInfo]] retain];
//    loadMapInfo = [[NSMutableArray arrayWithCapacity:5] retain];//正在下载
//    localMapInfo = [[NSMutableArray arrayWithCapacity:5] retain];//已下载
//    
//    // 下载状态, -1:未定义 1:正在下载　2:等待下载　3:已暂停　4:完成 5:校验失败 6:网络异常 7:读写异常 8:Wifi网络异常 9:未完成的离线包有更新包 10:已完成的离线包有更新包 11:没有完全下载完成的省份 12:该省份的所有城市都已经下载完成 13:该省份的部分城市需要更新
//    for(BMKOLUpdateElement* element in _arraylocalDownLoadMapInfo)
//    if (element.status == 4 || element.status == 12)//已下载
//    [localMapInfo addObject:element];
//    else//正在下载
//    [loadMapInfo addObject:element];
//    }
//    
//    ///获取当前城市和离线地图下载状态城市列表
//    -(void)getLocateCitys{
//    //重置省市展开收缩标记
//    start = -1;
//    end = -1;
//    //获取热门城市
//    _arrayHotCityData = [[_offlineMap getHotCityList]retain];
//    //获取支持离线下载城市列表
//    _arrayOfflineCityData = [[[[_offlineMap getOfflineCityList]retain]autorelease] mutableCopy];
//    //获取当前城市
//    NSInteger mapCityid = [[NSUserDefaults standardUserDefaults] integerForKey:@"mapCityid"];
//    for(BMKOLSearchRecord* obj in _arrayOfflineCityData){
//    if (obj.cityID == mapCityid) {
//    locateCity = obj;
//    break;
//    } else if (obj.cityType > 0) {
//    for(BMKOLSearchRecord* record in obj.childCities)
//    if (record.cityID == mapCityid) {
//    locateCity = record;
//    break;
//    }
//    }
//    }
//    }
    func getSizeText(cityID: Int) -> String {
        var status = cityLoadState(cityID)
        switch (status) {
        case -1:
            return "未定义"
        case 1:
            return "正在下载"
        case 2:
            return "等待下载"
        case 3:
            return "已暂停"
        case 4://完成
            return "已下载"
        case 5://校验失败
            return "失败"
        case 6://网络异常
            return "异常"
        case 7://读写异常
            return "异常"
        case 8://Wifi网络异常
            return "异常"
        case 9://未完成的离线包有更新包
            return "有更新"
        case 10://已完成的离线包有更新包
            return "有更新"
        case 11://没有完全下载完成的省份
            return ""
        case 12://该省份的所有城市都已经下载完成
            return ""
        case 13://该省份的部分城市需要更新
            return "有更新"
        default:
            return ""
        }
    }
    
    func getCitySize(cityID: Int) -> String {
        if(arrayOfflineCityData == nil){
            return ""
        }
        for obj in arrayOfflineCityData {
            var temp = obj as! BMKOLSearchRecord
            if (Int(temp.cityID) == cityID){
                return getDataSizeString(Int(temp.size)) as String
            } else if (temp.childCities != nil && temp.childCities.count > 0){
                for obj2 in temp.childCities {
                    var temp2 = obj2 as! BMKOLSearchRecord
                    if (Int(temp2.cityID) == cityID){
                        return getDataSizeString(Int(temp2.size)) as String
                    }
                }
            }
        }
        return ""
    }
    
    ///下载状态, -1:未定义 1:正在下载　2:等待下载　3:已暂停　4:完成 5:校验失败 6:网络异常 7:读写异常 8:Wifi网络异常 9:未完成的离线包有更新包 10:已完成的离线包有更新包 11:没有完全下载完成的省份 12:该省份的所有城市都已经下载完成 13:该省份的部分城市需要更新
    func cityLoadState(cityID: Int) -> Int {
        if(arraylocalDownLoadMapInfo == nil){
            return 0
        }
        for obj in arraylocalDownLoadMapInfo{
            var temp = obj as! BMKOLUpdateElement
            if (Int(temp.cityID) == cityID){
                return Int(temp.status)
            }
        }
        return -1
    }
    
    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
        offlineMap.delegate = self // 此处记得不用的时候需要置nil，否则影响内存的释放

    }
    
    override func viewWillDisappear(animated: Bool) {
        super.viewWillDisappear(animated)
        offlineMap.delegate = nil // 不用时，置nil

    }
    
    func createImageWithColor(color: UIColor) -> UIImage {
        var rect = CGRectMake(0.0, 0.0, 1.0, 1.0)
        UIGraphicsBeginImageContext(rect.size)
        var context = UIGraphicsGetCurrentContext()
        CGContextSetFillColorWithColor(context, color.CGColor)
        CGContextFillRect(context, rect)
        var theImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        return theImage
    }
    
    //#pragma mark 包大小转换工具类（将包大小转换成合适单位）
    func getDataSizeString(nSize: Int) -> NSString {
        var string: NSString!
        if (nSize < 1024) {
            string = NSString(format: "%dB", nSize)
        } else if (nSize < 1048576) {
            string = NSString(format: "%dK", nSize / 1024)
        } else if (nSize < 1073741824) {
            if ((nSize % 1048576) == 0 ) {
                string = NSString(format: "%dM", nSize / 1048576)
            } else {
                var decimal = 0 //小数
                var decimalStr: NSString!
                decimal = (nSize % 1048576)
                decimal = decimal / 1024
                if (decimal < 10) {
                    decimalStr = NSString(format: "%d", 0)
                } else if (decimal >= 10 && decimal < 100) {
                    var i = decimal / 10
                    if (i >= 5) {
                        decimalStr = NSString(format: "%d", 1)
                    } else {
                        decimalStr = NSString(format: "%d", 0)
                    }
                }
                else if (decimal >= 100 && decimal < 1024) {
                    var i = decimal / 100
                    if (i >= 5) {
                        decimal = i + 1
                        if (decimal >= 10) {
                            decimal = 9
                        }
                        decimalStr = NSString(format: "%d", decimal)
                    } else {
                        decimalStr = NSString(format: "%d", i)
                    }
                }
                if (decimalStr == nil || decimalStr.isEqualToString("")) {
                    string = NSString(format: "%dMss", nSize / 1048576)
                } else {
                    string = NSString(format: "%d.%@M", nSize / 1048576, decimalStr)
                }
            }
        } else {// >1G
            string = NSString(format: "%dG", nSize / 1073741824)
        }
        return string
    }
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
