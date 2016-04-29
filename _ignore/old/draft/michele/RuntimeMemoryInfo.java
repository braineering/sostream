package com.threecore.project.draft.michele;

import java.io.Serializable;

/**
 * Returns information about runtime memory status
 */
@Deprecated
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
    
    /*Example of use
     
      private RuntimeMemoryInfo memory = new RuntimeMemoryInfo();
      
      System.out.println("\n\n");
	  System.out.println("Max  memory : " + Long.toString(memory.maxMemory()));
	  System.out.println("Free  memory : " + Long.toString(memory.freeMemory()));
	  System.out.println("Allocated memory : " + Long.toString(memory.totalMemory()));
	  System.out.println("\n\n");
	  
     */
}