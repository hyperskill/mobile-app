import SwiftUI

extension ProgressScreenSectionTitleView {
    struct Appearance {
        let spacing: CGFloat
    }
}

struct ProgressScreenSectionTitleView<Icon>: View where Icon: View {
    let appearance: Appearance

    let title: String

    @ViewBuilder var icon: () -> Icon

    var body: some View {
        if title.isEmpty {
            EmptyView()
        } else {
            HStack(spacing: appearance.spacing) {
                icon()

                Text(title)
                    .font(.headline)
                    .foregroundColor(.primaryText)
            }
        }
    }
}

struct ProgressScreenSectionTitleView_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            ProgressScreenSectionTitleView(
                appearance: .init(spacing: LayoutInsets.defaultInset),
                title: "Python Core",
                icon: { ProjectLevelAvatarView(level: .easy) }
            )
        }
    }
}
