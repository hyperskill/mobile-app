import SwiftUI
import UIKit

final class RemoveBackButtonTitleHostingController<RootView: View>: UIHostingController<RootView> {
    override func viewDidLoad() {
        super.viewDidLoad()
        navigationController?.removeBackButtonTitleForTopController()
    }
}
