import SwiftUI

enum SpacebotAvatarImageResources {
    static let resources: [ImageResource] = [
        .fancySpaceRobotInAnEvolvingRedCape3DStyle,
        .fancySpaceRobotWearingAGraduationCap3DStyle,
        .fancySpaceRobotWithGearsAndToolsCarbonMeta,
        .fancySpaceRobotWithGlowingLights,
        .spaceRobotGoldMetal3D,
        .spaceRobotHoldingHugeGem2,
        .spaceRobotHoldingHugeGem3,
        .spaceRobotInAnEvolvingRedCape2,
        .spaceRobotInAnEvolvingRed,
        .spaceRobotWithGearsAndToolsCarbon2,
        .spaceRobotWithGearsAndToolsCarbon,
        .spaceRobotWithGlowingLights3,
        .spaceRobotWithGlowingLights4,
        .spaceRobotWithGlowingLights5,
        .spaceRobotWithHugeGem,
        .spacebotAndASCIIMirrorNoise35MmMinimalistCo,
        .spacebotAndNumberBaseConverter35MmMinimalist19879,
        .spacebotListensToMusicWithBigHeadphones,
        .spacebotListensToMusicWithBigHeadphones2,
        .spacebotWithTrafficLightNoise35MmMinimalist34428280
    ]
}

struct TopicCompletedModalSpacebotAvatarView: View {
    let imageResource: ImageResource

    var body: some View {
        Image(imageResource)
            .renderingMode(.original)
            .resizable()
            .scaledToFit()
            .clipShape(Circle())
            .frame(widthHeight: DeviceInfo.current.isPad ? 344 : 228)
    }
}

extension TopicCompletedModalSpacebotAvatarView {
    init(spacebotAvatarVariantIndex: Int) {
        if let imageResource = SpacebotAvatarImageResources.resources[safe: spacebotAvatarVariantIndex] {
            self.init(imageResource: imageResource)
        } else {
            self.init(imageResource: SpacebotAvatarImageResources.resources.first.require())
        }
    }
}

#if DEBUG
#Preview {
    ScrollView {
        LazyVStack {
            ForEach(
                Array(SpacebotAvatarImageResources.resources.enumerated()),
                id: \.offset
            ) { index, resource in
                HStack {
                    Text("\(index + 1)")
                    TopicCompletedModalSpacebotAvatarView(imageResource: resource)
                }
            }
        }
        .padding()
    }
}
#endif
