package com.stageroad0820.discordbot.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CommandPing extends ListenerAdapter {
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		// 본인이 입력한 메시지는 무시하도록 설정
		if (event.getAuthor().isBot()) return;

		Message message = event.getMessage();
		String content = message.getContentRaw();

		if (content.equalsIgnoreCase("!듀")) {
			System.out.println("[정보] 명령어 입력됨 : " + content);

			MessageChannel channel = event.getChannel();
			channel.sendMessage("퐁이다 이 자식아!").queue();
		}
	}
}
