import SwiftUI

extension RepetitionsExplanationView {
    struct Appearance {
        let bookImageWidthHeight: CGFloat = 80
    }
}

struct RepetitionsExplanationView: View {
    private(set) var appearance = Appearance()

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.largeInset) {
            Image(Images.TopicsRepetitions.bookImage)
                .resizable()
                .frame(widthHeight: appearance.bookImageWidthHeight)

            Text(Strings.TopicsRepetitions.explanationTitle)
                .font(.title)
                .foregroundColor(.primaryText)

            VStack(alignment: .leading, spacing: LayoutInsets.smallInset) {
                Text(Strings.TopicsRepetitions.explanationText1)

                Text(Strings.TopicsRepetitions.explanationText2)
            }
            .font(.subheadline)
            .foregroundColor(.secondaryText)

            VStack(alignment: .leading, spacing: LayoutInsets.smallInset) {
                Text(Strings.TopicsRepetitions.explanationText3)

                Text(Strings.TopicsRepetitions.explanationText4)
            }
            .font(.subheadline)
            .foregroundColor(.secondaryText)
        }
        .padding(LayoutInsets.largeInset)
        .background(Color.white)
    }
}

struct RepetitionsExplanationView_Previews: PreviewProvider {
    static var previews: some View {
        RepetitionsExplanationView()
    }
}
