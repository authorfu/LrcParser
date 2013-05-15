package com.github.authorfu;
  
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LyricParser{
	private BufferedReader reader;
	private Hashtable<String,String>infos;
	private ArrayList<Sentence> lyric;
	private Pattern numberPattern;
	private Pattern numericPattern;
	private LyricParser(InputStream in){
		this(new BufferedReader(new InputStreamReader(in)));
	}
	
	private LyricParser(BufferedReader reader){
		this.reader=reader;
		lyric=new ArrayList<Sentence>(25);
		numberPattern = Pattern.compile("[0-9]+");
		numericPattern=Pattern.compile("[0-9]+\\.[0-9]+");
		infos=new Hashtable<String,String>(0);
	}
	
	private void close() throws IOException{
		reader.close();
	}
	
	private void init() throws IOException{
		String temp = null;
		Sentence sentence=null;
		while ((temp = reader.readLine()) != null) {
			try{
				parseLine(temp.trim());
			}catch(LyricParseException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void parseLine(String line) throws LyricParseException{
		String[] timeAndContent;
		ArrayList<Sentence> sentences=new ArrayList<Sentence>(2);
		String time="";
		while(true){
			timeAndContent=extractTime(line);
			time=timeAndContent[0];
			if ("".equals(time)){
				break;
			}else if(!isNumber(time)){
				infos.put(time,line);
				break;
			}
			line=timeAndContent[1]; 
			Sentence sent=new Sentence(Long.parseLong(time));
			sentences.add(sent);
		}
		for(Sentence sent:sentences){
			sent.setContent(line);
		}
		lyric.addAll(sentences);
		sentences=null;
	}
	
	private boolean isNumber(String str){
		Matcher isNum = numberPattern.matcher(str);
		return isNum.matches();
	}
	
	private boolean isNumeric(String str){
		Matcher isNum = numericPattern.matcher(str);
		return isNum.matches();
	}
	
	private String[] extractTime(String line) throws LyricParseException{
		int posBracketRight=checkLine(line);
		String time=line.substring(1,posBracketRight);
		String[] ts=time.split("\\:",2);
		if (ts.length<2){
			throw new LyricParseException(line);
		}
		String[] result=new String[2];
		if (isNumber(ts[0])){
			result[0]=parseTime(ts);
			result[1]=line.substring(posBracketRight);
		}else if (!isNumber(ts[0])){
			result=ts;
		}
		return result;
	}
	
	private int checkLine(String line) throws LyricParseException{
		if (!"[".equals(line.substring(0,1))){
			throw new LyricParseException(line);
		}
		int posBracketRight=line.indexOf("]");
		if (posBracketRight<2){
			throw new LyricParseException(line);
		}
		return posBracketRight;
	}
	
	private String parseTime(String[] ts) throws LyricParseException{
		if (!isNumeric(ts[1])){
			throw new LyricParseException(ts[1]);
		}
		
		double second= Integer.parseInt(ts[0])*60+Double.parseDouble(ts[1]);
		return Math.round(second*1000)+"";
	}
	
	private static class LyricParseException extends Exception{
		public LyricParseException(String line){
			super(line);
		}

		public LyricParseException(){
			super();
		}

		private static final long serialVersionUID=-4668849369948178657L;
		
	}
}
