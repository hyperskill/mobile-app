import SwiftUI

struct StepQuizChoiceView: View {
    @ObservedObject var viewModel: StepQuizChoiceViewModel

    @Environment(\.isEnabled) private var isEnabled

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
        .conditionalOpacity(isEnabled: isEnabled)
    }
}

#if DEBUG
#Preview {
    ScrollView {
        VStack(spacing: LayoutInsets.defaultInset) {
            StepQuizChoiceAssembly
                .makePlaceholder(isMultipleChoice: false)
                .makeModule()
            Divider()

            StepQuizChoiceAssembly
                .makePlaceholder(isMultipleChoice: true)
                .makeModule()
            Divider()

            StepQuizChoiceAssembly
                .makePlaceholder(isMultipleChoice: false)
                .makeModule()
                .disabled(true)
        }
        .padding()
    }
}
#endif
