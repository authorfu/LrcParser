package com.github.authorfu.lrcparser.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parse a BufferedReader to a list of Sentence and other <i>tags</i> {@link http
 * ://en.wikipedia.org/wiki/LRC_%28file_format%29}
 * 
 * @author authorfu
 */
public class LyricParser{
	/**
	 * exception to indicate error to parse a line
	 * 
	 * @author authorfu
	 */
	private static class LyricParseException extends Exception{
		private static final long serialVersionUID=-4668849369948178657L;

		public LyricParseException(String line){
			super(line);
		}
	}
	public static LyricParser create(BufferedReader reader) throws IOException{
		LyricParser parser=new LyricParser(reader);
		return create(parser);
	}
	private static LyricParser create(LyricParser parser) throws IOException{
		parser.parse();
		parser.close();
		return parser;
	}
	private BufferedReader reader;

	/**
	 * ID Tags [by:Creator of the LRC file] the key is "by" and the value "Creator of the LRC file"
	 */
	private Hashtable<String,String> tags;

	/**
	 * a list of Sentence which presents each line of LRC file
	 */
	private ArrayList<Sentence> sentences;

	/**
	 * pattern "[0-9]+"
	 */
	private Pattern numberPattern;

	/**
	 * need a reader to input content
	 * 
	 * @param reader
	 */
	public LyricParser(BufferedReader reader){
		this.reader=reader;
		sentences=new ArrayList<Sentence>(25);
		tags=new Hashtable<String,String>(0);
		numberPattern=Pattern.compile("[0-9]+");
	}

	/**
	 * @param line
	 * @return the position of "]" in the line
	 * @throws LyricParseException
	 *             if "[" is not the begining and "]" is not found;
	 */
	private int checkLine(String line) throws LyricParseException{
		if (line.length()<3||!"[".equals(line.substring(0,1))){
			throw new LyricParseException(line);
		}
		int posBracketRight=line.indexOf("]");
		if (posBracketRight<2){
			throw new LyricParseException(line);
		}
		return posBracketRight;
	}

	public void close() throws IOException{
		reader.close();
	}

	/**
	 * 2 types of results: [by:Creator of the LRC file] => {"by","Creator of the LRC file"} [00:12.00]Line 1 lyrics
	 * =>{"12000","Line 1 lyrics"} checked if "12000" is number;
	 * 
	 * @param line
	 * @return
	 * @throws LyricParseException
	 */
	private String[] extractTime(String line) throws LyricParseException{
		int posBracketRight=checkLine(line);
		String time=line.substring(1,posBracketRight);

		String[] ts=time.split("\\:",2);
		if (ts.length<2){
			throw new LyricParseException(line);
		}
		String[] result=new String[2];
		if (isNumber(ts[0])){
			result[0]=parseTime(ts)+"";
			result[1]=line.substring(posBracketRight+1);
		}else if (!isNumber(ts[0])){
			result=ts;
		}
		return result;
	}

	public ArrayList<Sentence> getSentences(){
		return sentences;
	}

	public Hashtable<String,String> getTags(){
		return tags;
	}

	private boolean isNumber(String str){
		Matcher isNum=numberPattern.matcher(str);
		return isNum.matches();
	}

	/**
	 * sort sentences by starttime
	 */
	private void organize(){
		Collections.sort(sentences,new Sentence.SentenceComparator());
		int size=sentences.size();
		for(int i=0;i<size;i++){
			sentences.get(i).setIndex(i);
			if (i<size-1){
				long toTime=sentences.get(i+1).getFromTime()-1;
				sentences.get(i).setToTime(toTime);
			}
		}
	}

	/**
	 * parse lines
	 * 
	 * @throws IOException
	 *             BufferedReader IOException
	 */
	public void parse() throws IOException{
		String temp=null;
		while((temp=reader.readLine())!=null){
			parseLine(temp.trim());
		}
		organize();
	}

	private void parseLine(String line){
		String[] timeAndContent;
		ArrayList<Sentence> sentences=new ArrayList<Sentence>(2);
		String time="";
		while(true){
			// loop to support format like [00:12.00][00:17.20]F: Line 2 lyrics
			try{
				timeAndContent=extractTime(line); // exception will throw here
				time=timeAndContent[0];
				line=timeAndContent[1];
				if (!isNumber(time)){
					tags.put(time,line);
					break;
				}
				Sentence sent=new Sentence(Long.parseLong(time));
				sentences.add(sent);
			}catch(LyricParseException e){
				break;
			}
		}
		for(Sentence sent:sentences){
			sent.setContent(line);
		}
		this.sentences.addAll(sentences);
		sentences=null;
	}

	/**
	 * @param ts
	 *            time components format like 00:15.30
	 * @return long milliseconds
	 * @throws LyricParseException
	 */
	public static long parseTime(String[] ts) throws LyricParseException{
		double seconds;
		try{
			seconds=Double.parseDouble(ts[1]);
		}catch(NumberFormatException e){
			throw new LyricParseException(ts[1]);
		}
		double s=Integer.parseInt(ts[0])*60+seconds;
		return Math.round(s*1000);
	}

}
