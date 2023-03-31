package com.lonelybot.slack

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.lonelybot.NotionTags

data class Params(val token: String, val team_id: String, val team_domain: String, val channel_id: String,
                  val channel_name: String, val user_id: String, val user_name: String, val command: String, val text: String,
                  val api_app_id: String, val is_enterprise_install: String, val response_url: String, val trigger_id: String)
data class Message(val channel: String, val text: String, val user: String? = null, val username: String = "Señor Feudal Mister Lonely", val blocks: MutableList<SlackBlock> = mutableListOf())

open class SlackBlock(val type: String, var block_id: String?)
data class SlackImageBlock(@SerializedName("image_url")val imageUrl: String, @SerializedName("alt_text") val altText: String, val title: SlackText?): SlackBlock(type = BlockType.IMAGE.typeName, block_id = "id_$altText")
data class SlackTextSectionBlock(val text: SlackTextElement): SlackBlock(BlockType.SECTION.typeName, null)
data class SlackMultiSection(val fields: List<SlackTextElement>): SlackBlock(BlockType.SECTION.typeName, null)
data class SlackAccessorySection(val text: SlackTextElement, val accessory: SlackAccessory): SlackBlock(BlockType.SECTION.typeName, null)
class SlackDivider: SlackBlock(BlockType.DIVIDER.typeName, null)
data class SlackHeader(val text: SlackText): SlackBlock(BlockType.HEADER.typeName, null)

open class Element(val type: String?)
interface SlackTextElement
interface SlackAccessory
data class SlackText(val text: String) : Element(ElementType.TEXT.typeName), SlackTextElement
data class SlackMarkdown(val text: String) : Element(ElementType.MARKDOWN.typeName), SlackTextElement
data class SlackUserSelect(val placeholder: SlackText, @SerializedName("action_id") val actionId: String): Element(ElementType.USERS_SELECT.typeName), SlackAccessory
data class SlackMultiConversationSelect(val placeholder: SlackText, @SerializedName("action_id") val actionId: String): Element(ElementType.MULTI_CONVERSATIONS_SELECT.typeName), SlackAccessory
data class SlackStaticSelect(val placeholder: SlackText, val options: MutableList<SlackOption>): Element(ElementType.STATIC_SELECT.typeName), SlackAccessory
data class SlackImage(@SerializedName("image_url")val imageUrl: String, @SerializedName("alt_text") val altText: String): Element(ElementType.IMAGE.typeName), SlackAccessory
data class SlackButton(val text: SlackText, val value: String, @SerializedName("action_id") val actionId: String): Element(ElementType.BUTTON.typeName), SlackAccessory
data class SlackOverflow(@SerializedName("action_id") val actionId: String, val options: MutableList<SlackOption>): Element(ElementType.OVERFLOW.typeName), SlackAccessory
data class SlackDatePicker(val placeholder: SlackText, @SerializedName("action_id") val actionId: String, @SerializedName("initial_date") val initialDate: String): Element(ElementType.DATE_PICKER.typeName), SlackAccessory
data class SlackTimePicker(val placeholder: SlackText, @SerializedName("action_id") val actionId: String, @SerializedName("initial_time") val initialTime: String): Element(ElementType.TIMEPICKER.typeName), SlackAccessory
data class SlackCheckboxes(val options: MutableList<SlackOption>, @SerializedName("action_id") val actionId: String): Element(ElementType.CHECKBOXES.typeName), SlackAccessory 
data class SlackRadioButtons(val options: MutableList<SlackOption>, @SerializedName("action_id") val actionId: String): Element(ElementType.RADIO_BUTTONS.typeName), SlackAccessory 

open class SlackView(val type: String)
data class SlackHomeView(val blocks: MutableList<SlackBlock>): SlackView(ViewTypes.APP_HOME.typeName)

data class SlackOption(val text: SlackTextElement, val value: String, val description: SlackTextElement? = null)

data class Card(val fromUser: String, val color: NotionTags, val toUser: String, val onChannel: String, val reason: String? = null)

open class SlackAction(val type: String?, val team: Team?, val user: User?, val channel: Channel?, @SerializedName("callback_id") val callbackId: String?, 
                              @SerializedName("response_url") val responseUrl: String?)
data class SlackMessageAction(val message: Message): SlackAction(null, null, null, null, null, null)
data class SlackValues(val values: JsonObject)



data class Team(val id: String, val domain: String)

data class User(val id: String, val username: String, @SerializedName("team_id") val teamId: String, val name: String)

data class Channel(
    val id: String,
    val name: String?,
    @SerializedName("is_channel") val isChannel: Boolean?,
    @SerializedName("is_group") val isGroup: Boolean?,
    @SerializedName("is_im") val isIM: Boolean?,
    val created: Long, val creator: String?,
    @SerializedName("is_archived") val isArchived: Boolean?,
    @SerializedName("is_general") val isGeneral: Boolean?,
    val unlinked: Int?,
    @SerializedName("name_normalized") val nameNormalized: String?,
    @SerializedName("is_shared") val isShared: Boolean?,
    @SerializedName("is_ext_shared") val isExtShared: Boolean?,
    @SerializedName("is_org_shared") val isOrgShared: Boolean?,
    val pending_shared: List<String>?,
    @SerializedName("is_pending_ext_shared") val isPendingExtShared: Boolean?,
    @SerializedName("is_private") val isPrivate: Boolean?,
    @SerializedName("is_mpim") val isMpim: Boolean?,
    @SerializedName("previous_names") val previousNames: List<String>?
)

data class SlackEvent(val token: String, @SerializedName("team_id") val teamId: String, @SerializedName("api_app_id") val apiAppId: String, val event: SlackEventData,
                      val type: String, @SerializedName("event_id") val eventId: String, @SerializedName("event_time") val eventTime: Long,
                      val authorizations: List<SlackEventAuthorization>, @SerializedName("is_ext_shared_channel") val isExtSharedChannel: Boolean,
                      val challenge: String)

data class SlackEventData(val type: String, val user: String, val channel: String, val tab: String, @SerializedName("event_ts") val eventTs: String)

data class SlackEventAuthorization(@SerializedName("enterprise_id") val enterpriseId: String?, @SerializedName("team_id") val teamId: String,
                                   @SerializedName("user_id") val userId: String, @SerializedName("is_bot") val isBot: Boolean,
                                   @SerializedName("is_enterprise_install") val isEnterpriseInstall: Boolean)
