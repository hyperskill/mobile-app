import Foundation

struct ProfileViewData {
    let about: About

    struct About {
        let livesInText: String?
        let speaksText: String?

        let bio: String?

        let experience: String?

        let facebookUsername: String?
        let twitterUsername: String?
        let linkedInUsername: String?
        let redditUsername: String?
        let githubUsername: String?

        let buttonText: String
    }
}

extension ProfileViewData {
    static var placeholder: ProfileViewData {
        ProfileViewData(
            about: .init(
                livesInText: "Lives in India",
                speaksText: "Speaks English, Hindi",
                bio: """
Hey, I've been working out here a while. It's really hard so far, but I'm hanging in there. Hopefully, in time, I'll
finally learn to do these tasks faster than anyone else. Anyway, good luck to you and me! Thank you!
""",
                experience: """
I’ve learned so much spending in my mid-school. And I learned even more at the high-school. Amazing experience!
""",
                facebookUsername: "test",
                twitterUsername: "test",
                linkedInUsername: "test",
                redditUsername: "test",
                githubUsername: "test",
                buttonText: "View the full version of profile ↗"
            )
        )
    }
}
