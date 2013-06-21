daperz
======

A Java library for writing/reading data in a generic way to a store. The library is designed with the idea that the user will be constantly modifying this file (or sections of it) and the library will efficiently load and persistent data between Java objects and the store implementation (memory, file, etc).

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

https://github.com/ClickerMonkey/daperz/tree/master/build

**Projects using daperz:**
- [statastic](https://github.com/ClickerMonkey/statastic)

**Dependencies**
- [buffero](https://github.com/ClickerMonkey/buffero)
- [testility](https://github.com/ClickerMonkey/testility) *for unit tests*

**Testing Examples**

https://github.com/ClickerMonkey/daperz/tree/master/Testing/org/magnos/data
