import SwiftUI

extension StepQuizParsonsView {
    struct Appearance {
        let containerSpacing = LayoutInsets.defaultInset * 2
    }
}

struct StepQuizParsonsView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: StepQuizParsonsViewModel

    var body: some View {
        VStack(spacing: appearance.containerSpacing) {
            VStack(alignment: .leading, spacing: LayoutInsets.smallInset) {
                ForEach(Array(viewModel.viewData.lines), id: \.lineNumber) { line in
                    StepQuizParsonsItemView(
                        isSelected: line.lineNumber == viewModel.viewData.selectedLineNumber,
                        text: line.text,
                        level: line.level
                    )
                    .onTapGesture {
                        viewModel.doSelectLine(lineNumber: line.lineNumber)
                    }
                }
            }

            StepQuizParsonsControlsView(
                addTabControlConfiguration: .init(
                    action: withAnimation { viewModel.doAddTab },
                    isDisabled: viewModel.isAddTabDisabled()
                ),
                removeTabControlConfiguration: .init(
                    action: withAnimation { viewModel.doRemoveTab },
                    isDisabled: viewModel.isRemoveTabDisabled()
                ),
                downControlConfiguration: .init(
                    action: withAnimation { viewModel.doMoveDown },
                    isDisabled: viewModel.isMoveDownDisabled()
                ),
                upControlConfiguration: .init(
                    action: withAnimation { viewModel.doMoveUp },
                    isDisabled: viewModel.isMoveUpDisabled()
                )
            )
        }
    }
}

struct StepQuizParsonsView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizParsonsAssembly
            .makePlaceholder()
            .makeModule()
    }
}
