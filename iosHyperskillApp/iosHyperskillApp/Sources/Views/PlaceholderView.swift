import SwiftUI

struct PlaceholderView: View {
    var type: PlaceholderType

    var onClick: (() -> Void)

    var body: some View {
        VStack(spacing: 28) {
            Spacer()
            Image(type.image)
            Text(type.title)
            Button(type.buttonText, action: { onClick() })
                .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
            Spacer()
        }
    }
}

enum PlaceholderType {
    case networkError

    var image: String {
        switch self {
        case .networkError:
            return "placeholder-network-error"
        }
    }

    var title: String {
        switch self {
        case .networkError:
            return Strings.Placeholder.networkErrorTitle
        }
    }

    var buttonText: String {
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
