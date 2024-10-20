package org.v2.core;

import org.apache.poi.xwpf.usermodel.IRunBody;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.v2.Context;
import org.v2.util.Constant;
import org.v2.util.StrUtil;

import java.util.List;
import java.util.Stack;

/**
 * @author hjy
 * @date 2024/10/20 15:16
 */
public class ParagraphVisitor extends AbstractDocumentVisitor<XWPFParagraph>{

    private final RunVisitor runVisitor;

    public ParagraphVisitor() {
        this.runVisitor = new RunVisitor();
    }

    @Override
    public void visit(XWPFParagraph target, Context context) {
        List<XWPFRun> runs = target.getRuns();
        Stack<RunWrapper> stack = new Stack<>();

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
                    if (StrUtil.isPrefix(current) && (stack.isEmpty() || stack.peek() instanceof EndRunWrapper)) {
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
                            stack.push(new StartRunWrapper(run, text, j, i));
                            for (int k = j + 2; k < len; k++) {
                                if (text.charAt(k) == '}') {
                                    stack.push(new EndRunWrapper(run, text, k, i));
                                    hasEnd = true;
                                    j = k+1;
                                    break;
                                }
                            }
                        }
                    } else if (current == '}' && (!stack.isEmpty() && !(stack.peek() instanceof EndRunWrapper))) {
                        stack.push(new EndRunWrapper(run, text, j, i));
                        hasEnd = true;
                        isMid = false;
                    }
                }
                if (isMid && (!stack.isEmpty() && !(stack.peek() instanceof EndRunWrapper))) {
                    stack.push(new MidRunWrapper(run, text, 0, i));
                } else if (stack.size() >= 2 && stack.get(1) instanceof EndRunWrapper){//TODO
                    //处理一组run
//                    for (int s = 0; s < stack.size(); s++) {
//                        RunWrapper runWrapper = stack.get(s);
//                    }
                }

            }
        }
    }

    public static void main(String[] args) {
        System.out.println("a".substring(0, 0));
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

        public XWPFRun getRun() {
            return run;
        }

        public String getText() {
            return text;
        }

        public int getIndex() {
            return index;
        }

        public int getRunIndex() {
            return runIndex;
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
