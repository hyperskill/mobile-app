import Foundation
import SwiftUI
import UIKit

struct StepQuizFillBlanksSelectOptionsViewWrapper: UIViewRepresentable {
    let options: [StepQuizFillBlankOption]
    let selectedIndices: Set<Int>

    var isUserInteractionEnabled = true

    var onNewHeight: ((CGFloat) -> Void)?

    static func dismantleUIView(_ uiView: StepQuizFillBlanksSelectOptionsView, coordinator: Coordinator) {}

    func makeUIView(context: Context) -> StepQuizFillBlanksSelectOptionsView {
        StepQuizFillBlanksSelectOptionsView()
    }

    func updateUIView(_ uiView: StepQuizFillBlanksSelectOptionsView, context: Context) {
        let collectionViewAdapter = context.coordinator.collectionViewAdapter
        let shouldUpdateCollectionViewData = collectionViewAdapter.options
          != options && collectionViewAdapter.selectedIndices != selectedIndices

        collectionViewAdapter.options = options
        collectionViewAdapter.selectedIndices = selectedIndices
        collectionViewAdapter.isUserInteractionEnabled = isUserInteractionEnabled

//        if shouldUpdateCollectionViewData {
//            uiView.updateCollectionViewData(
//                delegate: collectionViewAdapter,
//                dataSource: collectionViewAdapter
//            )
//        }

        uiView.updateCollectionViewData(
            delegate: collectionViewAdapter,
            dataSource: collectionViewAdapter
        )

        uiView.onNewHeight = onNewHeight
    }

    func makeCoordinator() -> Coordinator {
        Coordinator()
    }
}

extension StepQuizFillBlanksSelectOptionsViewWrapper {
    class Coordinator: NSObject {
        private(set) var collectionViewAdapter = StepQuizFillBlanksSelectOptionsCollectionViewAdapter()

        override init() {
            super.init()
        }
    }
}
