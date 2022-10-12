import SwiftUI

struct ProfileDailyStudyRemindersView: View {
    var isActivated: Bool
    @State var selectedHour: Int

    var onIsActivatedChanged: (Bool) -> Void

    var onSelectedHourChanged: (Int) -> Void
    var onSelectedHourTapped: () -> Void

    var body: some View {
        VStack {
            Toggle(
                Strings.Profile.DailyStudyReminders.title,
                isOn: .init(
                    get: { isActivated },
                    set: { newValue in
                        withAnimation {
                            onIsActivatedChanged(newValue)
                        }
                    }
                )
            )
            .font(.title3)
            .foregroundColor(.primaryText)

            if isActivated {
                Divider()

                HourIntervalPicker(
                    text: Strings.Profile.DailyStudyReminders.schedule,
                    selectedInterval: $selectedHour,
                    onSelectedIntervalChanged: onSelectedHourChanged,
                    onSelectedIntervalTapped: onSelectedHourTapped
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
        ProfileDailyStudyRemindersView(
            isActivated: false,
            selectedHour: 21,
            onIsActivatedChanged: { _ in },
            onSelectedHourChanged: { _ in },
            onSelectedHourTapped: {}
        )
        .previewLayout(.sizeThatFits)
    }
}
