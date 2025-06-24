package dev.stone.practice.util;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 24/06/2025
 * Project: Practice
 */
public class WordUtil {

    public static String toCapital(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }

    public static String toCapitalEachWord(String word) {
        List<String> words = new ArrayList<>();
        for (String s : word.split(" ")) {
            words.add(toCapital(s));
        }
        return StringUtils.join(words, " ");
    }

    public static String formatWords(String word) {
        return toCapitalEachWord(word.replace("_", " "));
    }

}
