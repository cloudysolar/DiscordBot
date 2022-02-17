package com.stageroad0820.discordbot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {
	private static PlayerManager _instance;

	private static String songInfo;

	private final Map<Long, GuildMusicManager> musicManager;
	private final AudioPlayerManager audioPlayerManager;

	private PlayerManager() {
		this.musicManager = new HashMap<>();
		this.audioPlayerManager = new DefaultAudioPlayerManager();

		AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
		AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
	}

	public static PlayerManager getInstance() {
		if (_instance == null) {
			_instance = new PlayerManager();
		}

		return _instance;
	}

	public static String getSongInfo() {
		return songInfo;
	}

	public GuildMusicManager getMusicManager(Guild guild) {
		return this.musicManager.computeIfAbsent(guild.getIdLong(), (guildId) -> {
			final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);

			guild.getAudioManager().setSendingHandler(guildMusicManager.getHandler());

			return guildMusicManager;
		});
	}

	public void loadAndPlay(TextChannel channel, String trackUrl) {
		final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());

		this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(AudioTrack audioTrack) {
				musicManager.scheduler.queue(audioTrack);
				songInfo = audioTrack.getInfo().author + " - " + audioTrack.getInfo().title;

				channel.sendMessage("`" + songInfo + "` 노래가 재생목록에 추가되었습니다.").queue();
				System.out.println("[정보] " + songInfo + " 노래가 재생목록에 추가되었습니다.");
			}

			@Override
			public void playlistLoaded(AudioPlaylist audioPlaylist) {
				System.out.println("[정보] 재생목록이 준비되었습니다.");
			}

			@Override
			public void noMatches() {
				System.out.println("[정보] 일치하는 정보가 존재하지 않습니다.");
			}

			@Override
			public void loadFailed(FriendlyException e) {
				System.err.println("[오류!] 노래를 가져오는데 문제가 발생하여 재생할 수 없습니다. 아래 예외를 확인해주세요.");
				channel.sendMessage("문제가 발생하여 노래를 재생할 수 없습니다. 콘솔을 확인해주세요.").queue();
				e.printStackTrace();
			}
		});
	}
}
