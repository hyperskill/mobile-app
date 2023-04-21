import SwiftUI

struct PlaceholderView: View {
    var configuration: Configuration

    var body: some View {
        ZStack {
            switch configuration.presentationMode {
            case .local:
                buildContent()
            case .fullscreen:
                GeometryReader { geometryProxy in
                    buildContent(
                        containerSize: geometryProxy.size
                    )
                    .frame(width: geometryProxy.size.width, height: geometryProxy.size.height)
                }
            }
        }
        .padding(configuration.padding.edges, configuration.padding.length)
        .background(configuration.backgroundColor)
    }

    // MARK: Private API

    @ViewBuilder
    private func buildContent(containerSize: CGSize = .zero) -> some View {
        VStack(spacing: configuration.interItemSpacing) {
            switch configuration.primaryContentAlignment {
            case .vertical:
                buildPrimaryContent(containerSize: containerSize)
            case .horizontal(let spacing, let containerAlignment):
                HStack(spacing: spacing) {
                    buildPrimaryContent(containerSize: containerSize)
                }
                .frame(maxWidth: .infinity, alignment: containerAlignment)
            }

            if let button = configuration.button, !button.text.isEmpty {
                switch button.style {
                case .outline(let buttonStyle):
                    Button(button.text, action: button.action)
                        .buttonStyle(buttonStyle)
                case .roundedRectangle(let buttonStyle):
                    Button(button.text, action: button.action)
                        .buttonStyle(buttonStyle)
                }
            }
        }
    }

    @ViewBuilder
    private func buildPrimaryContent(containerSize: CGSize) -> some View {
        if let image = configuration.image,
           let uiImage = UIImage(named: image.name) {
            let size = calculateImageSize(image: image, in: containerSize)
            Image(uiImage: uiImage)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(width: size.width, height: size.height)
        }

        if let title = configuration.title, !title.text.isEmpty {
            Text(title.text)
                .font(title.font)
                .foregroundColor(title.foregroundColor)
        }
    }

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
        var presentationMode: PresentationMode = .fullscreen

        var image: Image?

        var title: Title?

        var button: Button?

        var primaryContentAlignment = PrimaryContentAlignment.vertical

        var interItemSpacing: CGFloat = 28

        var padding = Padding()

        var backgroundColor = Color(ColorPalette.surface)

        enum PresentationMode {
            case local
            case fullscreen
        }

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

            var style: Style = .roundedRectangle()

            enum Style {
                case outline(style: OutlineButtonStyle = OutlineButtonStyle(style: .violet))
                case roundedRectangle(style: RoundedRectangleButtonStyle = RoundedRectangleButtonStyle(style: .violet))
            }
        }

        struct Padding {
            var edges = Edge.Set.all
            var length: CGFloat?
        }

        enum PrimaryContentAlignment {
            case vertical
            case horizontal(
                spacing: CGFloat? = LayoutInsets.defaultInset,
                containerAlignment: Alignment = Alignment.leading
            )
        }
    }
}

struct PlaceholderView_Previews: PreviewProvider {
    static var previews: some View {
        PlaceholderView(configuration: .networkError(action: {}))

        PlaceholderView(configuration: .reloadContent(action: {}))
        PlaceholderView(configuration: .reloadContent(action: {}))
            .preferredColorScheme(.dark)
    }
}
