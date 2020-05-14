package io.legado.app.help

import android.os.Handler
import android.os.Looper
import io.legado.app.App
import io.legado.app.data.entities.BookSource
import io.legado.app.data.entities.RssSource
import io.legado.app.utils.splitNotBlank
import org.jetbrains.anko.toast

object SourceHelp {

    private val handler = Handler(Looper.getMainLooper())
    private val list18Plus: Array<String> by lazy {
        try {
            String(App.INSTANCE.assets.open("18PlusList.txt").readBytes()).splitNotBlank("\n")
        } catch (e: Exception) {
            arrayOf()
        }
    }

    fun insertRssSource(vararg rssSources: RssSource) {
        rssSources.forEach { rssSource ->
            if (is18Plus(rssSource.sortUrl)) {
                handler.post {
                    App.INSTANCE.toast("${rssSource.sourceName}是18+网址,禁止导入.")
                }
            } else {
                App.db.rssSourceDao().insert(rssSource)
            }
        }
    }

    fun insertBookSource(vararg bookSources: BookSource) {
        App.db.bookSourceDao().insert(*bookSources)
    }

    private fun is18Plus(url: String?): Boolean {
        url ?: return false
        list18Plus.forEach {
            if (url.contains(it)) {
                return true
            }
        }
        return false
    }

}