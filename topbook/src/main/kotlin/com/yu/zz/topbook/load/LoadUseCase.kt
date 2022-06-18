package com.yu.zz.topbook.load

interface LoadUseCase<Request, Response> {
    fun load(request: Request): Response
}

data class LoadRequestBean<Keyword>(val keyword: Keyword, val start: Int, val limit: Int)


interface ListUseCase<Bean> {

    fun stretch(list: List<Bean>): List<Bean>

    fun replace(list: List<Bean>): List<Bean>

    fun capture(): List<Bean>
}

class ListUseCaseImpl<Bean> : ListUseCase<Bean> {
    private val mList = mutableListOf<Bean>()

    override fun stretch(list: List<Bean>): List<Bean> {
        mList.addAll(list)
        return capture()
    }

    override fun replace(list: List<Bean>): List<Bean> {
        mList.clear()
        return capture();
    }

    override fun capture(): List<Bean> {
        return mList
    }

}