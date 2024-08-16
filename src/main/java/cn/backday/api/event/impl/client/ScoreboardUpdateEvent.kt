package cn.backday.api.event.impl.client

import com.darkmagician6.eventapi.events.Event

class ScoreboardUpdateEvent(var scoreboardLines: List<String>) : Event
