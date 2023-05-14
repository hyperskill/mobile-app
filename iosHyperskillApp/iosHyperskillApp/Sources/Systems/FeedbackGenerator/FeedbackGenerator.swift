import UIKit

final class FeedbackGenerator {
    private let feedbackType: FeedbackType
    private var feedbackGenerator: UIFeedbackGenerator?

    init(feedbackType: FeedbackType) {
        self.feedbackType = feedbackType
    }

    // MARK: Public API

    @MainActor
    func prepare() {
        getFeedbackGenerator().prepare()
    }

    @MainActor
    func triggerFeedback() {
        switch feedbackType {
        case .notification(let style):
            guard let notificationFeedbackGenerator = getFeedbackGenerator() as? UINotificationFeedbackGenerator else {
                return assertionFailure("FeedbackGenerator :: Wrong feedback generator type")
            }

            switch style {
            case .error:
                notificationFeedbackGenerator.notificationOccurred(.error)
            case .success:
                notificationFeedbackGenerator.notificationOccurred(.success)
            case .warning:
                notificationFeedbackGenerator.notificationOccurred(.warning)
            }
        case .impact:
            guard let impactFeedbackGenerator = getFeedbackGenerator() as? UIImpactFeedbackGenerator else {
                return assertionFailure("FeedbackGenerator :: Wrong feedback generator type")
            }

            impactFeedbackGenerator.impactOccurred()
        case .selection:
            guard let selectionFeedbackGenerator = getFeedbackGenerator() as? UISelectionFeedbackGenerator else {
                return assertionFailure("FeedbackGenerator :: Wrong feedback generator type")
            }

            selectionFeedbackGenerator.selectionChanged()
        }
    }

    // MARK: Private API

    private func getFeedbackGenerator() -> UIFeedbackGenerator {
        if let feedbackGenerator {
            return feedbackGenerator
        } else {
            let concreteFeedbackGenerator: UIFeedbackGenerator

            switch feedbackType {
            case .notification:
                concreteFeedbackGenerator = UINotificationFeedbackGenerator()
            case .impact(let style):
                switch style {
                case .heavy:
                    concreteFeedbackGenerator = UIImpactFeedbackGenerator(style: .heavy)
                case .light:
                    concreteFeedbackGenerator = UIImpactFeedbackGenerator(style: .light)
                case .medium:
                    concreteFeedbackGenerator = UIImpactFeedbackGenerator(style: .medium)
                case .rigid:
                    concreteFeedbackGenerator = UIImpactFeedbackGenerator(style: .rigid)
                case .soft:
                    concreteFeedbackGenerator = UIImpactFeedbackGenerator(style: .soft)
                }
            case .selection:
                concreteFeedbackGenerator = UISelectionFeedbackGenerator()
            }

            feedbackGenerator = concreteFeedbackGenerator

            return concreteFeedbackGenerator
        }
    }

    // MARK: Inner Types

    enum FeedbackType {
        case notification(NotificationStyle)
        case impact(style: ImpactStyle)
        case selection

        enum NotificationStyle {
            case error
            case success
            case warning
        }

        enum ImpactStyle {
            case heavy
            case light
            case medium
            case rigid
            case soft
        }
    }
}
