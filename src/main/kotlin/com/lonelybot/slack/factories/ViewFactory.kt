package com.lonelybot.slack.factories

import com.lonelybot.*
import com.lonelybot.adapters.SlackUserAdapter
import com.lonelybot.not.SlackUser
import com.lonelybot.services.notion.isPermitted
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
                }
            }   
        }
    }
}
