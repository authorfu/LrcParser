package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import com.github.authorfu.lrcparser.LrcParser;
import com.github.authorfu.lrcparser.parser.Sentence;

public class Test1{

	
	public static void main(String[] args){
		Test1 test=new Test1();
		try{
			BufferedReader reader=test.getTestReader2();
			test.parser=new LrcParser(reader);
		}catch(IOException e){
			e.printStackTrace();
		}
		//test.testBase();
		//test.testBase2();
		//test.testBase3();
		test.testBase4();
	}
	
	public LrcParser parser;
	private void testBase(){

		String[] contents=parser.findAllContents();
		System.out.println("length"+parser.getSentences().size());
		System.out.println(Arrays.toString(contents));
	}
	
	private void testBase2(){
		System.out.println("length"+parser.getSentences().size());
		System.out.println(parser.getSentences());
	}
	
	
	private void testBase3(){
		ArrayList<Sentence> sentences=parser.findAllSentences(-1,-1);
		System.out.println(sentences);
	//	System.out.println(parser.findAllSentences(-1,30400));
	}
	
	private void testBase4(){
		Hashtable<String,String> tags=parser.getTags();
		System.out.println(tags);
	
	}
	
	private LrcParser getLrcParser1(){
		try{
			LrcParser parser=new LrcParser(getTestReader1());
			return parser;
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	private BufferedReader getTestReader1(){
		String lyric="[00:12.00]Line 1 lyrics\n"+
					 "[00:17.20]Line 2 lyrics\n"+
					 "[00:21.10]Line 3 lyrics";
		return new BufferedReader(new StringReader(lyric));
		
	}
	
	private BufferedReader getTestReader2(){
		String filename="src/com/github/authorfu/lrcparser/test/test1.lrc";
		File file=new File(filename);
		System.out.println("file existed? "+file.exists());
		try{
			BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(file),"GB2312"));
			return reader;
		}catch(UnsupportedEncodingException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(FileNotFoundException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
