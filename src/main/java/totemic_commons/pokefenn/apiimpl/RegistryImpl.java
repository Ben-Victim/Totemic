package totemic_commons.pokefenn.apiimpl;

import java.util.*;

import totemic_commons.pokefenn.api.TotemicRegistry;
import totemic_commons.pokefenn.api.ceremony.Ceremony;
import totemic_commons.pokefenn.api.music.MusicInstrument;
import totemic_commons.pokefenn.api.totem.TotemEffect;
import totemic_commons.pokefenn.util.MathsUtil;
import vazkii.botania.totemic_custom.api.lexicon.LexiconCategory;

public class RegistryImpl implements TotemicRegistry
{
    private final Map<String, TotemEffect> totemEffects = new HashMap<>();
    private final List<String> totemList = new ArrayList<>();

    private final Map<String, MusicInstrument> instruments = new HashMap<>();

    private final Map<String, Ceremony> ceremonies = new HashMap<>();

    private final List<LexiconCategory> categories = new ArrayList<>();

    @Override
    public TotemEffect addTotem(TotemEffect effect)
    {
        if(totemEffects.containsKey(effect.getName()))
            throw new IllegalArgumentException("Duplicate Totem entry for ID " + effect.getName());
        totemEffects.put(effect.getName(), effect);
        totemList.add(effect.getName());
        return effect;
    }

    @Override
    public TotemEffect getTotem(String name)
    {
        return totemEffects.get(name);
    }

    @Override
    public Map<String, TotemEffect> getTotems()
    {
        return Collections.unmodifiableMap(totemEffects);
    }

    public List<String> getTotemList()
    {
        return Collections.unmodifiableList(totemList);
    }

    @Override
    public MusicInstrument addInstrument(MusicInstrument instrument)
    {
        if(instruments.containsKey(instrument.getName()))
            throw new IllegalArgumentException("Duplicate Music instrument entry for ID " + instrument.getName());
        instruments.put(instrument.getName(), instrument);
        return instrument;
    }

    @Override
    public MusicInstrument getInstrument(String name)
    {
        return instruments.get(name);
    }

    @Override
    public Map<String, MusicInstrument> getInstruments()
    {
        return Collections.unmodifiableMap(instruments);
    }

    @Override
    public Ceremony addCeremony(Ceremony ceremony)
    {
        if(ceremonies.containsKey(ceremony.getName()))
            throw new IllegalArgumentException("Duplicate Ceremony entry for ID " + ceremony.getName());
        //Search for ambiguous selectors
        //The selectors for ceremonies have to be prefix-free in order to ensure
        //that every ceremony can actually be selected
        for(Ceremony other: ceremonies.values())
        {
            if(MathsUtil.isPrefix(ceremony.getInstruments(), other.getInstruments()))
                throw new IllegalArgumentException(String.format(
                    "Could not add Ceremony %1$s because its selectors are prefixing the selectors of %2$s. This would make selecting %2$s impossible.\n%3$s prefixes %4$s",
                    ceremony.getName(), other.getName(), Arrays.toString(ceremony.getInstruments()), Arrays.toString(other.getInstruments())));

            if(MathsUtil.isPrefix(other.getInstruments(), ceremony.getInstruments()))
                throw new IllegalArgumentException(String.format(
                    "Could not add Ceremony %1$s because its selectors are prefixed by the selectors of %2$s. This would make selecting %1$s impossible.\n%3$s is prefixed by %4$s",
                    ceremony.getName(), other.getName(), Arrays.toString(ceremony.getInstruments()), Arrays.toString(other.getInstruments())));
        }

        ceremonies.put(ceremony.getName(), ceremony);
        return ceremony;
    }

    @Override
    public Ceremony getCeremony(String name)
    {
        return ceremonies.get(name);
    }

    @Override
    public Map<String, Ceremony> getCeremonies()
    {
        return Collections.unmodifiableMap(ceremonies);
    }

    @Override
    public LexiconCategory addCategory(LexiconCategory cat)
    {
        categories.add(cat);
        return cat;
    }

    @Override
    public List<LexiconCategory> getCategories()
    {
        return Collections.unmodifiableList(categories);
    }
}
