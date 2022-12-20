import UIKit

extension UIKitRoundedRectangleButton {
    struct Appearance {
        var insets = LayoutInsets.default
        var titleInsets = LayoutInsets(horizontal: LayoutInsets.defaultInset)

        var font = UIFont.preferredFont(forTextStyle: .body)
        var intrinsicHeight: CGFloat = 44

        var cornerRadius: CGFloat = 8
    }
}

final class UIKitRoundedRectangleButton: UIKitBounceButton {
    let appearance: Appearance

    var style: Style {
        didSet {
            updateAppearance()
        }
    }

    override var isEnabled: Bool {
        didSet {
            alpha = isEnabled ? 1.0 : 0.5
        }
    }

    override var intrinsicContentSize: CGSize {
        CGSize(width: UIView.noIntrinsicMetric, height: appearance.intrinsicHeight)
    }

    init(
        frame: CGRect = .zero,
        style: Style = .violet,
        appearance: Appearance = Appearance()
    ) {
        self.style = style
        self.appearance = appearance

        super.init(frame: frame)

        updateAppearance()
    }

    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func layoutSubviews() {
        super.layoutSubviews()
        updateAppearance()
    }

    // MARK: Private API

    private func updateAppearance() {
        setTitleColor(style.foregroundColor, for: .normal)
        titleLabel?.font = appearance.font
        titleEdgeInsets = appearance.titleInsets.uiEdgeInsets

        backgroundColor = style.backgroundColor
        clipsToBounds = true
        layer.cornerRadius = appearance.cornerRadius

        if let border = style.border {
            layer.borderWidth = border.width
            layer.borderColor = border.color.cgColor
        }
    }

    // MARK: Inner Types

    enum Style {
        case green
        case violet
        case outline

        fileprivate var foregroundColor: UIColor {
            switch self {
            case .green:
                return ColorPalette.onSecondary
            case .violet:
                return ColorPalette.onPrimary
            case .outline:
                return ColorPalette.primary
            }
        }

        fileprivate var backgroundColor: UIColor {
            switch self {
            case .green:
                return ColorPalette.secondary
            case .violet:
                return ColorPalette.primary
            case .outline:
                return UIColor.systemBackground
            }
        }

        fileprivate var border: Border? {
            switch self {
            case .green, .violet:
                return nil
            case .outline:
                return Border(color: ColorPalette.primaryAlpha38)
            }
        }

        struct Border {
            let color: UIColor
            var width: CGFloat = 1
        }
    }
}
