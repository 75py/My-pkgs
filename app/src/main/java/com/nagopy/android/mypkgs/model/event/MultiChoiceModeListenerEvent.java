/*
 * Copyright (C) 2015 75py
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nagopy.android.mypkgs.model.event;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.nagopy.android.mypkgs.model.AppData;

import java.util.Collections;
import java.util.List;

/**
 * MultiChoiceModeListener をEventBusで送信するためのイベントクラス群.
 */
public class MultiChoiceModeListenerEvent {

    private MultiChoiceModeListenerEvent() {
    }

    /**
     * {@link android.widget.AbsListView.MultiChoiceModeListener#onActionItemClicked(ActionMode, MenuItem)}のEventBus用クラス.
     */
    public static class ActionItemClickedEvent {
        public final ActionMode actionMode;
        public final MenuItem menuItem;
        public List<AppData> checkedItemList;

        public ActionItemClickedEvent(ActionMode actionMode, MenuItem menuItem) {
            this(actionMode, menuItem, Collections.<AppData>emptyList());
        }

        public ActionItemClickedEvent(ActionMode actionMode, MenuItem menuItem, List<AppData> checkedItemList) {
            this.actionMode = actionMode;
            this.menuItem = menuItem;
            this.checkedItemList = checkedItemList;
        }

    }

    /**
     * {@link android.widget.AbsListView.MultiChoiceModeListener#onCreateActionMode(ActionMode, Menu)}のEventBus用クラス.
     */
    public static class CreateActionModeEvent {
        public final ActionMode actionMode;
        public final Menu menu;

        public CreateActionModeEvent(ActionMode mode, Menu menu) {
            this.actionMode = mode;
            this.menu = menu;
        }
    }

    /**
     * {@link android.widget.AbsListView.MultiChoiceModeListener#onDestroyActionMode(ActionMode)}のEventBus用クラス.
     */
    public static class DestroyActionModeEvent {
        public final ActionMode actionMode;

        public DestroyActionModeEvent(ActionMode actionMode) {
            this.actionMode = actionMode;
        }
    }

    /**
     * {@link android.widget.AbsListView.MultiChoiceModeListener#onItemCheckedStateChanged(ActionMode, int, long, boolean)}のEventBus用クラス.
     */
    public static class ItemCheckedStateChangedEvent {
        public ActionMode actionMode;
        public int position;
        public long id;
        public boolean checked;
        public int checkedItemCount;

        private ItemCheckedStateChangedEvent() {
        }

        public static class Builder {
            ItemCheckedStateChangedEvent event;

            public Builder() {
                event = new ItemCheckedStateChangedEvent();
            }

            public Builder setActionMode(ActionMode actionMode) {
                event.actionMode = actionMode;
                return this;
            }

            public Builder setPosition(int position) {
                event.position = position;
                return this;
            }

            public Builder setId(long id) {
                event.id = id;
                return this;
            }

            public Builder setChecked(boolean checked) {
                event.checked = checked;
                return this;
            }

            public Builder setCheckedItemCount(int checkedItemCount) {
                event.checkedItemCount = checkedItemCount;
                return this;
            }

            public ItemCheckedStateChangedEvent build() {
                return event;
            }
        }
    }
}
