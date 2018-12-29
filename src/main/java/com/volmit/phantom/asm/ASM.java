package com.volmit.phantom.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

public class ASM
{
	public static ClassNode readNode(byte[] bytes)
	{
		ClassNode node = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(node, ClassReader.EXPAND_FRAMES);

		return node;
	}

	public static byte[] writeNode(ClassNode node)
	{
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		node.accept(cw);

		return cw.toByteArray();
	}
}
