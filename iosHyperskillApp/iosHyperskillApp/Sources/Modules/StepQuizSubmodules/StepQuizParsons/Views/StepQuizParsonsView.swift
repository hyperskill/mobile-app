import SwiftUI

extension StepQuizParsonsView {
    struct Appearance {
        let containerSpacing = LayoutInsets.defaultInset
        let interItemSpacing = LayoutInsets.smallInset
    }
}

struct StepQuizParsonsView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: StepQuizParsonsViewModel

    @Environment(\.isEnabled) private var isEnabled

    var body: some View {
        VStack(spacing: appearance.containerSpacing) {
            VStack(alignment: .leading, spacing: appearance.interItemSpacing) {
                ForEach(viewModel.viewData.lines, id: \.self) { line in
                    StepQuizParsonsItemView(
                        isSelected: line.lineNumber == viewModel.viewData.selectedLineNumber,
                        code: line.code,
                        level: line.level
                    )
                    .onTapGesture {
                        viewModel.doSelectLine(lineNumber: line.lineNumber)
                    }
                }
            }
            .animation(.default, value: viewModel.viewData)

            StepQuizParsonsControlsView(
                addTabControlConfiguration: .init(
                    action: viewModel.doAddTab,
                    isDisabled: viewModel.isAddTabDisabled()
                ),
                removeTabControlConfiguration: .init(
                    action: viewModel.doRemoveTab,
                    isDisabled: viewModel.isRemoveTabDisabled()
                ),
                downControlConfiguration: .init(
                    action: viewModel.doMoveDown,
                    isDisabled: viewModel.isMoveDownDisabled()
                ),
                upControlConfiguration: .init(
                    action: viewModel.doMoveUp,
                    isDisabled: viewModel.isMoveUpDisabled()
                )
            )
        }
        .onAppear(perform: viewModel.doProvideModuleInput)
        .opacity(isEnabled ? 1 : 0.5)
        .animation(.easeInOut(duration: 0.33), value: isEnabled)
    }
}

#if DEBUG
#Preview {
    ScrollView {
        VStack(spacing: LayoutInsets.defaultInset) {
            StepQuizParsonsAssembly
                .makePlaceholder()
                .makeModule()
            Divider()

            StepQuizParsonsAssembly
                .makePlaceholder()
                .makeModule()
                .disabled(true)
        }
        .padding()
    }
}
#endif
