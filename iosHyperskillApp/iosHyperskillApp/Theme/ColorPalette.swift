import shared
import UIKit

enum ColorPalette {
    private static let sharedColors = SharedResources.colors.shared

    // MARK: Blue

    static let blue600 = Self.sharedColors.color_blue_600.color.uiColor
    static let blue400 = Self.sharedColors.color_blue_400.color.uiColor
    static let blue200 = Self.sharedColors.color_blue_200.color.uiColor
    static let blue400Alpha60 = Self.sharedColors.color_blue_400_alpha_60.color.uiColor
    static let blue400Alpha38 = Self.sharedColors.color_blue_400_alpha_38.color.uiColor
    static let blue400Alpha12 = Self.sharedColors.color_blue_400_alpha_12.color.uiColor
    static let blue400Alpha7 = Self.sharedColors.color_blue_400_alpha_7.color.uiColor
    static let blue200Alpha60 = Self.sharedColors.color_blue_200_alpha_60.color.uiColor
    static let blue200Alpha38 = Self.sharedColors.color_blue_200_alpha_38.color.uiColor
    static let blue200Alpha12 = Self.sharedColors.color_blue_200_alpha_12.color.uiColor
    static let blue200Alpha7 = Self.sharedColors.color_blue_200_alpha_7.color.uiColor

    // MARK: White

    static let white50 = Self.sharedColors.color_white_50.color.uiColor
    static let white50Alpha87 = Self.sharedColors.color_white_50_alpha_87.color.uiColor
    static let white50Alpha75 = Self.sharedColors.color_white_50_alpha_75.color.uiColor
    static let white50Alpha50 = Self.sharedColors.color_white_50_alpha_50.color.uiColor
    static let white50Alpha25 = Self.sharedColors.color_white_50_alpha_25.color.uiColor

    // MARK: Brown
    
    static let brown = Self.sharedColors.color_brown.color.uiColor
    
    // MARK: Gray

    static let gray50 = Self.sharedColors.color_gray_50.color.uiColor

    // MARK: Red

    static let colorRed500 = Self.sharedColors.color_red_500.color.uiColor
    static let colorRed500Alpha12 = Self.sharedColors.color_red_500_alpha_12.color.uiColor
    static let colorRed500Alpha7 = Self.sharedColors.color_red_500_alpha_7.color.uiColor
    static let colorRed300 = Self.sharedColors.color_red_300.color.uiColor
    static let colorRed300Alpha12 = Self.sharedColors.color_red_300_alpha_12.color.uiColor
    static let colorRed300Alpha7 = Self.sharedColors.color_red_300_alpha_7.color.uiColor

    // MARK: Orange

    static let orange900 = Self.sharedColors.color_orange_900.color.uiColor
    static let orange900Alpha12 = Self.sharedColors.color_orange_900_alpha_12.color.uiColor
    static let orange900Alpha7 = Self.sharedColors.color_orange_900_alpha_7.color.uiColor
    static let orange300 = Self.sharedColors.color_orange_300.color.uiColor
    static let orange300Alpha12 = Self.sharedColors.color_orange_300_alpha_12.color.uiColor
    static let orange300Alpha7 = Self.sharedColors.color_orange_300_alpha_7.color.uiColor

    // MARK: Yellow

    static let yellow300 = Self.sharedColors.color_yellow_300.color.uiColor
    static let yellow300Alpha12 = Self.sharedColors.color_yellow_300_alpha_12.color.uiColor
    static let yellow300Alpha7 = Self.sharedColors.color_yellow_300_alpha_7.color.uiColor
    static let yellow200 = Self.sharedColors.color_yellow_200.color.uiColor
    static let yellow200Alpha12 = Self.sharedColors.color_yellow_200_alpha_12.color.uiColor
    static let yellow200Alpha7 = Self.sharedColors.color_yellow_200_alpha_7.color.uiColor

    // MARK: Green

    static let green400 = Self.sharedColors.color_green_400.color.uiColor
    static let green200 = Self.sharedColors.color_green_200.color.uiColor
    static let green400Alpha60 = Self.sharedColors.color_green_400_alpha_60.color.uiColor
    static let green400Alpha38 = Self.sharedColors.color_green_400_alpha_38.color.uiColor
    static let green400Alpha12 = Self.sharedColors.color_green_400_alpha_12.color.uiColor
    static let green400Alpha7 = Self.sharedColors.color_green_400_alpha_7.color.uiColor
    static let green200Alpha60 = Self.sharedColors.color_green_200_alpha_60.color.uiColor
    static let green200Alpha38 = Self.sharedColors.color_green_200_alpha_38.color.uiColor
    static let green200Alpha12 = Self.sharedColors.color_green_200_alpha_12.color.uiColor
    static let green200Alpha7 = Self.sharedColors.color_green_200_alpha_7.color.uiColor

    // MARK: Black

    static let black900 = Self.sharedColors.color_black_900.color.uiColor
    static let black850 = Self.sharedColors.color_black_850.color.uiColor
    static let black300 = Self.sharedColors.color_black_300.color.uiColor
    static let black900Alpha87 = Self.sharedColors.color_black_900_alpha_87.color.uiColor
    static let black900Alpha75 = Self.sharedColors.color_black_900_alpha_75.color.uiColor
    static let black900Alpha50 = Self.sharedColors.color_black_900_alpha_50.color.uiColor
    static let black900Alpha25 = Self.sharedColors.color_black_900_alpha_25.color.uiColor

    // MARK: Violet

    static let violet400 = Self.sharedColors.color_violet_400.color.uiColor
    static let violet400Alpha12 = Self.sharedColors.color_violet_400_alpha_12.color.uiColor
    static let violet400Alpha7 = Self.sharedColors.color_violet_400_alpha_7.color.uiColor
    static let violet200 = Self.sharedColors.color_violet_200.color.uiColor
    static let violet200Alpha12 = Self.sharedColors.color_violet_200_alpha_12.color.uiColor
    static let violet200Alpha7 = Self.sharedColors.color_violet_200_alpha_7.color.uiColor

    // MARK: - AppTheme -

    // MARK: Background

    static let background = Self.sharedColors.color_background.dynamicUIColor

    static let onBackground = Self.sharedColors.color_on_background.dynamicUIColor

    // MARK: Primary

    static let primary = Self.sharedColors.color_primary.dynamicUIColor
    static let primaryAlpha60 = Self.sharedColors.color_primary_alpha_60.dynamicUIColor
    static let primaryAlpha38 = Self.sharedColors.color_primary_alpha_38.dynamicUIColor

    static let primaryVariant = Self.sharedColors.color_primary_variant.color.uiColor

    static let onPrimary = Self.sharedColors.color_on_primary.dynamicUIColor

    // MARK: Secondary

    static let secondary = Self.sharedColors.color_secondary.dynamicUIColor
    static let secondaryAlpha60 = Self.sharedColors.color_secondary_alpha_60.dynamicUIColor
    static let secondaryAlpha38 = Self.sharedColors.color_secondary_alpha_38.dynamicUIColor

    static let onSecondary = Self.sharedColors.color_on_secondary.dynamicUIColor

    // MARK: Surface

    static let surface = Self.sharedColors.color_surface.dynamicUIColor

    static let onSurface = Self.sharedColors.color_on_surface.dynamicUIColor
    static let onSurfaceAlpha87 = Self.sharedColors.color_on_surface_alpha_87.dynamicUIColor
    static let onSurfaceAlpha75 = Self.sharedColors.color_on_surface_alpha_75.dynamicUIColor
    static let onSurfaceAlpha50 = Self.sharedColors.color_on_surface_alpha_50.dynamicUIColor
    static let onSurfaceAlpha25 = Self.sharedColors.color_on_surface_alpha_25.dynamicUIColor

    // MARK: Error

    static let error = Self.sharedColors.color_error.dynamicUIColor

    static let onError = Self.sharedColors.color_on_error.dynamicUIColor

    // MARK: - ThemeOverlay -

    // MARK: Blue

    static let overlayBlue = Self.sharedColors.color_overlay_blue.dynamicUIColor
    static let overlayBlueAlpha12 = Self.sharedColors.color_overlay_blue_alpha_12.dynamicUIColor
    static let overlayBlueAlpha7 = Self.sharedColors.color_overlay_blue_alpha_7.dynamicUIColor

    // MARK: Orange

    static let overlayOrange = Self.sharedColors.color_overlay_orange.dynamicUIColor
    static let overlayOrangeAlpha12 = Self.sharedColors.color_overlay_orange_alpha_12.dynamicUIColor
    static let overlayOrangeAlpha7 = Self.sharedColors.color_overlay_orange_alpha_7.dynamicUIColor

    // MARK: Green

    static let overlayGreen = Self.sharedColors.color_overlay_green.dynamicUIColor
    static let overlayGreenAlpha12 = Self.sharedColors.color_overlay_green_alpha_12.dynamicUIColor
    static let overlayGreenAlpha7 = Self.sharedColors.color_overlay_green_alpha_7.dynamicUIColor

    // MARK: Yellow

    static let overlayYellow = Self.sharedColors.color_overlay_yellow.dynamicUIColor
    static let overlayYellowAlpha12 = Self.sharedColors.color_overlay_yellow_alpha_12.dynamicUIColor
    static let overlayYellowAlpha7 = Self.sharedColors.color_overlay_yellow_alpha_7.dynamicUIColor

    // MARK: Violet

    static let overlayViolet = Self.sharedColors.color_overlay_violet.dynamicUIColor
    static let overlayVioletAlpha12 = Self.sharedColors.color_overlay_violet_alpha_12.dynamicUIColor
    static let overlayVioletAlpha7 = Self.sharedColors.color_overlay_violet_alpha_7.dynamicUIColor

    // MARK: Red

    static let overlayRed = Self.sharedColors.color_overlay_red.dynamicUIColor
    static let overlayRedAlpha12 = Self.sharedColors.color_overlay_red_alpha_12.dynamicUIColor
    static let overlayRedAlpha7 = Self.sharedColors.color_overlay_red_alpha_7.dynamicUIColor
}
