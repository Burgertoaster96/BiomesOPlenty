/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package biomesoplenty.neoforge.init;

import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.neoforge.core.BiomesOPlentyNeoForge;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidInteractionRegistry;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModFluidTypes
{
    public static Holder<FluidType> BLOOD_TYPE;
    public static Holder<FluidType> LIQUID_NULL_TYPE;

    public static void setup()
    {
        registerFluids();
    }

    public static void registerFluids()
    {
        BLOOD_TYPE = registerFluidType(() -> new FluidType(FluidType.Properties.create()
                        .descriptionId("block.biomesoplenty.blood")
                        .fallDistanceModifier(0F)
                        .canExtinguish(true)
                        .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                        .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                        .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
                        .density(3000)
                        .viscosity(6000))
        {
            @Override
            public PathType getBlockPathType(FluidState state, BlockGetter level, BlockPos pos, @Nullable Mob mob, boolean canFluidLog)
            {
                return canFluidLog ? super.getBlockPathType(state, level, pos, mob, true) : null;
            }

            @Override
            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer)
            {
                consumer.accept(new IClientFluidTypeExtensions()
                {
                    private static final ResourceLocation BLOOD_UNDERWATER = ResourceLocation.parse("biomesoplenty:textures/block/blood_underwater.png"),
                            BLOOD_STILL = ResourceLocation.parse("biomesoplenty:block/blood_still"),
                            BLOOD_FLOW = ResourceLocation.parse("biomesoplenty:block/blood_flow");

                    @Override
                    public ResourceLocation getStillTexture()
                    {
                        return BLOOD_STILL;
                    }

                    @Override
                    public ResourceLocation getFlowingTexture() { return BLOOD_FLOW; }

                    @Override
                    public ResourceLocation getRenderOverlayTexture(Minecraft mc) { return BLOOD_UNDERWATER; }

                    @Override
                    public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor)
                    {
                        return new Vector3f(0.407F, 0.121F, 0.137F);
                    }

                    @Override
                    public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape)
                    {
                        RenderSystem.setShaderFogStart(0.125F);
                        RenderSystem.setShaderFogEnd(5.0F);
                    }
                });
            }
        }, "blood");

        LIQUID_NULL_TYPE = registerFluidType(() -> new FluidType(FluidType.Properties.create()
                .descriptionId("block.biomesoplenty.liquid_null")
                .fallDistanceModifier(0F)
                .canExtinguish(false)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
                .density(3000)
                .viscosity(6000))
        {
            @Override
            public PathType getBlockPathType(FluidState state, BlockGetter level, BlockPos pos, @Nullable Mob mob, boolean canFluidLog)
            {
                return canFluidLog ? super.getBlockPathType(state, level, pos, mob, true) : null;
            }

            @Override
            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer)
            {
                consumer.accept(new IClientFluidTypeExtensions()
                {
                    private static final ResourceLocation LIQUID_NULL_UNDERWATER = ResourceLocation.parse("biomesoplenty:textures/block/liquid_null_underwater.png"),
                            LIQUID_NULL_STILL = ResourceLocation.parse("biomesoplenty:block/liquid_null_still"),
                            LIQUID_NULL_FLOW = ResourceLocation.parse("biomesoplenty:block/liquid_null_flow");

                    @Override
                    public ResourceLocation getStillTexture()
                    {
                        return LIQUID_NULL_STILL;
                    }

                    @Override
                    public ResourceLocation getFlowingTexture() { return LIQUID_NULL_FLOW; }

                    @Override
                    public ResourceLocation getRenderOverlayTexture(Minecraft mc) { return LIQUID_NULL_UNDERWATER; }

                    @Override
                    public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor)
                    {
                        return new Vector3f(0.0F, 0.0F, 0.0F);
                    }

                    @Override
                    public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape)
                    {
                        RenderSystem.setShaderFogStart(0.1F);
                        RenderSystem.setShaderFogEnd(2.5F);
                    }
                });
            }
        }, "liquid_null");
    }

    public static DeferredHolder<FluidType, FluidType> registerFluidType(Supplier<FluidType> fluidSupplier, String name)
    {
        return BiomesOPlentyNeoForge.FORGE_FLUID_REGISTER.register(name, fluidSupplier);
    }

    public static void registerFluidInteractions()
    {
        for (Map.Entry<ResourceKey<FluidType>, FluidType> fluidType : NeoForgeRegistries.FLUID_TYPES.entrySet())
        {
            if (fluidType.getValue() != NeoForgeMod.EMPTY_TYPE.value() && fluidType.getValue() != ModFluidTypes.BLOOD_TYPE.value())
            {
                FluidInteractionRegistry.addInteraction(fluidType.getValue(), new FluidInteractionRegistry.InteractionInformation(
                        ModFluidTypes.BLOOD_TYPE.value(),
                        fluidState -> fluidState.isSource() ? BOPBlocks.FLESH.defaultBlockState() : BOPBlocks.POROUS_FLESH.defaultBlockState()
                ));
            }
            if (fluidType.getValue() != NeoForgeMod.EMPTY_TYPE.value() && fluidType.getValue() != ModFluidTypes.LIQUID_NULL_TYPE.value())
            {
                FluidInteractionRegistry.addInteraction(fluidType.getValue(), new FluidInteractionRegistry.InteractionInformation(
                        ModFluidTypes.LIQUID_NULL_TYPE.value(), BOPBlocks.NULL_BLOCK.defaultBlockState()));
            }
        }
    }
}
