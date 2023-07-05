import PanModal
import SnapKit
import UIKit

protocol ProblemsLimitReachedModalViewControllerDelegate: AnyObject {
    func problemsLimitReachedModalViewControllerDidTapGoToHomescreenButton(
        _ viewController: ProblemsLimitReachedModalViewController
    )

    func problemsLimitReachedModalViewControllerDidAppear(_ viewController: ProblemsLimitReachedModalViewController)

    func problemsLimitReachedModalViewControllerDidDisappear(_ viewController: ProblemsLimitReachedModalViewController)
}

extension ProblemsLimitReachedModalViewController {
    struct Appearance {
        let backgroundColor = UIColor.systemBackground

        let contentStackViewSpacing: CGFloat = 32
        let contentStackViewInsets = LayoutInsets(
            horizontal: LayoutInsets.defaultInset,
            vertical: LayoutInsets.largeInset
        )

        let cubeImageViewSizeRatio = CGSize(width: 1, height: 0.20)

        let goToHomescreenButtonHeight: CGFloat = 44
    }
}

final class ProblemsLimitReachedModalViewController: PanModalPresentableViewController {
    private(set) var appearance = Appearance()

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
        let height = appearance.contentStackViewInsets.top + contentStackViewSize.height
        return .contentHeight(height)
    }

    override var longFormHeight: PanModalHeight { shortFormHeight }

    private weak var delegate: ProblemsLimitReachedModalViewControllerDelegate?

    init(delegate: ProblemsLimitReachedModalViewControllerDelegate?) {
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
        setupGoToHomescreenBackButton()
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
        containerStackView.spacing = LayoutInsets.largeInset
        containerStackView.alignment = .leading
        containerStackView.distribution = .fill

        contentStackView.addArrangedSubview(containerStackView)

        let titleLabel = UILabel()
        titleLabel.text = Strings.StepQuiz.ProblemsLimitReachedModal.title
        titleLabel.font = .preferredFont(forTextStyle: .title2, compatibleWith: .init(legibilityWeight: .bold))
        titleLabel.textColor = .primaryText
        titleLabel.lineBreakMode = .byWordWrapping
        titleLabel.numberOfLines = 0

        containerStackView.addArrangedSubview(titleLabel)

        let textLabel = UILabel()
        textLabel.text = Strings.StepQuiz.ProblemsLimitReachedModal.description
        textLabel.font = .preferredFont(forTextStyle: .body)
        textLabel.textColor = .primaryText
        textLabel.lineBreakMode = .byWordWrapping
        textLabel.numberOfLines = 0

        containerStackView.addArrangedSubview(textLabel)
    }

    private func setupGoToHomescreenBackButton() {
        let button = UIKitRoundedRectangleButton(style: .violet)
        button.setTitle(Strings.Common.goToHomescreen, for: .normal)
        button.addTarget(self, action: #selector(goToHomescreenButtonTap), for: .touchUpInside)

        contentStackView.addArrangedSubview(button)

        button.translatesAutoresizingMaskIntoConstraints = false
        button.snp.makeConstraints { make in
            make.width.equalToSuperview()
            make.height.equalTo(appearance.goToHomescreenButtonHeight)
        }
    }

    @objc
    private func goToHomescreenButtonTap() {
        delegate?.problemsLimitReachedModalViewControllerDidTapGoToHomescreenButton(self)
    }
}
