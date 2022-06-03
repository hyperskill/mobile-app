import SwiftUI

struct ChevronIcon: View {
    @Environment(\.isEnabled) private var isEnabled

    var direction: Direction

    var onTap: () -> Void

    var body: some View {
        Button(action: onTap) {
            Image(systemName: direction.systemImageName)
                .imageScale(.large)
                .frame(widthHeight: 24)
                .foregroundColor(Color(isEnabled ? ColorPalette.onSurfaceAlpha38 : ColorPalette.onSurfaceAlpha12))
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

struct StepQuizSortingIcon_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            ChevronIcon(direction: .up, onTap: {}).disabled(false)
            ChevronIcon(direction: .up, onTap: {}).disabled(true)
            ChevronIcon(direction: .down, onTap: {}).disabled(false)
            ChevronIcon(direction: .down, onTap: {}).disabled(true)
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
