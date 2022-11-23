import SwiftUI
import UIKit

final class RemoveBackButtonTitleHostingController: UIHostingController<AnyView> {
    override func viewDidLoad() {
        super.viewDidLoad()
        navigationController?.removeBackButtonTitleForTopController()
    }
}
