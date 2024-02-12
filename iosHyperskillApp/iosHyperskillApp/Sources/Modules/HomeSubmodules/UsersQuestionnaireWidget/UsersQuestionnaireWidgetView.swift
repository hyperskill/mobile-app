import SwiftUI

extension UsersQuestionnaireWidgetView {
    struct Appearance {
        let skeletonHeight: CGFloat = 114

        let spacing = LayoutInsets.defaultInset
    }
}

struct UsersQuestionnaireWidgetView: View {
    private(set) var appearance = Appearance()

    let stateKs: UsersQuestionnaireWidgetFeatureStateKs

    let viewModel: UsersQuestionnaireWidgetViewModel

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
                    viewModel.doCallToAction()
                },
                label: {
                    HStack(alignment: .center, spacing: 0) {
                        Text(Strings.UsersQuestionnaireWidget.title)
                            .font(.subheadline)

                        Spacer()

                        Button(
                            action: {
                                viewModel.doCloseAction()
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

#Preview {
    UsersQuestionnaireWidgetView(
        stateKs: .visible,
        viewModel: UsersQuestionnaireWidgetViewModel()
    )
    .padding(.horizontal)
}
