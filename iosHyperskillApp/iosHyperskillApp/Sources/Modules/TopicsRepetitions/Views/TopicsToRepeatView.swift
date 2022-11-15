import SwiftUI

struct TopicsToRepeatView: View {
    // TODO: create necessary structure in shared module
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
            "Variables",
            "Basic data types"
        ])
    ]

    let topicsToRepeatCount: Int

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.largeInset) {
            // TODO: Implement formatting in shared module
            Text("\(Strings.TopicsRepetitions.allTopicsToRepeat) \(topicsToRepeatCount)")
                .font(.title3)
                .foregroundColor(.primaryText)

            ForEach(data, id: \.0) { (track, topics) in
                // TODO: Implement formatting in shared module
                Text("\(Strings.TopicsRepetitions.topicsFromTrack) \(track)")
                    .font(.subheadline)
                    .foregroundColor(.secondaryText)

                VStack(alignment: .leading, spacing: LayoutInsets.smallInset) {
                    ForEach(topics, id: \.self) { topic in
                        Button(
                            action: {
                                // TODO: implement navigation to step quiz
                            },
                            label: {
                                Text(topic)
                                    .font(.body)
                                    .foregroundColor(.primaryText)
                            }
                        )
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .padding()
                        .addBorder()
                    }
                }

                ShowMoreButton {}
            }
        }
        .padding()
        .background(Color.white)
    }
}

struct TopicsToRepeatView_Previews: PreviewProvider {
    static var previews: some View {
        TopicsToRepeatView(topicsToRepeatCount: 4)
        .previewLayout(.sizeThatFits)
    }
}
