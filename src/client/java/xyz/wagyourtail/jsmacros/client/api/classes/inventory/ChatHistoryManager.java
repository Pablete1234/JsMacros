package xyz.wagyourtail.jsmacros.client.api.classes.inventory;

import net.minecraft.client.gui.components.ChatComponent;
import xyz.wagyourtail.jsmacros.client.access.IChatHud;
import xyz.wagyourtail.jsmacros.client.api.helper.TextHelper;
import xyz.wagyourtail.jsmacros.client.api.helper.screen.ChatHudLineHelper;
import xyz.wagyourtail.jsmacros.client.util.MainThread;
import xyz.wagyourtail.jsmacros.core.MethodWrapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @since 1.6.0
 */
public class ChatHistoryManager {
    private final ChatComponent hud;

    public ChatHistoryManager(ChatComponent hud) {
        this.hud = hud;
    }

    /**
     * @param index
     * @return
     * @since 1.6.0
     */
    public ChatHudLineHelper getRecvLine(int index) {
        return MainThread.await(() -> new ChatHudLineHelper(hud.allMessages.get(index), hud));
    }

    /**
     * @return the amount of messages in the chat history.
     * @since 1.8.4
     */
    public int getRecvCount() {
        return MainThread.await(hud.allMessages::size);
    }

    /**
     * @return all received messages in the chat history.
     * @since 1.8.4
     */
    public List<ChatHudLineHelper> getRecvLines() {
        return MainThread.await(() -> hud.allMessages.stream().map(l -> new ChatHudLineHelper(l, hud)).collect(Collectors.toList()));
    }

    /**
     * @param index
     * @param line
     * @since 1.6.0
     */
    public void insertRecvText(int index, TextHelper line) {
        insertRecvText(index, line, 0, false);
    }

    /**
     * you should probably run {@link #refreshVisible()} after...
     *
     * @param index
     * @param line
     * @param timeTicks
     * @since 1.6.0
     */
    public void insertRecvText(int index, TextHelper line, int timeTicks) {
        insertRecvText(index, line, timeTicks, false);
    }

    /**
     * @param index
     * @param line
     * @param timeTicks
     * @param await
     * @since 1.6.0
     */
    public void insertRecvText(int index, TextHelper line, int timeTicks, boolean await) {
        MainThread.run(() -> ((IChatHud) hud).jsmacros_addMessageAtIndexBypass(line.getRaw(), index, timeTicks), await);
    }

    /**
     * @param index
     * @since 1.6.0
     */
    public void removeRecvText(int index) {
        removeRecvText(index, false);
    }

    /**
     * @param index
     * @param await
     * @since 1.6.0
     */
    public void removeRecvText(int index, boolean await) {
        MainThread.run(() -> hud.allMessages.remove(index), await);
    }

    /**
     * @param text
     * @since 1.6.0
     */
    public void removeRecvTextMatching(TextHelper text) {
        removeRecvTextMatching(text, false);
    }

    /**
     * @param text
     * @param await
     * @since 1.6.0
     */
    public void removeRecvTextMatching(TextHelper text, boolean await) {
        MainThread.run(() -> hud.allMessages.removeIf(c -> c.content().equals(text.getRaw())), await);
    }

    /**
     * @param filter
     * @since 1.6.0
     */
    public void removeRecvTextMatchingFilter(MethodWrapper<ChatHudLineHelper, Object, Boolean, ?> filter) {
        removeRecvTextMatchingFilter(filter, false);
    }

    /**
     * @param filter
     * @param await
     * @since 1.6.0
     */
    public void removeRecvTextMatchingFilter(MethodWrapper<ChatHudLineHelper, Object, Boolean, ?> filter, boolean await) {
        MainThread.run(() -> hud.allMessages.removeIf((c) -> filter.test(new ChatHudLineHelper(c, hud))), await);
    }

    /**
     * this will reset the view of visible messages
     *
     * @since 1.6.0
     */
    public void refreshVisible() {
        refreshVisible(false);
    }

    /**
     * @param await
     * @since 1.6.0
     */
    public void refreshVisible(boolean await) {
        MainThread.run(hud::rescaleChat, await);
    }

    /**
     * @since 1.6.0
     */
    public void clearRecv() {
        clearRecv(false);
    }

    /**
     * @param await
     * @since 1.6.0
     */
    public void clearRecv(boolean await) {
        MainThread.run(() -> hud.clearMessages(false), await);
    }

    /**
     * @return direct reference to sent message history list. modifications will affect the list.
     * @since 1.6.0
     */
    public List<String> getSent() {
        return hud.getRecentChat();
    }

    /**
     * @since 1.6.0
     */
    public void clearSent() {
        clearSent(false);
    }

    /**
     * @param await
     * @since 1.6.0
     */
    public void clearSent(boolean await) {
        MainThread.run(() -> hud.getRecentChat().clear(), await);
    }

}
