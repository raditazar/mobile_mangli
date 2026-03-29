package ui.navigation

sealed class Screen{
    data object Login: Screen()
    data object Dashboard: Screen()
    data object Scanner: Screen()
    data object Pos: Screen()
    data object Profile: Screen()
    data object PackageManagement: Screen()
}