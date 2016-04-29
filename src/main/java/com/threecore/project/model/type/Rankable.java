package com.threecore.project.model.type;

import java.io.Serializable;

public interface Rankable<T> extends Chronological, Scorable, Comparable<T>, Copyable<T>, Serializable {

}
