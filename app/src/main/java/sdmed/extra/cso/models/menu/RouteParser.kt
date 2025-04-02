package sdmed.extra.cso.models.menu

object RouteParser {
    fun routeToClass(route: String?): Route {
        return when (route) {
            RouteName.EDI -> Route.EDI.Main
            RouteName.PRICE -> Route.PRICE.Main
            RouteName.HOME -> Route.HOME.Main
            RouteName.QNA -> Route.QNA.Main
            RouteName.MY -> Route.MY.Main
            RouteName.LANDING -> Route.LANDING.Main
            RouteName.LOGIN -> Route.LOGIN.Main
            else -> Route.LANDING.Main
        }
    }
}