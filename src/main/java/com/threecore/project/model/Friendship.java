package com.threecore.project.model;

import java.util.StringJoiner;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.flink.api.java.tuple.Tuple3;
import org.joda.time.DateTime;

import com.threecore.project.model.type.Chronological;
import com.threecore.project.model.type.Stringable;
import com.threecore.project.tool.JodaTimeTool;

public class Friendship extends Tuple3<Long, Long, Long> implements Chronological, Stringable {

	private static final long serialVersionUID = 1L;
	
	public static final Friendship UNDEFINED_FRIENDSHIP = new Friendship();
	
	public Friendship(final DateTime ts, final long user_id_1, final long user_id_2) {
		super(JodaTimeTool.getMillisFromDateTime(ts), user_id_1, user_id_2);
	}

	public Friendship(final long ts, final long user_id_1, final long user_id_2){
		super(ts, user_id_1, user_id_2);
	}
	
	public Friendship() {
		super(ModelCommons.UNDEFINED_LONG, 
			  ModelCommons.UNDEFINED_LONG, 
			  ModelCommons.UNDEFINED_LONG);
	}

	@Override
	public long getTimestamp(){
		return super.f0;
	}
	
	public long getUser1Id(){
		return super.f1;
	}
	
	public long getUser2Id(){
		return super.f2;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Friendship)) {
			return false;
		}
		
		final Friendship other = (Friendship) obj;
		
		return this.getUser1Id() == other.getUser1Id() &&
				this.getUser2Id() == other.getUser2Id();
	}	
	
	@Override
	public int hashCode() {
		int result = Long.hashCode(this.getUser1Id());
		result = 31 * result + Long.hashCode(this.getUser2Id());
		return result;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("ts", JodaTimeTool.getStringFromMillis(this.getTimestamp()))
				.append("user_id_1", this.getUser1Id())
				.append("user_id_2", this.getUser2Id())
				.toString();
	}
	
	@Override
	public String asString() {
		return new StringJoiner(ModelCommons.DELIMITER_IN)
				.add(JodaTimeTool.getStringFromMillis(this.getTimestamp()))
				.add(String.valueOf((this.getUser1Id())))
				.add(String.valueOf(this.getUser2Id()))
				.toString();
	}
	
	public static Friendship fromString(final String line) {
		final String array[] = line.split("[" + ModelCommons.DELIMITER_IN + "]", -1);
		
		final long ts = JodaTimeTool.getMillisFromString(array[0]);
		final long user_id_1 = Long.parseLong(array[1]);
		final long user_id_2 = Long.parseLong(array[2]);
		
		return new Friendship(ts, user_id_1, user_id_2);
	}
	
}