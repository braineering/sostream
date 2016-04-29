package com.threecore.project.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.flink.api.java.tuple.Tuple2;
import org.joda.time.DateTime;

import com.threecore.project.model.rank.Ranking;
import com.threecore.project.model.type.Chronological;
import com.threecore.project.model.type.Copyable;
import com.threecore.project.model.type.Stringable;
import com.threecore.project.tool.JodaTimeTool;

public class CommentRank extends Tuple2<Long, List<CommentScore>> implements Chronological, Stringable, Copyable<CommentRank> {

	private static final long serialVersionUID = 1L;	
	
	public static final CommentRank UNDEFINED_RANK = new CommentRank();	
	
	private int rankMaxSize;
	
	public CommentRank(final int rankMaxSize, final DateTime ts, final List<CommentScore> scores) {		
		super(JodaTimeTool.getMillisFromDateTime(ts), new ArrayList<CommentScore>(scores));
		this.rankMaxSize = rankMaxSize;
	}
	
	public CommentRank(final int rankMaxSize, final DateTime ts, final CommentScore... scores) {		
		super(JodaTimeTool.getMillisFromDateTime(ts), new ArrayList<CommentScore>(Arrays.asList(scores)));
		this.rankMaxSize = rankMaxSize;
	}
	
	public CommentRank(final int rankMaxSize, final DateTime ts) {
		super(JodaTimeTool.getMillisFromDateTime(ts), new ArrayList<CommentScore>());
		this.rankMaxSize = rankMaxSize;
	}				
	
	public CommentRank(final int rankMaxSize, final long ts, final List<CommentScore> scores) {		
		super(ts, new ArrayList<CommentScore>(scores));
		this.rankMaxSize = rankMaxSize;
	}
	
	public CommentRank(final int rankMaxSize, final long ts, final CommentScore... scores) {		
		super(ts, new ArrayList<CommentScore>(Arrays.asList(scores)));
		this.rankMaxSize = rankMaxSize;
	}
	
	public CommentRank(final int rankMaxSize, final long ts) {		
		super(ts, new ArrayList<CommentScore>());
		this.rankMaxSize = rankMaxSize;
	}
	
	public CommentRank(final int rankMaxSize) {
		super(ModelCommons.UNDEFINED_LONG, new ArrayList<CommentScore>());
		this.rankMaxSize = rankMaxSize;
	}
	
	public CommentRank() {
		super(ModelCommons.UNDEFINED_LONG, new ArrayList<CommentScore>());
		this.rankMaxSize = Ranking.DEFAULT_RANK_SIZE;
	}
	
	@Override
	public long getTimestamp(){
		return super.f0;
	}
	
	protected void setTimestamp(final long ts) {
		super.f0 = ts;
	}
	
	public List<CommentScore> getScores() {
		return super.f1;
	}
	
	protected void setScores(final List<CommentScore> scores) {
		super.f1 = scores;
	}
	
	public int getRankMaxSize() {
		return this.rankMaxSize;
	}
	
	public CommentRank copy(final CommentRank rank) {
		this.rankMaxSize = rank.getRankMaxSize();
		this.f0 = rank.getTimestamp();
		this.f1 = new ArrayList<CommentScore>(rank.getScores());
		return this;
	}	
	
	@Override
	public CommentRank copy() {
		return this.touch(this.getTimestamp());
	}
	
	public CommentRank touch(final long ts) {
		return new CommentRank(this.getRankMaxSize(), ts, this.getScores());
	}	
	
	public void overwriteWith(final CommentRank other) {
		if (this.getTimestamp() > other.getTimestamp()) {
			return;
		}
		this.setTimestamp(other.getTimestamp());
		this.setScores(other.getScores());
	}
		
	public boolean isEquivalent(final CommentRank other) {	
		int sizeA = this.getScores().size();
		int sizeB = other.getScores().size();
		if (sizeA != sizeB) {
			return false;
		}
		
		for (int pos = 0; pos < sizeA; pos++) {
			if (this.getScores().get(pos).getCommentId() != other.getScores().get(pos).getCommentId()) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isHashEquivalent(final CommentRank other) {
		return this.getScores().hashCode() == other.getScores().hashCode();
	}
	
	public boolean sameAs(CommentRank other) {
		if (this.getTimestamp() != other.getTimestamp()) {
			return false;
		}
		for (CommentScore scoreA : this.getScores()) {
			for (CommentScore scoreB : other.getScores()) {
				if (!scoreA.sameAs(scoreB)) {
					return false;
				}
			}
		}
		return true;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CommentRank)) {
			return false;
		}
		
		final CommentRank other = (CommentRank) obj;
		
		return this.getTimestamp() == other.getTimestamp() &&
				this.getScores().equals(other.getScores());
	}	
	
	@Override
	public int hashCode() {
		int result = (int) this.getTimestamp() % this.getRankMaxSize();
		result = 31 * result + this.getScores().hashCode();
		return result;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("timestamp", JodaTimeTool.getStringFromMillis(this.getTimestamp()))
				.append("rankMaxSize", this.getRankMaxSize())
				.append("scores", this.getScores())
				.toString();
	}
	
	@Override
	public String asString() {
		StringJoiner string = new StringJoiner(ModelCommons.DELIMITER_OUT);
		string.add(JodaTimeTool.getStringFromMillis(this.getTimestamp()));
		int maxsize = this.getRankMaxSize();
		int remaining = maxsize;
		for (int pos = 0; pos < this.getScores().size(); pos++) {
			string.add(this.getScores().get(pos).getComment());
			remaining--;
		}
		for (int r = remaining; r > 0; r--) {
			string.add("-");
		}		
				
		return string.toString();
	}
	
	public boolean isUpper(final CommentRank other) {
		// to be implemented
		return false;
	}
	
	public boolean isLower(final CommentRank other) {
		// to be implemented
		return false;
	}
	
	public static final CommentRank merge(final CommentRank rank1, final CommentRank rank2) {
		return new CommentRank(); // to be implemented;
	}

}
