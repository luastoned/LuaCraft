package com.luacraft.meta;

import java.io.IOException;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

import com.google.common.base.Charsets;
import com.luacraft.LuaCraftState;
import com.luacraft.classes.Angle;
import com.luacraft.classes.Color;
import com.luacraft.classes.Vector;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaRuntimeException;
import com.naef.jnlua.LuaState;

public class LuaByteBuf {

	public static JavaFunction __tostring = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			l.pushString(String.format("ByteBuf: 0x%08x", l.toPointer(1)));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function WriteVector
	 * @info Writes a vector to the buffer
	 * @arguments [[Vector]]:vector
	 * @return nil
	 */

	public static JavaFunction WriteVector = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			Vector vector = (Vector) l.checkUserdata(2, Vector.class, "Vector");
			self.writeDouble(vector.x);
			self.writeDouble(vector.y);
			self.writeDouble(vector.z);
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function ReadVector
	 * @info Reads a vector from the buffer
	 * @arguments nil
	 * @return [[Vector]]:vector
	 */

	public static JavaFunction ReadVector = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			new Vector(self.readDouble(), self.readDouble(), self.readDouble()).push(l);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function WriteAngle
	 * @info Writes an angle to the buffer
	 * @arguments [[Angle]]:angle
	 * @return nil
	 */

	public static JavaFunction WriteAngle = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			Angle angle = (Angle) l.checkUserdata(2, Angle.class, "Angle");
			self.writeDouble(angle.p);
			self.writeDouble(angle.y);
			self.writeDouble(angle.r);
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function ReadAngle
	 * @info Reads an angle from the buffer
	 * @arguments nil
	 * @return [[Angle]]:angle
	 */

	public static JavaFunction ReadAngle = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			new Angle(self.readDouble(), self.readDouble(), self.readDouble()).push(l);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function WriteColor
	 * @info Writes a color to the buffer
	 * @arguments [[Color]]:angle
	 * @return nil
	 */

	public static JavaFunction WriteColor = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			Color color = (Color) l.checkUserdata(2, Color.class, "Color");
			self.writeByte(color.r);
			self.writeByte(color.g);
			self.writeByte(color.b);
			self.writeByte(color.a);
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function ReadColor
	 * @info Reads a color from the buffer
	 * @arguments nil
	 * @return [[Color]]:angle
	 */

	public static JavaFunction ReadColor = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			new Color(self.readUnsignedByte(), self.readUnsignedByte(), self.readUnsignedByte(),
					self.readUnsignedByte()).push(l);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function WriteBool
	 * @info Writes a boolean to the buffer
	 * @arguments [[Boolean]]:bool
	 * @return nil
	 */

	public static JavaFunction WriteBool = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			self.writeBoolean(l.checkBoolean(2));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function ReadBool
	 * @info Reads a boolean from the buffer
	 * @arguments nil
	 * @return [[Boolean]]:bool
	 */

	public static JavaFunction ReadBool = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			l.pushBoolean(self.readBoolean());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function WriteByte
	 * @info Writes a byte to the buffer
	 * @arguments [[Number]]:byte
	 * @return nil
	 */

	public static JavaFunction WriteByte = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			self.writeByte(l.checkInteger(2));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function ReadByte
	 * @info Reads a byte from the buffer
	 * @arguments nil
	 * @return [[Number]]:byte
	 */

	public static JavaFunction ReadByte = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			l.pushInteger(self.readByte());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function ReadUByte
	 * @info Reads an unsigned byte from the buffer
	 * @arguments nil
	 * @return [[Number]]:byte
	 */

	public static JavaFunction ReadUByte = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			l.pushInteger(self.readUnsignedByte());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function WriteChar
	 * @info Writes a 2-byte UTF-16 number to the buffer
	 * @arguments [[Number]]:char
	 * @return nil
	 */

	public static JavaFunction WriteChar = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			self.writeChar(l.checkInteger(2));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function ReadChar
	 * @info Reads a 2-byte UTF-16 number from the buffer
	 * @arguments nil
	 * @return [[Number]]:char
	 */

	public static JavaFunction ReadChar = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			l.pushInteger(self.readChar());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function WriteDouble
	 * @info Writes a 64-bit floating point number to the buffer
	 * @arguments [[Number]]:double
	 * @return nil
	 */

	public static JavaFunction WriteDouble = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			self.writeDouble(l.checkNumber(2));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function ReadDouble
	 * @info Reads a 64-bit floating point number from the buffer
	 * @arguments nil
	 * @return [[Number]]:double
	 */

	public static JavaFunction ReadDouble = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			l.pushNumber(self.readDouble());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function WriteFloat
	 * @info Writes a 32-bit floating point number to the buffer
	 * @arguments [[Number]]:float
	 * @return nil
	 */

	public static JavaFunction WriteFloat = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			self.writeFloat((float) l.checkNumber(2));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function ReadFloat
	 * @info Reads a 32-bit floating point number from the buffer
	 * @arguments nil
	 * @return [[Number]]:float
	 */

	public static JavaFunction ReadFloat = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			l.pushNumber(self.readFloat());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function WriteInt
	 * @info Writes a 32-bit number to the buffer
	 * @arguments [[Number]]:integer
	 * @return nil
	 */

	public static JavaFunction WriteInt = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			self.writeInt(l.checkInteger(2));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function ReadInt
	 * @info Reads a 32-bit number from the buffer
	 * @arguments nil
	 * @return [[Number]]:integer
	 */

	public static JavaFunction ReadInt = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			l.pushNumber(self.readFloat());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function ReadUInt
	 * @info Reads a 32-bit unsigned number from the buffer
	 * @arguments nil
	 * @return [[Number]]:integer
	 */

	public static JavaFunction ReadUInt = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			l.pushNumber(self.readUnsignedInt());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function WriteLong
	 * @info Writes a 64-bit number to the buffer
	 * @arguments [[Number]]:long
	 * @return nil
	 */

	public static JavaFunction WriteLong = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			self.writeLong(l.checkInteger(2));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function ReadLong
	 * @info Reads a 64-bit number from the buffer
	 * @arguments nil
	 * @return [[Number]]:long
	 */

	public static JavaFunction ReadLong = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			l.pushNumber(self.readLong());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function WriteMedium
	 * @info Writes a 24-bit number to the buffer
	 * @arguments [[Number]]:medium
	 * @return nil
	 */

	public static JavaFunction WriteMedium = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			self.writeMedium(l.checkInteger(2));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function ReadMedium
	 * @info Reads a 24-bit number from the buffer
	 * @arguments nil
	 * @return [[Number]]:medium
	 */

	public static JavaFunction ReadMedium = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			l.pushNumber(self.readMedium());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function ReadUMedium
	 * @info Reads a 24-bit unsigned number from the buffer
	 * @arguments nil
	 * @return [[Number]]:medium
	 */

	public static JavaFunction ReadUMedium = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			l.pushNumber(self.readUnsignedMedium());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function WriteShort
	 * @info Writes a 16-bit number to the buffer
	 * @arguments [[Number]]:medium
	 * @return nil
	 */

	public static JavaFunction WriteShort = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			self.writeShort(l.checkInteger(2));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function ReadShort
	 * @info Reads a 16-bit number from the buffer
	 * @arguments nil
	 * @return [[Number]]:short
	 */

	public static JavaFunction ReadShort = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			l.pushNumber(self.readShort());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function ReadShort
	 * @info Reads a 16-bit unsigned number from the buffer
	 * @arguments nil
	 * @return [[Number]]:short
	 */

	public static JavaFunction ReadUShort = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			l.pushNumber(self.readUnsignedShort());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function WriteMedium
	 * @info Writes a NBTTag to the buffer
	 * @arguments [[NBTTag]]:tag
	 * @return nil
	 */

	public static JavaFunction WriteNBTTag = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			self.writeNBTTagCompoundToBuffer((NBTTagCompound) l.checkUserdata(2, NBTTagCompound.class, "NBTTag"));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function ReadNBTTag
	 * @info Reads a NBTTag from the buffer
	 * @arguments nil
	 * @return [[NBTTag]]:tag
	 */

	public static JavaFunction ReadNBTTag = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			try {
				l.pushUserdataWithMeta(self.readNBTTagCompoundFromBuffer(), "NBTTag");
			} catch (IOException e) {
				throw new LuaRuntimeException(e);
			}
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function WriteItemStack
	 * @info Writes an ItemStack to the buffer
	 * @arguments [[ItemStack]]:stack
	 * @return nil
	 */

	public static JavaFunction WriteItemStack = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			self.writeItemStackToBuffer((ItemStack) l.checkUserdata(2, ItemStack.class, "ItemStack"));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function ReadItemStack
	 * @info Reads a ItemStack from the buffer
	 * @arguments nil
	 * @return [[ItemStack]]:stack
	 */

	public static JavaFunction ReadItemStack = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			try {
				l.pushUserdataWithMeta(self.readItemStackFromBuffer(), "ItemStack");
			} catch (IOException e) {
				throw new LuaRuntimeException(e);
			}
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function WriteString
	 * @info Writes a string to the buffer
	 * @arguments [[String]]:string
	 * @return nil
	 */

	public static JavaFunction WriteString = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			self.writeString(l.checkString(2));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function ReadString
	 * @info Reads a string from the buffer
	 * @arguments [ [[Number]]:maxlength ]
	 * @return [[String]]:string
	 */

	public static JavaFunction ReadString = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			l.pushString(self.readStringFromBuffer((int) l.checkNumber(2, 32767)));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function WriteString
	 * @info Writes raw data to the buffer via a string
	 * @arguments [[String]]:data
	 * @return nil
	 */

	public static JavaFunction WriteData = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			self.writeBytes(l.checkString(2).getBytes());
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function ReadData
	 * @info Reads raw data from the buffer as a string
	 * @arguments [ [[Number]]:length ]
	 * @return [[String]]:string
	 */

	public static JavaFunction ReadData = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			byte[] data = new byte[l.checkInteger(2, self.readableBytes())];
			self.readBytes(data);
			l.pushByteArray(data);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function Seek
	 * @info Seeks the index of the buffer to a position
	 * @arguments [[Number]]:position
	 * @return nil
	 */

	public static JavaFunction Seek = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			self.readerIndex(l.checkInteger(2));
			self.writerIndex(l.checkInteger(2));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function Slice
	 * @info Slices the buffer at the current index into a new buffer
	 * @arguments [ [[Number]]:start, [[Number]]:end ]
	 * @return [[ByteBuf]]:buffer
	 */

	public static JavaFunction Slice = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			PacketBuffer buffer = new PacketBuffer(
					self.slice(l.checkInteger(2, self.readerIndex()), l.checkInteger(3, self.readableBytes())));
			l.pushUserdataWithMeta(buffer, "ByteBuf");
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function Copy
	 * @info Copies the buffer into a new buffer
	 * @arguments [ [[Number]]:start, [[Number]]:end ]
	 * @return [[ByteBuf]]:buffer
	 */

	public static JavaFunction Copy = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			PacketBuffer buffer = new PacketBuffer(
					self.copy(l.checkInteger(2, self.readerIndex()), l.checkInteger(3, self.readableBytes())));
			l.pushUserdataWithMeta(buffer, "ByteBuf");
			return 1;
		}
	};

	public static void Init(final LuaCraftState l) {
		l.newMetatable("ByteBuf");
		{
			l.pushValue(-1);
			l.setField(-2, "__index");

			l.pushJavaFunction(__tostring);
			l.setField(-2, "__tostring");

			l.pushJavaFunction(WriteVector);
			l.setField(-2, "WriteVector");
			l.pushJavaFunction(ReadVector);
			l.setField(-2, "ReadVector");
			l.pushJavaFunction(WriteAngle);
			l.setField(-2, "WriteAngle");
			l.pushJavaFunction(ReadAngle);
			l.setField(-2, "ReadAngle");
			l.pushJavaFunction(WriteColor);
			l.setField(-2, "WriteColor");
			l.pushJavaFunction(ReadColor);
			l.setField(-2, "ReadColor");
			l.pushJavaFunction(WriteBool);
			l.setField(-2, "WriteBool");
			l.pushJavaFunction(ReadBool);
			l.setField(-2, "ReadBool");
			l.pushJavaFunction(WriteByte);
			l.setField(-2, "WriteByte");
			l.pushJavaFunction(ReadByte);
			l.setField(-2, "ReadByte");
			l.pushJavaFunction(ReadUByte);
			l.setField(-2, "ReadUByte");
			l.pushJavaFunction(WriteChar);
			l.setField(-2, "WriteChar");
			l.pushJavaFunction(ReadChar);
			l.setField(-2, "ReadChar");
			l.pushJavaFunction(WriteDouble);
			l.setField(-2, "WriteDouble");
			l.pushJavaFunction(ReadDouble);
			l.setField(-2, "ReadDouble");
			l.pushJavaFunction(WriteFloat);
			l.setField(-2, "WriteFloat");
			l.pushJavaFunction(ReadFloat);
			l.setField(-2, "ReadFloat");
			l.pushJavaFunction(WriteInt);
			l.setField(-2, "WriteInt");
			l.pushJavaFunction(ReadInt);
			l.setField(-2, "ReadInt");
			l.pushJavaFunction(ReadUInt);
			l.setField(-2, "ReadUInt");
			l.pushJavaFunction(WriteLong);
			l.setField(-2, "WriteLong");
			l.pushJavaFunction(ReadLong);
			l.setField(-2, "ReadLong");
			l.pushJavaFunction(WriteMedium);
			l.setField(-2, "WriteMedium");
			l.pushJavaFunction(ReadMedium);
			l.setField(-2, "ReadMedium");
			l.pushJavaFunction(ReadUMedium);
			l.setField(-2, "ReadUMedium");
			l.pushJavaFunction(WriteShort);
			l.setField(-2, "WriteShort");
			l.pushJavaFunction(ReadShort);
			l.setField(-2, "WriteUShort");
			l.pushJavaFunction(ReadUShort);
			l.setField(-2, "ReadShort");
			l.pushJavaFunction(WriteNBTTag);
			l.setField(-2, "WriteNBTTag");
			l.pushJavaFunction(ReadNBTTag);
			l.setField(-2, "ReadNBTTag");
			l.pushJavaFunction(WriteItemStack);
			l.setField(-2, "WriteItemStack");
			l.pushJavaFunction(ReadItemStack);
			l.setField(-2, "ReadItemStack");
			l.pushJavaFunction(WriteString);
			l.setField(-2, "WriteString");
			l.pushJavaFunction(ReadString);
			l.setField(-2, "ReadString");
			l.pushJavaFunction(WriteData);
			l.setField(-2, "WriteData");
			l.pushJavaFunction(ReadData);
			l.setField(-2, "ReadData");
			l.pushJavaFunction(Seek);
			l.setField(-2, "Seek");
			l.pushJavaFunction(Slice);
			l.setField(-2, "Slice");
			l.pushJavaFunction(Copy);
			l.setField(-2, "Copy");
		}
		l.pop(1);
	}
}
