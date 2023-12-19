import Foundation
import SwiftUI
import UIKit

struct FillBlanksQuizViewWrapper: UIViewRepresentable {
    let components: [StepQuizFillBlankComponent]
    let options: [StepQuizFillBlankOption]
    let isUserInteractionEnabled: Bool

    var onInputDidChange: ((String, StepQuizFillBlankComponent) -> Void)?

    var onDidSelectComponent: ((IndexPath) -> Void)?
    var onDidDeselectComponent: ((IndexPath) -> Void)?

    static func dismantleUIView(_ uiView: FillBlanksQuizView, coordinator: Coordinator) {
        coordinator.onInputDidChange = nil
        coordinator.onDidSelectComponent = nil
        coordinator.onDidDeselectComponent = nil
        coordinator.onDeviceOrientationDidChange = nil
        coordinator.collectionViewAdapter.delegate = nil
        NotificationCenter.default.removeObserver(coordinator)
    }

    func makeUIView(context: Context) -> FillBlanksQuizView {
        FillBlanksQuizView()
    }

    func updateUIView(_ uiView: FillBlanksQuizView, context: Context) {
        let collectionViewAdapter = context.coordinator.collectionViewAdapter
        let shouldUpdateCollectionViewData = collectionViewAdapter.components != components

        collectionViewAdapter.components = components
        collectionViewAdapter.options = options
        collectionViewAdapter.isUserInteractionEnabled = isUserInteractionEnabled

        if shouldUpdateCollectionViewData {
            uiView.updateCollectionViewData(
                delegate: collectionViewAdapter,
                dataSource: collectionViewAdapter
            )

            // ALTAPPS-1062: Fixes incorrect collection view layout on first update
            if context.coordinator.isFirstCollectionViewDataUpdate {
                context.coordinator.isFirstCollectionViewDataUpdate = false
                DispatchQueue.main.asyncAfter(deadline: .now() + 0.1) {
                    uiView.invalidateCollectionViewLayout()
                }
            }
        }

        context.coordinator.onInputDidChange = { [weak collectionViewAdapter, weak uiView] inputText, component in
            guard let collectionViewAdapter, let uiView else {
                return
            }

            guard let index = collectionViewAdapter.components.firstIndex(
                where: { $0.id == component.id }
            ) else {
                return
            }

            collectionViewAdapter.components[index].inputText = inputText
            self.onInputDidChange?(inputText, component)

            uiView.invalidateCollectionViewLayout()
        }
        context.coordinator.onDeviceOrientationDidChange = { [weak uiView] in
            guard let uiView else {
                return
            }

            uiView.invalidateCollectionViewLayout()
        }
        context.coordinator.onDidSelectComponent = onDidSelectComponent
        context.coordinator.onDidDeselectComponent = onDidDeselectComponent
    }

    func makeCoordinator() -> Coordinator {
        Coordinator()
    }
}

extension FillBlanksQuizViewWrapper {
    class Coordinator: NSObject, FillBlanksQuizCollectionViewAdapterDelegate {
        private(set) var collectionViewAdapter = FillBlanksQuizCollectionViewAdapter()

        fileprivate var isFirstCollectionViewDataUpdate = true

        var onInputDidChange: ((String, StepQuizFillBlankComponent) -> Void)?

        var onDidSelectComponent: ((IndexPath) -> Void)?
        var onDidDeselectComponent: ((IndexPath) -> Void)?

        var onDeviceOrientationDidChange: (() -> Void)?

        override init() {
            super.init()

            collectionViewAdapter.delegate = self

            NotificationCenter.default.addObserver(
                self,
                selector: #selector(handleDeviceOrientationDidChange),
                name: UIDevice.orientationDidChangeNotification,
                object: nil
            )
        }

        func fillBlanksQuizCollectionViewAdapter(
            _ adapter: FillBlanksQuizCollectionViewAdapter,
            inputDidChange inputText: String,
            forComponent component: StepQuizFillBlankComponent
        ) {
            onInputDidChange?(inputText, component)
        }

        func fillBlanksQuizCollectionViewAdapter(
            _ adapter: FillBlanksQuizCollectionViewAdapter,
            didSelectComponentAt indexPath: IndexPath
        ) {
            onDidSelectComponent?(indexPath)
        }

        func fillBlanksQuizCollectionViewAdapter(
            _ adapter: FillBlanksQuizCollectionViewAdapter,
            didDeselectComponentAt indexPath: IndexPath
        ) {
            onDidDeselectComponent?(indexPath)
        }

        @objc
        private func handleDeviceOrientationDidChange() {
            onDeviceOrientationDidChange?()
        }
    }
}
