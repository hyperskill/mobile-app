import SwiftUI

struct StepQuizDescView: View {
    var text: String

    var body: some View {
        Text(text)
            .font(.subheadline)
            .foregroundColor(.primaryText)
    }
}

struct StepQuizDescView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizDescView(text: "Select a statement that will throw an exception.")
            .previewLayout(.sizeThatFits)
            .padding()
    }
}
