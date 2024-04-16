import SwiftUI

extension StepQuizMatchingView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset
        let itemHorizontalInset: CGFloat = 64
    }
}

struct StepQuizMatchingView: View {
    private(set) var appearance = Appearance()

    @ObservedObject var viewModel: StepQuizMatchingViewModel

    @Environment(\.isEnabled) private var isEnabled

    var body: some View {
        VStack(alignment: .leading, spacing: appearance.spacing) {
            ForEach(Array(viewModel.viewData.items.enumerated()), id: \.element.id) { index, item in
                switch item {
                case .title(let text):
                    HStack(spacing: 0) {
                        LatexView(
                            text: text,
                            configuration: .quizContent()
                        )
                        .padding()
                        .addBorder()

                        Spacer(minLength: appearance.itemHorizontalInset)
                    }
                case .option(let option):
                    HStack(spacing: 0) {
                        Spacer(minLength: appearance.itemHorizontalInset)

                        StepQuizSortingItemView(
                            text: option.text,
                            isMoveUpEnabled: index > 1,
                            isMoveDownEnabled: index < viewModel.viewData.items.count - 1,
                            onMoveUp: {
                                withAnimation {
                                    viewModel.doMoveUp(from: index)
                                }
                            },
                            onMoveDown: {
                                withAnimation {
                                    viewModel.doMoveDown(from: index)
                                }
                            }
                        )
                    }
                }
            }
        }
        .conditionalOpacity(isEnabled: isEnabled)
    }
}

#if DEBUG
#Preview {
    StepQuizMatchingAssembly
        .makePlaceholder()
        .makeModule()
        .padding()
}

#Preview("Disabled") {
    StepQuizMatchingAssembly
        .makePlaceholder()
        .makeModule()
        .padding()
        .disabled(true)
}
#endif
