import SwiftUI

struct PlaceholderView: View {
    var configuration: Configuration

    var body: some View {
        GeometryReader { geometryProxy in
            VStack(spacing: configuration.interItemSpacing) {
                if let image = configuration.image,
                   let uiImage = UIImage(named: image.name) {
                    let size = calculateImageSize(image: image, in: geometryProxy.size)
                    Image(uiImage: uiImage)
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: size.width, height: size.height)
                }

                if let title = configuration.title,
                   case let text = title.text.trimmed(),
                   !text.isEmpty {
                    Text(text)
                        .font(title.font)
                        .foregroundColor(title.foregroundColor)
                }

                if let button = configuration.button,
                   case let text = button.text.trimmed(),
                   !text.isEmpty {
                    Button(text, action: button.action)
                        .buttonStyle(button.style)
                }
            }
            .frame(width: geometryProxy.size.width, height: geometryProxy.size.height)
        }
        .padding(configuration.padding.edges, configuration.padding.length)
        .background(configuration.backgroundColor)
    }

    // MARK: Private API

    private func calculateImageSize(image: Configuration.Image, in containerSize: CGSize) -> CGSize {
        switch image.frame {
        case .size(let size):
            return size
        case .scale(let scale):
            let width = containerSize.width * scale
            let height = containerSize.height * scale

            let resultSizeValue = min(width, height)

            return CGSize(width: resultSizeValue, height: resultSizeValue)
        }
    }

    // MARK: Inner Types

    struct Configuration {
        var image: Image?

        var title: Title?

        var button: Button?

        var interItemSpacing: CGFloat = 28

        var padding = Padding()

        var backgroundColor = Color(ColorPalette.surface)

        struct Image {
            var name: String

            var frame: Frame

            enum Frame {
                case size(CGSize)
                case scale(CGFloat)
            }
        }

        struct Title {
            var text: String

            var font = Font.body
            var foregroundColor = Color.primaryText
        }

        struct Button {
            var text: String

            var action: (() -> Void)

            var style = RoundedRectangleButtonStyle(style: .violet)
        }

        struct Padding {
            var edges = Edge.Set.all
            var length: CGFloat?
        }
    }
}

struct PlaceholderView_Previews: PreviewProvider {
    static var previews: some View {
        PlaceholderView(configuration: .networkError(action: {}))
    }
}
