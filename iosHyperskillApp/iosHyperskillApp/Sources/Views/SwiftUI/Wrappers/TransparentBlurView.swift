import SwiftUI
import UIKit

struct TransparentBlurView: UIViewRepresentable {
    func makeUIView(context: Context) -> UIVisualEffectView {
        UIVisualEffectView(effect: UIBlurEffect(style: .systemUltraThinMaterial))
    }

    func updateUIView(_ uiView: UIVisualEffectView, context: Context) {
        DispatchQueue.main.async {
            guard let backdropLayer = uiView.layer.sublayers?.first else {
                return
            }

            backdropLayer.filters?.removeAll { filter in
                String(describing: filter) != "gaussianBlur"
            }
        }
    }
}

#Preview {
    TransparentBlurView()
        .padding()
}
