package io.n0sense.mycompiler.parser;

import io.n0sense.mycompiler.objects.SyntaxFragment;
import io.n0sense.mycompiler.util.SyntaxUtil;

import static io.n0sense.mycompiler.constant.ErrorConstants.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SyntaxAnalyzer {
    String inFileName;
    String outFileName;
    int bufferMax = 5000;
    List<SyntaxFragment> fragmentBuffer = new ArrayList<>(bufferMax);

    public SyntaxAnalyzer(String inFileName, String outFileName) {
        this.inFileName = inFileName;
        this.outFileName = outFileName;
    }

    public void process() throws IOException {
        File sourceFile = new File(inFileName);
        // 清空输出文件的内容（创建一个FileWriter，但不向其写入任何内容就flush然后close，其内容就会被清空）
//        new FileWriter(outFileName, false).close();
        // 对文件中的括号进行匹配
        if (!SyntaxUtil.isBracketMatched(inFileName)) {
            raiseError(bracketMismatched.formatted(inFileName), -2);
        }
        try (BufferedReader rd = new BufferedReader(new FileReader(sourceFile))) {
            String line = rd.readLine();
            while (line != null) {
                // 不处理没有内容的行
                if (line.trim().length() != 0) {
                    List<SyntaxFragment> fragments =
                            analyzeSyntaxFragments(SyntaxUtil.split(line).split(" "));
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

    private List<SyntaxFragment> analyzeSyntaxFragments(String[] parts) {
        List<SyntaxFragment> fragments = new ArrayList<>();
        for (String fragment : parts) {
            if (fragment.length() == 0)
                continue;
            // 判断顺序：保留关键字->标点符号->数字和标识符
            if (SyntaxUtil.isKeyword(fragment)) {
                fragments.add(new SyntaxFragment(1, fragment));
                continue;
            }
            // 判断首字符是否为数字，如果是，则判断整个串是否为数字，否则判断是否为标识符
            if (Character.isDigit(fragment.codePointAt(0))) {
                if (SyntaxUtil.isNumber(fragment))
                    fragments.add(new SyntaxFragment(3, fragment));
                else
                    raiseError(invalidNumber.formatted(fragment), -1);
            } else {
                if (SyntaxUtil.isIdentifier(fragment))
                    fragments.add(new SyntaxFragment(2, fragment));
                else if (SyntaxUtil.isOperator(fragment))
                    fragments.add(new SyntaxFragment(4, fragment));
                else if (SyntaxUtil.isSeparator(fragment))
                    fragments.add(new SyntaxFragment(5, fragment));
                else
                    raiseError(invalidIdentifier.formatted(fragment), -1);
            }
        }
        return fragments;
    }

    private void writeToFile() throws IOException {
        String template = "(%d, \"%s\")";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outFileName))) {
            for (SyntaxFragment sf : fragmentBuffer) {
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
