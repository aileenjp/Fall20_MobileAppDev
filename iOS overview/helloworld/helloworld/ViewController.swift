//
//  ViewController.swift
//  helloworld
//
//  Created by Aileen Pierce on 8/26/20.
//  Copyright © 2020 Aileen Pierce. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
    @IBOutlet weak var messageText: UILabel!
    @IBAction func buttonPressed(_ sender: UIButton) {
        messageText.text = "Hello World!"
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }


}

