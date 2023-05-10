package io.n0sense.mycompiler.util;

import java.io.*;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

public class LexicalUtil {
    public static List<String> keywords = List.of("auto", "break", "case", "char", "const",
            "continue", "default", "do", "double", "else", "enum", "extern", "float", "for",
            "goto", "if", "int", "long", "register", "return", "short", "signed", "sizeof",
            "static", "struct", "switch", "typedef", "union", "unsigned", "void", "volatile",
            "while");
    public static List<String> operator = List.of("+", "-", "*", "/", "=", "<", ">", "<=", ">=",
            "!=");
    public static List<String> separator = List.of(",", ";", "{", "}", "{}", "(", ")", "()", "[",
            "]", "[]", "\"");

    public static boolean isKeyword(String s) {
        return keywords.contains(s);
    }

    public static boolean isPunctuationMark(String s) {
        String t = s;
        t = t.replaceAll("\\p{P}", "");
        return t.length() != s.length();
    }

    public static boolean isOperator(String s) {
        return operator.contains(s);
    }

    public static boolean isSeparator(String s) {
        return separator.contains(s);
    }

    public static boolean isIdentifier(String s) {
        // 首字符为数字的字符串不是标识符
        if (Character.isDigit(s.codePointAt(0))) {
            return false;
        }
        String t = s;
        // 这个正则匹配大小写字母、下划线和数字，将匹配项全部删除
        t = t.replaceAll("^[a-zA-Z0-9_]+$", "");
        // 如果删除完后没有内容，说明这个字符串全部内容都是合法的
        return t.length() == 0;
    }

    public static boolean isNumber(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * 对给定的文件进行括号检查，检查左右括号是否匹配。
     * @param targetFile 需要检查文件对象
     * @return 如果左右括号能够匹配，则返回true，否则返回false
     */
    public static boolean isBracketMatched(File targetFile) {
        Stack<Character> symbolStack = new Stack<>();
        try (BufferedReader br = new BufferedReader(new FileReader(targetFile))) {
            String line = br.readLine();
            while (line != null) {
                for (int i = 0; i < line.length(); i++) {
                    switch (line.charAt(i)) {
                        case '(', '{', '[' -> symbolStack.push(line.charAt(i));
                        case ')' -> {
                            if (symbolStack.pop() != '(')
                                return false;
                        }
                        case '}' -> {
                            if (symbolStack.pop() != '{')
                                return false;
                        }
                        case ']' -> {
                            if (symbolStack.pop() != '[')
                                return false;
                        }
                    }
                }
                line = br.readLine();
            }
        } catch (EmptyStackException e) {
            // 操作中遇到空栈操作时，说明右括号过多
            return false;
        } catch (Throwable t) {
            t.printStackTrace();
        }
        // 如果遍历结束时栈内还有符号，说明左括号过多
        return symbolStack.isEmpty();
    }

    /**
     * 移除指定文件中的所有单行注释和多行注释。
     * @param targetFile 需要处理的文件对象
     * @return 如果对文件操作成功，即使文件中没有注释，也会返回true，否则返回false。
     */
    public static boolean removeComments(File targetFile) {
//        String multiLine = "/\\*(.|\\n)*?\\*/";
//        String singleLine =
        // 本来打算用正则表达式做匹配，但是正则表达式很难处理好字符串中的注释，遂改用手动处理
        BufferedReader br;
        BufferedWriter bw;
        List<String> buffer = new ArrayList<>();
        boolean withinString = false;
        boolean withinComment = false;
        try {
            br = new BufferedReader(new FileReader(targetFile));
            String line = br.readLine();
            StringBuilder newLine = new StringBuilder();
            while (line!=null) {
                for (int i = 0; i < line.length(); i++) {
                    // 遍历每个字符，单独处理
                    if (line.charAt(i) == '"' && !withinComment) {
                        // 处理不在注释块内的引号
                        // 为withinString置反。也就是引号内外状态切换
                        withinString = !withinString;
                        newLine.append(line.charAt(i));
                    } else if (line.charAt(i) == '/' && !withinString) {
                        // 下一个字符可能是注释符号，需要进行判断。字符串内的不处理
                        if (i!= 0 && line.charAt(i-1) == '*') {
                            // 判断多行注释的结束。关闭withinComment的标志位
                            withinComment = false;
                        } else if (i == line.length()-1) {
                            // 如果当前下标指向最后一个字符，这个地方就不可能有注释符号了，也就没有继续
                            // 判断的必要了，直接写入
                            newLine.append(line.charAt(i));
                        } else if (line.charAt(i+1) == '/'){
                            // 对于单行注释，后面的全部不处理（也就是直接退出for循环），直接写入并换行
                            break;
                        } else if (line.charAt(i+1) == '*') {
                            // 判断多行注释的开始。对于多行注释，如果withinString不为真的时候，说明
                            // 是需要处理的注释
                            withinComment = true;
                        } else {
                            // 不是注释的情况。正常写入
                            newLine.append(line.charAt(i));
                        }
                    } else {
                        if (!withinComment) {
                            newLine.append(line.charAt(i));
                        }
                    }
                }
                // 处理完当前行内内容，把当前行保存到缓冲区，然后读取下一行
                buffer.add(newLine.toString());
                newLine.setLength(0);
                line = br.readLine();
            }
            // 读取完毕，关闭读取流
            br.close();
            // 然后再打开写入流，把缓冲区的内容写入
            bw = new BufferedWriter(new FileWriter(targetFile));
            for (String s : buffer) {
                // 不保留空行
                if (s.strip().length() != 0) {
                    bw.write(s);
                    bw.newLine();
                }
            }
            bw.flush();
            bw.close();
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    /**
     * 对一句语句进行分词。
     * @param line 需要分词的句子
     * @return 分词完成的句子，使用一个或多个空格分隔。
     */
    public static String split(String line) {
        String src = line.trim();
        StringBuilder buffer = new StringBuilder();
        int[] identifier = new int[src.length()];
        int i;
        for (i = 0; i < src.length(); i++) {
            if ((Character.isLetter(src.charAt(i)) || Character.isDigit(src.charAt(i))))
                identifier[i] = 1;
            else if (isPunctuationMark(src.substring(i, i + 1)))
                identifier[i] = 2;
            else
                identifier[i] = 3;
        }
        for (i = 0; i < src.length() - 1; i++) {
            // 表示当前字符是字母或数字，但后一个字符不是的情况
            if (identifier[i] != identifier[i + 1]) {
                buffer.append(src.charAt(i)).append(" ");
            } else if (identifier[i] == 2 && identifier[i + 1] == 2) {
                buffer.append(src.charAt(i)).append(" ");
            } else {
                buffer.append(src.charAt(i));
            }
        }
        buffer.append(src.charAt(i));
        return buffer.toString();
    }
}
