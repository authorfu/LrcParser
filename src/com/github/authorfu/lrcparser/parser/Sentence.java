/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.authorfu.lrcparser.parser;

import java.io.Serializable;
import java.util.Comparator;

/**
 * content,fromTime,toTime and others
 * 
 * @author authorfu
 */
public class Sentence implements Serializable{

	private static final long serialVersionUID=-7463622619946509670L;
	private long fromTime=-1;// milliseconds, sentence start include
	private long toTime=-1;// milliseconds,sentence end time include
	private String content="";// content of the sentence
	private int index=-1;//position of the sentence in the lyric
	
	// private final static long DISAPPEAR_TIME = 1000L;//

	public Sentence(long fromTime){
		this("",fromTime,-1);
	}

	public Sentence(String content){
		this(content,-1,-1);
	}

	public Sentence(String content,long fromTime){
		this(content,fromTime,-1);
	}

	public Sentence(String content,long fromTime,long toTime){
		this.content=content;
		this.fromTime=fromTime;
		this.toTime=toTime;
	}

	public String getContent(){
		return content;
	}

	/**
	 * the duration of the sentence
	 * 
	 * @return toTime-fromTime+1; in millisecondes
	 */
	public long getDuring(){
		return toTime-fromTime+1;
	}

	public long getFromTime(){
		return fromTime;
	}

	public long getToTime(){
		return toTime;
	}

	/**
	 * check if time is between fromTime and toTime if time is negative return true
	 **/
	public boolean isInTime(long time){
		if (time<0)
			return true;
		boolean fromTimeCheck=(fromTime==-1||time>=fromTime);
		boolean toTimeCheck=(toTime==-1||time<=toTime);
		return fromTimeCheck&&toTimeCheck;
	}

	public void setContent(String content){
		this.content=content;
	}

	public void setFromTime(long fromTime){
		this.fromTime=fromTime;
	}

	public void setToTime(long toTime){
		this.toTime=toTime;
	}

	public String toString(){
		return "{index:"+index+"|"+fromTime+"("+content+")"+toTime+"}";
	}

	public static class SentenceComparator implements Comparator<Sentence>{

		@Override
		public int compare(Sentence sent1,Sentence sent2){
			return (int)(sent1.getFromTime()-sent2.getFromTime());
		}

	}

	public int getIndex(){
		return index;
	}

	public void setIndex(int index){
		this.index=index;
	}
	
	
}
