/*
 * Copyright (c) 2025 GeyserMC. http://geysermc.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * @author GeyserMC
 * @link https://github.com/GeyserMC/Geyser
 */

package org.geysermc.geyser.session.dialog;

import net.kyori.adventure.key.Key;
import org.cloudburstmc.nbt.NbtMap;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.session.cache.registry.JavaRegistries;
import org.geysermc.geyser.session.cache.tags.GeyserHolderSet;
import org.geysermc.geyser.session.dialog.action.DialogAction;
import org.geysermc.geyser.util.MinecraftKey;

import java.util.List;
import java.util.Optional;

public class DialogListDialog extends DialogWithButtons {

    public static final Key TYPE = MinecraftKey.key("dialog_list");

    private final GeyserHolderSet<Dialog> dialogs;

    public DialogListDialog(GeyserSession session, NbtMap map, IdGetter idGetter) {
        super(session, map, readDefaultExitAction(session, map, idGetter));
        dialogs = GeyserHolderSet.readHolderSet(JavaRegistries.DIALOG, map.get("dialogs"), idGetter, dialog -> Dialog.readDialogFromNbt(session, dialog, idGetter));
    }

    @Override
    protected List<DialogButton> buttons(DialogHolder holder) {
        return dialogs.resolve(holder.session()).stream()
                .map(dialog -> new DialogButton(dialog.externalTitle().orElseGet(dialog::title),
                        Optional.of(new DialogAction.ShowDialog(dialog))))
                .toList();
    }
}
