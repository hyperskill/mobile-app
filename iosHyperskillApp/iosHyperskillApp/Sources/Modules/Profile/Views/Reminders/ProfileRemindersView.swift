import SwiftUI

struct ProfileRemindersView: View {
    @State private var activated = true

    @State private var selectedHour: Int = 21

    var body: some View {
        VStack {
            Toggle(Strings.Profile.remindersTitle, isOn: $activated)
                .font(.title3)

            if activated {
                Divider()

                HourIntervalPicker(
                    text: Strings.Profile.remindersSchedule,
                    selectedInterval: $selectedHour
                )
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding()
        .background(BackgroundView(color: Color(ColorPalette.surface)))
    }
}

struct ProfileRemindersView_Previews: PreviewProvider {
    static var previews: some View {
        ProfileRemindersView()
            .previewLayout(.sizeThatFits)
    }
}
