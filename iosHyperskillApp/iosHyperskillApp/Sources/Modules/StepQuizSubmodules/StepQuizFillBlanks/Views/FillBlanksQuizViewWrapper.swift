import Foundation
import SwiftUI
import UIKit

struct FillBlanksQuizViewWrapper: UIViewRepresentable {
    let components: [StepQuizFillBlankComponent]

    var onInputDidChange: ((String, StepQuizFillBlankComponent) -> Void)?

    static func dismantleUIView(_ uiView: FillBlanksQuizView, coordinator: Coordinator) {
        coordinator.onInputDidChange = nil
        coordinator.collectionViewAdapter.delegate = nil
    }

    func makeUIView(context: Context) -> FillBlanksQuizView {
        FillBlanksQuizView()
    }

    func updateUIView(_ uiView: FillBlanksQuizView, context: Context) {
        let collectionViewAdapter = context.coordinator.collectionViewAdapter
        let shouldUpdateCollectionViewData = collectionViewAdapter.components != components

        collectionViewAdapter.components = components

        if shouldUpdateCollectionViewData {
            uiView.updateCollectionViewData(
                delegate: collectionViewAdapter,
                dataSource: collectionViewAdapter
            )
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
    }

    func makeCoordinator() -> Coordinator {
        Coordinator()
    }
}

extension FillBlanksQuizViewWrapper {
    class Coordinator: NSObject, FillBlanksQuizCollectionViewAdapterDelegate {
        private(set) var collectionViewAdapter = FillBlanksQuizCollectionViewAdapter()

        var onInputDidChange: ((String, StepQuizFillBlankComponent) -> Void)?

        override init() {
            super.init()
            collectionViewAdapter.delegate = self
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
        ) {}
    }
}
