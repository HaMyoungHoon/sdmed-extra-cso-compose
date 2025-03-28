package sdmed.extra.cso.models.menu

sealed interface Route {
    val data: RouteData
    open class EDI(final override val data: RouteData = RouteData(RouteName.EDI)): Route {
        data object Main: EDI()
    }
    open class PRICE(final override val data: RouteData = RouteData(RouteName.PRICE)): Route {
        data object Main: PRICE()
    }
    open class HOME(final override val data: RouteData = RouteData(RouteName.HOME)): Route {
        data object Main: HOME()
    }
    open class QNA(final override val data: RouteData = RouteData(RouteName.QNA)): Route {
        data object Main: QNA()
    }
    open class MY(final override val data: RouteData = RouteData(RouteName.MY)): Route {
        data object Main: MY()
    }

    open class LANDING(final override val data: RouteData = RouteData(RouteName.LANDING)): Route {
        data object Main: LANDING(RouteData(RouteName.LANDING, RouteName.LANDING, false))
    }

    open class LOGIN(final override val data: RouteData = RouteData(RouteName.LOGIN)): Route {
        data object Main: LANDING(RouteData(RouteName.LOGIN, RouteName.LOGIN, false))
    }
}