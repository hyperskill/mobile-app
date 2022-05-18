import SwiftUI

struct QuizView: View {
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>


    @State var choiceQuiz: ChoiceQuizViewData


    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 20) {
                QuizStatsView(
                    text: Strings.choiceQuizStatText(
                        users: choiceQuiz.statsUsers,
                        hours: choiceQuiz.statsHours
                    )
                )

                QuizDescView()

                QuizHintButton()

                ChoiceQuizView(
                    task: choiceQuiz.task,
                    choices: $choiceQuiz.choices,
                    isMultipleChoice: choiceQuiz.type == .multiple
                )

                if let status = choiceQuiz.status {
                    if let feedbackText = status.feedbackText {
                        QuizStatusView(status: status)
                        QuizFeedbackView(text: feedbackText)
                    }
                    if status == .evaluation {
                        QuizProgressView()
                    }
                }

                QuizActionButton(status: choiceQuiz.status)
            }
            .padding(.horizontal)
            .padding(.top)

            QuizDiscussions()
        }
        .navigationBarTitleDisplayMode(.inline)
        .navigationBarBackButtonTitleRemoved {
            self.presentationMode.wrappedValue.dismiss()
        }
        .navigationTitle(choiceQuiz.title)
        .toolbar { NavigationToolbarInfoButton() }
    }
}

fileprivate extension QuizStatus {
    var feedbackText: String? {
        switch self {
        case .correct:
            return Strings.choiceQuizCorrectFeedbackText
        case .wrong:
            return Strings.choiceQuizWrongFeedbackText
        default:
            return nil
        }
    }
}

struct ChoiceQuizView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            List {
                NavigationLink(
                    "Single Not solved",
                    destination: QuizView(choiceQuiz: .init(type: .single, status: nil))
                )

                NavigationLink(
                    "Single Correct",
                    destination: QuizView(choiceQuiz: .init(type: .single, status: .correct))
                )

                NavigationLink(
                    "Single Wrong",
                    destination: QuizView(choiceQuiz: .init(type: .single, status: .wrong))
                )

                NavigationLink(
                    "Single Evaluation",
                    destination: QuizView(choiceQuiz: .init(type: .single, status: .evaluation))
                )

                NavigationLink(
                    "Multiple Not solved",
                    destination: QuizView(choiceQuiz: .init(type: .multiple, status: nil))
                )

                NavigationLink(
                    "Multiple Correct",
                    destination: QuizView(choiceQuiz: .init(type: .multiple, status: .correct))
                )

                NavigationLink(
                    "Multiple Wrong",
                    destination: QuizView(choiceQuiz: .init(type: .multiple, status: .wrong))
                )

                NavigationLink(
                    "Multiple Evaluation",
                    destination: QuizView(choiceQuiz: .init(type: .multiple, status: .evaluation))
                )
            }
        }
    }
}
