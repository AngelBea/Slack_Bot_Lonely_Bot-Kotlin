package com.lonelybot.slack.factories

import com.lonelybot.*
import com.lonelybot.adapters.SlackUserAdapter
import com.lonelybot.services.global.isPermitted
import com.lonelybot.slack.SlackEmoji
import com.lonelybot.slack.SlackModule
import com.lonelybot.slack.SlackStyleButton
import java.sql.Time

class Modules {
    companion object{
        val MOD_HOME_ACTIONS = {primaryPosition: Int -> SlackModule{
                actions {
                    button("Changelog", VIEW_HOME_ACTION_ID_CHANGELOG, SlackEmoji.CURLED_PAPER, if (primaryPosition == 1) SlackStyleButton.PRIMARY else null)
                    button("Lonely Run", VIEW_HOME_ACTION_ID_LONELYRUN, SlackEmoji.RUNNER, if (primaryPosition == 2) SlackStyleButton.PRIMARY else null)
                    button("Lonely Card", VIEW_HOME_ACTION_ID_LONELYCARD, SlackEmoji.RED_SQUARE, if (primaryPosition == 3) SlackStyleButton.PRIMARY else null)
                    button("Lonely Me", VIEW_HOME_ACTION_ID_LONELYME, SlackEmoji.MONOCLE, if (primaryPosition == 4) SlackStyleButton.PRIMARY else null)
                }
            }
        }     
        
        val MOD_HOME_HEADER = { user: SlackUserAdapter -> SlackModule{
            addImageSection(user.let(VIEW_HOME_MESSAGE_MR_LONELY), VIEW_HOME_IMAGE_MR_LONELY, VIEW_HOME_ALT_MR_LONELY)
        }}
        
        val MOD_HOME_FOOTER = SlackModule {
            addContext {
                addMarkdownText("Puedes seguir el proyecto en <https://github.com/AngelBea/Slack_Bot_Lonely_Bot-Kotlin|Github>")
            }
        }
        
        val MOD_LONELYRUN_SAVE = { user: SlackUserAdapter -> SlackModule{
            val timePickerUserValueFriday = user.leavingOnFriday
            val timePickerValueFriday = if (timePickerUserValueFriday != null) Time.valueOf(timePickerUserValueFriday.plus(":00")) else VIEW_HOME_TIME_FRIDAY
            val timePickerUserValueWeek = user.leavingRestOfWeek
            val timePickerValueWeek = if (timePickerUserValueWeek != null) Time.valueOf(timePickerUserValueWeek.plus(":00")) else VIEW_HOME_TIME_WEEK

            if(user.isPermitted(Permissions.TIMEREMAINING)){
                addTimePickerSection(VIEW_HOME_DATEPICKER_FRIDAY, VIEW_HOME_ACTIONID_FRIDAY, VIEW_HOME_PLACEHOLDER_TIMEPICKER,  timePickerValueFriday) byId VIEW_HOME_SECTION_FRIDAY_ID
                addTimePickerSection(VIEW_HOME_DATEPICKER_WEEK, VIEW_HOME_ACTIONID_WEEK, VIEW_HOME_PLACEHOLDER_TIMEPICKER,  timePickerValueWeek) byId VIEW_HOME_SECTION_WEEK_ID
                addButtonSection( " ", "Guardar", VIEW_HOME_SAVE_BUTTON_ID)
            }
        }}
        
        val MOD_LONELYCARD_SUGGEST  = { user: SlackUserAdapter -> SlackModule{
            if (user.isPermitted(Permissions.CARD_ADMIN)){
                addStaticSelectSection(VIEW_INPUT_OPTION_MESSAGE, "tarjeta", VIEW_INPUT_OPTION_CARD_SELECTED_URL_ID, "Roja", "Amarilla") byId VIEW_INPUT_OPTION_CARD_URL_ID
                addPlainTextInput(false, VIEW_INPUT_CARD_URL_ID, " ") byId VIEW_INPUT_URL_BLOCK_ID
                addButtonSection( " ", "¡Subelo!", VIEW_HOME_SAVE_CARD_BUTTON_ID)
            }
        }}
        
        val MOD_LONELYME = { user: SlackUserAdapter -> SlackModule{
            if (user.isPermitted(Permissions.LONELYME)){
                addImage("https://i.imgur.com/UyAXgQj.png", "Banner Lonely Me")
                addImageSection("Te identifico como el esbirro <@${user.slackId}>", user.profileImgFull, user.slackUserName)
                addDivider()
                if (user.isPermitted(Permissions.CARDS)){
                    addTextSection("${SlackEmoji.YELLOW_SQUARE.value}${SlackEmoji.ARROW_FORWARD.value}Te han castigado con ${user.yellowCardsReceived} amarillas.")
                    addTextSection("${SlackEmoji.YELLOW_SQUARE.value}${SlackEmoji.ARROW_BACKWARD.value} Has castigado con ${user.yellowCardsShown} amarillas")
                    addTextSection("${SlackEmoji.RED_SQUARE.value}${SlackEmoji.ARROW_FORWARD.value} Has expulsado a alguien ${user.redCardsShown} veces")
                    addTextSection("${SlackEmoji.RED_SQUARE.value}${SlackEmoji.ARROW_BACKWARD.value} Te han expulsado ${user.redCardsReceived} veces")
                }else{
                    addTextSection("${SlackEmoji.YELLOW_SQUARE.value}${SlackEmoji.RED_SQUARE.value}No tengo información de expulsiones/castigos de tarjetas, puede ser porque no tengas permisos.")
                }

                if (user.isPermitted(Permissions.TIMEREMAINING) && user.leavingOnFriday != null && user.leavingRestOfWeek != null){
                    addTextSection("${SlackEmoji.CALENDAR.value} Empiezas a holgazanear a las ${user.leavingOnFriday} el Viernes")
                    addTextSection("${SlackEmoji.CALENDAR.value} Entre semana a las ${user.leavingRestOfWeek}")
                }else{
                    addTextSection("${SlackEmoji.CALENDAR.value} No tengo información de cuando sales, no podrás usar **/lonelyrun** si no me lo dices.")
                }
            }
        }}
        
        val MOD_CHANGELOG_0_01_100 = SlackModule{
            addTextSection("El primer changelog de Lonely Bot pasa por una remodelación completa de la Home de la APP. ¡Ahora Mr Lonely tiene su mansión a punto!")
            addHeader("V0.01.100")
            addDivider()
            addHeader("${SlackEmoji.RED_CIRCLE.value} UX")
            addTextSection("${SlackEmoji.ORANGE_CIRCLE.value} Ahora puedes navegar entre los distintos comandos y sus menús por medio de botones en la Home.")
            addTextSection("${SlackEmoji.ORANGE_CIRCLE.value} Se ha habilitado un changelog en la Home para trackear mejor los cambios que se hagan en el bot.")
            addHeader("${SlackEmoji.RED_CIRCLE.value} FUNCIONALIDADES")
            addTextSection("${SlackEmoji.ORANGE_CIRCLE.value} Se ha habilitado un prototipo de 'Lonely Who' a la pestaña de Lonely Me. Ahora puedes buscar usuarios y ver sus estadisticas. ¡Cuidado con los ${SlackEmoji.ROBOT.value}!")
            addDivider()
            addTextSection("Espero os haya gustado el primer changelog. ¡A romper el bot!")            
        }
    }
}