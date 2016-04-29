package com.threecore.project.operator.deprecated;

import java.util.List;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple6;
import org.apache.flink.util.Collector;

import com.threecore.project.tool.map.Pid2PostHashMap;
import com.threecore.project.tool.rank.Ranking;

@Deprecated
public final class RankBestThreeLong implements FlatMapFunction <Tuple2<Long, Long> , Tuple6<String, Long, String, Long, String, Long>>
{
	private static final long serialVersionUID = -281992164901005691L;
	
	private Pid2PostHashMap<Tuple2<Long,Long>> postMap = new Pid2PostHashMap<Tuple2<Long,Long>>();

	public void flatMap(Tuple2<Long, Long> value, Collector<Tuple6<String, Long, String, Long, String, Long>> out) throws Exception 
	{
		Tuple6<String,Long,String,Long,String,Long> output = new Tuple6<String,Long,String,Long,String,Long>();
		
		if(postMap.containsKey(value.f0)){
			Tuple2<Long,Long> temp = new Tuple2<Long,Long>();
			temp.f0 = value.f0;
			temp.f1 = value.f1;// + postMap.get(value.f0).f1;
			
			postMap.put(temp.f0, temp);
		}
		else{
			postMap.put(value.f0, value);
		}
		
		Ranking<> bestThree = new Ranking(3);
		
		for(int i = 0; i< postMap.getKeySet().size(); i++)
		{
			Tuple2<Long,Long> v = postMap.get(postMap.getKeySet().get(i));
			Rankable post = new RankableObject(v,v.f1);
			bestThree.updateWith(post);
		}
		
		List<Rankable> rankings = bestThree.getRanking();
		
		output.f0 = "-";
		output.f1 = (long) 0;
		output.f2 = "-";
		output.f3 = (long) 0;
		output.f4 = "-";
		output.f5 = (long) 0;
		
		int w = 0;
		
		for (int i = 0; i < rankings.size(); i++) 
		{	
			Tuple2<String, Long> tx = new Tuple2<String, Long>();
			
			tx.f0 = rankings.get(i).getObject().toString();
			tx.f1 = rankings.get(i).getCount();
			
			output.setField(tx.f0, i*2);
			output.setField(tx.f1, (i*2)+1);
			w++;
		}

		for (int j = 2 - w; j>=0; j--) {
			Tuple2<String, Long> tx = new Tuple2<String, Long>();
			
			tx.f0 = "-";
			tx.f1 = (long) 0;
			
			output.setField(tx.f0, j*2);
			output.setField(tx.f1, (j*2)+1);
		}
		
		out.collect(output);
	}
}