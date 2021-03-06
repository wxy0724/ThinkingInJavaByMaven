package com.fang.textsimilarity.nlp;

import java.util.Arrays;


/**
 * @author
 */
public class MyExplainJaroWinklerDistance {

    private float threshold = 0.7f;

    private int[] matches(String s1, String s2) {
        String max, min;
        if (s1.length() > s2.length()) {
            max = s1;
            min = s2;
        } else {
            max = s2;
            min = s1;
        }
        // 两个分别来自s1和s2的字符如果相距不超过 floor(max(|s1|,|s2|) / 2) -1, 我们就认为这两个字符串是匹配的, 因此，查找时，
        // 超过此距离则停止
        int range = Math.max(max.length() / 2 - 1, 0);
        // 短的字符串, 与长字符串匹配的索引位
        int[] matchIndexes = new int[min.length()];
        Arrays.fill(matchIndexes, -1);
        // 长字符串匹配的标记
        boolean[] matchFlags = new boolean[max.length()];
        // 匹配的数目
        int matches = 0;
        // 外层循环，字符串最短的开始
        for (int mi = 0; mi < min.length(); mi++) {
            char c1 = min.charAt(mi);
            // 可能匹配的距离，包括从给定位置从前查找和从后查找
            for (int xi = Math.max(mi - range, 0), xn = Math.min(mi + range + 1, max
                    .length()); xi < xn; xi++) {
                // 排除被匹配过的字符，若找到匹配的字符，则停止
                if (!matchFlags[xi] && c1 == max.charAt(xi)) {
                    matchIndexes[mi] = xi;
                    matchFlags[xi] = true;
                    matches++;
                    break;
                }
            }
        }

        // 记录min字符串里匹配的字符串，保持顺序
        char[] ms1 = new char[matches];
        // 记录max字符串里匹配的字符串，保持顺序
        char[] ms2 = new char[matches];
        for (int i = 0, si = 0; i < min.length(); i++) {
            if (matchIndexes[i] != -1) {
                ms1[si] = min.charAt(i);
                si++;
            }
        }
        for (int i = 0, si = 0; i < max.length(); i++) {
            if (matchFlags[i]) {
                ms2[si] = max.charAt(i);
                si++;
            }
        }

        // 查找换位的数目
        int transpositions = 0;
        for (int mi = 0; mi < ms1.length; mi++) {
            if (ms1[mi] != ms2[mi]) {
                transpositions++;
            }
        }

        // 查找相同前缀的数目
        int prefix = 0;
        for (int mi = 0; mi < min.length(); mi++) {
            if (s1.charAt(mi) == s2.charAt(mi)) {
                prefix++;
            } else {
                break;
            }
        }

        // 返回匹配数目（m），换位的数目（t），相同的前缀的数目，字符串最长
        return new int[] { matches, transpositions / 2, prefix, max.length() };
    }

    public float getDistance(String s1, String s2) {
        int[] mtp = matches(s1, s2);
        //  返回匹配数目（m）
        float m = (float) mtp[0];
        if (m == 0) {
            return 0f;
        }

        // Jaro Distance
        float j = ((m / s1.length() + m / s2.length() + (m - mtp[1]) / m)) / 3;

        // 计算Jaro-Winkler Distance， 这里调整分数的因数=Math.min(0.1f, 1f / mtp[3])
        float jw = j < getThreshold() ? j : j + Math.min(0.1f, 1f / mtp[3]) * mtp[2]
                * (1 - j);
        return jw;
    }

    /**
     * Sets the threshold used to determine when Winkler bonus should be used.
     * Set to a negative value to get the Jaro distance.
     * @param threshold the new value of the threshold
     */
    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }

    /**
     * Returns the current value of the threshold used for adding the Winkler bonus.
     * The default value is 0.7.
     * @return the current value of the threshold
     */
    public float getThreshold() {
        return threshold;
    }

}
