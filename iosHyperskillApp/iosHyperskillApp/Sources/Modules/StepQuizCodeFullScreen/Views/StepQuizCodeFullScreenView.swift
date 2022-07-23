import SwiftUI

struct StepQuizCodeFullScreenView: View {
    @StateObject private var viewModel: StepQuizCodeFullScreenViewModel

    @State private var selectedTab: StepQuizCodeFullScreenTab

    @State private var code: String?
    @State private var isEditingCode = false

    @Environment(\.presentationMode) private var presentationMode

    init(
        viewModel: StepQuizCodeFullScreenViewModel,
        initialTab: StepQuizCodeFullScreenTab = .code
    ) {
        self._viewModel = StateObject(wrappedValue: viewModel)
        self._selectedTab = State(initialValue: initialTab)
        self._code = State(initialValue: viewModel.codeQuizViewData.code)
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

                let viewData = viewModel.codeQuizViewData

                TabView(selection: $selectedTab) {
                    TabNavigationLazyView(
                        StepQuizCodeFullScreenDetailsView(
                            stepStats: viewData.stepStats,
                            stepText: viewData.stepText,
                            samples: viewData.samples,
                            executionTimeLimit: viewData.executionTimeLimit,
                            executionMemoryLimit: viewData.executionMemoryLimit
                        )
                    )
                    .tag(StepQuizCodeFullScreenTab.details)

                    StepQuizCodeFullScreenCodeView(
                        code: $code.onChange(viewModel.doCodeUpdate(code:)),
                        codeTemplate: viewData.codeTemplate,
                        language: viewData.language,
                        isActionButtonsVisible: !isEditingCode,
                        onDidBeginEditingCode: {
                            withAnimation {
                                isEditingCode = true
                            }
                        },
                        onDidEndEditingCode: {
                            withAnimation {
                                isEditingCode = false
                            }
                        },
                        onTapRetry: viewModel.doRetry,
                        onTapRunCode: viewModel.doRunCode
                    )
                    .tag(StepQuizCodeFullScreenTab.code)
                }
                .tabViewStyle(.page(indexDisplayMode: .never))
            }
            .navigationBarTitleDisplayMode(.inline)
            .navigationTitle(Strings.StepQuizCode.title)
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
