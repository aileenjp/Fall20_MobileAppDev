//
//  ViewController.swift
//  daVinci_c
//
//  Created by Aileen Pierce on 9/1/20.
//  Copyright © 2020 Aileen Pierce. All rights reserved.
//

import UIKit

class ViewController: UIViewController {

    @IBOutlet weak var artImage: UIImageView!
    
    @IBAction func chooseArt(_ sender: UIButton) {
        if sender.tag == 1{
            artImage.image = UIImage(named: "MonaLisa")
        } else if sender.tag == 2{
            artImage.image = UIImage(named: "Virtruvian")
        }
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }


}

