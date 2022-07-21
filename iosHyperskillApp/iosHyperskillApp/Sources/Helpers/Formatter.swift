import Foundation
import shared

final class Formatter {
    private let resourceProvider: ResourceProvider
    private let pluralsResources: SharedResources.plurals

    init(resourceProvider: ResourceProvider, pluralsResources: SharedResources.plurals = .shared) {
        self.resourceProvider = resourceProvider
        self.pluralsResources = pluralsResources
    }

    // MARK: Numbers

    /// Format floating point rating with 2 decimal points; 0.123456 -> "0.12"
    static func averageRating(_ number: Double, decimalPoints: Int = 2) -> String {
        String(format: "%.\(decimalPoints)f", number)
    }

    /// Format floating point rating with 2 decimal points; 0.123456 -> "0.12"
    static func averageRating(_ number: Float, decimalPoints: Int = 2) -> String {
        String(format: "%.\(decimalPoints)f", number)
    }

    // MARK: Count

    /// Format projects count with localized and pluralized suffix; 1 -> "1 project", 5 -> "5 projects"
    func projectsCount(_ count: Int) -> String { projectsCount(Int32(count)) }

    /// Format projects count with localized and pluralized suffix; 1 -> "1 project", 5 -> "5 projects"
    func projectsCount(_ count: Int32) -> String {
        resourceProvider.getQuantityString(
            pluralsResource: pluralsResources.projects,
            quantity: count,
            args: KotlinArray(size: 1, init: { _ in NSNumber(value: count) })
        )
    }

    /// Format topics count with localized and pluralized suffix; 1 -> "1 topic", 5 -> "5 topics"
    func topicsCount(_ count: Int) -> String { topicsCount(Int32(count)) }

    /// Format topics count with localized and pluralized suffix; 1 -> "1 topic", 5 -> "5 topics"
    func topicsCount(_ count: Int32) -> String {
        resourceProvider.getQuantityString(
            pluralsResource: pluralsResources.topics,
            quantity: count,
            args: KotlinArray(size: 1, init: { _ in NSNumber(value: count) })
        )
    }

    // MARK: Date

    /// Format days count with localized and pluralized suffix; 1 -> "1 day", 5 -> "5 days"
    func daysCount(_ count: Int) -> String { daysCount(Int32(count)) }

    /// Format days count with localized and pluralized suffix; 1 -> "1 day", 5 -> "5 days"
    func daysCount(_ count: Int32) -> String {
        resourceProvider.getQuantityString(
            pluralsResource: pluralsResources.days,
            quantity: count,
            args: KotlinArray(size: 1, init: { _ in NSNumber(value: count) })
        )
    }

    /// Format hours count with localized and pluralized suffix; 1 -> "1 hour", 5 -> "5 hours"
    func hoursInSeconds(_ seconds: TimeInterval, roundingRule: FloatingPointRoundingRule = .up) -> String {
        let hours = UnitConverters.Hour.from(seconds: seconds, roundingRule: roundingRule)

        return resourceProvider.getQuantityString(
            pluralsResource: pluralsResources.hours,
            quantity: Int32(hours),
            args: KotlinArray(size: 1, init: { _ in NSNumber(value: hours) })
        )
    }

    /// Format minutes count with localized and pluralized suffix; 1 -> "1 minute", 5 -> "5 minutes"
    func minutesCount(_ count: Int) -> String { minutesCount(Int32(count)) }

    /// Format minutes count with localized and pluralized suffix; 1 -> "1 minute", 5 -> "5 minutes"
    func minutesCount(_ count: Int32) -> String {
        resourceProvider.getQuantityString(
            pluralsResource: pluralsResources.minutes,
            quantity: count,
            args: KotlinArray(size: 1, init: { _ in NSNumber(value: count) })
        )
    }

    // MARK: Locale

    static func localizedCoutryName(
        for regionCode: String,
        languageCode: String = Locale.current.languageCode ?? "en"
    ) -> String? {
        Locale(identifier: languageCode + "_" + regionCode)
            .localizedString(forRegionCode: regionCode)
    }

    static func localizedLanguageName(for languageCode: String) -> String? {
        Locale.current.localizedString(forLanguageCode: languageCode)
    }
}

// MARK: - Formatter (default) -

extension Formatter {
    static var `default`: Formatter {
        Formatter(resourceProvider: AppGraphBridge.sharedAppGraph.commonComponent.resourceProvider)
    }
}
