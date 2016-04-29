package com.threecore.project.control;

import java.io.Serializable;

/**
 * Returns information about runtime memory status
 */

public class RuntimeMemoryInfo implements Serializable
{
	private static final long serialVersionUID = 7164627809168103391L;

    public long totalMemory() {
        return Runtime.getRuntime().totalMemory();
    }
    
    public long freeMemory() {
        return Runtime.getRuntime().freeMemory();
    }
    
    public long maxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    public long usedMememory() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }
    
}