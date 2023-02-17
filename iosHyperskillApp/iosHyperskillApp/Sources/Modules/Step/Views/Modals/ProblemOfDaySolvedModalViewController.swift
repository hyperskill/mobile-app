import PanModal
import SnapKit
import UIKit

extension ProblemOfDaySolvedModalViewController {
    struct Appearance {
        let backgroundColor = UIColor.systemBackground

        let contentStackViewSpacing: CGFloat = 32
        let contentStackViewInsets = LayoutInsets(
            horizontal: LayoutInsets.defaultInset,
            vertical: LayoutInsets.largeInset
        )

        let bookImageViewSizeRatio = CGSize(width: 0.63, height: 0.22)

        let gemsBadgeImageWidthHeight: CGFloat = 36
        let gemsBadgeSpacing: CGFloat = LayoutInsets.smallInset

        let goBackButtonHeight: CGFloat = 44
    }
}

final class ProblemOfDaySolvedModalViewController: PanModalPresentableViewController {
    private(set) var appearance = Appearance()

    private lazy var contentStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .vertical
        stackView.spacing = appearance.contentStackViewSpacing
        stackView.alignment = .leading
        stackView.distribution = .fill
        return stackView
    }()

    private let earnedGemsText: String

    weak var delegate: ProblemOfDaySolvedModalViewControllerDelegate?

    override var shortFormHeight: PanModalHeight {
        let contentStackViewSize = contentStackView.systemLayoutSizeFitting(UIView.layoutFittingCompressedSize)
        let height = appearance.contentStackViewInsets.top + contentStackViewSize.height
        return .contentHeight(height)
    }

    override var longFormHeight: PanModalHeight { shortFormHeight }

    init(earnedGemsText: String, delegate: ProblemOfDaySolvedModalViewControllerDelegate? = nil) {
        self.earnedGemsText = earnedGemsText
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
        delegate?.problemOfDaySolvedModalViewControllerDidAppear(self)
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        delegate?.problemOfDaySolvedModalViewControllerDidDisappear(self)
    }

    // MARK: Private API

    private func setup() {
        view.backgroundColor = appearance.backgroundColor

        setupContentStackView()
        setupBookImageView()
        setupTitleView()
        setupTextView()
        setupGemsView()
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

    private func setupBookImageView() {
        let image = UIImage(named: Images.StepQuiz.ProblemOfDaySolvedModal.book)?.withRenderingMode(.alwaysOriginal)
        let imageView = UIImageView(image: image)

        contentStackView.addArrangedSubview(imageView)

        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.snp.makeConstraints { make in
            make.width.equalTo(view).multipliedBy(appearance.bookImageViewSizeRatio.width)
            make.height.equalTo(view).multipliedBy(appearance.bookImageViewSizeRatio.height)
        }
    }

    private func setupTitleView() {
        let label = UILabel()
        label.text = Strings.Step.ProblemOfDaySolvedModal.title
        label.font = .preferredFont(forTextStyle: .largeTitle, compatibleWith: .init(legibilityWeight: .bold))
        label.textColor = .primaryText
        label.lineBreakMode = .byWordWrapping
        label.numberOfLines = 0

        contentStackView.addArrangedSubview(label)
    }

    private func setupTextView() {
        let label = UILabel()
        label.text = Strings.Step.ProblemOfDaySolvedModal.text
        label.font = .preferredFont(forTextStyle: .headline)
        label.textColor = .primaryText
        label.lineBreakMode = .byWordWrapping
        label.numberOfLines = 0

        contentStackView.addArrangedSubview(label)
    }

    private func setupGemsView() {
        let containerStackView = UIStackView()
        containerStackView.axis = .horizontal
        containerStackView.spacing = appearance.gemsBadgeSpacing

        contentStackView.addArrangedSubview(containerStackView)

        let image = UIImage(
            named: Images.StepQuiz.ProblemOfDaySolvedModal.gemsBadge
        )?.withRenderingMode(.alwaysOriginal)
        let imageView = UIImageView(image: image)

        containerStackView.addArrangedSubview(imageView)

        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.snp.makeConstraints { make in
            make.height.equalTo(appearance.gemsBadgeImageWidthHeight)
            make.width.equalTo(appearance.gemsBadgeImageWidthHeight)
        }

        let label = UILabel()
        label.text = earnedGemsText
        label.font = .preferredFont(forTextStyle: .title3)
        label.textColor = .primaryText
        containerStackView.addArrangedSubview(label)
    }

    private func setupGoBackButton() {
        let button = UIKitRoundedRectangleButton(style: .violet)
        button.setTitle(Strings.General.goToHomescreen, for: .normal)
        button.addTarget(self, action: #selector(goBackButtonTapped), for: .touchUpInside)

        contentStackView.addArrangedSubview(button)

        button.translatesAutoresizingMaskIntoConstraints = false
        button.snp.makeConstraints { make in
            make.width.equalToSuperview()
            make.height.equalTo(appearance.goBackButtonHeight)
        }
    }

    @objc
    private func goBackButtonTapped() {
        delegate?.problemOfDaySolvedModalViewControllerDidTapGoToHomescreenButton(self)
    }
}
