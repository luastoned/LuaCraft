# Paths
JDK_DIR=/run/media/jake/storage/Documents/Developer/Java/jdk1.7.0_79
LUA_INC_DIR=/run/media/jake/storage/Documents/Developer/Java/LuaJIT-2.0.4/src
VERSION=jit
#ARCH=amd64
ARCH=i386

# Tools
CC=gcc
LD=gcc

# Default arguments
CFLAGS=-m32 -c -fno-strict-aliasing -fPIC -O2 -Wall -DNDEBUG -D_REENTRANT -DLUA_USE_LINUX -DLUA_USE_POSIX
LDFLAGS=-m32 -shared

# Description blocks
all: libjnlua$(VERSION).so
	#javavm.so

libjnlua$(VERSION).so: jnlua.o
	$(LD) $(LDFLAGS) -Wl,-soname=libjnlua$(VERSION).so -olibjnlua$(VERSION).so -L$(LUA_INC_DIR) jnlua.o -lc -llua$(VERSION)

jnlua.o: ../jnlua.c
	$(CC) $(CFLAGS) -I$(JDK_DIR)/include -I$(JDK_DIR)/include/linux -I$(LUA_INC_DIR) ../jnlua.c

#javavm.so: javavm.o
#	$(LD) $(LDFLAGS) -Wl,-soname=javavm.so -ojavavm.so -L$(LUA_INC_DIR) -L$(JDK_DIR)/jre/lib/$(ARCH)/server javavm.o -llua$(VERSION) -ljvm

#javavm.o: ../javavm.c ../javavm.h
#	$(CC) $(CFLAGS) -I$(JDK_DIR)/include -I$(JDK_DIR)/include/linux -I$(LUA_INC_DIR) ../javavm.c

clean:
	-rm libjnlua$(VERSION).so jnlua.o
#	-rm javavm.so javavm.o

