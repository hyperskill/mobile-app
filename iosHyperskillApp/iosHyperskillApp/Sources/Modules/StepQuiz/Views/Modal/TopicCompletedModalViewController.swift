import PanModal
import SnapKit
import UIKit

extension TopicCompletedModalViewController {
    struct Appearance {
        let backgroundColor = UIColor.systemBackground

        let contentStackViewSpacing: CGFloat = 32
        let contentStackViewInsets = LayoutInsets(
            horizontal: LayoutInsets.defaultInset,
            vertical: LayoutInsets.largeInset
        )

        let bookImageViewSizeRatio = CGSize(width: 0.63, height: 0.22)

        let goToHomescreenButtonHeight: CGFloat = 44
    }
}

final class TopicCompletedModalViewController: PanModalPresentableViewController {
    private(set) var appearance = Appearance()

    private lazy var contentStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .vertical
        stackView.spacing = appearance.contentStackViewSpacing
        stackView.alignment = .leading
        stackView.distribution = .fill
        return stackView
    }()

    private let onGoToHomescreenButtonTap: () -> Void

    override var shortFormHeight: PanModalHeight {
        let contentStackViewSize = contentStackView.systemLayoutSizeFitting(UIView.layoutFittingCompressedSize)
        let height = appearance.contentStackViewInsets.top + contentStackViewSize.height
        return .contentHeight(height)
    }

    override var longFormHeight: PanModalHeight { shortFormHeight }

    init(onGoToHomescreenButtonTap: @escaping () -> Void) {
        self.onGoToHomescreenButtonTap = onGoToHomescreenButtonTap

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
        setupBookImageView()
        setupTitleWithTextView()
        setupGoToHomescreenButton()
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

    private func setupTitleWithTextView() {
        let containerStackView = UIStackView()
        containerStackView.axis = .vertical
        containerStackView.spacing = LayoutInsets.largeInset
        containerStackView.alignment = .leading
        containerStackView.distribution = .fill

        contentStackView.addArrangedSubview(containerStackView)

        let titleLabel = UILabel()
        titleLabel.text = Strings.General.goodJob
        titleLabel.font = .preferredFont(forTextStyle: .largeTitle, compatibleWith: .init(legibilityWeight: .bold))
        titleLabel.textColor = .primaryText
        titleLabel.lineBreakMode = .byWordWrapping
        titleLabel.numberOfLines = 0

        containerStackView.addArrangedSubview(titleLabel)

        let textLabel = UILabel()
        textLabel.text = Strings.StepQuiz.TopicCompletedModal.text
        textLabel.font = .preferredFont(forTextStyle: .headline)
        textLabel.textColor = .primaryText
        textLabel.lineBreakMode = .byWordWrapping
        textLabel.numberOfLines = 0

        containerStackView.addArrangedSubview(textLabel)
    }

    private func setupGoToHomescreenButton() {
        let button = UIKitRoundedRectangleButton(style: .violet)
        button.setTitle(Strings.General.goToHomescreen, for: .normal)
        button.addTarget(self, action: #selector(goToHomescreenButtonTapped), for: .touchUpInside)

        contentStackView.addArrangedSubview(button)

        button.translatesAutoresizingMaskIntoConstraints = false
        button.snp.makeConstraints { make in
            make.width.equalToSuperview()
            make.height.equalTo(appearance.goToHomescreenButtonHeight)
        }
    }

    @objc
    private func goToHomescreenButtonTapped() {
        onGoToHomescreenButtonTap()
        dismiss(animated: true)
    }
}
