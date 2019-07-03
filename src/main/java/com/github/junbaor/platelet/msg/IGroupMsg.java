package com.github.junbaor.platelet.msg;

import java.util.List;

public interface IGroupMsg {

    boolean sendTextMsg(String content, List<String> mentionedList, List<String> mentionedMobileList);

    boolean sendMarkdownMsg(String markdown);

    default boolean sendTextMsg(String content) {
        return sendTextMsg(content, null, null);
    }

    default boolean sendTextMsg(String content, List<String> mentionedList) {
        return sendTextMsg(content, mentionedList, null);
    }

}
