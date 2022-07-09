import Foundation

enum Formatter {
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
