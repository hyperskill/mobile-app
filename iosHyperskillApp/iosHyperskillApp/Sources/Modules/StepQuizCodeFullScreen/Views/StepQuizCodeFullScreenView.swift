import SwiftUI

struct StepQuizCodeFullScreenView: View {
    private let codeQuizViewData: StepQuizCodeViewData

    @State private var selectedTab: StepQuizCodeFullScreenTab

    @State private var code: String?

    @Environment(\.presentationMode) private var presentationMode

    init(
        codeQuizViewData: StepQuizCodeViewData,
        initialTab: StepQuizCodeFullScreenTab = .code
    ) {
        self.codeQuizViewData = codeQuizViewData
        self._selectedTab = State(initialValue: initialTab)
        self._code = State(initialValue: codeQuizViewData.code)
    }

    var body: some View {
        VStack {
            HSTabBar(
                titles: StepQuizCodeFullScreenTab.allCases.map(\.title),
                selectedTabIndex: Binding(
                    get: { selectedTab.rawValue },
                    set: { selectedTab = StepQuizCodeFullScreenTab.allCases[$0] }
                )
            )
            .background(BackgroundView())

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
                .tag(StepQuizCodeFullScreenTab.details)

                Button("Second Dismiss Modal") {
                    presentationMode.wrappedValue.dismiss()
                }
                .tag(StepQuizCodeFullScreenTab.code)
            }
            .tabViewStyle(.page(indexDisplayMode: .never))
        }
    }
}

struct StepQuizCodeFullScreenView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizCodeFullScreenAssembly
            .makePlaceholder()
            .makeModule()
    }
}
