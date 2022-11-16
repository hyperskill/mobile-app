import SwiftUI

struct TopicsRepetitionsRepeatBlock: View {
    #warning("create necessary structure in shared module")
    private let data: [(String, [String])] = [
        ("Python Core",
        [
            "Variables",
            "Quotes and multi-line strings",
            "Basic data types"
        ]),
        ("other tracks",
        [
            "Variables",
            "Classes",
            "Basic data types"
        ])
    ]

    let topicsToRepeatCount: Int

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.largeInset) {
            #warning("Implement formatting in shared module")
            Text("\(Strings.TopicsRepetitions.RepeatBlock.title) \(topicsToRepeatCount)")
                .font(.title3)
                .foregroundColor(.primaryText)
                .bold()

            ForEach(data, id: \.0) { (track, topics) in
                #warning("Implement formatting in shared module")
                Text("\(Strings.TopicsRepetitions.RepeatBlock.currentTrack) \(track)")
                    .font(.subheadline)
                    .foregroundColor(.secondaryText)

                VStack(alignment: .leading, spacing: LayoutInsets.smallInset) {
                    ForEach(topics, id: \.self) { topic in
                        Button(
                            action: {
                                #warning("implement navigation to step quiz")
                            },
                            label: {
                                Text(topic)
                                    .font(.body)
                                    .foregroundColor(.primaryText)
                            }
                        )
                        .buttonStyle(OutlineButtonStyle(borderColor: .border, alignment: .leading))
                    }
                }

                ShowMoreButton {}
            }
        }
        .padding()
        .background(Color(ColorPalette.surface))
    }
}

struct TopicsRepetitionsRepeatBlock_Previews: PreviewProvider {
    static var previews: some View {
        TopicsRepetitionsRepeatBlock(topicsToRepeatCount: 4)
            .previewLayout(.sizeThatFits)
    }
}
