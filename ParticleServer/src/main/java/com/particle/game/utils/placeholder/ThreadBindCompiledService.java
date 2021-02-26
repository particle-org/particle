package com.particle.game.utils.placeholder;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.particle.api.utils.ThreadBindCompiledServiceApi;
import com.particle.game.entity.attribute.identified.EntityNameService;
import com.particle.model.player.Player;
import com.particle.util.placeholder.CompileDB;
import com.particle.util.placeholder.CompileInterface;
import com.particle.util.placeholder.addon.CompileThreadBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class ThreadBindCompiledService implements ThreadBindCompiledServiceApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadBindCompiledService.class);

    private static Pattern pattern = Pattern.compile("\\$\\{[\\u4e00-\\u9fa5\\w:\\-~]+}");

    /**
     * 获取数据时最多等待300ms
     */
    private static final long DATA_REQUEST_TIME = 300;

    @Inject
    private EntityNameService entityNameService;

    @Override
    public String compile(Player player, String str) {
        // 查询玩家名
        String entityName = this.entityNameService.getEntityName(player);

        int lastAppear = 0;

        StringBuilder stringBuilder = new StringBuilder();

        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();

            // 黏贴find之前的内容
            if (start != lastAppear) {
                stringBuilder.append(str, lastAppear, start);
            }

            // 替换 find到的内容
            stringBuilder.append(compile(player, entityName, str.substring(start + 2, end - 1), "-"));

            lastAppear = end;
        }

        if (lastAppear != str.length()) stringBuilder.append(str.substring(lastAppear));

        return stringBuilder.toString();
    }

    /**
     * 编译操作
     * <p>
     * 这里认为Player所在的线程是同步线程，其它线程均视为异步线程
     *
     * @param player
     * @param placeHolder
     * @param defaultStr
     * @return
     */
    private String compile(Player player, String entityName, String placeHolder, String defaultStr) {
        // 合法性检查
        int index = placeHolder.indexOf(":");
        if (index < 0 || index >= placeHolder.length()) {
            return defaultStr;
        }

        // 数据抽取
        String namespace = placeHolder.substring(0, index);
        String key = placeHolder.substring(index + 1);

        // 查询Compile方法
        CompileInterface compileInterface = CompileDB.get(namespace);

        if (compileInterface == null) {
            LOGGER.error("Fail to compile component, because of {} not founded", namespace);

            return "";
        }

        // 检查Annotation
        CompileThreadBinder annotation = compileInterface.getClass().getAnnotation(CompileThreadBinder.class);
        // 未绑定执行线程
        if (annotation == null) {
            return execureCompile(compileInterface, key, entityName, defaultStr);
        }

        // 检查绑定线程
        switch (annotation.type()) {
            // 需要同步调用的方法
            case SYNC:
                // 检查调用线程
                if (player.getLevel().getLevelSchedule().getId() == Thread.currentThread().getId()) {
                    // 当前调用就在主线程，则直接执行
                    return execureCompile(compileInterface, key, entityName, defaultStr);
                } else {
                    // 当前调用线程为异步线程，则阻塞当前线程

                    // 缓存中间数据
                    final CompiledHolder holder = new CompiledHolder();
                    final long timestamp = System.currentTimeMillis();

                    // 构造数据获取任务
                    Future placeHolderCompileTask = player.getLevel().getLevelSchedule().scheduleSimpleTask("PlaceHolderCompile", () -> {
                        holder.setStr(execureCompile(compileInterface, key, entityName, defaultStr));
                    });

                    // 轮询数据查询结果
                    while (true) {
                        if (System.currentTimeMillis() - timestamp < DATA_REQUEST_TIME) {
                            if (placeHolderCompileTask.isDone()) {
                                return holder.getStr();
                            }

                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                                LOGGER.error("Place holder compile exception!", e);
                            }
                        } else {
                            break;
                        }
                    }
                }
                break;
            // 需要异步调用的方法
            case ASYNC:
                // 主线程不编译异步PlaceHolder
                if (player.getLevel().getLevelSchedule().getId() == Thread.currentThread().getId()) {
                    return defaultStr;
                }

                return execureCompile(compileInterface, key, entityName, defaultStr);
            // 不需要检查
            case ALL:
                return execureCompile(compileInterface, key, entityName, defaultStr);
        }

        return defaultStr;
    }

    private class CompiledHolder {
        private String str = null;

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }
    }

    private String execureCompile(CompileInterface compileInterface, String key, String entityName, String defaultStr) {
        String compile = compileInterface.compile(key, entityName);
        if (compile == null) {
            return defaultStr;
        } else {
            return compile;
        }
    }
}
