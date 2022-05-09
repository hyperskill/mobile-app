import SwiftUI

struct ChoiceView: View {
    var multiple: Bool

    static var options = [
        QuizOption(value: 0, desc: "char[] characters = new char[0];"),
        QuizOption(value: 1, desc: "char[] characters = new char[1];"),
        QuizOption(value: 2, desc: "char[] characters = new char[555];"),
        QuizOption(value: 3, desc: "char[] characters = new char[-5];")
    ]

    var body: some View {
        VStack(alignment: .leading) {
            VStack(alignment: .leading, spacing: 16) {
                Text(
                    multiple ?
                            Strings.choiceQuizMultipleOptionTaskText :
                            Strings.choiceQuizSingleOptionTaskText
                )
                .font(.caption)
                .foregroundColor(Color(ColorPalette.onSurfaceAlpha38))

                Divider()
            }.padding(.bottom, 20)

            if multiple {
                MultipleOptionChoiceView(options: ChoiceView.options)
            } else {
                SingleOptionChoiceView(options: ChoiceView.options)
            }
        }
    }
}

struct ChoiceView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            ChoiceView(multiple: true)
            ChoiceView(multiple: false)
        }
    }
}
