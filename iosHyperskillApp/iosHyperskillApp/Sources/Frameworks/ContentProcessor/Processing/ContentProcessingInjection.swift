import Regex
import UIKit
import WebKit

protocol ContentProcessingInjection: AnyObject {
    /// Script that will be injected to <head></head>
    var headScript: String { get }
    /// Script that will be injected after <body> tag
    var bodyHeadScript: String { get }
    /// Script that will be injected before <body> tag
    var bodyTailScript: String { get }

    func shouldInject(to code: String) -> Bool
}

extension ContentProcessingInjection {
    var headScript: String { "" }

    var bodyHeadScript: String { "" }

    var bodyTailScript: String { "" }

    func shouldInject(to code: String) -> Bool { true }
}

/// Injects common CSS files
final class CommonStylesInjection: ContentProcessingInjection {
    var headScript: String {
        """
        <link rel="stylesheet" type="text/css" href="wysiwyg.css">
        <link rel="stylesheet" type="text/css" href="altcontent.css">
        <link rel="stylesheet" type="text/css" href="alt.css">
        <link rel="stylesheet" type="text/css" href="bootstrap.css">
        <link rel="stylesheet" type="text/css" href="fonts.css">
        <link rel="stylesheet" type="text/css" href="highlight.css">
        <link rel="stylesheet" type="text/css" href="icons.css">
        <link rel="stylesheet" type="text/css" href="variables.css">
        """
    }
}

/// Injects step text CSS files
final class StepStylesInjection: ContentProcessingInjection {
    var headScript: String {
        """
        <link rel="stylesheet" type="text/css" href="step.css">
        """
    }
}

/// Injects meta-viewport
final class MetaViewportInjection: ContentProcessingInjection {
    var headScript: String {
        """
        <meta name="viewport" content="initial-scale=1" />
        """
    }
}

/// Local jQuery script injection
final class JQueryInjection: ContentProcessingInjection {
    var headScript: String {
        """
        <script type="text/javascript" src="jquery-3.4.1.min.js"></script>
        """
    }

    func shouldInject(to code: String) -> Bool {
        code.contains("<img")
    }
}

/// Clickable images
final class ClickableImagesInjection: ContentProcessingInjection {
    var headScript: String {
        """
        <script type="text/javascript">
        $(document).ready(function() {
            $("img").each(function() {
                var parentTagName = $(this).parent().get(0).tagName;
                if (parentTagName.toLowerCase() == "a") {
                    return true;
                }
                var $this = $(this);
                var src = "openimg://" + $this.attr('src');
                $this.addClass('image');
                var a = $('<a/>').attr('href', src);
                if ($this.attr('ar-thumbnail') != "") {
                    $this.wrap(a);
                }
            });
        });
        </script>
        """
    }

    func shouldInject(to code: String) -> Bool {
        code.contains("<img")
    }
}

/// Disable images callout on long tap
final class WebkitImagesCalloutDisableInjection: ContentProcessingInjection {
    var headScript: String {
        """
        <script type="text/javascript">
        document.documentElement.style.webkitTouchCallout = 'none';
        </script>
        """
    }
}

/// MathJax init script
final class MathJaxInjection: ContentProcessingInjection {
    var headScript: String {
        #"""
        <script type="text/x-mathjax-config">
            MathJax.Hub.Config({
                messageStyle: "none",
                TeX: {Macros: {R: '{\\mathbb R}', N: '{\\mathbb N}', Z: '{\\mathbb Z}' }},
                tex2jax: {
                    preview: "none",
                    inlineMath: [['$','$'], ['\\(','\\)']],
                    displayMath: [['$$','$$'], ['\\[','\\]']]
                }
            });
        </script>
        <script type="text/javascript" src="MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>
        """#
    }

    func shouldInject(to code: String) -> Bool {
        code.contains("math-tex") ||
        code.contains("\\[") ||
        code.contains("\\(") ||
        code.contains("$")
    }
}

/// Detect web scripts
final class WebScriptInjection: ContentProcessingInjection {
    private static let htmlEntitiesRegex = try? Regex(
        string: "&([a-z0-9]+|#[0-9]{1,6}|#x[0-9a-f]{1,6});",
        options: [.ignoreCase]
    )

    func shouldInject(to code: String) -> Bool {
        code.contains("wysiwyg") ||
        code.contains("<strong") ||
        code.contains("<em") ||
        code.contains("<u") ||
        code.contains("<strike") ||
        code.contains("<samp") ||
        code.contains("<sub") ||
        code.contains("<sup") ||
        code.contains("<h1") ||
        code.contains("<h2") ||
        code.contains("<h3") ||
        code.contains("<h4") ||
        code.contains("<h5") ||
        code.contains("<h6") ||
        code.contains("<img") ||
        code.contains("<iframe") ||
        code.contains("<audio") ||
        code.contains("<table") ||
        code.contains("<div") ||
        code.contains("<blockquote") ||
        code.contains("<ol") ||
        code.contains("<ul") ||
        code.contains("<span") ||
        code.contains("&quot") ||
        self.hasHTMLEntity(in: code)
    }

    private func hasHTMLEntity(in string: String) -> Bool {
        Self.htmlEntitiesRegex?.firstMatch(in: string) != nil
    }
}

/// Kotlin runnable code playground
final class KotlinRunnableSamplesInjection: ContentProcessingInjection {
    var headScript: String {
        """
        <script type="text/javascript" src="kotlin-playground-1.21.1.min.js" data-selector="kotlin-runnable"></script>
        """
    }

    func shouldInject(to code: String) -> Bool {
        code.contains("<kotlin-runnable")
    }
}

/// Code syntax highlight with highlight.js and line_wrapper.js
final class HightlightJSInjection: ContentProcessingInjection {
    var headScript: String {
        """
        <script type="text/javascript" src="highlight.js"></script>
        <script>
        hljs.initHighlightingOnLoad();
        </script>
        <script type="text/javascript" src="lines_wrapper.js"></script>
        """
    }

    func shouldInject(to code: String) -> Bool {
        code.contains("<code")
    }
}

/// Injects script that assigns font sizes.
final class FontSizeInjection: ContentProcessingInjection {
    private static let defaultBodyFontSize = 17
    private static let defaultH1FontSize = 28
    private static let defaultH2FontSize = 22
    private static let defaultH3FontSize = 20
    private static let defaultBlockquoteFontSize = 17

    private let bodyFontSizeStringValue: String
    private let h1FontSizeStringValue: String
    private let h2FontSizeStringValue: String
    private let h3FontSizeStringValue: String
    private let blockquoteFontSizeStringValue: String

    var bodyHeadScript: String {
        """
        <script type="text/javascript">
            document.documentElement.style.setProperty('--body-font-size', '\(self.bodyFontSizeStringValue)');
            document.documentElement.style.setProperty('--h1-font-size', '\(self.h1FontSizeStringValue)');
            document.documentElement.style.setProperty('--h2-font-size', '\(self.h2FontSizeStringValue)');
            document.documentElement.style.setProperty('--h3-font-size', '\(self.h3FontSizeStringValue)');
            document.documentElement.style.setProperty('--blockquote-font-size', '\(self.blockquoteFontSizeStringValue)');
        </script>
        """
    }

    init(
        bodyFontSizeStringValue: String,
        h1FontSizeStringValue: String,
        h2FontSizeStringValue: String,
        h3FontSizeStringValue: String,
        blockquoteFontSizeStringValue: String
    ) {
        self.bodyFontSizeStringValue = bodyFontSizeStringValue
        self.h1FontSizeStringValue = h1FontSizeStringValue
        self.h2FontSizeStringValue = h2FontSizeStringValue
        self.h3FontSizeStringValue = h3FontSizeStringValue
        self.blockquoteFontSizeStringValue = blockquoteFontSizeStringValue
    }

    convenience init(
        bodyFontSize: Int = FontSizeInjection.defaultBodyFontSize,
        h1FontSize: Int = FontSizeInjection.defaultH1FontSize,
        h2FontSize: Int = FontSizeInjection.defaultH2FontSize,
        h3FontSize: Int = FontSizeInjection.defaultH3FontSize,
        blockquoteFontSize: Int = FontSizeInjection.defaultBlockquoteFontSize
    ) {
        self.init(
            bodyFontSizeStringValue: String(bodyFontSize),
            h1FontSizeStringValue: String(h1FontSize),
            h2FontSizeStringValue: String(h2FontSize),
            h3FontSizeStringValue: String(h3FontSize),
            blockquoteFontSizeStringValue: String(blockquoteFontSize)
        )
    }

    convenience init(baseFontSize: Int) {
        self.init(
            bodyFontSize: baseFontSize,
            h1FontSize: Int((Float(baseFontSize) * 1.65).rounded()),
            h2FontSize: Int((Float(baseFontSize) * 1.3).rounded()),
            h3FontSize: Int((Float(baseFontSize) * 1.2).rounded()),
            blockquoteFontSize: baseFontSize
        )
    }
}

/// Injects script that assigns font weight.
final class FontWeightInjection: ContentProcessingInjection {
    private let fontWeight: Int

    var bodyHeadScript: String {
        """
        <script type="text/javascript">
            document.body.style.setProperty('--font-weight', \(self.fontWeight));
        </script>
        """
    }

    init(fontWeight: Int) {
        self.fontWeight = fontWeight
    }

    convenience init(fontWeightNameMapping: FontWeightNameMapping = .regular) {
        self.init(fontWeight: fontWeightNameMapping.rawValue)
    }
}

/// Injects script that assigns font family.
final class FontFamilyInjection: ContentProcessingInjection {
    private let style: FamilyStyle

    var bodyHeadScript: String {
        """
        <script type="text/javascript">
            document.documentElement.style.setProperty('--font-family', \(self.style.fontsString));
        </script>
        """
    }

    init(style: FamilyStyle) {
        self.style = style
    }

    convenience init(fontFamily: String) {
        self.init(style: fontFamily.lowercased().contains("monospace") ? .mono : .regular)
    }

    enum FamilyStyle {
        case mono
        case regular

        fileprivate var fontsString: String {
            switch self {
            case .mono:
                return "\"ui-monospace, 'SF Mono', Menlo, Consolas, monospace\""
            case .regular:
                return "\"-apple-system, Arial, Helvetica, sans-serif\""
            }
        }
    }
}

/// Injects script that assigns font size and weight.
final class FontInjection: ContentProcessingInjection {
    private let font: UIFont

    var bodyHeadScript: String {
        guard let size = self.font.fontDescriptor.object(forKey: .size) as? NSNumber,
              let face = self.font.fontDescriptor.object(forKey: .face) as? String,
              let family = self.font.fontDescriptor.object(forKey: .family) as? String else {
            return ""
        }

        let fontSizeScript = FontSizeInjection(baseFontSize: size.intValue).bodyHeadScript

        var fontWeightScript = ""
        if let fontWeightNameMapping = FontWeightNameMapping(fontFace: face) {
            fontWeightScript = FontWeightInjection(fontWeightNameMapping: fontWeightNameMapping).bodyHeadScript
        }

        let fontFamilyScript = FontFamilyInjection(fontFamily: family).bodyHeadScript

        return "\(fontFamilyScript)\(fontSizeScript)\(fontWeightScript)"
    }

    init(font: UIFont) {
        self.font = font
    }
}

/// Injects script that assigns text color for light and dark mode.
final class TextColorInjection: ContentProcessingInjection {
    private let lightColorHexString: String
    private let darkColorHexString: String

    var bodyHeadScript: String {
        """
        <script type="text/javascript">
            document.body.style.setProperty('--text-color-light', '#\(self.lightColorHexString)');
            document.body.style.setProperty('--text-color-dark', '#\(self.darkColorHexString)');
        </script>
        """
    }

    init(lightColorHexString: String, darkColorHexString: String) {
        self.lightColorHexString = lightColorHexString
        self.darkColorHexString = darkColorHexString
    }

    convenience init(lightColor: UIColor, darkColor: UIColor) {
        self.init(lightColorHexString: lightColor.hexString, darkColorHexString: darkColor.hexString)
    }

    convenience init(dynamicColor: UIColor) {
        let lightTraitCollection = UITraitCollection(traitsFrom: [.current, .init(userInterfaceStyle: .light)])
        let darkTraitCollection = UITraitCollection(traitsFrom: [.current, .init(userInterfaceStyle: .dark)])

        let lightColor = dynamicColor.resolvedColor(with: lightTraitCollection)
        let darkColor = dynamicColor.resolvedColor(with: darkTraitCollection)

        self.init(lightColor: lightColor, darkColor: darkColor)
    }
}
