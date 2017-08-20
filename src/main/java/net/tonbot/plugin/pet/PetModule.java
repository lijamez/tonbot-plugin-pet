package net.tonbot.plugin.pet;

import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;

import net.tonbot.common.BotUtils;
import net.tonbot.common.Prefix;

class PetModule extends AbstractModule {

	private final String prefix;
	private final BotUtils botUtils;

	public PetModule(String prefix, BotUtils botUtils) {
		this.prefix = Preconditions.checkNotNull(prefix, "prefix must be non-null.");
		this.botUtils = Preconditions.checkNotNull(botUtils, "botUtils must be non-null.");
	}

	@Override
	protected void configure() {
		bind(BotUtils.class).toInstance(botUtils);
		bind(String.class).annotatedWith(Prefix.class).toInstance(prefix);
	}
}
