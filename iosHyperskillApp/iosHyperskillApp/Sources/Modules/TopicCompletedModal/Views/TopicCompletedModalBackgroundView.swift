import shared
import SwiftUI

struct TopicCompletedModalBackgroundView: View {
    let style: TopicCompletedModalFeature.ViewStateBackgroundAnimationStyle

    @Environment(\.colorScheme) var colorScheme

    private var resourceName: String {
        let size = switch UIScreen.main.bounds.width {
        case ...414:
            "414"
        case ...430:
            "430"
        default:
            "834"
        }

        let theme = colorScheme == .dark ? "dark" : "light"
        let styleSuffix = style == .first ? "1" : "2"

        return "topic-completed-modal-background-video-\(size)-\(theme)-\(styleSuffix)"
    }

    var body: some View {
        BackgroundVideoView(
            resourceName: resourceName,
            resourceType: "mp4"
        )
        .edgesIgnoringSafeArea(.all)
    }
}

#if DEBUG
 #Preview {
    NavigationView {
        ZStack {
            TopicCompletedModalBackgroundView(style: .first)
        }
    }
 }
#endif
