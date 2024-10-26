package org.v2.core;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.v2.Context;
import org.v2.Pool;
import org.v2.util.StrUtil;

import java.util.*;

/**
 * @author hjy
 * @date 2024/10/20 15:16
 */
public class ParagraphVisitor extends AbstractDocumentVisitor<XWPFParagraph>{

    private RunVisitor runVisitor;

    public ParagraphVisitor() {
        init();
    }

    protected void init() {
        this.runVisitor = new RunVisitor();
    }

    @Override
    public void visit(XWPFParagraph target, Context context) {
        List<XWPFRun> runs = target.getRuns();
        HashSet<XWPFRun> rub = new HashSet<>();
        List<RunWrapper> linked = new LinkedList<>();
        for (int i = 0; i < runs.size(); i++) {
            XWPFRun run = runs.get(i);
            String text = run.getText(0);
            if (StrUtil.isKey(text)) {
                runVisitor.visit(run, context);
            } else {
                if (text == null) {
                    continue;
                }
                int len = text.length();
                boolean isMid = true;
                boolean hasEnd = false;
                for (int j = 0; j < len; j++) {
                    char current = text.charAt(j);
                    if (StrUtil.isPrefix(current) && (linked.isEmpty() || linked.getLast() instanceof EndRunWrapper)) {
                        if (j == len - 1) {
                            //已经是最后一个字符了
                            if (i < runs.size() - 1) {
                                //不是最后一个run
                                XWPFRun nextRun = runs.get(i + 1);
                                String nextText = nextRun.getText(0);
                                if (!nextText.isEmpty() && nextText.charAt(0) == '{') {
                                    run.setText(text.substring(0, text.length() - 1), 0);
                                    nextRun.setText(current + nextText, 0);
                                }
                            }
                        } else if (text.charAt(j + 1) == '{'){
                            //一个完整的key起始点
                            isMid = false;
                            linked.add(new StartRunWrapper(run, text, j, i));
                            for (int k = j + 2; k < len; k++) {
                                if (text.charAt(k) == '}') {
                                    linked.add(new EndRunWrapper(run, text, k, i));
                                    hasEnd = true;
                                    j = k+1;
                                    break;
                                }
                            }
                        }
                    } else if (current == '}' && (!linked.isEmpty() && !(linked.getLast() instanceof EndRunWrapper))) {
                        linked.add(new EndRunWrapper(run, text, j, i));
                        hasEnd = true;
                        isMid = false;
                    }
                }
                if (isMid && (!linked.isEmpty() && !(linked.getLast() instanceof EndRunWrapper))) {
                    linked.add(new MidRunWrapper(run, text, 0, i));
                } else if (hasEnd){
                    //处理一组run
                    List<RunWrapper> tmpLinked = new ArrayList<>();

                    while (!(linked.getLast() instanceof EndRunWrapper)) {
                        tmpLinked.add(linked.removeLast());
                    }

                    int index = 0;
                    String key = "";
                    for (int j = 0; j < linked.size(); j++) {
                        RunWrapper wrapper = linked.get(j);
                        rub.add(wrapper.run);
                        String innerText = wrapper.text;
                        int runIndex = wrapper.index;

                        if (wrapper instanceof StartRunWrapper) {

                            if (runIndex != 0) {
                                String newRunText = innerText.substring(0, runIndex);
                                XWPFRun tmpRun = target.insertNewRun(i);
                                tmpRun.setText(newRunText, 0);
                                i++;
                            }
                            index = runIndex;
                            if (linked.get(j + 1).run != wrapper.run) {
                                key += innerText.substring(index);
                            }
                        } else if (wrapper instanceof EndRunWrapper) {
                            key += innerText.substring(0, runIndex+1);
                            XWPFRun tmpRun = target.insertNewRun(i);
                            tmpRun.setText(key, 0);
                            runVisitor.visit(tmpRun, context);
                            i++;
                            index = runIndex;
                        }

                        RunWrapper next;
                        int k = j + 1;
                        if (k >= linked.size()) {
                            break;
                        }
                        while ((next = linked.get(k)).run == wrapper.run) {
                            if (next instanceof EndRunWrapper) {
                                innerText = next.text;
                                runIndex = next.index;

                                key += innerText.substring(index, runIndex+1);
                                XWPFRun tmpRun = target.insertNewRun(i);
                                tmpRun.setText(key, 0);
                                runVisitor.visit(tmpRun, context);
                                i++;
                                index = runIndex;
                            } else {
                                innerText = wrapper.text;
                                runIndex = wrapper.runIndex;
                                if (runIndex != 0) {
                                    String newRunText = innerText.substring(index, runIndex + 1);
                                    XWPFRun tmpRun = target.insertNewRun(i);
                                    tmpRun.setText(newRunText, 0);
                                    index = runIndex;
                                    i++;
                                }
                            }
                            k++;
                            if (k >= linked.size())
                                break;
                        }
                        if (j+1 != k) {
                            j = k-1;
                            continue;
                        }
                        while ((next = linked.get(k)) instanceof MidRunWrapper) {
                            key += next.text;
                            rub.add(next.run);
                            k++;
                        }
                        j=k-1;
                    }

                    RunWrapper last = linked.getLast();
                    String lastText = last.text;
                    if (last.index != lastText.length() - 1) {
                        String newRunText = lastText.substring(last.index + 1);
                        XWPFRun tmpRun = target.insertNewRun(i);
                        tmpRun.setText(newRunText, 0);
                        i++;
                    }

                    linked = tmpLinked;

                }

            }
        }

        if (rub.isEmpty()) {
            return;
        }
        for (int i = runs.size() - 1; i >= 0 ; i--) {
            if (rub.contains(runs.get(i))) {
                target.removeRun(i);
            }
        }

    }

    private static class RunWrapper {
        private final XWPFRun run;
        private final String text;
        private final int index;
        private final int runIndex;


        private RunWrapper(XWPFRun run, String text, int index, int runIndex) {
            this.run = run;
            this.text = text;
            this.index = index;
            this.runIndex = runIndex;
        }

    }

    private static class StartRunWrapper extends RunWrapper{
        private StartRunWrapper(XWPFRun run, String text, int index, int runIndex) {
            super(run, text, index, runIndex);
        }
    }

    private static class MidRunWrapper extends RunWrapper{
        private MidRunWrapper(XWPFRun run, String text, int index, int runIndex) {
            super(run, text, index, runIndex);
        }
    }

    private static class EndRunWrapper extends RunWrapper{
        private EndRunWrapper(XWPFRun run, String text, int index, int runIndex) {
            super(run, text, index, runIndex);
        }
    }


}
