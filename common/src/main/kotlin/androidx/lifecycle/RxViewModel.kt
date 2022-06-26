package androidx.lifecycle

import androidx.lifecycle.ViewModel
import com.yu.zz.bypass.RxCompositeDisposable
import java.io.Closeable


private const val keyRcd: String = "12312312"

val ViewModel.rcd: RxCompositeDisposable
    get() {
        val wrapper: RxCompositeDisposableWrapper? = this.getTag(keyRcd)
        if (wrapper != null) {
            return wrapper.rcd
        }
        return setTagIfAbsent(keyRcd, RxCompositeDisposableWrapper(RxCompositeDisposable())).rcd
    }

class RxCompositeDisposableWrapper constructor(val rcd: RxCompositeDisposable) : Closeable {
    override fun close() {
        if (!rcd.isDisposed) {
            rcd.dispose()
        }
    }
}

