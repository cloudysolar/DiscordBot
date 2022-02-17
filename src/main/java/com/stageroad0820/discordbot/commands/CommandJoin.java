package com.stageroad0820.discordbot.commands;

import net.dv8tion.jda.api.audio.SpeakingMode;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

public class CommandJoin extends ListenerAdapter {
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) return;

		Message message = event.getMessage();
		String content = message.getContentRaw();

		TextChannel channel = (TextChannel) event.getChannel();

		Member self = event.getGuild().getSelfMember();
		Member member = event.getMember();

		GuildVoiceState selfState = self.getVoiceState();
		GuildVoiceState memberState = member.getVoiceState();

		AudioManager manager = event.getGuild().getAudioManager();
		VoiceChannel audioChannel = (VoiceChannel) memberState.getChannel();

		if (content.equalsIgnoreCase("!접속")) {
			System.out.println("[정보] 명령어 입력됨 : " + content);

			if (selfState.inAudioChannel()) {
				channel.sendMessage("이미 음성 채널에 접속해있습니다. 해당 명령을 사용할 수 없습니다!").queue();
				return;
			}

			if (!memberState.inAudioChannel()) {
				channel.sendMessage("봇에게 이 명령을 실행하려면 먼저 음성 채널에 접속해야 합니다. 음성 채널에 접속하신 뒤 해당 명령을 다시 실행해주세요.").queue();
				return;
			}

			manager.openAudioConnection(audioChannel);
			manager.setSpeakingMode(SpeakingMode.PRIORITY);
			channel.sendMessage("봇이 음성채널 `\uD83D\uDD0A" + audioChannel.getName() + "`에 " + manager.getSpeakingMode() + " 음성 모드 상태로 접속하였습니다.").queue();
			System.out.println("[정보] 사용자의 요청으로 봇을 음성채널 " + audioChannel.getName() + "에 접속시켰습니다.");
		}
		else if (content.equalsIgnoreCase("!접속해제")) {
			System.out.println("[정보] 명령어 입력됨 : " + content);

			if (!selfState.inAudioChannel()) {
				channel.sendMessage("음성 채널에 접속해있지 않습니다. 해당 명령을 사용할 수 없습니다!").queue();
				return;
			}

			String name = channel.getName();

			manager.closeAudioConnection();
			channel.sendMessage("봇이 음성채널 `\uD83D\uDD0A" + name + "`에서 접속을 해제하였습니다.").queue();
			System.out.println("[정보] 사용자의 요청으로 봇을 음성채널 " + name + "에서 접속 해제시켰습니다.");
		}
	}
}
