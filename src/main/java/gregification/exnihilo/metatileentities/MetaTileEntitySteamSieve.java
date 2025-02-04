/*
    Copyright 2019, TheLimePixel, dan
    GregBlock Utilities

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package gregification.exnihilo.metatileentities;

import gregification.common.GFRecipeMaps;
import gregification.common.GFTextures;
import gregtech.api.capability.impl.NotifiableItemStackHandler;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.ImageWidget;
import gregtech.api.gui.widgets.ProgressWidget;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.SteamMetaTileEntity;
import gregtech.client.renderer.texture.Textures;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;


public class MetaTileEntitySteamSieve extends SteamMetaTileEntity {

    public MetaTileEntitySteamSieve(ResourceLocation metaTileEntityId, boolean isHighPressure) {
        super(metaTileEntityId, GFRecipeMaps.SIEVE_RECIPES, Textures.SIFTER_OVERLAY, isHighPressure);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder metaTileEntityHolder) {
        return new MetaTileEntitySteamSieve(metaTileEntityId, isHighPressure);
    }

    @Override
    protected IItemHandlerModifiable createImportItemHandler() {
        return new NotifiableItemStackHandler(2, this, false);
    }

    @Override
    protected IItemHandlerModifiable createExportItemHandler() {
        return new NotifiableItemStackHandler(30, this, true);
    }

    @Override
    protected ModularUI createUI(@Nonnull EntityPlayer player) {
        ModularUI.Builder builder = new ModularUI.Builder(GuiTextures.BACKGROUND_STEAM.get(this.isHighPressure), 176, 192)
                .label(6, 6, this.getMetaFullName()).shouldColor(false)
                .slot(this.importItems, 0, 17, 43, GuiTextures.SLOT_STEAM.get(isHighPressure))
                .slot(this.importItems, 1, 35, 43, GuiTextures.SLOT_STEAM.get(isHighPressure))
                .progressBar(workableHandler::getProgressPercent, 25, 68, 20, 20,
                        GFTextures.PROGRESS_BAR_SIFTER_STEAM.get(isHighPressure), ProgressWidget.MoveType.VERTICAL_INVERTED, workableHandler.getRecipeMap())
                .widget((new ImageWidget(79, 77, 18, 18, GuiTextures.INDICATOR_NO_STEAM.get(this.isHighPressure)))
                        .setPredicate(() -> this.workableHandler.isHasNotEnoughEnergy()))
                .bindPlayerInventory(player.inventory, GuiTextures.SLOT_STEAM.get(this.isHighPressure), 7, 109);

        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 6; x++) {
                builder.slot(this.exportItems, y * 6 + x, 61 + x * 18, 15 + y * 18, true, false, GuiTextures.SLOT_STEAM.get(isHighPressure));
            }
        }

        return builder.build(getHolder(), player);
    }
}
