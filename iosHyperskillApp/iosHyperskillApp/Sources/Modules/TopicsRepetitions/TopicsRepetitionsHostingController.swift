import SwiftUI
import UIKit

final class TopicsRepetitionsHostingController: UIHostingController<TopicsRepetitionsView> {
    override func viewDidLoad() {
        super.viewDidLoad()
        navigationController?.removeBackButtonTitleForTopController()
    }
}
