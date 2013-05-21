#Basic Usage
an introduction of LRC format can be found [here](http://en.wikipedia.org/wiki/LRC_%28file_format%29 "here")

####1. From a lyric file content to all readable lyric 

    String lyricString="[00:12.00]Line 1 lyrics\n"+
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

result:
> [Line 1 lyrics, Line 2 lyrics, Line 3 lyrics]

####2.More info
    ArrayList<Sentence> sentences=lyric.findAllSentences(-1,-1);
    System.out.println(sentences);

result:
> [{index:0|12000(Line 1 lyrics)17199}, {index:1|17200(Line 2 lyrics)21099}, {index:2|21100(Line 3 lyrics)-1}]

#####Sentence
a Sentence is a class which presents a line of lyric from `fromTime` to `toTime` filled by a `content` and positioned by an `index` .

	private long fromTime=-1;// milliseconds, sentence start include
	private long toTime=-1;// milliseconds,sentence end time include
	private String content="";// content of the sentence
	private int index=-1;//position of the sentence in the lyric

####3.ID Tags 
ID Tags info can be found by a `HashTable<String,String> tags= lyric.getTags();`


####4. more useful methods
see `com.github.authorfu.lrcparser.Lyric`