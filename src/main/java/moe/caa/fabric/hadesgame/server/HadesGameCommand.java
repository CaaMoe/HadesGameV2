package moe.caa.fabric.hadesgame.server;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

import java.util.List;
import java.util.Locale;

public class HadesGameCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        CommandNode<ServerCommandSource> node = dispatcher.register(
                CommandManager.literal("HadesGame").requires(source -> source.hasPermissionLevel(0))
                        .then(CommandManager.literal("start")
                                .executes(HadesGameCommand::executeStart))
                        .then(CommandManager.literal("forceEnd").requires(source -> source.hasPermissionLevel(4))
                                .executes(HadesGameCommand::executeForceEnd))
                        .then(CommandManager.literal("forceNextEvent").requires(source -> source.hasPermissionLevel(4))
                                .executes(HadesGameCommand::executeForceNextEvent))
                        .then(CommandManager.literal("forceCallEvent").requires(source -> source.hasPermissionLevel(4))
                                .executes(HadesGameCommand::executeForceCallEvent))
                        .then(CommandManager.literal("forceChangeEvent").requires(source -> source.hasPermissionLevel(4))
                                .then(CommandManager.argument("eventId", StringArgumentType.string())
                                        .suggests(suggestedStrings(GameCore.INSTANCE.getEventIds()))
                                        .executes(HadesGameCommand::executeForceChangeEvent)
                                )
                        )
        );

        dispatcher.register(
                CommandManager.literal("game").requires(source -> source.hasPermissionLevel(0)).redirect(node)
        );

        dispatcher.register(
                CommandManager.literal("hg").requires(source -> source.hasPermissionLevel(0)).redirect(node)
        );
    }


    public static SuggestionProvider<ServerCommandSource> suggestedStrings(List<String> list) {
        return (ctx, builder) -> {
            String remaining = builder.getRemaining().toLowerCase(Locale.ROOT);
            if (list.isEmpty()) {
                return Suggestions.empty();
            }
            for (String str : list) {
                if (str.toLowerCase(Locale.ROOT).contains(remaining)) {
                    builder.suggest(str);
                }
            }
            return builder.buildFuture();
        };
    }


    private static int executeForceChangeEvent(CommandContext<ServerCommandSource> context) {
        if (GameCore.INSTANCE.currentState != GameState.GAMING) {
            context.getSource().sendFeedback(new LiteralText("强制切换失败，当前游戏并未进行中"), true);
        } else {
            if (GameCore.INSTANCE.forceChangeEvent(StringArgumentType.getString(context, "eventId"))) {
                context.getSource().sendFeedback(new LiteralText("已强制切换事件"), true);
            } else {
                context.getSource().sendFeedback(new LiteralText("强制切换失败，找不到事件错误"), true);
            }
        }
        return 0;
    }


    private static int executeForceCallEvent(CommandContext<ServerCommandSource> context) {
        if (!GameCore.INSTANCE.nextEvent.SHOULD_COUNTDOWN) {
            context.getSource().sendFeedback(new LiteralText("催促事件失败，当前事件未开启倒计时服务"), true);
        } else {
            GameCore.INSTANCE.currentCountdown = 5;
            context.getSource().sendFeedback(new LiteralText("催促事件成功，事件将在 5 秒后执行"), true);
        }
        return 0;
    }

    private static int executeForceNextEvent(CommandContext<ServerCommandSource> context) {
        if (GameCore.INSTANCE.currentState != GameState.GAMING) {
            context.getSource().sendFeedback(new LiteralText("强制切换失败，当前游戏并未进行中"), true);
        } else {
            GameCore.INSTANCE.nextEvent();
            context.getSource().sendFeedback(new LiteralText("已强制切换事件为下一事件"), true);
        }
        return 0;
    }

    private static int executeStart(CommandContext<ServerCommandSource> context) {
        if (GameCore.INSTANCE.currentState == GameState.WAITING) {
            if (GameCore.INSTANCE.getAllPlayer().size() <= 1) {
                context.getSource().sendFeedback(new LiteralText("现在还不能开启游戏，游戏人数不足 2 人！"), true);
                return 0;
            }
            GameCore.INSTANCE.currentState = GameState.STARTING;
            context.getSource().sendFeedback(new LiteralText("已设置开启游戏"), true);
        } else {
            context.getSource().sendFeedback(new LiteralText("现在还不能开启游戏，因为游戏已经开始或正在开始或正在结束。如果需要强制结束游戏，请使用指令关闭游戏"), true);
        }
        return 0;
    }

    private static int executeForceEnd(CommandContext<ServerCommandSource> context) {
        if (GameCore.INSTANCE.currentState == GameState.WAITING) {
            context.getSource().sendFeedback(new LiteralText("强制结束失败，当前游戏并未开始"), true);
        } else {
            GameCore.INSTANCE.forceEndGame();
            context.getSource().sendFeedback(new LiteralText("已强制结束游戏"), true);
        }
        return 0;
    }
}
