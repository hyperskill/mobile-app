import SwiftUI

struct FeedbackGeneratorPreviewView: View {
    var body: some View {
        VStack(spacing: LayoutInsets.defaultInset * 2) {
            VStack(spacing: LayoutInsets.smallInset) {
                Button("Notification - Error") {
                    FeedbackGenerator(feedbackType: .notification(.error)).triggerFeedback()
                }
                Button("Notification - Success") {
                    FeedbackGenerator(feedbackType: .notification(.success)).triggerFeedback()
                }
                Button("Notification - Warning") {
                    FeedbackGenerator(feedbackType: .notification(.warning)).triggerFeedback()
                }
            }

            VStack(spacing: LayoutInsets.smallInset) {
                Button("Impact - Heavy") {
                    FeedbackGenerator(feedbackType: .impact(.heavy)).triggerFeedback()
                }
                Button("Impact - Light") {
                    FeedbackGenerator(feedbackType: .impact(.light)).triggerFeedback()
                }
                Button("Impact - Medium") {
                    FeedbackGenerator(feedbackType: .impact(.medium)).triggerFeedback()
                }
                Button("Impact - Rigid") {
                    FeedbackGenerator(feedbackType: .impact(.rigid)).triggerFeedback()
                }
                Button("Impact - Soft") {
                    FeedbackGenerator(feedbackType: .impact(.soft)).triggerFeedback()
                }
            }

            Button("Selection - Changed") {
                FeedbackGenerator(feedbackType: .selection).triggerFeedback()
            }
        }
        .padding()
    }
}

struct FeedbackGeneratorPreviewView_Previews: PreviewProvider {
    static var previews: some View {
        FeedbackGeneratorPreviewView()
    }
}
