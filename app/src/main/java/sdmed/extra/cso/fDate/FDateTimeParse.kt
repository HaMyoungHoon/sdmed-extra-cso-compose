package sdmed.extra.cso.fDate

object FDateTimeParse {
    // 일단 yyyy-MM-dd 한정
    fun parse(data: String, localize: FLocalize): FDateTime {
        val fDateTime2 = FDateTime2().setThis(data)
        return FDateTime().setThis(fDateTime2.getYear(), fDateTime2.getMonth(), fDateTime2.getDay())
    }
    fun tryParseQuoteString(format: String, pos: Int, result: StringBuilder): Pair<Boolean, Int> {
        var ret: Pair<Boolean, Int> = Pair(false, 0)
        val formatLen = format.length
        val beginPos = pos
        var posBuff = pos
        val quoteChar = format[posBuff++]
        var foundQuote = false
        while (posBuff < formatLen) {
            val ch = format[posBuff++]
            if (ch == quoteChar) {
                foundQuote = true
                break
            } else if (ch == '\\') {
                if (posBuff < formatLen) {
                    result.append(format[posBuff++])
                } else {
                    return ret
                }
            } else {
                result.append(ch)
            }
        }
        if (!foundQuote) {
            return ret
        }

        ret = Pair(true, posBuff - beginPos)
        return ret
    }
}