package com.github.junbaor.platelet.listener;

import cn.hutool.core.date.DateUtil;
import com.github.junbaor.platelet.msg.GroupMsg;
import com.github.junbaor.platelet.util.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.models.Job;
import org.gitlab4j.api.webhook.*;
import org.slf4j.MDC;

import javax.inject.Named;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Named
@Slf4j
public class WebhookListener implements WebHookListener {

    @Override
    public void onPushEvent(PushEvent event) {
        String botKey = MDC.get("key");

        List<EventCommit> commits = event.getCommits();
        Collections.reverse(commits);

        StringBuilder content = new StringBuilder();
        content.append(event.getUserName())
                .append(" pushed to branch ")
                .append("[").append(event.getBranch()).append("]")
                .append("(").append(event.getProject().getWebUrl()).append("/tree/").append(event.getBranch()).append(")")
                .append(" at repository ")
                .append("[").append(event.getProject().getName()).append("]")
                .append("(").append(event.getProject().getWebUrl()).append(")")
                .append("\n");

        for (EventCommit commit : commits) {
            String shortId = commit.getId().substring(0, 6);
            content.append("> ")
                    .append("[").append(shortId).append("]")
                    .append("(").append(commit.getUrl()).append(")")
                    .append(" : ")
                    .append(commit.getMessage())
                    .append("\n");
        }

        GroupMsg.getInstance(botKey).sendMarkdownMsg(content.toString());
    }

    @Override
    public void onMergeRequestEvent(MergeRequestEvent event) {
        String botKey = MDC.get("key");

        log.debug(event.toString());

        StringBuilder content = new StringBuilder();
        String sourceBranch = event.getObjectAttributes().getSourceBranch();
        String sourceBranchUrl = event.getProject().getWebUrl() + "/tree/" + sourceBranch;
        String targetBranch = event.getObjectAttributes().getTargetBranch();
        String targetBranchUrl = event.getProject().getWebUrl() + "/tree/" + targetBranch;
        String state = event.getObjectAttributes().getState();

        String action = "open";
        if (event.getChanges().getTitle() != null) {
            action = "update";
        }
        if (Objects.equals(state, "merged")) {
            action = "merged";
        }
        if (Objects.equals(state, "closed")) {
            action = "close";
        }

        content.append(event.getUser().getName())
                .append(" ")
                .append(action)
                .append(" the merge request from branch ")
                .append("[").append(sourceBranch).append("]")
                .append("(").append(sourceBranchUrl).append(")")
                .append(" to ")
                .append("[").append(targetBranch).append("]")
                .append("(").append(targetBranchUrl).append(")")
                .append("\n")
                .append("> ")
                .append("[").append(event.getObjectAttributes().getTitle()).append("]")
                .append("(").append(event.getObjectAttributes().getUrl()).append(")")
                .append("\n")
                .append("Status : ").append(state).append(" | ").append(event.getObjectAttributes().getMergeStatus()).append("\n")
                .append("Repository : ")
                .append("[").append(event.getProject().getName()).append("]")
                .append("(").append(event.getProject().getWebUrl()).append(")");

        GroupMsg.getInstance(botKey).sendMarkdownMsg(content.toString());
    }

    @Override
    public void onTagPushEvent(TagPushEvent event) {
        String botKey = MDC.get("key");

        log.debug(event.toString());

        StringBuilder content = new StringBuilder();
        String projectName = event.getProject().getName();
        String projectUrl = event.getProject().getWebUrl();
        String tagName = event.getRef().replaceAll("refs/tags/", "");
        String tagUrl = projectUrl + "/tree/" + tagName;

        content.append(event.getUserName())
                .append(" pushed tag ")
                .append("[").append(tagName).append("]")
                .append("(").append(tagUrl).append(")")
                .append(" at repository ")
                .append("[").append(projectName).append("]")
                .append("(").append(projectUrl).append(")");

        GroupMsg.getInstance(botKey).sendMarkdownMsg(content.toString());
    }

    @Override
    public void onPipelineEvent(PipelineEvent event) {
        String botKey = MDC.get("key");

        StringBuilder content = new StringBuilder();

        Map<String,String> queryParams = RequestUtils.getQueryMap(event.getRequestQueryString());

        if(queryParams.containsKey("branch") && !event.getObjectAttributes().getRef().equals(queryParams.get("branch"))){
            return;
        }

        content.append("**").append(event.getProject().getName()).append(" pipeline event**\n");
        content.append("> \n");
        content.append("> branch:").append(event.getObjectAttributes().getRef()).append("\n");
        content.append("> \n");
        content.append("> user:").append(event.getUser().getName()).append("\n");
        content.append("> \n");
        content.append("> commit message:").append(event.getCommit().getMessage()).append("\n");
        content.append("> \n");
        content.append("> start at:").append(DateUtil.format(event.getObjectAttributes().getCreatedAt(), "yyyy-MM-dd HH:mm:ss")).append("\n");
        content.append("> \n");
        content.append("> end at:").append(DateUtil.format(event.getObjectAttributes().getFinishedAt(), "yyyy-MM-dd HH:mm:ss")).append("\n");
        content.append("> \n");
        content.append("> status:").append(event.getObjectAttributes().getStatus()).append("\n");

        GroupMsg.getInstance(botKey).sendMarkdownMsg(content.toString());
    }
}
