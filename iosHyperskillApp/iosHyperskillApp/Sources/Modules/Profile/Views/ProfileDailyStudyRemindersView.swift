import SwiftUI

struct ProfileDailyStudyRemindersView: View {
    @State private var isActivated = false

    @State private var selectedHour: Int = 21

    var body: some View {
        VStack {
            Toggle(Strings.Profile.DailyStudyReminders.title, isOn: $isActivated)
                .font(.title3)

            if isActivated {
                Divider()

                HourIntervalPicker(
                    text: Strings.Profile.DailyStudyReminders.schedule,
                    selectedInterval: $selectedHour
                )
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding()
        .background(BackgroundView(color: Color(ColorPalette.surface)))
    }
}

struct ProfileDailyStudyRemindersView_Previews: PreviewProvider {
    static var previews: some View {
        ProfileDailyStudyRemindersView()
            .previewLayout(.sizeThatFits)
    }
}
