package com.threecore.project.tool.rank;

import com.threecore.project.model.interfaces.Copyable;
import com.threecore.project.model.interfaces.IScorable;

public class MemoryScoredRanking<T extends IScorable & Comparable<T> & Copyable<T>>  
								extends SimpleScoredRanking<T> implements IRank<T> {

	private static final long serialVersionUID = 5625434676223154029L;

}
