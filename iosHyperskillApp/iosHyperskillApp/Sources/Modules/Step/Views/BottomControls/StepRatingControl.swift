import SwiftUI

struct StepRatingControl: View {
    var onClick: ((Rating) -> Void)?

    var body: some View {
        VStack(alignment: .leading) {
            Text("How did you like the ") + Text("theory?").bold()

            StepRatingPickerView(onClick: onClick)
        }
    }

    enum Rating: Int, CaseIterable {
        case angry = 1
        case sad
        case neutral
        case happy
        case inLove

        fileprivate var imageName: String {
            switch self {
            case .angry:
                return Images.Step.Rating.angry
            case .sad:
                return Images.Step.Rating.sad
            case .neutral:
                return Images.Step.Rating.neutral
            case .happy:
                return Images.Step.Rating.happy
            case .inLove:
                return Images.Step.Rating.inLove
            }
        }
    }
}

private struct StepRatingPickerView: View {
    var onClick: ((StepRatingControl.Rating) -> Void)?

    var body: some View {
        HStack {
            ForEach(StepRatingControl.Rating.allCases.reversed(), id: \.rawValue) { rating in
                Spacer()
                Button(
                    action: {
                        onClick?(rating)
                    },
                    label: {
                        Image(rating.imageName)
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 24, height: 24)
                    }
                )
                .buttonStyle(BounceButtonStyle(bounceScale: 1.4))
                Spacer()
            }
        }
        .frame(height: 44)
        .background(Color.background)
        .cornerRadius(8)
    }
}

struct StepRatingControl_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StepRatingControl()

            StepRatingControl()
                .preferredColorScheme(.dark)
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
