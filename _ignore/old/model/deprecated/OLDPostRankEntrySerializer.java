package com.threecore.project.model.deprecated;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

@Deprecated
public class OLDPostRankEntrySerializer extends Serializer<OLDPostRankEntry> {

	@Override
	public void write(Kryo kryo, Output output, OLDPostRankEntry object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public OLDPostRankEntry read(Kryo kryo, Input input, Class<OLDPostRankEntry> type) {
		// TODO Auto-generated method stub
		return null;
	}

}
