import Foundation

enum HyperskillURLFactory {
    // MARK: Auth

    static func makeRegister(fromMobile: Bool = true) -> URL? {
        makeURL(path: .register, queryItems: fromMobile ? [.fromMobileApp] : [])
    }

    // MARK: Profile

    static func makeProfile(id: Int) -> URL? {
        makeURL(path: .profile(id))
    }

    // MARK: - Private API -

    private static func makeURL(
        path: Path?,
        host: String = ApplicationInfo.host,
        queryItems: [QueryItem] = []
    ) -> URL? {
        var components = URLComponents()
        components.scheme = "https"
        components.host = host
        components.path = path?.formattedPath ?? ""

        if !queryItems.isEmpty {
            components.queryItems = queryItems.map(\.urlQueryItem)
        }

        return components.url
    }

    private enum QueryItem {
        case fromMobileApp

        var urlQueryItem: URLQueryItem {
            switch self {
            case .fromMobileApp:
                return URLQueryItem(name: "from_mobile_app", value: "true")
            }
        }
    }

    private enum Path {
        case register
        case profile(Int)

        var formattedPath: String {
            switch self {
            case .register:
                return "/register"
            case .profile(let id):
                return "/profile/\(id)"
            }
        }
    }
}
