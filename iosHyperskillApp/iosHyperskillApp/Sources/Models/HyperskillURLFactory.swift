import Foundation

enum HyperskillURLFactory {
    static let jetbrainsHost = "jetbrains.com"
    static let hiHyperskillHost = "hi.hyperskill.org"
    static let supportHyperskillHost = "support.hyperskill.org"

    // MARK: Auth

    static func makeRegister(fromMobile: Bool = true) -> URL? {
        makeURL(path: .register, queryItems: fromMobile ? [.fromMobileApp] : [])
    }

    // MARK: Profile

    static func makeProfile(id: Int) -> URL? {
        makeURL(path: .profile(id))
    }

    // MARK: Track

    static func makeTrack(id: Int) -> URL? {
        makeURL(path: .track(id))
    }

    // MARK: StudyPlan

    static func makeStudyPlan() -> URL? {
        makeURL(path: .studyPlan)
    }

    // MARK: Settings

    static func makeTermOfService() -> URL? {
        makeURL(path: .termOfService, host: jetbrainsHost)
    }

    static func makePrivacyPolicy() -> URL? {
        makeURL(path: .privacyPolicy, host: hiHyperskillHost)
    }

    static func makeHelpCenter() -> URL? {
        makeURL(path: .helpCenter, host: supportHyperskillHost)
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
        case track(Int)
        case studyPlan
        case termOfService
        case privacyPolicy
        case helpCenter

        var formattedPath: String {
            switch self {
            case .register:
                return "/register"
            case .profile(let id):
                return "/profile/\(id)"
            case .track(let id):
                return "/tracks/\(id)"
            case .studyPlan:
                return "/study-plan"
            case .termOfService:
                return "/legal/terms/jetbrains-academy.html"
            case .privacyPolicy:
                return "/terms"
            case .helpCenter:
                return "/hc/en-us"
            }
        }
    }
}
