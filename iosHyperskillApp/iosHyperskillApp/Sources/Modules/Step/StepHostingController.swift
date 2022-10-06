import SwiftUI
import UIKit

final class StepHostingController: UIHostingController<StepView> {
    override func viewDidLoad() {
        super.viewDidLoad()
        navigationController?.removeBackButtonTitleForTopController()
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)

        if isMovingFromParent {
            print("StepHostingController :: isMovingFromParent")
        }
    }
}
