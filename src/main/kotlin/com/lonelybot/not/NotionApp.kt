package com.lonelybot.not

import com.google.gson.Gson
import com.lonelybot.*
import io.ktor.client.*
import io.ktor.client.plugins.observer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*

object NotionApp {
    private val ENDPOINT_QUERY = {tableId: String -> "https://api.notion.com/v1/databases/$tableId/query"}
    private val ENDPOINT_PAGE_CREATION = "https://api.notion.com/v1/pages"
    private val ENDPOINT_PAGE_UPDATE = { pageId: String -> "https://api.notion.com/v1/pages/$pageId" }

    private lateinit var response: HttpResponse
    object request{
        private val client: HttpClient = HttpClient {
            install(ResponseObserver) {
                onResponse {
                    it.bodyAsChannel().readUTF8Line().let(::println)
                    response = it
                }
            }
        }

        object post {
            suspend fun queryDatabase(notionFilterBuilder: NotionFilterBuilder, notionLogicalFilter: NotionLogicalFilter?, databaseId: String): HttpResponse{
                val filter = NotionFilter(notionLogicalFilter, notionFilterBuilder)
                return client.request {
                    headers {
                        append(HEADER_AUTH_NAME, "Bearer $NOTION_TOKEN")
                        append(HEADER_CONTENT_TYPE_NAME, "application/json")
                        append(HEADER_NOTION_VERSION_NAME, HEADER_NOTION_VERSION_VALUE)
                    }

                    method = HttpMethod.Post
                    url(databaseId.let(ENDPOINT_QUERY))
                    setBody(Gson().toJson(filter.filterObject)) 
                }
            }

            suspend fun insertPage(page: NotionPageBuilder): HttpResponse{
                return client.request{
                    headers{
                        append(HEADER_AUTH_NAME, "Bearer $NOTION_TOKEN")
                        append(HEADER_CONTENT_TYPE_NAME, "application/json")
                        append(HEADER_NOTION_VERSION_NAME, HEADER_NOTION_VERSION_VALUE)
                    }

                    method = HttpMethod.Post
                    url(ENDPOINT_PAGE_CREATION)
                    val body = page.toJson()
                    body.let(::println)
                    setBody(body) 
                }
            }

            suspend fun updatePage(id: String, builder: NotionPageBuilder): HttpResponse{
                return client.request{
                    headers{
                        append(HEADER_AUTH_NAME, "Bearer $NOTION_TOKEN")
                        append(HEADER_CONTENT_TYPE_NAME, "application/json")
                        append(HEADER_NOTION_VERSION_NAME, HEADER_NOTION_VERSION_VALUE)
                    }

                    method = HttpMethod.Patch
                    url(id.let(ENDPOINT_PAGE_UPDATE))
                    val body = mapOf("properties" to builder.properties)
                    body.let(::println)
                    setBody(Gson().toJson(body))
                }
            }
        }
    }
}