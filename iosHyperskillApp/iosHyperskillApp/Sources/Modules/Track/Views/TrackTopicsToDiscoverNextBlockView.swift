import SwiftUI

extension TrackTopicsToDiscoverNextBlockView {
    struct Appearance {
        let insets = LayoutInsets(horizontal: LayoutInsets.defaultInset, vertical: LayoutInsets.largeInset)

        var spacing = LayoutInsets.defaultInset
    }
}

struct TrackTopicsToDiscoverNextBlockView: View {
    private(set) var appearance = Appearance()

    var topics: [TrackViewData.TheoryTopic]

    var onTopicTapped: ((TrackViewData.TheoryTopic) -> Void)?

    var body: some View {
        VStack(alignment: .leading, spacing: appearance.spacing) {
            Text(Strings.Track.TopicsToDiscoverNext.title)
                .font(.title3)
                .foregroundColor(.primaryText)
                .bold()

            listView
        }
        .frame(maxWidth: .infinity)
        .padding(appearance.insets.edgeInsets)
        .background(BackgroundView(color: Color(ColorPalette.surface)))
    }

    private var listView: some View {
        LazyVStack(spacing: appearance.spacing) {
            ForEach(topics) { topic in
                Button(
                    action: {
                        onTopicTapped?(topic)
                    },
                    label: {
                        Text(topic.title)
                            .font(.body)
                            .foregroundColor(.primaryText)
                    }
                )
                .buttonStyle(OutlineButtonStyle(borderColor: .border, alignment: .leading))
            }
        }
    }
}

struct TrackTopicsToDiscoverNextBlockView_Previews: PreviewProvider {
    static var previews: some View {
        TrackTopicsToDiscoverNextBlockView(
            topics: [
                .init(id: 1, title: "Basic data types"),
                .init(id: 2, title: "Variables"),
                .init(id: 3, title: "Integer arithmetic"),
                .init(id: 4, title: "Basic data types")
            ]
        )
    }
}
