import SnapKit
import UIKit

extension StepQuizFillBlanksSelectOptionsCollectionViewCellContainerView {
    struct Appearance {
        var cornerRadius: CGFloat = 8
        let borderWidth: CGFloat = 1
    }
}

final class StepQuizFillBlanksSelectOptionsCollectionViewCellContainerView: UIView {
    let appearance: Appearance

    var state = State.default {
        didSet {
            self.updateState()
        }
    }

    init(
        frame: CGRect = .zero,
        appearance: Appearance = Appearance()
    ) {
        self.appearance = appearance
        super.init(frame: frame)

        self.clipsToBounds = true
        self.layer.cornerRadius = self.appearance.cornerRadius

        self.updateState()
    }

    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func traitCollectionDidChange(_ previousTraitCollection: UITraitCollection?) {
        super.traitCollectionDidChange(previousTraitCollection)
        self.performBlockIfAppearanceChanged(from: previousTraitCollection, block: self.updateState)
    }

    private func updateState() {
        self.backgroundColor = self.state.backgroundColor

        if let borderColor = self.state.borderColor {
            self.layer.borderColor = borderColor.cgColor
            self.layer.borderWidth = self.appearance.borderWidth
        } else {
            self.layer.borderColor = nil
            self.layer.borderWidth = 0
        }
    }

    enum State {
        case `default`
        case selected

        fileprivate var borderColor: UIColor? {
            switch self {
            case .default:
                ColorPalette.onSurfaceAlpha12
            case .selected:
                nil
            }
        }

        fileprivate var backgroundColor: UIColor {
            switch self {
            case .default:
                ColorPalette.background
            case .selected:
                ColorPalette.onSurfaceAlpha9
            }
        }
    }
}

@available(iOS 17, *)
#Preview {
    let view = StepQuizFillBlanksSelectOptionsCollectionViewCellContainerView()
    view.state = .default
    return view
}

@available(iOS 17, *)
#Preview {
    let view = StepQuizFillBlanksSelectOptionsCollectionViewCellContainerView()
    view.state = .selected
    return view
}
