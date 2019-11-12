package com.yu.zz.tb.deep

const val TOPBOOK_URL_BASE = "https://topbook.cc/"

const val TOPBOOK_INDEX_TECHNIQUE = 9

const val TOPBOOK_URL_TECHNIQUE = "webapi/content/article/${TOPBOOK_INDEX_TECHNIQUE}/page"

const val TOPBOOK_URL_PATH_PREFIX = "webapi/content/article/{index}/page"
//fun getTopBookUrlPathString(pos: String): String = String.format(TOPBOOK_URL_PATH_PREFIX, pos)