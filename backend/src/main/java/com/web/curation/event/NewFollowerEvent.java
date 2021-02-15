package com.web.curation.event;

import com.web.curation.domain.User;
import lombok.Getter;

@Getter
public class NewFollowerEvent {

    private User from;
    private User to;

    public NewFollowerEvent(User from, User to) {
        this.from = from;
        this.to = to;
    }
}
