import SwiftUI

struct StepQuizNameView: View {
    let text: String

    var dividerLocation = DividerLocation.top

    @Environment(\.colorScheme) private var colorScheme

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
            if dividerLocation == .top {
                divider
            }

            Text(text)
                .font(.caption)
                .foregroundColor(.disabledText)

            if dividerLocation == .bottom {
                divider
            }
        }
    }

    @ViewBuilder
    var divider: some View {
        switch colorScheme {
        case .light:
            Divider()
        case .dark:
            Divider()
                .overlay(Color(UIColor.separator))
        @unknown default:
            Divider()
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
