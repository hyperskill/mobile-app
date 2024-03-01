import PanModal
import shared
import SnapKit
import UIKit

protocol ProblemOfDaySolvedModalViewControllerDelegate: AnyObject {
    func problemOfDaySolvedModalViewControllerDidAppear(_ viewController: ProblemOfDaySolvedModalViewController)
    func problemOfDaySolvedModalViewControllerDidDisappear(_ viewController: ProblemOfDaySolvedModalViewController)

    func problemOfDaySolvedModalViewControllerDidTapGoBackButton(
        _ viewController: ProblemOfDaySolvedModalViewController
    )
    func problemOfDaySolvedModalViewControllerDidTapShareStreakButton(
        _ viewController: ProblemOfDaySolvedModalViewController,
        streak: Int
    )
}

extension ProblemOfDaySolvedModalViewController {
    struct Appearance {
        let backgroundColor = UIColor.systemBackground

        let contentStackViewSpacing: CGFloat = 32
        let contentStackViewInsets = LayoutInsets(
            horizontal: LayoutInsets.defaultInset,
            vertical: LayoutInsets.largeInset
        )

        let bookImageViewSizeRatio = CGSize(width: 0.63, height: 0.22)

        let detailItemsImageWidthHeight: CGFloat = 36
        let detailItemsSpacing: CGFloat = LayoutInsets.smallInset

        let buttonHeight: CGFloat = 44
    }
}

final class ProblemOfDaySolvedModalViewController: PanModalPresentableViewController {
    weak var delegate: ProblemOfDaySolvedModalViewControllerDelegate?

    private(set) var appearance = Appearance()

    private let earnedGemsText: String?
    private let shareStreakData: StepCompletionFeatureShareStreakDataKs

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

    init(
        earnedGemsText: String?,
        shareStreakData: StepCompletionFeatureShareStreakDataKs,
        delegate: ProblemOfDaySolvedModalViewControllerDelegate?
    ) {
        self.earnedGemsText = earnedGemsText
        self.shareStreakData = shareStreakData
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
        setupDetailItemsView()
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
        imageView.contentMode = .scaleAspectFit

        contentStackView.addArrangedSubview(imageView)

        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.snp.makeConstraints { make in
            make.width.equalTo(view).multipliedBy(appearance.bookImageViewSizeRatio.width)
            make.height.equalTo(view).multipliedBy(appearance.bookImageViewSizeRatio.height)
        }
    }

    private func setupTitleView() {
        let label = UILabel()
        label.text = Strings.StepQuiz.ProblemOfDaySolvedModal.title
        label.font = .preferredFont(for: .largeTitle, weight: .bold)
        label.textColor = .primaryText
        label.lineBreakMode = .byWordWrapping
        label.numberOfLines = 0

        contentStackView.addArrangedSubview(label)
    }

    private func setupTextView() {
        let label = UILabel()
        label.text = Strings.StepQuiz.ProblemOfDaySolvedModal.text
        label.font = .preferredFont(forTextStyle: .headline)
        label.textColor = .primaryText
        label.lineBreakMode = .byWordWrapping
        label.numberOfLines = 0

        contentStackView.addArrangedSubview(label)
    }

    private func setupDetailItemsView() {
        func makeItemView(imageResource: ImageResource, text: String) -> UIView {
            let containerStackView = UIStackView()
            containerStackView.axis = .horizontal
            containerStackView.spacing = appearance.detailItemsSpacing

            let image = UIImage(resource: imageResource).withRenderingMode(.alwaysOriginal)
            let imageView = UIImageView(image: image)
            imageView.contentMode = .scaleAspectFit

            containerStackView.addArrangedSubview(imageView)

            imageView.translatesAutoresizingMaskIntoConstraints = false
            imageView.snp.makeConstraints { make in
                make.height.equalTo(appearance.detailItemsImageWidthHeight)
                make.width.equalTo(appearance.detailItemsImageWidthHeight)
            }

            let label = UILabel()
            label.text = text
            label.font = .preferredFont(forTextStyle: .title3)
            label.textColor = .primaryText
            containerStackView.addArrangedSubview(label)

            return containerStackView
        }

        if earnedGemsText == nil,
           case .empty = shareStreakData {
            return
        }

        let itemsStackView = UIStackView()
        itemsStackView.axis = .vertical
        itemsStackView.spacing = LayoutInsets.defaultInset
        itemsStackView.alignment = .leading
        itemsStackView.distribution = .fill

        contentStackView.addArrangedSubview(itemsStackView)

        if let earnedGemsText {
            itemsStackView.addArrangedSubview(
                makeItemView(
                    imageResource: .problemOfDaySolvedModalGemsBadge,
                    text: earnedGemsText
                )
            )
        }

        switch shareStreakData {
        case .content(let data):
            itemsStackView.addArrangedSubview(
                makeItemView(
                    imageResource: .navigationBarStreakCompleted,
                    text: data.streakText
                )
            )
        case .empty:
            break
        }
    }

    private func setupActionButtons() {
        let stackView = UIStackView()
        stackView.axis = .vertical
        stackView.spacing = LayoutInsets.defaultInset
        stackView.alignment = .leading
        stackView.distribution = .fill

        contentStackView.addArrangedSubview(stackView)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.snp.makeConstraints { make in
            make.width.equalToSuperview()
        }

        let goBackButton = UIKitRoundedRectangleButton(style: .violet)
        goBackButton.setTitle(Strings.Common.goToTraining, for: .normal)
        goBackButton.addTarget(self, action: #selector(goBackButtonTapped), for: .touchUpInside)

        stackView.addArrangedSubview(goBackButton)
        goBackButton.translatesAutoresizingMaskIntoConstraints = false
        goBackButton.snp.makeConstraints { make in
            make.width.equalToSuperview()
            make.height.equalTo(appearance.buttonHeight)
        }

        switch shareStreakData {
        case .content:
            let shareStreakButton = UIKitRoundedRectangleButton(style: .outline)
            shareStreakButton.setTitle(Strings.StepQuiz.ProblemOfDaySolvedModal.shareStreakButton, for: .normal)
            shareStreakButton.addTarget(self, action: #selector(shareStreakButtonTapped), for: .touchUpInside)

            stackView.addArrangedSubview(shareStreakButton)
            shareStreakButton.translatesAutoresizingMaskIntoConstraints = false
            shareStreakButton.snp.makeConstraints { make in
                make.width.equalToSuperview()
                make.height.equalTo(appearance.buttonHeight)
            }
        case .empty:
            break
        }
    }

    @objc
    private func goBackButtonTapped() {
        delegate?.problemOfDaySolvedModalViewControllerDidTapGoBackButton(self)
        dismiss(animated: true)
    }

    @objc
    private func shareStreakButtonTapped() {
        switch shareStreakData {
        case .content(let data):
            delegate?.problemOfDaySolvedModalViewControllerDidTapShareStreakButton(self, streak: Int(data.streak))
        case .empty:
            assertionFailure("ProblemOfDaySolvedModalViewController: unexpected state")
        }
    }
}

@available(iOS 17, *)
#Preview {
    ProblemOfDaySolvedModalViewController(
        earnedGemsText: "+3 gems",
        shareStreakData: .content(
            StepCompletionFeatureShareStreakDataContent(
                streakText: "5 days streak",
                streak: 5
            )
        ),
        delegate: nil
    )
}

@available(iOS 17, *)
#Preview {
    ProblemOfDaySolvedModalViewController(
        earnedGemsText: "+3 gems",
        shareStreakData: .empty,
        delegate: nil
    )
}
