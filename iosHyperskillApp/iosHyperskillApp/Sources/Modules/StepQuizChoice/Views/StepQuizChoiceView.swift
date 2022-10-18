import SwiftUI

struct StepQuizChoiceView: View {
    @ObservedObject var viewModel: StepQuizChoiceViewModel

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
            ForEach(Array(viewModel.viewData.choices.enumerated()), id: \.offset) { index, choice in
                StepQuizChoiceElementView(
                    isSelected: choice.isSelected,
                    text: choice.text,
                    isMultipleChoice: viewModel.viewData.isMultipleChoice,
                    onTap: { viewModel.doChoiceSelection(at: index) }
                )
            }
        }
    }
}

#if DEBUG
struct StepQuizChoiceView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StepQuizChoiceAssembly
                .makePlaceholder(isMultipleChoice: false)
                .makeModule()

            StepQuizChoiceAssembly
                .makePlaceholder(isMultipleChoice: true)
                .makeModule()
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
#endif
