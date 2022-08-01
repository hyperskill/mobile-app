import SwiftUI

struct StepQuizCodeFullScreenView: View {
    @StateObject private var viewModel: StepQuizCodeFullScreenViewModel

    @State private var selectedTab: StepQuizCodeFullScreenTab

    @State private var code: String?
    @State private var isEditingCode = false

    @State private var isPresentingResetAlert = false

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
                        code: $code,
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
                    .onChange(of: code, perform: viewModel.doCodeUpdate(code:))
                    .tag(StepQuizCodeFullScreenTab.code)
                }
                .tabViewStyle(.page(indexDisplayMode: .never))
            }
            .navigationBarTitleDisplayMode(.inline)
            .navigationTitle(Strings.StepQuizCode.title)
            .toolbar(content: buildToolbarContent)
        }
        .navigationViewStyle(StackNavigationViewStyle())
        .onAppear {
            KeyboardManager.setEnabled(false)
        }
        .onDisappear {
            KeyboardManager.setEnabled(true)
        }
    }

    @ToolbarContentBuilder
    private func buildToolbarContent() -> some ToolbarContent {
        ToolbarItem(placement: .cancellationAction) {
            Button(
                action: { presentationMode.wrappedValue.dismiss() },
                label: { Image(systemName: "xmark") }
            )
        }

        ToolbarItem(placement: .navigationBarTrailing) {
            Menu(
                content: {
                    Button(Strings.StepQuizCode.reset) {
                        isPresentingResetAlert = true
                    }
                },
                label: {
                    Image(systemName: "ellipsis")
                }
            )
            .alert(isPresented: $isPresentingResetAlert) {
                Alert(
                    title: Text(Strings.StepQuizCode.resetCodeDialogTitle),
                    message: Text(Strings.StepQuizCode.resetCodeDialogExplanation),
                    primaryButton: .default(
                        Text(Strings.General.cancel)
                    ),
                    secondaryButton: .destructive(
                        Text(Strings.StepQuizCode.reset),
                        action: resetCode
                    )
                )
            }
        }
    }

    private func resetCode() {
        code = viewModel.codeQuizViewData.codeTemplate
    }
}

struct StepQuizCodeFullScreenView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizCodeFullScreenAssembly
            .makePlaceholder()
            .makeModule()
    }
}
