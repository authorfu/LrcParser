package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;

import com.github.authorfu.lrcparser.LrcParser;
import com.github.authorfu.lrcparser.Lyric;
import com.github.authorfu.lrcparser.parser.Sentence;

public class TestSample{
	public static void main(String[] args){
		ex2();
	}
	private static void ex1(){
String lyricString="[ar:Lyrics artist]\n"+
		 "[00:12.00]Line 1 lyrics\n"+
		 "[00:17.20]Line 2 lyrics\n"+
		 "[00:21.10]Line 3 lyrics";
BufferedReader reader =new BufferedReader(new StringReader(lyricString));
Lyric lyric;
try{
	lyric=LrcParser.create(reader);
	String[] contents=lyric.findAllContents();
	System.out.println(Arrays.toString(contents));
}catch(IOException e){
	e.printStackTrace();
}
	}
	
	
	private static void ex2(){
		String lyricString="[ar:Lyrics artist]\n"+
				 "[00:12.00]Line 1 lyrics\n"+
				 "[00:17.20]Line 2 lyrics\n"+
				 "[00:21.10]Line 3 lyrics";
		BufferedReader reader =new BufferedReader(new StringReader(lyricString));
		Lyric lyric;
		try{
			lyric=LrcParser.create(reader);
			ArrayList<Sentence> sentences=lyric.findAllSentences(-1,-1);
			System.out.println(sentences);
			lyric.getTags();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
