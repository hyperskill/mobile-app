import Foundation
import SwiftUI
import UIKit

struct StepQuizFillBlanksSelectOptionsViewWrapper: UIViewRepresentable {
    var moduleOutput: StepQuizFillBlanksSelectOptionsOutputProtocol?
    var moduleInput: ((StepQuizFillBlanksSelectOptionsInputProtocol) -> Void)?

    var isUserInteractionEnabled = true

    var onNewHeight: ((CGFloat) -> Void)?

    static func dismantleUIView(_ uiView: StepQuizFillBlanksSelectOptionsView, coordinator: Coordinator) {
        uiView.onNewHeight = nil
        coordinator.onUpdateCollectionViewData = nil
        coordinator.onDidSelectComponent = nil
        coordinator.collectionViewAdapter.delegate = nil
    }

    func makeUIView(context: Context) -> StepQuizFillBlanksSelectOptionsView {
        StepQuizFillBlanksSelectOptionsView()
    }

    func updateUIView(_ uiView: StepQuizFillBlanksSelectOptionsView, context: Context) {
        moduleInput?(context.coordinator)

        uiView.onNewHeight = onNewHeight

        let collectionViewAdapter = context.coordinator.collectionViewAdapter

        collectionViewAdapter.isUserInteractionEnabled = isUserInteractionEnabled
        context.coordinator.onUpdateCollectionViewData = { [weak uiView] collectionViewAdapter in
            guard let uiView else {
                return
            }

            uiView.updateCollectionViewData(
                delegate: collectionViewAdapter,
                dataSource: collectionViewAdapter
            )
        }
        context.coordinator.onDidSelectComponent = {
            [weak uiView, weak collectionViewAdapter, weak moduleOutput] indexPath in
            guard let uiView, let collectionViewAdapter else {
                return
            }

            uiView.updateCollectionViewData(
                delegate: collectionViewAdapter,
                dataSource: collectionViewAdapter
            )

            guard let moduleOutput else {
                assertionFailure("StepQuizFillBlanksSelectOptionsViewWrapper: moduleOutput is nil")
                return
            }

            moduleOutput.handleStepQuizFillBlanksSelectOptionsDidSelectOption(
                option: collectionViewAdapter.options[indexPath.row],
                at: indexPath.row
            )
        }
    }

    func makeCoordinator() -> Coordinator {
        let coordinator = Coordinator()
        moduleInput?(coordinator)
        return coordinator
    }
}

// MARK: - Coordinator -

extension StepQuizFillBlanksSelectOptionsViewWrapper {
    class Coordinator: NSObject {
        private(set) var collectionViewAdapter = StepQuizFillBlanksSelectOptionsCollectionViewAdapter()

        private var blanksCount: Int = 0

        var onUpdateCollectionViewData: ((StepQuizFillBlanksSelectOptionsCollectionViewAdapter) -> Void)?
        var onDidSelectComponent: ((IndexPath) -> Void)?

        override init() {
            super.init()
            collectionViewAdapter.delegate = self
        }
    }
}

extension StepQuizFillBlanksSelectOptionsViewWrapper.Coordinator: StepQuizFillBlanksSelectOptionsInputProtocol {
    func update(options: [StepQuizFillBlankOption], selectedIndices: Set<Int>, blanksCount: Int) {
        self.blanksCount = blanksCount

        let isOptionsChanged = collectionViewAdapter.options != options
        let isSelectedIndicesChanged = collectionViewAdapter.selectedIndices != selectedIndices

        let shouldUpdateCollectionViewData = isOptionsChanged || isSelectedIndicesChanged

        collectionViewAdapter.options = options
        collectionViewAdapter.selectedIndices = selectedIndices

        if shouldUpdateCollectionViewData {
            onUpdateCollectionViewData?(collectionViewAdapter)
        }
    }
}

extension StepQuizFillBlanksSelectOptionsViewWrapper.Coordinator:
  StepQuizFillBlanksSelectOptionsCollectionViewAdapterDelegate {
    func fillBlanksSelectOptionsCollectionViewAdapter(
        _ adapter: StepQuizFillBlanksSelectOptionsCollectionViewAdapter,
        didSelectComponentAt indexPath: IndexPath
    ) {
        guard (blanksCount - collectionViewAdapter.selectedIndices.count) > 0 else {
            return
        }

        let (isInserted, _) = collectionViewAdapter.selectedIndices.insert(indexPath.row)
        guard isInserted else {
            return
        }

        onDidSelectComponent?(indexPath)
    }
}
