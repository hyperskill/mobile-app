import PanModal
import SnapKit
import UIKit

protocol ProblemsLimitReachedModalViewControllerDelegate: AnyObject {
    func problemsLimitReachedModalViewControllerDidTapGoToHomescreenButton(
        _ viewController: ProblemsLimitReachedModalViewController
    )
    func problemsLimitReachedModalViewControllerDidTapUnlockLimitsButton(
        _ viewController: ProblemsLimitReachedModalViewController
    )

    func problemsLimitReachedModalViewControllerDidAppear(_ viewController: ProblemsLimitReachedModalViewController)

    func problemsLimitReachedModalViewControllerDidDisappear(_ viewController: ProblemsLimitReachedModalViewController)
}

extension ProblemsLimitReachedModalViewController {
    struct Appearance {
        let backgroundColor = UIColor.systemBackground

        let contentStackViewSpacing = LayoutInsets.defaultInset * 2
        let contentStackViewInsets = LayoutInsets.default

        let cubeImageViewSizeRatio = CGSize(width: 1, height: 0.20)

        let actionButtonButtonHeight: CGFloat = 44
    }
}

final class ProblemsLimitReachedModalViewController: PanModalPresentableViewController {
    private(set) var appearance = Appearance()

    private let titleText: String
    private let descriptionText: String
    private let unlockLimitsButtonText: String?

    private lazy var contentStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .vertical
        stackView.spacing = appearance.contentStackViewSpacing
        stackView.alignment = .leading
        stackView.distribution = .fill
        return stackView
    }()

    override var shortFormHeight: PanModalHeight {
        let contentStackViewSize = contentStackView.systemLayoutSizeFitting(UIView.layoutFittingCompressedSize)
        let height = appearance.contentStackViewInsets.top
            + contentStackViewSize.height
            + appearance.contentStackViewInsets.bottom
        return .contentHeight(height)
    }

    override var longFormHeight: PanModalHeight { shortFormHeight }

    private weak var delegate: ProblemsLimitReachedModalViewControllerDelegate?

    init(
        titleText: String,
        descriptionText: String,
        unlockLimitsButtonText: String?,
        delegate: ProblemsLimitReachedModalViewControllerDelegate?
    ) {
        self.titleText = titleText
        self.descriptionText = descriptionText
        self.unlockLimitsButtonText = unlockLimitsButtonText
        self.delegate = delegate
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

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        delegate?.problemsLimitReachedModalViewControllerDidAppear(self)
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        delegate?.problemsLimitReachedModalViewControllerDidDisappear(self)
    }

    // MARK: Private API

    private func setup() {
        view.backgroundColor = appearance.backgroundColor

        setupContentStackView()
        setupCubeImageView()
        setupTitleWithTextView()
        setupActionButtons()
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

    private func setupCubeImageView() {
        let image = UIImage(named: Images.StepQuiz.ProblemsLimitReachedModal.icon)?.withRenderingMode(.alwaysOriginal)
        let imageView = UIImageView(image: image)
        imageView.contentMode = .scaleAspectFit

        contentStackView.addArrangedSubview(imageView)

        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.snp.makeConstraints { make in
            make.width.equalTo(view).multipliedBy(appearance.cubeImageViewSizeRatio.width)
            make.height.equalTo(view).multipliedBy(appearance.cubeImageViewSizeRatio.height)
        }
    }

    private func setupTitleWithTextView() {
        let containerStackView = UIStackView()
        containerStackView.axis = .vertical
        containerStackView.spacing = LayoutInsets.defaultInset
        containerStackView.alignment = .leading
        containerStackView.distribution = .fill

        contentStackView.addArrangedSubview(containerStackView)

        let titleLabel = UILabel()
        titleLabel.text = titleText
        titleLabel.font = .preferredFont(for: .title2, weight: .bold)
        titleLabel.textColor = .primaryText
        titleLabel.lineBreakMode = .byWordWrapping
        titleLabel.numberOfLines = 0

        containerStackView.addArrangedSubview(titleLabel)

        let textLabel = UILabel()
        textLabel.text = descriptionText
        textLabel.font = .preferredFont(forTextStyle: .body)
        textLabel.textColor = .primaryText
        textLabel.lineBreakMode = .byWordWrapping
        textLabel.numberOfLines = 0

        containerStackView.addArrangedSubview(textLabel)
    }

    private func setupActionButtons() {
        let containerStackView = UIStackView()
        containerStackView.axis = .vertical
        containerStackView.spacing = LayoutInsets.defaultInset
        containerStackView.alignment = .leading
        containerStackView.distribution = .fill

        contentStackView.addArrangedSubview(containerStackView)
        containerStackView.translatesAutoresizingMaskIntoConstraints = false
        containerStackView.snp.makeConstraints { make in
            make.width.equalToSuperview()
        }

        if let unlockLimitsButtonText {
            let unlockLimitsButton = UIKitRoundedRectangleButton(style: .violet)
            unlockLimitsButton.setTitle(unlockLimitsButtonText, for: .normal)
            unlockLimitsButton.addTarget(self, action: #selector(unlockLimitsButtonTap), for: .touchUpInside)

            containerStackView.addArrangedSubview(unlockLimitsButton)

            unlockLimitsButton.translatesAutoresizingMaskIntoConstraints = false
            unlockLimitsButton.snp.makeConstraints { make in
                make.width.equalToSuperview()
                make.height.equalTo(appearance.actionButtonButtonHeight)
            }
        }

        let goBackButton = UIKitRoundedRectangleButton(
            style: unlockLimitsButtonText == nil ? .violet : .outline
        )
        goBackButton.setTitle(Strings.Common.goToTraining, for: .normal)
        goBackButton.addTarget(self, action: #selector(goToHomescreenButtonTap), for: .touchUpInside)

        containerStackView.addArrangedSubview(goBackButton)

        goBackButton.translatesAutoresizingMaskIntoConstraints = false
        goBackButton.snp.makeConstraints { make in
            make.width.equalToSuperview()
            make.height.equalTo(appearance.actionButtonButtonHeight)
        }
    }

    @objc
    private func unlockLimitsButtonTap() {
        delegate?.problemsLimitReachedModalViewControllerDidTapUnlockLimitsButton(self)
    }

    @objc
    private func goToHomescreenButtonTap() {
        delegate?.problemsLimitReachedModalViewControllerDidTapGoToHomescreenButton(self)
    }
}

#if DEBUG
@available(iOS 17, *)
#Preview {
    ProblemsLimitReachedModalViewController(
        titleText: "Title text",
        descriptionText: "Description text",
        unlockLimitsButtonText: nil,
        delegate: nil
    )
}

@available(iOS 17, *)
#Preview {
    ProblemsLimitReachedModalViewController(
        titleText: "Title text",
        descriptionText: "Description text",
        unlockLimitsButtonText: "Unlock limits button text",
        delegate: nil
    )
}
#endif
