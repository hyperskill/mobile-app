import SnapKit
import UIKit

protocol UIKitScrollableStackViewDelegate: AnyObject {
    func scrollableStackViewRefreshControlDidRefresh(_ scrollableStackView: UIKitScrollableStackView)
}

final class UIKitScrollableStackView: UIView {
    private let orientation: Orientation

    weak var delegate: UIKitScrollableStackViewDelegate?

    private lazy var stackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = orientation.stackViewOrientation
        return stackView
    }()

    private lazy var scrollView = UIScrollView()

    // MARK: - Refresh control

    var isRefreshControlEnabled = false {
        didSet {
            guard oldValue != isRefreshControlEnabled else {
                return
            }

            let refreshControl = isRefreshControlEnabled ? UIRefreshControl() : nil
            if let refreshControl {
                refreshControl.addTarget(
                    self,
                    action: #selector(onRefreshControlValueChanged),
                    for: .valueChanged
                )
            }

            scrollView.refreshControl = refreshControl
        }
    }

    private var refreshControl: UIRefreshControl? {
        scrollView.subviews.first(where: { $0 is UIRefreshControl }) as? UIRefreshControl
    }

    // MARK: - Blocks

    var arrangedSubviews: [UIView] {
        stackView.arrangedSubviews
    }

    // MARK: - Proxy properties

    var showsHorizontalScrollIndicator: Bool {
        get {
            scrollView.showsHorizontalScrollIndicator
        }
        set {
            scrollView.showsHorizontalScrollIndicator = newValue
        }
    }

    var showsVerticalScrollIndicator: Bool {
        get {
            scrollView.showsVerticalScrollIndicator
        }
        set {
            scrollView.showsVerticalScrollIndicator = newValue
        }
    }

    var spacing: CGFloat {
        get {
            stackView.spacing
        }
        set {
            stackView.spacing = newValue
        }
    }

    var contentInsetAdjustmentBehavior: UIScrollView.ContentInsetAdjustmentBehavior {
        get {
            scrollView.contentInsetAdjustmentBehavior
        }
        set {
            scrollView.contentInsetAdjustmentBehavior = newValue
        }
    }

    var scrollDelegate: UIScrollViewDelegate? {
        get {
            scrollView.delegate
        }
        set {
            scrollView.delegate = newValue
        }
    }

    var contentInsets: UIEdgeInsets {
        get {
            scrollView.contentInset
        }
        set {
            scrollView.contentInset = newValue
        }
    }

    var contentOffset: CGPoint {
        get {
            scrollView.contentOffset
        }
        set {
            scrollView.contentOffset = newValue
        }
    }

    var verticalScrollIndicatorInsets: UIEdgeInsets {
        get {
            scrollView.verticalScrollIndicatorInsets
        }
        set {
            scrollView.verticalScrollIndicatorInsets = newValue
        }
    }

    var horizontalScrollIndicatorInsets: UIEdgeInsets {
        get {
            scrollView.horizontalScrollIndicatorInsets
        }
        set {
            scrollView.horizontalScrollIndicatorInsets = newValue
        }
    }

    var automaticallyAdjustsScrollIndicatorInsets: Bool {
        get {
            scrollView.automaticallyAdjustsScrollIndicatorInsets
        }
        set {
            scrollView.automaticallyAdjustsScrollIndicatorInsets = newValue
        }
    }

    var shouldBounce: Bool {
        get {
            scrollView.bounces
        }
        set {
            scrollView.bounces = newValue
        }
    }

    var isPagingEnabled: Bool {
        get {
            scrollView.isPagingEnabled
        }
        set {
            scrollView.isPagingEnabled = newValue
        }
    }

    var isScrollEnabled: Bool {
        get {
            scrollView.isScrollEnabled
        }
        set {
            scrollView.isScrollEnabled = newValue
        }
    }

    var contentSize: CGSize {
        get {
            scrollView.contentSize
        }
        set {
            scrollView.contentSize = newValue
        }
    }

    // MARK: - Inits

    init(frame: CGRect = .zero, orientation: Orientation) {
        self.orientation = orientation
        super.init(frame: frame)

        self.setupView()
        self.addSubviews()
        self.makeConstraints()
    }

    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    // MARK: Public interface

    func addArrangedView(_ view: UIView) {
        stackView.addArrangedSubview(view)
    }

    func removeArrangedView(_ view: UIView) {
        for subview in stackView.subviews where subview == view {
            stackView.removeArrangedSubview(subview)
            subview.removeFromSuperview()
        }
    }

    func insertArrangedView(_ view: UIView, at index: Int) {
        stackView.insertArrangedSubview(view, at: index)
    }

    func removeAllArrangedViews() {
        for subview in stackView.subviews {
            removeArrangedView(subview)
        }
    }

    func startRefreshing() {
        refreshControl?.beginRefreshing()
    }

    func endRefreshing() {
        refreshControl?.endRefreshing()
    }

    func scrollTo(arrangedViewIndex: Int) {
        guard let targetFrame = arrangedSubviews[safe: arrangedViewIndex]?.frame else {
            return
        }

        scrollView.scrollRectToVisible(targetFrame, animated: true)
    }

    // MARK: - Private methods

    @objc
    private func onRefreshControlValueChanged() {
        delegate?.scrollableStackViewRefreshControlDidRefresh(self)
    }

    enum Orientation {
        case vertical
        case horizontal

        var stackViewOrientation: NSLayoutConstraint.Axis {
            switch self {
            case .vertical:
                NSLayoutConstraint.Axis.vertical
            case .horizontal:
                NSLayoutConstraint.Axis.horizontal
            }
        }
    }
}

extension UIKitScrollableStackView: ProgrammaticallyInitializableViewProtocol {
    func setupView() {
        stackView.clipsToBounds = false
        scrollView.clipsToBounds = false

        // For pull-to-refresh when contentSize is too small for scrolling
        if orientation == .horizontal {
            scrollView.alwaysBounceHorizontal = true
        } else {
            scrollView.alwaysBounceVertical = true
        }
        scrollView.bounces = true
    }

    func addSubviews() {
        addSubview(scrollView)
        scrollView.addSubview(stackView)
    }

    func makeConstraints() {
        scrollView.translatesAutoresizingMaskIntoConstraints = false
        scrollView.snp.makeConstraints { make in
            make.edges.equalToSuperview()
        }

        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.snp.makeConstraints { make in
            make.edges.equalToSuperview()

            if case .vertical = orientation {
                make.width.equalTo(scrollView.snp.width)
            } else {
                make.height.equalTo(scrollView.snp.height)
            }
        }
    }
}

// MARK: - PanModalScrollable -

protocol PanModalScrollable: AnyObject {
    var panScrollable: UIScrollView? { get }
}

extension UIKitScrollableStackView: PanModalScrollable {
    var panScrollable: UIScrollView? { scrollView }
}
