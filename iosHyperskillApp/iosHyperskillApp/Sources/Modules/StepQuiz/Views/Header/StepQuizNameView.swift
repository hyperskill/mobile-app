import SwiftUI

struct StepQuizNameView: View {
    let text: String

    var dividerLocation = DividerLocation.top

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
            if dividerLocation == .top {
                Divider()
            }

            Text(text)
                .font(.caption)
                .foregroundColor(.disabledText)

            if dividerLocation == .bottom {
                Divider()
            }
        }
    }

    enum DividerLocation {
        case top
        case bottom
    }
}

struct StepQuizTitleView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizNameView(text: "Select one option from the list")
            .previewLayout(.sizeThatFits)
            .padding()
    }
}
