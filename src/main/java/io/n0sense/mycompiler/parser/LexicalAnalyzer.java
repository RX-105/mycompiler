package io.n0sense.mycompiler.parser;

import io.n0sense.mycompiler.objects.LexicalFragment;
import io.n0sense.mycompiler.parser.automaton.DFA;
import io.n0sense.mycompiler.parser.automaton.RegexDFA;
import io.n0sense.mycompiler.util.LexicalUtil;
import org.apache.commons.io.FileUtils;

import static io.n0sense.mycompiler.constant.ErrorConstants.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class LexicalAnalyzer {
    String inFileName;
    String outFileName;
    int bufferMax = 5000;
    List<LexicalFragment> fragmentBuffer = new ArrayList<>(bufferMax);
    DFA c;

    public LexicalAnalyzer(String inFileName, String outFileName) {
        this.inFileName = inFileName;
        this.outFileName = outFileName;

        String startState = "a";
        HashSet<String> acceptStates = new HashSet<>(Arrays.asList("b", "c", "d", "e", "f", "g"));
        HashSet<String> setOfStates = new HashSet<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g"));
        ArrayList<String[]> transition = new ArrayList<>();
        // c语言标识符。可以以字母和下划线开头
        transition.add(new String[] { "a", "[A-Za-z_]", "b" });
        transition.add(new String[] { "b", "[A-Za-z0-9_]", "b" });
        // 数字
        transition.add(new String[] {"a", "[0-9]", "c"});
        transition.add(new String[] {"b", "[0-9]", "c"});
        // 运算符号
        transition.add(new String[] {"a", "[-+*/]", "d"});
        transition.add(new String[] {"a", "[!<>=]", "e"});
        transition.add(new String[] {"e", "[=]", "f"});
        // 分隔符
        transition.add(new String[] {"a", "[,;{}()]", "g"});
        c = new RegexDFA(setOfStates, startState, acceptStates, transition);
    }

    public void process() throws IOException {
        File origin = new File(inFileName);
        File sourceFile = File.createTempFile("source", ".c");
        FileUtils.copyFile(origin, sourceFile);
        // 对文件中的括号进行匹配
        if (!LexicalUtil.isBracketMatched(sourceFile)) {
            raiseError(bracketMismatched.formatted(inFileName), -2);
        }
        // 移除文件中的注释
        LexicalUtil.removeComments(sourceFile);
        try (BufferedReader rd = new BufferedReader(new FileReader(sourceFile))) {
            String line = rd.readLine();
            while (line != null) {
                // 不处理没有内容的行
                if (line.trim().length() != 0) {
                    List<LexicalFragment> fragments =
                            analyzeSyntaxFragments(LexicalUtil.split(line).split(" "));
                    fragmentBuffer.addAll(fragments);
                    // 当缓冲区元素大于bufferMax时，一次性写入文件，并清空缓冲区
                    if (fragmentBuffer.size() > bufferMax)
                        writeToFile();
                }
                line = rd.readLine();
            }
            // 读取完毕后再次写入文件
            writeToFile();
        }
    }

    private List<LexicalFragment> analyzeSyntaxFragments(String[] parts) {
        List<LexicalFragment> fragments = new ArrayList<>();
        for (String fragment : parts) {
            if (fragment.length() == 0)
                continue;
            // 判断顺序：保留关键字->标点符号->数字和标识符
            if (LexicalUtil.isKeyword(fragment)) {
                fragments.add(new LexicalFragment(1, fragment));
                continue;
            }
            // 判断首字符是否为数字，如果是，则判断整个串是否为数字，否则判断是否为标识符
            if (Character.isDigit(fragment.codePointAt(0))) {
                if (LexicalUtil.isNumber(fragment))
                    fragments.add(new LexicalFragment(3, fragment));
                else
                    raiseError(invalidNumber.formatted(fragment), -1);
            } else {
                if (LexicalUtil.isOperator(fragment))
                    fragments.add(new LexicalFragment(4, fragment));
                else if (LexicalUtil.isSeparator(fragment))
                    fragments.add(new LexicalFragment(5, fragment));
                else if (LexicalUtil.isIdentifier(fragment))
                    fragments.add(new LexicalFragment(2, fragment));
                else
                    raiseError(invalidIdentifier.formatted(fragment), -1);
            }
        }
        return fragments;
    }

    private void writeToFile() throws IOException {
        String template = "(%d, \"%s\")";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outFileName))) {
            for (LexicalFragment sf : fragmentBuffer) {
                bw.write(template.formatted(sf.identity, sf.content));
                bw.newLine();
            }
        }
        fragmentBuffer.clear();
    }

    private void raiseError(String why, int returnCode) {
        System.err.println(error);
        System.err.println(why);
        System.exit(returnCode);
    }
}
