/*
 * Copyright (C) 2013 authorfu <fujiayi2012@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.authorfu.lrcparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.github.authorfu.lrcparser.parser.LyricParser;
import com.github.authorfu.lrcparser.parser.Sentence;

/**
 * @author authorfu <br>
 *         Wrapper for {@link LyricParser} Parse lrc format content from Stream to readable contents Basic usage: see
 *         test package for more examples
 */
public class LrcParser{
	private ArrayList<Sentence> sentences;
	private Hashtable<String,String> tags;

	public LrcParser(BufferedReader reader) throws IOException{
		this(LyricParser.create(reader));
	}

	public LrcParser(InputStream in) throws IOException{
		this(LyricParser.create(in));
	}

	private LrcParser(LyricParser parser){
		sentences=parser.getSentences();
		tags=parser.getTags();
	}

	public static String[] findContents(List<Sentence> sentences){
		String[] contents=new String[sentences.size()];
		for(int i=0;i<contents.length;i++){
			contents[i]=sentences.get(i).getContent();
		}
		return contents;
	}

	/**
	 * list all lyric between <code>fromTime</code> to <code>toTime</code>
	 * 
	 * @param fromTime
	 * @param toTime
	 * @return
	 */
	public String[] findAllContents(long fromTime,long toTime){
		return findContents(findAllSentences(fromTime,toTime));
	}

	/**
	 * list all lyric.
	 * 
	 * @return
	 */
	public String[] findAllContents(){
		return findAllContents(-1,-1);
	}

	/**
	 * find Sentence correspondant the line in lyric correspondant at a specific <code>time</code>
	 * 
	 * @param time
	 * @return
	 */
	public Sentence findSentence(long time){
		if (time<0)
			throw new RuntimeException("time<0");
		List<Sentence> sentences=findAllSentences(time,time);
		if (sentences.isEmpty())
			return null;
		return sentences.get(0);
	}

	/**
	 * find content of the line in lyric correspondant at a specific time
	 * 
	 * @param time
	 * @return
	 */
	public String findContent(long time){
		Sentence sent=findSentence(time);
		if (sent==null)
			return null;
		return sent.getContent();
	}

	/**
	 * getAllSentenes between fromTime and toTime
	 * 
	 * @param fromTime
	 *            -1 means no condition limited
	 * @param toTime
	 *            -1 means no condition limited
	 * @return List<Sentence>
	 */
	public ArrayList<Sentence> findAllSentences(long fromTime,long toTime){
		boolean begin=false;
		ArrayList<Sentence> result=new ArrayList<Sentence>(sentences.size()/3);
		if (fromTime>=0&&toTime>=0&&fromTime>toTime){// fromTime>toTime
			return result;
		}
		for(Sentence sent:sentences){
			if (!begin){
				if (sent.getFromTime()>=fromTime){
					begin=true;
					result.add(sent);
				}
			}else{
				if (toTime>=0&&sent.getFromTime()>toTime){
					break;
				}
				result.add(sent);
			}
		}
		return result;
	}

	public ArrayList<Sentence> getSentences(){
		return sentences;
	}

	public Hashtable<String,String> getTags(){
		return tags;
	}

}
