/*******************************************************************************
 * Copyright 2022, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package biomesoplenty.common.biome;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.common.util.biome.BiomeUtil;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;

public class BOPSecondaryOverworldBiomeBuilder extends BOPOverworldBiomeBuilder
{
    private final ResourceKey<Biome>[][] MIDDLE_BIOMES_BOP = new ResourceKey[][]{
            {BOPBiomes.TUNDRA,               BOPBiomes.TUNDRA,               BOPBiomes.MUSKEG,           BOPBiomes.SNOWY_MAPLE_WOODS,    BOPBiomes.DEAD_FOREST},
            {BOPBiomes.SEASONAL_FOREST,      BOPBiomes.SEASONAL_FOREST,      BOPBiomes.SEASONAL_FOREST,  BOPBiomes.MAPLE_WOODS,          BOPBiomes.MAPLE_WOODS},
            {BOPBiomes.LAVENDER_FIELD,       BOPBiomes.LAVENDER_FIELD,       BOPBiomes.REDWOOD_FOREST,   BOPBiomes.CLOVER_PATCH,         BOPBiomes.CLOVER_PATCH},
            {BOPBiomes.MEDITERRANEAN_FOREST, BOPBiomes.MEDITERRANEAN_FOREST, BOPBiomes.WOODLAND,         BOPBiomes.RAINFOREST,           BOPBiomes.VOLCANIC_PLAINS},
            {BOPBiomes.WASTELAND,            BOPBiomes.WASTELAND,            BOPBiomes.WASTELAND,        BOPBiomes.WASTELAND,            BOPBiomes.WASTELAND}
    };

    private final ResourceKey<Biome>[][] MIDDLE_BIOMES_VARIANT_BOP = new ResourceKey[][]{
            {null,              null,                       null,                          BOPBiomes.SNOWBLOSSOM_GROVE, BOPBiomes.OLD_GROWTH_DEAD_FOREST},
            {null,              BOPBiomes.ASPEN_FOREST,     BOPBiomes.PUMPKIN_PATCH,       null,                        null},
            {null,              BOPBiomes.JACARANDA_FOREST, BOPBiomes.ORCHARD,             null,                        null},
            {null,              null,                       BOPBiomes.OLD_GROWTH_WOODLAND, BOPBiomes.VOLCANIC_PLAINS,   null},
            {null,              null,                       null,                          null,                        null}
    };

    private final ResourceKey<Biome>[][] PLATEAU_BIOMES_BOP = new ResourceKey[][]{
            {BOPBiomes.TUNDRA,               BOPBiomes.TUNDRA,               BOPBiomes.SNOWBLOSSOM_GROVE, BOPBiomes.SNOWY_MAPLE_WOODS, BOPBiomes.DEAD_FOREST},
            {BOPBiomes.SEASONAL_FOREST,      BOPBiomes.ASPEN_FOREST,         BOPBiomes.HIGHLAND,          BOPBiomes.HIGHLAND,          BOPBiomes.HIGHLAND},
            {BOPBiomes.LAVENDER_FIELD,       BOPBiomes.LAVENDER_FIELD,       BOPBiomes.HIGHLAND,          BOPBiomes.HIGHLAND,          BOPBiomes.HIGHLAND},
            {BOPBiomes.MEDITERRANEAN_FOREST, BOPBiomes.MEDITERRANEAN_FOREST, BOPBiomes.WOODLAND,          BOPBiomes.ROCKY_RAINFOREST,  BOPBiomes.VOLCANO},
            {BOPBiomes.WASTELAND_STEPPE,     BOPBiomes.WASTELAND_STEPPE,     BOPBiomes.WASTELAND_STEPPE,  BOPBiomes.WASTELAND_STEPPE,  BOPBiomes.WASTELAND_STEPPE}
    };

    private final ResourceKey<Biome>[][] PLATEAU_BIOMES_VARIANT_BOP = new ResourceKey[][]{
            {null, null,                       null,                          BOPBiomes.SNOWBLOSSOM_GROVE, BOPBiomes.OLD_GROWTH_DEAD_FOREST},
            {null, null,                       null,                          BOPBiomes.MAPLE_WOODS,       null},
            {null, BOPBiomes.JACARANDA_FOREST, null,                          null,                        BOPBiomes.MOOR},
            {null, null,                       BOPBiomes.OLD_GROWTH_WOODLAND, BOPBiomes.VOLCANO,           null},
            {null, null,                       null,                          null,                        null}
    };

    private final ResourceKey<Biome>[][] RIVER_BIOMES_BOP = new ResourceKey[][]{
            {Biomes.FROZEN_RIVER,           Biomes.FROZEN_RIVER,           Biomes.FROZEN_RIVER,           Biomes.FROZEN_RIVER,           Biomes.RIVER},
            {null,                          null,                          null,                          null,                          null},
            {null,                          null,                          null,                          null,                          null},
            {null,                          null,                          null,                          null,                          null},
            {BOPBiomes.WASTELAND,           BOPBiomes.WASTELAND,           BOPBiomes.WASTELAND,           BOPBiomes.WASTELAND,           BOPBiomes.WASTELAND}
    };

    private final ResourceKey<Biome>[][] SWAMP_BIOMES_BOP = new ResourceKey[][]{
            {BOPBiomes.HOT_SPRINGS, BOPBiomes.HOT_SPRINGS, BOPBiomes.HOT_SPRINGS, BOPBiomes.HOT_SPRINGS, BOPBiomes.HOT_SPRINGS},
            {BOPBiomes.BOG,         BOPBiomes.BOG,         BOPBiomes.BOG,         BOPBiomes.BOG,         BOPBiomes.BOG},
            {BOPBiomes.MARSH,       BOPBiomes.MARSH,       BOPBiomes.MARSH,       BOPBiomes.MARSH,       BOPBiomes.MARSH},
            {BOPBiomes.BAYOU,       BOPBiomes.BAYOU,       BOPBiomes.BAYOU,       BOPBiomes.FLOODPLAIN,  BOPBiomes.FLOODPLAIN},
            {BOPBiomes.WASTELAND,   BOPBiomes.WASTELAND,   BOPBiomes.BAYOU,       BOPBiomes.FLOODPLAIN,  BOPBiomes.FLOODPLAIN}
    };

    @Override
    protected ResourceKey<Biome> pickMiddleBiomeBOP(Registry<Biome> biomeRegistry, int temperatureIndex, int humidityIndex, Climate.Parameter weirdness)
    {
        ResourceKey<Biome> middleBiome = BiomeUtil.biomeOrFallback(biomeRegistry, this.MIDDLE_BIOMES_BOP[temperatureIndex][humidityIndex], this.MIDDLE_BIOMES[temperatureIndex][humidityIndex]);

        if (weirdness.max() < 0) return middleBiome;
        else
        {
            return BiomeUtil.biomeOrFallback(biomeRegistry, this.MIDDLE_BIOMES_VARIANT_BOP[temperatureIndex][humidityIndex], middleBiome);
        }
    }

    @Override
    protected ResourceKey<Biome> pickPlateauBiomeBOP(Registry<Biome> biomeRegistry, int temperatureIndex, int humidityIndex, Climate.Parameter weirdness)
    {
        if (weirdness.max() < 0L) return BiomeUtil.biomeOrFallback(biomeRegistry, this.PLATEAU_BIOMES_BOP[temperatureIndex][humidityIndex], this.PLATEAU_BIOMES[temperatureIndex][humidityIndex]);
        else return BiomeUtil.biomeOrFallback(biomeRegistry, this.PLATEAU_BIOMES_VARIANT_BOP[temperatureIndex][humidityIndex], this.PLATEAU_BIOMES_BOP[temperatureIndex][humidityIndex], this.PLATEAU_BIOMES[temperatureIndex][humidityIndex]);
    }

    @Override
    protected ResourceKey<Biome> pickRiverBiomeBOP(Registry<Biome> biomeRegistry, int temperatureIndex, int humidityIndex)
    {
        return BiomeUtil.biomeOrFallback(biomeRegistry, this.RIVER_BIOMES_BOP[temperatureIndex][humidityIndex], Biomes.RIVER);
    }

    @Override
    protected ResourceKey<Biome> pickSwampBiomeBOP(Registry<Biome> biomeRegistry, int temperatureIndex, int humidityIndex, Climate.Parameter weirdness)
    {
        return BiomeUtil.biomeOrFallback(biomeRegistry, this.SWAMP_BIOMES_BOP[temperatureIndex][humidityIndex], this.pickMiddleBiomeBOP(biomeRegistry, temperatureIndex, humidityIndex, weirdness), Biomes.SWAMP);
    }
}
