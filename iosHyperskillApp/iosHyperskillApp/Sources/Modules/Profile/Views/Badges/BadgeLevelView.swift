import SwiftUI

extension BadgeLevelView {
    struct Appearance {
        var rootSpacing = LayoutInsets.smallInset

        let levelContainerSpacing: CGFloat = 4

        var currentLevelFont = Font.caption2
        let currentLevelForegroundColor = Color.primaryText

        var nextLevelFont = Font.caption2
        let nextLevelForegroundColor = Color.disabledText
    }
}

struct BadgeLevelView: View {
    private(set) var appearance = Appearance()

    let currentLevel: String
    let nextLevel: String?

    let progress: Float

    let description: String?

    var body: some View {
        VStack(spacing: appearance.rootSpacing) {
            HStack(spacing: appearance.levelContainerSpacing) {
                Text(currentLevel)
                    .font(appearance.currentLevelFont)
                    .foregroundColor(appearance.currentLevelForegroundColor)

                Spacer()

                if let nextLevel {
                    Label(nextLevel, systemImage: "lock")
                        .font(appearance.nextLevelFont)
                        .foregroundColor(appearance.nextLevelForegroundColor)
                }
            }

            LinearGradientProgressView(
                progress: progress,
                gradientStyle: .badge
            )

            if let description {
                Text(description)
                    .font(.subheadline)
                    .foregroundColor(.secondaryText)
                    .frame(maxWidth: .infinity, alignment: .leading)
            }
        }
    }
}

extension BadgeLevelView {
    init(currentLevel: String, nextLevel: Int?, progress: Float) {
        self.init(
            currentLevel: currentLevel,
            nextLevel: nextLevel.flatMap(String.init),
            progress: progress,
            description: nil
        )
    }
}

struct BadgeLevelView_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: LayoutInsets.largeInset) {
            BadgeLevelView(
                currentLevel: "Level 2",
                nextLevel: "3",
                progress: 0.3,
                description: nil
            )

            BadgeLevelView(
                appearance: .init(
                    rootSpacing: LayoutInsets.defaultInset,
                    currentLevelFont: .headline,
                    nextLevelFont: .headline
                ),
                currentLevel: "Locked",
                nextLevel: "Level 1",
                progress: 0,
                description: "Complete 1 project to unlock this badge"
            )
        }
        .padding()
    }
}
