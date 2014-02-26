package net.fec.openrq;


import java.util.List;


/**
 * @author Jos&#233; Lopes &lt;jlopes&#064;lasige.di.fc.ul.pt&gt;
 * @author Ricardo Fonseca &lt;ricardof&#064;lasige.di.fc.ul.pt&gt;
 */
public interface RQEncoder {

	/**
	 * Returns an immutable list with the source blocks of this encoder.
	 * <p>
	 * Each source block is capable of producing encoding symbols independently from other source blocks. It is possible
	 * to encode symbols from different source blocks in parallel.
	 * 
	 * @return an immutable list with the source blocks of this encoder
	 */
	public List<SourceBlock> getSourceBlocks();

	/**
	 * Returns the common properties associated to this encoder.
	 * 
	 * @return the common properties associated to this encoder
	 */
	public RQCommonProperties getProperties();
}
