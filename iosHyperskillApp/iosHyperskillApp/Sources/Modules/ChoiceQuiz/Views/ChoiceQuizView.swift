import SwiftUI

enum QuizStatus {
    case correct
    case wrong
}

fileprivate extension QuizStatus {
    var statusText: String {
        switch self {
        case .correct:
            return Strings.choiceQuizCorrectStatusText
        case .wrong:
            return Strings.choiceQuizWrongStatusText
        }
    }

    var feedbackText: String {
        switch self {
        case .correct:
            return Strings.choiceQuizCorrectFeedbackText
        case .wrong:
            return Strings.choiceQuizWrongFeedbackText
        }
    }
}

struct ChoiceQuizView: View {
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>

    var multiple: Bool

    fileprivate var status: QuizStatus?


    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 20) {
                QuizStatView(text: Strings.choiceQuizStatText)

                QuizDescView()

                HintButton()

                ChoiceView(multiple: multiple)

                if let status = status {
                    QuizStatusView(text: status.statusText, status: status)
                    FeedbackView(text: status.feedbackText)
                }

                QuizActionButton(status: status)
            }
            .padding(.horizontal)
            .padding(.top)

            QuizDiscussions()
        }
        .navigationBarTitleDisplayMode(.inline)
        .toolbar(content: { QuizNavigationToolbar.build(presentationMode) })
    }
}

struct ChoiceQuizView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            List {
                NavigationLink("Single Not solved", destination: ChoiceQuizView(multiple: false, status: nil))

                NavigationLink("Single Correct", destination: ChoiceQuizView(multiple: false, status: .correct))

                NavigationLink("Single Not correct", destination: ChoiceQuizView(multiple: false, status: .wrong))

                NavigationLink("Multiple Not solved", destination: ChoiceQuizView(multiple: true, status: nil))

                NavigationLink("Multiple Correct", destination: ChoiceQuizView(multiple: true, status: .correct))

                NavigationLink("Multiple Not correct", destination: ChoiceQuizView(multiple: true, status: .wrong))
            }
        }
    }
}
