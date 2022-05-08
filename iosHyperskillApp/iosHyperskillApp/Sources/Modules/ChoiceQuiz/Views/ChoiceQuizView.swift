import SwiftUI

enum QuizStatus {
    case correct
    case wrong
}

fileprivate extension QuizStatus {
    var statusText: String {
        switch self {
        case .correct:
            return "You’re absolutely correct!"
        case .wrong:
            return "Not correct, but keep on trying \nand never give up!"
        }
    }

    var feedbackText: String {
        switch self {
        case .correct:
            return "That's right! Since any comparison results in a boolean value, there is no need to write everything twice."
        case .wrong:
            return "Practice makes perfect. Let's learn from mistakes and try again."
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
                QuizStatView(text: "2438 users solved this problem. Latest completion was about 13 hours ago.")

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
