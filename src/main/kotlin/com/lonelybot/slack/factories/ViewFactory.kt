package com.lonelybot.slack.factories

import com.lonelybot.*
import com.lonelybot.adapters.SlackUserAdapter
import com.lonelybot.notion.decorators.MemeDecorator
import com.lonelybot.services.global.isPermitted
import com.lonelybot.slack.builders.SlackViewBuilder
import java.sql.Time


class ViewFactory {
    companion object{
        fun buildHomeForUser(user: SlackUserAdapter): SlackViewBuilder {            
            return SlackViewBuilder{
                val timePickerUserValueFriday = user.leavingOnFriday
                val timePickerValueFriday = if (timePickerUserValueFriday != null) Time.valueOf(timePickerUserValueFriday.plus(":00")) else VIEW_HOME_TIME_FRIDAY
                val timePickerUserValueWeek = user.leavingRestOfWeek
                val timePickerValueWeek = if (timePickerUserValueWeek != null) Time.valueOf(timePickerUserValueWeek.plus(":00")) else VIEW_HOME_TIME_WEEK
                
                home { 
                    toUser(user.slackId)
                    addImageSection(user.let(VIEW_HOME_MESSAGE_MR_LONELY), VIEW_HOME_IMAGE_MR_LONELY, VIEW_HOME_ALT_MR_LONELY)
                    addDivider()
                    if(user.isPermitted(Permissions.TIMEREMAINING)){
                        addTimePickerSection(VIEW_HOME_DATEPICKER_FRIDAY, VIEW_HOME_ACTIONID_FRIDAY, VIEW_HOME_PLACEHOLDER_TIMEPICKER,  timePickerValueFriday) byId VIEW_HOME_SECTION_FRIDAY_ID
                        addTimePickerSection(VIEW_HOME_DATEPICKER_WEEK, VIEW_HOME_ACTIONID_WEEK, VIEW_HOME_PLACEHOLDER_TIMEPICKER,  timePickerValueWeek) byId VIEW_HOME_SECTION_WEEK_ID
                        addButtonSection( " ", "Guardar", VIEW_HOME_SAVE_BUTTON_ID)
                    }
                    if (user.isPermitted(Permissions.CARD_ADMIN)){
                        addDivider()
                        addStaticSelectSection(VIEW_INPUT_OPTION_MESSAGE, "tarjeta", VIEW_INPUT_OPTION_CARD_SELECTED_URL_ID, "Roja", "Amarilla") byId VIEW_INPUT_OPTION_CARD_URL_ID
                        addPlainTextInput(false, VIEW_INPUT_CARD_URL_ID, " ") byId VIEW_INPUT_URL_BLOCK_ID
                        addButtonSection( " ", "¡Subelo!", VIEW_HOME_SAVE_CARD_BUTTON_ID)
                    }
                    addDivider()
                    addContext { 
                        addMarkdownText("Puedes seguir el proyecto en <https://github.com/AngelBea/Slack_Bot_Lonely_Bot-Kotlin|Github>")
                    }
                }
            }   
        }
        
        fun buildLoadingModal(externalId: String, triggerId: String): SlackViewBuilder{
            return SlackViewBuilder{
                modal(triggerId){
                    title("Cargando...")
                    close("Cancelar")
                    blocks { 
                        addImage("https://i.imgur.com/IfjQjcJ.gif", "Loading")
                    }
                }
            } byId externalId
        }
        
        fun buildLoadingModalError(externalId: String, triggerId: String, msg: String): SlackViewBuilder {
            return SlackViewBuilder{
                modal(triggerId){
                    title("Error")
                    close("OK!")
                    blocks { 
                        addTextSection(msg)
                    }
                }
            } byId externalId
        }
        
        fun buildModalYellowCard(fromUser: SlackUserAdapter, toUser: SlackUserAdapter, triggerId: String, channelId: String): SlackViewBuilder{
            return SlackViewBuilder{
                modal(triggerId){
                    title(VIEW_MODAL_YELLOW_CARD_TITLE)
                    blocks {
                        addUserSelectionSection(
                            VIEW_MODAL_YELLOW_CARD_TEXT, VIEW_MODAL_YELLOW_CARD_PH, VIEW_MODAL_YELLOW_USER_CARD_ACTION_ID, toUser.slackId
                        ) byId VIEW_MODAL_YELLOW_CARD_USER_BLOCK_ID
                        addDivider()
                        addPlainTextInput(false, VIEW_MODAL_YELLOW_CARD_TEXT_ACTION_ID, " ") byId VIEW_MODAL_YELLOW_CARD_TEXT_BLOCK_ID
                        addDivider()
                        addConversationSelectionSection(
                            VIEW_MODAL_CARD_CHANNEL_TEXT, VIEW_MODAL_CARD_CHANNEL_PH, VIEW_MODAL_YELLOW_CARD_CHANNEL_ACTION_ID, channelId
                        ) byId VIEW_MODAL_YELLOW_CARD_CHANNEL_BLOCK_ID
                    }
                    close(VIEW_MODAL_YELLOW_CARD_CLOSE)
                    submit(VIEW_MODAL_YELLOW_CARD_SUBMIT)
                }
            } byId VIEW_MODAL_YELLOW_CARD_ID
        }
        
        fun buildModalRedCard(fromUser: SlackUserAdapter, toUser: SlackUserAdapter, triggerId: String, channelId: String): SlackViewBuilder{
            return SlackViewBuilder{
                modal(triggerId){
                    title(VIEW_MODAL_RED_CARD_TITLE)
                    blocks {
                        addUserSelectionSection(
                            VIEW_MODAL_RED_CARD_TEXT, VIEW_MODAL_RED_CARD_PH, VIEW_MODAL_RED_USER_CARD_ACTION_ID, toUser.slackId
                        ) byId VIEW_MODAL_RED_CARD_USER_BLOCK_ID
                        addDivider()
                        addPlainTextInput(false, VIEW_MODAL_RED_CARD_TEXT_ACTION_ID, " ") byId VIEW_MODAL_RED_CARD_TEXT_BLOCK_ID
                        addDivider()
                        addConversationSelectionSection(
                            VIEW_MODAL_CARD_CHANNEL_TEXT, VIEW_MODAL_CARD_CHANNEL_PH, VIEW_MODAL_RED_CARD_CHANNEL_ACTION_ID, channelId
                        ) byId VIEW_MODAL_RED_CARD_CHANNEL_BLOCK_ID
                    }
                    close(VIEW_MODAL_RED_CARD_CLOSE)
                    submit(VIEW_MODAL_RED_CARD_SUBMIT)
                }
            } byId VIEW_MODAL_RED_CARD_ID
        }
        
        fun buildModalUploadConfirmation(triggerId: String, meme: MemeDecorator): SlackViewBuilder {
            return SlackViewBuilder{
                modal(triggerId){
                    title(UPLOAD_MODAL_TITLE)
                    blocks { 
                        addTextSection(UPLOAD_MODAL_MSG_IMG)
                        addImage(meme.url!!, meme.name!!)
                        addDivider()
                        addTextSection(UPLOAD_MODAL_MSG_URL.plus(" ").plus("<${meme.url}|${meme.name}>"))
                    }
                    close("OK!")
                } 
            } byId UPLOAD_MODAL_ID
        }
        
    }
}
