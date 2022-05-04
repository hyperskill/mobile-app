import SwiftUI

struct SingleOptionChoiceView: View {
    var options: [QuizOption]

    @State private var chosenValue = -1

    var body: some View {
        VStack(alignment: .leading, spacing: 20) {
            ForEach(options, id: \.value) { option in
                SingleOptionView(option: option, chosenValue: $chosenValue)
            }
        }
    }
}

struct SingleOptionChoiceView_Previews: PreviewProvider {
    static var previews: some View {
        SingleOptionChoiceView(options: ChoiceView.options)
    }
}
