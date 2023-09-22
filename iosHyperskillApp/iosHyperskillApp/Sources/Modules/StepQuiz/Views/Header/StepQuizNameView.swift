import SwiftUI

struct StepQuizNameView: View {
    let text: String

    var body: some View {
        Text(text)
            .font(.caption)
            .foregroundColor(.tertiaryText)
    }
}

struct StepQuizTitleView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizNameView(text: "Select one option from the list")
            .previewLayout(.sizeThatFits)
            .padding()
    }
}
