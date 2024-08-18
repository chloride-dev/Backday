package cn.backday.event.impl.client

import com.darkmagician6.eventapi.events.Event

class TabListUpdateEvent(var tabListEntries: List<String>) : Event
