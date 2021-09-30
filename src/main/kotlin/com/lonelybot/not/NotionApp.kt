package com.lonelybot.not

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.lonelybot.*
import io.ktor.client.*
import io.ktor.client.features.observer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*

object NotionApp {
    private val ENDPOINT_QUERY = {tableId: String -> "https://api.notion.com/v1/databases/$tableId/query"}
    private lateinit var response: HttpResponse
    object request{
        private val client: HttpClient = HttpClient {
            install(ResponseObserver) {
                onResponse {
                    println(it.content.readUTF8Line())
                    response = it
                }
            }
        }

        object post {
            suspend fun <T: Any> queryDatabase(notionFilterBuilder: NotionFilterBuilder<T>, notionLogicalFilter: NotionLogicalFilter?, databaseId: String): HttpResponse{
                val filter = NotionFilter(notionLogicalFilter, notionFilterBuilder)
                return client.request {
                    headers {
                        append(HEADER_AUTH_NAME, "Bearer $NOTION_TOKEN")
                        append(HEADER_CONTENT_TYPE_NAME, "application/json")
                        append(HEADER_NOTION_VERSION_NAME, HEADER_NOTION_VERSION_VALUE)
                    }

                    method = HttpMethod.Post
                    url(databaseId.let(ENDPOINT_QUERY))
                    body = Gson().toJson(filter.filterObject)
                }
            }
        }
    }
}