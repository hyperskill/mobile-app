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
    }
}

#if DEBUG
struct StepQuizParsonsView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizParsonsAssembly
            .makePlaceholder()
            .makeModule()
    }
}
#endif
