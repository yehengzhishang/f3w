package com.yu.zz.topbook.load

interface AnswerUseCase<Word, Result> {
    fun ask(word: Word): Result
}

data class ListRequestBean<Keyword>(val keyword: Keyword, val start: Int, val limit: Int)


interface ListUseCase<Bean> {

    fun stretch(list: List<Bean>?): List<Bean>

    fun replace(list: List<Bean>?): List<Bean>

    fun capture(): List<Bean>
}


class ListUseCaseImpl<Bean> constructor() : ListUseCase<Bean> {
    private val mList = mutableListOf<Bean>()

    override fun stretch(list: List<Bean>?): List<Bean> {
        list?.let { mList.addAll(it) }
        return capture()
    }

    override fun replace(list: List<Bean>?): List<Bean> {
        mList.clear()
        return stretch(list)
    }

    override fun capture(): List<Bean> {
        return mList.toList()
    }

}