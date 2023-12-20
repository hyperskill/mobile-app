import shared
import UIKit

enum ColorPalette {
    private static let sharedColors = SharedResources.colors.shared

    // MARK: Blue

    static let blue600 = sharedColors.color_blue_600.uiColor
    static let blue400 = sharedColors.color_blue_400.uiColor
    static let blue200 = sharedColors.color_blue_200.uiColor
    static let blue400Alpha60 = sharedColors.color_blue_400_alpha_60.uiColor
    static let blue400Alpha38 = sharedColors.color_blue_400_alpha_38.uiColor
    static let blue400Alpha12 = sharedColors.color_blue_400_alpha_12.uiColor
    static let blue400Alpha7 = sharedColors.color_blue_400_alpha_7.uiColor
    static let blue200Alpha60 = sharedColors.color_blue_200_alpha_60.uiColor
    static let blue200Alpha38 = sharedColors.color_blue_200_alpha_38.uiColor
    static let blue200Alpha12 = sharedColors.color_blue_200_alpha_12.uiColor
    static let blue200Alpha7 = sharedColors.color_blue_200_alpha_7.uiColor

    // MARK: White

    static let white50 = sharedColors.color_white_50.uiColor
    static let white50Alpha87 = sharedColors.color_white_50_alpha_87.uiColor
    static let white50Alpha60 = sharedColors.color_white_50_alpha_60.uiColor
    static let white50Alpha38 = sharedColors.color_white_50_alpha_38.uiColor
    static let white50Alpha12 = sharedColors.color_white_50_alpha_12.uiColor

    // MARK: Brown

    static let brown = sharedColors.color_brown.uiColor

    // MARK: Gray

    static let gray50 = sharedColors.color_gray_50.uiColor

    // MARK: Red

    static let colorRed500 = sharedColors.color_red_500.uiColor
    static let colorRed500Alpha12 = sharedColors.color_red_500_alpha_12.uiColor
    static let colorRed500Alpha7 = sharedColors.color_red_500_alpha_7.uiColor
    static let colorRed300 = sharedColors.color_red_300.uiColor
    static let colorRed300Alpha12 = sharedColors.color_red_300_alpha_12.uiColor
    static let colorRed300Alpha7 = sharedColors.color_red_300_alpha_7.uiColor

    // MARK: Orange

    static let orange900 = sharedColors.color_orange_900.uiColor
    static let orange900Alpha12 = sharedColors.color_orange_900_alpha_12.uiColor
    static let orange900Alpha7 = sharedColors.color_orange_900_alpha_7.uiColor
    static let orange300 = sharedColors.color_orange_300.uiColor
    static let orange300Alpha12 = sharedColors.color_orange_300_alpha_12.uiColor
    static let orange300Alpha7 = sharedColors.color_orange_300_alpha_7.uiColor

    // MARK: Yellow

    static let yellow300 = sharedColors.color_yellow_300.uiColor
    static let yellow300Alpha12 = sharedColors.color_yellow_300_alpha_12.uiColor
    static let yellow300Alpha7 = sharedColors.color_yellow_300_alpha_7.uiColor
    static let yellow200 = sharedColors.color_yellow_200.uiColor
    static let yellow200Alpha12 = sharedColors.color_yellow_200_alpha_12.uiColor
    static let yellow200Alpha7 = sharedColors.color_yellow_200_alpha_7.uiColor

    // MARK: Green

    static let green400 = sharedColors.color_green_400.uiColor
    static let green200 = sharedColors.color_green_200.uiColor
    static let green400Alpha60 = sharedColors.color_green_400_alpha_60.uiColor
    static let green400Alpha38 = sharedColors.color_green_400_alpha_38.uiColor
    static let green400Alpha12 = sharedColors.color_green_400_alpha_12.uiColor
    static let green400Alpha7 = sharedColors.color_green_400_alpha_7.uiColor
    static let green200Alpha60 = sharedColors.color_green_200_alpha_60.uiColor
    static let green200Alpha38 = sharedColors.color_green_200_alpha_38.uiColor
    static let green200Alpha12 = sharedColors.color_green_200_alpha_12.uiColor
    static let green200Alpha7 = sharedColors.color_green_200_alpha_7.uiColor

    // MARK: Black

    static let black900 = sharedColors.color_black_900.uiColor
    static let black850 = sharedColors.color_black_850.uiColor
    static let black300 = sharedColors.color_black_300.uiColor
    static let black900Alpha87 = sharedColors.color_black_900_alpha_87.uiColor
    static let black900Alpha60 = sharedColors.color_black_900_alpha_60.uiColor
    static let black900Alpha38 = sharedColors.color_black_900_alpha_38.uiColor
    static let black900Alpha12 = sharedColors.color_black_900_alpha_12.uiColor
    static let black900Alpha7 = sharedColors.color_black_900_alpha_7.uiColor

    // MARK: Violet

    static let violet400 = sharedColors.color_violet_400.uiColor
    static let violet400Alpha12 = sharedColors.color_violet_400_alpha_12.uiColor
    static let violet400Alpha7 = sharedColors.color_violet_400_alpha_7.uiColor
    static let violet200 = sharedColors.color_violet_200.uiColor
    static let violet200Alpha12 = sharedColors.color_violet_200_alpha_12.uiColor
    static let violet200Alpha7 = sharedColors.color_violet_200_alpha_7.uiColor

    // MARK: - AppTheme -

    // MARK: Background

    static let background = sharedColors.color_background.uiColor

    static let onBackground = sharedColors.color_on_background.uiColor

    // MARK: Primary

    static let primary = sharedColors.color_primary.uiColor
    static let primaryAlpha60 = sharedColors.color_primary_alpha_60.uiColor
    static let primaryAlpha38 = sharedColors.color_primary_alpha_38.uiColor

    static let primaryVariant = sharedColors.color_primary_variant.uiColor

    static let onPrimary = sharedColors.color_on_primary.uiColor
    static let onPrimaryAlpha87 = sharedColors.color_on_primary_alpha_87.uiColor
    static let onPrimaryAlpha60 = sharedColors.color_on_primary_alpha_60.uiColor

    // MARK: Secondary

    static let secondary = sharedColors.color_secondary.uiColor
    static let secondaryAlpha60 = sharedColors.color_secondary_alpha_60.uiColor
    static let secondaryAlpha38 = sharedColors.color_secondary_alpha_38.uiColor

    static let onSecondary = sharedColors.color_on_secondary.uiColor

    // MARK: Surface

    static let surface = sharedColors.color_surface.uiColor

    static let onSurface = sharedColors.color_on_surface.uiColor
    static let onSurfaceAlpha87 = sharedColors.color_on_surface_alpha_87.uiColor
    static let onSurfaceAlpha60 = sharedColors.color_on_surface_alpha_60.uiColor
    static let onSurfaceAlpha38 = sharedColors.color_on_surface_alpha_38.uiColor
    static let onSurfaceAlpha12 = sharedColors.color_on_surface_alpha_12.uiColor
    static let onSurfaceAlpha9 = sharedColors.color_on_surface_alpha_9.uiColor

    // MARK: Error

    static let error = sharedColors.color_error.uiColor

    static let onError = sharedColors.color_on_error.uiColor

    // MARK: - ThemeOverlay -

    // MARK: Blue

    static let overlayBlue = sharedColors.color_overlay_blue.uiColor
    static let overlayBlueAlpha12 = sharedColors.color_overlay_blue_alpha_12.uiColor
    static let overlayBlueAlpha7 = sharedColors.color_overlay_blue_alpha_7.uiColor
    static let overlayBlueBrand = sharedColors.color_overlay_blue_brand.uiColor

    // MARK: Orange

    static let overlayOrange = sharedColors.color_overlay_orange.uiColor
    static let overlayOrangeAlpha12 = sharedColors.color_overlay_orange_alpha_12.uiColor
    static let overlayOrangeAlpha7 = sharedColors.color_overlay_orange_alpha_7.uiColor

    // MARK: Green

    static let overlayGreen = sharedColors.color_overlay_green.uiColor
    static let overlayGreenAlpha12 = sharedColors.color_overlay_green_alpha_12.uiColor
    static let overlayGreenAlpha7 = sharedColors.color_overlay_green_alpha_7.uiColor

    // MARK: Yellow

    static let overlayYellow = sharedColors.color_overlay_yellow.uiColor
    static let overlayYellowAlpha12 = sharedColors.color_overlay_yellow_alpha_12.uiColor
    static let overlayYellowAlpha7 = sharedColors.color_overlay_yellow_alpha_7.uiColor

    // MARK: Violet

    static let overlayViolet = sharedColors.color_overlay_violet.uiColor
    static let overlayVioletAlpha12 = sharedColors.color_overlay_violet_alpha_12.uiColor
    static let overlayVioletAlpha7 = sharedColors.color_overlay_violet_alpha_7.uiColor

    // MARK: Red

    static let overlayRed = sharedColors.color_overlay_red.uiColor
    static let overlayRedAlpha12 = sharedColors.color_overlay_red_alpha_12.uiColor
    static let overlayRedAlpha7 = sharedColors.color_overlay_red_alpha_7.uiColor

    // MARK: - New Colors -

    static let newTextPrimary = sharedColors.text_primary.uiColor
    static let newTextSecondary = sharedColors.text_secondary.uiColor
    static let newTextOnColor = sharedColors.text_on_color.uiColor

    static let newButtonPrimary = sharedColors.button_primary.uiColor
    static let newButtonPrimaryActive = sharedColors.button_primary_active.uiColor

    static let newButtonTertiary = sharedColors.button_tertiary.uiColor
    static let newButtonTertiaryActive = sharedColors.button_tertiary_active.uiColor

    static let newButtonGhost = sharedColors.button_ghost.uiColor
    static let newButtonGhostActive = sharedColors.button_ghost_active.uiColor

    static let newButtonDisabled = sharedColors.button_disabled.uiColor

    static let newLayer1 = sharedColors.layer_1.uiColor
}
