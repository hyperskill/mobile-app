import Foundation
import shared

final class Formatter {
    private let resourceProvider: ResourceProvider
    private let pluralsResources: SharedResources.plurals

    init(resourceProvider: ResourceProvider, pluralsResources: SharedResources.plurals = .shared) {
        self.resourceProvider = resourceProvider
        self.pluralsResources = pluralsResources
    }

    // MARK: Count

    /// Format days count with localized and pluralized suffix; 1 -> "1 day", 5 -> "5 days"
    func daysCount(_ count: Int) -> String {
        daysCount(Int32(count))
    }

    /// Format days count with localized and pluralized suffix; 1 -> "1 day", 5 -> "5 days"
    func daysCount(_ count: Int32) -> String {
        resourceProvider.getQuantityString(
            pluralsResource: pluralsResources.days,
            quantity: count,
            args: KotlinArray(size: 1, init: { _ in NSNumber(value: count) })
        )
    }

    /// Format minutes count with localized and pluralized suffix; 1 -> "1 minute", 5 -> "5 minutes"
    func minutesCount(seconds: Int) -> String {
        let minute = 60
        return minutesCount(seconds / minute)
    }

    /// Format minutes count with localized and pluralized suffix; 1 -> "1 minute", 5 -> "5 minutes"
    func minutesCount(_ count: Int) -> String {
        minutesCount(Int32(count))
    }

    /// Format minutes count with localized and pluralized suffix; 1 -> "1 minute", 5 -> "5 minutes"
    func minutesCount(_ count: Int32) -> String {
        resourceProvider.getQuantityString(
            pluralsResource: pluralsResources.minutes,
            quantity: count,
            args: KotlinArray(size: 1, init: { _ in NSNumber(value: count) })
        )
    }

    /// Format minutes count with localized and pluralized suffix; 1 -> "1 hour", 5 -> "5 hours"
    func hoursCount(_ count: Int) -> String {
        hoursCount(Int32(count))
    }

    /// Format minutes count with localized and pluralized suffix; 1 -> "1 hour", 5 -> "5 hours"
    func hoursCount(_ count: Int32) -> String {
        resourceProvider.getQuantityString(
            pluralsResource: pluralsResources.hours,
            quantity: count,
            args: KotlinArray(size: 1, init: { _ in NSNumber(value: count) })
        )
    }

    // MARK: Locale

    func localizedCoutryName(
        for regionCode: String,
        languageCode: String = Locale.current.languageCode ?? "en"
    ) -> String? {
        Locale(identifier: languageCode + "_" + regionCode)
            .localizedString(forRegionCode: regionCode)
    }

    func localizedLanguageName(for languageCode: String) -> String? {
        Locale.current.localizedString(forLanguageCode: languageCode)
    }
}

// MARK: - Formatter (default) -

extension Formatter {
    static var `default`: Formatter {
        Formatter(resourceProvider: AppGraphBridge.sharedAppGraph.commonComponent.resourceProvider)
    }
}
