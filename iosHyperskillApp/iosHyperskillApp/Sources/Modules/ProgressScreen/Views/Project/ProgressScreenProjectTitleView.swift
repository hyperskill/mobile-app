import SwiftUI

extension ProgressScreenProjectTitleView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset

        let avatarAppearance = ProjectLevelAvatarView.Appearance(
            imageSize: .init(width: 16, height: 16),
            padding: .small
        )
    }
}

struct ProgressScreenProjectTitleView: View {
    private(set) var appearance = Appearance()

    let projectLevel: SharedProjectLevelWrapper?

    let title: String

    private var isEmpty: Bool {
        projectLevel == nil && title.isEmpty
    }

    var body: some View {
        if isEmpty {
            EmptyView()
        } else {
            HStack(spacing: appearance.spacing) {
                if let projectLevel {
                    ProjectLevelAvatarView(
                        appearance: appearance.avatarAppearance,
                        level: projectLevel
                    )
                }

                Text(title)
                    .font(.headline)
                    .foregroundColor(.primaryText)
            }
        }
    }
}

struct ProgressScreenProjectTitleView_Previews: PreviewProvider {
    static var previews: some View {
        ProgressScreenProjectTitleView(
            projectLevel: .easy,
            title: "Python Core"
        )
        .padding()
    }
}
