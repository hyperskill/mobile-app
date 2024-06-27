import SwiftUI

extension StepTheoryHeaderView {
    enum Appearance {
        static let clockImageWidthHeight: CGFloat = 12
    }
}

struct StepTheoryHeaderView: View {
    let timeToComplete: String

    var body: some View {
        HStack {
            Image(Images.Step.clock)
                .resizable()
                .renderingMode(.template)
                .aspectRatio(contentMode: .fit)
                .frame(widthHeight: Appearance.clockImageWidthHeight)

            Text(timeToComplete)
                .font(.caption)
        }
        .foregroundColor(.secondaryText)
    }
}

#if DEBUG
#Preview {
    StepTheoryHeaderView(timeToComplete: "3 minutes remaining")
        .padding()
}
#endif
