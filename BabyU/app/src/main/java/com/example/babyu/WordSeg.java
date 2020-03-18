package com.example.babyu;

import com.chenlb.mmseg4j.ComplexSeg;
import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MMSeg;
import com.chenlb.mmseg4j.Seg;
import com.chenlb.mmseg4j.Word;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;

public class WordSeg {
    public Dictionary dic;
    String ss;
    String txt ="9月1號的日記";

    public WordSeg() {
        System.setProperty("mmseg.dic.path", "./SegChinese/data");//這裡可以指定自訂詞庫 // 如果沒設定就是使用預設
        dic = Dictionary.getInstance();
    }

    public Seg getSeg() {
        return new ComplexSeg(dic);
    }

    public String segWords(String txt, String wordSpilt) throws IOException {
        Reader input = new StringReader(txt);
        StringBuilder sb = new StringBuilder();
        Seg seg = getSeg();
        MMSeg mmSeg = new MMSeg(input, seg);
        Word word = null;
        boolean first = true;
        while((word=mmSeg.next())!=null) {
            if(!first) {
                sb.append(wordSpilt);
            }
            String w = word.getString();
            sb.append(w);
            first = false;
        }
        return sb.toString();
    }
    public void run(String[] args) throws IOException {

        if(args.length > 0) {
            txt = args[0];
        }
        ss = segWords(txt, " ");
        String[] words=ss.split(" ");
        System.out.println(Arrays.toString(words));
    }
    public static void main(String[] args) throws IOException {
        new WordSeg().run(args);
    }
}

