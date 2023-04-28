package io.n0sense.mycompiler.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

public class SyntaxUtil {
    public static List<String> keywords = List.of("auto", "break", "case", "char", "const",
            "continue", "default", "do", "double", "else", "enum", "extern", "float", "for",
            "goto", "if", "int", "long", "register", "return", "short", "signed", "sizeof",
            "static", "struct", "switch", "typedef", "union", "unsigned", "void", "volatile",
            "while");
    public static List<String> operator = List.of("+", "-", "*", "/", "=", "<", ">", "<=", ">=",
            "!=");
    public static List<String> separator = List.of(",", ";", "{", "}", "{}", "(", ")", "()");

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
     * @param fileName 需要检查文件的文件名
     * @return 如果左右括号能够匹配，则返回true，否则返回false
     */
    public static boolean isBracketMatched(String fileName) {
        Stack<Character> symbolStack = new Stack<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
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
            else if (SyntaxUtil.isPunctuationMark(src.substring(i, i + 1)))
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
