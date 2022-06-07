import SnapKit
import UIKit
import WebKit

protocol ProcessedContentWebViewDelegate: AnyObject {
    func processedContentTextViewDidLoadContent(_ view: ProcessedContentWebView)
    func processedContentTextView(_ view: ProcessedContentWebView, didReportNewHeight height: Int)
    func processedContentTextView(_ view: ProcessedContentWebView, didOpenImageURL url: URL)
    func processedContentTextView(_ view: ProcessedContentWebView, didOpenLink url: URL)
}

extension ProcessedContentWebView {
    struct Appearance {
        var insets = LayoutInsets(top: 10, left: 16, bottom: 4, right: 16)
        var backgroundColor = UIColor.systemBackground
    }
}

final class ProcessedContentWebView: UIView {
    private static let pollDocumentReadyStateInterval: TimeInterval = 0.5
    private static let reloadTimeStandardInterval: TimeInterval = 0.5
    private static let reloadTimeout: TimeInterval = 10.0
    private static let defaultWebViewHeight: CGFloat = 5
    private static let clearWebViewContentURLString = "about:blank"

    let appearance: Appearance
    weak var delegate: ProcessedContentWebViewDelegate?

    private lazy var webViewConfiguration: WKWebViewConfiguration = {
        let userContentController = WKUserContentController()
        // Remove paddings and margins
        let userScript = WKUserScript(
            source: """
            var style = document.createElement('style');
            style.innerHTML = 'body { padding: 0 !important; margin: 0 !important; }';
            document.head.appendChild(style);
            """,
            injectionTime: .atDocumentEnd,
            forMainFrameOnly: true
        )

        let userScriptViewport = WKUserScript(
            source: """
            var meta = document.createElement('meta');
            meta.setAttribute('name', 'viewport');
            meta.setAttribute('content', 'width=device-width, initial-scale=1.0, maximum-scale=1.0');
            document.getElementsByTagName('head')[0].appendChild(meta);
            """,
            injectionTime: .atDocumentEnd,
            forMainFrameOnly: true
        )

        userContentController.addUserScript(userScript)
        userContentController.addUserScript(userScriptViewport)

        let webViewConfig = WKWebViewConfiguration()
        webViewConfig.userContentController = userContentController
        webViewConfig.dataDetectorTypes = [.link]

        return webViewConfig
    }()

    private lazy var webView: WKWebView = {
        let webView = WKWebView(frame: .zero, configuration: self.webViewConfiguration)
        webView.isOpaque = false
        webView.backgroundColor = self.appearance.backgroundColor
        webView.scrollView.backgroundColor = self.appearance.backgroundColor

        webView.navigationDelegate = self
        webView.scrollView.isScrollEnabled = false
        webView.scrollView.delegate = self
        webView.scrollView.showsHorizontalScrollIndicator = false
        webView.scrollView.showsVerticalScrollIndicator = false
        webView.scrollView.contentInset = .zero
        webView.scrollView.contentInsetAdjustmentBehavior = .never

        return webView
    }()

    private var webViewHeightConstraint: Constraint?

    /// Keeps track of current web view height.
    private var currentWebViewHeight = Int(ProcessedContentWebView.defaultWebViewHeight)
    private var isLoadHTMLStringInProgress = false
    private var isClearWebViewContentInProgress = false
    private var htmlTextToLoadAfterWebViewContentCleared: String?

    /// A Boolean value that determines whether auto-scrolling is enabled.
    ///
    /// If the value of this property is `true`, auto-scrolling is enabled and view will enable scrolling for wider content than webView's size,
    /// and if it is `false`, auto-scrolling is disabled.
    var isAutoScrollingEnabled = true

    var isScrollEnabled: Bool {
        get {
            self.webView.scrollView.isScrollEnabled
        }
        set {
            self.webView.scrollView.isScrollEnabled = newValue
        }
    }

    var insets: LayoutInsets? {
        didSet {
            let insets = self.insets ?? LayoutInsets(uiEdgeInsets: .zero)
            self.webView.snp.updateConstraints { make in
                make.top.equalToSuperview().offset(insets.top)
                make.leading.equalToSuperview().offset(insets.leading)
                make.trailing.equalToSuperview().offset(-insets.trailing)
                make.bottom.equalToSuperview().offset(-insets.bottom)
            }
            self.layoutIfNeeded()
        }
    }

    var height: Int {
        get {
            self.currentWebViewHeight
        }
        set {
            if self.currentWebViewHeight != newValue {
                self.currentWebViewHeight = newValue
                self.webViewHeightConstraint?.update(offset: newValue)
            }
        }
    }

    override var intrinsicContentSize: CGSize {
        CGSize(
            width: UIView.noIntrinsicMetric,
            height: CGFloat(self.height)
                + self.appearance.insets.top
                + self.appearance.insets.bottom
        )
    }

    init(frame: CGRect = .zero, appearance: Appearance = Appearance()) {
        self.appearance = appearance
        super.init(frame: frame)

        self.setupView()
        self.addSubviews()
        self.makeConstraints()
    }

    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func layoutSubviews() {
        super.layoutSubviews()
        self.invalidateIntrinsicContentSize()
    }

    // MARK: Public API

    func loadHTMLText(_ text: String) {
        if self.isClearWebViewContentInProgress {
            self.htmlTextToLoadAfterWebViewContentCleared = text
        } else {
            self.isLoadHTMLStringInProgress = true

            self.webView.stopLoading()
            let baseURL = URL(fileURLWithPath: Bundle.main.bundlePath)
            self.webView.loadHTMLString(text, baseURL: baseURL)
        }
    }

    func clearContent() {
        if self.isClearWebViewContentInProgress {
            return
        }

        self.isClearWebViewContentInProgress = true

        self.height = Int(Self.defaultWebViewHeight)

        guard let clearWebViewContentURL = URL(string: Self.clearWebViewContentURLString) else {
            self.isClearWebViewContentInProgress = false
            return
        }

        self.isLoadHTMLStringInProgress = false

        self.webView.stopLoading()
        self.webView.load(URLRequest(url: clearWebViewContentURL))
    }

    // MARK: Private API

    private func waitForDocumentCompleteState() async {
        var retryCount = 1
        var currentDocumentReadyState: String?

        while currentDocumentReadyState != "complete" {
            do {
                let delayInSeconds = TimeInterval(retryCount) * Self.pollDocumentReadyStateInterval
                try await Task.sleep(nanoseconds: UnitConverters.Nanosecond.from(second: delayInSeconds))

                let result = try await self.webView.evaluateJavaScript("document.readyState;")
                print("ProcessedContentWebView :: waitForDocumentCompleteState result = \(String(describing: result))")

                currentDocumentReadyState = result as? String
                retryCount += 1
            } catch {
                return print("ProcessedContentWebView :: waitForDocumentCompleteState error = \(error)")
            }
        }
    }

    private func getContentHeight() async -> Int {
        let result = try? await self.webView.evaluateJavaScript("document.body.scrollHeight;")
        print("ProcessedContentWebView :: getContentHeight result = \(String(describing: result))")
        return result as? Int ?? 0
    }

    private func getContentWidth() async -> Int {
        let result = try? await self.webView.evaluateJavaScript("document.body.scrollWidth;")
        print("ProcessedContentWebView :: getContentWidth result = \(String(describing: result))")
        return result as? Int ?? 0
    }

    private func fetchHeightWithInterval(_ count: Int = 0) {
        let currentTime = TimeInterval(count) * Self.reloadTimeStandardInterval
        guard currentTime <= Self.reloadTimeout else {
            return
        }

        DispatchQueue.main.asyncAfter(deadline: .now() + currentTime) { [weak self] in
            guard let strongSelf = self else {
                return
            }

            Task {
                let height = await strongSelf.getContentHeight()

                await MainActor.run {
                    if strongSelf.height != height {
                        strongSelf.delegate?.processedContentTextView(strongSelf, didReportNewHeight: height)
                    }

                    strongSelf.height = height
                }
            }

            strongSelf.fetchHeightWithInterval(count + 1)
        }
    }
}

// MARK: - ProcessedContentWebView: ProgrammaticallyInitializableViewProtocol -

extension ProcessedContentWebView: ProgrammaticallyInitializableViewProtocol {
    func addSubviews() {
        self.addSubview(self.webView)
    }

    func makeConstraints() {
        self.webView.translatesAutoresizingMaskIntoConstraints = false
        self.webView.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(self.appearance.insets.top)
            make.leading.equalToSuperview().offset(self.appearance.insets.leading)
            make.trailing.equalToSuperview().offset(-self.appearance.insets.trailing)
            make.bottom.equalToSuperview().offset(-self.appearance.insets.bottom)
            self.webViewHeightConstraint = make.height.equalTo(Self.defaultWebViewHeight).priority(.low).constraint
        }
    }
}

// MARK: - ProcessedContentWebView: WKNavigationDelegate -

extension ProcessedContentWebView: WKNavigationDelegate {
    private static let imageLinkPrefix: String = "openimg://"

    // swiftlint:disable:next implicitly_unwrapped_optional
    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        Task {
            await self.waitForDocumentCompleteState()

            async let heightTask = self.getContentHeight()
            async let widthTask = self.getContentWidth()

            let (height, width) = await (heightTask, widthTask)

            await MainActor.run {
                self.isLoadHTMLStringInProgress = false

                self.height = height
                self.delegate?.processedContentTextView(self, didReportNewHeight: height)

                if self.isClearWebViewContentInProgress {
                    self.isClearWebViewContentInProgress = false

                    if let htmlText = self.htmlTextToLoadAfterWebViewContentCleared {
                        self.loadHTMLText(htmlText)
                    } else {
                        self.delegate?.processedContentTextViewDidLoadContent(self)
                    }

                    self.htmlTextToLoadAfterWebViewContentCleared = nil
                } else {
                    self.delegate?.processedContentTextViewDidLoadContent(self)
                }

                if self.isAutoScrollingEnabled {
                    self.isScrollEnabled = CGFloat(width) > self.webView.bounds.size.width
                }

                self.fetchHeightWithInterval()
            }
        }
    }

    func webView(
        _ webView: WKWebView,
        decidePolicyFor navigationAction: WKNavigationAction,
        decisionHandler: @escaping (WKNavigationActionPolicy) -> Void
    ) {
        guard let url = navigationAction.request.url else {
            return decisionHandler(.cancel)
        }

        if url.absoluteString.starts(with: Self.imageLinkPrefix) {
            return self.handleDecidePolicyForImageURL(url, decisionHandler: decisionHandler)
        }

        if self.isLoadHTMLStringInProgress && navigationAction.navigationType == .other {
            return decisionHandler(.allow)
        }

        if url.absoluteString == Self.clearWebViewContentURLString {
            return decisionHandler(.allow)
        }

        self.delegate?.processedContentTextView(self, didOpenLink: url)

        decisionHandler(.cancel)
    }

    // MARK: Private Helpers

    private func handleDecidePolicyForImageURL(_ url: URL, decisionHandler: (WKNavigationActionPolicy) -> Void) {
        var validPath = String(url.absoluteString.dropFirst(Self.imageLinkPrefix.count))

        validPath.replaceFirst(matching: "//", with: "://")

        if let imageURL = URL(string: validPath) {
            self.delegate?.processedContentTextView(self, didOpenImageURL: imageURL)
        }

        decisionHandler(.cancel)
    }
}

// MARK: - ProcessedContentWebView: UIScrollViewDelegate -

extension ProcessedContentWebView: UIScrollViewDelegate {
    // swiftlint:disable:next identifier_name
    func viewForZooming(in: UIScrollView) -> UIView? { nil }

    func scrollViewWillBeginZooming(_ scrollView: UIScrollView, with view: UIView?) {
        scrollView.pinchGestureRecognizer?.isEnabled = false
    }
}
