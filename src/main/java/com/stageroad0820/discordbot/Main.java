package com.stageroad0820.discordbot;

import com.stageroad0820.discordbot.commands.CommandJoin;
import com.stageroad0820.discordbot.commands.CommandPing;
import com.stageroad0820.discordbot.commands.CommandPlay;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Main {
	// 디스코드 봇 로그인에 필요한 토큰
	private static final String TOKEN = "봇 토큰";

	private static JDABuilder builder;

	public static JDABuilder getBuilder() {
		return builder;
	}

	public static void main(String[] args) {
		// JDABuilder를 이용하면 디스코드 봇에 대한 추가적인 설정이 가능함.
		builder = JDABuilder.createDefault(TOKEN);

		/*
			- setAutoReconnect(true) : 봇과의 연결이 끊어졌을 때 자동으로 재연결하도록 설정
			- setStatus(OnlineStatus) : 디스코드 봇의 온라인 상태를 설정
				- ONLINE : (초록색) 온라인
				- DO_NOT_DISTRUB : (빨강색) 다른 용무 중
				- IDLE : (노란색) 자리 비움
				- INVISIBLE : (회색) 오프라인으로 표시
				- OFFLINE : (회색) 오프라인
				- UNKNOWN : 사용 불가
			- setActivity(Activity) : 디스코드 봇의 활동 상태를 설정
				- playing : ~ 하는 중
				- competing : ~ 참가 중
				- listening : ~ 듣는 중
				- streaming : ~ 방송 중 / ~ 하는 중
				- watching : ~ 시청 중
		 */
		builder.setAutoReconnect(true);
		builder.setStatus(OnlineStatus.ONLINE);

		builder.addEventListeners(new CommandPing());
		builder.addEventListeners(new CommandPlay());
		builder.addEventListeners(new CommandJoin());

		try {
			JDA jda = builder.build();
			System.out.println("[정보] 디스코드 봇 " + jda.getSelfUser().getName() + "에게 정상적으로 로그인되었습니다.");
		} catch (LoginException e) {
			System.err.println("[오류!] 해당 디스코드 봇에 로그인 할 수 없습니다. 아래 오류 내용을 확인해주세요.");
		}
	}
}
