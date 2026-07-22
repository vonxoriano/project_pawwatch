package edu.cit.soriano.pawwatch.mobile.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import edu.cit.soriano.pawwatch.mobile.ui.features.admin.AdminDashboardScreen
import edu.cit.soriano.pawwatch.mobile.ui.features.application.MyApplicationsScreen
import edu.cit.soriano.pawwatch.mobile.ui.features.animal.AnimalBrowseScreen
import edu.cit.soriano.pawwatch.mobile.ui.features.animal.AnimalDetailScreen
import edu.cit.soriano.pawwatch.mobile.ui.features.animal.FavoritesScreen
import edu.cit.soriano.pawwatch.mobile.ui.features.application.ApplyApplicationScreen
import edu.cit.soriano.pawwatch.mobile.ui.features.auth.LoginScreen
import edu.cit.soriano.pawwatch.mobile.ui.features.auth.RegisterScreen
import edu.cit.soriano.pawwatch.mobile.ui.features.profile.ProfileScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val ADOPTER_DASHBOARD = "adopter_dashboard"
    const val ADMIN_DASHBOARD = "admin_dashboard"
    const val ANIMAL_DETAIL = "animal_detail/{animalId}"
    const val MY_APPLICATIONS = "my_applications"
    const val FAVORITES = "favorites"
    const val PROFILE = "profile"
    const val APPLY_APPLICATION = "apply_application/{animalId}"

    fun animalDetail(animalId: Long) = "animal_detail/$animalId"
    fun applyApplication(animalId: Long) = "apply_application/$animalId"
}

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LOGIN) {

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { role ->
                    if (role == "ADMIN") {
                        navController.navigate(Routes.ADMIN_DASHBOARD) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Routes.ADOPTER_DASHBOARD) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.ADOPTER_DASHBOARD) {
            AnimalBrowseScreen(
                onAnimalClick = { animalId ->
                    navController.navigate(Routes.animalDetail(animalId))
                },
                onMyApplicationsClick = {
                    navController.navigate(Routes.MY_APPLICATIONS)
                },
                onFavoritesClick = {
                    navController.navigate(Routes.FAVORITES)
                },
                onProfileClick = {
                    navController.navigate(Routes.PROFILE)
                },
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.ADOPTER_DASHBOARD) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Routes.ANIMAL_DETAIL,
            arguments = listOf(navArgument("animalId") { type = NavType.LongType })
        ) { backStackEntry ->
            val animalId = backStackEntry.arguments?.getLong("animalId") ?: return@composable
            AnimalDetailScreen(
                animalId = animalId,
                onBack = { navController.popBackStack() },
                onApplyClick = { id ->
                    navController.navigate(Routes.applyApplication(id))
                }
            )
        }

        composable(
            route = Routes.APPLY_APPLICATION,
            arguments = listOf(navArgument("animalId") { type = NavType.LongType })
        ) { backStackEntry ->
            val animalId = backStackEntry.arguments?.getLong("animalId") ?: return@composable
            ApplyApplicationScreen(
                animalId = animalId,
                onBack = { navController.popBackStack() },
                onSubmitted = {
                    navController.navigate(Routes.MY_APPLICATIONS) {
                        popUpTo(Routes.ADOPTER_DASHBOARD)
                    }
                }
            )
        }

        composable(Routes.MY_APPLICATIONS) {
            MyApplicationsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.FAVORITES) {
            FavoritesScreen(
                onAnimalClick = { animalId ->
                    navController.navigate(Routes.animalDetail(animalId))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.ADMIN_DASHBOARD) {
            AdminDashboardScreen(
                onProfileClick = {
                    navController.navigate(Routes.PROFILE)
                },
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.ADMIN_DASHBOARD) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}