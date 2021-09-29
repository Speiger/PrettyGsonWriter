package speiger.src.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import com.google.gson.stream.JsonWriter;

/**
 * @author Speiger
 * 
 * Custom JsonWriter that just prettifies everything a lot more.
 */
public class PrettyGsonWriter extends JsonWriter
{
	String indent;
	
	Stack<Byte> blocked = new Stack<>();
	int[] usedArrays = new int[32];
	int arrayPointer = 0;
	
	String[] singleNames = new String[32];
	int namePointer = 0;
	String nextArray = null;
	Set<String> singleLineObjects = new HashSet<>();
	
	
	public PrettyGsonWriter(Writer out)
	{
		this(new Overrider(out));
	}
	
	public PrettyGsonWriter(Writer out, String indent)
	{
		this(new Overrider(out));
		setTabs(indent);
	}
	
	private PrettyGsonWriter(Overrider rider)
	{
		super(rider);
		rider.owner = this;
		blocked.push((byte) 0);
	}
	
	/**
	 * Function that defines which Arrays should compress its Objects.
	 * @param names that should be compressed
	 * @return self
	 */
	public PrettyGsonWriter addSinlgeLines(String... names)
	{
		singleLineObjects.addAll(Arrays.asList(names));
		return this;
	}
	
	/**
	 * its setIndent because the function is final i have to change its name
	 * @param key the indent that should be used
	 * @return self
	 */
	public PrettyGsonWriter setTabs(String key)
	{
		setIndent(key);
		if(key.length() == 0) indent = null;
		else indent = key;
		return this;
	}
	
	/**
	 * Helper function that saves the currently written state of the JsonWriter
	 * Since I don't want to override everything completely this is the solution i came up with.
	 * @param value this defines if the Compression should kick in or not
	 * @param object if the current element is a Object or Arrays
	 */
	private void push(boolean value, boolean object)
	{
		if(indent == null) return;
		if(value)
		{
			if(++arrayPointer >= usedArrays.length) usedArrays = Arrays.copyOf(usedArrays, usedArrays.length * 2);
			if(!object && ++namePointer >= singleNames.length) singleNames = Arrays.copyOf(singleNames, singleNames.length * 2);
		}
		else if((blocked.peek() & 1) != 0) usedArrays[arrayPointer]++;
		blocked.push((byte)((value ? 1 : 0) | (value && !object ? 2 : 0)));
	}
	
	/**
	 * Will pop the state buffer and reduce/reset the pointers
	 */
	private void pop()
	{
		if(indent == null) return;
		byte value = blocked.pop();
		if((value & 1) != 0) usedArrays[arrayPointer--] = 0;
		if((value & 2) != 0) singleNames[namePointer--] = null;
	}
	
	/**
	 * Saves the Potential next JsonArray Name if it matches the desired SingleLine Objects Arrays.
	 */
	@Override
	public JsonWriter name(String name) throws IOException
	{
		if(singleLineObjects.contains(name)) nextArray = name;
		return super.name(name);
	}
	
	/**
	 * Pushes the Compression State and saves the Last Json Name that was written.
	 */
	@Override
	public JsonWriter beginArray() throws IOException
	{
		super.beginArray();
		push(true, false);
		singleNames[namePointer] = nextArray;
		nextArray = null;
		return this;
	}
	
	/**
	 * This does a lot of rule checks.
	 * First it checks if the Object should be compressed
	 * Then push the Object State through.
	 * Then Let the JsonWriter do its thing.
	 * Afterwards if the Object is compressed then push the Compression Flag through.
	 */
	@Override
	public JsonWriter beginObject() throws IOException
	{
		boolean shouldRework = shouldBlockObject();
		push(false, true);
		super.beginObject();
		if(shouldRework) push(shouldRework, true);
		return this;
	}
	
	/***
	 * Small fix so the Array does not end wrongly.
	 * Tests if it was a Value or Object Array and then popes the state as it is needed
	 */
	@Override
	public JsonWriter endArray() throws IOException
	{
		boolean ignore = usedArrays[arrayPointer] > 0;
		if(ignore) pop();
		super.endArray();
		if(!ignore) pop();
		return this;
	}
	
	/**
	 * Lets the Object finish its thing and then pops either once or twice the state depending on what it needs.
	 */
	@Override
	public JsonWriter endObject() throws IOException
	{
		super.endObject();
		if(shouldBlockObject()) pop();
		pop();
		return this;
	}
	
	/**
	 *  @return true if a valid SingleLineArray was found and is currently written to
	 */
	public boolean shouldBlockObject()
	{
		return singleNames[namePointer] != null;
	}
	
	/**
	 * @return true if single line compression is right now active
	 */
	public boolean isBlocked()
	{
		return (blocked.peek() & 1) != 0;
	}
	
	/**
	 * @author Speiger
	 * 
	 * Hacked BufferedWriter that alters the Outcome of what the JsonWriter tries to push.
	 *
	 */
	public static class Overrider extends BufferedWriter
	{
		PrettyGsonWriter owner;
		
		public Overrider(Writer out)
		{
			super(out);
		}
		
		/**
		 * Removes array new Lines and adds extra empty spaces to the Arrays after the value was written.
		 */
		@Override
		public void write(int c) throws IOException
		{
			if(owner.indent != null && owner.isBlocked())
			{
				if(c == '\n') return;
				else if(c == ',' && owner.shouldBlockObject())
				{
					super.write(", ");
					return;
				}
			}
			super.write(c);
		}
		
		/**
		 * adds extra empty spaces to the Arrays after the value was written.
		 * Because JsonWriter uses 2 different functions to write its ","
		 */
		@Override
		public Writer append(char c) throws IOException
		{
			if(owner.indent != null && c == ',' && owner.isBlocked() && (owner.usedArrays[owner.arrayPointer] <= 0))
			{
				super.write(", ");
				return this;
			}
			return super.append(c);
		}
		
		/**
		 * Blocks the indent of the Arrays if the Compression is right now active.
		 * So you don have tabs between each value
		 */
		@Override
		public void write(String str) throws IOException
		{
			if(owner.indent != null && owner.isBlocked() && str.equals(owner.indent)) return;
			super.write(str);
		}
	}
}
