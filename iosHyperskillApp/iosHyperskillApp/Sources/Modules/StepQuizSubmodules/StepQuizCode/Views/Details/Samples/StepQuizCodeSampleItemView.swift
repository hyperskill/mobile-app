import SwiftUI

extension StepQuizCodeSampleItemView {
    struct Appearance {
        let textFont = Font(UIFont.monospacedSystemFont(ofSize: 14, weight: .regular))
    }
}

struct StepQuizCodeSampleItemView: View {
    private(set) var appearance = Appearance()

    let title: String

    let subtitle: String

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
                Text(title)
                    .foregroundColor(.secondaryText)

                Text(subtitle)
                    .foregroundColor(.primaryText)
            }
            .font(appearance.textFont)
            .padding()
            .frame(maxWidth: .infinity, alignment: .leading)
        }
        .background(BackgroundView())
    }
}

struct StepQuizCodeSampleItemView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizCodeSampleItemView(title: "Sample Input 1:", subtitle: "200")
            .previewLayout(.sizeThatFits)
            .padding()
    }
}
