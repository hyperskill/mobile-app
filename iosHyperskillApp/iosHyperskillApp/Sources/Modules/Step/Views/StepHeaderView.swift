import SwiftUI

struct StepHeaderView: View {
    let title: String
    let timeToComplete: String

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text(title)
                .font(.title2)
                .foregroundColor(.systemPrimaryText)
            HStack {
                Image("step-time-to-complete")
                    .renderingMode(.template)
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 12, height: 12)
                Text(timeToComplete)
                    .font(.caption)
            }
            .foregroundColor(.secondaryText)
        }
    }
}

struct StepHeaderView_Previews: PreviewProvider {
    static var previews: some View {
        StepHeaderView(title: "Theory", timeToComplete: "3 minutes remaining")
    }
}
