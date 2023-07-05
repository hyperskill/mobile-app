import SwiftUI

extension BadgeView {
    static func ideRequired() -> BadgeView {
        BadgeView(text: Strings.Badge.ideRequired, style: .violet)
    }

    static func current() -> BadgeView {
        BadgeView(text: Strings.Badge.current, style: .blue)
    }

    static func solveUnlimited() -> BadgeView {
        BadgeView(text: Strings.Badge.solveUnlimited, style: .violet)
    }

    static func repeatUnlimited() -> BadgeView {
        BadgeView(text: Strings.Badge.repeatUnlimited, style: .violet)
    }

    static func selected() -> BadgeView {
        BadgeView(text: Strings.Badge.selected, style: .blue)
    }

    static func bestRating() -> BadgeView {
        BadgeView(text: Strings.Badge.bestRating, style: .blue)
    }

    static func fastestToComplete() -> BadgeView {
        BadgeView(text: Strings.Badge.fastestToComplete, style: .blue)
    }

    static func beta() -> BadgeView {
        BadgeView(text: Strings.Badge.beta, style: .orange)
    }

    static func completed() -> BadgeView {
        BadgeView(text: Strings.General.completed, style: .green)
    }
}

// MARK: - BadgeViewConcreateTypes_Previews: PreviewProvider -

struct BadgeViewConcreateTypes_Previews: PreviewProvider {
    static var previews: some View {
        VStack {
            BadgeView.ideRequired()

            BadgeView.current()

            BadgeView.solveUnlimited()
            BadgeView.repeatUnlimited()

            BadgeView.selected()
            BadgeView.bestRating()
            BadgeView.fastestToComplete()

            BadgeView.beta()
            BadgeView.completed()
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
