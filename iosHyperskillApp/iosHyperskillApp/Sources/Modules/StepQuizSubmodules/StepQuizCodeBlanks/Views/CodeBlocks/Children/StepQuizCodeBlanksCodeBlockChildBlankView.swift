import SwiftUI

struct StepQuizCodeBlanksCodeBlockChildBlankView: View {
    var width: CGFloat = 208
    var height: CGFloat = 48

    let isActive: Bool

    var body: some View {
        Color(ColorPalette.onSurfaceAlpha9)
            .frame(width: width, height: height)
            .addBorder(
                color: isActive ? StepQuizCodeBlanksAppearance.activeBorderColor : .clear,
                width: isActive ? 1 : 0
            )
            .animation(.default, value: isActive)
    }
}

extension StepQuizCodeBlanksCodeBlockChildBlankView {
    init(style: Style, isActive: Bool) {
        let size = style.size
        self.init(width: size.width, height: size.height, isActive: isActive)
    }

    enum Style {
        case small
        case large

        fileprivate var size: CGSize {
            switch self {
            case .small:
                CGSize(width: 100, height: 40)
            case .large:
                CGSize(width: 208, height: 48)
            }
        }
    }
}

#if DEBUG
#Preview {
    VStack {
        StepQuizCodeBlanksCodeBlockChildBlankView(style: .small, isActive: true)
        StepQuizCodeBlanksCodeBlockChildBlankView(style: .large, isActive: false)
    }
}
#endif
