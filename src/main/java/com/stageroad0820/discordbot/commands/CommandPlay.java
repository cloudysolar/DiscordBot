package com.stageroad0820.discordbot.commands;

import com.stageroad0820.discordbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CommandPlay extends ListenerAdapter {
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) return;

		Message message = event.getMessage();
		String content = message.getContentRaw();

		if (content.equalsIgnoreCase("!재생")) {
			System.out.println("[정보] 명령어 입력됨 : " + content);

			TextChannel channel = (TextChannel) event.getChannel();

			Member self = event.getGuild().getSelfMember();
			Member member = event.getMember();

			GuildVoiceState selfState = self.getVoiceState();
			GuildVoiceState memberState = member.getVoiceState();

			if (!selfState.inAudioChannel()) {
				channel.sendMessage("현재 봇이 음성 채널에 접속해있지 않아 재생 명령을 실행할 수 없습니다. `!접속`을 이용해 봇을 먼저 음성 채널에 접속시켜주세요.").queue();
				return;
			}

			if (!memberState.inAudioChannel()) {
				channel.sendMessage("봇에게 이 명령을 실행하려면 먼저 음성 채널에 접속해야 합니다. 음성 채널에 접속하신 뒤 해당 명령을 다시 실행해주세요.").queue();
				return;
			}

			if (!memberState.getChannel().equals(selfState.getChannel())) {
				channel.sendMessage("봇과 같은 음성 채널에 접속해있어야 합니다. 봇이 접속해있는 채널로 이동해주세요.").queue();
				return;
			}

			String url = "https://www.youtube.com/watch?v=1JYqC1cNlcc";

			PlayerManager.getInstance().loadAndPlay(channel, url);
			System.out.println("[정보] " + channel.getName() + " 음성채널에서 유튜브 링크 [" + url + "] (을)를 재생합니다.");
		}
	}
}
