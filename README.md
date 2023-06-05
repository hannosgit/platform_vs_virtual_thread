# Source Code for Comparison between Platform Threads and Virtual Threads

This repository contains 3 main programs to compare the two thread types.

## Creation Time

The purpose of this program is to measure the creation time of the given thread type.
The first argument is the number of virtual threads to create per run.
If a second argument is provided, then platform threads are created.
Otherwise, virtual threads are created.

The following example creates 100 virtual threads per run:
```
java --source 20 --enable-preview src/ThreadCreationTime.java 100
```

## Maximum Quantity of Threads

The purpose of this program is to measure the maximum possible quantity of threads before running out of memory.
The first argument is the number of method calls per thread.
If a second argument is provided, then platform threads are created.
Otherwise, virtual threads are created.

The following example creates 1.000 platform threads with 100 method calls per thread:
```
java --source 20 --enable-preview src/MaxPossibleMain.java 100 plt
```

## Memory Usage

The purpose of this program is to measure the memory consumption of the thread types.
The first argument is the number of virtual threads to create and the second argument is the number of the method calls in each thread.
If a third argument is provided, then platform threads are created.
Otherwise, virtual threads are created.

The following example creates 1.000 virtual threads with 10 method calls per thread:
```
java --source 20 --enable-preview src/Memory.java 1000 10
```
