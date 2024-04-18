import shared
import SnapKit
import UIKit

extension ProblemsLimitInfoModalView {
    struct Appearance {
        let backgroundColor = UIColor.systemBackground

        let contentStackViewSpacing = LayoutInsets.defaultInset * 2
        let contentStackViewInsets = LayoutInsets.default

        let iconImageViewSizeRatio = CGSize(width: 1, height: 0.20)

        let actionButtonHeight: CGFloat = 44
    }
}

final class ProblemsLimitInfoModalView: UIView {
    let appearance: Appearance

    private lazy var contentStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .vertical
        stackView.spacing = appearance.contentStackViewSpacing
        stackView.alignment = .leading
        stackView.distribution = .fill
        return stackView
    }()

    private lazy var iconImageView: UIImageView = {
        let image = UIImage(named: Images.StepQuiz.ProblemsLimitReachedModal.icon)?.withRenderingMode(.alwaysOriginal)
        let imageView = UIImageView(image: image)
        imageView.contentMode = .scaleAspectFit
        return imageView
    }()

    private lazy var textContainerStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .vertical
        stackView.spacing = LayoutInsets.defaultInset
        stackView.alignment = .leading
        stackView.distribution = .fill
        return stackView
    }()

    private lazy var titleLabel: UILabel = {
        let label = UILabel()
        label.font = .preferredFont(for: .title2, weight: .bold)
        label.textColor = .primaryText
        label.lineBreakMode = .byWordWrapping
        label.numberOfLines = 0
        return label
    }()

    private lazy var descriptionLabel: UILabel = {
        let label = UILabel()
        label.font = .preferredFont(forTextStyle: .body)
        label.textColor = .primaryText
        label.lineBreakMode = .byWordWrapping
        label.numberOfLines = 0
        return label
    }()

    private lazy var actionButtonsStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .vertical
        stackView.spacing = LayoutInsets.defaultInset
        stackView.alignment = .leading
        stackView.distribution = .fill
        return stackView
    }()

    private lazy var unlockLimitsButton: UIKitRoundedRectangleButton = {
        let button = UIKitRoundedRectangleButton(style: .violet)
        button.isHidden = true
        button.addTarget(self, action: #selector(unlockLimitsButtonTap), for: .touchUpInside)
        return button
    }()

    private lazy var goBackButton: UIKitRoundedRectangleButton = {
        let button = UIKitRoundedRectangleButton()
        button.setTitle(Strings.Common.goToTraining, for: .normal)
        button.addTarget(self, action: #selector(goToHomescreenButtonTap), for: .touchUpInside)
        return button
    }()

    var onUnlockLimitsButtonTap: (() -> Void)?
    var onGoToHomescreenButtonTap: (() -> Void)?

    override var intrinsicContentSize: CGSize {
        let contentStackViewSize = contentStackView.systemLayoutSizeFitting(UIView.layoutFittingCompressedSize)

        let height = appearance.contentStackViewInsets.top
            + contentStackViewSize.height
            + appearance.contentStackViewInsets.bottom

        return CGSize(width: UIView.noIntrinsicMetric, height: height)
    }

    init(
        frame: CGRect = .zero,
        appearance: Appearance = Appearance()
    ) {
        self.appearance = appearance
        super.init(frame: frame)

        self.setupView()
        self.addSubviews()
        self.makeConstraints()
    }

    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    func renderState(_ state: ProblemsLimitInfoModalFeature.ViewState) {
        titleLabel.text = state.title
        descriptionLabel.text = state.description_

        if let unlockLimitsButtonText = state.unlockLimitsButtonText {
            unlockLimitsButton.setTitle(unlockLimitsButtonText, for: .normal)
            unlockLimitsButton.isHidden = false

            goBackButton.style = .outline
        } else {
            goBackButton.style = .violet
        }
    }

    @objc
    private func unlockLimitsButtonTap() {
        onUnlockLimitsButtonTap?()
    }

    @objc
    private func goToHomescreenButtonTap() {
        onGoToHomescreenButtonTap?()
    }
}

extension ProblemsLimitInfoModalView: ProgrammaticallyInitializableViewProtocol {
    func setupView() {
        backgroundColor = appearance.backgroundColor
    }

    func addSubviews() {
        addSubview(contentStackView)

        contentStackView.addArrangedSubview(iconImageView)
        contentStackView.addArrangedSubview(textContainerStackView)

        textContainerStackView.addArrangedSubview(titleLabel)
        textContainerStackView.addArrangedSubview(descriptionLabel)

        contentStackView.addArrangedSubview(actionButtonsStackView)

        actionButtonsStackView.addArrangedSubview(unlockLimitsButton)
        actionButtonsStackView.addArrangedSubview(goBackButton)
    }

    func makeConstraints() {
        contentStackView.translatesAutoresizingMaskIntoConstraints = false
        contentStackView.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(appearance.contentStackViewInsets.top)
            make.leading.equalTo(safeAreaLayoutGuide).offset(appearance.contentStackViewInsets.leading)
            make.bottom.lessThanOrEqualToSuperview().offset(-appearance.contentStackViewInsets.bottom)
            make.trailing.equalTo(safeAreaLayoutGuide).offset(-appearance.contentStackViewInsets.trailing)
        }

        iconImageView.translatesAutoresizingMaskIntoConstraints = false
        iconImageView.snp.makeConstraints { make in
            make.width.equalTo(self).multipliedBy(appearance.iconImageViewSizeRatio.width)
            make.height.equalTo(self).multipliedBy(appearance.iconImageViewSizeRatio.height)
        }

        actionButtonsStackView.translatesAutoresizingMaskIntoConstraints = false
        actionButtonsStackView.snp.makeConstraints { make in
            make.width.equalToSuperview()
        }

        unlockLimitsButton.translatesAutoresizingMaskIntoConstraints = false
        unlockLimitsButton.snp.makeConstraints { make in
            make.width.equalToSuperview()
            make.height.equalTo(appearance.actionButtonHeight)
        }

        goBackButton.translatesAutoresizingMaskIntoConstraints = false
        goBackButton.snp.makeConstraints { make in
            make.width.equalToSuperview()
            make.height.equalTo(appearance.actionButtonHeight)
        }
    }
}

#if DEBUG
@available(iOS 17, *)
#Preview {
    let view = ProblemsLimitInfoModalView()
    view.renderState(
        ProblemsLimitInfoModalFeature.ViewState(
            title: "Title text",
            description: "Description text",
            unlockLimitsButtonText: nil
        )
    )
    return view
}

@available(iOS 17, *)
#Preview {
    let view = ProblemsLimitInfoModalView()
    view.renderState(
        ProblemsLimitInfoModalFeature.ViewState(
            title: "Title text",
            description: "Description text",
            unlockLimitsButtonText: "Unlock limits button text"
        )
    )
    return view
}
#endif
