import SnapKit
import UIKit

extension StageImplementUnsupportedModalView {
    struct Appearance {
        let contentStackViewSpacing: CGFloat = LayoutInsets.defaultInset * 2
        let contentStackViewInsets = LayoutInsets(
            horizontal: LayoutInsets.defaultInset,
            vertical: LayoutInsets.defaultInset
        )

        let imageViewImageName = Images.StageImplement.UnsupportedModal.icon
        let imageViewSizeRatio = CGSize(width: 0.63, height: 0.20)

        let textContainerStackViewSpacing: CGFloat = LayoutInsets.defaultInset

        let titleLabelText = Strings.StageImplement.UnsupportedModal.title
        let titleLabelTextFont = UIFont.preferredFont(
            forTextStyle: .title2,
            compatibleWith: .init(legibilityWeight: .bold)
        )
        let titleLabelTextColor = UIColor.primaryText

        let descriptionLabelText = Strings.StageImplement.UnsupportedModal.description
        let descriptionLabelFont = UIFont.preferredFont(forTextStyle: .body)
        let descriptionLabelTextColor = UIColor.primaryText

        let callToActionButtonTitle = Strings.General.goToHomescreen
        let callToActionButtonHeight: CGFloat = 44

        let backgroundColor = UIColor.systemBackground
    }
}

final class StageImplementUnsupportedModalView: UIView {
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

    private lazy var imageView: UIImageView = {
        let image = UIImage(named: appearance.imageViewImageName)?.withRenderingMode(.alwaysOriginal)

        let imageView = UIImageView(image: image)
        imageView.contentMode = .scaleAspectFit

        return imageView
    }()

    private lazy var titleLabel: UILabel = {
        let label = UILabel()
        label.text = appearance.titleLabelText
        label.font = appearance.titleLabelTextFont
        label.textColor = appearance.titleLabelTextColor
        label.lineBreakMode = .byWordWrapping
        label.numberOfLines = 0
        return label
    }()

    private lazy var descriptionLabel: UILabel = {
        let label = UILabel()
        label.text = appearance.descriptionLabelText
        label.font = appearance.descriptionLabelFont
        label.textColor = appearance.descriptionLabelTextColor
        label.lineBreakMode = .byWordWrapping
        label.numberOfLines = 0
        return label
    }()

    private lazy var callToActionButton: UIButton = {
        let button = UIKitRoundedRectangleButton(style: .violet)
        button.setTitle(appearance.callToActionButtonTitle, for: .normal)
        button.addTarget(self, action: #selector(callToActionButtonTapped), for: .touchUpInside)
        return button
    }()

    override var intrinsicContentSize: CGSize {
        let contentStackViewSize = contentStackView.systemLayoutSizeFitting(UIView.layoutFittingCompressedSize)

        let height = appearance.contentStackViewInsets.top
          + contentStackViewSize.height
          + appearance.contentStackViewInsets.bottom

        return CGSize(width: UIView.noIntrinsicMetric, height: height)
    }

    var onCallToActionButtonTapped: (() -> Void)?

    init(
        frame: CGRect = .zero,
        appearance: Appearance = Appearance()
    ) {
        self.appearance = appearance
        super.init(frame: frame)

        setupView()
        addSubviews()
        makeConstraints()
    }

    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    @objc
    private func callToActionButtonTapped() {
        onCallToActionButtonTapped?()
    }
}

extension StageImplementUnsupportedModalView: ProgrammaticallyInitializableViewProtocol {
    func setupView() {
        backgroundColor = appearance.backgroundColor
    }

    func addSubviews() {
        addSubview(contentStackView)

        contentStackView.addArrangedSubview(imageView)
        contentStackView.addArrangedSubview(textContainerStackView)

        textContainerStackView.addArrangedSubview(titleLabel)
        textContainerStackView.addArrangedSubview(descriptionLabel)

        contentStackView.addArrangedSubview(callToActionButton)
    }

    func makeConstraints() {
        contentStackView.translatesAutoresizingMaskIntoConstraints = false
        contentStackView.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(appearance.contentStackViewInsets.top)
            make.leading.equalTo(safeAreaLayoutGuide).offset(appearance.contentStackViewInsets.leading)
            make.bottom.lessThanOrEqualToSuperview().offset(-appearance.contentStackViewInsets.bottom)
            make.trailing.equalTo(safeAreaLayoutGuide).offset(-appearance.contentStackViewInsets.trailing)
        }

        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.snp.makeConstraints { make in
            make.width.equalTo(self).multipliedBy(appearance.imageViewSizeRatio.width)
            make.height.equalTo(self).multipliedBy(appearance.imageViewSizeRatio.height)
        }

        callToActionButton.translatesAutoresizingMaskIntoConstraints = false
        callToActionButton.snp.makeConstraints { make in
            make.width.equalToSuperview()
            make.height.equalTo(appearance.callToActionButtonHeight)
        }
    }
}
