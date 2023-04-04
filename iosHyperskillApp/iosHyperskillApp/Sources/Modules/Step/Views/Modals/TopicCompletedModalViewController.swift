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

        let actionButtonHeight: CGFloat = 44
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

    private let modalText: String

    private let isNextStepAvailable: Bool

    weak var delegate: TopicCompletedModalViewControllerDelegate?

    override var shortFormHeight: PanModalHeight {
        let contentStackViewSize = contentStackView.systemLayoutSizeFitting(UIView.layoutFittingCompressedSize)
        let height = appearance.contentStackViewInsets.top + contentStackViewSize.height
        return .contentHeight(height)
    }

    override var longFormHeight: PanModalHeight { shortFormHeight }

    init(modalText: String, isNextStepAvailable: Bool, delegate: TopicCompletedModalViewControllerDelegate? = nil) {
        self.modalText = modalText
        self.isNextStepAvailable = isNextStepAvailable
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
        delegate?.topicCompletedModalViewControllerDidAppear(self)
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        delegate?.topicCompletedModalViewControllerDidDisappear(self)
    }

    // MARK: Private API

    private func setup() {
        view.backgroundColor = appearance.backgroundColor

        setupContentStackView()
        setupBookImageView()
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
        textLabel.text = modalText
        textLabel.font = .preferredFont(forTextStyle: .headline)
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

        if isNextStepAvailable {
            let continueWithNextTopicButton = makeActionButton(
                style: .violet,
                title: Strings.StepQuiz.TopicCompletedModal.continueWithNextTopicButtonText,
                action: #selector(continueWithNextTopicButtonTapped)
            )

            containerStackView.addArrangedSubview(continueWithNextTopicButton)
            continueWithNextTopicButton.snp.makeConstraints { make in
                make.width.equalToSuperview()
            }
        }

        let goToHomescreenButton = makeActionButton(
            style: isNextStepAvailable ? .outline : .violet,
            title: Strings.General.goToHomescreen,
            action: #selector(goToHomescreenButtonTapped)
        )

        containerStackView.addArrangedSubview(goToHomescreenButton)
        goToHomescreenButton.snp.makeConstraints { make in
            make.width.equalToSuperview()
        }
    }

    private func makeActionButton(
        style: UIKitRoundedRectangleButton.Style,
        title: String,
        action: Selector
    ) -> UIKitRoundedRectangleButton {
        let button = UIKitRoundedRectangleButton(style: style)
        button.setTitle(title, for: .normal)
        button.addTarget(self, action: action, for: .touchUpInside)

        button.translatesAutoresizingMaskIntoConstraints = false
        button.snp.makeConstraints { make in
            make.height.equalTo(appearance.actionButtonHeight)
        }

        return button
    }

    @objc
    private func goToHomescreenButtonTapped() {
        delegate?.topicCompletedModalViewControllerDidTapGoToHomescreenButton(self)
    }

    @objc
    private func continueWithNextTopicButtonTapped() {
        delegate?.topicCompletedModalViewControllerDidTapContinueWithNextTopicButton(self)
    }
}
