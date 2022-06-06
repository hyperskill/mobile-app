import SwiftUI

struct StepQuizMatchingView: View {
    @State var viewData: StepQuizMatchingViewData

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
            ForEach(Array(viewData.items.enumerated()), id: \.element.option) { index, item in
                StepQuizMatchingItemVIew(
                    titleText: item.title.text,
                    optionText: item.option.text,
                    onMoveUp: {
                        withAnimation {
                            doMoveUp(from: index)
                        }
                    },
                    onMoveDown: {
                        withAnimation {
                            doMoveDown(from: index)
                        }
                    },
                    isMoveUpEnabled: index > 0,
                    isMoveDownEnabled: index < viewData.items.count - 1
                )
            }
        }
    }

    private func doMoveUp(from index: Int) {
        let tmp = viewData.items[index - 1].option
        viewData.items[index - 1].option = viewData.items[index].option
        viewData.items[index].option = tmp
    }

    private func doMoveDown(from index: Int) {
        let tmp = viewData.items[index + 1].option
        viewData.items[index + 1].option = viewData.items[index].option
        viewData.items[index].option = tmp
    }
}

struct StepQuizMatchingView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizMatchingView(viewData: .makePlaceholder())
            .padding()
    }
}
