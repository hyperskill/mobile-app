import SnapKit
import SwiftUI
import UIKit

class PanModalSwiftUIViewController<Content: View>: PanModalPresentableViewController {
    @Binding private var isPresented: Bool

    private let content: () -> Content

    init(isPresented: Binding<Bool>, content: @escaping () -> Content) {
        self._isPresented = isPresented
        self.content = content
        super.init()
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        self.view.backgroundColor = .systemBackground

        let contentHostingController = UIHostingController(rootView: self.content())
        self.addChild(contentHostingController)
        self.view.addSubview(contentHostingController.view)

        contentHostingController.view.translatesAutoresizingMaskIntoConstraints = false
        contentHostingController.view.snp.makeConstraints { make in
            make.top.equalToSuperview()
            make.bottom.lessThanOrEqualToSuperview()
            make.leading.trailing.equalTo(self.view.safeAreaLayoutGuide)
        }

        contentHostingController.didMove(toParent: self)
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        self.isPresented = false
    }
}
