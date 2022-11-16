import shared
import SnapKit
import SwiftUI
import UIKit

protocol AppViewDelegate: AnyObject {
    func appView(
        _ view: AppView,
        didRequestAddPlaceholderHostingController hostingController: UIHostingController<PlaceholderView>
    )
    func appView(
        _ view: AppView,
        didConfigurePlaceholderHostingController hostingController: UIHostingController<PlaceholderView>
    )
    func appViewPlaceholderActionButtonTapped(_ view: AppView)
}

extension AppView {
    struct Appearance {
        let backgroundColor = ColorPalette.surface
    }
}

final class AppView: UIView {
    let appearance: Appearance

    weak var delegate: AppViewDelegate?

    private lazy var loadingIndicator: UIActivityIndicatorView = {
        let loadingIndicatorView = UIActivityIndicatorView(style: .medium)
        loadingIndicatorView.hidesWhenStopped = true
        loadingIndicatorView.startAnimating()
        return loadingIndicatorView
    }()

    private weak var placeholderHostingController: UIHostingController<PlaceholderView>?

    init(frame: CGRect = .zero, appearance: Appearance = Appearance(), delegate: AppViewDelegate? = nil) {
        self.appearance = appearance
        self.delegate = delegate

        super.init(frame: frame)

        setupView()
        addSubviews()
        makeConstraints()

        setPlaceholderHidden(true)
    }

    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    func renderState(_ state: AppFeatureStateKs) {
        switch state {
        case .idle, .loading:
            loadingIndicator.isHidden = false
            loadingIndicator.startAnimating()

            setPlaceholderHidden(true)
        case .networkError:
            loadingIndicator.stopAnimating()
            setPlaceholderHidden(false)
        case .ready:
            loadingIndicator.stopAnimating()
            setPlaceholderHidden(true)
        }
    }

    private func setPlaceholderHidden(_ isHidden: Bool) {
        placeholderHostingController?.view.isHidden = isHidden
        placeholderHostingController?.view.alpha = isHidden ? 0 : 1
    }
}

extension AppView: ProgrammaticallyInitializableViewProtocol {
    func setupView() {
        backgroundColor = appearance.backgroundColor
    }

    func addSubviews() {
        addSubview(loadingIndicator)

        let placeholderView = PlaceholderView(
            configuration: .networkError { [weak self] in
                guard let self else {
                    return
                }

                self.delegate?.appViewPlaceholderActionButtonTapped(self)
            }
        )
        let placeholderHostingController = UIHostingController(rootView: placeholderView)
        delegate?.appView(self, didRequestAddPlaceholderHostingController: placeholderHostingController)
        addSubview(placeholderHostingController.view)
        self.placeholderHostingController = placeholderHostingController
    }

    func makeConstraints() {
        loadingIndicator.translatesAutoresizingMaskIntoConstraints = false
        loadingIndicator.snp.makeConstraints { make in
            make.center.equalToSuperview()
        }

        if let placeholderHostingController {
            placeholderHostingController.view.translatesAutoresizingMaskIntoConstraints = false
            placeholderHostingController.view.snp.makeConstraints { make in
                make.edges.equalToSuperview()
            }
            delegate?.appView(self, didConfigurePlaceholderHostingController: placeholderHostingController)
        }
    }
}

struct AppView_Previews: PreviewProvider {
    static var previews: some View {
        UIKitViewControllerPreview {
            AppAssembly().makeModule()
        }
    }
}
