package com.yu.zz.topbook.employ

const val TOPBOOK_URL_BASE = "https://topbook.cc/"

const val TOPBOOK_PATH_CATEGORY_ID = "categoryId"
const val TOPBOOK_URL_PATH_PREFIX = "webapi/content/article/{$TOPBOOK_PATH_CATEGORY_ID}/page"

const val TOPBOOK_URL_PAGE = "webapi/content/category/page"

const val TOPBOOK_URL_TOPIC_LIST = "webapi/community/topic/page"

const val TOPBOOK_URL_TOPIC_VIEWPOINTS = "webapi/community/viewpoint/page"

const val TOPBOOK_URL_SEARCH_PREFIX = "webapi/search/"
const val TOPBOOK_URL_SEARCH_TOPICS = "${TOPBOOK_URL_SEARCH_PREFIX}topics"
//fun getTopBookUrlPathString(pos: String): String = String.format(TOPBOOK_URL_PATH_PREFIX, pos)