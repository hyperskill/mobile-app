import SwiftUI

struct StepQuizFillBlanksView: View {
    @StateObject var viewModel: StepQuizFillBlanksViewModel

    @Environment(\.isEnabled) private var isEnabled

    var body: some View {
        VStack(spacing: 0) {
            FillBlanksQuizViewWrapper(
                components: viewModel.viewData.components,
                options: viewModel.viewData.options,
                isUserInteractionEnabled: isEnabled,
                onInputDidChange: viewModel.doInputTextUpdate(_:for:),
                onDidSelectComponent: viewModel.doSelectComponent(at:),
                onDidDeselectComponent: viewModel.doDeselectComponent(at:)
            )

            if viewModel.mode == .select {
                StepQuizFillBlanksSelectOptionsViewWrapper(
                    moduleOutput: viewModel,
                    moduleInput: { [weak viewModel] moduleInput in
                        viewModel?.selectOptionsModuleInput = moduleInput
                    },
                    isUserInteractionEnabled: isEnabled
                )
            }
        }
        .onAppear {
            viewModel.doProvideModuleInput()
            KeyboardManager.setEnableAutoToolbar(true)
        }
        .onDisappear {
            KeyboardManager.setEnableAutoToolbar(false)
        }
        .conditionalOpacity(isEnabled: isEnabled)
    }
}
