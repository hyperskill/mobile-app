import SwiftUI

extension TopicsRepetitionsCountView {
    struct Appearance {
        let topicsCountHorizontalPadding: CGFloat = 12
    }
}

struct TopicsRepetitionsCountView: View {
    private(set) var appearance = Appearance()

    let topicsToRepeatCount: Int

    var body: some View {
        HStack(spacing: LayoutInsets.smallInset) {
            Text(String(topicsToRepeatCount))
                .font(.subheadline)
                .foregroundColor(.primaryText)
                .padding(.horizontal, appearance.topicsCountHorizontalPadding)
                .padding(.vertical, LayoutInsets.smallInset)
                .background(Color(ColorPalette.overlayBlueAlpha12))
                .cornerRadius(LayoutInsets.smallInset)

            Text(Strings.TopicsRepetitions.cardTextUncompleted)
                .font(.subheadline)
                .foregroundColor(.secondaryText)
        }
    }
}

struct TopicsRepetitionsCountView_Previews: PreviewProvider {
    static var previews: some View {
        TopicsRepetitionsCountView(topicsToRepeatCount: 4)
    }
}
