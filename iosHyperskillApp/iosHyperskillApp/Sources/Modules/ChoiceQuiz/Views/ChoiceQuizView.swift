import SwiftUI

struct ChoiceQuizView: View {
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>

    var multiple: Bool
    var correct: Bool
    var notCorrect: Bool


    init(multiple: Bool, correct: Bool, notCorrect: Bool) {
        QuizNavigationToolbar.applyAppearance()
        self.multiple = multiple
        self.correct = correct
        self.notCorrect = notCorrect
    }


    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 20) {
                QuizStatView()

                QuizDescView()

                HintButton()

                ChoiceView(multiple: multiple)

                if correct {
                    CorrectAlertView()
                    FeedbackView(text: "That's right! Since any comparison results in a boolean value, there is no need to write everything twice.")
                }

                if notCorrect {
                    IncorrectAlertView()
                    FeedbackView(text: "Practice makes perfect. Let's learn from mistakes and try again.")
                }

                if correct {
                    Button("Send", action: {})
                        .buttonStyle(RoundedRectangleButtonStyle(style: .green))
                } else {
                    Button(notCorrect ? "Retry" : "Send", action: {})
                        .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
                }
            }
            .padding(.horizontal)
            .padding(.top)

            Spacer()
            //todo не понимаю, как правильно зафиксировать это
            // снизу scroll view
            QuizDiscussions()
        }
        .navigationBarBackButtonHidden(true)
        .toolbar(content: { QuizNavigationToolbar.build(presentationMode) })
    }
}

struct ChoiceQuizView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            List {
                NavigationLink("Single Not solved", destination: ChoiceQuizView(multiple: false, correct: false, notCorrect: false))

                NavigationLink("Single Correct", destination: ChoiceQuizView(multiple: false, correct: true, notCorrect: false))

                NavigationLink("Single Not correct", destination: ChoiceQuizView(multiple: false, correct: false, notCorrect: true))

                NavigationLink("Multiple Not solved", destination: ChoiceQuizView(multiple: true, correct: false, notCorrect: false))

                NavigationLink("Multiple Correct", destination: ChoiceQuizView(multiple: true, correct: true, notCorrect: false))

                NavigationLink("Multiple Not correct", destination: ChoiceQuizView(multiple: true, correct: false, notCorrect: true))
            }
        }
    }
}
