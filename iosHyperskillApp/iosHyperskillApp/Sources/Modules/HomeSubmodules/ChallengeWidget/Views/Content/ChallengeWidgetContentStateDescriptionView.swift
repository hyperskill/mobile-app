import Atributika
import shared
import SwiftUI

struct ChallengeWidgetContentStateDescriptionView: View {
    let stateKs: ChallengeWidgetViewStateContentKs

    let onOpenLink: (URL) -> Void

    var body: some View {
        let description = stateKs.sealed.headerData.description_

        switch stateKs {
        case .announcement, .happeningNow:
            LatexView(
                text: description,
                configuration: .init(
                    appearance: .init(
                        labelFont: .preferredFont(forTextStyle: .subheadline),
                        labelTextColor: .secondaryText,
                        backgroundColor: .clear
                    ),
                    htmlToAttributedStringConverter: HTMLToAttributedStringConverter(
                        font: .preferredFont(forTextStyle: .subheadline),
                        tagStyles: [
                            Style("a")
                                .foregroundColor(ColorPalette.primary, .normal)
                                .foregroundColor(ColorPalette.primary.withAlphaComponent(0.5), .highlighted)
                        ]
                    )
                ),
                onOpenLink: onOpenLink
            )
        case .completed, .partiallyCompleted, .ended:
            Text(description)
                .font(.callout)
                .fontWeight(.semibold)
                .foregroundColor(.secondaryText)
        }
    }
}

extension ChallengeWidgetContentStateDescriptionView: Equatable {
    static func == (
        lhs: ChallengeWidgetContentStateDescriptionView,
        rhs: ChallengeWidgetContentStateDescriptionView
    ) -> Bool {
        lhs.stateKs.sealed.headerData.description_ == rhs.stateKs.sealed.headerData.description_
    }
}
