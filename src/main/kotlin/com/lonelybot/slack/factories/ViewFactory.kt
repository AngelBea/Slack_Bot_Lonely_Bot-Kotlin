package com.lonelybot.slack.factories

import com.lonelybot.*
import com.lonelybot.adapters.SlackUserAdapter
import com.lonelybot.notion.decorators.MemeDecorator
import com.lonelybot.slack.SlackEmoji
import com.lonelybot.slack.builders.SlackViewBuilder


class ViewFactory {
    companion object{
        fun buildHomeForUser(user: SlackUserAdapter): SlackViewBuilder {            
            return SlackViewBuilder{
                home { 
                    toUser(user.slackId)
                    this append Modules.MOD_HOME_HEADER.invoke(user)
                    addDivider()
                    this append Modules.MOD_HOME_ACTIONS.invoke(1)
                    addDivider()
                    this append Modules.MOD_CHANGELOG_0_01_100
                    addDivider()
                    this append Modules.MOD_HOME_FOOTER
                }
            }   
        }
        
        
        fun buildLonelyRunMenu(user: SlackUserAdapter): SlackViewBuilder {
            return SlackViewBuilder {
                home {
                    toUser(user.slackId)
                    this append Modules.MOD_HOME_HEADER.invoke(user)
                    addDivider()
                    this append Modules.MOD_HOME_ACTIONS.invoke(2)
                    addDivider()
                    this append Modules.MOD_LONELYRUN_SAVE.invoke(user)
                    addDivider()
                    this append Modules.MOD_HOME_FOOTER
                }
            }
        }

        fun buildLonelyCardMenu(user: SlackUserAdapter): SlackViewBuilder {
            return SlackViewBuilder {
                home {
                    toUser(user.slackId)
                    this append Modules.MOD_HOME_HEADER.invoke(user)
                    addDivider()
                    this append Modules.MOD_HOME_ACTIONS.invoke(3)
                    addDivider()
                    this append Modules.MOD_LONELYCARD_SUGGEST.invoke(user)
                    addDivider()
                    this append Modules.MOD_HOME_FOOTER
                }
            }
        }
        
        fun buildLonelyMeMenu(user: SlackUserAdapter, withError: Boolean = false, toUser: SlackUserAdapter? = null): SlackViewBuilder {
            return SlackViewBuilder {
                home {
                    toUser(user.slackId)
                    this append Modules.MOD_HOME_HEADER.invoke(user)
                    addDivider()
                    this append Modules.MOD_HOME_ACTIONS.invoke(4)
                    addDivider()
                    addUserSelectionSection("Escoge a quien cotillear", "Busca un usuario", VIEW_HOME_SEL_USER) byId VIEW_HOME_SEL_USER_BLOCK_ID
                    if (withError){
                        addTextSection("No he encontrado el usuario que me has pedido. ¿No habrás elegido un bot? ${SlackEmoji.MONOCLE.value}")
                    }else{
                        this append Modules.MOD_LONELYME.invoke(toUser ?: user)                        
                    }
                    addDivider()
                    this append Modules.MOD_HOME_FOOTER
                }
            }
        }

        fun buildOAuthMenu(user: SlackUserAdapter, withError: Boolean = false, toUser: SlackUserAdapter? = null): SlackViewBuilder {
            return SlackViewBuilder {
                home {
                    toUser(user.slackId)
                    this append Modules.MOD_HOME_HEADER.invoke(user)
                    addDivider()
                    this append Modules.MOD_HOME_ACTIONS.invoke(5)
                    addDivider()
                    this append Modules.MOD_LONELY_AUTH.invoke(user)
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

        fun buildModalResponse(triggerId: String, message:String, fromUser: String, channelId: String): SlackViewBuilder{
            return SlackViewBuilder{
                modal(triggerId){
                    title(RESPONSE_MODAL_TITLE)
                    blocks {
                        addConversationSelectionSection(
                            "<@${fromUser}> escribió en <#$channelId> enviar respuesta a:", "Channel", RESPONSE_MODAL_CHANNEL_ID_ACTION, channelId, true
                        ) byId RESPONSE_MODAL_CHANNEL_ID
                        addTextSection("> :bust_in_silhouette: <@${fromUser}>\n>$message", true) byId RESPONSE_MODAL_MESSAGE_SECTION
                        addDivider()
                        addPlainTextInput(true, RESPONSE_MODAL_INPUT_MESSAGE_ID_ACTION, "Escribe tu mensaje: ") byId RESPONSE_MODAL_INPUT_MESSAGE_ID
                        close("Cerrar")
                        submit("¡Responder!")
                    }
                }
            } byId RESPONSE_MODAL_ID
        }
    }
}
