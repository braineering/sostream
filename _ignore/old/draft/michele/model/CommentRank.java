package com.threecore.project.draft.michele.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.flink.api.java.tuple.Tuple2;

import com.threecore.project.model.type.Chronological;
import com.threecore.project.model.type.Copyable;
import com.threecore.project.model.type.Stringable;
import com.threecore.project.tool.TimeFormat;

@Deprecated
public class CommentRank extends Tuple2<Long, List<CommentScore>> 
	implements Chronological, Stringable, Copyable<CommentRank> {

	private static final long serialVersionUID = -4261601273021853123L;
	
	public static final String DELIMITER = ",";
	
	public static final Long TS_UNDEFINED = 0L;
	
	public static final List<CommentScore> newList = null;
	
	public CommentRank() {
		this(TS_UNDEFINED, null);
	}
	
	public CommentRank(final Long ts, List<CommentScore> list) {		
		super(ts, list);
	}

	@Override
	public long getTimestamp(){
		return super.f0;
	}
	
	protected void setTimestamp(final Long ts) {
		super.f0 = ts;
	}
	
	public List<CommentScore> getList() {
		return super.f1;
	}
	
	protected void addCommentAtList(final CommentScore comment) {
		super.f1.add(comment);
	}
	
	protected CommentScore getHeadOfList() {
		return super.f1.get(0);
	}

	protected CommentScore getCommentScoreFromPosition(final int pos) {
		return super.f1.get(pos);
	}
		
	public CommentScore get(final int pos) {
		assert (pos >= 1) : "pos must be >= 1";
		
		if (pos == 1)
			return this.getHeadOfList();
		else if (pos >= 2)
			return this.f1.get(pos);
		else
			return null;
	}
	
	public List<CommentScore> getAll() {
		List<CommentScore> list = new ArrayList<CommentScore>();
		
		for(int i = 0; i< this.f1.size(); i++)
			list.add(this.f1.get(i));
		
		return list;
	}
	
	public CommentRank copy(final CommentRank rank) {
		
		this.f0 = rank.getTimestamp();
		this.f1 = rank.getList();
		return this;
	}	
	
	@Override
	public CommentRank copy() {
		return this.touch(this.getTimestamp());
	}
	
	public CommentRank touch(final Long ts) {
		return new CommentRank(ts, this.getList());
	}	
	
	public void updateWith(final CommentRank other) {
		if (TimeFormat.isAfter(other.getTimestamp(), this.getTimestamp()))
			this.setTimestamp(other.getTimestamp());
		
		Set<CommentScore> set = new HashSet<CommentScore>();
		
		for(int i = 0; i< other.f1.size(); i++)
			set.add(other.f1.get(i));
		
		List<CommentScore> list = new ArrayList<CommentScore>();
		
		java.util.Collections.sort(list);
		java.util.Collections.reverse(list);
		
		for(int i = 0; i< list.size(); i++)
			this.addCommentAtList(list.get(i));
	}
	
	/*public boolean isUpper(final CommentRank other) {
		if (this.getThird().isDefined())
			return this.getThird().compareTo(other.getFirst()) == 1;
		else if (this.getSecond().isDefined())
			return this.getSecond().compareTo(other.getFirst()) == 1;
		else if (this.getFirst().isDefined())
			return this.getFirst().compareTo(other.getFirst()) == 1;
		else
			return false;
	}
	
	public boolean isLower(final CommentRank other) {
		if (other.getThird().isDefined())
			return this.getFirst().compareTo(other.getThird()) == -1;
		else if (other.getSecond().isDefined())
			return this.getFirst().compareTo(other.getSecond()) == -1;
		else if (other.getFirst().isDefined())
			return this.getFirst().compareTo(other.getFirst()) == -1;
		else
			return false;
	}*/
	
	public boolean isEquivalent(final CommentRank other) 
	{
		if(this.f1.size() != other.f1.size())
			return false;
		
		boolean temp = true;
		for(int i=0; i< this.f1.size(); i++) {
			temp = temp && this.f1.get(i).getCommentId().equals(other.get(i).getCommentId());
		}
		
		return temp;
	}
	
	public boolean isHashEquivalent(final CommentRank other) 
	{
		if(this.f1.size() != other.f1.size())
			return false;
		
		boolean temp = true;
		for(int i=0; i< this.f1.size(); i++) {
			temp = temp && this.f1.get(i).getCommentId().hashCode() == other.get(i).getCommentId().hashCode();
		}
		
		return temp;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CommentRank))
			return false;
		
		final CommentRank other = (CommentRank) obj;
		boolean temp = true;
		temp = temp && this.getTimestamp() == other.getTimestamp();
		for(int i=0; i< this.f1.size(); i++) {
			temp = temp && this.f1.get(i).getCommentId().hashCode() == other.get(i).getCommentId().hashCode();
		}
		
		return temp;
	}	
	
	/*@Override
	public int hashCode() {
		return Objects.hash(this.getFirst(), this.getSecond(), this.getThird());
	}*/
	
	@Override
	public String toString() {
		
		ToStringBuilder ts = new ToStringBuilder(this);
		
		for(int i=0; i< this.f1.size(); i++) {
			if(this.get(i)!=null)
				ts.append("Comment"+Integer.toString(i),this.get(i));
		}
		
		return ts.toString();
	}
	
	@Override
	public String asString() {
		StringJoiner sj = new StringJoiner(CommentRank.DELIMITER);
		
		for(int i=0; i< this.f1.size(); i++) {
			sj.add(this.get(i).asString());
		}
		return sj.toString();			
	}
	
	/*public static CommentRank fromString(final String line) {
		final String array[] = line.split("[" + CommentRank.DELIMITER + "]", -1);
		return CommentRank.fromArray(array);
	}
	
	public static CommentRank fromArray(final String array[]) {
		assert (array.length == 13) : "PostRank array must have a size of 13.";
		
		final Long ts = TimeFormat.parseLongWithoutOffset(array[0]);
		final Long top1_post_id = (array[1].equals(PostScore.UNDEFINED_STRING)) ? PostScore.UNDEFINED_LONG : Long.parseLong(array[1]);
		final Long top1_user_id = (array[2].equals(PostScore.UNDEFINED_STRING)) ? PostScore.UNDEFINED_LONG : Long.parseLong(array[2]);
		final Long top1_score = (array[3].equals(PostScore.UNDEFINED_STRING)) ? PostScore.UNDEFINED_LONG : Long.parseLong(array[3]);
		final Long top1_commenters = (array[4].equals(PostScore.UNDEFINED_STRING)) ? PostScore.UNDEFINED_LONG : Long.parseLong(array[4]);
		final Long top2_post_id = (array[5].equals(PostScore.UNDEFINED_STRING)) ? PostScore.UNDEFINED_LONG : Long.parseLong(array[5]);
		final Long top2_user_id = (array[6].equals(PostScore.UNDEFINED_STRING)) ? PostScore.UNDEFINED_LONG : Long.parseLong(array[6]);
		final Long top2_score = (array[7].equals(PostScore.UNDEFINED_STRING)) ? PostScore.UNDEFINED_LONG : Long.parseLong(array[7]);
		final Long top2_commenters = (array[8].equals(PostScore.UNDEFINED_STRING)) ? PostScore.UNDEFINED_LONG : Long.parseLong(array[8]);
		final Long top3_post_id = (array[9].equals(PostScore.UNDEFINED_STRING)) ? PostScore.UNDEFINED_LONG : Long.parseLong(array[9]);
		final Long top3_user_id = (array[10].equals(PostScore.UNDEFINED_STRING)) ? PostScore.UNDEFINED_LONG : Long.parseLong(array[10]);
		final Long top3_score = (array[11].equals(PostScore.UNDEFINED_STRING)) ? PostScore.UNDEFINED_LONG : Long.parseLong(array[11]);
		final Long top3_commenters = (array[12].equals(PostScore.UNDEFINED_STRING)) ? PostScore.UNDEFINED_LONG : Long.parseLong(array[12]);
		
		PostScore first = new PostScore(top1_post_id, top1_user_id, top1_score, top1_commenters);
		PostScore second = new PostScore(top2_post_id, top2_user_id, top2_score, top2_commenters);
		PostScore third = new PostScore(top3_post_id, top3_user_id, top3_score, top3_commenters);
		
		return new CommentRank(ts, first, second, third);
	}*/
}
