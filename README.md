daperz
======

![Stable](http://i4.photobucket.com/albums/y123/Freaklotr4/stage_stable.png)

A Java library for writing/reading data in a generic way to a store. The library is designed with the idea that the user will be constantly modifying this file (or sections of it) and the library will efficiently load and persistent data between Java objects and the store implementation (memory, file, etc).

**Terminology**
- Data *- a fixed size, specifically located, object that be be read and written to a store*
- Var *- Data that has a single value, i.e. StringVar, IntVar*
- DataArray *- An array of Data elements that are the same type and size*
- DataSet *- A set of Data elements that can be any type and size*

**Features**
- Store implementations include File (uses RandomAccessStream), Memory Mapped File (uses MappedByteBuffer), and Memory (uses ByteBuffer)
- Stores can be set to automatically flush Data when the store is written to.
- Stores can be refreshed, rereading the underlying data and updating the Data objects associated to the Store.

**Documentation**
- [JavaDoc](http://gh.magnos.org/?r=http://clickermonkey.github.com/Daperz/)

**Example**

```java
Store store = new MappedStore("info.dat"); // or FileStore or MemoryStore
store.create(StoreAccess.ReadWrite, 128);

StringVar magic = new StringVar(4, "ABCD");
IntVar version = new IntVar(2);
DataArray<IntVar> array = DataArray.create(IntVar.class, 24, false);
array.set(0, new IntVar(4));
array.set(1, new IntVar(76234));
array.set(2, new IntVar(3434));
 
DataSet data = new DataSet(128);
data.setStore(store);
data.add(version);
data.add(magic);
data.add(array);
data.write();

store.close();
```

**Builds**
- [daperz-1.0.0.jar](http://gh.magnos.org/?r=https://github.com/ClickerMonkey/Daperz/blob/master/build/daperz-1.0.0.jar?raw=true)
- [daperz-src-1.0.0.jar](http://gh.magnos.org/?r=https://github.com/ClickerMonkey/Daperz/blob/master/build/daperz-src-1.0.0.jar?raw=true) *- includes source code*
- [daperz-all-1.0.0.jar](http://gh.magnos.org/?r=https://github.com/ClickerMonkey/Daperz/blob/master/build/daperz-1.0.0.jar?raw=true) *- includes all dependencies*
- [daperz-all-src-1.0.0.jar](http://gh.magnos.org/?r=https://github.com/ClickerMonkey/Daperz/blob/master/build/daperz-src-1.0.0.jar?raw=true) *- includes all dependencies and source code*

**Projects using daperz:**
- [statastic](http://gh.magnos.org/?r=https://github.com/ClickerMonkey/Statastic)

**Dependencies**
- [buffero](http://gh.magnos.org/?r=https://github.com/ClickerMonkey/Buffero)
- [testility](http://gh.magnos.org/?r=https://github.com/ClickerMonkey/Testility) *for unit tests*

**Testing Examples**
- [Testing/org/magnos/data](http://gh.magnos.org/?r=https://github.com/ClickerMonkey/Daperz/tree/master/Testing/org/magnos/data)
