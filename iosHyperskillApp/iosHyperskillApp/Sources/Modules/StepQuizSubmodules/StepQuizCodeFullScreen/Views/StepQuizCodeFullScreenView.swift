import SwiftUI

struct StepQuizCodeFullScreenView: View {
    @StateObject var viewModel: StepQuizCodeFullScreenViewModel

    @State var selectedTab: StepQuizCodeFullScreenTab

    let navigationTitle: String

    @State private var isEditingCode = false

    @Environment(\.presentationMode) private var presentationMode

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
                            stepText: viewData.stepText,
                            samples: viewData.samples,
                            executionTimeLimit: viewData.executionTimeLimit,
                            executionMemoryLimit: viewData.executionMemoryLimit
                        )
                    )
                    .tag(StepQuizCodeFullScreenTab.details)

                    StepQuizCodeFullScreenCodeView(
                        code: .init(
                            get: { viewModel.codeQuizViewData.code },
                            set: { viewModel.codeQuizViewData.code = $0 }
                        ),
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
                    .onChange(of: viewModel.codeQuizViewData.code, perform: viewModel.doCodeUpdate(code:))
                    .tag(StepQuizCodeFullScreenTab.code)
                }
                .tabViewStyle(.page(indexDisplayMode: .never))
            }
            .navigationBarTitleDisplayMode(.inline)
            .navigationTitle(navigationTitle)
            .toolbar(content: buildToolbarContent)
        }
        .navigationViewStyle(StackNavigationViewStyle())
        .onAppear {
            viewModel.doProvideModuleInput()
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
                    Button(
                        action: viewModel.doRetry,
                        label: {
                            Label(Strings.StepQuizCode.reset, systemImage: "gobackward")
                        }
                    )
                },
                label: {
                    Image(systemName: "ellipsis")
                }
            )
        }
    }
}

#if DEBUG
struct StepQuizCodeFullScreenView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizCodeFullScreenAssembly
            .makePlaceholder()
            .makeModule()
    }
}
#endif
