package com.threecore.project.model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.flink.api.java.tuple.Tuple13;

import com.threecore.project.tool.TimeFormat;

public class PostRank extends Tuple13<LocalDateTime, Long, Long, Long, Long,
													 Long, Long, Long, Long,
													 Long, Long, Long, Long> {

	private static final long serialVersionUID = -2616425583852108209L;
	
	public static final String UNDEF_STRING = "-";
	public static final long UNDEF_LONG = -1;
	
	public PostRank() {
		this(LocalDateTime.MIN);
	}
	
	public PostRank(final LocalDateTime ts) {		
		this(ts, UNDEF_LONG, UNDEF_LONG, UNDEF_LONG, UNDEF_LONG);
	}
	
	public PostRank(final LocalDateTime ts, final long top1_post_id, final long top1_user_id, final long top1_score, final long top1_commenters) {		
		this(ts, top1_post_id, top1_user_id, top1_score, top1_commenters,
				UNDEF_LONG, UNDEF_LONG, UNDEF_LONG, UNDEF_LONG);
	}
	
	public PostRank(final LocalDateTime ts, final long top1_post_id, final long top1_user_id, final long top1_score, final long top1_commenters,
			final long top2_post_id, final long top2_user_id, final long top2_score, final long top2_commenters) {		
		this(ts, top1_post_id, top1_user_id, top1_score, top1_commenters,
				top2_post_id, top2_user_id, top2_score, top2_commenters,
				UNDEF_LONG, UNDEF_LONG, UNDEF_LONG, UNDEF_LONG);
	}
	
	public PostRank(final LocalDateTime ts, final long top1_post_id, final long top1_user_id, final long top1_score, final long top1_commenters,
			final long top2_post_id, final long top2_user_id, final long top2_score, final long top2_commenters,
			final long top3_post_id, final long top3_user_id, final long top3_score, final long top3_commenters) {
		
		super(ts, top1_post_id, top1_user_id, top1_score, top1_commenters,
				top2_post_id, top2_user_id, top2_score, top2_commenters,
				top3_post_id, top3_user_id, top3_score, top3_commenters);
		
		assert (top1_post_id > 0 || top1_post_id == UNDEF_LONG) : "top1_post_id must be >= 0, or == " + UNDEF_LONG + ".";
		assert (top1_user_id > 0 || top1_user_id == UNDEF_LONG) : "top1_user_id must be >= 0, or == " + UNDEF_LONG + ".";
		assert (top1_score >= 0  || top1_score == UNDEF_LONG) : "top1_score must be >= 0, or == " + UNDEF_LONG + ".";
		assert (top1_commenters >= 0 || top1_commenters == UNDEF_LONG) : "top1_commenters must be >= 0, or == " + UNDEF_LONG + ".";
		
		assert (top2_post_id > 0 || top2_post_id == UNDEF_LONG) : "top2_post_id must be >= 0, or == " + UNDEF_LONG + ".";
		assert (top2_user_id > 0 || top2_user_id == UNDEF_LONG) : "top2_user_id must be >= 0, or == " + UNDEF_LONG + ".";
		assert (top2_score >= 0  || top2_score == UNDEF_LONG) : "top2_score must be >= 0, or == " + UNDEF_LONG + ".";
		assert (top2_commenters >= 0 || top2_commenters == UNDEF_LONG) : "top2_commenters must be >= 0, or == " + UNDEF_LONG + ".";
		
		assert (top3_post_id > 0 || top3_post_id == UNDEF_LONG) : "top3_post_id must be >= 0, or == " + UNDEF_LONG + ".";
		assert (top3_user_id > 0 || top3_user_id == UNDEF_LONG) : "top3_user_id must be >= 0, or == " + UNDEF_LONG + ".";
		assert (top3_score >= 0  || top3_score == UNDEF_LONG) : "top3_score must be >= 0, or == " + UNDEF_LONG + ".";
		assert (top3_commenters >= 0 || top3_commenters == UNDEF_LONG) : "top3_commenters must be >= 0, or == " + UNDEF_LONG + ".";
	}
	
	public LocalDateTime getTimestamp(){
		return super.f0;
	}
	
	public long getTimestampMillis() {
		return this.getTimestamp().toInstant(ZoneOffset.UTC).toEpochMilli();
	}
	
	public long getTop1PostId() {
		return super.f1;
	}
	
	public long getTop1UserId() {
		return super.f2;
	}	
	
	public long getTop1Score() {
		return super.f3;
	}
	
	public long getTop1Commenters() {
		return super.f4;
	}
	
	public long getTop2PostId() {
		return super.f5;
	}
	
	public long getTop2UserId() {
		return super.f6;
	}	
	
	public long getTop2Score() {
		return super.f7;
	}
	
	public long getTop2Commenters() {
		return super.f8;
	}
	
	public long getTop3PostId() {
		return super.f9;
	}
	
	public long getTop3UserId() {
		return super.f10;
	}	
	
	public long getTop3Score() {
		return super.f11;
	}
	
	public long getTop3Commenters() {
		return super.f12;
	}
	
	public PostRank copy(final PostRank rank) {
		this.f0 = rank.getTimestamp();
		this.f1 = rank.getTop1PostId();
		this.f2 = rank.getTop1UserId();
		this.f3 = rank.getTop1Score();
		this.f4 = rank.getTop1Commenters();
		this.f5 = rank.getTop2PostId();
		this.f6 = rank.getTop2UserId();
		this.f7 = rank.getTop2Score();
		this.f8 = rank.getTop2Commenters();
		this.f9 = rank.getTop3PostId();
		this.f10 = rank.getTop3UserId();
		this.f11 = rank.getTop3Score();
		this.f12 = rank.getTop3Commenters();
		return this;
	}
	
	public PostRank copy() {
		return this.touch(this.getTimestamp());
	}
	
	public PostRank touch(final LocalDateTime ts) {
		return new PostRank(ts, 
				this.getTop1PostId(), this.getTop1UserId(), this.getTop1Score(), this.getTop1Commenters(),
				this.getTop2PostId(), this.getTop2UserId(), this.getTop2Score(), this.getTop2Commenters(),
				this.getTop3PostId(), this.getTop3UserId(), this.getTop3Score(), this.getTop3Commenters());
	}
	
	public static PostRank merge(final PostRank rank1, final PostRank rank2) {
		//LocalDateTime ts = (rank1.getTimestamp().compareTo(rank2.getTimestamp()) >= 0) ? rank1.getTimestamp() : rank2.getTimestamp();		
		
		PostRank rank = new PostRank();
		
		if (!rank1.getTimestamp().equals(rank2.getTimestamp()))
			return null;
		
		if (rank1.getTop3Score() > rank2.getTop1Score()) {
			return rank1.copy();
		} else if (rank2.getTop3Score() > rank1.getTop1Score()) {
			return rank2.copy();
		}
		
		return rank;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PostRank))
			return false;
		
		final PostRank other = (PostRank) obj;
		
		return this.getTop1PostId() == other.getTop1PostId() && 
				this.getTop1Score() == other.getTop1Score() && 
				this.getTop1Commenters() == other.getTop1Commenters() &&
				this.getTop2PostId() == other.getTop2PostId() && 
				this.getTop2Score() == other.getTop2Score() && 
				this.getTop2Commenters() == other.getTop2Commenters() &&
				this.getTop3PostId() == other.getTop3PostId() && 
				this.getTop3Score() == other.getTop3Score() && 
				this.getTop3Commenters() == other.getTop3Commenters();
	}	
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getTop1PostId(), this.getTop1Score(), this.getTop1Commenters(),
							this.getTop2PostId(), this.getTop2Score(), this.getTop2Commenters(),
							this.getTop3PostId(), this.getTop3Score(), this.getTop3Commenters());
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("timestamp", this.getTimestamp())
				.append("top1_post_id", this.getTop1PostId())
				.append("top1_user_id", this.getTop1UserId())
				.append("top1_score", this.getTop1Score())
				.append("top1_commenters", this.getTop1Commenters())
				.append("top2_post_id", this.getTop2PostId())
				.append("top2_user_id", this.getTop2UserId())
				.append("top2_score", this.getTop2Score())
				.append("top2_commenters", this.getTop2Commenters())
				.append("top3_post_id", this.getTop3PostId())
				.append("top3_user_id", this.getTop3UserId())
				.append("top3_score", this.getTop3Score())
				.append("top3_commenters", this.getTop3Commenters())
				.toString();
	}
	
	public static PostRank fromString(final String line) {
		final String tuple[] = line.split("[,]", -1);
		return PostRank.fromArray(tuple);
	}
	
	public static PostRank fromArray(final String tuple[]) {
		if (tuple.length != 13)
			return null;
		
		final LocalDateTime ts = TimeFormat.parseWithoutOffset(tuple[0]);
		final long top1_post_id = (tuple[1].equals(UNDEF_STRING)) ? UNDEF_LONG : Long.parseLong(tuple[1]);
		final long top1_user_id = (tuple[2].equals(UNDEF_STRING)) ? UNDEF_LONG : Long.parseLong(tuple[2]);
		final long top1_score = (tuple[3].equals(UNDEF_STRING)) ? UNDEF_LONG : Long.parseLong(tuple[3]);
		final long top1_commenters = (tuple[4].equals(UNDEF_STRING)) ? UNDEF_LONG : Long.parseLong(tuple[4]);
		final long top2_post_id = (tuple[5].equals(UNDEF_STRING)) ? UNDEF_LONG : Long.parseLong(tuple[5]);
		final long top2_user_id = (tuple[6].equals(UNDEF_STRING)) ? UNDEF_LONG : Long.parseLong(tuple[6]);
		final long top2_score = (tuple[7].equals(UNDEF_STRING)) ? UNDEF_LONG : Long.parseLong(tuple[7]);
		final long top2_commenters = (tuple[8].equals(UNDEF_STRING)) ? UNDEF_LONG : Long.parseLong(tuple[8]);
		final long top3_post_id = (tuple[9].equals(UNDEF_STRING)) ? UNDEF_LONG : Long.parseLong(tuple[9]);
		final long top3_user_id = (tuple[10].equals(UNDEF_STRING)) ? UNDEF_LONG : Long.parseLong(tuple[10]);
		final long top3_score = (tuple[11].equals(UNDEF_STRING)) ? UNDEF_LONG : Long.parseLong(tuple[11]);
		final long top3_commenters = (tuple[12].equals(UNDEF_STRING)) ? UNDEF_LONG : Long.parseLong(tuple[12]);
		
		return new PostRank(ts, top1_post_id, top1_user_id, top1_score, top1_commenters,
				top2_post_id, top2_user_id, top2_score, top2_commenters,
				top3_post_id, top3_user_id, top3_score, top3_commenters);
	}
	
	public static PostRank fromTuple(final Tuple13<LocalDateTime, Long, Long, Long, Long,
																  Long, Long, Long, Long,
																  Long, Long, Long, Long> tuple) {
		final LocalDateTime ts = tuple.f0;
		final long top1_post_id = tuple.f1;
		final long top1_user_id = tuple.f2;
		final long top1_score = tuple.f3;
		final long top1_commenters = tuple.f4;
		final long top2_post_id = tuple.f5;
		final long top2_user_id = tuple.f6;
		final long top2_score = tuple.f7;
		final long top2_commenters = tuple.f8;
		final long top3_post_id = tuple.f9;
		final long top3_user_id = tuple.f10;
		final long top3_score = tuple.f11;
		final long top3_commenters = tuple.f12;
		
		return new PostRank(ts, top1_post_id, top1_user_id, top1_score, top1_commenters,
				top2_post_id, top2_user_id, top2_score, top2_commenters,
				top3_post_id, top3_user_id, top3_score, top3_commenters);
	}
	
	public String asString() {
		return TimeFormat.stringWithoutOffset(this.getTimestamp()) + "," + 
				((this.getTop1PostId() == UNDEF_LONG) ? UNDEF_STRING : this.getTop1PostId()) + "," +
				((this.getTop1UserId() == UNDEF_LONG) ? UNDEF_STRING : this.getTop1UserId()) + "," +
				((this.getTop1Score() == UNDEF_LONG) ? UNDEF_STRING : this.getTop1Score()) + "," +
				((this.getTop1Commenters() == UNDEF_LONG) ? UNDEF_STRING : this.getTop1Commenters()) + "," +
				((this.getTop2PostId() == UNDEF_LONG) ? UNDEF_STRING : this.getTop2PostId()) + "," +
				((this.getTop2UserId() == UNDEF_LONG) ? UNDEF_STRING : this.getTop2UserId()) + "," +
				((this.getTop2Score() == UNDEF_LONG) ? UNDEF_STRING : this.getTop2Score()) + "," +
				((this.getTop2Commenters() == UNDEF_LONG) ? UNDEF_STRING : this.getTop2Commenters()) + "," +
				((this.getTop3PostId() == UNDEF_LONG) ? UNDEF_STRING : this.getTop3PostId()) + "," +
				((this.getTop3UserId() == UNDEF_LONG) ? UNDEF_STRING : this.getTop3UserId()) + "," +
				((this.getTop3Score() == UNDEF_LONG) ? UNDEF_STRING : this.getTop3Score()) + "," +
				((this.getTop3Commenters() == UNDEF_LONG) ? UNDEF_STRING : this.getTop3Commenters());
				
	}
	
	public String[] asArray() {
		return new String[]{TimeFormat.stringWithoutOffset(this.getTimestamp()),
				((this.getTop1PostId() == UNDEF_LONG) ? UNDEF_STRING : String.valueOf(this.getTop1PostId())),
				((this.getTop1UserId() == UNDEF_LONG) ? UNDEF_STRING : String.valueOf(this.getTop1UserId())),
				((this.getTop1Score() == UNDEF_LONG) ? UNDEF_STRING : String.valueOf(this.getTop1Score())),
				((this.getTop1Commenters() == UNDEF_LONG) ? UNDEF_STRING : String.valueOf(this.getTop1Commenters())),
				((this.getTop2PostId() == UNDEF_LONG) ? UNDEF_STRING : String.valueOf(this.getTop2PostId())),
				((this.getTop2UserId() == UNDEF_LONG) ? UNDEF_STRING : String.valueOf(this.getTop2UserId())),
				((this.getTop2Score() == UNDEF_LONG) ? UNDEF_STRING : String.valueOf(this.getTop2Score())),
				((this.getTop2Commenters() == UNDEF_LONG) ? UNDEF_STRING : String.valueOf(this.getTop2Commenters())),
				((this.getTop3PostId() == UNDEF_LONG) ? UNDEF_STRING : String.valueOf(this.getTop3PostId())),
				((this.getTop3UserId() == UNDEF_LONG) ? UNDEF_STRING : String.valueOf(this.getTop3UserId())),
				((this.getTop3Score() == UNDEF_LONG) ? UNDEF_STRING : String.valueOf(this.getTop3Score())),
				((this.getTop3Commenters() == UNDEF_LONG) ? UNDEF_STRING : String.valueOf(this.getTop3Commenters()))};
	}
	
	

}
