import shared
import SnapKit
import UIKit

extension RequestReviewModalView {
    struct Appearance {
        let contentStackViewSpacing: CGFloat = LayoutInsets.defaultInset * 2
        let contentStackViewInsets = LayoutInsets.default

        let textContainerStackViewSpacing: CGFloat = LayoutInsets.defaultInset
        let buttonsContainerStackViewSpacing: CGFloat = LayoutInsets.defaultInset

        let titleLabelTextFont = UIFont.preferredFont(for: .largeTitle, weight: .bold)
        let titleLabelTextColor = UIColor.newPrimaryText

        let descriptionLabelTextFont = UIFont.preferredFont(forTextStyle: .headline)
        let descriptionLabelTextColor = UIColor.newPrimaryText

        let positiveButtonButtonHeight: CGFloat = 44
        let negativeButtonButtonHeight: CGFloat = 44

        let backgroundColor = UIColor.systemBackground
    }
}

final class RequestReviewModalView: UIView {
    let appearance: Appearance

    private lazy var contentStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .vertical
        stackView.spacing = appearance.contentStackViewSpacing
        stackView.alignment = .leading
        stackView.distribution = .fill
        return stackView
    }()

    private lazy var textContainerStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .vertical
        stackView.spacing = appearance.textContainerStackViewSpacing
        stackView.alignment = .leading
        stackView.distribution = .fill
        return stackView
    }()

    private lazy var buttonsContainerStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .vertical
        stackView.spacing = appearance.buttonsContainerStackViewSpacing
        stackView.alignment = .fill
        stackView.distribution = .fill
        return stackView
    }()

    private lazy var titleLabel: UILabel = {
        let label = UILabel()
        label.font = appearance.titleLabelTextFont
        label.textColor = appearance.titleLabelTextColor
        label.lineBreakMode = .byWordWrapping
        label.numberOfLines = 0
        return label
    }()

    private lazy var descriptionLabel: UILabel = {
        let label = UILabel()
        label.font = appearance.descriptionLabelTextFont
        label.textColor = appearance.descriptionLabelTextColor
        label.lineBreakMode = .byWordWrapping
        label.numberOfLines = 0
        label.isHidden = true
        return label
    }()

    private lazy var positiveButton: UIKitRoundedRectangleButton = {
        let button = UIKitRoundedRectangleButton()
        button.addTarget(self, action: #selector(positiveButtonTapped), for: .touchUpInside)
        return button
    }()

    private lazy var negativeButton: UIKitRoundedRectangleButton = {
        let button = UIKitRoundedRectangleButton(style: .outline)
        button.addTarget(self, action: #selector(negativeButtonTapped), for: .touchUpInside)
        return button
    }()

    var onPositiveButtonTap: (() -> Void)?
    var onNegativeButtonTap: (() -> Void)?

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

    func renderState(_ state: RequestReviewModalFeature.ViewState) {
        titleLabel.text = state.title

        descriptionLabel.text = state.description_
        descriptionLabel.isHidden = state.description_?.isEmpty ?? true

        positiveButton.setTitle(state.positiveButtonText, for: .normal)
        negativeButton.setTitle(state.negativeButtonText, for: .normal)

        if state.state == .negative {
            positiveButton.style = .violet

            buttonsContainerStackView.axis = .vertical
            buttonsContainerStackView.distribution = .fill
        } else {
            positiveButton.style = .outline

            buttonsContainerStackView.axis = .horizontal
            buttonsContainerStackView.distribution = .fillEqually
        }

        layoutIfNeeded()
        invalidateIntrinsicContentSize()
    }

    @objc
    private func positiveButtonTapped() {
        onPositiveButtonTap?()
    }

    @objc
    private func negativeButtonTapped() {
        onNegativeButtonTap?()
    }
}

extension RequestReviewModalView: ProgrammaticallyInitializableViewProtocol {
    func setupView() {
        backgroundColor = appearance.backgroundColor
    }

    func addSubviews() {
        addSubview(contentStackView)

        contentStackView.addArrangedSubview(textContainerStackView)
        textContainerStackView.addArrangedSubview(titleLabel)
        textContainerStackView.addArrangedSubview(descriptionLabel)

        contentStackView.addArrangedSubview(buttonsContainerStackView)
        buttonsContainerStackView.addArrangedSubview(positiveButton)
        buttonsContainerStackView.addArrangedSubview(negativeButton)
    }

    func makeConstraints() {
        contentStackView.translatesAutoresizingMaskIntoConstraints = false
        contentStackView.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(appearance.contentStackViewInsets.top)
            make.leading.equalTo(safeAreaLayoutGuide).offset(appearance.contentStackViewInsets.leading)
            make.bottom.lessThanOrEqualToSuperview().offset(-appearance.contentStackViewInsets.bottom)
            make.trailing.equalTo(safeAreaLayoutGuide).offset(-appearance.contentStackViewInsets.trailing)
        }

        buttonsContainerStackView.translatesAutoresizingMaskIntoConstraints = false
        buttonsContainerStackView.snp.makeConstraints { make in
            make.width.equalToSuperview()
        }

        positiveButton.translatesAutoresizingMaskIntoConstraints = false
        positiveButton.snp.makeConstraints { make in
            make.height.equalTo(appearance.positiveButtonButtonHeight)
        }

        negativeButton.translatesAutoresizingMaskIntoConstraints = false
        negativeButton.snp.makeConstraints { make in
            make.height.equalTo(appearance.negativeButtonButtonHeight)
        }
    }
}

#if DEBUG
@available(iOS 17.0, *)
#Preview {
    let view = RequestReviewModalView()
    view.renderState(
        RequestReviewModalFeature.ViewState(
            title: "Do you enjoy\nHyperskill app?",
            description: nil,
            positiveButtonText: "Yes",
            negativeButtonText: "No",
            state: RequestReviewModalFeature.ViewStateState.awaiting
        )
    )
    return view
}

@available(iOS 17.0, *)
#Preview {
    let view = RequestReviewModalView()
    view.renderState(
        RequestReviewModalFeature.ViewState(
            title: "Thank you!",
            description: "Share what you disliked to help us improve your experience.",
            positiveButtonText: "Write a request",
            negativeButtonText: "Maybe later",
            state: RequestReviewModalFeature.ViewStateState.negative
        )
    )
    return view
}
#endif
