import SwiftUI

struct StepQuizNameView: View {
    let text: String

    var body: some View {
        Text(text)
            .font(.headline)
            .foregroundColor(.primaryText)
    }
}

#if DEBUG
#Preview {
    StepQuizNameView(
        text: "Select one option from the list"
    )
    .padding()
}
#endif
