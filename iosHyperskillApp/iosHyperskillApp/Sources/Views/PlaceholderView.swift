import SwiftUI

struct PlaceholderView: View {
    var type: PlaceholderType

    var onClick: (() -> Void)?

    var body: some View {
        VStack(spacing: 28) {
            Image(type.image)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(width: 122, height: 122)

            Text(type.title)
                .font(.body)
                .foregroundColor(.primaryText)

            if let buttonText = type.buttonText, !buttonText.isEmpty {
                Button(buttonText, action: { onClick?() })
                    .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}

enum PlaceholderType {
    case networkError

    var image: String {
        switch self {
        case .networkError:
            return Images.Placeholder.networkError
        }
    }

    var title: String {
        switch self {
        case .networkError:
            return Strings.Placeholder.networkErrorTitle
        }
    }

    var buttonText: String? {
        switch self {
        case .networkError:
            return Strings.Placeholder.networkErrorButtonText
        }
    }
}

struct PlaceholderView_Previews: PreviewProvider {
    static var previews: some View {
        PlaceholderView(type: .networkError, onClick: {})
    }
}
