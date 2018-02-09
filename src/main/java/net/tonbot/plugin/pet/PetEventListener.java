package net.tonbot.plugin.pet;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;

import net.tonbot.common.BotUtils;
import net.tonbot.common.Prefix;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

class PetEventListener {
	
	private static final Map<List<String>, List<String>> TRIGGERS_AND_EMOTES = ImmutableMap.of(
			ImmutableList.of(
					"good bot",
					"nice bot",
					"cool bot",
					"pet pet"),
			ImmutableList.of(
					"https://cdn.discordapp.com/emojis/317088491370708992.png", // :blobMelt:
					"https://cdn.discordapp.com/emojis/396521774461747210.gif", // :ablobAww:
					"https://cdn.discordapp.com/emojis/316447368268480512.png", // :blobUwu:
					"https://cdn.discordapp.com/emojis/319329339680489472.png", // :blobPats:
					"https://cdn.discordapp.com/emojis/395192995315580938.gif", // :ablobsmilehappy:
					"https://cdn.discordapp.com/emojis/393999959227236362.png", // :blobxd:
					"https://cdn.discordapp.com/emojis/357833988208984064.png", // :owoblobgrin:
					"https://cdn.discordapp.com/emojis/384454064299048960.png" // :blobhugsmiley:
			),
			ImmutableList.of(
					"bad bot"),
			ImmutableList.of(
					"https://cdn.discordapp.com/emojis/381954537122037770.png", // :blobeyessad:
					"https://cdn.discordapp.com/emojis/363166255412150275.png", // :blobsadlife:
					"https://cdn.discordapp.com/emojis/381954611394641930.png", // :blobeyescry:
					"https://cdn.discordapp.com/emojis/395193003620171776.gif", // :ablobfrown:
					"https://cdn.discordapp.com/emojis/357829175576363018.png", // :owoblobfrowningbig:
					"https://cdn.discordapp.com/emojis/376003414951198721.png", // :blobglareawkward:
					"https://cdn.discordapp.com/emojis/365925855668600842.png", // :geblobtilt:
					"https://cdn.discordapp.com/emojis/396521773719617537.gif" // :ablobsweats:
			));

	private final String prefix;
	private final BotUtils botUtils;

	@Inject
	public PetEventListener(@Prefix String prefix, BotUtils botUtils) {
		this.botUtils = Preconditions.checkNotNull(botUtils, "botUtils must be non-null.");
		this.prefix = Preconditions.checkNotNull(prefix, "prefix must be non-null.");
	}

	@EventSubscriber
	public void onMessageReceived(MessageReceivedEvent event) {
		if (TRIGGERS_AND_EMOTES.isEmpty()) {
			return;
		}

		String messageContent = event.getMessage().getContent();

		TRIGGERS_AND_EMOTES.entrySet().stream()
				.filter(entry -> {
					List<String> triggers = entry.getKey();
					return triggers.stream()
						.filter(trigger -> StringUtils.startsWithIgnoreCase(messageContent, prefix + " " + trigger)
								|| StringUtils.startsWithIgnoreCase(messageContent, trigger))
						.findAny()
						.isPresent();
					
				})
				.findAny()
				.ifPresent(entry -> {
					EmbedBuilder embedBuilder = new EmbedBuilder();

					String emoteUrl = pickRandom(entry.getValue());
					embedBuilder.withImage(emoteUrl);

					botUtils.sendEmbed(event.getChannel(), embedBuilder.build());
				});
	}

	private String pickRandom(List<String> list) {
		int randomIndex = ThreadLocalRandom.current().nextInt(0, list.size());
		return list.get(randomIndex);
	}
}
