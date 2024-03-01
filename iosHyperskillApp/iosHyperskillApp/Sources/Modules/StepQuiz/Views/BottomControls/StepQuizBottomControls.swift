import SwiftUI

struct StepQuizBottomControls: View {
    var onShowDiscussionsClick: (() -> Void)?

    var body: some View {
        VStack {
            Divider()

            StepQuizDiscussionsButton(onClick: onShowDiscussionsClick).padding()
        }
        .background(BackgroundView())
    }
}

struct QuizDiscussions_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StepQuizBottomControls()

            StepQuizBottomControls()
                .preferredColorScheme(.dark)
        }
    }
}
