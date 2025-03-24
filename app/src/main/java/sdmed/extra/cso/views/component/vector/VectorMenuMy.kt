package sdmed.extra.cso.views.component.vector

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp

class VectorMenuMy: FVectorBase() {
    override var viewportWidth = 960F
    override var viewportHeight = 960F
    override fun vector(tintColor: Color, fillColor: Color): ImageVector {
        return ImageVector.Builder(name, defaultWidth, defaultHeight, viewportWidth, viewportHeight).apply {
            addPath(fill = SolidColor(fillColor),
                pathData = PathParser().parsePathString("M680,841Q672,841 664,839Q656,837 649,832L529,762Q515,754 507.5,740.5Q500,727 500,711L500,570Q500,554 507.5,540.5Q515,527 529,519L649,449Q656,444 664,442Q672,440 680,440Q688,440 695.5,442.5Q703,445 710,449L830,519Q844,527 852,540.5Q860,554 860,570L860,711Q860,727 852,740.5Q844,754 830,762L710,832Q703,836 695.5,838.5Q688,841 680,841").toNodes())
            addPath(fill = SolidColor(fillColor),
                pathData = PathParser().parsePathString("M400,480Q334,480 287,433Q240,386 240,320Q240,254 287,207Q334,160 400,160Q466,160 513,207Q560,254 560,320Q560,386 513,433Q466,480 400,480").toNodes())
            addPath(fill = SolidColor(fillColor),
                pathData = PathParser().parsePathString("M80,800L80,688Q80,655 97,626Q114,597 144,582Q195,556 259,538Q323,520 400,520Q408,520 414,520Q420,520 426,522Q418,540 412.5,559.5Q407,579 404,600L400,600Q329,600 272.5,618Q216,636 180,654Q171,659 165.5,668Q160,677 160,688L160,720L412,720Q418,741 428,761.5Q438,782 450,800L80,800").toNodes())
            addPath(fill = SolidColor(fillColor),
                pathData = PathParser().parsePathString("M400,400Q433,400 456.5,376.5Q480,353 480,320Q480,287 456.5,263.5Q433,240 400,240Q367,240 343.5,263.5Q320,287 320,320Q320,353 343.5,376.5Q367,400 400,400").toNodes())
            addPath(fill = SolidColor(fillColor),
                pathData = PathParser().parsePathString("M400,320Q400,320 400,320Q400,320 400,320Q400,320 400,320Q400,320 400,320Q400,320 400,320Q400,320 400,320Q400,320 400,320Q400,320 400,320").toNodes())
            addPath(fill = SolidColor(fillColor),
                pathData = PathParser().parsePathString("M412,720L412,720L412,720Q412,720 412,720Q412,720 412,720Q412,720 412,720Q412,720 412,720L412,720Q412,720 412,720Q412,720 412,720").toNodes())
            addPath(fill = SolidColor(fillColor),
                pathData = PathParser().parsePathString("M586,554L680,609L774,554L680,500L586,554").toNodes())
            addPath(fill = SolidColor(fillColor),
                pathData = PathParser().parsePathString("M710,762L800,710L800,600L710,653L710,762").toNodes())
            addPath(fill = SolidColor(fillColor),
                pathData = PathParser().parsePathString("M560,710L650,763L650,654L560,601L560,710").toNodes())
        }.build()
    }

    @Preview
    @Composable
    fun previewMenuMy(@PreviewParameter(PreviewColorProvider::class) data: Pair<Color, Color>) {
        Surface { screen(data.first, data.second, 24.dp) }
    }
}