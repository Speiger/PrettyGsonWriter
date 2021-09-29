package speiger.src.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import com.google.gson.stream.JsonWriter;

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
	
	public PrettyGsonWriter addSinlgeLines(String... names)
	{
		singleLineObjects.addAll(Arrays.asList(names));
		return this;
	}
	
	public PrettyGsonWriter setTabs(String key)
	{
		setIndent(key);
		if(key.length() == 0) indent = null;
		else indent = key;
		return this;
	}
	
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
	
	private void pop()
	{
		if(indent == null) return;
		byte value = blocked.pop();
		if((value & 1) != 0) usedArrays[arrayPointer--] = 0;
		if((value & 2) != 0) singleNames[namePointer--] = null;
	}
	
	@Override
	public JsonWriter name(String name) throws IOException
	{
		if(singleLineObjects.contains(name)) nextArray = name;
		return super.name(name);
	}
	
	@Override
	public JsonWriter beginArray() throws IOException
	{
		super.beginArray();
		push(true, false);
		singleNames[namePointer] = nextArray;
		nextArray = null;
		return this;
	}
	
	@Override
	public JsonWriter beginObject() throws IOException
	{
		boolean shouldRework = shouldBlockObject();
		if(shouldRework) push(false, true);
		else push(shouldRework, true);
		super.beginObject();
		if(shouldRework) push(shouldRework, true);
		return this;
	}
	
	@Override
	public JsonWriter endArray() throws IOException
	{
		boolean ignore = usedArrays[arrayPointer] > 0;
		if(ignore) pop();
		super.endArray();
		if(!ignore) pop();
		return this;
	}
	
	@Override
	public JsonWriter endObject() throws IOException
	{
		super.endObject();
		if(shouldBlockObject()) pop();
		pop();
		return this;
	}
	
	public boolean shouldBlockObject()
	{
		return singleNames[namePointer] != null;
	}
	
	public boolean isBlocked()
	{
		return (blocked.peek() & 1) != 0;
	}
	
	public static class Overrider extends BufferedWriter
	{
		PrettyGsonWriter owner;
		public Overrider(Writer out)
		{
			super(out);
		}
		
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
			if(owner.indent != null && owner.isBlocked() && c == '\n') return;
			super.write(c);
		}
		
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
		
		@Override
		public void write(String str) throws IOException
		{
			if(owner.indent != null && owner.isBlocked() && str.equals(owner.indent)) return;
			super.write(str);
		}
	}
}
