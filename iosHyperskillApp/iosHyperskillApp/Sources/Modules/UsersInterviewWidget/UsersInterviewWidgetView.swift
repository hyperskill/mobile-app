import SwiftUI

extension UsersInterviewWidgetView {
    struct Appearance {
        let skeletonHeight: CGFloat = 114

        let illustrationSize = CGSize(width: 89, height: 86)

        let spacing = LayoutInsets.defaultInset
        let interitemSpacing = LayoutInsets.smallInset
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
                    ZStack(alignment: Alignment(horizontal: .trailing, vertical: .top)) {
                        HStack(alignment: .center, spacing: appearance.spacing) {
                            textConent
                            illustration
                        }
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .padding([.leading, .vertical])

                        closeButton
                    }
                    .foregroundColor(.white)
                    .background(backgroundGradient)
                }
            )
            .buttonStyle(BounceButtonStyle())
        }
    }

    private var textConent: some View {
        VStack(alignment: .leading, spacing: appearance.interitemSpacing) {
            Text(Strings.UsersInterviewWidget.title)
                .font(.headline)
            Text(Strings.UsersInterviewWidget.subtitle)
                .font(.subheadline)
        }
    }

    private var illustration: some View {
        Image(.usersInterviewWidgetIllustration)
            .renderingMode(.original)
            .resizable()
            .aspectRatio(contentMode: .fit)
            .frame(size: appearance.illustrationSize)
    }

    private var closeButton: some View {
        Button(
            action: {
                withAnimation {
                    viewModel.doCloseAction()
                }
            },
            label: {
                Image(systemName: "xmark.circle.fill")
                    .padding(.all, appearance.interitemSpacing)
            }
        )
    }

    private var backgroundGradient: some View {
        Image(.usersInterviewWidgetGradient)
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
