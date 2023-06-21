import PanModal
import SnapKit
import SwiftUI
import UIKit

class PanModalSwiftUIViewController<Content: View>: PanModalPresentableViewController {
    @Binding private var isPresented: Bool

    private let content: () -> Content

    private lazy var scrollView = UIScrollView()

    override var panScrollable: UIScrollView? { self.scrollView }

    override var shortFormHeight: PanModalHeight {
        isShortFormEnabled ? .contentHeight(scrollView.contentSize.height) : super.shortFormHeight
    }

    init(isPresented: Binding<Bool>, content: @escaping () -> Content) {
        self._isPresented = isPresented
        self.content = content
        super.init()
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        view.backgroundColor = .systemBackground

        view.addSubview(scrollView)
        scrollView.translatesAutoresizingMaskIntoConstraints = false
        scrollView.snp.makeConstraints { make in
            make.top.bottom.equalToSuperview()
            make.leading.trailing.equalTo(view.safeAreaLayoutGuide)
            make.width.equalToSuperview()
        }

        let contentHostingController = UIHostingController(rootView: content())
        addChild(contentHostingController)
        scrollView.addSubview(contentHostingController.view)

        contentHostingController.view.translatesAutoresizingMaskIntoConstraints = false
        contentHostingController.view.snp.makeConstraints { make in
            make.edges.equalToSuperview()
            make.width.equalToSuperview()
        }
        contentHostingController.didMove(toParent: self)
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)

        DispatchQueue.main.async {
            self.panModalSetNeedsLayoutUpdate()
            self.panModalTransition(to: .shortForm)
        }
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        isPresented = false
    }
}
