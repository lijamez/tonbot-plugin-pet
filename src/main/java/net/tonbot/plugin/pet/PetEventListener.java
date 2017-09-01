package net.tonbot.plugin.pet;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;

import net.tonbot.common.BotUtils;
import net.tonbot.common.Prefix;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

class PetEventListener {

	private static final List<String> TRIGGER_WORDS = ImmutableList.of(
			"good bot",
			"nice bot",
			"cool bot",
			"pet pet");

	private static final List<String> EMOTE_URLS = ImmutableList.of(
			"https://cdn.discordapp.com/emojis/317088491370708992.png", // :blobMelt:
			"https://cdn.discordapp.com/emojis/317089314196422657.png", // :blobAww:
			"https://cdn.discordapp.com/emojis/316447368268480512.png", // :blobUwu:
			"https://cdn.discordapp.com/emojis/319329339680489472.png" // :blobPats:
	);

	private final String prefix;
	private final BotUtils botUtils;

	@Inject
	public PetEventListener(@Prefix String prefix, BotUtils botUtils) {
		this.botUtils = Preconditions.checkNotNull(botUtils, "botUtils must be non-null.");
		this.prefix = Preconditions.checkNotNull(prefix, "prefix must be non-null.");
	}

	@EventSubscriber
	public void onMessageReceived(MessageReceivedEvent event) {
		if (EMOTE_URLS.isEmpty()) {
			return;
		}

		String messageContent = event.getMessage().getContent();

		boolean triggered = TRIGGER_WORDS.stream()
				.filter(trigger -> StringUtils.startsWithIgnoreCase(messageContent, prefix + " " + trigger)
						|| StringUtils.startsWithIgnoreCase(messageContent, trigger))
				.findAny()
				.isPresent();

		if (triggered) {
			EmbedBuilder embedBuilder = new EmbedBuilder();

			String emoteUrl = pickRandomEmote();
			embedBuilder.withImage(emoteUrl);

			botUtils.sendEmbed(event.getChannel(), embedBuilder.build());
		}
	}

	private String pickRandomEmote() {
		int index = ThreadLocalRandom.current().nextInt(0, EMOTE_URLS.size());
		return EMOTE_URLS.get(index);
	}
}
