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
                    FeedbackGenerator(feedbackType: .impact(style: .heavy)).triggerFeedback()
                }
                Button("Impact - Light") {
                    FeedbackGenerator(feedbackType: .impact(style: .light)).triggerFeedback()
                }
                Button("Impact - Medium") {
                    FeedbackGenerator(feedbackType: .impact(style: .medium)).triggerFeedback()
                }
                Button("Impact - Rigid") {
                    FeedbackGenerator(feedbackType: .impact(style: .rigid)).triggerFeedback()
                }
                Button("Impact - Soft") {
                    FeedbackGenerator(feedbackType: .impact(style: .soft)).triggerFeedback()
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
