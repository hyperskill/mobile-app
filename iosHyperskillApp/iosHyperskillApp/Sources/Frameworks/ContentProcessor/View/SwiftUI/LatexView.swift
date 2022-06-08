import SwiftUI

struct LatexView: View {
    @Binding var text: String?

    var configuration = SwiftUIProcessedContentView.Configuration()

    var onContentLoaded: (() -> Void)?

    @State private var height: CGFloat = 5

    var body: some View {
        SwiftUIProcessedContentView(
            text: $text,
            configuration: configuration,
            onContentLoaded: onContentLoaded,
            onHeightUpdated: { newHeight in
                height = CGFloat(newHeight)
            },
            onOpenImageURL: { _ in },
            onOpenLink: { _ in }
        )
        .frame(height: height)
        .frame(maxWidth: .infinity)
    }
}

struct LatexView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            LatexView(text: .constant("Plain text"))

            LatexView(text: .constant("Rich <b>text</b>!!!"))
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
