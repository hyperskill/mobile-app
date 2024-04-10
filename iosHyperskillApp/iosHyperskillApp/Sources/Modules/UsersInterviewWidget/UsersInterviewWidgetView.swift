import SwiftUI

extension UsersInterviewWidgetView {
    struct Appearance {
        let skeletonHeight: CGFloat = 114

        let spacing = LayoutInsets.defaultInset
    }
}

struct UsersInterviewWidgetView: View {
    private(set) var appearance = Appearance()

    let stateKs: UsersInterviewWidgetFeatureStateKs

    let viewModel: UsersInterviewWidgetViewModel

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(
                onViewDidAppear: viewModel.logViewedEvent
            )
            buildBody()
        }
    }

    @ViewBuilder
    private func buildBody() -> some View {
        switch stateKs {
        case .idle, .loading:
            SkeletonRoundedView()
                .frame(height: appearance.skeletonHeight)
        case .hidden:
            EmptyView()
        case .visible:
            Button(
                action: {
                    withAnimation {
                        viewModel.doCallToAction()
                    }
                },
                label: {
                    HStack(alignment: .center, spacing: 0) {
                        Text(Strings.UsersQuestionnaireWidget.title)
                            .font(.subheadline)

                        Spacer()

                        Button(
                            action: {
                                withAnimation {
                                    viewModel.doCloseAction()
                                }
                            },
                            label: {
                                Image(systemName: "xmark.circle.fill")
                                    .padding(.all, appearance.spacing)
                            }
                        )
                        .offset(x: appearance.spacing, y: 0)
                    }
                    .foregroundColor(.white)
                    .frame(maxWidth: .infinity)
                    .padding(.horizontal)
                    .background(backgroundGradient)
                }
            )
            .buttonStyle(BounceButtonStyle())
        }
    }

    private var backgroundGradient: some View {
        Image(.brandGradient3)
            .renderingMode(.original)
            .resizable()
            .addBorder(color: .clear, width: 0)
    }
}

#if DEBUG
#Preview {
    UsersInterviewWidgetView(
        stateKs: .visible,
        viewModel: UsersInterviewWidgetViewModel()
    )
    .padding(.horizontal)
}
#endif
