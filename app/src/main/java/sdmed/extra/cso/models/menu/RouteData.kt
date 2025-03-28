package sdmed.extra.cso.models.menu

data class RouteData(
    var path: String = "",
    var mother: String? = null,
    var navigation: Boolean = true
) {
    constructor(route: String): this() {
        this.path = route
    }
    fun thisMyRoute(route: String?): Boolean {
        route ?: return false
        return route.startsWith(path)
    }
}