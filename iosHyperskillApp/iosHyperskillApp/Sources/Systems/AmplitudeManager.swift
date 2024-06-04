import AmplitudeSwift
import Foundation
import shared

final class AmplitudeManager: AmplitudeAnalyticEngine {
    static let shared = AmplitudeManager()

    private lazy var amplitude: Amplitude = {
        let logLevel: LogLevelEnum
        #if DEBUG
        logLevel = .DEBUG
        #else
        logLevel = .OFF
        #endif

        return Amplitude(
            configuration: .init(
                apiKey: BuildKonfig.companion.AMPLITUDE_DEV_KEY,
                logLevel: logLevel,
                defaultTracking: DefaultTrackingOptions(
                    sessions: true,
                    appLifecycles: true,
                    screenViews: false // This feature is not supported in SwiftUI
                )
            )
        )
    }()

    static func configure() {
        let _ = shared.amplitude
    }

    // MARK: AnalyticEngine

    override func reportEvent(
        event: AnalyticEvent,
        userProperties: AnalyticEventUserProperties,
        force: Bool,
        completionHandler: @escaping (Error?) -> Void
    ) {
        completionHandler(nil)

        guard let amplitudeAnalyticEvent = AmplitudeAnalyticEventMapper.shared.map(analyticEvent: event) else {
            return assertionFailure("AmplitudeManager: Failed to map event = \(event) to AmplitudeAnalyticEvent")
        }

        amplitude.track(
            event: BaseEvent(
                userId: userProperties.userId?.stringValue,
                eventType: amplitudeAnalyticEvent.name,
                eventProperties: amplitudeAnalyticEvent.params,
                userProperties: userProperties.properties
            )
        )
    }
}
