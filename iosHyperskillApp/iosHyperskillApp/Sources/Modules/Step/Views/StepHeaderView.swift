import SwiftUI

extension StepHeaderView {
    struct Appearance {
        let clockImageWidthHeight: CGFloat = 12
    }
}

struct StepHeaderView: View {
    private(set) var appearance = Appearance()

    let timeToComplete: String

    var body: some View {
        HStack {
            Image(Images.Step.clock)
                .resizable()
                .renderingMode(.template)
                .aspectRatio(contentMode: .fit)
                .frame(widthHeight: appearance.clockImageWidthHeight)
            Text(timeToComplete)
                .font(.caption)
        }
        .foregroundColor(.secondaryText)
    }
}

struct StepHeaderView_Previews: PreviewProvider {
    static var previews: some View {
        StepHeaderView(timeToComplete: "3 minutes remaining")
            .previewLayout(.sizeThatFits)
            .padding()
    }
}
