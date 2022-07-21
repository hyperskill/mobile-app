import SwiftUI

struct StepQuizCodeFullScreenView: View {
    @Environment(\.presentationMode) private var presentationMode

    var body: some View {
        Button("Dismiss Modal") {
            presentationMode.wrappedValue.dismiss()
        }
    }
}

struct StepQuizCodeFullScreenView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizCodeFullScreenView()
    }
}
