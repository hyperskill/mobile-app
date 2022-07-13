import shared
import SwiftUI

extension HomeView {
    struct Appearance {
        let spacing: CGFloat = 20
    }
}

struct HomeView: View {
    private(set) var appearance = Appearance()

    private let formatter: Formatter

    @ObservedObject private var viewModel: HomeViewModel

    init(formatter: Formatter, viewModel: HomeViewModel) {
        self.viewModel = viewModel
        self.formatter = formatter
        self.viewModel.onViewAction = self.handleViewAction(_:)
    }

    var body: some View {
        VStack(alignment: .leading, spacing: appearance.spacing) {
            Text(Strings.Home.helloLetsLearn)
                .font(.title)
                .foregroundColor(.primaryText)

            Text(Strings.Home.keepPracticing)
                .font(.subheadline)
                .foregroundColor(.secondaryText)

            buildBody()

            Spacer()
        }
        .onAppear(perform: viewModel.startListening)
        .onDisappear(perform: viewModel.stopListening)
        .padding()
        .background(Color(ColorPalette.background).ignoresSafeArea())
    }

    // MARK: Private API

    @ViewBuilder
    private func buildBody() -> some View {
        switch viewModel.state {
        case is HomeFeatureStateIdle:
            ProgressView()
                .onAppear {
                    viewModel.loadContent()
                }
                .frame(maxWidth: .infinity, alignment: .center)
        case is HomeFeatureStateLoading:
            ProgressView()
                .frame(maxWidth: .infinity, alignment: .center)
        case is HomeFeatureStateNetworkError:
            PlaceholderView(
                configuration: .networkError(backgroundColor: Color(ColorPalette.background)) {
                    viewModel.loadContent(forceUpdate: true)
                }
            )
        case let data as HomeFeatureStateContent:
            buildContent(data: data)
        default:
            Text("Unkwown state")
        }
    }

    @ViewBuilder
    private func buildContent(data: HomeFeatureStateContent) -> some View {
        VStack(spacing: appearance.spacing) {
            if let streak = data.streak {
                StreakViewBuilder(streak: streak, viewType: .card).build()
            }

            if data.problemOfDayState as? HomeFeatureProblemOfDayStateSolved != nil {
                ProblemOfDayCardView(state: .completed, timeToSolve: nil, nextProblemIn: self.nextProblemIn())
            } else if data.problemOfDayState as? HomeFeatureProblemOfDayStateEmpty != nil {
                ProblemOfDayCardView(state: .unavailable, timeToSolve: nil, nextProblemIn: nil)
            } else if let problem = data.problemOfDayState as? HomeFeatureProblemOfDayStateNeedToSolve,
            let secondsToComplete = problem.step.secondsToComplete {
                ProblemOfDayCardView(
                    state: .uncompleted,
                    timeToSolve: formatter.minutesCount(seconds: Int(truncating: secondsToComplete)),
                    nextProblemIn: nil
                )
            }
        }
    }

    private func handleViewAction(_ viewAction: HomeFeatureActionViewAction) {
        print("HomeView :: \(#function) viewAction = \(viewAction)")
    }

    // TODO: нужно ли эту функцию куда-то вынести?
    private func nextProblemIn() -> String {
        guard let timezoneNewYork = TimeZone(identifier: "America/New_York"),
              let timezoneUtc = TimeZone(identifier: "UTC") else {
            return ""
        }

        var calendar = Calendar.current

        calendar.timeZone = timezoneUtc

        let todayNewYork = Date().convertToTimeZone(
            initTimeZone: timezoneUtc,
            timeZone: timezoneNewYork
        )


        let midnight = calendar.startOfDay(for: Date())
        let tomorrow = calendar.date(byAdding: .day, value: 1, to: midnight)

        if let tomorrow = tomorrow {
            let tomorrowEpoch = tomorrow.timeIntervalSince1970

            let diff = Int(tomorrowEpoch - todayNewYork.timeIntervalSince1970)

            let hours = diff / 3600
            let minutes = (diff - hours * 3600) / 60
            return "\(hours) \(formatter.hoursCount(hours)) \(minutes) \(formatter.hoursCount(minutes))"
        } else {
            return ""
        }
    }
}

struct HomeView_Previews: PreviewProvider {
    static var previews: some View {
        HomeAssembly().makeModule()
    }
}
