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
import java.io.InputStreamReader;

import com.github.authorfu.lrcparser.parser.LyricParser;

/**
 * @author authorfu <br>
 *         Wrapper for {@link LyricParser} Parse lrc format content from Stream to readable contents Basic usage: see
 *         test package for more examples
 */
public class LrcParser{
	private Lyric lyric;
	
	public LrcParser(BufferedReader reader) throws IOException{
		this(LyricParser.create(reader));
	}


	private LrcParser(LyricParser parser){
		lyric=new Lyric(parser.getTags(),parser.getSentences());
	}

	public Lyric getLyric(){
		return lyric;
	}
	
	public static Lyric create(BufferedReader reader) throws IOException{
		return (new LrcParser(reader)).getLyric();
	}

	public static Lyric create(InputStream in) throws IOException{
		return create(new BufferedReader(new InputStreamReader(in)));
	}

}
