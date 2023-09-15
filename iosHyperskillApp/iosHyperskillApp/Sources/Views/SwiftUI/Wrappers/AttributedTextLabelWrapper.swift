import Foundation
import SwiftUI
import UIKit

struct AttributedTextLabelWrapper: UIViewRepresentable {
    let attributedText: NSAttributedString

    func makeUIView(context: Context) -> UILabel {
        let label = UILabel()
        label.attributedText = attributedText
        return label
    }

    func updateUIView(_ uiView: UILabel, context: Context) {
        if uiView.attributedText != attributedText {
            uiView.attributedText = attributedText
        }
    }
}
