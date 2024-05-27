import SwiftUI

final class TopicCompletedModalHostingController: UIHostingController<TopicCompletedModalView> {
    override var supportedInterfaceOrientations: UIInterfaceOrientationMask { .portrait }

    override var shouldAutorotate: Bool { true }
}
