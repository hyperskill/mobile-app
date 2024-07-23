import Foundation
import shared

protocol TopicCompletedModalOutputProtocol: AnyObject {
    func topicCompletedModalDidRequestGoToStudyPlan()
    func topicCompletedModalDidRequestContinueWithNextTopic()
    func topicCompletedModalDidRequestPaywall(paywallTransitionSource: PaywallTransitionSource)
}
