//
//  ViewController.swift
//  lqzxc
//
//  Created by jyb on 15/7/25.
//  Copyright (c) 2015年 jyb. All rights reserved.
//

import UIKit

class ViewController: UIViewController, BMKMapViewDelegate {

    @IBOutlet var mapview: BMKMapView!
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
        mapview.delegate = self; // 此处记得不用的时候需要置nil，否则影响内存的释放
    }
    
    override func viewWillDisappear(animated: Bool) {
        super.viewWillDisappear(animated)
        mapview = nil; // 不用时，置nil
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    
}

