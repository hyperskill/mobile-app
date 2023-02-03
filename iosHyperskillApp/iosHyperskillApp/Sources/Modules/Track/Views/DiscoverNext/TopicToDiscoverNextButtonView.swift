import shared
import SwiftUI

extension TopicToDiscoverNextButtonView {
    struct Appearance {
        let rightDetailIconWidthHeight: CGFloat = 12

        let backgroundProgressColor = Color(ColorPalette.overlayGreenAlpha7)

        let buttonStyle = OutlineButtonStyle(borderColor: .border, alignment: .leading, paddingEdgeSet: [])
    }
}

struct TopicToDiscoverNextButtonView: View {
    private(set) var appearance = Appearance()

    let topic: Topic

    let onTap: () -> Void

    var body: some View {
        Button(
            action: onTap,
            label: {
                HStack {
                    Text(topic.title)
                        .font(.body)
                        .foregroundColor(.primaryText)

                    Spacer()

                    if let topicProgress = topic.progress {
                        if topicProgress.isCompleted {
                            Image(systemName: "checkmark")
                                .resizable()
                                .renderingMode(.template)
                                .aspectRatio(contentMode: .fit)
                                .foregroundColor(Color(ColorPalette.secondary))
                                .frame(widthHeight: appearance.rightDetailIconWidthHeight)
                        } else if topicProgress.isSkipped {
                            Image(Images.Track.TopicsToDiscoverNext.skippedTopic)
                                .resizable()
                                .renderingMode(.template)
                                .aspectRatio(contentMode: .fit)
                                .foregroundColor(Color(ColorPalette.onSurfaceAlpha38))
                                .frame(widthHeight: appearance.rightDetailIconWidthHeight)
                        } else if topicProgress.completenessPercentage > 0 {
                            Text("\(Int(topicProgress.completenessPercentage.rounded()))%")
                                .font(.subheadline)
                                .foregroundColor(Color(ColorPalette.secondary))
                        }
                    }
                }
                .padding(.horizontal)
                .frame(minHeight: appearance.buttonStyle.minHeight)
                .background(
                    GeometryReader { geometry in
                        Rectangle()
                            .stroke(lineWidth: 0)
                            .background(appearance.backgroundProgressColor)
                            .cornerRadius(appearance.buttonStyle.cornerRadius)
                            .frame(
                                width: geometry.size.width * CGFloat(topic.progress?.completenessPercentage ?? 0) / 100
                            )
                    }
                )
            }
        )
        .buttonStyle(appearance.buttonStyle)
    }
}

struct TopicToDiscoverNextButtonView_Previews: PreviewProvider {
    static var previews: some View {
        VStack {
            TopicToDiscoverNextButtonView(
                topic: .init(
                    id: 4,
                    progressId: "",
                    theoryId: nil,
                    title: "Pro data types",
                    progress: TopicProgress(
                        id: "",
                        stagePosition: 0,
                        repeatedCount: 0,
                        isCompleted: false,
                        isSkipped: true,
                        capacity: 0
                    )
                ),
                onTap: {}
            )

            TopicToDiscoverNextButtonView(
                topic: .init(
                    id: 4,
                    progressId: "",
                    theoryId: nil,
                    title: "Pro data types",
                    progress: TopicProgress(
                        id: "",
                        stagePosition: 0,
                        repeatedCount: 0,
                        isCompleted: true,
                        isSkipped: false,
                        capacity: 0
                    )
                ),
                onTap: {}
            )

            TopicToDiscoverNextButtonView(
                topic: .init(
                    id: 4,
                    progressId: "",
                    theoryId: nil,
                    title: "Pro data types",
                    progress: TopicProgress(
                        id: "",
                        stagePosition: 0,
                        repeatedCount: 0,
                        isCompleted: false,
                        isSkipped: false,
                        capacity: 0.4
                    )
                ),
                onTap: {}
            )
        }
        .padding()
        .background(Color(ColorPalette.surface))
        .previewLayout(.sizeThatFits)
        .preferredColorScheme(.dark)
    }
}
