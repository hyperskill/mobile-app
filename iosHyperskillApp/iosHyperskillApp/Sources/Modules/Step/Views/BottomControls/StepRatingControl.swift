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
                return "step-rating-angry"
            case .sad:
                return "step-rating-sad"
            case .neutral:
                return "step-rating-neutral"
            case .happy:
                return "step-rating-happy"
            case .inLove:
                return "step-rating-in-love"
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
        .background(Color(ColorPalette.background))
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
        .padding()
    }
}
