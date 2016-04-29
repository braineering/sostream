package com.threecore.project.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.flink.api.java.tuple.Tuple4;
import org.joda.time.DateTime;

import com.threecore.project.model.score.post.comparator.PostScoreComparatorDesc;
import com.threecore.project.model.type.Chronological;
import com.threecore.project.model.type.Copyable;
import com.threecore.project.model.type.Stringable;
import com.threecore.project.tool.JodaTimeTool;

public class PostRank extends Tuple4<Long, PostScore, PostScore, PostScore> 
					  implements Chronological, Stringable, Copyable<PostRank> {

	private static final long serialVersionUID = 1L;	
	
	public static final PostRank UNDEFINED_RANK = new PostRank();	
	
	public PostRank(final DateTime ts, final PostScore first, final PostScore second, final PostScore third) {		
		super(JodaTimeTool.getMillisFromDateTime(ts), first, second, third);
	}
	
	public PostRank(final DateTime ts, final PostScore first, final PostScore second) {		
		super(JodaTimeTool.getMillisFromDateTime(ts), first, second, PostScore.UNDEFINED_SCORE);
	}
	
	public PostRank(final DateTime ts, final PostScore first) {		
		super(JodaTimeTool.getMillisFromDateTime(ts), first, PostScore.UNDEFINED_SCORE, PostScore.UNDEFINED_SCORE);
	}
	
	public PostRank(final DateTime ts) {
		super(JodaTimeTool.getMillisFromDateTime(ts), PostScore.UNDEFINED_SCORE, PostScore.UNDEFINED_SCORE, PostScore.UNDEFINED_SCORE);
	}				
	
	public PostRank(final long ts, final PostScore first, final PostScore second, final PostScore third) {		
		super(ts, first, second, third);
	}	
	
	public PostRank(final long ts, final PostScore first, final PostScore second) {		
		super(ts, first, second, PostScore.UNDEFINED_SCORE);
	}
	
	public PostRank(final long ts, final PostScore first) {		
		super(ts, first, PostScore.UNDEFINED_SCORE, PostScore.UNDEFINED_SCORE);
	}
	
	public PostRank(final long ts) {		
		super(ts, PostScore.UNDEFINED_SCORE, PostScore.UNDEFINED_SCORE, PostScore.UNDEFINED_SCORE);
	}
	
	public PostRank() {
		super(ModelCommons.UNDEFINED_LONG, PostScore.UNDEFINED_SCORE, PostScore.UNDEFINED_SCORE, PostScore.UNDEFINED_SCORE);
	}
	
	@Override
	public long getTimestamp(){
		return super.f0;
	}
	
	protected void setTimestamp(final long ts) {
		super.f0 = ts;
	}
	
	public PostScore getFirst() {
		return super.f1;
	}
	
	protected void setFirst(final PostScore first) {
		super.f1 = first;
	}
	
	public PostScore getSecond() {
		return super.f2;
	}
	
	protected void setSecond(final PostScore second) {
		super.f2 = second;
	}
	
	public PostScore getThird() {
		return super.f3;
	}
	
	protected void setThird(final PostScore third) {
		super.f3 = third;
	}
	
	public PostScore get(final int pos) {		
		if (pos == 1)
			return this.getFirst();
		else if (pos == 2)
			return this.getSecond();
		else if (pos == 3)
			return this.getThird();
		else
			return null;
	}
	
	public List<PostScore> getScores() {
		List<PostScore> list = new ArrayList<PostScore>();
		
		list.add(this.getFirst());
		list.add(this.getSecond());
		list.add(this.getThird());			
		
		return list;
	}
	
	public PostRank copy(final PostRank rank) {
		this.f0 = rank.getTimestamp();
		this.f1 = rank.getFirst();
		this.f2 = rank.getSecond();
		this.f3 = rank.getThird();
		return this;
	}	
	
	@Override
	public PostRank copy() {
		return this.touch(this.getTimestamp());
	}
	
	public PostRank touch(final long ts) {
		return new PostRank(ts, this.getFirst(), this.getSecond(), this.getThird());
	}	
	
	public void overwriteWith(final PostRank other) {
		if (this.getTimestamp() > other.getTimestamp()) {
			return;
		}
		this.setTimestamp(other.getTimestamp());
		this.setFirst(other.getFirst());
		this.setSecond(other.getSecond());
		this.setThird(other.getThird());
	}
	
	public void updateWith(final PostRank other) {
		if (other.getTimestamp() > this.getTimestamp()) {
			this.setTimestamp(other.getTimestamp());
		}
		
		Map<Long, PostScore> map = new HashMap<Long, PostScore>();
		
		if (this.getFirst().isDefined())
			map.put(this.getFirst().getPostId(), this.getFirst());
		if (this.getSecond().isDefined())
			map.put(this.getSecond().getPostId(), this.getSecond());
		if (this.getThird().isDefined())
			map.put(this.getThird().getPostId(), this.getThird());
		
		if (other.getFirst().isDefined()) {
			if (map.containsKey(other.getFirst().getPostId())) {
				if (map.get(other.getFirst().getPostId()).getTimestamp() < other.getFirst().getTimestamp()) {
					map.put(other.getFirst().getPostId(), other.getFirst());
				}
			} else {
				map.put(other.getFirst().getPostId(), other.getFirst());
			}
		}
		
		if (other.getSecond().isDefined()) {
			if (map.containsKey(other.getSecond().getPostId())) {
				if (map.get(other.getSecond().getPostId()).getTimestamp() < other.getSecond().getTimestamp()) {
					map.put(other.getSecond().getPostId(), other.getSecond());
				}
			} else {
				map.put(other.getSecond().getPostId(), other.getSecond());
			}
		}
		
		if (other.getThird().isDefined()) {
			if (map.containsKey(other.getThird().getPostId())) {
				if (map.get(other.getThird().getPostId()).getTimestamp() < other.getThird().getTimestamp()) {
					map.put(other.getThird().getPostId(), other.getThird());
				}
			} else {
				map.put(other.getThird().getPostId(), other.getThird());
			}
		}
		
		List<PostScore> list = new ArrayList<PostScore>(map.values());
		
		//java.util.Collections.sort(list, new PostScoreComparatorDesc());
		java.util.Collections.sort(list, PostScoreComparatorDesc.getInstance());
		
		this.setFirst((list.size() >= 1) ? list.get(0) : PostScore.UNDEFINED_SCORE);
		this.setSecond((list.size() >= 2) ? list.get(1) : PostScore.UNDEFINED_SCORE);
		this.setThird((list.size() >= 3) ? list.get(2) : PostScore.UNDEFINED_SCORE);
	}
	
	public boolean isUpper(final PostRank other) {
		if (this.getThird().isDefined()) {
			return this.getThird().compareTo(other.getFirst()) == 1;
		} else if (this.getSecond().isDefined()) {
			return this.getSecond().compareTo(other.getFirst()) == 1;
		} else if (this.getFirst().isDefined()) {
			return this.getFirst().compareTo(other.getFirst()) == 1;
		} else {
			return false;
		}
	}
	
	public boolean isLower(final PostRank other) {
		if (other.getThird().isDefined()) {
			return this.getFirst().compareTo(other.getThird()) == -1;
		} else if (other.getSecond().isDefined()) {
			return this.getFirst().compareTo(other.getSecond()) == -1;
		} else if (other.getFirst().isDefined()) {
			return this.getFirst().compareTo(other.getFirst()) == -1;
		} else {
			return false;
		}
	}
	
	public boolean isEquivalent(final PostRank other) {		
		return this.getFirst().getPostId() == other.getFirst().getPostId() &&
				this.getSecond().getPostId() == other.getSecond().getPostId() &&
				this.getThird().getPostId() == other.getThird().getPostId();
	}
	
	public boolean isHashEquivalent(final PostRank other) {
		return this.getFirst().hashCode() == other.getFirst().hashCode() &&
				this.getSecond().hashCode() == other.getSecond().hashCode() &&
				this.getThird().hashCode() == other.getThird().hashCode();
	}
	
	public boolean sameAs(PostRank other) {
		return this.getTimestamp() == other.getTimestamp() &&
				this.getFirst().sameAs(other.getFirst()) &&
				this.getSecond().sameAs(other.getSecond()) &&
				this.getThird().sameAs(other.getThird());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PostRank)) {
			return false;
		}
		
		final PostRank other = (PostRank) obj;
		
		return this.getTimestamp() == other.getTimestamp() &&
				this.getFirst().equals(other.getFirst()) &&
				this.getSecond().equals(other.getSecond()) &&
				this.getThird().equals(other.getThird());
	}	
	
	@Override
	public int hashCode() {
		int result = this.getFirst() != null ? this.getFirst().hashCode() : 0;
		result = 31 * result + (this.getSecond() != null ? this.getSecond().hashCode() : 0);
		result = 31 * result + (this.getThird() != null ? this.getThird().hashCode() : 0);
		return result;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("timestamp", JodaTimeTool.getStringFromMillis(this.getTimestamp()))
				.append("top1", this.getFirst())
				.append("top2", this.getSecond())
				.append("top3", this.getThird())
				.toString();
	}
	
	@Override
	public String asString() {
		return new StringJoiner(ModelCommons.DELIMITER_OUT)
				.add(JodaTimeTool.getStringFromMillis(this.getTimestamp()))
				.add(this.getFirst().asString())
				.add(this.getSecond().asString())
				.add(this.getThird().asString())
				.toString();			
	}
	
	public static final PostRank merge(final PostRank rank1, final PostRank rank2) {
		return new PostRank(); // to be implemented;
	}

}
