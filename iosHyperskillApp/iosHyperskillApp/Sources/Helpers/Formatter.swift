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

    /// Format topics to repeat count with localized and pluralized suffix; 1 -> "topic to repeat", 5 -> "topics to repeat"
    func topicsToRepeatTodayCount(_ count: Int) -> String { topicsToRepeatTodayCount(Int32(count)) }

    /// Format topics to repeat count with localized and pluralized suffix; 1 -> "topic to repeat", 5 -> "topics to repeat"
    func topicsToRepeatTodayCount(_ count: Int32) -> String {
        resourceProvider.getQuantityString(
            pluralsResource: pluralsResources.topics_to_repeat_today,
            quantity: count
        )
    }

    /// Format times count with localized and pluralized suffix; 1 -> "1 time", 5 -> "5 times"
    func timesCount(_ count: Int) -> String { timesCount(Int32(count)) }

    /// Format times count with localized and pluralized suffix; 1 -> "1 time", 5 -> "5 times"
    func timesCount(_ count: Int32) -> String {
        resourceProvider.getQuantityString(
            pluralsResource: pluralsResources.times,
            quantity: count,
            args: KotlinArray(size: 1, init: { _ in NSNumber(value: count) })
        )
    }

    // MARK: Date

    /// Format seconds with localized and pluralized suffix; 1 -> "1 second", 5 -> "5 seconds"
    func secondsCount(_ seconds: TimeInterval, roundingRule: FloatingPointRoundingRule = .up) -> String {
        secondsCount(Int(seconds.rounded(roundingRule)))
    }

    /// Format seconds with localized and pluralized suffix; 1 -> "1 second", 5 -> "5 seconds"
    func secondsCount(_ count: Int) -> String { secondsCount(Int32(count)) }

    /// Format seconds with localized and pluralized suffix; 1 -> "1 second", 5 -> "5 seconds"
    func secondsCount(_ count: Int32) -> String {
        resourceProvider.getQuantityString(
            pluralsResource: pluralsResources.seconds,
            quantity: count,
            args: KotlinArray(size: 1, init: { _ in NSNumber(value: count) })
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

    /// Format minutes or seconds count with localized and pluralized suffix;  25 -> "1 seconds", 61 -> "1 minute"
    func minutesOrSecondsCount(seconds: TimeInterval, roundingRule: FloatingPointRoundingRule = .up) -> String {
        let roundedSeconds = seconds.rounded(roundingRule)
        return roundedSeconds >= .oneMinute
            ? minutesCount(UnitConverters.Minute.from(seconds: roundedSeconds, roundingRule: roundingRule))
            : secondsCount(Int(roundedSeconds))
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

    /// Format hours and minutes count with localized and pluralized suffix;  7260 -> "2 hours 1 minute", 7320 -> "2 hours 2 minute", 21600 -> "6 hours"
    func hoursWithMinutesCount(seconds: TimeInterval, roundingRule: FloatingPointRoundingRule = .up) -> String {
        let seconds = Int(seconds.rounded(roundingRule))

        let secondsPerMinute = Int(TimeInterval.oneMinute)
        let secondsPerHour = Int(TimeInterval.oneHour)

        let hours = seconds / secondsPerHour
        let minutes = (seconds % secondsPerHour) / secondsPerMinute

        var result = ""

        if hours > 0 {
            result += hoursCount(hours)
            if minutes > 0 {
                result += " \(minutesCount(minutes))"
            }
        } else {
            result += minutesCount(max(1, minutes))
        }

        return result
    }

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

    // MARK: Locale

    static func localizedCountryName(
        for regionCode: String,
        languageCode: String = Locale.current.languageCode ?? "en"
    ) -> String? {
        Locale(identifier: languageCode + "_" + regionCode).localizedString(forRegionCode: regionCode)
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
