import PanModal
import SnapKit
import UIKit

extension ProblemOfDaySolvedModalViewController {
    struct Appearance {
        let backgroundColor = UIColor.systemBackground

        let defaultPadding = LayoutInsets.largeInset
        let largePadding: CGFloat = 32

        let bookImageWidthMultiplier: CGFloat = 0.63
        let bookImageHeightMultiplier: CGFloat = 0.22

        let gemsBadgeImageWidthHeight: CGFloat = 36
        let gemsBadgeSpacing: CGFloat = LayoutInsets.smallInset

        let goBackButtonHeight: CGFloat = 44

        let contentHeight: CGFloat = 510
    }
}

final class ProblemOfDaySolvedModalViewController: PanModalPresentableViewController {
    private(set) var appearance = Appearance()

    private weak var contentStackView: UIStackView?

    private let gemsCount: Int
    private let onGoBackButtonTap: () -> Void

    override var shortFormHeight: PanModalHeight {
//        if let contentStackHeight = contentStackView?
//            .systemLayoutSizeFitting(UIView.layoutFittingCompressedSize)
//            .height {
//            return .contentHeight(contentStackHeight)
//        }
        return .contentHeight(appearance.contentHeight)
    }

    override var longFormHeight: PanModalHeight { shortFormHeight }

    init(gemsCount: Int, onGoBackButtonTap: @escaping () -> Void) {
        self.gemsCount = gemsCount
        self.onGoBackButtonTap = onGoBackButtonTap

        super.init()
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        setup()
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
        let contentStackView = UIStackView()
        contentStackView.axis = .vertical
        contentStackView.spacing = appearance.largePadding
        contentStackView.alignment = .leading

        contentStackView.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(contentStackView)

        contentStackView.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(appearance.defaultPadding)
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(appearance.defaultPadding)
            make.trailing.equalTo(view.safeAreaLayoutGuide).offset(-appearance.defaultPadding)
            make.bottom.lessThanOrEqualToSuperview().offset(-appearance.defaultPadding)
        }

        self.contentStackView = contentStackView
    }

    private func setupBookImageView() {
        let bookImageView = UIImageView(
            image: UIImage(
                named: Images.StepQuiz.ProblemOfDaySolvedModal.book
            )?.withRenderingMode(.alwaysOriginal)
        )

        bookImageView.translatesAutoresizingMaskIntoConstraints = false
        contentStackView?.addArrangedSubview(bookImageView)

        bookImageView.snp.makeConstraints { make in
            make.width.equalToSuperview().multipliedBy(appearance.bookImageWidthMultiplier)
            make.height.equalToSuperview().multipliedBy(appearance.bookImageHeightMultiplier)
        }
    }

    private func setupTitleView() {
        let titleView = UILabel()
        titleView.text = Strings.StepQuiz.ProblemOfDaySolvedModal.title
        titleView.font = .preferredFont(forTextStyle: .largeTitle, compatibleWith: .init(legibilityWeight: .bold))
        titleView.textColor = .primaryText
        titleView.lineBreakMode = .byWordWrapping
        titleView.numberOfLines = 0

        titleView.translatesAutoresizingMaskIntoConstraints = false
        contentStackView?.addArrangedSubview(titleView)
    }

    private func setupTextView() {
        let textView = UILabel()
        textView.text = Strings.StepQuiz.ProblemOfDaySolvedModal.text
        textView.font = .preferredFont(forTextStyle: .headline)
        textView.textColor = .primaryText

        textView.translatesAutoresizingMaskIntoConstraints = false
        contentStackView?.addArrangedSubview(textView)
    }

    private func setupGemsView() {
        let gemsView = UIStackView()
        gemsView.axis = .horizontal

        gemsView.addArrangedSubview(buildGemsBadge())

        let totalGemsLabelView = UILabel()
        totalGemsLabelView.font = .preferredFont(forTextStyle: .subheadline)
        totalGemsLabelView.textColor = .secondaryText
        totalGemsLabelView.translatesAutoresizingMaskIntoConstraints = false
        totalGemsLabelView.textAlignment = .right

        totalGemsLabelView.text = Strings.StepQuiz.ProblemOfDaySolvedModal.totalGems

        totalGemsLabelView.translatesAutoresizingMaskIntoConstraints = false
        gemsView.addArrangedSubview(totalGemsLabelView)

        gemsView.translatesAutoresizingMaskIntoConstraints = false
        contentStackView?.addArrangedSubview(gemsView)
    }

    private func buildGemsBadge() -> UIStackView {
        let gemsBadgeView = UIStackView()
        gemsBadgeView.axis = .horizontal
        gemsBadgeView.spacing = appearance.gemsBadgeSpacing
        gemsBadgeView.translatesAutoresizingMaskIntoConstraints = false

        let badgeImageView = UIImageView(
            image: UIImage(named: Images.StepQuiz.ProblemOfDaySolvedModal.gemsBadge)?
                .withRenderingMode(.alwaysOriginal)
        )

        badgeImageView.snp.makeConstraints { make in
            make.height.equalTo(appearance.gemsBadgeImageWidthHeight)
            make.width.equalTo(appearance.gemsBadgeImageWidthHeight)
        }
        badgeImageView.translatesAutoresizingMaskIntoConstraints = false
        gemsBadgeView.addArrangedSubview(badgeImageView)

        let gemsCountTextView = UILabel()
        gemsCountTextView.text = String(self.gemsCount)
        gemsCountTextView.font = .preferredFont(forTextStyle: .title3)
        gemsCountTextView.textColor = .primaryText

        gemsCountTextView.translatesAutoresizingMaskIntoConstraints = false
        gemsBadgeView.addArrangedSubview(gemsCountTextView)

        return gemsBadgeView
    }

    private func setupGoBackButton() {
        let goBackButton = UIKitRoundedRectangleButton(style: .violet)
        goBackButton.setTitle(Strings.StepQuiz.ProblemOfDaySolvedModal.goButtonText, for: .normal)

        goBackButton.addTarget(self, action: #selector(goBackButtonTapped), for: .touchUpInside)

        goBackButton.snp.makeConstraints { make in
            make.height.equalTo(appearance.goBackButtonHeight)
        }
        goBackButton.translatesAutoresizingMaskIntoConstraints = false
        contentStackView?.addArrangedSubview(goBackButton)
    }

    @objc
    private func goBackButtonTapped() {
        onGoBackButtonTap()
        dismiss(animated: true)
    }
}
