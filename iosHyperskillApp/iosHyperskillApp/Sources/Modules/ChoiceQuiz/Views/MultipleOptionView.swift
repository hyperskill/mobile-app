import SwiftUI

struct MultipleOptionView: View {
    var option: QuizOption
    @Binding var chosenValues: Set<Int>

    var body: some View {
        //todo checkbox
        Button(action: {}, label: { Text("Checkbox") })
    }
}

struct MultipleOptionView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            MultipleOptionView(option: ChoiceView.options[0], chosenValues: .constant([0]))
            MultipleOptionView(option: ChoiceView.options[0], chosenValues: .constant([1]))
        }
    }
}
