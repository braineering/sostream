package com.threecore.project.draft.michele.model.rank.compute;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.threecore.project.draft.michele.model.CommentRank;
import com.threecore.project.draft.michele.model.CommentScore;
import com.threecore.project.draft.michele.model.rank.CommentRanking;
import com.threecore.project.tool.TimeFormat;

@Deprecated
public abstract class AbstractCommentRanking implements CommentRanking {

	private static final long serialVersionUID = 2328032462130784867L;

	public static final String DELIMITER = "|";
	
	protected Long rankMaxSize;
	
	protected Long ts;
	
	public AbstractCommentRanking() {
		this.rankMaxSize = 0L;
		this.ts = 0L;
	}
	
	public AbstractCommentRanking(final long k, final CommentRanking other) {
		assert (other != null) : "other must be != null.";
		this.rankMaxSize = k;
		this.ts = other.getTimestamp();
	}	
	
	@Override
	public long getTimestamp() {
		return this.ts;
	}
	
	protected void setTimestamp(final Long ts) {
		this.ts = ts;
	}
	
	@Override
	public abstract long getRankSize();
	
	@Override
	public abstract int getRankOf(final Long postId);
	
	@Override
	public CommentScore getCommentScoreOfPosition(int position) {
		return this.getRanking().get(position);
	}
	
	protected void setCommentScoreOfPosition(int position, final CommentScore score) {
		this.getRanking().add(position, score);
	}
	
	@Override
	public abstract List<CommentScore> getRanking();	
	
	@Override
	public abstract void updateWith(final CommentRanking rank);
	
	@Override
	public abstract void updateWith(final CommentScore score);	
	
	@Override
	public CommentRank toCommentRank() {
		
		/**
		 * DA SISTEMARE
		 */
		
		CommentRank rank = new CommentRank();
		rank.f0 = getTimestamp();
		rank.f1 = getRanking();
		
		return rank;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AbstractCommentRanking))
			return false;
		
		final AbstractCommentRanking other = (AbstractCommentRanking) obj;
		
		boolean temp = true;
		temp = temp && this.getTimestamp() == other.getTimestamp();
		for(long i=0; i< this.getRankSize(); i++) {
			temp = temp && this.getRankOf(i)  == other.getRankOf(i);
		}
		
		return temp;
		
	}	
	
	/*@Override
	public int hashCode() 
	{
		return Objects.hash(this.getFirst(), this.getSecond(), this.getThird());
	}*/

	@Override
	public String toString() {
		
		ToStringBuilder ts = new ToStringBuilder(this);
		
		for(long i=0; i< this.getRankSize(); i++) {
			ts.append("Comment"+Long.toString(i),this.getRankOf(i));
		}
		
		return ts.toString();
	}
	
	@Override
	public String asString() {
		return this.toCommentRank().asString();	
	}
	
	protected void touchWithTimestamp(final Long ts) {
		if (TimeFormat.isAfter(ts, this.getTimestamp()))
			this.setTimestamp(ts);
	}

}
