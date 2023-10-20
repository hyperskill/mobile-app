import Foundation
import SwiftUI
import UIKit

struct FillBlanksQuizViewWrapper: UIViewRepresentable {
    let viewData: StepQuizFillBlanksViewData
    
    static func dismantleUIView(_ uiView: FillBlanksQuizView, coordinator: Coordinator) {
    }

    func makeUIView(context: Context) -> FillBlanksQuizView {
        FillBlanksQuizView()
    }

    func updateUIView(_ uiView: FillBlanksQuizView, context: Context) {
        let collectionViewAdapter = context.coordinator.collectionViewAdapter
        let shouldUpdateCollectionViewData = collectionViewAdapter.components != viewData.components

        collectionViewAdapter.components = viewData.components

        if shouldUpdateCollectionViewData {
            uiView.updateCollectionViewData(
                delegate: collectionViewAdapter,
                dataSource: collectionViewAdapter
            )
        }
    }

    func makeCoordinator() -> Coordinator {
        Coordinator()
    }
}

extension FillBlanksQuizViewWrapper {
    class Coordinator: NSObject, FillBlanksQuizCollectionViewAdapterDelegate {
        private(set) var collectionViewAdapter = FillBlanksQuizCollectionViewAdapter()

        override init() {
            super.init()
            collectionViewAdapter.delegate = self
        }

        func fillBlanksQuizCollectionViewAdapter(
            _ adapter: FillBlanksQuizCollectionViewAdapter,
            inputDidChange inputText: String,
            forComponent component: StepQuizFillBlankComponent
        ) {
        }

        func fillBlanksQuizCollectionViewAdapter(
            _ adapter: FillBlanksQuizCollectionViewAdapter,
            didSelectComponentAt indexPath: IndexPath
        ) {}
    }
}
