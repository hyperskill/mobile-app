import PanModal
import SnapKit
import UIKit

extension ProblemOfDaySolvedModalViewController {
    struct Appearance {
        let backgroundColor = UIColor.systemBackground

        let defaultPadding = LayoutInsets.largeInset
        let largePadding: CGFloat = 32

        let bookImageWidth: CGFloat = 258
        let bookImageHeight: CGFloat = 156

        let gemsBadgeImageWidthHeight: CGFloat = 48
        let gemsBadgeSpacing: CGFloat = 6

        let goBackButtonHeight: CGFloat = 44

        let contentHeight: CGFloat = 510
    }
}

final class ProblemOfDaySolvedModalViewController: PanModalPresentableViewController {
    private(set) var appearance = Appearance()

    private weak var bookImageView: UIImageView?
    private weak var titleView: UILabel?
    private weak var textView: UILabel?
    private weak var gemsCountTextView: UILabel?
    private weak var gemsView: UIStackView?
    private weak var goBackButton: UIButton?

    private let gemsCount: Int
    private let onGoBackButtonTap: () -> Void

    override var shortFormHeight: PanModalHeight {
        .contentHeight(appearance.contentHeight)
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

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        gemsCountTextView?.text = String(self.gemsCount)
        goBackButton?.addTarget(self, action: #selector(goBackButtonTapped), for: .touchUpInside)
    }

    // MARK: Private API

    private func setup() {
        view.backgroundColor = appearance.backgroundColor

        setupBookImageView()
        setupTitleView()
        setupTextView()
        setupGemsView()
        setupGoBackButton()
    }

    private func setupBookImageView() {
        let bookImageView = UIImageView(
            image: UIImage(
                named: Images.ProblemOfDaySolvedModal.book
            )?.withRenderingMode(.alwaysOriginal)
        )

        view.addSubview(bookImageView)
        bookImageView.translatesAutoresizingMaskIntoConstraints = false

        bookImageView.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(appearance.defaultPadding)
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(appearance.defaultPadding)
            make.width.equalTo(appearance.bookImageWidth)
            make.height.equalTo(appearance.bookImageHeight)
        }

        self.bookImageView = bookImageView
    }

    private func setupTitleView() {
        let titleView = UILabel()
        titleView.text = Strings.ProblemOfDaySolvedModal.title
        titleView.font = .preferredFont(forTextStyle: .largeTitle, compatibleWith: .init(legibilityWeight: .bold))
        titleView.textColor = .primaryText
        titleView.numberOfLines = 2

        view.addSubview(titleView)
        titleView.translatesAutoresizingMaskIntoConstraints = false

        titleView.snp.makeConstraints { make in
            make.top.equalTo(bookImageView.require().snp.bottom).offset(appearance.largePadding)
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(appearance.defaultPadding)
            make.trailing.equalTo(view.safeAreaLayoutGuide).offset(-appearance.defaultPadding)
        }

        self.titleView = titleView
    }

    private func setupTextView() {
        let textView = UILabel()
        textView.text = Strings.ProblemOfDaySolvedModal.text
        textView.font = .preferredFont(forTextStyle: .headline)
        textView.textColor = .primaryText

        view.addSubview(textView)
        textView.translatesAutoresizingMaskIntoConstraints = false

        textView.snp.makeConstraints { make in
            make.top.equalTo(titleView.require().snp.bottom).offset(appearance.defaultPadding)
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(appearance.defaultPadding)
            make.trailing.equalTo(view.safeAreaLayoutGuide).offset(-appearance.defaultPadding)
        }

        self.textView = textView
    }

    private func setupGemsView() {
        let gemsView = UIStackView()
        gemsView.axis = .horizontal

        gemsView.addArrangedSubview(buildGemsBadge())

        let totalGemsLabelView = UILabel()
        totalGemsLabelView.text = Strings.ProblemOfDaySolvedModal.totalGems
        totalGemsLabelView.font = .preferredFont(forTextStyle: .subheadline)
        totalGemsLabelView.textColor = .secondaryText
        totalGemsLabelView.translatesAutoresizingMaskIntoConstraints = false
        totalGemsLabelView.textAlignment = .right
        gemsView.addArrangedSubview(totalGemsLabelView)

        view.addSubview(gemsView)
        gemsView.translatesAutoresizingMaskIntoConstraints = false

        gemsView.snp.makeConstraints { make in
            make.top.equalTo(textView.require().snp.bottom).offset(appearance.largePadding)
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(appearance.defaultPadding)
            make.trailing.equalTo(view.safeAreaLayoutGuide).offset(-appearance.defaultPadding)
        }

        self.gemsView = gemsView
    }

    private func buildGemsBadge() -> UIStackView {
        let gemsBadgeView = UIStackView()
        gemsBadgeView.axis = .horizontal
        gemsBadgeView.spacing = appearance.gemsBadgeSpacing

        let badgeImageView = UIImageView(
            image: UIImage(named: Images.ProblemOfDaySolvedModal.gemsBadge)?
                .withRenderingMode(.alwaysOriginal)
        )
        badgeImageView.translatesAutoresizingMaskIntoConstraints = false
        badgeImageView.snp.makeConstraints { make in
            make.height.equalTo(appearance.gemsBadgeImageWidthHeight)
            make.width.equalTo(appearance.gemsBadgeImageWidthHeight)
        }

        let gemsCountTextView = UILabel()
        gemsCountTextView.text = "300"
        gemsCountTextView.font = .preferredFont(forTextStyle: .title3)
        gemsCountTextView.textColor = .primaryText

        self.gemsCountTextView = gemsCountTextView

        gemsBadgeView.addArrangedSubview(badgeImageView)
        gemsBadgeView.addArrangedSubview(gemsCountTextView)

        return gemsBadgeView
    }

    private func setupGoBackButton() {
        let goBackButton = UIKitRoundedRectangleButton(style: .violet)

        view.addSubview(goBackButton)
        goBackButton.translatesAutoresizingMaskIntoConstraints = false
        goBackButton.snp.makeConstraints { make in
            make.top.equalTo(gemsView.require().snp.bottom).offset(appearance.largePadding)
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(appearance.defaultPadding)
            make.bottom.lessThanOrEqualToSuperview().offset(-appearance.defaultPadding)
            make.trailing.equalTo(view.safeAreaLayoutGuide).offset(-appearance.defaultPadding)
            make.height.equalTo(appearance.goBackButtonHeight)
        }

        goBackButton.setTitle(Strings.ProblemOfDaySolvedModal.goButtonText, for: .normal)

        self.goBackButton = goBackButton
    }

    @objc
    private func goBackButtonTapped() {
        onGoBackButtonTap()
    }
}
