import SwiftUI

struct StepQuizSortingIcon: View {
    @Environment(\.isEnabled) private var isEnabled

    let direction: Direction

    let action: () -> Void

    var body: some View {
        Button(action: action) {
            Image(systemName: direction.systemImageName)
                .font(.title2.bold())
                .padding(.horizontal, LayoutInsets.smallInset / 2)
                .foregroundColor(Color(isEnabled ? ColorPalette.primary : ColorPalette.primaryAlpha60))
        }
    }

    enum Direction {
        // swiftlint:disable:next identifier_name
        case up
        case down

        fileprivate var systemImageName: String {
            switch self {
            case .up:
                return Images.SystemSymbol.Chevron.up
            case .down:
                return Images.SystemSymbol.Chevron.down
            }
        }
    }
}

#if DEBUG
#Preview {
    VStack(spacing: 8) {
        StepQuizSortingIcon(direction: .up, action: {}).disabled(false)
        StepQuizSortingIcon(direction: .up, action: {}).disabled(true)
        StepQuizSortingIcon(direction: .down, action: {}).disabled(false)
        StepQuizSortingIcon(direction: .down, action: {}).disabled(true)
    }
    .padding()
}
#endif
