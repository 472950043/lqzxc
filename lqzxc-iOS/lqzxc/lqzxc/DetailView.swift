//
//  DetailView.swift
//  lqzxc
//
//  Created by jyb on 15/7/29.
//  Copyright (c) 2015年 jyb. All rights reserved.
//

import UIKit

class DetailView: UIView {

    var alertLevelWindow: UIWindow!
    @IBOutlet var bg: UIButton!
    @IBOutlet var alertview: UIView!
    @IBOutlet var title: UILabel!
    
    @IBOutlet var label1: UILabel!
    @IBOutlet var label2: UILabel!
    @IBOutlet var label3: UILabel!
    @IBOutlet var label4: UILabel!
    @IBOutlet var label5: UILabel!
    @IBOutlet var label6: UILabel!
    
    @IBOutlet var img1: UIImageView!
    @IBOutlet var img2: UIImageView!
    
    @IBOutlet var btn1: UIButton!
    @IBOutlet var btn2: UIButton!
    
    @IBOutlet var hline: UIView!
    @IBOutlet var vline: UIView!
    //xib初始化时调用
    required init(coder decoder: NSCoder){
        super.init(coder: decoder)
        //        fatalError("init(coder:) has not been implemented")
    }
    
    //类方法（静态方法）加载xib
    class func instanceGridView() -> DetailView{
        // type method implementation goes here
        var nibView = NSBundle.mainBundle().loadNibNamed("DetailView", owner: nil, options: nil) as [AnyObject]
        return nibView[0] as! DetailView
    }
    
    //普通方法，设置属性
    func initStringView(text: String, text1: String, text2: String, text3: String, text4: String, text5: String, text6: String){
        getWindow()
        self.title.text = text
        self.label1.text = text1
        self.label2.text = text2
        self.label3.text = text3
        self.label4.text = text4
        self.label5.text = text5
        self.label6.text = text6
        self.img1.hidden = true
        self.img2.hidden = true
        overwriteLine()
    }
    
    func initImgView(text: String, text1: String, text3: String, text5: String, text6: String){
        getWindow()
        self.title.text = text
        self.label1.text = text1
        self.label2.hidden = true
        self.label3.text = text3
        self.label4.hidden = true
        self.label5.text = text5
        self.label6.text = text6
        overwriteLine()
    }
    
    func getWindow(){
        alertLevelWindow = UIWindow(frame: UIScreen.mainScreen().bounds)
        alertLevelWindow.windowLevel = UIWindowLevelAlert
        alertLevelWindow.hidden = false
        alertLevelWindow.backgroundColor = UIColor.clearColor()
        alertLevelWindow.addSubview(self)
        alertLevelWindow.makeKeyAndVisible()
//        println(alertLevelWindow)
    }
    
    var borderColor = UIColor(red: 197.0/255, green: 197.0/255, blue: 197.0/255, alpha: 1)
    func overwriteLine(){
        self.frame = UIScreen.mainScreen().bounds
        self.alertview.layer.cornerRadius = 5
        self.btn1.setBackgroundImage(createImageWithColor(borderColor), forState: UIControlState.Highlighted)
        self.btn2.setBackgroundImage(createImageWithColor(borderColor), forState: UIControlState.Highlighted)
        self.hline.backgroundColor = UIColor.whiteColor()
        self.vline.backgroundColor = UIColor.whiteColor()
        var view1 = UIView(frame:CGRectMake(0, 0, hline.frame.width, 0.5))
        view1.layer.borderWidth = 0.5
        view1.layer.borderColor = borderColor.CGColor
        self.hline.addSubview(view1)
        var view2 = UIView(frame:CGRectMake(0, 0, 0.5, vline.frame.height))
        view2.layer.borderWidth = 0.5
        view2.layer.borderColor = borderColor.CGColor
        self.vline.addSubview(view2)
    }
    
    func createImageWithColor(color: UIColor) -> UIImage {
        var rect = CGRectMake(0.0, 0.0, 1.0, 1.0)
        UIGraphicsBeginImageContext(rect.size)
        var context = UIGraphicsGetCurrentContext()
        CGContextSetFillColorWithColor(context, color.CGColor)
        CGContextFillRect(context, rect)
        var theImage = UIGraphicsGetImageFromCurrentImageContext()
        return theImage
    }

    func show(){
        self.bg.alpha = 0
        self.alertview.alpha = 0
        self.alertview.transform = CGAffineTransformScale(CGAffineTransformIdentity, 1.3, 1.3)
        UIView.animateWithDuration(0.2, animations: { () -> Void in
            self.bg.alpha = 0.4
            self.alertview.alpha = 1
            self.alertview.transform = CGAffineTransformScale(CGAffineTransformIdentity, 1, 1)
        })
    }
    
    func hide(){
        UIView.animateWithDuration(0.2, animations: { () -> Void in
            self.bg.alpha = 0
            self.alertview.alpha = 0
            }, completion: { (Bool) -> Void in
                self.removeFromSuperview()
        })
    }
    
    @IBAction func btnCanel(sender: AnyObject) {
        hide()
    }
    @IBAction func cancel(sender: AnyObject) {
//        hide()
    }
    
    //text: String, text: String  img: String
    /*
    // Only override drawRect: if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func drawRect(rect: CGRect) {
        // Drawing code
    }
    */

}
