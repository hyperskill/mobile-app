import Foundation
import RevenueCat
import shared

final class PurchaseManager: shared.IosPurchaseManager {
    static let shared = PurchaseManager()

    private init() {}

    // MARK: Protocol Conforming

    func isConfigured() -> Bool {
        Purchases.isConfigured
    }

    func configure(userId: Int64) {
        #if DEBUG
        print("PurchaseManager: Configuring RevenueCat...")
        #endif

        Purchases.logLevel = ApplicationInfo.isDebugModeAvailable ? .debug : .info
        Purchases.configure(
            withAPIKey: RevenueCatInfo.publicAPIKey,
            appUserID: "\(userId)"
        )
    }

    func login(
        userId: Int64,
        completionHandler: @escaping (SwiftyResult<KotlinUnit, KotlinThrowable>?, Error?) -> Void
    ) {
        #if DEBUG
        print("PurchaseManager: log in user with id \(userId)")
        #endif

        Purchases.shared.logIn("\(userId)") { customerInfo, created, error in
            #if DEBUG
            print("PurchaseManager: log in user with id \(userId) completed")
            #endif

            if let error {
                #if DEBUG
                print("PurchaseManager: log in user with id \(userId) failed, error: \(error)")
                #endif

                let failureResult = SwiftyResultFailure<KotlinUnit, KotlinThrowable>(
                    failure: KotlinThrowable(message: error.description)
                )

                completionHandler(failureResult, nil)
            } else {
                #if DEBUG
                print("""
PurchaseManager: log in user with id \(userId) succeeded, \
customerInfo: \(String(describing: customerInfo)), \
created: \(created)
""")
                #endif

                let successResult = SwiftyResultSuccess<KotlinUnit, KotlinThrowable>(
                    value: KotlinUnit()
                )

                completionHandler(successResult, nil)
            }
        }
    }

    func purchase(
        productId: String,
        platformPurchaseParams: PlatformPurchaseParams,
        completionHandler: @escaping (SwiftyResult<PurchaseResult, KotlinThrowable>?, Error?) -> Void
    ) {
        #if DEBUG
        print("PurchaseManager: purchase \(productId)...")
        #endif

        getProduct(id: productId) { storeProduct in
            guard let storeProduct else {
                #if DEBUG
                print("PurchaseManager: purchase \(productId) failed, no product found")
                #endif

                let result = SwiftyResultSuccess<PurchaseResult, KotlinThrowable>(
                    value: PurchaseResultErrorNoProductFound(productId: productId)
                )

                return completionHandler(result, nil)
            }

            Purchases.shared.purchase(
                product: storeProduct
            ) { storeTransaction, customerInfo, error, userCancelled in
                if userCancelled {
                    #if DEBUG
                    print("PurchaseManager: purchase \(productId) cancelled by user")
                    #endif

                    let result = SwiftyResultSuccess<PurchaseResult, KotlinThrowable>(
                        value: PurchaseResultCancelledByUser()
                    )

                    return completionHandler(result, nil)
                }

                if let error {
                    let purchaseResult = error.asSharedPurchaseResult()

                    #if DEBUG
                    print("""
PurchaseManager: purchase \(productId) failed, error: \(error), purchaseResult: \(purchaseResult)
""")
                    #endif

                    let result = SwiftyResultSuccess<PurchaseResult, KotlinThrowable>(
                        value: purchaseResult
                    )

                    return completionHandler(result, nil)
                }

                if let storeTransaction, let customerInfo {
                    #if DEBUG
                    print("""
PurchaseManager: purchase \(productId) succeeded, storeTransaction: \(storeTransaction), customerInfo: \(customerInfo)
""")
                    #endif

                    let purchaseResult = PurchaseResultSucceed(
                        orderId: storeTransaction.transactionIdentifier,
                        productIds: [storeTransaction.productIdentifier]
                    )
                    let result = SwiftyResultSuccess<PurchaseResult, KotlinThrowable>(
                        value: purchaseResult
                    )

                    completionHandler(result, nil)
                } else {
                    #if DEBUG
                    print("PurchaseManager: purchase \(productId) failed, no storeTransaction or customerInfo")
                    #endif

                    let purchaseResult = PurchaseResultErrorOtherError(
                        message: "No storeTransaction or customerInfo found for \(productId) purchase",
                        underlyingErrorMessage: nil
                    )
                    let result = SwiftyResultSuccess<PurchaseResult, KotlinThrowable>(
                        value: purchaseResult
                    )

                    completionHandler(result, nil)
                }
            }
        }
    }

    func getManagementUrl(
        completionHandler: @escaping (SwiftyResult<NSString, KotlinThrowable>?, Error?) -> Void
    ) {
        #if DEBUG
        print("PurchaseManager: get management URL...")
        #endif

        Purchases.shared.getCustomerInfo { customerInfo, error in
            if let error {
                #if DEBUG
                print("PurchaseManager: get management URL failed, error: \(error)")
                #endif

                let failureResult = SwiftyResultFailure<NSString, KotlinThrowable>(
                    failure: KotlinThrowable(message: error.description)
                )

                completionHandler(failureResult, nil)
            } else {
                #if DEBUG
                print("""
PurchaseManager: get management URL succeeded, managementURL: \(String(describing: customerInfo?.managementURL))
""")
                #endif

                let managementURLString = customerInfo?.managementURL?.absoluteString ?? ""
                let successResult = SwiftyResultSuccess<NSString, KotlinThrowable>(
                    value: managementURLString as NSString
                )

                completionHandler(successResult, nil)
            }
        }
    }

    func getFormattedProductPrice(
        productId: String,
        completionHandler: @escaping (String?, Error?) -> Void
    ) {
        #if DEBUG
        print("PurchaseManager: get formatted product price for \(productId)...")
        #endif

        getProduct(id: productId) { storeProduct in
            if let storeProduct {
                #if DEBUG
                print("""
PurchaseManager: get formatted product price for \(productId) succeeded, \
localizedPriceString: \(storeProduct.localizedPriceString)
""")
                #endif
                completionHandler(storeProduct.localizedPriceString, nil)
            } else {
                #if DEBUG
                print("PurchaseManager: get formatted product price for \(productId) failed")
                #endif
                completionHandler(nil, nil)
            }
        }
    }

    func checkTrialOrIntroDiscountEligibility(
        productId: String,
        completionHandler: @escaping (KotlinBoolean?, (any Error)?) -> Void
    ) {
        Purchases.shared.checkTrialOrIntroDiscountEligibility(
            productIdentifiers: [productId]
        ) { eligibilities in
            if let eligibility = eligibilities[productId] {
                let isEligible = eligibility.status == .eligible
                completionHandler(KotlinBoolean(value: isEligible), nil)
            } else {
                completionHandler(KotlinBoolean(value: false), nil)
            }
        }
    }

    private func getProduct(
        id: String,
        completionHandler: @escaping (StoreProduct?) -> Void
    ) {
        Purchases.shared.getProducts([id]) { storeProducts in
            completionHandler(storeProducts.first)
        }
    }
}

// MARK: - RevenueCat.PublicError (shared.PurchaseResult) -

private extension RevenueCat.PublicError {
    func asSharedPurchaseResult() -> shared.PurchaseResult {
        guard let errorCode = self as? RevenueCat.ErrorCode else {
            return PurchaseResultErrorOtherError(
                message: description,
                underlyingErrorMessage: userInfo.description
            )
        }

        let errorDescription = errorCode.description
        let errorUserInfoDescription = errorCode.errorUserInfo.description

        switch errorCode {
        case .purchaseCancelledError:
            return PurchaseResultCancelledByUser()
        case .storeProblemError:
            return PurchaseResultErrorStoreProblemError(
                message: errorDescription,
                underlyingErrorMessage: errorUserInfoDescription
            )
        case .purchaseNotAllowedError:
            return PurchaseResultErrorPurchaseNotAllowedError(
                message: errorDescription,
                underlyingErrorMessage: errorUserInfoDescription
            )
        case .productAlreadyPurchasedError:
            return PurchaseResultErrorProductAlreadyPurchasedError(
                message: errorDescription,
                underlyingErrorMessage: errorUserInfoDescription
            )
        case .receiptAlreadyInUseError:
            return PurchaseResultErrorReceiptAlreadyInUseError(
                message: errorDescription,
                underlyingErrorMessage: errorUserInfoDescription
            )
        case .paymentPendingError:
            return PurchaseResultErrorPaymentPendingError(
                message: errorDescription,
                underlyingErrorMessage: errorUserInfoDescription
            )
        default:
            return PurchaseResultErrorOtherError(
                message: errorDescription,
                underlyingErrorMessage: errorUserInfoDescription
            )
        }
    }
}
