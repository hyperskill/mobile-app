import SwiftUI

struct QuizDescView: View {
    var body: some View {
        VStack(alignment: .leading, spacing: 20) {
            Text("Select a statement that will throw an exception.")
                .font(.subheadline)
        }
    }
}

struct QuizDescView_Previews: PreviewProvider {
    static var previews: some View {
        QuizDescView()
    }
}
