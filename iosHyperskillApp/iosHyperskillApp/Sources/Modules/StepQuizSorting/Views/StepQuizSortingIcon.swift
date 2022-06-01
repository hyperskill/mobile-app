import SwiftUI

struct StepQuizSortingIcon: View {
    @Environment(\.isEnabled) private var isEnabled

    var direction: SortingDirection

    var onClick: () -> Void

    var body: some View {
        Button(
            action: onClick,
            label: {
                Image(systemName: direction.image)
                    .imageScale(.large)
                    .frame(widthHeight: 24)
                    .foregroundColor(
                        Color(isEnabled ? ColorPalette.onSurfaceAlpha38 : ColorPalette.onSurfaceAlpha12)
                    )
            }
        )
        .disabled(!isEnabled)
    }
}

enum SortingDirection {
    case upward
    case down

    var image: String {
        switch self {
        case .upward:
            return Images.StepSorting.chevronUp
        case .down:
            return Images.StepSorting.chevronDown
        }
    }
}

struct StepQuizSortingIcon_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StepQuizSortingIcon(direction: .upward, onClick: {}).disabled(false)
            StepQuizSortingIcon(direction: .upward, onClick: {}).disabled(true)
            StepQuizSortingIcon(direction: .down, onClick: {}).disabled(false)
            StepQuizSortingIcon(direction: .down, onClick: {}).disabled(true)
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
