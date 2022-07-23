import SwiftUI

struct StepQuizCodeFullScreenView: View {
    private let codeQuizViewData: StepQuizCodeViewData

    @State private var selectedTab: StepQuizCodeFullScreenTab

    @State private var code: String?

    @State private var isNavigationBarHidden = false

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
        NavigationView {
            VStack(spacing: 0) {
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

                    StepQuizCodeFullScreenCodeView(
                        code: $code,
                        codeTemplate: codeQuizViewData.codeTemplate,
                        language: codeQuizViewData.language,
                        onDidBeginEditingCode: {
                            isNavigationBarHidden = true
                        },
                        onDidEndEditingCode: {
                            isNavigationBarHidden = false
                        }
                    )
                    .tag(StepQuizCodeFullScreenTab.code)
                }
                .tabViewStyle(.page(indexDisplayMode: .never))
            }
            .navigationBarTitleDisplayMode(.inline)
            .navigationTitle(Strings.StepQuizCode.title)
            .navigationBarHidden(isNavigationBarHidden)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button(
                        action: { presentationMode.wrappedValue.dismiss() },
                        label: { Image(systemName: "xmark") }
                    )
                }

                ToolbarItem(placement: .navigationBarTrailing) {
                    Button(
                        action: {},
                        label: { Image(systemName: "ellipsis") }
                    )
                }
            }
        }
        .navigationViewStyle(StackNavigationViewStyle())
        .animation(.default, value: isNavigationBarHidden)
        .onAppear {
            KeyboardManager.setEnabled(false)
        }
        .onDisappear {
            KeyboardManager.setEnabled(true)
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
