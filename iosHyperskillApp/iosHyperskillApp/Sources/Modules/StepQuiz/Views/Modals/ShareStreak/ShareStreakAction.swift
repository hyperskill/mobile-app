import Foundation
import UIKit

enum ShareStreakAction {
    static func image(for streak: Int) -> UIImage? {
        let imageName = "share-streak-day-\(streak)"
        let image = UIImage(named: imageName)

        #if DEBUG
        if image == nil {
            assertionFailure("ShareStreakAction: image for \(imageName) not found")
        }
        #endif

        return image
    }

    static func makeActivityViewController(for streak: Int) -> UIActivityViewController {
        var items: [Any] = [
            ShareStreakActivityItemSource()
        ]
        if let image = image(for: streak) {
            items.append(image)
        }

        let activityViewController = UIActivityViewController(
            activityItems: items,
            applicationActivities: nil
        )
        activityViewController.excludedActivityTypes = [.airDrop]

        if let popoverPresentationController = activityViewController.popoverPresentationController {
            popoverPresentationController.sourceView = SourcelessRouter().currentPresentedViewController()?.view
        }

        return activityViewController
    }
}

private final class ShareStreakActivityItemSource: NSObject, UIActivityItemSource {
    func activityViewControllerPlaceholderItem(_ activityViewController: UIActivityViewController) -> Any {
        let urlString = Strings.StepQuiz.ShareStreakModal.sharingURL
        return URL(string: urlString) ?? urlString
    }

    func activityViewController(
        _ activityViewController: UIActivityViewController,
        itemForActivityType activityType: UIActivity.ActivityType?
    ) -> Any? {
        guard activityType?.rawValue != "com.burbn.instagram.shareextension" else {
            return nil
        }

        return "\(Strings.StepQuiz.ShareStreakModal.sharingText)\n\(Strings.StepQuiz.ShareStreakModal.sharingURL)"
    }
}
