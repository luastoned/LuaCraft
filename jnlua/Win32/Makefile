# Paths
JDK_DIR=C:\Program Files\Java\jdk1.8.0_201
LUA_DIR=D:\Developer\JitJat\src
VERSION=51

# Tools
CC=cl
LD=link
MT=mt

# Default arguments
CFLAGS=/nologo /c /O2 /GL /W3 /DNDEBUG -DLUA_WIN
LDFLAGS=/nologo /DLL /LTCG
MTFLAGS=-nologo

# Description blocks
all: jnlua$(VERSION).dll

jnlua$(VERSION).dll: jnlua.obj
	$(LD) $(LDFLAGS) /OUT:jnlua$(VERSION).dll /LIBPATH:"$(LUA_DIR)" jnlua.obj lua$(VERSION).lib

jnlua.obj: ..\jnlua.c
	$(CC) $(CFLAGS) /MD /DLUA_BUILD_AS_DLL /I"$(JDK_DIR)\include" /I"$(JDK_DIR)\include\win32" /I"$(LUA_DIR)" ..\jnlua.c

clean:
	-del jnlua$(VERSION).dll jnlua$(VERSION).dll.manifest jnlua$(VERSION).exp jnlua$(VERSION).lib jnlua.obj
