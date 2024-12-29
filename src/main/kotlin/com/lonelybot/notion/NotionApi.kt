package com.lonelybot.notion

import com.lonelybot.API
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.netty.handler.codec.http.HttpHeaderValues
import me.angelbea.application.notion.builders.NotionFilter
import me.angelbea.application.notion.builders.NotionPageBuilder

object NotionApi: API() {
    private val ENDPOINT_QUERY = { tableId: String -> "https://api.notion.com/v1/databases/$tableId/query"}
    private val ENDPOINT_PAGE_CREATION = "https://api.notion.com/v1/pages"
    private val ENDPOINT_PAGE_UPDATE = { pageId: String -> "https://api.notion.com/v1/pages/$pageId" }
    private val ENDPOINT_PAGE_CHILDREN = { pageId: String -> "https://api.notion.com/v1/blocks/$pageId/children" }

    object Databases{
        suspend fun queryDatabase(notionFilter: NotionFilter, databaseId: String): HttpResponse{
            return client.request {
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${System.getenv("NOTION_TOKEN")}")
                    append(HttpHeaders.ContentType, HttpHeaderValues.APPLICATION_JSON.toString())
                    append(NotionCustomHeaders.Headers.NOTION_VERSION, NotionCustomHeaders.Values.VERSION_22_06_28)
                }

                method = HttpMethod.Post
                url(databaseId.let(ENDPOINT_QUERY))
                setBody(json.toJson(notionFilter))
            }
        }
    }
    object Pages {
        suspend fun insertPage(page: NotionPageBuilder): HttpResponse{
            return client.request{
                headers{
                    append(HttpHeaders.Authorization, "Bearer ${System.getenv("NOTION_TOKEN")}")
                    append(HttpHeaders.ContentType, HttpHeaderValues.APPLICATION_JSON.toString())
                    append(NotionCustomHeaders.Headers.NOTION_VERSION, NotionCustomHeaders.Values.VERSION_22_06_28)
                }

                method = HttpMethod.Post
                url(ENDPOINT_PAGE_CREATION)
                val body = page.toJson()
                setBody(body)
            }
        }

        suspend fun updatePage(id: String, builder: NotionPageBuilder): HttpResponse{
            return client.request{
                headers{
                    append(HttpHeaders.Authorization, "Bearer ${System.getenv("NOTION_TOKEN")}")
                    append(HttpHeaders.ContentType, HttpHeaderValues.APPLICATION_JSON.toString())
                    append(NotionCustomHeaders.Headers.NOTION_VERSION, NotionCustomHeaders.Values.VERSION_22_06_28)
                }

                method = HttpMethod.Patch
                url(id.let(ENDPOINT_PAGE_UPDATE))
                val body = mapOf("properties" to builder.properties)
                setBody(json.toJson(body))
            }
        }
    }

    object Blocks{
        suspend fun getChildren(id: String): HttpResponse{
            return client.request{
                headers{
                    append(HttpHeaders.Authorization, "Bearer ${System.getenv("NOTION_TOKEN")}")
                    append(HttpHeaders.ContentType, HttpHeaderValues.APPLICATION_JSON.toString())
                    append(NotionCustomHeaders.Headers.NOTION_VERSION, NotionCustomHeaders.Values.VERSION_22_06_28)
                }

                method = HttpMethod.Get
                url(id.let(ENDPOINT_PAGE_CHILDREN))
            }
        }
    }
}