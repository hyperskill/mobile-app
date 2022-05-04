import SwiftUI

struct MultipleOptionChoiceView: View {
    var options: [QuizOption]

    @State private var chosenValues: Set<Int> = []

    var body: some View {
        VStack(alignment: .leading, spacing: 20) {
            ForEach(options, id: \.value) { option in
                MultipleOptionView(option: option, chosenValues: $chosenValues)
            }
        }
    }
}

struct MultipleOptionChoiceView_Previews: PreviewProvider {
    static var previews: some View {
        MultipleOptionChoiceView(options: ChoiceView.options)
    }
}
