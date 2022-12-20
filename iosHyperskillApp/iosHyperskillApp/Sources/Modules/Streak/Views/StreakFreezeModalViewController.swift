import PanModal
import shared
import SnapKit
import UIKit

extension StreakFreezeModalViewController {
    struct Appearance {
        let backgroundColor = UIColor.systemBackground

        let contentStackViewSpacing: CGFloat = 32
        let contentStackViewInsets = LayoutInsets(
            horizontal: LayoutInsets.defaultInset,
            vertical: LayoutInsets.largeInset
        )

        let imageViewSizeRatio = CGSize(width: 0.36, height: 0.28)

        let statusBadgeImageWidthHeight: CGFloat = 36
        let statusBadgeSpacing: CGFloat = LayoutInsets.smallInset

        let actionButtonHeight: CGFloat = 44
    }
}

final class StreakFreezeModalViewController: PanModalPresentableViewController {
    private(set) var appearance = Appearance()

    private lazy var contentStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .vertical
        stackView.spacing = appearance.contentStackViewSpacing
        stackView.alignment = .leading
        stackView.distribution = .fill
        return stackView
    }()

    private let streakFreezeState: ProfileFeatureStreakFreezeStateKs
    private let onActionButtonTap: () -> Void

    override var shortFormHeight: PanModalHeight {
        let contentStackViewSize = contentStackView.systemLayoutSizeFitting(UIView.layoutFittingCompressedSize)
        let height = appearance.contentStackViewInsets.top + contentStackViewSize.height
        return .contentHeight(height)
    }

    override var longFormHeight: PanModalHeight { shortFormHeight }

    init(streakFreezeState: ProfileFeatureStreakFreezeStateKs, onActionButtonTap: @escaping () -> Void) {
        self.streakFreezeState = streakFreezeState
        self.onActionButtonTap = onActionButtonTap

        super.init()
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        setup()
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)

        DispatchQueue.main.async {
            self.panModalSetNeedsLayoutUpdate()
            self.panModalTransition(to: .shortForm)
        }
    }

    // MARK: Private API

    private func setup() {
        view.backgroundColor = appearance.backgroundColor

        setupContentStackView()
        setupImageView()
        setupTitleView()
        setupStatusStackView()
        setupTextView()
        setupGoBackButton()
    }

    private func setupContentStackView() {
        view.addSubview(contentStackView)

        contentStackView.translatesAutoresizingMaskIntoConstraints = false
        contentStackView.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(appearance.contentStackViewInsets.top)
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(appearance.contentStackViewInsets.leading)
            make.bottom.lessThanOrEqualToSuperview().offset(-appearance.contentStackViewInsets.bottom)
            make.trailing.equalTo(view.safeAreaLayoutGuide).offset(-appearance.contentStackViewInsets.trailing)
        }
    }

    private func setupImageView() {
        let image = UIImage(named: streakFreezeState.image)?.withRenderingMode(.alwaysOriginal)
        let imageView = UIImageView(image: image)

        contentStackView.addArrangedSubview(imageView)

        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.snp.makeConstraints { make in
            make.width.equalTo(view).multipliedBy(appearance.imageViewSizeRatio.width)
            make.height.equalTo(view).multipliedBy(appearance.imageViewSizeRatio.height)
        }
    }

    private func setupTitleView() {
        let label = UILabel()
        label.text = streakFreezeState.title
        label.font = .preferredFont(forTextStyle: .largeTitle, compatibleWith: .init(legibilityWeight: .bold))
        label.textColor = .primaryText
        label.lineBreakMode = .byWordWrapping
        label.numberOfLines = 0

        contentStackView.addArrangedSubview(label)
    }

    private func setupStatusStackView() {
        let containerStackView = UIStackView()
        containerStackView.axis = .horizontal
        containerStackView.distribution = .equalSpacing

        contentStackView.addArrangedSubview(containerStackView)
        containerStackView.translatesAutoresizingMaskIntoConstraints = false
        containerStackView.snp.makeConstraints { make in
            make.width.equalToSuperview()
        }

        containerStackView.addArrangedSubview(makeStatusBadgeView())

        let totalGemsLabel = UILabel()
        totalGemsLabel.font = .preferredFont(forTextStyle: .subheadline)
        totalGemsLabel.textColor = .secondaryText
        totalGemsLabel.text = streakFreezeState.statusText
        containerStackView.addArrangedSubview(totalGemsLabel)
    }

    private func makeStatusBadgeView() -> UIStackView {
        let containerStackView = UIStackView()
        containerStackView.axis = .horizontal
        containerStackView.spacing = appearance.statusBadgeSpacing

        let image = UIImage(
            named: streakFreezeState.statusIcon
        )?.withRenderingMode(.alwaysOriginal)
        let imageView = UIImageView(image: image)

        containerStackView.addArrangedSubview(imageView)

        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.snp.makeConstraints { make in
            make.height.equalTo(appearance.statusBadgeImageWidthHeight)
            make.width.equalTo(appearance.statusBadgeImageWidthHeight)
        }

        let label = UILabel()
        label.text = String(streakFreezeState.statusCount)
        label.font = .preferredFont(forTextStyle: .title3)
        label.textColor = .primaryText
        containerStackView.addArrangedSubview(label)

        return containerStackView
    }

    private func setupTextView() {
        let label = UILabel()
        label.text = Strings.Streak.FreezeModal.text
        label.font = .preferredFont(forTextStyle: .headline)
        label.textColor = .primaryText
        label.lineBreakMode = .byWordWrapping
        label.numberOfLines = 0

        contentStackView.addArrangedSubview(label)
    }

    private func setupGoBackButton() {
        let button = UIKitRoundedRectangleButton(style: .violet)
        button.setTitle(streakFreezeState.buttonText, for: .normal)
        button.addTarget(self, action: #selector(goBackButtonTapped), for: .touchUpInside)

        contentStackView.addArrangedSubview(button)

        button.translatesAutoresizingMaskIntoConstraints = false
        button.snp.makeConstraints { make in
            make.width.equalToSuperview()
            make.height.equalTo(appearance.actionButtonHeight)
        }
    }

    @objc
    private func goBackButtonTapped() {
        onActionButtonTap()
        dismiss(animated: true)
    }
}

fileprivate extension ProfileFeatureStreakFreezeStateKs {
    var image: String {
        switch self {
        case .canBuy, .alreadyHave:
            return Images.Profile.Streak.FreezeModal.snowflake
        case .notEnoughGems:
            return Images.Profile.Streak.FreezeModal.diamond
        }
    }

    var title: String {
        switch self {
        case .canBuy:
            return Strings.Streak.FreezeModal.canBuyTitle
        case .alreadyHave:
            return Strings.Streak.FreezeModal.alreadyHaveTitle
        case .notEnoughGems:
            return Strings.Streak.FreezeModal.notEnoughGemsTitle
        }
    }

    var statusIcon: String {
        switch self {
        case .canBuy:
            return Images.StepQuiz.ProblemOfDaySolvedModal.gemsBadge
        case .alreadyHave:
            return Images.Profile.Streak.FreezeModal.snowflakeBadge
        case .notEnoughGems:
            return Images.Profile.Streak.FreezeModal.gemsBadgeLocked
        }
    }

    var statusCount: String {
        switch self {
        case .canBuy(let stateCanBuy):
            return String(stateCanBuy.price)
        case .notEnoughGems(let stateNotEnoughGems):
            return String(stateNotEnoughGems.price)
        case .alreadyHave:
            return "1"
        }
    }

    var statusText: String {
        switch self {
        case .canBuy, .notEnoughGems:
            return Strings.Streak.FreezeModal.oneDayStreakFreeze
        case .alreadyHave:
            return Strings.Streak.FreezeModal.youHaveOneDayStreakFreeze
        }
    }

    var buttonText: String {
        switch self {
        case .canBuy(let stateCanBuy):
            return AppGraphBridge.sharedAppGraph.commonComponent.resourceProvider.getString(
                stringResource: Strings.Streak.FreezeModal.getItForGemsResource,
                args: KotlinArray(size: 1, init: { _ in NSNumber(value: stateCanBuy.price) })
            )
        case .notEnoughGems, .alreadyHave:
            return Strings.Streak.FreezeModal.continueLearning
        }
    }
}
