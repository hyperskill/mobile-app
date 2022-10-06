import SwiftUI
import UIKit

final class StepHostingController: UIHostingController<StepView> {
    override func viewDidLoad() {
        super.viewDidLoad()
        navigationController?.removeBackButtonTitleForTopController()
    }
}
