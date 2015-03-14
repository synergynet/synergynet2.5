/*
 * Copyright (c) 2009 University of Durham, England
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'SynergyNet' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package synergynetframework.appsystem.services.net.objectmessaging.utility.serializers;


import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;

import synergynetframework.appsystem.services.net.objectmessaging.Network;
import synergynetframework.appsystem.services.net.objectmessaging.connections.ConnectionHandler;

public class ArraySerializer extends Serializer {
	static private final ArraySerializer instance = new ArraySerializer();

	private Integer fixedDimensionCount;
	private boolean elementsAreSameType;
	private boolean elementsAreNotNull;

	public ArraySerializer () {
	}

	public ArraySerializer (Integer dimensions) {
		fixedDimensionCount = dimensions;
	}

	public ArraySerializer (Integer dimensions, boolean elementsAreNotNull, boolean elementsAreSameType) {
		this.fixedDimensionCount = dimensions;
		this.elementsAreNotNull = elementsAreNotNull;
		this.elementsAreSameType = elementsAreSameType;
	}

	public void writeObjectData (ConnectionHandler connectionHandler, ByteBuffer buffer, Object array, boolean lengthKnown)
		throws SerializationException {
		int[] dimensions = getDimensions(array);
		if (fixedDimensionCount == null) ByteSerializer.putUnsigned(buffer, dimensions.length);
		for (int i = 0, n = dimensions.length; i < n; i++)
			IntSerializer.put(buffer, dimensions[i], true);
		Serializer elementSerializer = null;
		Class<?> elementClass = getElementClass(array.getClass());
		if (elementsAreSameType || Modifier.isFinal(elementClass.getModifiers()))
			elementSerializer = Network.getRegisteredClass(elementClass).serializer;
		writeArray(connectionHandler, elementSerializer, buffer, array, 0, dimensions.length);
	}

	private void writeArray (ConnectionHandler connectionHandler, Serializer elementSerializer, ByteBuffer buffer, Object array, int dimension,
		int dimensionCount) throws SerializationException {
		int length = Array.getLength(array);
		if (dimension > 0) {
			IntSerializer.put(buffer, length, true);
		}
		boolean elementsAreArrays = dimension < dimensionCount - 1;
		for (int i = 0; i < length; i++) {
			Object element = Array.get(array, i);
			if (elementsAreArrays) {
				if (element != null) writeArray(connectionHandler, elementSerializer, buffer, element, dimension + 1, dimensionCount);
			} else if (elementSerializer != null) {
				if (elementsAreNotNull)
					elementSerializer.writeObjectData(connectionHandler, buffer, element, false);
				else
					elementSerializer.writeObject(connectionHandler, element, buffer);
			} else {
				Network.writeClassAndObject(connectionHandler, element, buffer);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T readObjectData (ConnectionHandler connectionHandler, ByteBuffer buffer, Class<T> type, boolean lengthKnown)
		throws SerializationException {
		int dimensionCount = fixedDimensionCount != null ? fixedDimensionCount : ByteSerializer.getUnsigned(buffer);
		int[] dimensions = new int[dimensionCount];
		for (int i = 0; i < dimensionCount; i++)
			dimensions[i] = IntSerializer.get(buffer, true);
		Serializer elementSerializer = null;
		Class<?> elementClass = getElementClass(type);
		if (elementsAreSameType || Modifier.isFinal(elementClass.getModifiers()))
			elementSerializer = Network.getRegisteredClass(elementClass).serializer;
		T array = (T)Array.newInstance(elementClass, dimensions);
		readArray(connectionHandler, elementSerializer, elementClass, buffer, array, 0, dimensions);
		return array;
	}

	private void readArray (ConnectionHandler connectionHandler, Serializer elementSerializer, Class<?> elementClass, ByteBuffer buffer,
		Object array, int dimension, int[] dimensions) throws SerializationException {
		boolean elementsAreArrays = dimension < dimensions.length - 1;
		int length;
		if (dimension == 0)
			length = dimensions[0];
		else
			length = IntSerializer.get(buffer, true);
		for (int i = 0; i < length; i++) {
			if (elementsAreArrays) {
				Object element = Array.get(array, i);
				if (element != null)
					readArray(connectionHandler, elementSerializer, elementClass, buffer, element, dimension + 1, dimensions);
			} else if (elementSerializer != null) {
				if (elementsAreNotNull)
					Array.set(array, i, elementSerializer.readObjectData(connectionHandler, buffer, elementClass, false));
				else
					Array.set(array, i, elementSerializer.readObject(connectionHandler, buffer, elementClass));
			} else {
				// Each element could be a different type. Look up the class with the object.
				Array.set(array, i, Network.readClassAndObject(connectionHandler, buffer));
			}
		}
	}

	static public int getDimensionCount (Class<?> arrayClass) {
		int depth = 0;
		Class<?> nextClass = arrayClass.getComponentType();
		while (nextClass != null) {
			depth++;
			nextClass = nextClass.getComponentType();
		}
		return depth;
	}

	static public int[] getDimensions (Object array) {
		int depth = 0;
		Class<?> nextClass = array.getClass().getComponentType();
		while (nextClass != null) {
			depth++;
			nextClass = nextClass.getComponentType();
		}
		int[] dimensions = new int[depth];
		dimensions[0] = Array.getLength(array);
		if (depth > 1) collectDimensions(array, 1, dimensions);
		return dimensions;
	}

	static private void collectDimensions (Object array, int dimension, int[] dimensions) {
		boolean elementsAreArrays = dimension < dimensions.length - 1;
		for (int i = 0, s = Array.getLength(array); i < s; i++) {
			Object element = Array.get(array, i);
			if (element == null) continue;
			dimensions[dimension] = Math.max(dimensions[dimension], Array.getLength(element));
			if (elementsAreArrays) collectDimensions(element, dimension + 1, dimensions);
		}
	}

	static public Class<?> getElementClass (Class<?> arrayClass) {
		Class<?> elementClass = arrayClass;
		while (elementClass.getComponentType() != null)
			elementClass = elementClass.getComponentType();
		return elementClass;
	}

	static public void put (ConnectionHandler connectionHandler, ByteBuffer buffer, Object array, Integer dimensions,
		boolean elementsAreNotNull, boolean elementsAreSameType) throws SerializationException {
		instance.fixedDimensionCount = dimensions;
		instance.elementsAreNotNull = elementsAreNotNull;
		instance.elementsAreSameType = elementsAreSameType;
		instance.writeObjectData(connectionHandler, buffer, array, false);
	}

	static public <T> T get (ConnectionHandler connectionHandler, ByteBuffer buffer, Class<T> type, Integer dimensions,
		boolean elementsAreNotNull, boolean elementsAreSameType) throws SerializationException {
		instance.fixedDimensionCount = dimensions;
		instance.elementsAreNotNull = elementsAreNotNull;
		instance.elementsAreSameType = elementsAreSameType;
		return instance.readObjectData(connectionHandler, buffer, type, false);
	}
}
