import SwiftUI

struct StepQuizCodeFullScreenView: View {
    private let codeQuizViewData: StepQuizCodeViewData

    @State private var selectedTab = StepQuizCodeFullScreenTabItem.code

    @State private var code: String?

    @Environment(\.presentationMode) private var presentationMode

    init(codeQuizViewData: StepQuizCodeViewData) {
        self.codeQuizViewData = codeQuizViewData
        self.code = codeQuizViewData.code
    }

    var body: some View {
        TabView(selection: $selectedTab) {
            TabNavigationLazyView(
                StepQuizCodeFullScreenDetailsView(
                    stepStats: codeQuizViewData.stepStats,
                    stepText: codeQuizViewData.stepText,
                    samples: codeQuizViewData.samples,
                    executionTimeLimit: codeQuizViewData.executionTimeLimit,
                    executionMemoryLimit: codeQuizViewData.executionMemoryLimit
                )
            )
            .tag(StepQuizCodeFullScreenTabItem.details)

            Button("Second Dismiss Modal") {
                presentationMode.wrappedValue.dismiss()
            }
            .tag(StepQuizCodeFullScreenTabItem.code)
        }
        .tabViewStyle(.page(indexDisplayMode: .never))
    }
}

struct StepQuizCodeFullScreenView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizCodeFullScreenAssembly
            .makePlaceholder()
            .makeModule()
    }
}
