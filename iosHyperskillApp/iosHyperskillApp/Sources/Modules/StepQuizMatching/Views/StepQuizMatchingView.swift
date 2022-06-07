import SwiftUI

struct StepQuizMatchingView: View {
    @State var viewData: StepQuizMatchingViewData

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
            ForEach(Array(viewData.items.enumerated()), id: \.element.option) { index, item in
                StepQuizMatchingItemView(
                    title: item.title.text,
                    option: item.option.text,
                    isMoveUpEnabled: index > 0,
                    isMoveDownEnabled: index < viewData.items.count - 1,
                    onMoveUp: {
                        withAnimation {
                            doMoveUp(from: index)
                        }
                    },
                    onMoveDown: {
                        withAnimation {
                            doMoveDown(from: index)
                        }
                    }
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

#if DEBUG
struct StepQuizMatchingView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizMatchingView(viewData: .placeholder)
            .padding()
    }
}
#endif
