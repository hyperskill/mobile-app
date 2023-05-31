// swiftlint:disable:next file_header
//import NukeUI
//import PanModal
//import shared
//import SnapKit
//import UIKit
//
//extension AuthNewUserTrackModalViewController {
//    struct Appearance {
//        let backgroundColor = UIColor.systemBackground
//
//        let contentStackViewSpacing = LayoutInsets.defaultInset
//        let contentStackViewInsets = LayoutInsets.large
//
//        let trackImageWidthHeight: CGFloat = 48
//
//        let smallContainerSpacing = LayoutInsets.smallInset
//
//        let starImageWidthHeight: CGFloat = 20
//
//        let startLearningButtonHeight: CGFloat = 44
//    }
//}
//
//final class AuthNewUserTrackModalViewController: PanModalPresentableViewController {
//    private(set) var appearance = Appearance()
//
//    private lazy var contentStackView: UIStackView = {
//        let stackView = UIStackView()
//        stackView.axis = .vertical
//        stackView.spacing = appearance.contentStackViewSpacing
//        stackView.alignment = .leading
//        stackView.distribution = .fill
//        return stackView
//    }()
//
//    private let track: PlaceholderNewUserViewData.Track
//    private let onStartLearningButtonTap: () -> Void
//
//    override var shortFormHeight: PanModalHeight {
//        let contentStackViewSize = contentStackView.systemLayoutSizeFitting(UIView.layoutFittingCompressedSize)
//        let height = appearance.contentStackViewInsets.top + contentStackViewSize.height
//        return .contentHeight(height)
//    }
//
//    override var longFormHeight: PanModalHeight { shortFormHeight }
//
//    init(track: PlaceholderNewUserViewData.Track, onStartLearningButtonTap: @escaping () -> Void) {
//        self.track = track
//        self.onStartLearningButtonTap = onStartLearningButtonTap
//
//        super.init()
//    }
//
//    override func viewDidLoad() {
//        super.viewDidLoad()
//        setup()
//    }
//
//    override func viewWillAppear(_ animated: Bool) {
//        super.viewWillAppear(animated)
//
//        DispatchQueue.main.async {
//            self.panModalSetNeedsLayoutUpdate()
//            self.panModalTransition(to: .shortForm)
//        }
//    }
//
//    // MARK: Private API
//
//    private func setup() {
//        view.backgroundColor = appearance.backgroundColor
//
//        setupContentStackView()
//        setupTrackImageAndRating()
//        setupTitleWithTimeToComplete()
//        setupDescription()
//        setupStartLearningButton()
//    }
//
//    private func setupContentStackView() {
//        view.addSubview(contentStackView)
//
//        contentStackView.translatesAutoresizingMaskIntoConstraints = false
//        contentStackView.snp.makeConstraints { make in
//            make.top.equalToSuperview().offset(appearance.contentStackViewInsets.top)
//            make.leading.equalTo(view.safeAreaLayoutGuide).offset(appearance.contentStackViewInsets.leading)
//            make.bottom.lessThanOrEqualToSuperview().offset(-appearance.contentStackViewInsets.bottom)
//            make.trailing.equalTo(view.safeAreaLayoutGuide).offset(-appearance.contentStackViewInsets.trailing)
//        }
//    }
//
//    private func setupTrackImageAndRating() {
//        let containerStackView = UIStackView()
//        containerStackView.axis = .horizontal
//        containerStackView.distribution = .equalSpacing
//        containerStackView.alignment = .center
//
//        contentStackView.addArrangedSubview(containerStackView)
//        containerStackView.translatesAutoresizingMaskIntoConstraints = false
//        containerStackView.snp.makeConstraints { make in
//            make.width.equalToSuperview()
//        }
//
//        let imageView = LazyImageView()
//        imageView.source = track.imageSource
//
//        containerStackView.addArrangedSubview(imageView)
//
//        imageView.translatesAutoresizingMaskIntoConstraints = false
//        imageView.snp.makeConstraints { make in
//            make.width.equalTo(appearance.trackImageWidthHeight)
//            make.height.equalTo(appearance.trackImageWidthHeight)
//        }
//
//        containerStackView.addArrangedSubview(makeTrackRatingView())
//    }
//
//    private func makeTrackRatingView() -> UIStackView {
//        let containerStackView = UIStackView()
//        containerStackView.axis = .horizontal
//        containerStackView.spacing = appearance.smallContainerSpacing
//
//        let image = UIImage(systemName: "star.fill")?.withRenderingMode(.alwaysTemplate)
//        let imageView = UIImageView(image: image)
//        imageView.tintColor = ColorPalette.overlayYellow
//
//        containerStackView.addArrangedSubview(imageView)
//
//        imageView.translatesAutoresizingMaskIntoConstraints = false
//        imageView.snp.makeConstraints { make in
//            make.height.equalTo(appearance.starImageWidthHeight)
//            make.width.equalTo(appearance.starImageWidthHeight)
//        }
//
//        let label = UILabel()
//        label.text = String(track.rating)
//        label.font = .preferredFont(forTextStyle: .subheadline)
//        label.textColor = .secondaryText
//        containerStackView.addArrangedSubview(label)
//
//        return containerStackView
//    }
//
//    private func setupTitleWithTimeToComplete() {
//        let containerStackView = UIStackView()
//        containerStackView.axis = .vertical
//        containerStackView.spacing = appearance.smallContainerSpacing
//
//        contentStackView.addArrangedSubview(containerStackView)
//        containerStackView.translatesAutoresizingMaskIntoConstraints = false
//        containerStackView.snp.makeConstraints { make in
//            make.width.equalToSuperview()
//        }
//
//        let title = UILabel()
//        title.text = String(track.title)
//        title.font = .preferredFont(forTextStyle: .title2, compatibleWith: .init(legibilityWeight: .bold))
//        title.textColor = .primaryText
//        containerStackView.addArrangedSubview(title)
//
//        let timeToComplete = UILabel()
//        timeToComplete.text = String(track.timeToComplete)
//        timeToComplete.font = .preferredFont(forTextStyle: .subheadline)
//        timeToComplete.textColor = .secondaryText
//        containerStackView.addArrangedSubview(timeToComplete)
//    }
//
//    private func setupDescription() {
//        let description = UILabel()
//        description.text = String(track.description_)
//        description.font = .preferredFont(forTextStyle: .body)
//        description.textColor = .primaryText
//        description.numberOfLines = 0
//        description.lineBreakMode = .byWordWrapping
//        contentStackView.addArrangedSubview(description)
//    }
//
//    private func setupStartLearningButton() {
//        let button = UIKitRoundedRectangleButton(style: .violet)
//        button.setTitle(Strings.Auth.NewUserPlaceholder.startLearningButton, for: .normal)
//        button.addTarget(self, action: #selector(startLearningButtonTapped), for: .touchUpInside)
//
//        contentStackView.addArrangedSubview(button)
//
//        button.translatesAutoresizingMaskIntoConstraints = false
//        button.snp.makeConstraints { make in
//            make.width.equalToSuperview()
//            make.height.equalTo(appearance.startLearningButtonHeight)
//        }
//    }
//
//    @objc
//    private func startLearningButtonTapped() {
//        onStartLearningButtonTap()
//    }
//}
